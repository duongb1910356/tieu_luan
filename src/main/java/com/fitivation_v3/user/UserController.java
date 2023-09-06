package com.fitivation_v3.user;

import com.fitivation_v3.exception.BadRequestException;
import com.fitivation_v3.files.FileStorageService;
import com.fitivation_v3.user.dto.UpdateUserDto;
import com.fitivation_v3.user.dto.UserDto;
import java.io.IOException;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private FileStorageService fileStorageService;

  @GetMapping("/all")
  public String allGetAccess() {
    return "Public user content";
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserDto> getUserById(@PathVariable ObjectId userId) {
    Optional<UserDto> userDto = userService.getUserDtoById(userId);
    return userDto.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
        .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
  }

  @PatchMapping("/update/avatar/{userId}")
  public ResponseEntity<UserDto> updateUserAvatarById(@PathVariable ObjectId userId,
      @RequestParam("image") MultipartFile file)
      throws IOException {
    UserDto userDto = userService.updateUserAvatarById(userId, file);
    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }

  @PatchMapping("/update/{userId}")
  public ResponseEntity<UserDto> updateUserById(@PathVariable ObjectId userId, @RequestBody
  UpdateUserDto updateUserDto) {
    Boolean isUpdate = userService.updateUserById(userId, updateUserDto);
    if (isUpdate) {
      Optional<UserDto> userDto = userService.getUserDtoById(userId);
      return userDto.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
          .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
    throw new BadRequestException("Update user failed");
  }
}
