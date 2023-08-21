package com.fitivation_v3.user;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
  public Optional<User> findByUsername(String username);
  public Boolean existsByUsername(String username);
}
