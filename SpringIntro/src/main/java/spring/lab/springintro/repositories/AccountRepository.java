package spring.lab.springintro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.lab.springintro.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}