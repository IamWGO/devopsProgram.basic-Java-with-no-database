package utility.view;

import controllers.MainController;
import utility.Theme;
import utility.colors.DefaultColor;

public class DefaultView {
  Theme printOut = new Theme(new DefaultColor());
  MainController mainObject;

  public DefaultView(MainController mainObject) {
    this.mainObject = mainObject;
  }

  public void printText(String text){
    printOut.print(text);
  }
  public void printHeaderMenu(){
    if (mainObject.getIsAdmin()) {
      printOut.println("::::: LOGIN TO BACKEND :::: ");
    }
    else {
      if (!mainObject.getTempOrderItems().isEmpty()) {
        printOut.println("::::: LOGIN TO CONFIRM ORDER :::: ");
      } else {
        printOut.println("::::: LOGIN TO FRONTEND :::: ");
      }
    }

  }

  public void printMaxTryInfo(int maxTry, int nextTryInSecond){
    printOut.println("You have tried " + maxTry + " times.");
    printOut.println("Q : Go back to main or enter to try in  "+ nextTryInSecond + " seconds.");
  }

  public void printWrongPassword(int maxTry, int countTry){
    printOut.printWarning(" Wrong username or password !!! you have "
            +(maxTry - countTry) + " left.");
  }

  public void printEmptyLine(){
    System.out.println();
  }

  /* ++++++ Backend ++++++ */
  public void printBackendMenu() {
    printOut.printInfo("""
            ::::::::::::::::::: BACKEND MENU :::::::::::::::::::
            1. Manage Products
            2. Manage Customers
            3. Manage Orders
            4. Logout
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");

    printOut.print("Input choice: ");
  }

  public void printBackendMenuWarning() {
    printOut.println("Input a number or Q to exit program");
  }


  /* ++++++ Frontend ++++++ */
  public void printFrontendMenu() {
    if (mainObject.getIsCustomerLogin()) {
      printOut.printInfo("""
            ::::::::::::::::::: FRONTEND MENU :::::::::::::::::::
            1. View product
            2. Order history
            3. View Profile
            4. Logout
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");
    } else {
      printOut.printInfo("""
            ::::::::::::::::::: FRONTEND MENU :::::::::::::::::::
            1. View product
            2. Order history
            3. Login
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");
    }

    printOut.print("Input choice: ");
  }

  public void printRegisterOrLoginMenu() {
    printOut.printInfo("""
            ::::::::::::::::::: CUSTOMER MENU :::::::::::::::::::
            Note: You need to login before you can confirm order
                  or view order history
            
            1. You already have an account
            2. Register
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");
    printOut.print("Input choice: ");
  }

  public void printNoPermission(){
    printOut.printWarning("You don't have permission");
  }

  public void printFrontendMenuWarning() {
    printOut.println("Input a number or Q to exit program");
  }
}
