package json.exr.json.service.impl;

import com.google.gson.Gson;
import json.exr.json.constants.RootPath;
import json.exr.json.model.dto.UserSeedDto;
import json.exr.json.model.entity.Users;
import json.exr.json.repository.UserRepository;
import json.exr.json.service.UserService;
import json.exr.json.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static json.exr.json.constants.RootPath.*;

@Service
public class UserServiceImpl implements UserService {

    private static final String USERS_FILE = "users.json";

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public void seedUsers() throws IOException {

        if (userRepository.count() == 0) {
            Arrays.stream(gson.fromJson(
                    Files.readString(Path.of(RESOURCE_DIRECTORY + USERS_FILE)),
                    UserSeedDto[].class))
                    .filter(validationUtil::isValid)
                    .map(userSeedDto -> modelMapper.map(userSeedDto, Users.class))
                    .forEach(userRepository::save);
        }
    }

    @Override
    public Users findRandomUser() {
        long randomId = ThreadLocalRandom
                .current().nextLong(1, userRepository.count()) + 1;
        return userRepository
                .findById(randomId)
                .orElse(null);
    }
}
