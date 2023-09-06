package com.fitivation_v3.review;

import com.fitivation_v3.exception.BadRequestException;
import com.fitivation_v3.facility.Facility;
import com.fitivation_v3.facility.FacilityRepository;
import com.fitivation_v3.files.FileData;
import com.fitivation_v3.files.FileStorageService;
import com.fitivation_v3.review.dto.ReviewCreateDto;
import com.fitivation_v3.security.service.UserDetailsImpl;
import com.fitivation_v3.user.User;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReviewService {

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private FileStorageService fileStorageService;

  @Autowired
  private ModelMapper mapper;

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  public Review createReviewWithImage(ReviewCreateDto reviewCreateDto, MultipartFile file)
      throws IOException {
    try {
      UserDetailsImpl userDetails =
          (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      User user = mapper.map(userDetails, User.class);

      FileData fileData = fileStorageService.uploadImageToFileSystem(file,
          new ObjectId(reviewCreateDto.getFacilityId()));
      List<FileData> listFileData = new ArrayList<>();
      listFileData.add(fileData);

      reviewCreateDto.setImages(listFileData);

      Review review = mapper.map(reviewCreateDto, Review.class);
      //mapper tu dong lay authorId gan qua _id nên phải setId lại bằng rỗng
      review.setId(new ObjectId());
      review.setFacilityId(new ObjectId(reviewCreateDto.getFacilityId()));
      review.setAuthor(user);
      review = reviewRepository.save(review);

      float rate = calculateAverageRateByFacilityId(new ObjectId(reviewCreateDto.getFacilityId()));
      Optional<Facility> facility = facilityRepository.findById(
          new ObjectId(reviewCreateDto.getFacilityId()));
      if (facility.isPresent()) {
        facility.get().setAvagerstar(rate);
        facilityRepository.save(facility.get());
      }

      return review;
    } catch (IOException ex) {
      throw new BadRequestException("Can't create review");
    }
  }

  public Review createReviewWithoutImage(ReviewCreateDto reviewCreateDto) {
    try {
      UserDetailsImpl userDetails =
          (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      User user = mapper.map(userDetails, User.class);

      Review review = mapper.map(reviewCreateDto, Review.class);
      review.setId(new ObjectId());
      review.setFacilityId(new ObjectId(reviewCreateDto.getFacilityId()));
      review.setAuthor(user);
      review = reviewRepository.save(review);

      float rate = calculateAverageRateByFacilityId(new ObjectId(reviewCreateDto.getFacilityId()));
      Optional<Facility> facility = facilityRepository.findById(
          new ObjectId(reviewCreateDto.getFacilityId()));
      if (facility.isPresent()) {
        facility.get().setAvagerstar(rate);
        facilityRepository.save(facility.get());
      }

      return review;
    } catch (Exception ex) {
      throw new BadRequestException("Can't create review: " + ex);
    }
  }

  public List<Review> getReviewByFacilityId(ObjectId id) {
    List<Review> list = reviewRepository.findByFacilityId(id);
    return list;
  }

  public float calculateAverageRateByFacilityId(ObjectId facilityId) {
    Aggregation aggregation = Aggregation.newAggregation(
        Aggregation.match(Criteria.where("facilityId").is(facilityId)),
        Aggregation.group().avg("rate").as("averageRate")
    );

    AggregationResults<Document> result =
        mongoTemplate.aggregate(aggregation, Review.class, Document.class);

    if (!result.getMappedResults().isEmpty()) {
      Document document = result.getMappedResults().get(0);
      Double averageRate = document.getDouble("averageRate");

      return (float) (Math.round(averageRate * 10.0) / 10.0);
    }
    return 0.0F;
  }
}
