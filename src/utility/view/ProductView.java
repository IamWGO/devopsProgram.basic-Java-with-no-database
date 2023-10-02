package utility.view;

import dataclass.OrderItem;
import dataclass.Product;
import utility.Theme;
import utility.colors.ProductColor;
import utility.stats.OperationState;

import java.util.ArrayList;

public class ProductView {
  Theme printOut = new Theme(new ProductColor());

  public void printSelectToCartMenu(ArrayList<OrderItem> tempOrderItem) {
    if (tempOrderItem.isEmpty()) {
      printOut.printInfo("""
            
            ::::::::::::::::::: SHOPPING MENU :::::::::::::::::::
            1. All Products
            2. Add product to cart
            3. View cart
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");
    } else {
      printOut.printInfo("""
            
            ::::::::::::::::::: SHOPPING MENU :::::::::::::::::::
            1. All Products
            2. Add product to cart
            3. View cart
            4. Delete order
            5. Confirm order
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");
    }


    printOut.print("Input choice: ");
  }

  public void printBackendMenu() {
    printOut.printInfo("""
            
            ::::::::::::::::::: PRODUCT MENU :::::::::::::::::::
            1. All Products
            2. Search
            3. New Product
            4. Edit Product
            5. Delete
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");

    printOut.print("Input choice: ");
  }

  public void printAddToCartMenu(OperationState state) {
    printOut.printInfo(
             ":::::::::::::::::: ADD TO CART :::::::::::::::::::"
                     + ((state == OperationState.ADD) ? "\n- Input a list number to add to cart" : "")
                     + ((state == OperationState.DELETE) ? "\n- Input a list number to delete from cart" : "")
                     +"\n- Input Q to go back"
                     +"\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::"
                    );

    printOut.print("Input list Number: ");
  }

  public void printUpdateProductMenu() {
    printOut.printInfo(
            """
                    :::::::::::::::::: ADD TO CART :::::::::::::::::::
                    - Input a list number to update
                    - Input Q to go back
                    :::::::::::::::::::::::::::::::::::::::::::::::::::::::"""
    );

    printOut.print("Input list Number: ");
  }

  public void printDetail(Product currentItem){
    //TODO : add design
    String searchResult = currentItem.objectToLineFormat();
    printOut.println("--> search product");
    printOut.println(searchResult);
  }

  public void printMenuWarning() {
    printOut.println("Input a number or Q to exit program");
  }

  public void printNotFound(){
    printOut.printInfo("No item found.");
  }

  public void printEmptyItem(){
    printOut.printInfo("No item found.");
  }

  public void printUpdateResult(Product currentItem){
    printOut.printInfo("updated : " + currentItem.objectToLineFormat());
  }

  public void printDeleteResult(Product currentItem){
    printOut.printInfo("Removed : " + currentItem.objectToLineFormat());
  }

  public void printHeadLine(){
    printOut.printDashLine(67);
    printOut.println(
            printOut.addWhiteSpace("No.",4) + "|"
                    + printOut.addWhiteSpace("Code" ,8) + "|"
                    + printOut.addWhiteSpace("Product Name" ,30) + "|"
                    + printOut.addWhiteSpace("Price",10) + "|"
                    + printOut.addWhiteSpace("Status",10) + "|");
    printOut.printDashLine(67);

  }

  public void printRow(int rowIndex, Product currentItem) {
    printOut.println(
            printOut.addWhiteSpace((rowIndex +1) + ". ",4) + "|"
                    + printOut.addWhiteSpace(currentItem.getProductCode() ,8) + "|"
                    + printOut.addWhiteSpace(currentItem.getProductName(),30) + "|"
                    + printOut.addWhiteSpace(currentItem.getPriceString(),10) + "|"
                    + printOut.addWhiteSpace(currentItem.getIsInStockText(),10)
    );
  }

  public void printFooter(){
    printOut.printEndOfList();
  }

  public void printSuccessAddTempOrderItem(Product currentItem){
    printOut.printSuccess(currentItem.getProductName() + " added");
  }

  public void printSuccessAddProduct(Product currentItem){
    printOut.printSuccess(currentItem.getProductName() + " added");
  }

  public void printSuccessDeleteTempOrderItem(Product currentItem){
    printOut.printSuccess(currentItem.getProductName() + " deleted");
  }
  public void printEmptyLine(){
    System.out.println();
  }
  public void printText(String text){
    printOut.print(text);
  }

  public void printCancel(){
    printOut.printInfo("Cancel Process !!");
  }
}
