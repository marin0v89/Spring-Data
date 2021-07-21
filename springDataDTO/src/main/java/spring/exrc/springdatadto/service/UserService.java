package spring.exrc.springdatadto.service;

import org.springframework.stereotype.Service;
import spring.exrc.springdatadto.model.dto.UserLoginDto;
import spring.exrc.springdatadto.model.dto.UserRegisterDto;

@Service
public interface UserService {
    void registerUser(UserRegisterDto userRegisterDto);

    void loginUser(UserLoginDto userLoginDto);

    void logoutUser();
}
