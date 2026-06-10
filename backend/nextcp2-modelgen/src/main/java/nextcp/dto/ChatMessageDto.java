package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ChatMessageDto
{

    public Long id;
    public String role;
    public String content;
    public String status;
    public Long timestamp;

    public ChatMessageDto()
    {
    }

    public ChatMessageDto(Long id, String role, String content, String status, Long timestamp)
    {
        this.id = id;
        this.role = role;
        this.content = content;
        this.status = status;
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ChatMessageDto [");
        sb.append("id=").append(this.id).append(", ");
        sb.append("role=").append(this.role).append(", ");
        sb.append("content=").append(this.content).append(", ");
        sb.append("status=").append(this.status).append(", ");
        sb.append("timestamp=").append(this.timestamp).append(", ");
        sb.append("]");
        return sb.toString();
    }

}