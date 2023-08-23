package com.fitivation_v3.user;

import java.util.Date;
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

  private String username; //email
  private String password;
  private Set<Role> roles;

  private String displayName;
  private String avatar;
  private Date birth;
  private String phone;
  private Sex sex;


  public User(String username, String password){
    this.username = username;
    this.password = password;
  }
}
