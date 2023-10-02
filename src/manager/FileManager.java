package manager;

import controllers.MainController;
import dataclass.*;
import utility.stats.FileState;
import utility.stats.MainState;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class FileManager {
  String filepath = "src/datafiles/";
  String filename;

  MainController mainObject;
  MainState mainState;
  FileState fileState;

  public FileManager(MainController mainObject, MainState mainState, FileState fileState) {
    this.mainObject = mainObject;
    this.mainState = mainState;
    this.fileState = fileState;
  }

  public void choose(){

    switch (mainObject.getMainState()) {
      case ADMIN -> filename = filepath + "Admin.txt";
      case PRODUCT -> filename = filepath + "Product.txt";
      case CUSTOMER -> filename = filepath + "Customer.txt";
      case ORDER -> filename = filepath + "Order.txt";
      case ORDER_ITEM -> filename = filepath + "OrderItem.txt";
    }

    switch (mainObject.getFileState()) {
      case READ -> readFile();
      case NEW_LINE -> addNewLine(mainObject.getWriteToFileText());
      case OVERWRITE -> overwriteFile(mainObject.getWriteToFileText());
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
        switch (mainObject.getMainState()) {
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
      mainObject.addNewAdmin(newItem);

    } catch (IllegalStateException ex) {  // Skip
      System.out.println("Invalid input string. Expected 2 fields." + ex);
    }
  }
  private void addNewCustomer(String contentLine) {
    //1,Waleerat,Gottlieb,customer,1234567,lee@gmail.com,-,-,-,1
    String[] parts = contentLine.split(",");
    try {
      int customerId = FormatManager.stringToInt(parts[0]);
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
      mainObject.addNewCustomer(newItem);
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
      int productId = FormatManager.stringToInt(parts[0]);
      String productName = parts[1];
      double price = FormatManager.StringToDouble(parts[2]);
      boolean isSoldOut =  parts[3].equals("1");
      boolean isActive =  parts[4].equals("1");
      Product newItem = new Product(productId, productName, price, isSoldOut, isActive);
      mainObject.addNewProduct(newItem);
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
      int orderId = FormatManager.stringToInt(parts[0]);
      int customerId = FormatManager.stringToInt(parts[1]);
      String remark = parts[2];
      boolean isPending =  parts[3].equals("1");
      LocalDateTime orderDate = FormatManager.stringToLocalDate(parts[4]);
      LocalDateTime completeDate = FormatManager.stringToLocalDate(parts[5]);

      Order newItem = new Order(orderId,customerId,remark,isPending,orderDate,completeDate);
      mainObject.addNewOrder(newItem);
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
      int orderItemId = FormatManager.stringToInt(parts[0]);
      int orderId = FormatManager.stringToInt(parts[1]);
      int productId = FormatManager.stringToInt(parts[2]);
      int amount = FormatManager.stringToInt(parts[3]);
      double fullPrice = FormatManager.StringToDouble(parts[4]);
      double salePrice = FormatManager.StringToDouble(parts[5]);

      OrderItem newItem = new OrderItem(orderItemId, orderId, productId, amount, fullPrice, salePrice);
      mainObject.addNewOrderItem(newItem);
    } catch (ArrayIndexOutOfBoundsException ex) {
      //System.out.println("Empty line in" + filename);
    } catch (IllegalStateException ex) {  // Skip
      System.out.println("fields is not match content line: " + ex);
    }
  }

}