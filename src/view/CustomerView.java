package view;

import dataclass.Customer;
import utility.stats.OperationState;

public class CustomerView extends OutPut {
  public void printBackendMenu() {
    System.out.println("""
            
            ::::::::::::::::::: CUSTOMER MENU :::::::::::::::::::
            1. All customer
            2. View Profile
            3. View Order
            4. Update Status
            5. Delete customer
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");

    System.out.print("Input choice: ");
  }

  public void printCustomerMenu(OperationState state) {
    System.out.println(
            "\n :::::::::::::::::: VIEW PROFILE MENU :::::::::::::::::::"
                    +"\n 1. Input index (No.) "
                    + (switch (state){
              case ADD -> "";
              case SEARCH -> " to view profile";
              case DELETE -> " to delete (Only customer which has no orders)";
              case UPDATE_STATUS -> " to update Status";
              case VIEW_ORDER -> " to view order";
            })
                    +"\n Q. Go back"
                    +"\n :::::::::::::::::::::::::::::::::::::::::::::::::::::::");

    System.out.print("Input choice: ");
  }

  public void printRegisterOrLoginMenu() {
    System.out.println("""
            ::::::::::::::::::: CUSTOMER MENU :::::::::::::::::::
            Note: You need to login before you can confirm order
                  or view order history
            
            1. You already have an account
            2. Register
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");
    System.out.print("Input choice: ");
  }

  public void printHeadLine(){
    printDashLine(130);
    println(
            addWhiteSpace("No.",4) + "|"
                    + addWhiteSpace("Code" ,7) + "|"
                    + addWhiteSpace("Username",12) + "|"
                    + addWhiteSpace("Full Name",22) + "|"
                    + addWhiteSpace("Email",22) + "|"
                    + addWhiteSpace("Address",22) + "|"
                    + addWhiteSpace("ZipCode",7) + "|"
                    + addWhiteSpace("Country",15) + "|"
                    + addWhiteSpace("Active",8)
    );
    printDashLine(130);
  }

  public void printRow(int rowIndex, Customer currentItem) {
    println(
            addWhiteSpace((rowIndex +1) + ". ",4) + "|"
                    + addWhiteSpace(currentItem.getCustomerCode() ,7) + "|"
                    + addWhiteSpace(currentItem.getUsername(),12) + "|"
                    + addWhiteSpace(currentItem.getFullName(),22) + "|"
                    + addWhiteSpace(currentItem.getEmail(),22) + "|"
                    + addWhiteSpace(currentItem.getAddress(),22) + "|"
                    + addWhiteSpace(currentItem.getZipCode(),7) + "|"
                    + addWhiteSpace(currentItem.getCountry(),15) + "|"
                    + addWhiteSpace(currentItem.getIsActiveString(),8)
    );
  }

  public void printDetail(Customer currentItem){
    printDashLine(70);
    println("|"+ " ".repeat(22)
            + "CUSTOMER PROFILE " + currentItem.getCustomerCode()
            + " ". repeat(22)+ "|");
    printDashLine(70);

    println(
            addWhiteSpace("|",5)
                    + addWhiteSpace("Code",15) + ": "
                    + addWhiteSpace(currentItem.getCustomerCode() ,39)
                    + addWhiteSpace("",8) + "|");
    println(
            addWhiteSpace("|",5)
                    + addWhiteSpace("Name",15) + ": "
                    + addWhiteSpace(currentItem.getFullName() ,39)
                    + addWhiteSpace("",8) + "|");

    println(
            addWhiteSpace("|",5)
                    + addWhiteSpace("Username",15) + ": "
                    + addWhiteSpace(currentItem.getUsername() ,39)
                    + addWhiteSpace("",8) + "|");

    println(
            addWhiteSpace("|",5)
                    + addWhiteSpace("Email",15) + ": "
                    + addWhiteSpace(currentItem.getEmail() ,39)
                    + addWhiteSpace("",8) + "|");

    println(
            addWhiteSpace("|",5)
                    + addWhiteSpace("Address",15) + ": "
                    + addWhiteSpace(currentItem.getAddress() ,39)
                    + addWhiteSpace("",8) + "|");

    println(
            addWhiteSpace("|",5)
                    + addWhiteSpace("Zipcode",15) + ": "
                    + addWhiteSpace(currentItem.getZipCode() ,39)
                    + addWhiteSpace("",8) + "|");

    println(
            addWhiteSpace("|",5)
                    + addWhiteSpace("Country",15) + ": "
                    + addWhiteSpace(currentItem.getCountry() ,39)
                    + addWhiteSpace("",8) + "|");
    printDashLine(70);
  }


  public void printDeleteResult(boolean isSuccess, Customer currentItem){
    if (isSuccess)
      println(currentItem.getFullName() + " deleted");
    else
      println("Can't delete " + currentItem.getFullName());
  }
}
