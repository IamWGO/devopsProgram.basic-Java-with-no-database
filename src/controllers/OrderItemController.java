package controllers;

import dataclass.OrderItem;
import dataclass.Product;
import utility.stats.MainState;
import utility.view.OrderView;

import java.util.ArrayList;

public class OrderItemController {
  OrderView view = new OrderView();
  MainController mainObject;
  public OrderItemController(MainController mainObject) {
    this.mainObject = mainObject;
  }

  // ++ Frontend ++ ------------------------------------------------------------------------------
  public boolean updateOrderItem(int orderItemId, int amount){
    if (!mainObject.isHasBothPermission()) return false;

    // will return -1 if no item
    int selectedIndex = searchByIndex(orderItemId);

    if (selectedIndex == -1){
      return false;
    }

    mainObject.orderItems.get(selectedIndex).setAmount(amount);
    // Rewrite file because we remove an item form ArrayList
    rewriteFile();
    return true;
  }

  public boolean deleteOrderItem(int ItemIndex){
    if (!mainObject.isHasAdminPermission()) return false;

    // will return -1 if no item
    int selectedIndex = searchByIndex(ItemIndex);

    if (selectedIndex == -1){
      return false;
    }
    // get member info before delete
    mainObject.orderItems.remove(selectedIndex);

    // Rewrite file because we remove an item form ArrayList
    rewriteFile();

    //Success delete
    return true;
  }

  public void addOrderItem(OrderItem newItem){
    if (!mainObject.isHasCustomerPermission()) return;
    //set mainState to get filename
    mainObject.mainState = MainState.ORDER_ITEM;

    int orderItemId = getNextId();
    newItem.setOrderItemId(orderItemId);

    String contentLine = newItem.objectToLineFormat();
    mainObject.orderItems.add(newItem);
    mainObject.addNewLine(contentLine);

  }

  // ++ Both Backend and Frontend ++ ------------------------------------------------------------------------------
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
  // ++ HelpFunction ++ ------------------------------------------------------------------------------
  public void rewriteFile(){
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

  public int getNextId(){
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

  private int searchByIndex(int ItemIndex){
    //search start from 0 ... n
    if (ItemIndex < 0 || ItemIndex > (mainObject.products.size())) {
      return -1;
    }
    return ItemIndex;
  }

  private int searchByIndex(String inputString){
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
}
