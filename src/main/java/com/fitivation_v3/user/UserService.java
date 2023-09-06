package com.fitivation_v3.user;

import com.fitivation_v3.exception.BadRequestException;
import com.fitivation_v3.exception.NotFoundException;
import com.fitivation_v3.files.FileData;
import com.fitivation_v3.files.FileStorageService;
import com.fitivation_v3.security.service.UserDetailsImpl;
import com.fitivation_v3.user.dto.UpdateUserDto;
import com.fitivation_v3.user.dto.UserDto;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private FileStorageService fileStorageService;

  @Autowired
  private ModelMapper mapper;

  public Optional<UserDto> getUserDtoById(ObjectId id) {
    Optional<User> user = userRepository.findById(id);
    return Optional.ofNullable(mapper.map(user, UserDto.class));
  }

  public Optional<User> getUserById(ObjectId id) {
    return userRepository.findById(id);
  }

  public Boolean updateUserById(ObjectId id, UpdateUserDto updateUserDto) {
    Query query = new Query(Criteria.where("_id").is(id));
    Update update = new Update();
    Field[] fields = updateUserDto.getClass().getDeclaredFields();
    for (Field field : fields) {
      try {
        field.setAccessible(true);
        Object newValue = field.get(updateUserDto);
        if (newValue != null) {
          update.set(field.getName(), newValue);
        }
      } catch (IllegalAccessException e) {
        System.out.println("Error update user: " + e);
      }
    }

    if (!update.getUpdateObject().isEmpty()) {
      mongoTemplate.updateFirst(query, update, User.class);
      return true;
    }
    return false;
  }

  public UserDto updateUserAvatarById(ObjectId id, MultipartFile file) throws IOException {
    Optional<User> user = userRepository.findById(id);
    FileData fileData = fileStorageService.uploadImageToFileSystem(file, null);
    if (user.isPresent()) {
      user.get().setAvatar(fileData.getFilePath());
      userRepository.save(user.get());
      return mapper.map(user, UserDto.class);
    }
    throw new NotFoundException("Not found user to update avatar");
  }

  public void updateCustomerIdStripe(String customerId) {
    try {
      UserDetailsImpl userDetails =
          (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      User user = mapper.map(userDetails, User.class);

      user.setCustomerIdStripe(customerId);

      userRepository.save(user);
    } catch (Exception ex) {
      System.out.println("Error update customer id stripe: " + ex);
      throw new BadRequestException("Error update customer id stripe");
    }
  }

}
