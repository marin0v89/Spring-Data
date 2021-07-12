package spring.lab.springintro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.lab.springintro.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
