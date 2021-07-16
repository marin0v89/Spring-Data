package erxs.spring.springintroexercise.service.impl;

import erxs.spring.springintroexercise.constants.Constants;
import erxs.spring.springintroexercise.models.entity.Author;
import erxs.spring.springintroexercise.repository.AuthorRepository;
import erxs.spring.springintroexercise.service.AuthorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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

    @Override
    public List<String> getAllAuthorsByBooks() {

        return authorRepository
                .findAllBooksDesc()
                .stream()
                .map(author -> String.format("%s %s %d",
                        author.getFirstName(),
                        author.getLastName(),
                        author.getBooks().size()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAuthorsWhosFirstNameEndsWith(String endsWith) {
        return authorRepository
                .findAllByFirstNameEndingWith(endsWith)
                .stream()
                .map(author -> String.format("%s %s"
                        ,author.getFirstName()
                        ,author.getLastName()))
                .collect(Collectors.toList());

    }
}
