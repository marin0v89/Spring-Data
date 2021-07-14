package erxs.spring.springintroexercise.repository;

import erxs.spring.springintroexercise.models.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface BookRepository extends JpaRepository<Book, Long> {
}
