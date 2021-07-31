package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.PostSeedRootDto;
import softuni.exam.instagraphlite.models.entity.Post;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;
import softuni.exam.instagraphlite.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PostServiceImpl implements PostService {

    private static final String POST_FILE = "src/main/resources/files/posts.xml";

    private final PostRepository postRepository;
    private final UserService userService;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public PostServiceImpl(PostRepository postRepository, UserService userService, PictureService pictureService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files
                .readString(Path.of(POST_FILE));
    }

    @Override
    public String importPosts() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        xmlParser.fromFiles(POST_FILE, PostSeedRootDto.class)
                .getPosts()
                .stream()
                .filter(postSeedDto -> {
                    var isValid = validationUtil.isValid(postSeedDto)
                            && userService.isEntityExists(postSeedDto.getUser().getUsername())
                            && pictureService.entityExists(postSeedDto.getPicture().getPath());

                    if (isValid) {
                        sb
                                .append(String.format("Successfully imported Post, made by %s", postSeedDto.getUser().getUsername()))
                                .append(System.lineSeparator());
                    } else {
                        sb
                                .append("Invalid Post")
                                .append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(postSeedDto -> {
                    var post = modelMapper.map(postSeedDto, Post.class);
                    post.setUser(userService.findByUsername(postSeedDto.getUser().getUsername()));
                    post.setPicture(pictureService.findByPath(postSeedDto.getPicture().getPath()));
                    return post;

                })
                .forEach(postRepository::save);


        return sb.toString().trim();
    }
}
