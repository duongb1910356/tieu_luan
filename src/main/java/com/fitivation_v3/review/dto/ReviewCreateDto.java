package com.fitivation_v3.review.dto;

import com.fitivation_v3.files.FileData;
import com.fitivation_v3.review.Review;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateDto {

  private String facilityId;
  private String comment;
  private int rate;
  private List<FileData> images;
}
