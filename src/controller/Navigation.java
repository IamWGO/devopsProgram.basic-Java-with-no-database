package controller;

import manager.*;
import view.NavigationView;

import java.util.Scanner;
public class Navigation {
  Scanner scan = new Scanner(System.in);
  NavigationView outPut = new NavigationView();
  ShopManager shopManager;
  AuthManager authManager;
  CustomerManager customerManager;
  ProductManager productManager;
  OrderManager orderManager;

  public Navigation(ShopManager shopManager) {
    this.shopManager = shopManager;
    this.authManager = new AuthManager(shopManager);
    this.customerManager = new CustomerManager(shopManager);
    this.productManager = new ProductManager(shopManager);
    this.orderManager = new OrderManager(shopManager);
  }


  //:::::::::::::::::::::::: Main Menu ::::::::::::::::::::::::
  public void mainMenu(){
    // get data from text files

    boolean run = true;
    while (run) {
      outPut.printMainMenu();
      // select menu
      String choice = scan.nextLine();
      outPut.printEmptyLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> {
          shopManager.setIsAdmin(true);
          authManager.doLogin();
          if (shopManager.getIsAdminLogin() && shopManager.getIsAdmin()) {
            this.backendMenu();
          }
        }
        case "2" -> {
          shopManager.setIsAdmin(false);
          this.frontendMenu();
        }
        case "Q" -> {
          outPut.println("Exit Program :)");
          run = false; // quit while loop
        }
        default -> outPut.println("Input a number or Q to exit program");
      }
    }

  }



  //:::::::::::::::::::::::: Backend Menu ::::::::::::::::::::::::
  public void backendMenu(){
    if (!shopManager.isHasAdminPermission()) return;

    boolean run = true;
    while (run) {
      if (!shopManager.getIsAdminLogin()) run = false;

      outPut.printBackendMenu();
      // select menu
      String choice = scan.nextLine();
      outPut.printEmptyLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> manageProduct();
        case "2" -> manageCustomer();
        case "3" -> manageOrder();
        case "4" -> {
          authManager.setLogout();
          run = false;
        }
        case "Q" -> {
          run = false; // quit while loop
        }
        default -> outPut.printMenuWarning();
      }
    }
  }

  private void manageProduct(){
    if (!shopManager.isHasAdminPermission()) return;
    ProductController product = new ProductController(shopManager);
    product.menu();
  }
  private void manageCustomer(){
    if (!shopManager.isHasAdminPermission()) return;

    CustomerController customer = new CustomerController(shopManager);
    customer.menu();
  }
  private void manageOrder(){
    if (!shopManager.isHasAdminPermission()) return;

    OrderController order =  new OrderController(shopManager);
    order.menu();
  }


  //:::::::::::::::::::::::: Frontend Menu ::::::::::::::::::::::::
  public void frontendMenu(){
    boolean run = true;
    while (run) {
      outPut.printFrontendMenu(shopManager.getIsCustomerLogin());
      // select menu
      String choice = scan.nextLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> viewProduct();
        case "2" -> getOrderHistory();
        case "3" -> {
          // check text in printFrontendMenu()
          if (!shopManager.getIsCustomerLogin()) {
            authManager.authenticationMenu();
          } else {
            customerManager.viewProfileByCustomerId(shopManager.getLoginCustomerId());
          }
        }
        case "4" -> authManager.setLogout();
        case "Q" ->  run = false;
        default -> outPut.printMenuWarning();
      }

    }
  }

  private void viewProduct(){
    productManager.frontendProductList();
  }

  private void getOrderHistory(){
    if (!shopManager.isHasCustomerPermission()) {
      authManager.doLogin();
      return;
    }


    while (true) {
      orderManager.orderHistory(shopManager.getLoginCustomerId());

      outPut.printViewOrderMenu();
      // select menu
      String inputString = scan.nextLine();
      outPut.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = orderManager.searchByInputNumber(inputString);

      if (itemIndex == -1) {
        outPut.printNotFound();
        return;
      }
      orderManager.getOrderDetail(itemIndex);
    }

  }


}
