package controllers;

import dataclass.Order;
import dataclass.OrderItem;
import dataclass.Product;
import utility.stats.MainState;
import utility.stats.OperationState;
import utility.view.OrderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderController {
  Scanner scan = new Scanner(System.in);
  OrderView view = new OrderView();
  OrderItemController orderItemObject;

  MainController mainObject;

  public OrderController(MainController mainObject) {
    this.mainObject = mainObject;
    orderItemObject = new OrderItemController(mainObject);
  }

  public void menu() {
    if (!mainObject.isHasAdminPermission()) return;

    boolean run = true;
    while (run) {
      view.printBackendMenu();
      // select menu
      String choice = scan.nextLine();
      view.printEmptyLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> listAll();
        case "2" -> search();
        case "3" -> updateOrderItem();
        case "4" -> delete();
        case "Q" -> run = false; // quit while loop
        default -> view.printMenuWarning();
      }
    }
  }

  // ++ Frontend ++ ------------------------------------------------------------------------------
  public void addTempOrderItem(Product productItem, int amount){
    // add to tempOrderItem
    mainObject.tempOrderItems.add(new OrderItem(0,0, productItem.getProductId(), amount,
            productItem.getPrice(), productItem.getPrice()
    ));
  }
  // FrontEnd
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
    int orderId = getNextId();
    Order newItem = new Order(orderId, mainObject.loginCustomerId, "", true);
    String contentLine = newItem.objectToLineFormat();
    mainObject.orders.add(newItem);
    mainObject.addNewLine(contentLine);

    // save tempOrderItems to orderItem
    for (int i = 0; i < mainObject.tempOrderItems.size(); i++) {
      //update temporary orderId to real orderId
      mainObject.tempOrderItems.get(i).setOrderId(orderId);
      //add order item
      //- add new line to OrderItem.text.
      orderItemObject.addOrderItem(mainObject.tempOrderItems.get(i));
    }
    // Print order confirmed
    view.printOrderConfirmed();
  }

  // ++ Backend ++ ------------------------------------------------------------------------------
  private void listAll() {
    if (!mainObject.isHasBothPermission()) return;

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

  private void search() {
    if (!mainObject.isHasAdminPermission()) return;

    if (mainObject.products.isEmpty()) {
      view.printEmptyItem();
      return;
    }

    view.printBackendOrderMenu(OperationState.SEARCH);
    // select menu
    String inputString = scan.nextLine();
    view.printEmptyLine();

    int itemIndex = searchByInputNumber(inputString);
    if (itemIndex == -1){
      view.printNotFound();
      return;
    }

    getOrderDetail(itemIndex);

  }

  // Only pending order
  private void delete() {
    if (!mainObject.isHasAdminPermission()) return;

    if (mainObject.orders.isEmpty()) {
      view.printEmptyItem();
      return;
    }

    while (true) {
      // Get only pending order
      ArrayList<Order> orders = new ArrayList<>();
      for (int i = 0; i < mainObject.orders.size(); i++) {
        if (mainObject.orders.get(i).getIsPending())
          orders.add(mainObject.orders.get(i));
      }
      printOrderList(orders);

      view.printBackendOrderMenu(OperationState.DELETE);
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = searchByInputNumber(inputString);

      if (itemIndex == -1){
        view.printNotFound();
        return;
      }

      Order selectedItem = orders.get(itemIndex);
      // remove order
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
            orderItemObject.deleteOrderItem(i);
            break;
          }
        }
      }

      // Rewrite file because we remove an item form ArrayList
      rewriteFile();

      view.printUpdateResult();

    }
  }
  // ++ Both Backend and Frontend ++ ------------------------------------------------------------------------------
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

  private void printOrderList(ArrayList<Order> orders){
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

  private void getOrderDetail(int orderIndexId){
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
    ArrayList<OrderItem> orderItems = orderItemObject.getOrderItems(orderItem.getOrderId());
    orderItemObject.listOrderItems(orderItems);
  }

  public void listOrderItems(ArrayList<OrderItem> orderItems) {

    view.printOrderItemHeadLine();
    if (orderItems.isEmpty()) {
      view.printNotFound();
      return;
    }

    for (int i = 0; i < orderItems.size(); i++) {
      String productName = orderItemObject.getProductItemByProductId(orderItems.get(i).getProductId()).getProductName();
      view.printOrderItemRow(i, productName, orderItems.get(i));
    }
  }

  // ++ HelpFunction ++ ------------------------------------------------------------------------------
  private int searchOrderByIndex(int searchId){
    //search start from 0 ... n
    if (searchId < 0 || searchId > (mainObject.orders.size())) {
      return -1;
    }
    return searchId;
  }

  private void updateOrderItem() {
    if (!mainObject.isHasAdminPermission()) return;

    mainObject.mainState = MainState.ORDER_ITEM;

    while (true) {
      listAll();

      view.printBackendOrderMenu(OperationState.DELETE);
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = searchByInputNumber(inputString);

      if (itemIndex == -1){
        view.printNotFound();
        return;
      }

      mainObject.orders.get(itemIndex).setPending();
      rewriteFile();

      view.printUpdateResult();

    }

  }

  public String getCustomerName(int customerId){

    for (int i = 0; i < mainObject.customers.size(); i++) {
      if (mainObject.customers.get(i).getCustomerId() == customerId) {
        return mainObject.customers.get(i).getFullName();
      }
    }
    return "-";
  }

  private double getTotalPurchase(int orderId){
    double totalToPay = 0.0;
    for (int i = 0; i < mainObject.orderItems.size(); i++) {
      if (mainObject.orderItems.get(i).getOrderId() == orderId) {
        totalToPay = totalToPay + mainObject.orderItems.get(i).getToPay();
      }
    }
    return totalToPay;
  }
  private void rewriteFile(){
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

  private int getNextId(){
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

  private int searchByInputNumber(String inputString){
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


}
