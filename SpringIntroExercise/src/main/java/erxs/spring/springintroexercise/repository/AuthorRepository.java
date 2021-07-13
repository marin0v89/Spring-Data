package erxs.spring.springintroexercise.repository;

import erxs.spring.springintroexercise.models.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
