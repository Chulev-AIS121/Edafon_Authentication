package com.example.Edafon_Authentication.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope // С помощью аннотации @SessionScope меняю область видимости бина на видимость в рамках сессии
public class LoggedUserManagerService {

    private String username;

    public String getUsername()
    {
        return username;
    }
    public void setUsername (String username)
    {
        this.username = username;
    }

    public boolean loginManager()
    {
        return username != null && !username.isEmpty();
    }
}
