package com.fitivation_v3.test;

import com.fitivation_v3.exception.AuthorizeException;
import com.fitivation_v3.exception.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
  @GetMapping("/all")
  public String allAccess() {
    return "Public content";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('ROLE_USER')")
  public String userAccess() {
    throw new AuthorizeException();
//    return "ROLE_USER Content";
  }
}
