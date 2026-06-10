package codegen.java;

public class FieldSpecification
{
    private String name;
    private String type;
    private boolean optional;

    public FieldSpecification(String fieldName, String fieldType)
    {
        this.name = fieldName;

        String t = fieldType == null ? null : fieldType.trim();
        if (t != null && t.endsWith("?"))
        {
            // a trailing '?' in dto.yaml (e.g. "Long?") marks the field as nullable
            this.optional = true;
            this.type = t.substring(0, t.length() - 1).trim();
        }
        else
        {
            this.optional = false;
            this.type = fieldType;
        }
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public boolean isOptional()
    {
        return optional;
    }
}