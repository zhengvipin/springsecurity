package com.springsecurity;

import io.jsonwebtoken.Clock;
import io.jsonwebtoken.impl.DefaultClock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Test
    public void timeDemo() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date aDate = dateFormat.parse("2018-05-13 19:00:00");
        System.out.println(aDate.before(new Date()));

        Clock clock = DefaultClock.INSTANCE;
        System.out.println(aDate.before(clock.now()));
    }

    @Test
    public void getPassword(){
        System.out.println(passwordEncoder.encode("admin"));
    }
}
