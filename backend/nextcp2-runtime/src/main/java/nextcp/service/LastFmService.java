package nextcp.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import nextcp.dto.Config;
import nextcp.dto.ToastrMessage;
import nextcp.lastfm.dto.AuthSessionStatus;
import nextcp.lastfm.service.LastFmAuthService;

@Service
public class LastFmService
{
    @Autowired
    private Config config = null;

    @Autowired
    private ConfigService confService = null;

    @Autowired
    private LastFmAuthService lastFmAuth = null;

    @Autowired
    private ApplicationEventPublisher publisher = null;

    public LastFmService()
    {
    }

    public String getLastFmAppRegistrationPath()
    {
        return lastFmAuth.getAppRegistrationUrl();
    }

    public AuthSessionStatus createLastFmSession()
    {
        AuthSessionStatus session = lastFmAuth.createSession();

        if (session != null && session.getSession() != null && !StringUtils.isBlank(session.getSession().getKey()))
        {
            config.lastFmSessionKey = session.getSession().getKey();
            confService.writeAndSendConfig();
            this.publisher.publishEvent(new ToastrMessage("", "info", "LastFM", "successfully connected to LastFM"));
        }

        return session;
    }
}
