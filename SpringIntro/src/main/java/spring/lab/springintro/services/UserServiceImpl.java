package spring.lab.springintro.services;

import org.springframework.stereotype.Service;
import spring.lab.springintro.models.Account;
import spring.lab.springintro.models.User;
import spring.lab.springintro.repositories.AccountRepository;
import spring.lab.springintro.repositories.UserRepository;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    public UserServiceImpl(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void register(String username, int age, BigDecimal amount) {
        var user = new User();
        user.setUsername(username);
        user.setAge(age);
        this.userRepository.save(user);

        var firstAccount = new Account();
        firstAccount.setBalance(amount);

        firstAccount.setUser(user);

        this.accountRepository.save(firstAccount);
    }
}
