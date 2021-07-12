package spring.lab.springintro.services;

import java.math.BigDecimal;

public interface UserService {
    void register(String username,int age, BigDecimal amount);
}
