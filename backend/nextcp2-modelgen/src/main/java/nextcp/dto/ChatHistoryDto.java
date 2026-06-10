package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ChatHistoryDto
{

    public List<ChatMessageDto> messages;
    public Integer max;

    public ChatHistoryDto()
    {
    }

    public ChatHistoryDto(List<ChatMessageDto> messages, Integer max)
    {
        this.messages = messages;
        this.max = max;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ChatHistoryDto [");
        sb.append("messages=").append(this.messages).append(", ");
        sb.append("max=").append(this.max).append(", ");
        sb.append("]");
        return sb.toString();
    }

}