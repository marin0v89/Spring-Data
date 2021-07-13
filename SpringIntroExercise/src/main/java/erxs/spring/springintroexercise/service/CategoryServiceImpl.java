package erxs.spring.springintroexercise.service;

import erxs.spring.springintroexercise.constants.Constants;
import erxs.spring.springintroexercise.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seedCategories() throws IOException {
        Files
                .readAllLines(Path.of(Constants.CATEGORIES_FILE_PATH));
    }
}
