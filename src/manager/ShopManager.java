package manager;

import service.FileService;
import dataclass.*;
import shop.Backend;
import shop.Frontend;
import utility.stats.FileState;
import utility.stats.MainState;
import utility.view.DefaultView;

import java.util.ArrayList;
import java.util.Scanner;

public class ShopManager {
  DefaultView view  = new DefaultView(this);
  Scanner scan = new Scanner(System.in);
  public AuthManager authObject = new AuthManager(this);

  FileService fileService;
  public MainState mainState;
  FileState fileState;
  String writeToFileText;

  public ArrayList<Admin> admins = new ArrayList<>();
  public ArrayList<Customer> customers = new ArrayList<>();
  public ArrayList<Product> products = new ArrayList<>();
  public ArrayList<Order> orders = new ArrayList<>();
  public ArrayList<OrderItem> orderItems = new ArrayList<>();
  public ArrayList<OrderItem> tempOrderItems = new ArrayList<>();

  boolean isAdmin;
  boolean isAdminLogin = false;
  boolean isCustomerLogin = false;
  int loginCustomerId = 0;

  // get all data to ArrayList when MainController loaded
  public ShopManager() {
    readAllFiles();
  }

/*TODO :
   1. check: read all files in the MainController : Admin, Customer, Product, Order, OrderList
   2. use Composition technic
   3. File manager create, read, write : FileManager
   4. Helper class : converting type, Read write file
*  */

  public void menu(){
    // get data from text files

    boolean run = true;
    while (run) {
      printMenu();
      // select menu
      String choice = scan.nextLine();
      view.printEmptyLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> {
          this.setIsAdmin(true);
          authObject.doLogin();
          if (this.getIsAdminLogin() && this.getIsAdmin()) {
            Backend backend = new Backend(this, authObject);
            backend.menu();
          }
        }
        case "2" -> {
          this.setIsAdmin(false);
          Frontend frontend = new Frontend(this, authObject);
          frontend.menu();
        }
        case "Q" -> {
          view.printText("Exit Program :)");
          run = false; // quit while loop
        }
        default -> view.printText("Input a number or Q to exit program\n");
      }
    }

  }

  public boolean getIsAdmin() {
    return isAdmin;
  }

  public boolean getIsAdminLogin() {
    return isAdminLogin;
  }

  public boolean getIsCustomerLogin() {
    return isCustomerLogin;
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

  public boolean isHasAdminPermission(){
    if (!getIsAdminLogin())  view.printNoPermission();
    return getIsAdminLogin();
  }

  public boolean isHasCustomerPermission(){
    if (!getIsCustomerLogin())  view.printNoPermission();
    return getIsCustomerLogin();
  }

  public boolean isHasBothPermission(){
    boolean isHasPermission = (getIsCustomerLogin() || getIsAdminLogin());
    if (!isHasPermission)  view.printNoPermission();
    return isHasPermission;
  }

  public int getLoginCustomerId() {
    return loginCustomerId;
  }

  public ArrayList<OrderItem> getTempOrderItems() {
    return tempOrderItems;
  }

  public void setIsAdmin(boolean newValue) {
    isAdmin = newValue;
  }

  public void setAdminLogin(boolean newValue) {
    isAdminLogin = newValue;
  }

  public void setCustomerLogin(boolean newValue) {
    isCustomerLogin = newValue;
  }

  public void setLoginCustomerId(int customerId){
    loginCustomerId = customerId;
  }

  public void loginForNewCustomer(int customerId){
    isCustomerLogin = true;
    setLoginCustomerId(customerId);
  }

  public void addNewAdmin(Admin newItem) {
    this.admins.add(newItem);
  }

  // Get newItem from FileManager->addNewCustomer
  public void addNewCustomer(Customer newItem) {
    this.customers.add(newItem);
  }
  // Get newItem from FileManager->addNewProduct
  public void addNewProduct(Product newItem) {
    this.products.add(newItem);
  }
  // Get newItem from FileManager->addNewOrder
  public void addNewOrder(Order newItem) {
    this.orders.add(newItem);
  }
  // Get newItem from FileManager->addNewOrderItem
  public void addNewOrderItem(OrderItem newItem) {
    this.orderItems.add(newItem);
  }

  public void readAllFiles(){
    //Loop enum to reduce lines
    //ADMIN,PRODUCT,CUSTOMER,ORDER,ORDERITEM,
    for (MainState mainStateItem : MainState.values()) {
      mainState = mainStateItem;
      fileState = FileState.READ;
      fileService = new FileService(this, mainState, fileState);
      fileService.choose();
    }
  }

  //TODO : Add item
  public void addNewLine(String contentLine){
    this.writeToFileText = contentLine;
    this.fileState = FileState.NEW_LINE;
    fileService.choose();
  }

  //TODO : Update and Delete
  public void overwriteFile(String contentLines){
    this.writeToFileText = contentLines;
    //Write file
    this.fileState = FileState.OVERWRITE;
    fileService.choose();
    //
  }

  void printMenu() {
    System.out.println("""
            ::::::::::::::::::: Main Menu :::::::::::::::::::
            1. Backend
            2. Frontend
            Q. Exit Program
            :::::::::::::::::::::::::::::::::::::::::::::::::::::::""");
    System.out.print("Input choice: ");
  }
}