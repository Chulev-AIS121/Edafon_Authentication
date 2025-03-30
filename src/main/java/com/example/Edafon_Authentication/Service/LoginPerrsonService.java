package com.example.Edafon_Authentication.Service;


import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component //  С помощью стереотипной аннотации сообщаю Spring, что это бин
@RequestScope //  С помощью аннотации @RequestScope ограничиваем область видимости
// бина текущим запросом. Теперь Spring будет создавать новый экземпляр класса для каждого HTTP-запроса
public class LoginPerrsonService {


    private final LoggedUserManagerService loggedUserManagerService;
    private final LoginCountService loginCountService;

    public LoginPerrsonService(LoggedUserManagerService loggedUserManagerService,
                               LoginCountService loginCountService)
    {
        this.loggedUserManagerService = loggedUserManagerService;
        this.loginCountService = loginCountService;
    }

    private String username;
    private String password;

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
    public void setPassword( String password)
    {
        this.password = password;
    }

    public boolean login()
    {
        loginCountService.increment();

        String username = this.getUsername();
        String password = this.getPassword();
        if("Ivan".equals(username) && "pasword".equals(password))
        {
            loggedUserManagerService.setUsername(username);
            return true;
        } else
        {
            return  false;
        }

    }
}
