package manager;

import dataclass.Order;
import dataclass.OrderItem;
import dataclass.Product;
import utility.stats.MainState;
import utility.view.OrderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderManager {
  Scanner scan = new Scanner(System.in);
  OrderView view = new OrderView();
  ShopManager mainObject;
  public OrderManager(ShopManager mainObject) {
    this.mainObject = mainObject;
  }

  // ++ Order ++ ------------------------------------------------------------------------------
  public void addTempOrderItem(Product productItem, int amount){
    // add to tempOrderItem
    mainObject.tempOrderItems.add(new OrderItem(0,0, productItem.getProductId(), amount,
            productItem.getPrice(), productItem.getPrice()
    ));
  }

  public void confirmOrder() {
    if (!mainObject.isHasCustomerPermission()) return;

    view.printText("\n Du you want to update profile? (y/n)  : ");

    String inputString = scan.nextLine();
    if (!inputString.equalsIgnoreCase("y")) {
      view.printCancel();
      return;
    }

    //set mainState to get filename
    mainObject.mainState = MainState.ORDER;
    // Add Order
    int orderId = getNextOrderId();
    Order newItem = new Order(orderId, mainObject.getLoginCustomerId(), "", true);
    String contentLine = newItem.objectToLineFormat();
    mainObject.orders.add(newItem);
    mainObject.addNewLine(contentLine);

    // save tempOrderItems to orderItem
    for (int i = 0; i < mainObject.tempOrderItems.size(); i++) {
      //update temporary orderId to real orderId
      mainObject.tempOrderItems.get(i).setOrderId(orderId);
      //add order item
      //- add new line to OrderItem.text.
      addOrderItem(mainObject.tempOrderItems.get(i));
    }
    // Print order confirmed
    view.printOrderConfirmed();
  }

  public void deleteOrder(int itemIndex){
    Order selectedItem = mainObject.orders.get(itemIndex);
    ArrayList<Order> tempOrders = mainObject.orders;
    for (int i = 0; i < mainObject.orders.size(); i++) {
      if (mainObject.orders.get(i).getOrderId() == selectedItem.getOrderId()) {
        tempOrders.remove(i);
        break;
      }
    }
    mainObject.orders = tempOrders;

    // delete orderItem
    List<OrderItem> indicesToRemove = new ArrayList<>();
    for (int i = 0; i < mainObject.orderItems.size(); i++) {
      if (mainObject.orderItems.get(i).getOrderId() == selectedItem.getOrderId()) {
        indicesToRemove.add(mainObject.orderItems.get(i));
      }
    }

    // Remove elements in reverse order to avoid index issues
    for (int i = indicesToRemove.size() - 1; i >= 0; i--) {
      OrderItem toRemoveItem = indicesToRemove.get(i);

      for (int j = 0; j < mainObject.orderItems.size(); j++) {
        if (toRemoveItem.equals(mainObject.orderItems.get(i))){
          deleteOrderItem(i);
          break;
        }
      }
    }

    // Rewrite file because we remove an item form ArrayList
    rewriteFile();

    view.printUpdateResult();
  }

  public void orderHistory(int customerId){
    //if (!mainObject.isHasBothPermission()) return;

    ArrayList<Order> orderHistory = new ArrayList<>();
    // get only order of login customer
    for (int i = 0; i < mainObject.orders.size(); i++) {
      if (mainObject.orders.get(i).getCustomerId() == customerId) {
        orderHistory.add(mainObject.orders.get(i));
      }
    }
    // Print Order List
    printOrderList(orderHistory);

  }

  public void showAllItems(){
    if (mainObject.getIsAdminLogin()) {
      // Print Order List
      printOrderList(mainObject.orders);
    } else {
      // Print only order of login customer.
      ArrayList<Order> orders = new ArrayList<>();
      for (int i = 0; i < mainObject.orders.size(); i++) {
        if ( mainObject.orders.get(i).getCustomerId() == mainObject.getLoginCustomerId())
          orders.add(mainObject.orders.get(i));
      }
      printOrderList(orders);
    }
  }

  public void printOrderList(ArrayList<Order> orders){
    if (!mainObject.isHasBothPermission()) return;

    view.printOrderHeadLine();
    if (orders.isEmpty()) {
      view.printNotFound();
      return;
    }

    for (int i = 0; i < orders.size(); i++) {
      String customerName = getCustomerName(orders.get(i).getCustomerId());
      double totalPurchase = getTotalPurchase(orders.get(i).getOrderId());
      view.printOrderRow(i, customerName, totalPurchase, orders.get(i));
      // get Order detail
      //getOrderDetail(i);
    }
  }

  public void getOrderDetail(int orderIndexId){
    if (!mainObject.isHasBothPermission()) return;

    int selectedIndex = searchOrderByIndex(orderIndexId);
    if (selectedIndex == -1){
      view.printNotFound();
      return;
    }

    // Get Order Detail
    Order orderItem = mainObject.orders.get(selectedIndex);
    String customerName  = getCustomerName(orderItem.getCustomerId());
    view.printOrderDetail(customerName , orderItem);

    // List order item
    ArrayList<OrderItem> orderItems = getOrderItems(orderItem.getOrderId());
    listOrderItems(orderItems);
  }

  public void updateOrderItem(int itemIndex){
    mainObject.orders.get(itemIndex).setPending();
    rewriteFile();

    view.printUpdateResult();
  }

  public int searchOrderByIndex(int searchId){
    //search start from 0 ... n
    if (searchId < 0 || searchId > (mainObject.orders.size())) {
      return -1;
    }
    return searchId;
  }



  public String getCustomerName(int customerId){

    for (int i = 0; i < mainObject.customers.size(); i++) {
      if (mainObject.customers.get(i).getCustomerId() == customerId) {
        return mainObject.customers.get(i).getFullName();
      }
    }
    return "-";
  }

  public double getTotalPurchase(int orderId){
    double totalToPay = 0.0;
    for (int i = 0; i < mainObject.orderItems.size(); i++) {
      if (mainObject.orderItems.get(i).getOrderId() == orderId) {
        totalToPay = totalToPay + mainObject.orderItems.get(i).getToPay();
      }
    }
    return totalToPay;
  }
  public void rewriteFile(){
    if (!mainObject.isHasBothPermission()) return;
    //set mainState to get filename
    mainObject.mainState = MainState.ORDER;
    // Rewrite file because we remove an item form ArrayList
    String contentLines = "";
    ArrayList<Order> tempOrder = new ArrayList<>();
    for (int i = 0; i < mainObject.orders.size(); i++) {
      contentLines = contentLines.concat(mainObject.orders.get(i).objectToLineFormat());
      if (i < mainObject.orders.size()-1) contentLines = contentLines.concat("\n");

      tempOrder.add(mainObject.orders.get(i));
    }

    mainObject.orders = tempOrder;
    mainObject.overwriteFile(contentLines);
  }

  public int getNextOrderId(){
    if (mainObject.orders.isEmpty()) return 1;

    int maxProductId = -1;

    for (Order order : mainObject.orders) {
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
      // if choice is not in range then return -1
      if (choice < 0 || choice > (mainObject.orders.size())) {
        return -1;
      }
      // return selected index
      return choice - 1;
    } catch (NumberFormatException ex) {
      // if user input string then return -1
      return  -1;
    }
  }

  // ++ OrderItem ++ ------------------------------------------------------------------------------
  public void addOrderItem(OrderItem newItem){
    if (!mainObject.isHasCustomerPermission()) return;
    //set mainState to get filename
    mainObject.mainState = MainState.ORDER_ITEM;

    int orderItemId = getNextOrderItemId();
    newItem.setOrderItemId(orderItemId);

    String contentLine = newItem.objectToLineFormat();
    mainObject.orderItems.add(newItem);
    mainObject.addNewLine(contentLine);

  }

  public ArrayList<OrderItem> getOrderItems(int orderId){
    ArrayList<OrderItem> orderItems = new ArrayList<>();
    for (int i = 0; i < mainObject.orderItems.size(); i++) {
      if (mainObject.orderItems.get(i).getOrderId() == orderId) {
        orderItems.add(mainObject.orderItems.get(i));
      }
    }

    return orderItems;
  }

  public void listOrderItems(ArrayList<OrderItem> orderItems) {
    view.printOrderItemHeadLine();
    if (orderItems.isEmpty()) {
      view.printNotFound();
    } else {
      for (int i = 0; i < orderItems.size(); i++) {
        // Get product Name
        String productName = getProductItemByProductId(orderItems.get(i).getProductId()).getProductName();
        view.printOrderItemRow(i,productName, orderItems.get(i));
      }
    }
    view.printOrderFooter();
  }

  public Product getProductItemByProductId(int productId){

    for (int i = 0; i < mainObject.products.size(); i++) {
      if (mainObject.products.get(i).getProductId() == productId) {
        return mainObject.products.get(i);
      }
    }
    return new Product();
  }

  public boolean deleteOrderItem(int ItemIndex){
    if (!mainObject.isHasAdminPermission()) return false;

    // will return -1 if no item
    int selectedIndex = searchOrderItemByIndex(ItemIndex);

    if (selectedIndex == -1){
      return false;
    }
    // get member info before delete
    mainObject.orderItems.remove(selectedIndex);

    // Rewrite file because we remove an item form ArrayList
    rewriteOrderItemFile();

    //Success delete
    return true;
  }



  // ++ Order ++ ------------------------------------------------------------------------------


  // ++ OrderItem Help Functions ++ ------------------------------------------------------------------------------


  public void rewriteOrderItemFile(){
    if (!mainObject.isHasBothPermission()) return;

    //set mainState to get filename
    mainObject.mainState = MainState.ORDER_ITEM;

    // Rewrite file because we remove an item form ArrayList
    String contentLines = "";
    ArrayList<OrderItem> tempOrderItem = new ArrayList<>();
    for (int i = 0; i < mainObject.orderItems.size(); i++) {
      contentLines = contentLines.concat(mainObject.orderItems.get(i).objectToLineFormat());
      if (i < mainObject.orderItems.size()-1) contentLines = contentLines.concat("\n");

      tempOrderItem.add(mainObject.orderItems.get(i));
    }

    mainObject.orderItems = tempOrderItem;
    mainObject.overwriteFile(contentLines);
  }
  private int searchOrderItemByIndex(int ItemIndex){
    //search start from 0 ... n
    if (ItemIndex < 0 || ItemIndex > (mainObject.products.size())) {
      return -1;
    }
    return ItemIndex;
  }

  private int searchOrderItemByIndex(String inputString){
    try {
      int choice = Integer.parseInt(inputString);
      // if choice is not in range then return -1
      if (choice < 0 || choice > (mainObject.orderItems.size())) {
        return -1;
      }
      // return selected index
      return choice - 1;
    } catch (NumberFormatException ex) {
      // if user input string then return -1
      return  -1;
    }
  }
  public int getNextOrderItemId(){

    if (mainObject.orderItems.isEmpty()) return 1;

    int maxProductId = -1;

    for (OrderItem orderItem : mainObject.orderItems) {
      if (orderItem.getOrderItemId() > maxProductId) {
        maxProductId = orderItem.getOrderItemId();
      }
    }
    maxProductId++;
    return maxProductId;
  }
}
