package erxs.spring.springintroexercise.appRunner;

import erxs.spring.springintroexercise.models.entity.AgeRestriction;
import erxs.spring.springintroexercise.models.entity.Book;
import erxs.spring.springintroexercise.service.AuthorService;
import erxs.spring.springintroexercise.service.BookService;
import erxs.spring.springintroexercise.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader bufferedReader;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService, BufferedReader bufferedReader) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run(String... args) throws Exception {

        seedComponents();

//        Problem solutions
//        problemOne(2000);
//        problemTwo(1990);
//        problemThree();
//        problemFour("George", "Powell");
        System.out.println("Please select the problem number :");
        int exNumber = Integer.parseInt(bufferedReader.readLine());

        switch (exNumber) {
            case 1 -> bookTitleByAgeRestriction();
        }
    }

    private void bookTitleByAgeRestriction() throws IOException {
        System.out.println("Please enter age restriction :");
        AgeRestriction ageRestriction =
                AgeRestriction.valueOf(bufferedReader.readLine().toUpperCase());

        bookService.findAllByAgeRestriction(ageRestriction)
                .forEach(book -> System.out.println(book.getTitle()));
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
