package erxs.spring.springintroexercise.service;

import erxs.spring.springintroexercise.constants.Constants;
import erxs.spring.springintroexercise.models.entity.Author;
import erxs.spring.springintroexercise.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void seedAuthors() throws IOException {

        if (authorRepository.count() > 0) {
            return;
        }

        Files
                .readAllLines(Path.of(Constants.AUTHORS_FILE_PATH))
                .forEach(row -> {
                    String[] firstName = row.split("\\s+");
                    String[] lastName = row.split("\\s+");
                    var author = new Author(firstName[0], lastName[1]);
                    authorRepository.save(author);
                });


    }
}
