package devicedriver.McIntosh.control;

public class CommandStructure extends BaseCommandStructure
{

    public CommandStructure(String string)
    {
        super(string);
    }

    public CommandStructure(String string, int i)
    {
        super(string, i);
    }

    @Override
    public boolean hasReturnValue()
    {
        return false;
    }

}
