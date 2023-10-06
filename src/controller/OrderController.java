package controller;

import dataclass.Order;
import manager.OrderManager;
import manager.ShopManager;
import utility.stats.MainState;
import utility.stats.OperationState;
import view.OrderView;
import view.ProductView;

import java.util.ArrayList;
import java.util.Scanner;

public class OrderController {
  Scanner scan = new Scanner(System.in);
  OrderView outPut = new OrderView();
  ShopManager shopManager;
  OrderManager orderManager;

  public OrderController(ShopManager shopManager) {
    this.shopManager = shopManager;
    orderManager = new OrderManager(shopManager);
  }

  public void menu() {
    if (!shopManager.isHasAdminPermission()) return;

    boolean run = true;
    while (run) {
      outPut.printBackendMenu();
      // select menu
      String choice = scan.nextLine();
      outPut.printEmptyLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> listAll();
        case "2" -> OrderDetail();
        case "3" -> update();
        case "4" -> delete();
        case "Q" -> run = false; // quit while loop
        default -> outPut.printMenuWarning();
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
      outPut.printEmptyItem();
      return;
    }

    orderManager.showAllItems();

    outPut.printBackendOrderMenu(OperationState.SEARCH);
    // select menu
    String inputString = scan.nextLine();
    outPut.printEmptyLine();

    int itemIndex = orderManager.searchByInputNumber(inputString);
    if (itemIndex == -1){
      outPut.printNotFound();
      return;
    }

    orderManager.getOrderDetail(itemIndex);
  }


  private void update() {
    if (!shopManager.isHasAdminPermission()) return;

    shopManager.mainState = MainState.ORDER_ITEM;

    while (true) {
      listAll();

      outPut.printBackendOrderMenu(OperationState.DELETE);
      // select menu
      String inputString = scan.nextLine();
      outPut.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = orderManager.searchByInputNumber(inputString);

      if (itemIndex == -1){
        outPut.printNotFound();
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
      outPut.printEmptyItem();
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

      outPut.printBackendOrderMenu(OperationState.DELETE);
      // select menu
      String inputString = scan.nextLine();
      outPut.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = orderManager.searchByInputNumber(inputString);

      if (itemIndex == -1){
        outPut.printNotFound();
        return;
      }

      // remove order
      orderManager.deleteOrder(itemIndex);

    }
  }
}
