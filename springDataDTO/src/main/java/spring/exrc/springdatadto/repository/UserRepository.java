package spring.exrc.springdatadto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.exrc.springdatadto.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);
}
