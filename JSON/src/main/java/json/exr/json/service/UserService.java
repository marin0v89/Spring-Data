package json.exr.json.service;

import json.exr.json.model.entity.Users;

import java.io.IOException;

public interface UserService {
    void seedUsers() throws IOException;

    Users findRandomUser();
}
