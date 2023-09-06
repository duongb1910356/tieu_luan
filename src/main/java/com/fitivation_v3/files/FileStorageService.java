package com.fitivation_v3.files;

import com.fitivation_v3.exception.NotFoundException;
import com.fitivation_v3.security.service.UserDetailsImpl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

  @Autowired
  FileDataRepository fileDataRepository;

  @Value("${app.rootDirectory}/files/")
  private String FOLDER_PATH;

  @Value("http://localhost:8080/api/file/")
  private String URL_ROOT;

  public FileData uploadImageToFileSystem(MultipartFile file, ObjectId facilityId)
      throws IOException {
    UserDetailsImpl userDetails =
        (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String uniqueFileName = generateUniqueFileName(
        Objects.requireNonNull(file.getOriginalFilename()));
    String filePath = StringUtils.cleanPath(
        FOLDER_PATH + uniqueFileName);

    FileData fileData = fileDataRepository.save(
        FileData.builder().name(uniqueFileName).type(file.getContentType()).facilityId(facilityId)
            .filePath(URL_ROOT + uniqueFileName)
            .userIdUpload(userDetails.getId()).build());

    if (fileData.getId() != null) {
      file.transferTo(new File(filePath));
      return fileData;
    }
    return null;
  }

  public List<FileData> uploadImagesToFileSystem(MultipartFile[] files, ObjectId facilityId)
      throws IOException {
    List<FileData> uploadedFiles = new ArrayList<>();
    for (MultipartFile file : files) {
      FileData fileData = uploadImageToFileSystem(file, facilityId);
      uploadedFiles.add(fileData);
    }
    
    return uploadedFiles;
  }

  public byte[] downloadImageFileSystem(String fileName) throws IOException {
    Optional<FileData> fileData = fileDataRepository.findByName(fileName);
    if (fileData.isPresent()) {
      String filePath = StringUtils.cleanPath(FOLDER_PATH + fileData.get().getName());
      return Files.readAllBytes(new File(filePath).toPath());
    }
    throw new NotFoundException("Not found image");
  }

  public static String generateUniqueFileName(String originalFileName) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    String timestamp = dateFormat.format(new Date());

    String randomUUID = UUID.randomUUID().toString().replaceAll("-", "");

    int lastDotIndex = originalFileName.lastIndexOf(".");
    String extension = "";
    if (lastDotIndex != -1) {
      extension = originalFileName.substring(lastDotIndex);
    }

    return timestamp + "_" + randomUUID + extension;
  }

  public Optional<FileData> delete(String fileName) {
    Optional<FileData> fileData = fileDataRepository.findByName(fileName);
    if (fileData.isPresent()) {
      fileDataRepository.deleteByName(fileData.get().getName());
      deleteFile(fileName);
    }
    return fileData;
  }

  public boolean deleteFile(String fileName) {
    String filePath = StringUtils.cleanPath(FOLDER_PATH + fileName);
    File fileToDelete = new File(filePath);

    if (fileToDelete.exists()) {
      return fileToDelete.delete();
    } else {
      return false;
    }
  }
}
