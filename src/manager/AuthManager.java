package manager;

import dataclass.Admin;
import dataclass.Customer;
import view.CustomerView;

import java.util.Scanner;

public class AuthManager {
  Scanner scan = new Scanner(System.in);
  CustomerView outPut = new CustomerView();
  int maxTry = 3;
  ShopManager shopManager;
  CustomerManager customerManager;

  public AuthManager(ShopManager shopManager) {
    this.shopManager = shopManager;
    this.customerManager = new CustomerManager(shopManager);
  }

  public void doLogin(){
    int countTry = 0;
    String username;
    String password;

    while (true) {
      // check max try
      if (maxTry == countTry) {
        countTry = 0;

        outPut.println("\nYou have tried " + maxTry + " times.");
        outPut.println("Q : Go back to main or enter to try again.");
        String inputChoice = scan.nextLine();

        if (inputChoice.equalsIgnoreCase("Q")) break;
      }

      this.getHeader();
      // input username/password
      outPut.print("Username: ");
      username = scan.nextLine();

      outPut.print("Password: ");
      password = scan.nextLine();

      //check if correct
      boolean isSuccess;
      if (shopManager.getIsAdmin()) {
        isSuccess = isAdminLoginSuccess(username, password);
      } else {
        isSuccess = isCustomerLoginSuccess(username,password);
      }

      if (isSuccess) {
        setLogin();
        break;
      } else {
        countTry++;
        outPut.println(" Wrong username or password !!! you have "
                +(maxTry - countTry) + " left.");
      }

    }
  }

  private void getHeader(){
    if (shopManager.getIsAdmin()) {
      outPut.println("\n::::: LOGIN TO BACKEND :::: ");
    }
    else {
      if (!shopManager.getTempOrderItems().isEmpty()) {
        outPut.println("\n::::: LOGIN TO CONFIRM ORDER :::: ");
      } else {
        outPut.println("\n::::: LOGIN TO FRONTEND :::: ");
      }
    }
  }

  public void setLogin() {
    System.out.println("+++ Set Login ++");

    if (shopManager.getIsAdmin()) {
      shopManager.setAdminLogin(true);
    } else {
      shopManager.setCustomerLogin(true);
    }
  }

  public void setLogout(){
    if (shopManager.getIsAdmin()) {
      shopManager.setAdminLogin(false);
    } else {
      shopManager.setLoginCustomerId(0);
      shopManager.setCustomerLogin(false);
    }

    shopManager.setIsAdmin(false);
  }

  private boolean isCustomerLoginSuccess(String username, String password){

    for (Customer customer : shopManager.customers) {
      if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
        shopManager.setLoginCustomerId(customer.getCustomerId());
        return true;
      }
    }
    return false;
  }

  private boolean isAdminLoginSuccess(String username, String password){
    for (Admin admin : shopManager.admins) {
      if (admin.getUsername().equals(username) && admin.getPassword().equals(password))
        return true;
    }
    return false;
  }


  public void authenticationMenu(){
    boolean run = true;
    while (run) {
      outPut.printRegisterOrLoginMenu();
      // select menu
      String choice = scan.nextLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> {
          doLogin();
        }
        case "2" -> {
          customerManager.register();
        }
        case "Q" ->  run = false;
        default -> outPut.printMenuWarning();
      }

      // exit loop if login success
      if (shopManager.getIsCustomerLogin()) run = false;

    }
  }

}
