package nextcp.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import nextcp.util.DIDLParser;
import org.jupnp.support.model.DIDLContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DidlContent
{
    private static final Logger log = LoggerFactory.getLogger(DidlContent.class.getName());

    public DIDLContent generateDidlContent(String didlContentXml) throws Exception
    {
        try
        {
            if (StringUtils.isAllBlank(didlContentXml))
            {
                log.warn("DIDL is NULL or empty.");
                return null;
            }

            DIDLParser didlParser = new DIDLParser();
            String normalizedXml;
            if (didlContentXml.contains("&lt;"))
            {
                log.warn("DIDL content contains unescaped XML content !");
                normalizedXml = StringEscapeUtils.unescapeXml(didlContentXml);
            }
            else
            {
                log.debug("DIDL content has regular XML.");
                normalizedXml = didlContentXml;
            }

            DIDLContent didl = didlParser.parse(normalizedXml);
            return didl;
        }
        catch (Exception e)
        {
            log.warn(String.format("DIDL Content is : %s", didlContentXml));
            log.warn("error parsing xml", e);
            return null;
        }
    }
}
