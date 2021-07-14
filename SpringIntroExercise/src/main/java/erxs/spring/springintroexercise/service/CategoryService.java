package erxs.spring.springintroexercise.service;

import erxs.spring.springintroexercise.models.entity.Category;

import java.io.IOException;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;

    Set<Category> getRandomCategories();
}
