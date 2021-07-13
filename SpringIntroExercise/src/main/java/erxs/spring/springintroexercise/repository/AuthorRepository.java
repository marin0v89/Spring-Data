package erxs.spring.springintroexercise.repository;

import erxs.spring.springintroexercise.models.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
