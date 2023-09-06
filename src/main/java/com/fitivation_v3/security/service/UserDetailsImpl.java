package com.fitivation_v3.security.service;

import com.fitivation_v3.user.Sex;
import com.fitivation_v3.user.User;
import com.google.gson.annotations.Expose;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails {

  private static final long serialVersionUID = 1L;
  private ObjectId id;
  private String username;
  @Expose
  private String password;
  private Collection<? extends GrantedAuthority> authorities;

  private String displayName;
  private String avatar;
  private Date birth;
  private String phone;
  private Sex sex;

  public UserDetailsImpl(ObjectId id, String username, String password,
      Collection<? extends GrantedAuthority> authorities, String displayName, String avatar,
      Date birth, String phone, Sex sex) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.authorities = authorities;
    this.displayName = displayName;
    this.avatar = avatar;
    this.birth = birth;
    this.phone = phone;
    this.sex = sex;
  }

  public static UserDetailsImpl build(User user) {
    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.toString()))
        .collect(Collectors.toList());

    return new UserDetailsImpl(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        authorities,
        user.getDisplayName(),
        user.getAvatar(),
        user.getBirth(),
        user.getPhone(),
        user.getSex());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}
