package json.exr.json.service.impl;

import json.exr.json.constants.RootPath;
import json.exr.json.service.CategoryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static json.exr.json.constants.RootPath.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORIES_FILE = "categories.json";

    @Override

    public void seedCategories() throws IOException {

        String fileContent = Files
                .readString(Path.of(RESOURCE_DIRECTORY + CATEGORIES_FILE));
    }
}
