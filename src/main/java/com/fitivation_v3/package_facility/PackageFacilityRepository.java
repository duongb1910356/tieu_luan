package com.fitivation_v3.package_facility;

import com.fitivation_v3.facility.Facility;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageFacilityRepository extends MongoRepository<PackageFacility, ObjectId> {

  public List<PackageFacility> findByFacility(Facility facility);
}
