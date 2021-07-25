package json.exr.json.repository;

import json.exr.json.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u " +
            "WHERE (SELECT COUNT(p)FROM Products p WHERE p.seller.id = u.id)>0 " +
            "ORDER BY u.lastName, u.firstName")
    List<Users> findAllUsersWithMoreThanOneSoldProduct();
}
