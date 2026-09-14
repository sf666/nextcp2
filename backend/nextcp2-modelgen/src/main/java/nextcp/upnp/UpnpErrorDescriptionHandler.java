package nextcp.upnp;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Pulls the human readable part out of a UPnP SOAP fault body.
 */
public class UpnpErrorDescriptionHandler {

	private static final Logger log = LoggerFactory.getLogger(UpnpErrorDescriptionHandler.class.getName());

	private static final String XPATH_ERROR_DESCRIPTION = "//*/errorDescription";
	private static final String XPATH_ERROR_CODE = "//*/errorCode";

	/**
	 * Standard UPnP wording that prefixes a device's own message. Stripped so the
	 * caller sees what the device actually said.
	 */
	private final static String PRE_TEXT = "Current state of service prevents invoking that action.";

	private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private final XPathFactory xpathfactory = XPathFactory.newInstance();

	public UpnpErrorDescriptionHandler() {
		factory.setNamespaceAware(false);
		// The body comes off the network from a device we do not control, so no
		// external entities and no doctype.
		try {
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
			factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			factory.setXIncludeAware(false);
			factory.setExpandEntityReferences(false);
		} catch (ParserConfigurationException e) {
			log.warn("cannot harden XML reader", e);
		}
	}

	/**
	 * @param description SOAP fault body, may be null, already-extracted text, or not XML at all
	 * @return the device's error text, or an empty string if none could be read
	 */
	public String extractErrorText(String description) {
		if (description == null || description.isBlank()) {
			return "";
		}
		if (!looksLikeFaultBody(description)) {
			// Someone handed us a reason that had already been pulled out of a fault. Parsing that as
			// XML threw, and the caller was left with an empty message - which is how "entry already
			// in Playlist." never reached anyone. Idempotent instead: a reason passed in is a reason
			// passed back, so it no longer matters how often this runs on the way to the toast.
			return stripStandardPrefix(description.trim());
		}
		try {
			// DocumentBuilder and XPath are not thread safe and this handler is
			// shared per device, so both are built per call rather than reused.
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
			XPath xpath = xpathfactory.newXPath();
			XPathExpression expr = xpath.compile(XPATH_ERROR_DESCRIPTION);

			Document doc = builder.parse(new InputSource(new StringReader(description)));
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				String text = node == null ? null : node.getTextContent();
				if (text == null || text.isBlank()) {
					continue;
				}
				// Report the device's message whether or not it carries the
				// standard prefix. Returning nothing for an unexpected wording
				// left the caller with an empty error.
				return stripStandardPrefix(text.trim());
			}
		} catch (SAXException | IOException | XPathExpressionException | ParserConfigurationException e) {
			log.warn("cannot extract error message", e);
		}
		return "";
	}

	/**
	 * @param description SOAP fault body, may be null or not XML at all
	 * @return the UPnP error code, or an empty string if none could be read
	 */
	public String extractErrorCode(String description) {
		return firstText(description, XPATH_ERROR_CODE);
	}

	private static final String SEPARATOR = " : ";

	/** A fault body starts with its XML declaration or an element; a reason pulled out of one does not. */
	private static boolean looksLikeFaultBody(String candidate) {
		return candidate.stripLeading().startsWith("<");
	}

	private static String stripStandardPrefix(String text) {
		return text.startsWith(PRE_TEXT) ? text.substring(PRE_TEXT.length()).trim() : text;
	}

	private String firstText(String description, String xpathExpression) {
		if (description == null || description.isBlank() || !looksLikeFaultBody(description)) {
			return "";
		}
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
			XPathExpression expr = xpathfactory.newXPath().compile(xpathExpression);
			NodeList nodes = (NodeList) expr.evaluate(builder.parse(new InputSource(new StringReader(description))),
					XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				String text = node == null ? null : node.getTextContent();
				if (text != null && !text.isBlank()) {
					return text.trim();
				}
			}
		} catch (SAXException | IOException | XPathExpressionException | ParserConfigurationException e) {
			log.warn("cannot extract {}", xpathExpression, e);
		}
		return "";
	}

	/**
	 * The device's own words about a failed action, short enough to show a user: the error code and
	 * the description, without the SOAP envelope they arrived in.
	 *
	 * @return "501 : the device's message", one of the two when only one could be read, or an empty
	 *         string when the body held neither
	 */
	public String summarize(String faultBody) {
		String code = extractErrorCode(faultBody);
		String text = extractErrorText(faultBody);
		// Already summarized once : do not put the code in front of it a second time.
		if (!code.isBlank() && text.startsWith(code + SEPARATOR)) {
			return text;
		}
		if (code.isBlank()) {
			return text;
		}
		return text.isBlank() ? code : code + SEPARATOR + text;
	}

}
