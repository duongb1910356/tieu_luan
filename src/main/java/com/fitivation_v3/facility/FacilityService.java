package com.fitivation_v3.facility;

import com.fitivation_v3.exception.BadRequestException;
import com.fitivation_v3.exception.NotFoundException;
import com.fitivation_v3.facility.dto.FacilityCreateDto;
import com.fitivation_v3.security.service.UserDetailsImpl;
import com.fitivation_v3.user.UserRepository;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class FacilityService {

  @Autowired
  private FacilityRepository facilityRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ModelMapper mapper;

  @Autowired
  private MongoTemplate mongoTemplate;

  public Facility createFacility(FacilityCreateDto facilityCreateDto) {
    try {
      String slugAddress =
          facilityCreateDto.getAddress().getStreet() + " " + facilityCreateDto.getAddress()
              .getWard()
              + " " + facilityCreateDto.getAddress().getDistrict() + " "
              + facilityCreateDto.getAddress()
              .getProvince();
      slugAddress = StringUtils.stripAccents(slugAddress);
      Point point = new Point(facilityCreateDto.getLocation().getX(),
          facilityCreateDto.getLocation().getY());

      UserDetailsImpl userDetails =
          (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      ObjectId userId = userDetails.getId();

      Facility facility = mapper.map(facilityCreateDto, Facility.class);

      facility.setSlugAddress(slugAddress);
      facility.setOwnerId(userId);
//      facility.setLocation(point);

      return facilityRepository.save(facility);
    } catch (Exception ex) {
      throw new BadRequestException("Can not facility created");
    }
  }

  public Optional<Facility> getFacilityById(ObjectId facilityId) {
    try {
      if (facilityRepository.existsById(facilityId)) {
        Optional<Facility> facility = facilityRepository.findById(facilityId);
        System.out.println("Facility exist: " + facility.isPresent());
        System.out.println("Facility name: " + facility.get().getName());
        return facility;
      } else {
        return Optional.empty();
      }
    } catch (Exception ex) {
      throw new BadRequestException("Can't retrieve facility: " + ex);
    }
  }

  public List<Facility> getNearbyFacilities(double longtitude, double latitude) {
    try {
      double maxDistanceKm = 5;
      Point userLocation = new Point(longtitude, latitude);
      Distance maxDistance = new Distance(maxDistanceKm, Metrics.KILOMETERS);

      NearQuery nearQuery = NearQuery.near(userLocation)
          .spherical(true)
          .distanceMultiplier(6378.137);

      Aggregation aggregation = Aggregation.newAggregation(
          Aggregation.geoNear(nearQuery, "location"),
          Aggregation.project("_id", "address", "avagerstar", "describe", "name")
              .and("location").as("distance")
      );
      AggregationResults<Facility> results = mongoTemplate.aggregate(aggregation, "facilities",
          Facility.class);

      return results.getMappedResults();
    } catch (Exception ex) {
      System.out.println("Exception getNearBy: " + ex);
      throw new NotFoundException("Can't aggregate facility near you");
    }
  }

  public List<Facility> searchFacilities(String searchText) {
    String searchSlugAddress = StringUtils.stripAccents(searchText);
    Criteria criteria = new Criteria().orOperator(
        Criteria.where("slugAddress").regex(searchSlugAddress, "i")
    );

    Query query = new Query(criteria);

    return mongoTemplate.find(query, Facility.class);
//    return facilityRepository.searchBySlugAddress(searchSlugAddress);

  }
}
