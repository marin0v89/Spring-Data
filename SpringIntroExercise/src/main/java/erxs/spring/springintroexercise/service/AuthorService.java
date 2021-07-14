package erxs.spring.springintroexercise.service;

import erxs.spring.springintroexercise.models.entity.Author;

import java.io.IOException;
import java.util.List;

public interface AuthorService {
    void seedAuthors() throws IOException;

    Author getRandomAuthor();

    List<String> getAllAuthorsByBooks();
}
