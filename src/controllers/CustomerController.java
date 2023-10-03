package controllers;

import dataclass.Customer;
import manager.CustomerManager;
import manager.OrderManager;
import manager.ShopManager;
import utility.stats.MainState;
import utility.stats.OperationState;
import utility.view.CustomerView;

import java.util.ArrayList;
import java.util.Scanner;

public class CustomerController {
  Scanner scan = new Scanner(System.in);
  CustomerView view = new CustomerView();

  ShopManager mainObject;
  CustomerManager customerManager;
  public CustomerController(ShopManager mainObject) {
    this.mainObject = mainObject;
    this.customerManager = new CustomerManager(mainObject);
  }

  public void menu() {
    if (!mainObject.isHasAdminPermission()) return;

    boolean run = true;
    while (run) {
      view.printBackendMenu();
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      // toUpperCase to check Q command
      switch (inputString.toUpperCase()) {
        case "1" -> listAll();
        case "2" -> search();
        case "3" -> viewCustomerOrder();
        case "4" -> updateStatus();
        case "5" -> delete();
        case "Q" -> run = false; // quit while loop
        default -> view.printMenuWarning();
      }
    }
  }

  private void listAll() {
    if (!mainObject.isHasAdminPermission()) return;

    if (mainObject.customers.isEmpty()) {
      view.printHeadLine();
      view.printNotFound();
      return;
    }

    customerManager.showCustomerList();
  }

  private void search() {
    if (!mainObject.isHasAdminPermission()) return;

    if (mainObject.customers.isEmpty()) {
      view.printEmptyItem();
      return;
    }

    while (true) {
      customerManager.showCustomerList();

      view.printCustomerMenu(OperationState.SEARCH);
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = customerManager.searchByInputNumber(inputString);
      viewProfileByItemIndex(itemIndex);
    }
  }

  private void viewCustomerOrder(){
    if (!mainObject.isHasAdminPermission()) return;

    while (true) {
      customerManager.showCustomerList();

      view.printCustomerMenu(OperationState.VIEW_ORDER);
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;

      int itemIndex = customerManager.searchByInputNumber(inputString);
      Customer selectedItem = mainObject.customers.get(itemIndex);

      OrderManager orderManager = new OrderManager(mainObject);
      orderManager.orderHistory(selectedItem.getCustomerId());
    }
  }


  public void register(){
    //set mainState to get filename
    mainObject.mainState = MainState.CUSTOMER;
    Customer newItem = customerManager.newItemForm();

    view.printText("\n Confirm to update? (y/n)  : ");
    String inputString = scan.nextLine();

    if (!inputString.equalsIgnoreCase("y")) {
      view.printCancel();
      return;
    }

    // add new Item
    customerManager.addNewItem(newItem);
    // Set customer
    mainObject.loginForNewCustomer(newItem.getCustomerId());
  }

  private void updateForm(int indexItem){
    if (!mainObject.isHasBothPermission()) return;

    Customer selectedItem = mainObject.customers.get(indexItem);
    Customer updateItem = customerManager.updateProfile(selectedItem);

    view.printText("\n Confirm to update? (y/n)  : ");
    String inputString = scan.nextLine();

    if (!inputString.equalsIgnoreCase("y")) {
      view.printCancel();
      return;
    }

    // Update Customer
    customerManager.updateItem(selectedItem, updateItem);

  }

  //++ Backend ++ ------------------------------------------------------------------------------




  private void updateStatus(){
    if (!mainObject.isHasAdminPermission()) return;

    if (mainObject.customers.isEmpty()) {
      view.printEmptyItem();
      return;
    }

    while (true) {
      customerManager.showCustomerList();

      view.printCustomerMenu(OperationState.UPDATE_STATUS);
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = customerManager.searchByInputNumber(inputString);
      // update customer
      customerManager.updateStatus(itemIndex);

    }
  }

  private void delete() {
    if (!mainObject.isHasAdminPermission()) return;

    if (mainObject.customers.isEmpty()) {
      view.printEmptyItem();
      return;
    }

    while (true) {
      customerManager.showCustomerList();

      view.printCustomerMenu(OperationState.DELETE);
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;

      //Delete
      int itemIndex = customerManager.searchByInputNumber(inputString);
      customerManager.deleteCustomer(itemIndex);
    }
  }

  public void viewProfileByCustomerId(int customerId) {
    if (!mainObject.isHasBothPermission()) return;

    int itemIndex = customerManager.getCustomerIndexItemById(customerId);

    if (itemIndex == -1){
      view.printNotFound();
      return;
    }
    // View Profile
    showProfile(itemIndex);
  }

  private void viewProfileByItemIndex(int searchIndex){
    if (!mainObject.isHasBothPermission()) return;

    int itemIndex = customerManager.searchByItemIndex(searchIndex);

    if (itemIndex == -1){
      view.printNotFound();
      return;
    }
    // View Profile
    showProfile(itemIndex);
  }

  private void showProfile(int itemIndex){
    if (!mainObject.isHasBothPermission()) return;

    view.printDetail(mainObject.customers.get(itemIndex));
    view.printText("\n Confirm Order? (y/n)  : ");

    String inputString = scan.nextLine();
    if (!inputString.equalsIgnoreCase("y")) {
      view.printCancel();
      return;
    }

    updateForm(itemIndex);
  }
}
