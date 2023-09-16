package com.fitivation_v3.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSummary {

  private int rate;
  private long count;
  private float percent;
  private long total;
}
