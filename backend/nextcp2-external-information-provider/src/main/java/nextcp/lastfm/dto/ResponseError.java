package nextcp.lastfm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "error", "message" })
public class ResponseError
{

    @JsonProperty("error")
    private Integer error;
    @JsonProperty("message")
    private String message;

    @JsonProperty("error")
    public Integer getError()
    {
        return error;
    }

    @JsonProperty("error")
    public void setError(Integer error)
    {
        this.error = error;
    }

    @JsonProperty("message")
    public String getMessage()
    {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message)
    {
        this.message = message;
    }

}