package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.log;

import org.apache.catalina.SessionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLSessionBindingListener;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.lang.annotation.Annotation;

@Component
public class LoginTimeoutListener implements HttpSessionListener {

    private static final Logger logger = LoggerFactory.getLogger(LoginTimeoutListener.class);

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        //TODO FIX ME TO LOG USER & IP OF EXPIRED SESSION
        //logger.info("[LOGIN EXPIRED] USERNAME: "+ event.getSession().toString()+ " | IP: "+LogHelper.getUserIpAddress());
    }
}
