//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
public class LoginTimeoutListener implements HttpSessionListener {

    private static final Logger logger = LoggerFactory.getLogger(LoginTimeoutListener.class);

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        //TODO FIX ME TO LOG USER & IP OF EXPIRED SESSION
        //logger.info("[LOGIN EXPIRED] USERNAME: "+ event.getSession().toString()+ " | IP: "+LogHelper.getUserIpAddress());
    }
}
