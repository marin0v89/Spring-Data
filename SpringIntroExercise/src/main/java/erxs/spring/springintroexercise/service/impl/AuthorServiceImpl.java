package erxs.spring.springintroexercise.service.impl;

import erxs.spring.springintroexercise.constants.Constants;
import erxs.spring.springintroexercise.models.entity.Author;
import erxs.spring.springintroexercise.repository.AuthorRepository;
import erxs.spring.springintroexercise.service.AuthorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

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
                    String[] fullName = row.split("\\s+");
                    var author = new Author(fullName[0], fullName[1]);
                    authorRepository.save(author);
                });


    }

    @Override
    public Author getRandomAuthor() {
        long randomAuthor =
                ThreadLocalRandom
                        .current()
                        .nextLong(1, authorRepository.count() + 1);

        return authorRepository
                .findById(randomAuthor)
                .orElse(null);
    }
}
