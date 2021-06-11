package nextcp.lastfm.auth;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import nextcp.SpringLastFMTestConfiguration;
import nextcp.lastfm.ILastFmConfig;
import nextcp.lastfm.LastFmConfig;
import nextcp.lastfm.service.LastFmAuthService;

@SpringBootTest
@Configuration
@ContextConfiguration(classes = SpringLastFMTestConfiguration.class)
@ComponentScan(
{ "nextcp.lastfm" })
public class TestAuth
{

    @Autowired
    private ILastFmConfig config = null;

    @Autowired
    private LastFmAuthService authenticator = null;

    public TestAuth()
    {
    }

    @Test
    public void doAuthentication()
    {
        assertTrue(authenticator.getUserGrantingAuthToken().getToken().length() > 0);
    }

    @Bean
    public ILastFmConfig getConfig()
    {
        return new LastFmConfig();
    }
}
