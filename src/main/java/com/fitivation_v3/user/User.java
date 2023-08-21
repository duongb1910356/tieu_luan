package com.fitivation_v3.user;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
  @Id
  private ObjectId id;

  private String username;
  private String email;
  private String password;
  private Set<Role> roles;

  public User(String username, String email, String password){
    this.username = username;
    this.email = email;
    this.password = password;
  }
}
