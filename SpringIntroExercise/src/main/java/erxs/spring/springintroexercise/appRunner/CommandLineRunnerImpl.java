package erxs.spring.springintroexercise.appRunner;

import erxs.spring.springintroexercise.service.AuthorService;
import erxs.spring.springintroexercise.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
    }

    @Override
    public void run(String... args) throws Exception {

        categoryService.seedCategories();
        authorService.seedAuthors();
    }
}
