package controller;

import dataclass.Customer;
import manager.CustomerManager;
import manager.OrderManager;
import manager.ShopManager;
import utility.stats.MainState;
import utility.stats.OperationState;
import view.CustomerView;

import java.util.Scanner;

public class CustomerController {
  Scanner scan = new Scanner(System.in);
  CustomerView outPut = new CustomerView();
  ShopManager shopManager;
  CustomerManager customerManager;

  public CustomerController(ShopManager shopManager) {
    this.shopManager = shopManager;
    customerManager = new CustomerManager(shopManager);
  }

  public void menu() {
    if (!shopManager.isHasAdminPermission()) return;

    boolean run = true;
    while (run) {
      outPut.printBackendMenu();
      // select menu
      String inputString = scan.nextLine();
      outPut.printEmptyLine();

      // toUpperCase to check Q command
      switch (inputString.toUpperCase()) {
        case "1" -> listAll();
        case "2" -> search();
        case "3" -> viewCustomerOrder();
        case "4" -> updateStatus();
        case "5" -> delete();
        case "Q" -> run = false; // quit while loop
        default -> outPut.printMenuWarning();
      }
    }
  }

  private void listAll() {
    if (!shopManager.isHasAdminPermission()) return;

    if (shopManager.customers.isEmpty()) {
      outPut.printHeadLine();
      outPut.printNotFound();
      return;
    }

    customerManager.showCustomerList();
  }

  private void search() {
    if (!shopManager.isHasAdminPermission()) return;

    if (shopManager.customers.isEmpty()) {
      outPut.printEmptyItem();
      return;
    }

    while (true) {
      customerManager.showCustomerList();

      outPut.println("printCustomerMenu ::::");
      // select menu
      String inputString = scan.nextLine();
      outPut.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = customerManager.searchByInputNumber(inputString);
      viewProfileByItemIndex(itemIndex);
    }
  }

  private void viewProfileByItemIndex(int searchIndex){
    if (!shopManager.isHasBothPermission()) return;

    int itemIndex = customerManager.searchByItemIndex(searchIndex);

    if (itemIndex == -1){
      outPut.printNotFound();
      return;
    }
    // View Profile
    customerManager.showProfile(itemIndex);
  }


  private void viewCustomerOrder(){
    if (!shopManager.isHasAdminPermission()) return;

    while (true) {
      customerManager.showCustomerList();

      outPut.printCustomerMenu(OperationState.VIEW_ORDER);
      // select menu
      String inputString = scan.nextLine();
      outPut.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;

      // get customer
      int itemIndex = customerManager.searchByInputNumber(inputString);
      Customer selectedItem = shopManager.customers.get(itemIndex);
      // show Order
      OrderManager orderManager = new OrderManager(shopManager);
      orderManager.orderHistory(selectedItem.getCustomerId());
    }
  }

  private void updateStatus(){
    if (!shopManager.isHasAdminPermission()) return;

    if (shopManager.customers.isEmpty()) {
      outPut.printEmptyItem();
      return;
    }

    while (true) {
      customerManager.showCustomerList();
      outPut.printCustomerMenu(OperationState.UPDATE_STATUS);
      // select menu
      String inputString = scan.nextLine();
      outPut.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = customerManager.searchByInputNumber(inputString);

      // update customer
      customerManager.updateStatus(itemIndex);

    }
  }

  private void delete() {
    if (!shopManager.isHasAdminPermission()) return;

    if (shopManager.customers.isEmpty()) {
      outPut.printEmptyItem();
      return;
    }

    while (true) {
      customerManager.showCustomerList();

      outPut.printCustomerMenu(OperationState.DELETE);
      // select menu
      String inputString = scan.nextLine();
      outPut.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;

      //Delete
      int itemIndex = customerManager.searchByInputNumber(inputString);
      customerManager.deleteCustomer(itemIndex);
    }
  }

}
