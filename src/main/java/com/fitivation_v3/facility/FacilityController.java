package com.fitivation_v3.facility;

import com.fitivation_v3.facility.dto.FacilityCreateDto;
import com.fitivation_v3.shared.ListResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResults;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/facility")
public class FacilityController {

  @Autowired
  private FacilityService facilityService;

  @GetMapping("/search/{searchText}")
  public ResponseEntity<?> getNearbyFacilities(@PathVariable String searchText) {
    List<Facility> facilities = facilityService.searchFacilities(searchText);
    ListResponse<Facility> facilityListResponse = new ListResponse<>();
    facilityListResponse.setItems(facilities);
    facilityListResponse.setTotals(facilities.size());

    return new ResponseEntity<>(facilityListResponse, HttpStatus.OK);
  }

  @GetMapping("/get_nearby_facilities")
  public ResponseEntity<?> getNearbyFacilities(@RequestParam("longtitude") double longtitude,
      @RequestParam("latitude") double latitude,
      @RequestParam(value = "sortby", required = false) String sortBy) {
    List<Facility> facilities = facilityService.getNearbyFacilities(longtitude, latitude);
    ListResponse<Facility> facilityListResponse = new ListResponse<>();

    if (Objects.equals(sortBy, "rate")) {
      List<Facility> sortedFacilities = facilities.stream()
          .sorted((a, b) -> Double.compare(b.getAvagerstar(), a.getAvagerstar()))
          .toList();
      facilityListResponse.setItems(sortedFacilities);
      facilityListResponse.setTotals(sortedFacilities.size());

    } else {
      facilityListResponse.setItems(facilities);
      facilityListResponse.setTotals(facilities.size());
    }

    return new ResponseEntity<>(facilityListResponse, HttpStatus.OK);

  }

  @GetMapping("/owner/{ownerId}")
  public ResponseEntity<?> getFacilitesByOwnerId(@PathVariable ObjectId ownerId) {
    List<Facility> facilities = facilityService.getFacilitiesByOwnerId(ownerId);
    return new ResponseEntity<>(facilities, HttpStatus.OK);
  }

  @GetMapping("/statistic/subscription")
  public ResponseEntity<?> getSubscriptionsCountByFacilityOwnedByUser() {
    Map<String, Long> facilitySubscriptionCount = facilityService.getSubscriptionsCountByFacilityOwnedByUser();
    return new ResponseEntity<>(facilitySubscriptionCount, HttpStatus.OK);
  }


  @GetMapping("/{facilityId}")
  public ResponseEntity<Facility> getFacilityById(@PathVariable ObjectId facilityId) {
    Optional<Facility> facility = facilityService.getFacilityById(facilityId);
    return facility.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/delete/{facilityId}")
  public ResponseEntity<Facility> deleteFacilityById(@PathVariable ObjectId facilityId) {
    Optional<Facility> facility = facilityService.deleteFacilityById(facilityId);
    return facility.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
  }

  @PostMapping("/create")
  public ResponseEntity<Facility> createFacility(@RequestBody FacilityCreateDto facilityCreateDto) {
    Facility facility = facilityService.createFacility(facilityCreateDto);
    return new ResponseEntity<>(facility, HttpStatus.OK);
  }

}
