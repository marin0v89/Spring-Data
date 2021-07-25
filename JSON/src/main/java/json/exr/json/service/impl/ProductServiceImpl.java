package json.exr.json.service.impl;

import com.google.gson.Gson;
import json.exr.json.model.dto.ProductNameAndPriceDto;
import json.exr.json.model.dto.ProductSeedDto;
import json.exr.json.model.entity.Products;
import json.exr.json.repository.ProductRepository;
import json.exr.json.service.CategoryService;
import json.exr.json.service.ProductService;
import json.exr.json.service.UserService;
import json.exr.json.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static json.exr.json.constants.RootPath.RESOURCE_DIRECTORY;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String FILE_PATH = "products.json";

    private final ProductRepository productRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public ProductServiceImpl(ProductRepository productRepository, UserService userService, CategoryService categoryService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public void seedProducts() throws IOException {
        if (productRepository.count() > 0) {
            return;
        }

        String fileContent =
                Files.readString(Path.of(RESOURCE_DIRECTORY + FILE_PATH));

        ProductSeedDto[] productSeedDtos =
                gson.fromJson(fileContent, ProductSeedDto[].class);

        Arrays.stream(productSeedDtos)
                .filter(validationUtil::isValid)
                .map(productSeedDto -> {
                    Products products = modelMapper.map(productSeedDto, Products.class);
                    products.setSeller(userService.findRandomUser());

                    if (products.getPrice().compareTo(BigDecimal.valueOf(900L)) > 0) {
                        products.setBuyer(userService.findRandomUser());
                    }
                    products.setCategories(categoryService.findRandomCategories());

                    return products;
                })
                .forEach(productRepository::save);
    }

    @Override
    public List<ProductNameAndPriceDto> findAllProductsInRangeOrderByPrice(BigDecimal lower, BigDecimal upper) {
        return productRepository
                .findAllByPriceBetweenAndBuyerIsNullOrderByPriceDesc(lower, upper)
                .stream()
                .map(products -> {
                    ProductNameAndPriceDto productNameAndPriceDto = modelMapper
                            .map(products, ProductNameAndPriceDto.class);

                    productNameAndPriceDto.setSeller(String.format("%s %s ",
                            products.getSeller().getFirstName(),
                            products.getSeller().getLastName()));

                    return productNameAndPriceDto;
                })
                .collect(Collectors.toList());
    }
}
