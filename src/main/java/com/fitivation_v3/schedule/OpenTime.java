package com.fitivation_v3.schedule;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpenTime {

  private DayOfWeek dayOfWeek;
  private Set<ShiftTime> shiftTimes;
}
