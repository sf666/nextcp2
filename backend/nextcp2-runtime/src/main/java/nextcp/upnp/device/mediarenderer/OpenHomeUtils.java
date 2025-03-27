package nextcp.upnp.device.mediarenderer;

import java.io.StringReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import nextcp.dto.InputSourceDto;
import nextcp.dto.MusicItemDto;
import nextcp.rest.DtoBuilder;

public class OpenHomeUtils
{
    private static final Logger log = LoggerFactory.getLogger(OpenHomeUtils.class.getName());

    private DtoBuilder dtoBuilder = null;
    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder builder = null;

    public OpenHomeUtils(DtoBuilder dtoBuilder)
    {

        factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try
        {
            builder = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            throw new RuntimeException("Could not create DocumentBuilder.");
        }

        this.dtoBuilder = dtoBuilder;
    }

    /**
     * 
     * @param byteArray
     *            bytestream of unsigned big-endian encoded 32-bit numbers ...
     * @return uint 32 bit numbers as Long, skipping 0.
     */
    public LinkedList<Long> convertUintByteArrayToLong(byte[] byteArray)
    {
        LinkedList<Long> playlistIds = new LinkedList<>();
        if (byteArray != null && byteArray.length > 0)
        {
            ByteBuffer bb = ByteBuffer.wrap(byteArray);
            while (bb.hasRemaining())
            {
                Long id = 0xFFFFFFFFL & bb.getInt();
                playlistIds.addLast(id);
            }
        }

        return playlistIds;
    }

    public String makeStringList(LinkedList<Long> ids)
    {
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (Long id : ids)
        {
            if (first)
            {
                first = false;
            }
            else
            {
                sb.append(" ");
            }
            if (id > 0)
            {
                sb.append(Long.toString(id));
            }

        }
        return sb.toString();
    }

    public String convertUintByteArrayToStringList(byte[] ba)
    {
        return makeStringList(convertUintByteArrayToLong(ba));
    }

    public List<MusicItemDto> convertToMediaItemDto(String xml, String rootNode)
    {
    	log.info("radio station root node : " + rootNode);
//    	log.info("radio station xml : " + xml);
        List<MusicItemDto> result = new ArrayList<>();
        try
        {
            XPath xpath = XPathFactory.newInstance().newXPath();

            String unescapedXml = StringEscapeUtils.unescapeXml(xml);
            log.debug("radio unescaped XML : " + unescapedXml);

            InputSource is = new InputSource(new StringReader(xml));
            XPathExpression expr = xpath.compile("//" + rootNode + "/Entry");
            NodeList nodeList = (NodeList) expr.evaluate(is, XPathConstants.NODESET);
            
            log.info("Number of radio stations : " + nodeList.getLength());
            log.info("Number of radio stations : ");
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                log.info("Node {}", i);
                Node nNode = nodeList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    try
                    {
                        Element eElement = (Element) nNode;
                        String meta = extractElementTextByTag(eElement, "Metadata");
                        MusicItemDto dto = dtoBuilder.extractXmlAsMusicItem(meta);
                        if (dto != null)
                        {
                            dto.objectID = extractElementTextByTag(eElement, "Id");
                            dto.streamingURL = extractElementTextByTag(eElement, "Uri");
                            result.add(dto);
                            log.info("added : {}", dto.toString());
                        }
                    }
                    catch (Exception e)
                    {
                        log.warn("cannot create MediaItem. XML : " + xml, e);
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.warn("cannot create MediaItem list. XML : " + xml, e);
        }

        return result;
    }

    private String extractElementTextByTag(Element eElement, String tag)
    {
        try
        {
            return eElement.getElementsByTagName(tag).item(0).getTextContent();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public LinkedList<InputSourceDto> convertToInputSourceList(String xml)
    {
        LinkedList<InputSourceDto> result = new LinkedList<>();

        try
        {
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPath xPathSearch = XPathFactory.newInstance().newXPath();

            InputSource is = new InputSource(new StringReader(xml));
            String expression = "/SourceList/Source";
            NodeList nodes = (NodeList) xpath.evaluate(expression, is, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
            {
                Node entry = nodes.item(i);
                InputSourceDto dto = new InputSourceDto();
                dto.id = i;
                dto.Name = xPathSearch.evaluate("Name", entry);
                dto.Type = xPathSearch.evaluate("Type", entry);
                dto.Visible = Boolean.parseBoolean(xPathSearch.evaluate("Visible", entry));
                result.addLast(dto);
            }
        }
        catch (XPathExpressionException e)
        {
            log.warn("cannot create InputSource", e);
        }

        return result;
    }
}
