package manager;

import dataclass.Order;
import dataclass.OrderItem;
import dataclass.Product;
import utility.stats.MainState;
import view.OrderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderManager {
  Scanner scan = new Scanner(System.in);
  OrderView outPut = new OrderView();
  ShopManager shopManager;
  AuthManager authManager;
  public OrderManager(ShopManager shopManager) {
    this.shopManager = shopManager;
    this.authManager = new AuthManager(shopManager);
  }

  // ++ Order ++ ------------------------------------------------------------------------------
  public void addTempOrderItem(Product productItem, int amount){
    // add to tempOrderItem
    shopManager.tempOrderItems.add(new OrderItem(0,0, productItem.getProductId(), amount,
            productItem.getPrice(), productItem.getPrice()
    ));
  }

  public void confirmOrder() {
    if (!shopManager.isHasCustomerPermission()) return;

    outPut.print("\n Du you want to update profile? (y/n)  : ");

    String inputString = scan.nextLine();
    if (!inputString.equalsIgnoreCase("y")) {
      outPut.printCancel();
      return;
    }

    //set mainState to get filename
    shopManager.mainState = MainState.ORDER;
    // Add Order
    int orderId = getNextOrderId();
    Order newItem = new Order(orderId, shopManager.getLoginCustomerId(), "", true);
    String contentLine = newItem.objectToLineFormat();
    shopManager.orders.add(newItem);
    shopManager.addNewLine(contentLine);

    // save tempOrderItems to orderItem
    for (int i = 0; i < shopManager.tempOrderItems.size(); i++) {
      //update temporary orderId to real orderId
      shopManager.tempOrderItems.get(i).setOrderId(orderId);
      //add order item
      //- add new line to OrderItem.text.
      addOrderItem(shopManager.tempOrderItems.get(i));
    }

    // clear temp order
    shopManager.tempOrderItems = new ArrayList<>();
    // Print order confirmed
    outPut.printOrderConfirmed();
  }

  public void deleteOrder(int itemIndex){
    Order selectedItem = shopManager.orders.get(itemIndex);
    ArrayList<Order> tempOrders = shopManager.orders;
    for (int i = 0; i < shopManager.orders.size(); i++) {
      if (shopManager.orders.get(i).getOrderId() == selectedItem.getOrderId()) {
        tempOrders.remove(i);
        break;
      }
    }
    shopManager.orders = tempOrders;

    // delete orderItem
    List<OrderItem> indicesToRemove = new ArrayList<>();
    for (int i = 0; i < shopManager.orderItems.size(); i++) {
      if (shopManager.orderItems.get(i).getOrderId() == selectedItem.getOrderId()) {
        indicesToRemove.add(shopManager.orderItems.get(i));
      }
    }

    // Remove elements in reverse order to avoid index issues
    for (int i = indicesToRemove.size() - 1; i >= 0; i--) {
      OrderItem toRemoveItem = indicesToRemove.get(i);

      for (int j = 0; j < shopManager.orderItems.size(); j++) {
        if (toRemoveItem.equals(shopManager.orderItems.get(i))){
          deleteOrderItem(i);
          break;
        }
      }
    }

    // Rewrite file because we remove an item form ArrayList
    rewriteFile();

    outPut.printUpdatedResult();
  }

  public void orderHistory(int customerId){
    //if (!mainObject.isHasBothPermission()) return;

    ArrayList<Order> orderHistory = new ArrayList<>();
    // get only order of login customer
    for (int i = 0; i < shopManager.orders.size(); i++) {
      if (shopManager.orders.get(i).getCustomerId() == customerId) {
        orderHistory.add(shopManager.orders.get(i));
      }
    }
    // Print Order List
    printOrderList(orderHistory);

  }

  public void showAllItems(){
    if (shopManager.getIsAdminLogin()) {
      // Print Order List
      printOrderList(shopManager.orders);
    } else {
      // Print only order of login customer.
      ArrayList<Order> orders = new ArrayList<>();
      for (int i = 0; i < shopManager.orders.size(); i++) {
        if ( shopManager.orders.get(i).getCustomerId() == shopManager.getLoginCustomerId())
          orders.add(shopManager.orders.get(i));
      }
      printOrderList(orders);
    }
  }

  public void printOrderList(ArrayList<Order> orders){
    if (!shopManager.isHasBothPermission()) return;

    outPut.printOrderHeadLine();
    if (orders.isEmpty()) {
      outPut.printNotFound();
      return;
    }

    for (int i = 0; i < orders.size(); i++) {
      String customerName = getCustomerName(orders.get(i).getCustomerId());
      double totalPurchase = getTotalPurchase(orders.get(i).getOrderId());
      outPut.printOrderRow(i, customerName, totalPurchase, orders.get(i));
      // get Order detail
      //getOrderDetail(i);
    }
  }

  public void getOrderDetail(int orderIndexId){
    if (!shopManager.isHasBothPermission()) return;

    int selectedIndex = searchOrderByIndex(orderIndexId);
    if (selectedIndex == -1){
      outPut.printNotFound();
      return;
    }

    // Get Order Detail
    Order orderItem = shopManager.orders.get(selectedIndex);
    String customerName  = getCustomerName(orderItem.getCustomerId());
    outPut.printOrderDetail(customerName , orderItem);

    // List order item
    ArrayList<OrderItem> orderItems = getOrderItems(orderItem.getOrderId());
    listOrderItems(orderItems);
  }

  public void updateOrderItem(int itemIndex){
    shopManager.orders.get(itemIndex).setPending();
    rewriteFile();

    outPut.printUpdatedResult();
  }

  public int searchOrderByIndex(int searchId){
    //search start from 0 ... n
    if (searchId < 0 || searchId > (shopManager.orders.size())) {
      return -1;
    }
    return searchId;
  }



  public String getCustomerName(int customerId){

    for (int i = 0; i < shopManager.customers.size(); i++) {
      if (shopManager.customers.get(i).getCustomerId() == customerId) {
        return shopManager.customers.get(i).getFullName();
      }
    }
    return "-";
  }

  public double getTotalPurchase(int orderId){
    double totalToPay = 0.0;
    for (int i = 0; i < shopManager.orderItems.size(); i++) {
      if (shopManager.orderItems.get(i).getOrderId() == orderId) {
        totalToPay = totalToPay + shopManager.orderItems.get(i).getToPay();
      }
    }
    return totalToPay;
  }
  public void rewriteFile(){
    if (!shopManager.isHasBothPermission()) return;
    //set mainState to get filename
    shopManager.mainState = MainState.ORDER;
    // Rewrite file because we remove an item form ArrayList
    String contentLines = "";
    ArrayList<Order> tempOrder = new ArrayList<>();
    for (int i = 0; i < shopManager.orders.size(); i++) {
      contentLines = contentLines.concat(shopManager.orders.get(i).objectToLineFormat());
      if (i < shopManager.orders.size()-1) contentLines = contentLines.concat("\n");

      tempOrder.add(shopManager.orders.get(i));
    }

    shopManager.orders = tempOrder;
    shopManager.overwriteFile(contentLines);
  }

  public int getNextOrderId(){
    if (shopManager.orders.isEmpty()) return 1;

    int maxProductId = -1;

    for (Order order : shopManager.orders) {
      if (order.getOrderId() > maxProductId) {
        maxProductId = order.getOrderId();
      }
    }
    maxProductId++;
    return maxProductId;
  }

  public int searchByInputNumber(String inputString){
    try {
      int choice = Integer.parseInt(inputString);
      return (choice < 0 || choice > (shopManager.orders.size())) ? -1 : choice - 1;
    } catch (NumberFormatException ex) {
      // if user input string then return -1
      return  -1;
    }
  }

  // ++ OrderItem ++ ------------------------------------------------------------------------------
  public void addOrderItem(OrderItem newItem){
    if (!shopManager.isHasCustomerPermission()) return;
    //set mainState to get filename
    shopManager.mainState = MainState.ORDER_ITEM;

    int orderItemId = getNextOrderItemId();
    newItem.setOrderItemId(orderItemId);

    String contentLine = newItem.objectToLineFormat();
    shopManager.orderItems.add(newItem);
    shopManager.addNewLine(contentLine);

  }

  public ArrayList<OrderItem> getOrderItems(int orderId){
    ArrayList<OrderItem> orderItems = new ArrayList<>();
    for (int i = 0; i < shopManager.orderItems.size(); i++) {
      if (shopManager.orderItems.get(i).getOrderId() == orderId) {
        orderItems.add(shopManager.orderItems.get(i));
      }
    }

    return orderItems;
  }

  public void listOrderItems(ArrayList<OrderItem> orderItems) {
    outPut.printOrderItemHeadLine();
    if (orderItems.isEmpty()) {
      outPut.printNotFound();
    } else {
      for (int i = 0; i < orderItems.size(); i++) {
        // Get product Name
        String productName = getProductItemByProductId(orderItems.get(i).getProductId()).getProductName();
        outPut.printOrderItemRow(i,productName, orderItems.get(i));
      }
    }
    outPut.printEndOfList();
  }

  public Product getProductItemByProductId(int productId){

    for (int i = 0; i < shopManager.products.size(); i++) {
      if (shopManager.products.get(i).getProductId() == productId) {
        return shopManager.products.get(i);
      }
    }
    return new Product();
  }

  public boolean deleteOrderItem(int ItemIndex){
    if (!shopManager.isHasAdminPermission()) return false;

    // will return -1 if no item
    int selectedIndex = searchOrderItemByIndex(ItemIndex);

    if (selectedIndex == -1){
      return false;
    }
    // get member info before delete
    shopManager.orderItems.remove(selectedIndex);

    // Rewrite file because we remove an item form ArrayList
    rewriteOrderItemFile();

    //Success delete
    return true;
  }

  // ++ OrderItem Help Functions ++ ------------------------------------------------------------------------------

  public void rewriteOrderItemFile(){
    if (!shopManager.isHasBothPermission()) return;

    //set mainState to get filename
    shopManager.mainState = MainState.ORDER_ITEM;

    // Rewrite file because we remove an item form ArrayList
    String contentLines = "";
    ArrayList<OrderItem> tempOrderItem = new ArrayList<>();
    for (int i = 0; i < shopManager.orderItems.size(); i++) {
      contentLines = contentLines.concat(shopManager.orderItems.get(i).objectToLineFormat());
      if (i < shopManager.orderItems.size()-1) contentLines = contentLines.concat("\n");

      tempOrderItem.add(shopManager.orderItems.get(i));
    }

    shopManager.orderItems = tempOrderItem;
    shopManager.overwriteFile(contentLines);
  }
  private int searchOrderItemByIndex(int ItemIndex){
    //search start from 0 ... n
    if (ItemIndex < 0 || ItemIndex > (shopManager.products.size())) {
      return -1;
    }
    return ItemIndex;
  }

  public int getNextOrderItemId(){

    if (shopManager.orderItems.isEmpty()) return 1;

    int maxProductId = -1;

    for (OrderItem orderItem : shopManager.orderItems) {
      if (orderItem.getOrderItemId() > maxProductId) {
        maxProductId = orderItem.getOrderItemId();
      }
    }
    maxProductId++;
    return maxProductId;
  }
}
