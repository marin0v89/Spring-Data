package json.exr.json.service;

import json.exr.json.model.entity.Categories;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
public interface CategoryService {
    void seedCategories() throws IOException;

    Set<Categories> findRandomCategories();
}
