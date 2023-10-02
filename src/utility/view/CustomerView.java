package utility.view;

import dataclass.Customer;
import utility.Theme;
import utility.colors.CustomerColor;
import utility.stats.OperationState;

public class CustomerView {
  Theme printOut = new Theme(new CustomerColor());
  public void printBackendMenu() {
    printOut.printInfo("""
            
            ::::::::::::::::::: CUSTOMER MENU :::::::::::::::::::
            1. All customer
            2. View Profile
            3. View Order
            4. Update Status
            5. Delete customer
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");

    printOut.print("Input choice: ");
  }

  public void printCustomerMenu(OperationState state) {
    printOut.printInfo(
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

    printOut.print("Input choice: ");
  }

  public void printDetail(Customer currentItem){
    printOut.printDashLine(70);
    printOut.println("|"+ " ".repeat(22)
            + "CUSTOMER PROFILE " + currentItem.getCustomerCode()
            + " ". repeat(22)+ "|");
    printOut.printDashLine(70);

    printOut.println(
            printOut.addWhiteSpace("|",5)
                    + printOut.addWhiteSpace("Code",15) + ": "
                    + printOut.addWhiteSpace(currentItem.getCustomerCode() ,39)
                    + printOut.addWhiteSpace("",8) + "|");
    printOut.println(
            printOut.addWhiteSpace("|",5)
                    + printOut.addWhiteSpace("Name",15) + ": "
                    + printOut.addWhiteSpace(currentItem.getFullName() ,39)
                    + printOut.addWhiteSpace("",8) + "|");

    printOut.println(
            printOut.addWhiteSpace("|",5)
                    + printOut.addWhiteSpace("Username",15) + ": "
                    + printOut.addWhiteSpace(currentItem.getUsername() ,39)
                    + printOut.addWhiteSpace("",8) + "|");

    printOut.println(
            printOut.addWhiteSpace("|",5)
                    + printOut.addWhiteSpace("Email",15) + ": "
                    + printOut.addWhiteSpace(currentItem.getEmail() ,39)
                    + printOut.addWhiteSpace("",8) + "|");

    printOut.println(
            printOut.addWhiteSpace("|",5)
                    + printOut.addWhiteSpace("Address",15) + ": "
                    + printOut.addWhiteSpace(currentItem.getAddress() ,39)
                    + printOut.addWhiteSpace("",8) + "|");

    printOut.println(
            printOut.addWhiteSpace("|",5)
                    + printOut.addWhiteSpace("Zipcode",15) + ": "
                    + printOut.addWhiteSpace(currentItem.getZipCode() ,39)
                    + printOut.addWhiteSpace("",8) + "|");

    printOut.println(
            printOut.addWhiteSpace("|",5)
                    + printOut.addWhiteSpace("Country",15) + ": "
                    + printOut.addWhiteSpace(currentItem.getCountry() ,39)
                    + printOut.addWhiteSpace("",8) + "|");
    printOut.printDashLine(70);
  }

  public void printHeadLine(){
    printOut.printDashLine(130);
    printOut.println(
            printOut.addWhiteSpace("No.",4) + "|"
                    + printOut.addWhiteSpace("Code" ,7) + "|"
                    + printOut.addWhiteSpace("Username",12) + "|"
                    + printOut.addWhiteSpace("Full Name",22) + "|"
                    + printOut.addWhiteSpace("Email",22) + "|"
                    + printOut.addWhiteSpace("Address",22) + "|"
                    + printOut.addWhiteSpace("ZipCode",7) + "|"
                    + printOut.addWhiteSpace("Country",15) + "|"
                    + printOut.addWhiteSpace("Active",8)
    );
    printOut.printDashLine(130);
  }

  public void printRow(int rowIndex, Customer currentItem) {
    printOut.println(
            printOut.addWhiteSpace((rowIndex +1) + ". ",4) + "|"
                    + printOut.addWhiteSpace(currentItem.getCustomerCode() ,7) + "|"
                    + printOut.addWhiteSpace(currentItem.getUsername(),12) + "|"
                    + printOut.addWhiteSpace(currentItem.getFullName(),22) + "|"
                    + printOut.addWhiteSpace(currentItem.getEmail(),22) + "|"
                    + printOut.addWhiteSpace(currentItem.getAddress(),22) + "|"
                    + printOut.addWhiteSpace(currentItem.getZipCode(),7) + "|"
                    + printOut.addWhiteSpace(currentItem.getCountry(),15) + "|"
                    + printOut.addWhiteSpace(currentItem.getIsActiveString(),8)
    );
  }

  public void printMenuWarning() {
    printOut.println("Input a number or Q to exit program");
  }

  public void printNotFound(){
    printOut.printInfo("No customers found.");
  }

  public void printEmptyItem(){
    printOut.printInfo("No customers found.");
  }

  public void printAddResult(Customer currentItem){
    printOut.printSuccess(currentItem.getFullName() + " added");
  }

  public void printDeleteResult(boolean isSuccess, Customer currentItem){
    if (isSuccess)
      printOut.printSuccess(currentItem.getFullName() + " deleted");
    else
      printOut.printWarning("Can't delete " + currentItem.getFullName());
  }
  public void printUpdateResult(Customer currentItem){
    printOut.printInfo("updated : " + currentItem.objectToLineFormat());
  }

  public void printFooter(){
    printOut.printDashLine(130);
  }

  public void printText(String text){
    printOut.print(text);
  }

  public void printEmptyLine(){
    System.out.println();
  }

  public void printCancel(){
    printOut.printInfo("Cancel Process !!");
  }

}
