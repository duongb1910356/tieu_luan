package com.fitivation_v3.review;

import com.fitivation_v3.review.dto.ReviewCreateDto;
import com.fitivation_v3.shared.ListResponse;
import java.io.IOException;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

  @Autowired
  private ReviewService reviewService;

  @PostMapping("/create")
  public ResponseEntity<?> createReview(
      @ModelAttribute ReviewCreateDto reviewCreateDto,
      @RequestParam(value = "image", required = false) MultipartFile file) throws IOException {
    if (file != null && !file.isEmpty()) {
      Review review = reviewService.createReviewWithImage(reviewCreateDto, file);
      return new ResponseEntity<Review>(review, HttpStatus.OK);
    } else {
      Review review = reviewService.createReviewWithoutImage(reviewCreateDto);
      return new ResponseEntity<Review>(review, HttpStatus.OK);
    }
  }

  @GetMapping("/facility/{facilityId}")
  public ResponseEntity<ListResponse<Review>> getReviewByFacilityId(
      @PathVariable ObjectId facilityId) {
    List<Review> reviews = reviewService.getReviewByFacilityId(facilityId);
    ListResponse<Review> listResponse = new ListResponse<>();
    listResponse.setItems(reviews);
    listResponse.setTotals(reviews.size());
    return new ResponseEntity<>(listResponse, HttpStatus.OK);
  }
}
