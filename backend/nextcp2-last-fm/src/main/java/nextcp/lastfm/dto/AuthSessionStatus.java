package nextcp.lastfm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "session" })
public class AuthSessionStatus
{

    public AuthSessionStatus()
    {
    }

    @JsonProperty("session")
    private Session session;

    @JsonProperty("session")
    public Session getSession()
    {
        return session;
    }

    @JsonProperty("session")
    public void setSession(Session session)
    {
        this.session = session;
    }

}