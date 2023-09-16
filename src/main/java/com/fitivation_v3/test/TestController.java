package com.fitivation_v3.test;

import com.fitivation_v3.exception.AuthorizeException;
import com.fitivation_v3.exception.NotFoundException;
import com.fitivation_v3.user.User;
import com.fitivation_v3.user.UserRepository;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

  @Autowired
  private UserRepository userRepository;

  @GetMapping("/all")
  public String allAccess() {
    return "Public content";
  }

  @GetMapping("/user")
  public ResponseEntity<?> userAccess() {
    Optional<User> user = userRepository.findById(new ObjectId("64f58b621a2b6367541e581a"));
    return ResponseEntity.ok(user.get());
//    return "ROLE_USER Content";
  }
}
