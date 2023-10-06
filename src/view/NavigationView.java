package view;

public class NavigationView extends OutPut{

  public void printMainMenu() {
    System.out.println("""
            
            ::::::::::::::::::: Main Menu :::::::::::::::::::
            1. Backend
            2. Frontend
            Q. Exit Program
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");
    System.out.print("Input choice: ");
  }

  public void printFrontendMenu(boolean isCustomerLogin) {
    if (isCustomerLogin) {
      System.out.println("""
            
            ::::::::::::::::::: FRONTEND MENU :::::::::::::::::::
            1. View product
            2. Order history
            3. View Profile
            4. Logout
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");
    } else {
      System.out.println("""
            
            ::::::::::::::::::: FRONTEND MENU :::::::::::::::::::
            1. View product
            2. Order history
            3. Login / Register
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");
    }

    System.out.print("Input choice: ");
  }


  public void printViewOrderMenu() {
    System.out.println(
            """
                    
                    :::::::::::::::::: SEARCH ORDER :::::::::::::::::::
                    - Input a list number to search order
                    - Input Q to go back
                    :::::::::::::::::::::::::::::::::::::::::::::::::::::::"""
    );
    System.out.print("Input list Number: ");
  }

  public void printBackendMenu() {
    System.out.println("""
            
            ::::::::::::::::::: BACKEND MENU :::::::::::::::::::
            1. Manage Products
            2. Manage Customers
            3. Manage Orders
            4. Logout
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");

    System.out.print("Input choice: ");
  }

}
