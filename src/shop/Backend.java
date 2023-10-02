package shop;

import controllers.AuthController;
import controllers.CustomerController;
import controllers.MainController;
import controllers.OrderController;
import controllers.ProductController;
import utility.view.DefaultView;

import java.util.Scanner;

public class Backend {
  Scanner scan = new Scanner(System.in);
  DefaultView view;

  MainController mainObject;
  AuthController auth;
  public Backend(MainController mainObject, AuthController auth) {
    this.mainObject = mainObject;
    this.auth = auth;

    view = new DefaultView(mainObject);


  }

  public void menu(){
    if (!mainObject.isHasAdminPermission()) return;

    boolean run = true;
    while (run) {
      if (!mainObject.getIsAdminLogin()) run = false;

      view.printBackendMenu();
      // select menu
      String choice = scan.nextLine();
      view.printEmptyLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> manageProduct();
        case "2" -> manageCustomer();
        case "3" -> manageOrder();
        case "4" -> {
          mainObject.authObject.setLogout();
          run = false;
        }
        case "Q" -> {
          run = false; // quit while loop
        }
        default -> view.printBackendMenuWarning();
      }
    }
  }

  private void manageProduct(){
    if (!mainObject.isHasAdminPermission()) return;
    ProductController product = new ProductController(mainObject);
    product.menu();
  }
  private void manageCustomer(){
    if (!mainObject.isHasAdminPermission()) return;

    CustomerController customer = new CustomerController(mainObject);
    customer.menu();
  }
  private void manageOrder(){
    if (!mainObject.isHasAdminPermission()) return;

    OrderController order =  new OrderController(mainObject);
    order.menu();
  }

}
