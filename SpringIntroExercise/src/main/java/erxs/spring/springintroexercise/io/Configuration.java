package erxs.spring.springintroexercise.io;

import org.springframework.context.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public BufferedReader bufferedReader() {
        return new BufferedReader(new InputStreamReader(System.in));
    }
}
