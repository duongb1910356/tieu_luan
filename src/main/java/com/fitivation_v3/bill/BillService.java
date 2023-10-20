package com.fitivation_v3.bill;

import com.fitivation_v3.bill.dto.CreateBillDto;
import com.fitivation_v3.cart.Cart;
import com.fitivation_v3.exception.BadRequestException;
import com.fitivation_v3.security.service.UserDetailsImpl;
import com.fitivation_v3.subscription.SubscriptionService;
import com.fitivation_v3.user.User;
import com.stripe.model.Transfer;
import com.stripe.param.TransferCreateParams;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BillService {

  @Autowired
  private BillRepository billRepository;

  @Autowired
  private SubscriptionService subscriptionService;

  @Autowired
  private ModelMapper mapper;

  public Bill createBill(CreateBillDto createBillDto) {
    try {
      Bill bill = mapper.map(createBillDto, Bill.class);
      return billRepository.save(bill);
    } catch (Exception ex) {
      throw new BadRequestException("Can not bill created");
    }
  }

  public Bill createBillFromCart(Cart cart, String paymentIntentId, User user) {
    try {

      Bill bill = Bill.createBillFromCart(cart, paymentIntentId, user);
      return billRepository.save(bill);
    } catch (Exception ex) {
      System.out.println("Can not bill created: " + ex);
      throw new BadRequestException("Can not bill created");
    }
  }

  public Bill completeBillPayment(String paymentIntentId, User user) {
    try {

      Bill bill = billRepository.findByPaymentIntent(paymentIntentId);
      bill.setStatus(true);

      subscriptionService.createSubscription(bill, user);

      return billRepository.save(bill);
    } catch (Exception ex) {
      System.out.println("Can not completed bill: " + ex);
      throw new BadRequestException("Can not completed bill");
    }
  }

  public List<Bill> getBillOfMe() {
    try {
      UserDetailsImpl userDetails =
          (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      User user = mapper.map(userDetails, User.class);

      List<Bill> billList = billRepository.findByCustomerIdStripe(user.getCustomerIdStripe());
      return billList;
    } catch (Exception ex) {
      System.out.println("Can not get list bills: " + ex);
      throw new BadRequestException("Can not get list bills");
    }
  }

  public Map<String, Double> satisfiedBill() {
    try {
      UserDetailsImpl userDetails =
          (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      User user = mapper.map(userDetails, User.class);

      Map<String, Double> revenueByMonth = new HashMap<>();
      Calendar calendar = Calendar.getInstance();

      for (int i = 0; i < 6; i++) {
        Date endDate = calendar.getTime();
        calendar.add(Calendar.MONDAY, -1);
        Date startDate = calendar.getTime();

        List<Bill> bills = billRepository.findByOwnerIdAndDateCreatedBetween(user.getId(),
            startDate, endDate);
        double totalRevenue = bills.stream().mapToDouble(Bill::getTotalPrice).sum();
        totalRevenue = totalRevenue / 1000000; //Làm tròn thành triệu

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        String monthKey = dateFormat.format(startDate);
        revenueByMonth.put(monthKey, totalRevenue);
      }

      Map<String, Double> sortedRevenueByMonth = new TreeMap<>(revenueByMonth);

      return sortedRevenueByMonth;

    } catch (Exception ex) {
      System.out.println("Can not satisfied bills: " + ex);
      throw new BadRequestException("Can not satisfied bills");
    }
  }
}
