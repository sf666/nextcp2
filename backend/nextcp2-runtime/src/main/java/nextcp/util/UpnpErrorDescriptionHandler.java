package nextcp.util;

import java.io.IOException;
import java.io.StringReader;
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
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class UpnpErrorDescriptionHandler {

	private static final Logger log = LoggerFactory.getLogger(UpnpErrorDescriptionHandler.class.getName());
	
	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder builder = null;
	private XPathFactory xpathfactory = XPathFactory.newInstance();
	private XPath xpath = xpathfactory.newXPath();
	private XPathExpression expr = null;
	
	private final static String PRE_TEXT = "Current state of service prevents invoking that action.";

	
	public UpnpErrorDescriptionHandler() {
		factory.setNamespaceAware(false);
		try {
			builder = factory.newDocumentBuilder();
			expr = xpath.compile("//*/errorDescription/text()");
		} catch (ParserConfigurationException | XPathExpressionException e) {
			log.error("cannot build XML reader", e);
		}
	}
	
	public String extractErrorText(String description) {
		InputSource is = new InputSource(new StringReader(description));
		try {
			Document doc = builder.parse(is);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;

			for (int i = 0; i < nodes.getLength();) {
				String text = nodes.item(i).getNodeValue();
				if (text.startsWith(PRE_TEXT)) {
					text = text.substring(PRE_TEXT.length());
					text = text.trim();
					return text;
				}
				
			}
		} catch (SAXException | IOException | XPathExpressionException e) {
			log.warn("cannot extract error message", e);
		}
		return "";
	}	

}
