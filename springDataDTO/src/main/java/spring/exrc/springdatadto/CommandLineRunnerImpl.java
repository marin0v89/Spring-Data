package spring.exrc.springdatadto;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import spring.exrc.springdatadto.model.dto.UserLoginDto;
import spring.exrc.springdatadto.model.dto.UserRegisterDto;
import spring.exrc.springdatadto.service.UserService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final BufferedReader bufferedReader;
    private final UserService userService;

    public CommandLineRunnerImpl(UserService userService) {
        this.userService = userService;
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Please enter commands :");

        String[] inputCommands = bufferedReader.readLine().split("\\|");

        switch (inputCommands[0]) {
            case "RegisterUser" -> userService
                    .registerUser(new UserRegisterDto(
                            inputCommands[1], inputCommands[2],
                            inputCommands[3], inputCommands[4]));
            case "LoginUser" -> userService.loginUser
                    (new UserLoginDto(inputCommands[1],inputCommands[2]));
            case "Logout" -> userService.logoutUser();
        }

    }
}
