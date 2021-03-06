package erxs.spring.springintroexercise.service.impl;

import erxs.spring.springintroexercise.constants.Constants;
import erxs.spring.springintroexercise.models.entity.AgeRestriction;
import erxs.spring.springintroexercise.models.entity.Book;
import erxs.spring.springintroexercise.models.entity.Category;
import erxs.spring.springintroexercise.models.entity.EditionType;
import erxs.spring.springintroexercise.repository.BookRepository;
import erxs.spring.springintroexercise.service.AuthorService;
import erxs.spring.springintroexercise.service.BookService;
import erxs.spring.springintroexercise.service.CategoryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedBooks() throws IOException {
        if (bookRepository.count() > 0) {
            return;
        }

        Files
                .readAllLines(Path.of(Constants.BOOKS_FILE_PATH))
                .forEach(row -> {
                    String[] bookInfo = row.split("\\s+");

                    var book = createBook(bookInfo);
                    bookRepository.save(book);
                });
    }

    @Override
    public List<Book> findAllBooksAfterYear(int year) {

        return bookRepository
                .findAllByReleaseDateAfter
                        (LocalDate.of(year, 12, 31));
    }

    @Override
    public List<String> findAllAuthorsAfterYear(int year) {

        return bookRepository.
                findAllByReleaseDateBefore
                        (LocalDate.of(year, 1, 1))
                .stream()
                .map(book -> String.format("%s %s", book.getAuthor().getFirstName(),
                        book.getAuthor().getLastName()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksByAuthor(String firstName, String lastName) {
        return bookRepository
                .findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(firstName, lastName)
                .stream()
                .map(book -> String.format("%s %s %d"
                        , book.getTitle()
                        , book.getReleaseDate(),
                        book.getCopies()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction) {
        return bookRepository.findAllByAgeRestriction(ageRestriction);
    }

    @Override
    public List<String> findAllGoldBooks() {
        return bookRepository.
                findAllByEditionTypeAndCopiesLessThan(EditionType.GOLD, 5000)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksByPrice() {
        return bookRepository
                .findAllByPriceLessThanOrPriceGreaterThan(BigDecimal.valueOf(5L), BigDecimal.valueOf(40L))
                .stream()
                .map(book -> String.format("%s - $%.2f",
                        book.getTitle(),
                        book.getPrice()))
                .collect(Collectors.toList());

    }

    @Override
    public List<String> findNotReleasedBooks(int year) {
        LocalDate lower = LocalDate.of(year, 1, 1);
        LocalDate upper = LocalDate.of(year, 12, 31);
        return bookRepository
                .findAllByReleaseDateBeforeOrReleaseDateAfter(lower, upper)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findBooksReleasedBeforeDate(int date, int month, int year) {
        return bookRepository.findAllByReleaseDateBefore(LocalDate.of(year, month, date))
                .stream()
                .map(book -> String.format("%s %s %.2f",
                        book.getTitle(),
                        book.getEditionType(),
                        book.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findBooksThatTitleContains(String contains) {
        return bookRepository
                .findAllByTitleContaining(contains)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());

    }

    @Override
    public List<String> findBookTitleWritenByAuthor(String startsWith) {
        return bookRepository
                .findAllByAuthor_LastNameStartsWith(startsWith)
                .stream()
                .map(book -> String.format("%s (%s %s)"
                        , book.getTitle()
                        , book.getAuthor().getFirstName()
                        , book.getAuthor().getLastName()))
                .collect(Collectors.toList());
    }

    @Override
    public int countAllBooksWithLength(int length) {
        return bookRepository.countOfBooksWithTitleLengthMoreThan(length);
    }

    @Override
    public List<String> findBookByTitle(String title) {
        return bookRepository
                .findBookByTitle(title)
                .stream()
                .map(book -> String.format("%s %s %s %.2f"
                        , book.getTitle()
                        , book.getEditionType()
                        , book.getAgeRestriction()
                        , book.getPrice()))
                .collect(Collectors.toList());
    }


    private Book createBook(String[] bookInfo) {
        EditionType editionType = EditionType.values()[Integer.parseInt(bookInfo[0])];
        LocalDate releaseDate = LocalDate.parse(bookInfo[1],
                DateTimeFormatter.ofPattern("d/M/yyyy"));
        Integer copies = Integer.parseInt(bookInfo[2]);
        BigDecimal price = new BigDecimal(bookInfo[3]);
        AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(bookInfo[4])];
        String title = Arrays.stream(bookInfo)
                .skip(5)
                .collect(Collectors.joining(" "));
        var author = authorService.getRandomAuthor();
        Set<Category> categories = categoryService.getRandomCategories();


        return new Book
                (editionType, releaseDate, copies, price,
                        ageRestriction, title, author, categories);
    }
}
