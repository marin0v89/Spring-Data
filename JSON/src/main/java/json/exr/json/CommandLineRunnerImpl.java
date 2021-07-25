package json.exr.json;

import com.google.gson.Gson;
import json.exr.json.model.dto.ProductNameAndPriceDto;
import json.exr.json.model.dto.UserSoldDto;
import json.exr.json.service.CategoryService;
import json.exr.json.service.ProductService;
import json.exr.json.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private static final String OUTPUT_DIR = "src/main/resources/files/output/";
    private static final String PRODUCTS_IN_RANGE = "products-in-range.json";
    private static final String PRODUCTS_SOLD_FILE = "users-sold-products.json";

    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader bufferedReader;
    private final Gson gson;


    public CommandLineRunnerImpl(CategoryService categoryService, UserService userService, ProductService productService, Gson gson) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.gson = gson;
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
        System.out.println("Please enter exercise :");
        int exNum = Integer.parseInt(bufferedReader.readLine());

        switch (exNum) {
            case 1 -> productsInRange();
            case 2 -> usersSoldProducts();
        }
    }

    private void usersSoldProducts() throws IOException {
        List<UserSoldDto> userSoldDtos =
                userService.findAllUsersWithMoreThanOneSell();
        String content = gson.toJson(userSoldDtos);

        writeToFile(OUTPUT_DIR + PRODUCTS_SOLD_FILE, content);
    }

    private void productsInRange() throws IOException {

        List<ProductNameAndPriceDto> productDtos =
                productService.
                        findAllProductsInRangeOrderByPrice
                                (BigDecimal.valueOf(500L), BigDecimal.valueOf(1000L));

        String content = gson.toJson(productDtos);

        writeToFile(OUTPUT_DIR + PRODUCTS_IN_RANGE, content);
    }

    private void writeToFile(String path, String content) throws IOException {
        Files
                .write(Path.of(path), Collections.singleton(content));


    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        userService.seedUsers();
        productService.seedProducts();
    }
}
