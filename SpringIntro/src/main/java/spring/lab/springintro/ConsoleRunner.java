package spring.lab.springintro;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import spring.lab.springintro.services.UserService;

@Service
public class ConsoleRunner implements CommandLineRunner {

    private final UserService userService;

    public ConsoleRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
