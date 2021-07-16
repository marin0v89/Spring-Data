package erxs.spring.springintroexercise.repository;

import erxs.spring.springintroexercise.models.entity.AgeRestriction;
import erxs.spring.springintroexercise.models.entity.Book;
import erxs.spring.springintroexercise.models.entity.EditionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByReleaseDateAfter(LocalDate releaseDate);

    List<Book> findAllByReleaseDateBefore(LocalDate releaseDate);

    List<Book> findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(String firstName, String lastName);

    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType,Integer copies);

    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal priceOne, BigDecimal priceTwo);

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate lower, LocalDate upper);

    List<Book> findAllByTitleContaining(String contains);

    List<Book> findAllByAuthor_LastNameStartsWith(String author_lastName);

    @Query("SELECT COUNT(b) FROM Book b WHERE length(b.title) > :length ")
    int countOfBooksWithTitleLengthMoreThan(@Param(value = "length") int length);

    List<Book> findBookByTitle(String title);
}
