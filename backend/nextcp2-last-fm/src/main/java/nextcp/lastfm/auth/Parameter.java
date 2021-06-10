package nextcp.lastfm.auth;

public class Parameter implements Comparable<Parameter>
{
    public String name = "";
    public String value = "";

    public Parameter()
    {
    }

    public Parameter(String name, String value)
    {
        super();
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj)
    {
        return name.equals(obj.toString());
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public int compareTo(Parameter o)
    {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString()
    {
        return String.format("%s%s", name, value);
    }

    public String getAsString()
    {
        return toString();
    }
}
