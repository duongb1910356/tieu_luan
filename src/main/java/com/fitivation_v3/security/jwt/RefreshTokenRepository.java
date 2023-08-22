package com.fitivation_v3.security.jwt;

import com.fitivation_v3.user.User;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, ObjectId> {
  Optional<RefreshToken> findByToken(String token);

  Boolean deleteByUser(User user);
}
