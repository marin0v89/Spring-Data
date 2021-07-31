package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.PictureSeedDto;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {

    private static final String PICTURES_FILE = "src/main/resources/files/pictures.json";

    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public PictureServiceImpl(PictureRepository pictureRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files
                .readString(Path.of(PICTURES_FILE));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readFromFileContent(), PictureSeedDto[].class))
                .filter(pictureSeedDto -> {
                    var isValid = validationUtil.isValid(pictureSeedDto)
                            && !entityExists(pictureSeedDto.getPath());
                    if (isValid) {
                        sb
                                .append(String.format
                                        ("Successfully imported Picture, with size %.2f", pictureSeedDto.getSize()))

                                .append(System.lineSeparator());
                    } else {
                        sb
                                .append("Invalid Picture")
                                .append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(pictureSeedDto -> modelMapper.map(pictureSeedDto, Picture.class))
                .forEach(pictureRepository::save);

        return sb.toString().trim();
    }

    @Override
    public boolean entityExists(String path) {
        return pictureRepository.existsByPath(path);
    }

    @Override
    public String exportPictures() {
        StringBuilder sb = new StringBuilder();
        List<Picture> allBySizeGreaterThanOrderBySize = pictureRepository
                .findAllBySizeGreaterThanOrderBySize(30000L);
        for (Picture picture : allBySizeGreaterThanOrderBySize) {
            sb
                    .append(String.format("%.2f – %s",
                            picture.getSize(), picture.getPath()))
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    @Override
    public Picture findByPath(String path) {

        return pictureRepository.findByPath(path)
                .orElse(null);

    }
}