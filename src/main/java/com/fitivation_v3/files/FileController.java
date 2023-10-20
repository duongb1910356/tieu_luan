package com.fitivation_v3.files;

import com.fitivation_v3.exception.BadRequestException;
import com.fitivation_v3.exception.NotFoundException;
import com.fitivation_v3.shared.ListResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/file")
public class FileController {

  @Autowired
  FileStorageService fileStorageService;

  @PostMapping("/upload")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<FileData> uploadFileToFileSystem(@RequestParam("image") MultipartFile file,
      @RequestParam(value = "facilityId", required = false)
      ObjectId facilityId)
      throws IOException {
    FileData uploadImage = fileStorageService.uploadImageToFileSystem(file, facilityId);
    if (uploadImage.getId() == null) {
      throw new BadRequestException("Upload image failed");
    } else {
      return new ResponseEntity<>(uploadImage, HttpStatus.OK);
    }
  }

  @PatchMapping("/upload_bulk")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<ListResponse<FileData>> uploadManyFileToFileSystem(
      @RequestParam("images") MultipartFile[] files,
      @RequestParam(value = "facilityId", required = false)
      ObjectId facilityId)
      throws IOException {
    List<FileData> uploadImages = fileStorageService.uploadImagesToFileSystem(files, facilityId);
    if (uploadImages.isEmpty()) {
      throw new BadRequestException("Upload image failed");
    } else {
      ListResponse<FileData> listResponse = new ListResponse<>();
      listResponse.setItems(uploadImages);
      listResponse.setTotals(uploadImages.size());
      return new ResponseEntity<>(listResponse, HttpStatus.OK);
    }
  }

  @GetMapping("/{fileName}")
  public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName)
      throws IOException {
    byte[] imageData = fileStorageService.downloadImageFileSystem(fileName);
    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png"))
        .body(imageData);
  }

  @DeleteMapping("/delete/{fileName}")
  public ResponseEntity<FileData> deleteImage(@PathVariable String fileName) {
    Optional<FileData> fileData = fileStorageService.delete(fileName);
    if (fileData.isPresent()) {
      return new ResponseEntity<>(fileData.get(), HttpStatus.OK);
    }
    throw new NotFoundException("Not found exception to delete");
  }
}
