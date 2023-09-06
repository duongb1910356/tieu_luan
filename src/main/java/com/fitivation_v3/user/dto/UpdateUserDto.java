package com.fitivation_v3.user.dto;

import com.fitivation_v3.user.Sex;
import com.mongodb.lang.Nullable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDto {

  private String displayName;
  private String avatar;
  private Date birth;
  private String phone;
  private Sex sex;
  private String customerIdStripe;

}
