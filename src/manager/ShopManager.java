package manager;

import dataclass.*;
import manager.AuthManager;
import service.FileService;
import utility.stats.FileState;
import utility.stats.MainState;
import view.OrderView;
import view.OutPut;

import java.util.ArrayList;
import java.util.Scanner;

public class ShopManager {
  Scanner scan = new Scanner(System.in);
  OutPut outPut = new OrderView();
  //composition technic
  public AuthManager authObject = new AuthManager(this);

  public ShopManager() {
    // get all data to ArrayList when program loaded
    readAllFiles();
  }

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

  //---------------- Permission Control
  public void loginForNewCustomer(int customerId){
    isCustomerLogin = true;
    setLoginCustomerId(customerId);
  }

  public boolean isHasAdminPermission(){
    if (!getIsAdminLogin())  outPut.printNoPermission();
    return getIsAdminLogin();
  }

  public boolean isHasCustomerPermission(){
    if (!getIsCustomerLogin())  outPut.printNoPermission();
    return getIsCustomerLogin();
  }

  public boolean isHasBothPermission(){
    boolean isHasPermission = (getIsCustomerLogin() || getIsAdminLogin());
    if (!isHasPermission)  outPut.printNoPermission();
    return isHasPermission;
  }

  //---------------- Read/Write file control state
  public MainState getMainState(){
    return mainState;
  }
  public void setMainState(MainState state) {
    mainState = state;
  }

  //----------------- Manage File Methods -------------------
  public FileState getFileState(){
    return fileState;
  }
  public String getWriteToFileText(){
    return writeToFileText;
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

  // Read all files when program loaded
  public void readAllFiles(){
    //ADMIN,PRODUCT,CUSTOMER,ORDER,ORDERITEM,
    for (MainState mainStateItem : MainState.values()) {
      mainState = mainStateItem;
      fileState = FileState.READ;
      fileService = new FileService(this, mainState, fileState);
      fileService.choose();
    }
  }

  public void addNewLine(String contentLine){
    this.writeToFileText = contentLine;
    this.fileState = FileState.NEW_LINE;
    fileService.choose();
  }

  public void overwriteFile(String contentLines){
    this.writeToFileText = contentLines;
    //Write file
    this.fileState = FileState.OVERWRITE;
    fileService.choose();
    //
  }

  //----------------- getter and setter
  public boolean getIsAdmin() {
    return isAdmin;
  }

  public boolean getIsAdminLogin() {
    return isAdminLogin;
  }

  public boolean getIsCustomerLogin() {
    return isCustomerLogin;
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
}
