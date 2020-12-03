package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class InputSourceDto
{

    public Integer id;
    public String Name;
    public String Type;
    public Boolean Visible;

    public InputSourceDto()
    {
    }

    public InputSourceDto(Integer id, String Name, String Type, Boolean Visible)
    {
        this.id = id;
        this.Name = Name;
        this.Type = Type;
        this.Visible = Visible;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("InputSourceDto [");
        sb.append("id=").append(this.id).append(", ");
        sb.append("Name=").append(this.Name).append(", ");
        sb.append("Type=").append(this.Type).append(", ");
        sb.append("Visible=").append(this.Visible).append(", ");
        sb.append("]");
        return sb.toString();
    }

}