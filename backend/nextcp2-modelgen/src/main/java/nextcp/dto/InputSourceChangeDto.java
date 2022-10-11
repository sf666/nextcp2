package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class InputSourceChangeDto
{

    public String udn;
    public InputSourceDto inputSource;

    public InputSourceChangeDto()
    {
    }

    public InputSourceChangeDto(String udn, InputSourceDto inputSource)
    {
        this.udn = udn;
        this.inputSource = inputSource;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("InputSourceChangeDto [");
        sb.append("udn=").append(this.udn).append(", ");
        sb.append("inputSource=").append(this.inputSource).append(", ");
        sb.append("]");
        return sb.toString();
    }

}