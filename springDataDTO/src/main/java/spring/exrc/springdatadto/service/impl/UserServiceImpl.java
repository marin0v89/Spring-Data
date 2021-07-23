package spring.exrc.springdatadto.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import spring.exrc.springdatadto.model.dto.UserLoginDto;
import spring.exrc.springdatadto.model.dto.UserRegisterDto;
import spring.exrc.springdatadto.model.entity.User;
import spring.exrc.springdatadto.repository.UserRepository;
import spring.exrc.springdatadto.service.UserService;
import spring.exrc.springdatadto.util.ValidationUtil;

import javax.validation.ConstraintViolation;
import java.util.Set;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private User loggedInUser;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void registerUser(UserRegisterDto userRegisterDto) {
        if (userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            System.out.println("Wrong password");
            return;
        }

        Set<ConstraintViolation<UserRegisterDto>> violation =
                validationUtil.violation(userRegisterDto);

        if (!violation.isEmpty()) {
            violation
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return;
        }
        var user = modelMapper.map(userRegisterDto, User.class);
        if (this.userRepository.findAll().isEmpty()) {
            user.setAdmin(true);
        }
        try {
            userRepository.save(user);
        } catch (Throwable e) {
            System.out.println("User is already registered.");
        }
        System.out.println(user.getFullName() + " was registered");
    }

    @Override
    public void loginUser(UserLoginDto userLoginDto) {
        if (loggedInUser != null) {
            System.out.println("Another user is logged in");
        }
        Set<ConstraintViolation<UserLoginDto>> violation =
                validationUtil.violation(userLoginDto);

        if (!violation.isEmpty()) {
            violation
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            return;
        }
        var user = userRepository
                .findByEmailAndPassword(userLoginDto.getEmail(), userLoginDto.getPassword());

        if (user == null) {
            System.out.println("Incorrect username / password");
            return;
        }

        loggedInUser = user;
        System.out.println("Successfully logged in " + user.getFullName());
    }

    @Override
    public void logoutUser() {
        if (loggedInUser == null) {
            System.out.println("Cannot log out. No user was logged in.");
            return;
        }


        loggedInUser = null;
        System.out.println("User %s successfully logged out "+ loggedInUser.getFullName());

    }
}
