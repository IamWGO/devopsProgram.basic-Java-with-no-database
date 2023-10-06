package service;


import dataclass.*;
import manager.ShopManager;
import utility.stats.FileState;
import utility.stats.MainState;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class FileService {
  String filepath = "src/datafiles/";
  String filename;

  ShopManager shopManager;
  MainState mainState;
  FileState fileState;

  public FileService(ShopManager shopManager, MainState mainState, FileState fileState) {
    this.shopManager = shopManager;
    this.mainState = mainState;
    this.fileState = fileState;
  }
  public void chooseString(String choice){
    switch (choice.toUpperCase()) {
      case "ADMIN" :
        filename = filepath + "Admin.txt";
      case "PRODUCT" : {
        filename = filepath + "Product.txt";
      }
      case "CUSTOMER" : {
        filename = filepath + "Customer.txt";
      }
      case "ORDER" : {
        filename = filepath + "Order.txt";
      }
      case "ORDER_ITEM" : {
        filename = filepath + "OrderItem.txt";
      }
      break;
      default:
        throw new IllegalStateException("Unexpected value: " + choice.toUpperCase());
    }
  }

  public void choose(){

    switch (shopManager.getMainState()) {
      case ADMIN -> filename = filepath + "Admin.txt";
      case PRODUCT -> filename = filepath + "Product.txt";
      case CUSTOMER -> filename = filepath + "Customer.txt";
      case ORDER -> filename = filepath + "Order.txt";
      case ORDER_ITEM -> filename = filepath + "OrderItem.txt";
    }

    switch (shopManager.getFileState()) {
      case READ -> readFile();
      case NEW_LINE -> addNewLine(shopManager.getWriteToFileText());
      case OVERWRITE -> overwriteFile(shopManager.getWriteToFileText());
    }

  }

  private void readFile(){
    try {
      File file = new File(filename);
      if (file.createNewFile()) {
        //Create file Task.txt and write content.
        FileWriter writer = new FileWriter(file);
        // put writer.write("");  if we don't have the first content
        writer.write("");
        writer.close();
      } else {
        //File already exists.
        readWithScanner(filename);
      }
    } catch (IOException ex) {
      System.out.println("An error occurred." + ex);
    }
  }

  private void readWithScanner(String filename){
    try(Scanner contentLines  = new Scanner(new File(filename))) {
      while (contentLines.hasNextLine()) {
        String contentLine = contentLines.nextLine();
        if (contentLine.isEmpty()) continue;

        // save to TaskList
        switch (shopManager.getMainState()) {
          case ADMIN -> addNewAdmin(contentLine);
          case PRODUCT -> addNewProduct(contentLine);
          case CUSTOMER -> addNewCustomer(contentLine);
          case ORDER -> addNewOrder(contentLine);
          case ORDER_ITEM -> addNewOrderItem(contentLine);
        }
      }
    } catch (FileNotFoundException ex) {
      System.out.println("File not found " + ex);
    }
  }

  private void addNewLine(String contentLine){
    writeToFile(contentLine, false);
  }
  private void overwriteFile(String contentLines){

    if (contentLines.length() >= 2) {
      String lastTwoLetters = contentLines.substring(contentLines.length() - 2);
      if (lastTwoLetters.equals("\n"))
        contentLines = contentLines.substring(0,contentLines.length() - 2);
    }

    writeToFile(contentLines, true);
  }


  /* We use write BufferedWriter */
  //TODO: fix empty line created when delete all  : might have to change write file library
  private void writeToFile(String writeContent, boolean isRewrite){
    if (writeContent.isEmpty()) {

      return;
    }

    try {
      // Create a FileWriter in append mode (true as the second argument)
      FileWriter fileWriter = new FileWriter(filename, !isRewrite);

      // Create a BufferedWriter for efficient writing
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

      // Write the new line to the file
      bufferedWriter.write(writeContent);
      // Add a newline character
      if (fileState != FileState.OVERWRITE)  bufferedWriter.newLine();

      // Close the BufferedWriter and FileWriter
      bufferedWriter.close();
      fileWriter.close();
    } catch (IOException e) {
      System.out.println("An error occurred while adding a line to the file.");
    }
  }

  private void addNewAdmin(String contentLine) {
    //admin,1234567
    String[] parts = contentLine.split(",");
    try {
      String username = parts[0];
      String password = parts[1];
      Admin newItem = new Admin(username, password);
      shopManager.addNewAdmin(newItem);

    } catch (IllegalStateException ex) {  // Skip
      System.out.println("Invalid input string. Expected 2 fields." + ex);
    }
  }
  private void addNewCustomer(String contentLine) {
    //1,Waleerat,Gottlieb,customer,1234567,lee@gmail.com,-,-,-,1
    String[] parts = contentLine.split(",");
    try {
      int customerId = FormatService.stringToInt(parts[0]);
      String firstname = parts[1];
      String lastname = parts[2];
      String username = parts[3];
      String password = parts[4];
      String email = parts[5];
      String address = parts[6];
      String zipCode = parts[7];
      String country = parts[8];
      boolean isActive =  parts[9].equals("1");

      Customer newItem = new Customer(customerId, firstname, lastname, username, password,
              email,address,zipCode,country,isActive);
      shopManager.addNewCustomer(newItem);
    } catch (ArrayIndexOutOfBoundsException ex) {
      //System.out.println("Empty line in" + filename);
    } catch (IllegalStateException ex) {  // Skip
      System.out.println("Order fields is not match content line: " + ex);
    }
  }

  private void addNewProduct(String contentLine) {
    //1,Product 3,100.0,0,1
    String[] parts = contentLine.split(",");
    try {
      int productId = FormatService.stringToInt(parts[0]);
      String productName = parts[1];
      String productDetail = parts[2];
      double price = FormatService.StringToDouble(parts[3]);
      boolean isSoldOut =  parts[4].equals("1");
      boolean isActive =  parts[5].equals("1");
      Product newItem = new Product(productId, productName,productDetail, price, isSoldOut, isActive);
      shopManager.addNewProduct(newItem);
    } catch (ArrayIndexOutOfBoundsException ex) {
      //System.out.println("Empty line in" + filename);
    } catch (IllegalStateException ex) {  // Skip
      System.out.println("Order fields is not match content line: " + ex);
    }
  }

  private void addNewOrder(String contentLine) {
    //1,3,Remark11,2023-09-28 23:48:33,2023-09-28 23:48:33
    String[] parts = contentLine.split(",");
    try {
      int orderId = FormatService.stringToInt(parts[0]);
      int customerId = FormatService.stringToInt(parts[1]);
      String remark = parts[2];
      boolean isPending =  parts[3].equals("1");
      LocalDateTime orderDate = FormatService.stringToLocalDate(parts[4]);
      LocalDateTime completeDate = FormatService.stringToLocalDate(parts[5]);

      Order newItem = new Order(orderId,customerId,remark,isPending,orderDate,completeDate);
      shopManager.addNewOrder(newItem);
    } catch (ArrayIndexOutOfBoundsException ex) {
      //System.out.println("Empty line in" + filename);
    } catch (IllegalStateException ex) {  // Skip
      System.out.println("Order fields is not match content line: " + ex);
    }
  }

  private void addNewOrderItem(String contentLine) {
    //1,3,12,233.0,200.0
    String[] parts = contentLine.split(",");
    try {
      int orderItemId = FormatService.stringToInt(parts[0]);
      int orderId = FormatService.stringToInt(parts[1]);
      int productId = FormatService.stringToInt(parts[2]);
      int amount = FormatService.stringToInt(parts[3]);
      double fullPrice = FormatService.StringToDouble(parts[4]);
      double salePrice = FormatService.StringToDouble(parts[5]);

      OrderItem newItem = new OrderItem(orderItemId, orderId, productId, amount, fullPrice, salePrice);
      shopManager.addNewOrderItem(newItem);
    } catch (ArrayIndexOutOfBoundsException ex) {
      //System.out.println("Empty line in" + filename);
    } catch (IllegalStateException ex) {  // Skip
      System.out.println("fields is not match content line: " + ex);
    }
  }

}