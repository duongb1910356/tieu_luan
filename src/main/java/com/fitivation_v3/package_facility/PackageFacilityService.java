package com.fitivation_v3.package_facility;

import com.fitivation_v3.exception.BadRequestException;
import com.fitivation_v3.facility.Facility;
import com.fitivation_v3.facility.FacilityRepository;
import com.fitivation_v3.package_facility.dto.CreatePackageFacilityDto;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackageFacilityService {

  @Autowired
  private PackageFacilityRepository packageFacilityRepository;

  @Autowired
  private FacilityRepository facilityRepository;

  public List<PackageFacility> createPackageFacility(
      CreatePackageFacilityDto createPackageFacility) {
    List<PackageFacility> packageFacilityList = new ArrayList<>();
    try {
      String packageName = createPackageFacility.getName();
      int basePrice = createPackageFacility.getBasePrice();
      Map<Integer, Float> discountMap = createPackageFacility.getDiscount();
      ObjectId facilityId = createPackageFacility.getFacilityId();
      Optional<Facility> facility = facilityRepository.findById(facilityId);

      for (Map.Entry<Integer, Float> entry : discountMap.entrySet()) {
        int type = entry.getKey();
        float discount = entry.getValue();

        PackageFacility packageFacility = new PackageFacility();
        packageFacility.setName(packageName);
        packageFacility.setBasePrice(basePrice);
        packageFacility.setType(type);
        packageFacility.setDiscount(discount);
        facility.ifPresent(packageFacility::setFacility);

        packageFacilityRepository.save(packageFacility);

        packageFacilityList.add(packageFacilityRepository.save(packageFacility));
      }

      return packageFacilityList;
    } catch (Exception ex) {
      System.out.println("Fail create package: " + ex);
      throw new BadRequestException("Fail create package");
    }
  }

  public List<PackageFacility> getListPackageFacility(ObjectId facilityId) {
    try {
      Optional<Facility> facility = facilityRepository.findById(facilityId);
      List<PackageFacility> packageFacilities = new ArrayList<>();
      if (facility.isPresent()) {
        packageFacilities = packageFacilityRepository.findByFacility(facility.get());
        packageFacilities.sort(Comparator.comparing(PackageFacility::getType));
      }
      return packageFacilities;
    } catch (Exception ex) {
      throw new BadRequestException("Can't get package of facility");
    }
  }
}
