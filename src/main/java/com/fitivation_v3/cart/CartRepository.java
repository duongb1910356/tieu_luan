package com.fitivation_v3.cart;

import com.fitivation_v3.user.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends MongoRepository<Cart, ObjectId> {

  public Cart findCartByUser(User user);
}
