package json.exr.json.service.impl;

import com.google.gson.Gson;
import json.exr.json.model.dto.CategorySeedDto;
import json.exr.json.model.entity.Categories;
import json.exr.json.repository.CategoryRepository;
import json.exr.json.service.CategoryService;
import json.exr.json.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static json.exr.json.constants.RootPath.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORIES_FILE = "categories.json";

    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(Gson gson, ValidationUtil validationUtil, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override

    public void seedCategories() throws IOException {
        if (categoryRepository.count() > 0) {
            return;
        }

        String fileContent = Files
                .readString(Path.of(RESOURCE_DIRECTORY + CATEGORIES_FILE));

        CategorySeedDto[] categorySeedDtos =
                gson.fromJson(fileContent, CategorySeedDto[].class);

        Arrays.stream(categorySeedDtos)
                .filter(validationUtil::isValid)
                .map(categorySeedDto -> modelMapper.map(categorySeedDto, Categories.class))
                .forEach(categoryRepository::save);
    }

    @Override
    public Set<Categories> findRandomCategories() {
        Set<Categories> categoriesSet = new HashSet<>();
        int catCount = ThreadLocalRandom.current().nextInt(1, 3);
        long total = categoryRepository.count();

        for (int i = 0; i < catCount; i++) {
            long randomId = ThreadLocalRandom
                    .current().nextLong(1, total + 1);
            categoriesSet.add(categoryRepository
                    .findById(randomId).orElse(null));
        }
        return categoriesSet;
    }
}
