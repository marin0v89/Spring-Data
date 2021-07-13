package erxs.spring.springintroexercise.service.impl;

import erxs.spring.springintroexercise.constants.Constants;
import erxs.spring.springintroexercise.models.entity.Category;
import erxs.spring.springintroexercise.repository.CategoryRepository;
import erxs.spring.springintroexercise.service.CategoryService;
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
        if (categoryRepository.count() > 0) {
            return;
        }
        Files
                .readAllLines(Path.of(Constants.CATEGORIES_FILE_PATH))
                .stream()
                .filter(row -> !row.isBlank())
                .forEach(catName -> {
                    var category = new Category(catName);
                    categoryRepository.save(category);
                });
    }
}
