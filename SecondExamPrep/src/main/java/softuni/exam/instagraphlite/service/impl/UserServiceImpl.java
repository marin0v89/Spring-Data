package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.UserSeedDto;
import softuni.exam.instagraphlite.models.entity.Post;
import softuni.exam.instagraphlite.models.entity.User;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final String USERS_FILE = "src/main/resources/files/users.json";

    private final UserRepository userRepository;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public UserServiceImpl(UserRepository userRepository, PictureService pictureService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.userRepository = userRepository;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files
                .readString(Path.of(USERS_FILE));
    }

    @Override
    public String importUsers() throws IOException {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readFromFileContent(), UserSeedDto[].class))
                .filter(userSeedDto -> {
                    var isValid = validationUtil.isValid(userSeedDto)
                            && !isEntityExists(userSeedDto.getUsername())
                            && pictureService.entityExists(userSeedDto.getProfilePicture());
                    if (isValid) {
                        sb
                                .append(String.format("Successfully imported User: %s", userSeedDto.getUsername()))
                                .append(System.lineSeparator());
                    } else {
                        sb
                                .append("Invalid User")
                                .append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(userSeedDto -> {
                    var user = modelMapper.map(userSeedDto, User.class);
                    user.setProfilePicture
                            (pictureService.findByPath(userSeedDto.getProfilePicture()));
                    return user;
                })
                .forEach(userRepository::save);

        return sb.toString().trim();
    }

    @Override
    public boolean isEntityExists(String username) {
        return userRepository
                .existsByUsername(username);
    }

    @Override
    public String exportUsersWithTheirPosts() {
        StringBuilder sb = new StringBuilder();
        List<User> posts = userRepository
                .findAllByPostsOrderByPostsCountDescUserId();

        for (User post : posts) {
            sb.append(String.format("""
                            User: %s
                            Post count: %d
                            """

                    , post.getUsername(), post.getPosts().size()));
            Set<Post> caption = post.getPosts();
            for (Post capt : caption) {
                sb.append(String.format("""
                                        ==Post Details:
                                        ----Caption: %s
                                        ----Picture Size: %.2f
                                        """
                                , capt.getCaption(), capt.getPicture().getSize()))
                        .append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
