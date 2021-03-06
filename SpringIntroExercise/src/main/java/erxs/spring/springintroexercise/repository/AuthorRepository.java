package erxs.spring.springintroexercise.repository;

import erxs.spring.springintroexercise.models.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a ORDER BY size(a.books) DESC")
    List<Author> findAllBooksDesc();

    List<Author>findAllByFirstNameEndingWith(String endWith);
}
