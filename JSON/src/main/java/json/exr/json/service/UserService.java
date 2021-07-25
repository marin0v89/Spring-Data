package json.exr.json.service;

import json.exr.json.model.dto.UserSoldDto;
import json.exr.json.model.entity.Users;

import java.io.IOException;
import java.util.List;

public interface UserService {
    void seedUsers() throws IOException;

    Users findRandomUser();

    List<UserSoldDto> findAllUsersWithMoreThanOneSell();
}
