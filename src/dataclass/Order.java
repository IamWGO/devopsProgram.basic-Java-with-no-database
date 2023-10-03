package dataclass;

import service.FormatService;

import java.time.LocalDateTime;

public class Order {
  int orderId;
  int customerId;
  String remark;
  boolean isPending;
  LocalDateTime orderDate;
  LocalDateTime completeDate;

  public Order(int orderId, int customerId, String remark, boolean isPending) {
    this.orderId = orderId;
    this.customerId = customerId;
    this.remark = remark;
    this.isPending = isPending;
    this.orderDate = LocalDateTime.now();
    this.completeDate = LocalDateTime.now();
  }

  public Order(int orderId, int customerId, String remark, boolean isPending, LocalDateTime orderDate, LocalDateTime completeDate) {
    this.orderId = orderId;
    this.customerId = customerId;
    this.remark = remark;
    this.isPending = isPending;
    this.orderDate = orderDate;
    this.completeDate = completeDate;
  }

  public boolean getIsPending() {
    return isPending;
  }

  public int getOrderId() {
    return orderId;
  }

  public String getOrderCode() {
    return String.format("%05d", orderId);
  }

  public String getIsPenddingString() {
    return isPending ? "Padding" : "Completed";
  }


  public int getCustomerId() {
    return customerId;
  }

  public String getIsPendingText(){
    return isPending ? "Pending" : "Completed";
  }

  public String getOrderDate() {
    return FormatService.localDateToString(orderDate);
  }

  public String getCompleteDate(){
    return FormatService.localDateToString(completeDate);
  }

  public String getRemark() {
    return remark;
  }


  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setPending() {
    isPending = !isPending;
  }

  public void setCompleteDate(LocalDateTime completeDate) {
    this.completeDate = completeDate;
  }

  public String objectToLineFormat(){
    return  orderId + "," + customerId + ","
            + (!remark.isEmpty() ? remark : "-") + "," + (isPending ? "1" : "0")+ ","
            + FormatService.localDateToString(orderDate) + ","
            + FormatService.localDateToString(completeDate)
            ;
  }

}
