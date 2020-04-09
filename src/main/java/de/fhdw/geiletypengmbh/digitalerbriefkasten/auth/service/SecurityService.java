package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
