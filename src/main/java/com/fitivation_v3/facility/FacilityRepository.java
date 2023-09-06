package com.fitivation_v3.facility;

import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends MongoRepository<Facility, ObjectId> {

  Optional<Facility> findByName(String facilityName);

  List<Facility> findByLocationNear(Point location, Distance distance);

  @Query("{'slugAddress': {$regex: ?0, $options: 'i'}}")
  List<Facility> searchBySlugAddress(String textSearch);
}
