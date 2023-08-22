package com.fitivation_v3.security.jwt;

import com.fitivation_v3.user.User;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "refreshtokens")
public class RefreshToken {
  @Id
  private ObjectId id;

  @DBRef
  private User user;

  private String token;
  private Instant expiryDate;
}
