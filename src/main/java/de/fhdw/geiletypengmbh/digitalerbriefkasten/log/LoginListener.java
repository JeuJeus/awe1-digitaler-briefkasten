//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class LoginListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private static final Logger logger = LoggerFactory.getLogger(LoginListener.class);

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        UserDetails user = (UserDetails) event.getAuthentication().getPrincipal();
        logger.info(String.format("[LOGIN] USERNAME: %s | IP: %s", user.getUsername(), LogHelper.getUserIpAddress()));
    }

}
