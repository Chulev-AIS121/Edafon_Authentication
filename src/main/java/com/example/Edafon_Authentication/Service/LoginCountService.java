package com.example.Edafon_Authentication.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

@Service
@ApplicationScope
//  Аннотация @ApplicationScope распространяет область видимости бина на всё приложение
public class LoginCountService {
    private int count;

    public void increment()
    {
        count++;
    }
    public int getCount()
    {
        return count;
    }
}
