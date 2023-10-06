package view;

import dataclass.Order;
import dataclass.OrderItem;
import utility.stats.OperationState;

// We use inheritance for View
public class OrderView extends OutPut{
  public void printBackendMenu() {
    System.out.println("""
            
            ::::::::::::::::::: ORDER MENU :::::::::::::::::::
            1. All Orders
            2. Search
            3. Set completed Order
            4. Delete Orders
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");

    System.out.println("Input choice: ");
  }

  public void printBackendOrderMenu(OperationState state){
    System.out.println(
            ":::::::::::::::::: SEARCH ORDER :::::::::::::::::::"
                    + ((state == OperationState.SEARCH) ? "\n- Input a list number to search order" : "")
                    + ((state == OperationState.DELETE) ? "\n- Input a list number to delete from cart" : "")
                    +"\n- Input Q to go back"
                    +"\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::"
    );

    System.out.print("Input list Number: ");
  }


  public void printOrderConfirmed(){
    System.out.print("Order confirmed!!");
  }

  public void printDeleteOrderItem(boolean isSuccess){
    if (isSuccess) {
      System.out.print("Deleted orderItem");
    } else {
      System.out.print("You can delete order Item only the order is pending.");
    }

  }

  public void printOrderHeadLine(){
    printDashLine(65);
    println(
            addWhiteSpace("No.",4) + "|"
                    + addWhiteSpace("Code" ,8) + "|"
                    + addWhiteSpace("Customer Name",22) + "|"
                    + addWhiteSpace("Purchase",10) + "|"
                    + addWhiteSpace("Status",10) + "|"
                    + addWhiteSpace("Order Date",22) + "|");
    printDashLine(65);
  }

  public void printOrderRow(int rowIndex, String customerName, double totalPurchase, Order currentItem) {
    println(
            addWhiteSpace((rowIndex +1) + ". ",4) + "|"
                    + addWhiteSpace(currentItem.getOrderCode(),8) + "|"
                    + addWhiteSpace(customerName ,22) + "|"
                    + addWhiteSpace(totalPurchase + "",10) + "|"
                    + addWhiteSpace(currentItem.getIsPenddingString(),10) + "|"
                    + addWhiteSpace(currentItem.getOrderDate(),22)
    );
  }

  public void printOrderDetail(String customerName,  Order currentItem) {
    printDashLine(70);

    println(
            addWhiteSpace("|",5)
                    + addWhiteSpace("Name",15) + ":"
                    + addWhiteSpace(customerName ,40)
                    + addWhiteSpace("",8) + "|");

    println(
            addWhiteSpace("|",5)
                    + addWhiteSpace("Status",15) + ":"
                    + addWhiteSpace(currentItem.getIsPendingText() ,40)
                    + addWhiteSpace("",8) + "|");

    println(
            addWhiteSpace("|",5)
                    + addWhiteSpace("Order Date",15) + ":"
                    + addWhiteSpace(currentItem.getOrderDate() ,40)
                    + addWhiteSpace("",8) + "|");
    println(
            addWhiteSpace("|",5)
                    + addWhiteSpace("Approve Date",15) + ":"
                    + addWhiteSpace(currentItem.getCompleteDate() ,40)
                    + addWhiteSpace("",8) + "|");

    //printDashLine(70);
  }

  public void printOrderItemHeadLine(){
    printDashLine(75);
    println(
            addWhiteSpace("No.",4) + "|"
                    + addWhiteSpace("Product Name" ,30) + "|"
                    + addWhiteSpace("Amount",8) + "|"
                    + addWhiteSpace("Full Price",12) + "|"
                    + addWhiteSpace("Sale Price",12) + "|"
                    + addWhiteSpace("Total",12) + "|");
    printDashLine(75);
  }

  public void printOrderItemRow(int rowIndex, String productName, OrderItem currentItem) {
    println(
            addWhiteSpace((rowIndex +1) + ". ",4) + "|"
                    + addWhiteSpace(productName ,30) + "|"
                    + addWhiteSpace(currentItem.getAmount()+"",8) + "|"
                    + addWhiteSpace(currentItem.getFullPriceString(),12) + "|"
                    + addWhiteSpace(currentItem.getSalePriceString(),12)
                    + addWhiteSpace(currentItem.getToPay() + "",12)
    );
  }


}
