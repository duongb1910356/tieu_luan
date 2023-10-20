package com.fitivation_v3.cart;

import com.fitivation_v3.cart_item.Item;
import com.fitivation_v3.cart_item.ItemRepository;
import com.fitivation_v3.cart_item.dto.ItemDto;
import com.fitivation_v3.exception.BadRequestException;
import com.fitivation_v3.facility.Facility;
import com.fitivation_v3.facility.FacilityService;
import com.fitivation_v3.package_facility.PackageFacility;
import com.fitivation_v3.package_facility.PackageFacilityRepository;
import com.fitivation_v3.security.service.UserDetailsImpl;
import com.fitivation_v3.user.User;
import java.util.ArrayList;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CartService {

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private PackageFacilityRepository packageFacilityRepository;

  @Autowired
  private FacilityService facilityService;

  @Autowired
  ModelMapper mapper;

  public Cart createCart() {
    UserDetailsImpl userDetails =
        (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = mapper.map(userDetails, User.class);
    Cart cart = Cart.builder().user(user).build();

    return cartRepository.save(cart);
  }


  public Cart getCartOfMe() {
    UserDetailsImpl userDetails =
        (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = mapper.map(userDetails, User.class);

    Cart cart = cartRepository.findCartByUser(user);

    for (Item item : cart.getItems()) {
      ObjectId facilityId = item.getPackageFacility().getFacility().getId();
      Optional<Facility> facility = facilityService.getFacilityById(facilityId);
      item.getPackageFacility().setFacility(facility.get());
    }

    cart.caculateTotalPrice();
    cart.caculateOriginPrice();

    return cart;
  }

  public Cart addItemToCart(ItemDto itemDto) {
    try {
      Optional<PackageFacility> packageFacility = packageFacilityRepository.findById(
          itemDto.getPackageId());
      Item item = new Item(packageFacility.get(), itemDto.getQuantity());
      item = itemRepository.save(item);

      Cart cart = this.deleteAllItemOfCart();
//      for (Item el : cart.getItems()) {
//        this.deleteItemFromCart(el.getId());
//      }
//      cart.setItems(new ArrayList<Item>());
      cart.addItemToCart(item);

      return cartRepository.save(cart);
    } catch (Exception ex) {
      System.out.println("Can't add item to cart: " + ex);
      throw new BadRequestException("Can't add item to cart");
    }
  }

  public Cart deleteItemFromCart(ObjectId itemId) {
    try {
      Cart cart = getCartOfMe();
      cart.deleteItemFromCart(itemId);
      cart.caculateOriginPrice();
      cart.caculateTotalPrice();

      return cartRepository.save(cart);
    } catch (Exception ex) {
      System.out.println("Can't add item to cart: " + ex);
      throw new BadRequestException("Can't delete item from cart");
    }
  }

  public Cart deleteAllItemOfCart() {
    try {
      Cart cartTemp = getCartOfMe();
      for (Item el : cartTemp.getItems()) {
        this.deleteItemFromCart(el.getId());
      }

      return getCartOfMe();
    } catch (Exception ex) {
      System.out.println("Can't add item to cart: " + ex);
      throw new BadRequestException("Can't delete item from cart");
    }
  }
}
