package com.fitivation_v3.security.service;

import com.fitivation_v3.security.jwt.RefreshToken;
import com.fitivation_v3.security.jwt.RefreshTokenRepository;
import com.fitivation_v3.user.UserRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
  @Value("${bezkoder.app.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private UserRepository userRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(ObjectId userId) {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(userRepository.findById(userId).get());
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) throws IllegalArgumentException {
    if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
      System.out.println("da chay toi day");
      refreshTokenRepository.delete(token);
      System.out.println("da qua day");
      throw new IllegalArgumentException("Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Transactional
  public Boolean deleteByUserId(ObjectId userId) {
    return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
  }
}
