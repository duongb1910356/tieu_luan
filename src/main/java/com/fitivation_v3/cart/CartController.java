package com.fitivation_v3.cart;

import com.fitivation_v3.cart_item.dto.ItemDto;
import jakarta.websocket.server.PathParam;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

  @Autowired
  private CartService cartService;

  @GetMapping("/me")
  public ResponseEntity<?> getCartOfMe() {
    Cart cart = cartService.getCartOfMe();
    return new ResponseEntity<>(cart, HttpStatus.OK);
  }

  @PatchMapping("/add_item")
  public ResponseEntity<?> addItemToCart(@RequestBody ItemDto itemDto) {
    Cart cart = cartService.addItemToCart(itemDto);
    return new ResponseEntity<>(cart, HttpStatus.OK);
  }

  @PatchMapping("/delete_item/{itemId}")
  public ResponseEntity<?> deleteItemFromCart(@PathVariable ObjectId itemId) {
    Cart cart = cartService.deleteItemFromCart(itemId);
    return new ResponseEntity<>(cart, HttpStatus.OK);
  }
}
