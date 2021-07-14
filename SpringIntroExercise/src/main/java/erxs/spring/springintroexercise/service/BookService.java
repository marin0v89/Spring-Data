package erxs.spring.springintroexercise.service;

import erxs.spring.springintroexercise.models.entity.Author;
import erxs.spring.springintroexercise.models.entity.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsAfterYear(int year);

    List<String> findAllBooksByAuthor(String firstName,String lastName);


}
