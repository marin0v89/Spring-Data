package erxs.spring.springintroexercise.appRunner;

import erxs.spring.springintroexercise.models.entity.Book;
import erxs.spring.springintroexercise.service.AuthorService;
import erxs.spring.springintroexercise.service.BookService;
import erxs.spring.springintroexercise.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {

        seedComponents();

//        Problem solutions
//        problemOne(2000);
//        problemTwo(1990);
//        problemThree();
//        problemFour("George", "Powell");
    }

    private void problemFour(String firstName, String lastName) {
        bookService.findAllBooksByAuthor(firstName, lastName)
                .forEach(System.out::println);
    }

    private void problemThree() {
        authorService.getAllAuthorsByBooks()
                .forEach(System.out::println);
    }

    private void problemTwo(int year) {
        bookService
                .findAllAuthorsAfterYear(year)
                .forEach(System.out::println);
    }

    private void problemOne(int year) {
        bookService
                .findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedComponents() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
