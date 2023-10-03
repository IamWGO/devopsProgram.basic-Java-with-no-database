package manager;

import dataclass.Admin;
import dataclass.Customer;
import utility.view.DefaultView;

import java.util.Scanner;

public class AuthManager {
  Scanner scan = new Scanner(System.in);
  DefaultView view;

  int maxTry = 3;
  int nextTryInSecond = 10;

  ShopManager mainObject;
  public AuthManager(ShopManager mainObject) {
    this.mainObject = mainObject;
    view = new DefaultView(mainObject);
  }

  public void doLogin(){
    int countTry = 0;
    String username;
    String password;

    while (true) {
      // Print header
      if (countTry == 0) {
        view.printHeaderMenu();
      }

      // check max try
      if (maxTry == countTry) {
        view.printMaxTryInfo(maxTry, nextTryInSecond);

        String inputChoice = scan.nextLine();

        if (inputChoice.equalsIgnoreCase("Q")) break;
        delay();
        countTry = 0;
      }

      // input username/password
      view.printText("Username: ");
      username = scan.nextLine();

      view.printText("Password: ");
      password = scan.nextLine();

      //check if correct
      boolean isSuccess;
      if (mainObject.getIsAdmin()) {
        isSuccess = isAdminLoginSuccess(username, password);
      } else {
        isSuccess = isCustomerLoginSuccess(username,password);
      }

      if (isSuccess) {
        setLogin();
        break;
      } else {
        countTry++;
        view.printWrongPassword(maxTry,countTry);
      }

    }
  }

  public void customerLogin(){
    doLogin();
  }

  public void setLogin() {
    if (mainObject.getIsAdmin()) {
      mainObject.setAdminLogin(true);
    } else {
      mainObject.setCustomerLogin(true);
    }
  }

  public void setLogout(){
    if (mainObject.getIsAdmin()) {
      mainObject.setAdminLogin(false);
    } else {
      mainObject.setLoginCustomerId(0);
      mainObject.setCustomerLogin(false);
    }

    mainObject.setIsAdmin(false);
  }

  private boolean isCustomerLoginSuccess(String username, String password){

    for (Customer customer : mainObject.customers) {
      if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
        mainObject.setLoginCustomerId(customer.getCustomerId());
        return true;
      }
    }
    return false;
  }

  private boolean isAdminLoginSuccess(String username, String password){
    for (Admin admin : mainObject.admins) {
      if (admin.getUsername().equals(username) && admin.getPassword().equals(password))
        return true;
    }
    return false;
  }


  private void delay(){
    try {
      Thread.sleep(nextTryInSecond * 1000L); // Convert seconds to milliseconds
    } catch (InterruptedException e) {
      // Handle the exception if necessary
    }
  }

}
