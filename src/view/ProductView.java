package view;

import dataclass.OrderItem;
import dataclass.Product;
import utility.stats.OperationState;

import java.util.ArrayList;

public class ProductView extends OutPut{

  public void printBackendMenu() {
    System.out.println("""
            
            ::::::::::::::::::: PRODUCT MENU :::::::::::::::::::
            1. All Products
            2. New Product
            3. Edit Product
            4. Delete
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");

    System.out.print("Input choice: ");
  }

  public void printUpdateProductMenu() {
    System.out.println(
            """
                    :::::::::::::::::: ADD TO CART :::::::::::::::::::
                    - Input a list number to update
                    - Input Q to go back
                    :::::::::::::::::::::::::::::::::::::::::::::::::::::::"""
    );

    System.out.print("Input list Number: ");
  }

  public void printSelectToCartMenu(ArrayList<OrderItem> tempOrderItem) {
    if (tempOrderItem.isEmpty()) {
      System.out.println("""
            
            ::::::::::::::::::: SHOPPING MENU :::::::::::::::::::
            1. All Products
            2. Add product to cart
            3. View cart
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");
    } else {
      System.out.println("""
            
            ::::::::::::::::::: SHOPPING MENU :::::::::::::::::::
            1. All Products
            2. Add product to cart
            3. View cart
            4. Delete order
            5. Confirm order
            Q. Go back
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");
    }


    System.out.print("Input choice: ");
  }

  public void printAddToCartMenu(OperationState state) {
    System.out.println(
            ":::::::::::::::::: ADD TO CART :::::::::::::::::::"
                    + ((state == OperationState.ADD) ? "\n- Input a list number to add to cart" : "")
                    + ((state == OperationState.DELETE) ? "\n- Input a list number to delete from cart" : "")
                    +"\n- Input Q to go back"
                    +"\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::"
    );

    System.out.print("Input list Number: ");
  }

  public void printHeadLine(){
    printDashLine(97);
    println(
            addWhiteSpace("No.",4) + "|"
                    + addWhiteSpace("Code" ,8) + "|"
                    + addWhiteSpace("Product Name" ,30) + "|"
                    + addWhiteSpace("Product Detail" ,30) + "|"
                    + addWhiteSpace("Price",10) + "|"
                    + addWhiteSpace("Status",10) + "|");
    printDashLine(97);

  }

  public void printRow(int rowIndex, Product currentItem) {
    println(
            addWhiteSpace((rowIndex +1) + ". ",4) + "|"
                    + addWhiteSpace(currentItem.getProductCode() ,8) + "|"
                    + addWhiteSpace(currentItem.getProductName(),30) + "|"
                    + addWhiteSpace(currentItem.getProductDetail(),30) + "|"
                    + addWhiteSpace(currentItem.getPriceString(),10) + "|"
                    + addWhiteSpace(currentItem.getIsInStockText(),10)
    );
  }

  public void printSuccessAddTempOrderItem(Product currentItem){
    System.out.println(currentItem.getProductName() + " added");
  }

  public void printSuccessDeleteTempOrderItem(Product currentItem){
    System.out.println(currentItem.getProductName() + " deleted");
  }

  public void printEndOfList(){
    printEndOfList();
  }
}
