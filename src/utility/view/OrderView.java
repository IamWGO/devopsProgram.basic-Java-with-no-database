package utility.view;

import dataclass.Order;
import dataclass.OrderItem;
import utility.Theme;
import utility.colors.OrderColor;
import utility.stats.OperationState;

public class OrderView {
  Theme printOut = new Theme(new OrderColor());

  public void printFrontendMenu() {
    printOut.printInfo("""
            
            ::::::::::::::::::: ORDER MENU :::::::::::::::::::
            1. All Orders
            2. Logout
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");

    printOut.print("Input choice: ");
  }

  public void printBackendMenu() {
    printOut.printInfo("""
            
            ::::::::::::::::::: ORDER MENU :::::::::::::::::::
            1. All Orders
            2. Search
            3. Set completed Order
            4. Delete Orders
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");

    printOut.print("Input choice: ");
  }

  public void printBackendOrderMenu(OperationState state){
    printOut.printInfo(
            ":::::::::::::::::: SEARCH ORDER :::::::::::::::::::"
                    + ((state == OperationState.SEARCH) ? "\n- Input a list number to search order" : "")
                    + ((state == OperationState.DELETE) ? "\n- Input a list number to delete from cart" : "")
                    +"\n- Input Q to go back"
                    +"\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::"
    );

    printOut.print("Input list Number: ");
  }

  public void printOrderHeadLine(){
    printOut.printDashLine(65);
    printOut.println(
            printOut.addWhiteSpace("No.",4) + "|"
                    + printOut.addWhiteSpace("Code" ,8) + "|"
                    + printOut.addWhiteSpace("Customer Name",22) + "|"
                    + printOut.addWhiteSpace("Purchase",10) + "|"
                    + printOut.addWhiteSpace("Status",10) + "|"
                    + printOut.addWhiteSpace("Order Date",22) + "|");
    printOut.printDashLine(65);
  }

  public void printOrderRow(int rowIndex, String customerName, double totalPurchase, Order currentItem) {
    printOut.println(
            printOut.addWhiteSpace((rowIndex +1) + ". ",4) + "|"
                    + printOut.addWhiteSpace(currentItem.getOrderCode(),8) + "|"
                    + printOut.addWhiteSpace(customerName ,22) + "|"
                    + printOut.addWhiteSpace(totalPurchase + "",10) + "|"
                    + printOut.addWhiteSpace(currentItem.getIsPenddingString(),10) + "|"
                    + printOut.addWhiteSpace(currentItem.getOrderDate(),22)
    );
  }

  public void printOrderFooter(){
    printOut.printEndOfList();
  }




  public void printOrderItemHeadLine(){
    printOut.printDashLine(75);
    printOut.println(
            printOut.addWhiteSpace("No.",4) + "|"
                    + printOut.addWhiteSpace("Product Name" ,30) + "|"
                    + printOut.addWhiteSpace("Amount",8) + "|"
                    + printOut.addWhiteSpace("Full Price",12) + "|"
                    + printOut.addWhiteSpace("Sale Price",12) + "|"
                    + printOut.addWhiteSpace("Total",12) + "|");
    printOut.printDashLine(75);
  }

  public void printOrderItemRow(int rowIndex, String productName, OrderItem currentItem) {
    printOut.println(
            printOut.addWhiteSpace((rowIndex +1) + ". ",4) + "|"
                    + printOut.addWhiteSpace(productName ,30) + "|"
                    + printOut.addWhiteSpace(currentItem.getAmount()+"",8) + "|"
                    + printOut.addWhiteSpace(currentItem.getFullPriceString(),12) + "|"
                    + printOut.addWhiteSpace(currentItem.getSalePriceString(),12)
                    + printOut.addWhiteSpace(currentItem.getToPay() + "",12)
    );
  }

  public void printOrderDetail(String customerName,  Order currentItem) {
    printOut.printDashLine(70);

    printOut.println(
            printOut.addWhiteSpace("|",5)
                    + printOut.addWhiteSpace("Name",15) + ":"
                    + printOut.addWhiteSpace(customerName ,40)
                    + printOut.addWhiteSpace("",8) + "|");

    printOut.println(
            printOut.addWhiteSpace("|",5)
                    + printOut.addWhiteSpace("Status",15) + ":"
                    + printOut.addWhiteSpace(currentItem.getIsPendingText() ,40)
                    + printOut.addWhiteSpace("",8) + "|");

    printOut.println(
            printOut.addWhiteSpace("|",5)
                    + printOut.addWhiteSpace("Order Date",15) + ":"
                    + printOut.addWhiteSpace(currentItem.getOrderDate() ,40)
                    + printOut.addWhiteSpace("",8) + "|");
    printOut.println(
            printOut.addWhiteSpace("|",5)
                    + printOut.addWhiteSpace("Approve Date",15) + ":"
                    + printOut.addWhiteSpace(currentItem.getCompleteDate() ,40)
                    + printOut.addWhiteSpace("",8) + "|");

    //printOut.printDashLine(70);
  }

  public void printMenuWarning() {
    printOut.println("Input a number or Q to exit program");
  }

  public void printNotFound(){
    printOut.printInfo("No items found.");
  }

  public void printEmptyItem(){
    printOut.printInfo("No item found.");
  }

  public void printUpdateResult(){
    printOut.printInfo("Updated !!!");
  }

  public void printDeleteOrderItem(boolean isSuccess){
    if (isSuccess) {
      printOut.printInfo("Deleted orderItem");
    } else {
      printOut.printWarning("You can delete order Item only the order is pending.");
    }

  }

  public void printCancel(){
    printOut.printInfo("Cancel Process !!");
  }

  public void printDeleteResult(Order currentItem){
    printOut.printInfo("Removed : " + currentItem.objectToLineFormat());
  }

  public void printDeleteResult(OrderItem currentItem){
    printOut.printInfo("Removed : " + currentItem.objectToLineFormat());
  }
  public void printText(String text){
    printOut.print(text);
  }

  public void printOrderConfirmed(){
    printOut.printSuccess("Order confirmed!!");
  }

  public void printEmptyLine(){
    System.out.println();
  }
}
