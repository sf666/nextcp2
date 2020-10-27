package codegen;

import org.fourthline.cling.model.meta.ActionArgument;
import org.fourthline.cling.model.meta.StateVariable;
import org.fourthline.cling.model.types.Base64Datatype;
import org.fourthline.cling.model.types.BooleanDatatype;
import org.fourthline.cling.model.types.Datatype;
import org.fourthline.cling.model.types.Datatype.Builtin;
import org.fourthline.cling.model.types.IntegerDatatype;
import org.fourthline.cling.model.types.StringDatatype;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytesDatatype;
import org.fourthline.cling.model.types.UnsignedIntegerOneByteDatatype;
import org.fourthline.cling.model.types.UnsignedIntegerTwoBytesDatatype;

public class Variable
{
    private String name;
    private String type;
    private String upnpType;
    private Datatype<?> upnpDataType;

    public Variable()
    {
    }

    public Variable(ActionArgument<?> argument)
    {
        this.name = argument.getName();
        this.upnpDataType = argument.getDatatype();
        initTypes();
    }

    public Variable(StateVariable<?> argument)
    {
        this.name = argument.getName();
        this.upnpDataType = argument.getTypeDetails().getDatatype();
        initTypes();
    }

    private void initTypes()
    {
        this.type = getJavaType(upnpDataType.getBuiltin());
        this.upnpType = upnpDataType.getBuiltin().getDescriptorName().toLowerCase();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Datatype<?> getUpnpDataType()
    {
        return upnpDataType;
    }

    public String getUpnpType()
    {
        return upnpType;
    }

    public void setUpnpType(String upnpType)
    {
        this.upnpType = upnpType;
    }

    public void setUpnpDataType(Datatype<?> upnpDataType)
    {
        this.upnpDataType = upnpDataType;
    }

    protected String getJavaType(Builtin builtin)
    {
        if (builtin.getDatatype() instanceof IntegerDatatype)
        {
            return "Integer";
        }
        else if (builtin.getDatatype() instanceof StringDatatype)
        {
            return "String";
        }
        else if (builtin.getDatatype() instanceof BooleanDatatype)
        {
            return "Boolean";
        }
        else if (builtin.getDatatype() instanceof Base64Datatype)
        {
            return "byte[]";
        }
        else if (builtin.getDatatype() instanceof UnsignedIntegerFourBytesDatatype)
        {
            return "Long";
        }
        else if (builtin.getDatatype() instanceof UnsignedIntegerTwoBytesDatatype)
        {
            return "Long";
        }
        else if (builtin.getDatatype() instanceof UnsignedIntegerOneByteDatatype)
        {
            return "Long";
        }

        throw new RuntimeException("Unknown Datatype : " + builtin.getDatatype().toString());
    }

}
