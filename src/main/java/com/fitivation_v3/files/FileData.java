package com.fitivation_v3.files;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fitivation_v3.config.ObjectIdSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "files")
public class FileData {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId id;
  private String name;
  private String type;
  private String filePath;
  
  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId userIdUpload;

  @JsonSerialize(using = ObjectIdSerializer.class)
  private ObjectId facilityId;
}
