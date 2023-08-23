package com.fitivation_v3.files;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileDataRepository extends MongoRepository<FileData, ObjectId> {

  public Optional<FileData> findByName(String fileName);

  public void deleteByName(String fileName);
}
