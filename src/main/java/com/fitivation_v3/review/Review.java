package com.fitivation_v3.review;

import com.fitivation_v3.files.FileData;
import com.fitivation_v3.user.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reviews")
public class Review {

  @Id
  private ObjectId id;

  @DBRef
  private User author;

  private ObjectId facilityId;
  private String comment;
  private int rate;
  private List<FileData> images;
  private List<Review> reply;
}
