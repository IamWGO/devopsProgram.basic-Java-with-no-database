package test;

import service.FileService;
import dataclass.*;
import manager.ShopManager;
import utility.stats.FileState;
import utility.stats.MainState;

import java.util.ArrayList;
import java.util.Scanner;

public class TestManageFile {
  Scanner scan = new Scanner(System.in);

  MainState mainState = MainState.ORDER_ITEM;
  FileState fileState = FileState.READ;

  FileService fileService = new FileService(new ShopManager(), mainState,fileState);

  ArrayList<Admin> admins = new ArrayList<>();
  ArrayList<Customer> customers = new ArrayList<>();
  ArrayList<Product> products = new ArrayList<>();
  ArrayList<Order> orders = new ArrayList<>();
  ArrayList<OrderItem> orderItems = new ArrayList<>();
  String writeToFileText;


  public void menu(){
    boolean run = true;
    while (run) {
      printMenu();
      // select menu
      String choice = scan.nextLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> read();
        case "2" -> write();
        case "3" -> overwrite();
        case "Q" -> {
          run = false; // quit while loop
        }
        default -> System.out.println("Input a number or Q to exit program");
      }

    }
  }

  public MainState getMainState(){
    return mainState;
  }

  public FileState getFileState(){
    return fileState;
  }

  public String getWriteToFileText(){
    return writeToFileText;
  }

  public void addNewCustomer(Customer newItem){
    customers.add(newItem);
  }

  public void addNewProduct(Product newItem){
    products.add(newItem);
  }

  public void addNewOrder(Order newItem){
    orders.add(newItem);
  }

  public void addNewOrderItem(OrderItem newItem){
    orderItems.add(newItem);
  }

  public void addNewAdmin(Admin newItem){
    admins.add(newItem);
  }


  private void read(){
    fileState = FileState.READ;
    fileService.choose();
  }
  private void write(){
    fileState = FileState.NEW_LINE;
    switch (getMainState()) {
      case ADMIN -> {
        Admin newItem = new Admin("newadmin", "1234567");
        writeToFileText = newItem.objectToLineFormat();
        fileService.choose();
      }
      case PRODUCT -> {
        Product newItem = new Product(1, "Product 1", "product detail", 100);
        writeToFileText = newItem.objectToLineFormat();
        fileService.choose();
      }
      case CUSTOMER -> {
        Customer newItem = new Customer(2,"waleerat","Gottlieb","customer",
                "1234567","waleerat@gmail.com","","","",false);
        writeToFileText = newItem.objectToLineFormat();
        fileService.choose();
      }
      case ORDER -> {
        Order newItem = new Order(1,3,"Remark1", true);
        writeToFileText = newItem.objectToLineFormat();
        fileService.choose();
      }
      case ORDER_ITEM -> {
        OrderItem newItem = new OrderItem(2,1,1,12,233,200);
        writeToFileText = newItem.objectToLineFormat();
        fileService.choose();
      }

    }

  }
  private void overwrite(){
    fileState = FileState.OVERWRITE;
    writeToFileText = "";

    switch (getMainState()) {
      case ADMIN -> System.out.println("admin();");
      case PRODUCT -> {
        ArrayList<Product> dummyItems = new ArrayList<>();
        dummyItems.add(new Product(1, "Product 1","-",  100));
        dummyItems.add(new Product(1, "Product 2","-", 100));
        dummyItems.add(new Product(1, "Product 3","-", 100));

        for (int i = 0; i < dummyItems.size(); i++) {
          writeToFileText = writeToFileText.concat(dummyItems.get(i).objectToLineFormat());
          if (i < dummyItems.size()-1) writeToFileText = writeToFileText.concat("\n");
        }
        fileService.choose();
      }
      case CUSTOMER -> {
        ArrayList<Customer> dummyItems = new ArrayList<>();

        dummyItems.add(new Customer(1,"waleerat","Gottlieb","customer",
                "1234567","waleerat@gmail.com","","","",false));
        dummyItems.add(new Customer(2,"waleerat","Gottlieb","customer",
                "1234567","waleerat@gmail.com","","","",false));
        dummyItems.add(new Customer(3,"waleerat","Gottlieb","customer",
                "1234567","waleerat@gmail.com","","","",false));

        for (int i = 0; i < dummyItems.size(); i++) {
          writeToFileText = writeToFileText.concat(dummyItems.get(i).objectToLineFormat());
          if (i < dummyItems.size()-1) writeToFileText = writeToFileText.concat("\n");
        }
        fileService.choose();
      }
      case ORDER -> {
        ArrayList<Order> dummyItems = new ArrayList<>();
        dummyItems.add(new Order(1,3,"Remark1", true));
        dummyItems.add(new Order(2,3,"Remark2", true));
        dummyItems.add(new Order(3,3,"Remark3", true));
        dummyItems.add(new Order(4,3,"Remark4", true));

        for (int i = 0; i < dummyItems.size(); i++) {
          writeToFileText = writeToFileText.concat(dummyItems.get(i).objectToLineFormat());
          if (i < dummyItems.size()-1) writeToFileText = writeToFileText.concat("\n");
        }
        fileService.choose();
      }
      case ORDER_ITEM -> {
        ArrayList<OrderItem> dummyItems = new ArrayList<>();
        dummyItems.add(new OrderItem(1,1,1,12,233,200));
        dummyItems.add(new OrderItem(2,1,2,12,233,200));
        dummyItems.add(new OrderItem(3,1,3,12,233,200));

        for (int i = 0; i < dummyItems.size(); i++) {
          writeToFileText = writeToFileText.concat(dummyItems.get(i).objectToLineFormat());
          if (i < dummyItems.size()-1) writeToFileText = writeToFileText.concat("\n");
        }
        fileService.choose();
      }
    }





  }

  void printMenu() {
    System.out.println("""
            ::::::::::::::::::: MENU :::::::::::::::::::
            1. Read
            2. Write
            3. Rewrite
            Q. Quit
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");

    System.out.print("Input choice: ");


  }
}
