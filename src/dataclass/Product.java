package dataclass;

public class Product {
  int productId;
  String productName;
  String productDetail;
  double price;
  boolean isSoldOut;
  boolean isActive;

  public Product(){}

  public Product(int productId, String productName,String productDetail, double price) {
    this.productId = productId;
    this.productName = productName;
    this.productDetail = productDetail;
    this.price = price;
    this.isSoldOut = false;
    this.isActive = true;
  }

  public Product(int productId, String productName,String productDetail, double price, boolean isSoldOut, boolean isActive) {
    this.productId = productId;
    this.productName = productName;
    this.productDetail = productDetail;
    this.price = price;
    this.isSoldOut = isSoldOut;
    this.isActive = isActive;
  }

  public void updateItem(Product updateItem) {
    this.productId = updateItem.productId;
    this.productName = updateItem.productName;
    this.productDetail = updateItem.productDetail;
    this.price = updateItem.price;
    this.isSoldOut = updateItem.isSoldOut;
    this.isActive = updateItem.isActive;
  }

  public int getProductId() {
    return productId;
  }

  public String getProductCode() {
    return String.format("P%05d", productId);
  }
  public String getPriceString() {
    return String.format("%.2f", price);
  }

  public String getProductName() {
    return productName;
  }

  public String getProductDetail() {
    return productDetail;
  }
  public String getIsInStockText(){
    return (isSoldOut) ? "Sold Out" : "In Stock";
  }

  public double getPrice() {
    return price;
  }

  public void setSoldOut() {
    isSoldOut = !isSoldOut;
  }

  public void setActive() {
    isActive = !isActive;
  }

  public void setProductInfo(String productName, Double price){
    this.productName = productName;
    this.price = price;
  }

  public String objectToLineFormat(){
    return  productId + "," + productName + ","+
            productDetail + ","+ price + ","
            + ((isSoldOut) ? "1" : "0") + "," + ((isActive) ? "1" : "0")
            ;
  }

}
