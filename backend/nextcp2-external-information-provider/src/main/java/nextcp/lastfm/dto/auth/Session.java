package nextcp.lastfm.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "subscriber", "name", "key" })
public class Session
{

    @JsonProperty("subscriber")
    private Integer subscriber;
    @JsonProperty("name")
    private String name;
    @JsonProperty("key")
    private String key;

    @JsonProperty("subscriber")
    public Integer getSubscriber()
    {
        return subscriber;
    }

    @JsonProperty("subscriber")
    public void setSubscriber(Integer subscriber)
    {
        this.subscriber = subscriber;
    }

    @JsonProperty("name")
    public String getName()
    {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name)
    {
        this.name = name;
    }

    @JsonProperty("key")
    public String getKey()
    {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key)
    {
        this.key = key;
    }

}