package nextcp.config;

import codegen.ICodegenConfig;

public class CodeGenConfig implements ICodegenConfig
{
    private boolean generateUpnpCode = false;
    private String generateUpnpCodePath = "/tmp";

    public CodeGenConfig()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean isGenerateUpnpCode()
    {
        return generateUpnpCode;
    }

    @Override
    public String getGenerateUpnpCodePath()
    {
        return generateUpnpCodePath;
    }

    public void setGenerateUpnpCode(boolean generateUpnpCode)
    {
        this.generateUpnpCode = generateUpnpCode;
    }

    public void setGenerateUpnpCodePath(String generateUpnpCodePath)
    {
        this.generateUpnpCodePath = generateUpnpCodePath;
    }

}
