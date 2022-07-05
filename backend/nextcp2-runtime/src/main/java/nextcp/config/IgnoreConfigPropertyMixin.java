package nextcp.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import nextcp.dto.UiClientConfig;

public abstract class IgnoreConfigPropertyMixin
{
    // This part will be saven in database JSON Key-Value store.
    @JsonIgnore
    List<UiClientConfig> clientConfig;
}
