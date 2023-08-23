package com.fitivation_v3.auth;

import com.fitivation_v3.auth.dto.JwtResponseDto;
import com.fitivation_v3.auth.dto.LoginDto;
import com.fitivation_v3.auth.dto.SignupDto;
import com.fitivation_v3.auth.dto.TokenRefreshRequest;
import com.fitivation_v3.auth.dto.TokenRefreshResponse;
import com.fitivation_v3.security.jwt.RefreshToken;
import com.fitivation_v3.security.service.RefreshTokenService;
import com.fitivation_v3.security.service.UserDetailsImpl;
import com.fitivation_v3.security.jwt.JwtUtils;
import com.fitivation_v3.user.Role;
import com.fitivation_v3.user.User;
import com.fitivation_v3.user.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;

  @GetMapping("/all")
  public String allGetAccess() {
    return "Public auth content";
  }

  @PostMapping("/all")
  public String allPostAccess() {
    return "Public auth content";
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
            loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
    return ResponseEntity.ok(new JwtResponseDto(
        jwt,
        refreshToken.getToken(),
        userDetails.getId(),
        userDetails.getUsername(),
        roles,
        userDetails.getDisplayName(),
        userDetails.getAvatar(),
        userDetails.getBirth(),
        userDetails.getPhone(),
        userDetails.getSex()));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignupDto signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body("Error: Username is already taken");
    }
    User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()));
    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();
    strRoles.forEach(role -> {
      switch (role) {
        case "ROLE_ADMIN":
        case "ROLE_OWNER":
        case "ROLE_USER":
          roles.add(Role.valueOf(role));
          break;
      }
    });
    user.setRoles(roles);
    userRepository.save(user);
    return ResponseEntity.ok("User registered successfully!");
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();
    System.out.println("refreshToken: " + requestRefreshToken);
    Optional<RefreshToken> refr = refreshTokenService.findByToken(requestRefreshToken);
    System.out.println("Value of refreshtoken: " + refr.get().getUser().getUsername());
    return refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(user -> {
          String token = jwtUtils.generateTokenFromUsername(user.getUsername());
          return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        })
        .orElseThrow(() -> new IllegalArgumentException("Refresh token is not in database!"));

//    String requestRefreshToken = request.getRefreshToken();
//    System.out.println("refreshToken: " + requestRefreshToken);
//    Optional<RefreshToken> refr = refreshTokenService.findByToken(requestRefreshToken);
//    System.out.println("Value of refreshtoken: " + refr.get().getUser().getUsername());
//    return ResponseEntity.ok("Call refreshtoken");
  }
}
