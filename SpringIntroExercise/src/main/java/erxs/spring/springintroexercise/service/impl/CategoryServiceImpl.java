package erxs.spring.springintroexercise.service.impl;

import erxs.spring.springintroexercise.constants.Constants;
import erxs.spring.springintroexercise.models.entity.Category;
import erxs.spring.springintroexercise.repository.CategoryRepository;
import erxs.spring.springintroexercise.service.CategoryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

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

    @Override
    public Set<Category> getRandomCategories() {
        Set<Category> categories = new HashSet<>();
        int randomInt = ThreadLocalRandom
                .current()
                .nextInt(1, 3);

        long count = categoryRepository.count();

        for (int i = 0; i < randomInt; i++) {
            long rand = ThreadLocalRandom
                    .current().nextLong(1, count + 1);

            Category category = categoryRepository
                    .findById(rand)
                    .orElse(null);

            categories.add(category);
        }
        return categories;
    }
}
