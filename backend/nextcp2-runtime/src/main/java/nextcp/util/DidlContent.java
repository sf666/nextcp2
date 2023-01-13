package nextcp.util;

import org.jupnp.support.contentdirectory.DIDLParser;
import org.jupnp.support.model.DIDLContent;

public class DidlContent
{
    public DIDLContent generateDidlContent(String didlContentXml) throws Exception
    {
        DIDLParser didlParser = new DIDLParser();
        DIDLContent didl = didlParser.parse(didlContentXml);
        return didl;
    }
}
