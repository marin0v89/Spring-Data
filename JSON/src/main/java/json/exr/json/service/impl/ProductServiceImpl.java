package json.exr.json.service.impl;

import json.exr.json.constants.RootPath;
import json.exr.json.service.ProductService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static json.exr.json.constants.RootPath.RESOURCE_DIRECTORY;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String FILE_PATH = "products.json";

    @Override
    public void seedProducts() throws IOException {
        String fileContent =
                Files.readString(Path.of(RESOURCE_DIRECTORY + FILE_PATH));
    }
}
