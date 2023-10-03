package shop;

import controllers.*;
import manager.AuthManager;
import manager.OrderManager;
import manager.ShopManager;
import utility.stats.OperationState;
import utility.view.DefaultView;

import java.util.Scanner;

public class Frontend {
  Scanner scan = new Scanner(System.in);
  DefaultView view;

  ShopManager shopManager;
  AuthManager authObject;
  public Frontend(ShopManager shopManager, AuthManager auth) {
    this.shopManager = shopManager;
    this.authObject = auth;

    view = new DefaultView(shopManager);
  }

  public void menu(){
    boolean run = true;
    while (run) {
      view.printFrontendMenu();
      // select menu
      String choice = scan.nextLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> viewProduct();
        case "2" -> getOrderHistory();
        case "3" -> {
          // check text in printFrontendMenu()
          if (!shopManager.getIsCustomerLogin()) {
            authObject.customerLogin();
          } else {
            CustomerController customerObject = new CustomerController(shopManager);
            customerObject.viewProfileByCustomerId(shopManager.getLoginCustomerId());
          }
        }
        case "4" -> authObject.setLogout();
        case "Q" ->  run = false;
        default -> view.printFrontendMenuWarning();
      }

    }
  }

  public void authenticationMenu(){

    boolean run = true;
    while (run) {
      view.printRegisterOrLoginMenu();
      // select menu
      String choice = scan.nextLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> {
          authObject.customerLogin();
        }
        case "2" -> {
          CustomerController customer = new CustomerController(shopManager);
          customer.register();
        }
        case "Q" ->  run = false;
        default -> view.printFrontendMenuWarning();
      }

      // exit loop if login success
      if (shopManager.getIsCustomerLogin()) run = false;

    }
  }

  private void viewProduct(){
    ProductController productObject = new ProductController(shopManager);
    productObject.frontendProductList();
  }

  private void getOrderHistory(){
    if (!shopManager.isHasCustomerPermission()) {
      authObject.doLogin();
      return;
    }
    OrderManager orderManager = new OrderManager(shopManager);

    while (true) {
      orderManager.orderHistory(shopManager.getLoginCustomerId());

      view.printViewOrderMenu();
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = orderManager.searchByInputNumber(inputString);

      if (itemIndex == -1) {
        view.printNotFound();
        return;
      }
      orderManager.getOrderDetail(itemIndex);
    }

  }

}
