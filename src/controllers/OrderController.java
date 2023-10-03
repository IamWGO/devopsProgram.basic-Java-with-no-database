package controllers;

import dataclass.Order;
import manager.OrderManager;
import manager.ProductManager;
import manager.ShopManager;
import shop.Frontend;
import utility.stats.MainState;
import utility.stats.OperationState;
import utility.view.OrderView;

import java.util.ArrayList;
import java.util.Scanner;

public class OrderController {
  Scanner scan = new Scanner(System.in);
  OrderView view = new OrderView();

  ShopManager mainObject;
  OrderManager orderManager;
  ProductManager productManager;

  public OrderController(ShopManager mainObject) {
    this.mainObject = mainObject;
    orderManager =  new OrderManager(mainObject);
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
        case "3" -> update();
        case "4" -> delete();
        case "Q" -> run = false; // quit while loop
        default -> view.printMenuWarning();
      }
    }
  }

  private void listAll() {
    if (!mainObject.isHasBothPermission()) return;
    orderManager.showAllItems();
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

    int itemIndex = orderManager.searchByInputNumber(inputString);
    if (itemIndex == -1){
      view.printNotFound();
      return;
    }

    orderManager.getOrderDetail(itemIndex);
  }


  private void update() {
    if (!mainObject.isHasAdminPermission()) return;

    mainObject.mainState = MainState.ORDER_ITEM;

    while (true) {
      listAll();

      view.printBackendOrderMenu(OperationState.DELETE);
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = orderManager.searchByInputNumber(inputString);

      if (itemIndex == -1){
        view.printNotFound();
        return;
      }
      // Update item
      orderManager.updateOrderItem(itemIndex);

    }

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
      orderManager.printOrderList(orders);

      view.printBackendOrderMenu(OperationState.DELETE);
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = orderManager.searchByInputNumber(inputString);

      if (itemIndex == -1){
        view.printNotFound();
        return;
      }

      // remove order
      orderManager.deleteOrder(itemIndex);

    }
  }

  private void confirmOrder(){
    if (!mainObject.isHasCustomerPermission()) {
      Frontend frontend = new Frontend(mainObject, mainObject.authObject);
      frontend.authenticationMenu();
      //mainObject.authObject.customerLogin();
      return;
    }

    if (mainObject.tempOrderItems.isEmpty()) {
      view.printEmptyItem();
      return;
    }

    // confirm order and clear temp order items
    orderManager.confirmOrder();
    mainObject.tempOrderItems = new ArrayList<>();

  }


}
