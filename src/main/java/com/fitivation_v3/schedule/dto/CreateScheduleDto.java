package com.fitivation_v3.schedule.dto;

import com.fitivation_v3.schedule.OpenTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateScheduleDto {

  private Set<OpenTime> openTimes;
}
