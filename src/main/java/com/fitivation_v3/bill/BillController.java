package com.fitivation_v3.bill;

import com.fitivation_v3.bill.dto.CreateBillDto;
import com.fitivation_v3.exception.BadRequestException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/bill")
public class BillController {

  @Autowired
  private BillService billService;

  @PostMapping("/create")
  public ResponseEntity<?> createBill(@RequestBody CreateBillDto createBillDto) {
    Bill bill = billService.createBill(createBillDto);
    return new ResponseEntity<>(bill, HttpStatus.OK);
  }

  @GetMapping("/me")
  public ResponseEntity<?> getBillOfMe() {
    try {
      List<Bill> bills = billService.getBillOfMe();
      return new ResponseEntity<>(bills, HttpStatus.OK);

    } catch (Exception ex) {
      System.out.println("Error get bills: " + ex);
      throw new BadRequestException("Error get bills");
    }
  }

  @GetMapping("/satisfied/bills")
  public ResponseEntity<?> satisfiedBills() {
    try {
      Map<String, Double> revenueByMonth = billService.satisfiedBill();
      return new ResponseEntity<>(revenueByMonth, HttpStatus.OK);

    } catch (Exception ex) {
      System.out.println("Error revenue bills: " + ex);
      throw new BadRequestException("Error revenue bills");
    }
  }

}
