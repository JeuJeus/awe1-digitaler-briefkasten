//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private static final Logger logger = LoggerFactory.getLogger(LoginFailureListener.class);

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        logger.warn(String.format("[LOGIN FAILURE] USERNAME: %s | IP: %s", event.getAuthentication().getName(), LogHelper.getUserIpAddress()));
    }
}
