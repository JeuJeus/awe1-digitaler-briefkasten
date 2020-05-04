package de.fhdw.geiletypengmbh.digitalerbriefkasten.service.account;

public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
