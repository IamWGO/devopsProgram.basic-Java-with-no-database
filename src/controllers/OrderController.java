package controllers;

import dataclass.Order;
import manager.OrderManager;
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

  ShopManager shopManager;
  OrderManager orderManager;

  public OrderController(ShopManager shopManager) {
    this.shopManager = shopManager;
    orderManager =  new OrderManager(shopManager);
  }

  public void menu() {
    if (!shopManager.isHasAdminPermission()) return;

    boolean run = true;
    while (run) {
      view.printBackendMenu();
      // select menu
      String choice = scan.nextLine();
      view.printEmptyLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> listAll();
        case "2" -> OrderDetail();
        case "3" -> update();
        case "4" -> delete();
        case "Q" -> run = false; // quit while loop
        default -> view.printMenuWarning();
      }
    }
  }

  private void listAll() {
    if (!shopManager.isHasBothPermission()) return;
    orderManager.showAllItems();
  }

  private void OrderDetail() {
    if (!shopManager.isHasAdminPermission()) return;

    if (shopManager.products.isEmpty()) {
      view.printEmptyItem();
      return;
    }

    orderManager.showAllItems();

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
    if (!shopManager.isHasAdminPermission()) return;

    shopManager.mainState = MainState.ORDER_ITEM;

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
    if (!shopManager.isHasAdminPermission()) return;

    if (shopManager.orders.isEmpty()) {
      view.printEmptyItem();
      return;
    }

    while (true) {
      // Get only pending order
      ArrayList<Order> orders = new ArrayList<>();
      for (int i = 0; i < shopManager.orders.size(); i++) {
        if (shopManager.orders.get(i).getIsPending())
          orders.add(shopManager.orders.get(i));
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
    if (!shopManager.isHasCustomerPermission()) {
      Frontend frontend = new Frontend(shopManager, shopManager.authObject);
      frontend.authenticationMenu();
      //mainObject.authObject.customerLogin();
      return;
    }

    if (shopManager.tempOrderItems.isEmpty()) {
      view.printEmptyItem();
      return;
    }

    // confirm order and clear temp order items
    orderManager.confirmOrder();
    shopManager.tempOrderItems = new ArrayList<>();

  }


}
