package shop;

import controllers.*;
import manager.AuthManager;
import manager.OrderManager;
import manager.ShopManager;
import utility.view.DefaultView;

import java.util.Scanner;

public class Frontend {
  Scanner scan = new Scanner(System.in);
  DefaultView view;

  ShopManager mainObject;
  AuthManager authObject;
  public Frontend(ShopManager mainObject, AuthManager auth) {
    this.mainObject = mainObject;
    this.authObject = auth;

    view = new DefaultView(mainObject);
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
          if (!mainObject.getIsCustomerLogin()) {
            authObject.customerLogin();
          } else {
            CustomerController customerObject = new CustomerController(mainObject);
            customerObject.viewProfileByCustomerId(mainObject.getLoginCustomerId());
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
          CustomerController customer = new CustomerController(mainObject);
          customer.register();
        }
        case "Q" ->  run = false;
        default -> view.printFrontendMenuWarning();
      }

      // exit loop if login success
      if (mainObject.getIsCustomerLogin()) run = false;

    }
  }

  private void viewProduct(){
    ProductController productObject = new ProductController(mainObject);
    productObject.frontendProductList();
  }

  private void getOrderHistory(){
    if (mainObject.getIsCustomerLogin()) {
      OrderManager orderManager = new OrderManager(mainObject);
      orderManager.orderHistory(mainObject.getLoginCustomerId());
    } else {
      authenticationMenu();
    }
  }

}
