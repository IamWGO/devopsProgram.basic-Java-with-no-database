package dataclass;

public class OrderItem {
  int orderItemId;
  int orderId;
  int productId;
  int amount;
  double fullPrice;
  double salePrice;

  public OrderItem(int orderItemId, int orderId, int productId, int amount, double fullPrice, double salePrice) {
    this.orderItemId = orderItemId;
    this.orderId = orderId;
    this.productId = productId;
    this.amount = amount;
    this.fullPrice = fullPrice;
    this.salePrice = salePrice;
  }

  public int getOrderItemId() {
    return orderItemId;
  }

  public int getProductId() {
    return productId;
  }

  public int getOrderId() {
    return orderId;
  }

  public int getAmount() {
    return amount;
  }

  public String getFullPriceString() {
    return String.format("%.2f", fullPrice);
  }

  public String getSalePriceString() {
    return String.format("%.2f", salePrice);
  }

  public double getToPay() {
    return salePrice * amount;
  }

  public double getSalePrice() {
    return salePrice;
  }

  public void setOrderItemId(int orderItemId) {
    this.orderItemId = orderItemId;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }
  public String objectToLineFormat(){
    return  orderItemId + "," + orderId + "," + productId + ","+ amount + ","
            + String.format("%.2f", fullPrice) + ","+ String.format("%.2f", salePrice)
            ;
  }
}