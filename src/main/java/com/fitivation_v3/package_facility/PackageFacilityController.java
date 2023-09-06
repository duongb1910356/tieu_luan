package com.fitivation_v3.package_facility;

import com.fitivation_v3.package_facility.dto.CreatePackageFacilityDto;
import com.fitivation_v3.shared.ListResponse;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/package_facility")
public class PackageFacilityController {

  @Autowired
  private PackageFacilityService packageFacilityService;

  @PostMapping("/create")
  public ResponseEntity<?> createPackageFacility(
      @RequestBody CreatePackageFacilityDto createPackageFacilityDto) {
    List<PackageFacility> packageFacilityList = packageFacilityService.createPackageFacility(
        createPackageFacilityDto);
    System.out.println("Size: " + packageFacilityList.size());
    ListResponse<PackageFacility> packageFacilityListResponse = new ListResponse<>();
    packageFacilityListResponse.setItems(packageFacilityList);
    packageFacilityListResponse.setTotals(packageFacilityList.size());
    return new ResponseEntity<>(packageFacilityListResponse, HttpStatus.OK);
  }

  @GetMapping("/facility/{facilityId}")
  public ResponseEntity<?> getPackageFacility(@PathVariable ObjectId facilityId) {
    ListResponse<PackageFacility> packageFacilityListResponse = new ListResponse<>();
    List<PackageFacility> packageFacilities = packageFacilityService.getListPackageFacility(
        facilityId);

    packageFacilityListResponse.setItems(packageFacilities);
    packageFacilityListResponse.setTotals(packageFacilities.size());
    return new ResponseEntity<>(packageFacilities, HttpStatus.OK);
  }
}
