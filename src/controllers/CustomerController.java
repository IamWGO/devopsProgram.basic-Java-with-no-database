package controllers;

import dataclass.Customer;
import utility.stats.MainState;
import utility.stats.OperationState;
import utility.view.CustomerView;

import java.util.ArrayList;
import java.util.Scanner;

public class CustomerController {
  Scanner scan = new Scanner(System.in);
  CustomerView view = new CustomerView();

  MainController mainObject;
  public CustomerController(MainController mainObject) {
    this.mainObject = mainObject;
  }

  public void menu() {
    if (!mainObject.isHasAdminPermission()) return;

    boolean run = true;
    while (run) {
      view.printBackendMenu();
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      // toUpperCase to check Q command
      switch (inputString.toUpperCase()) {
        case "1" -> listAll();
        case "2" -> search();
        case "3" -> viewCustomerOrder();
        case "4" -> updateStatus();
        case "5" -> delete();
        case "Q" -> run = false; // quit while loop
        default -> view.printMenuWarning();
      }
    }
  }

  //++ Frontend ++ ------------------------------------------------------------------------------
  public void register(){
    //set mainState to get filename
    mainObject.mainState = MainState.CUSTOMER;
    //TODO : validate
    view.printText("::::: Register Customer ::::: \n");
    String firstname = newItemInputForm("firstname");
    String lastname = newItemInputForm("lastname");
    String username = newItemInputForm("username");
    String password = newItemInputForm("password");
    String email = newItemInputForm("email");
    String address = newItemInputForm("address");
    String zipCode = newItemInputForm("zipCode");
    String country = newItemInputForm("country");

    view.printText("\n Confirm to update? (y/n)  : ");
    String inputString = scan.nextLine();


    if (!inputString.equalsIgnoreCase("y")) {
      view.printCancel();
      return;
    }

    int customerId = getNextId();
    Customer newItem = new Customer(customerId,firstname,lastname,username,
            password,email,address,zipCode,country,true
    );
    String contentLine = newItem.objectToLineFormat();
    mainObject.customers.add(newItem);
    mainObject.addNewLine(contentLine);
    view.printAddResult(newItem);

    // Set customer
    mainObject.loginForNewCustomer(customerId);
  }

  private void updateForm(int indexItem){
    if (!mainObject.isHasBothPermission()) return;

    Customer selectedItem = mainObject.customers.get(indexItem);

    view.printText("::::: Update Customer ::::: \n");
    view.printText("-- Enter to skip edit --  \n");
    String firstname = updateItemInputForm(selectedItem.getFirstname(),"firstname");
    String lastname = updateItemInputForm(selectedItem.getLastname(),"lastname");
    String username = updateItemInputForm(selectedItem.getUsername(),"username");
    String password = updateItemInputForm(selectedItem.getPassword(),"password");
    String email = updateItemInputForm(selectedItem.getEmail(),"email");
    String address = updateItemInputForm(selectedItem.getAddress(),"address");
    String zipCode = updateItemInputForm(selectedItem.getZipCode(),"zipCode");
    String country = updateItemInputForm(selectedItem.getCountry(),"country");


    view.printText("\n Confirm to update? (y/n)  : ");
    String inputString = scan.nextLine();

    if (!inputString.equalsIgnoreCase("y")) {
      view.printCancel();
      return;
    }

    Customer updateItem = new Customer(selectedItem.getCustomerId(),firstname,lastname,username,
            password,email,address,zipCode,country,selectedItem.getIsActive()
    );

    // Update Customer
    mainObject.customers.get(indexItem).updateItem(updateItem);
    view.printUpdateResult(updateItem);

    // Overwrite file
    rewriteFile();

  }

  //++ Backend ++ ------------------------------------------------------------------------------
  private void listAll() {
    if (!mainObject.isHasAdminPermission()) return;

    if (mainObject.customers.isEmpty()) {
      view.printHeadLine();
      view.printNotFound();
      return;
    }

    showCustomerList();
  }

  private void showCustomerList(){
    if (!mainObject.isHasBothPermission()) return;

    view.printHeadLine();
    for (int i = 0; i < mainObject.customers.size(); i++) {
      view.printRow(i, mainObject.customers.get(i));
    }
    view.printFooter();
  }

  private void updateStatus(){
    if (!mainObject.isHasAdminPermission()) return;

    if (mainObject.customers.isEmpty()) {
      view.printEmptyItem();
      return;
    }

    while (true) {
      showCustomerList();

      view.printCustomerMenu(OperationState.UPDATE_STATUS);
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = searchByInputNumber(inputString);
      Customer selectedItem = mainObject.customers.get(itemIndex);
      selectedItem.setActive();

      // Overwrite file
      rewriteFile();
    }
  }

  private void delete() {
    if (!mainObject.isHasAdminPermission()) return;

    if (mainObject.customers.isEmpty()) {
      view.printEmptyItem();
      return;
    }

    while (true) {
      showCustomerList();

      view.printCustomerMenu(OperationState.DELETE);
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = searchByInputNumber(inputString);
      Customer selectedItem = mainObject.customers.get(itemIndex);

      if (!checkIfHasOrder(selectedItem.getCustomerId())) {
        mainObject.customers.remove(itemIndex);
        // Overwrite file
        rewriteFile();
        view.printDeleteResult(true, selectedItem);
      } else {
        view.printDeleteResult(false, selectedItem);
      }
    }
  }

  private void search() {
    if (!mainObject.isHasAdminPermission()) return;

    if (mainObject.customers.isEmpty()) {
      view.printEmptyItem();
      return;
    }

    while (true) {
      showCustomerList();

      view.printCustomerMenu(OperationState.SEARCH);
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;
      int itemIndex = searchByInputNumber(inputString);
      viewProfileByItemIndex(itemIndex);
    }
  }

  //++ Both frontend and backend  ++ ------------------------------------------------------------------------------
  public void viewProfileByCustomerId(int customerId) {
    if (!mainObject.isHasBothPermission()) return;

    int itemIndex = getCustomerIndexItemById(customerId);

    if (itemIndex == -1){
      view.printNotFound();
      return;
    }
    // View Profile
    showProfile(itemIndex);
  }

  private void viewProfileByItemIndex(int searchIndex){
    if (!mainObject.isHasBothPermission()) return;

    int itemIndex = searchByItemIndex(searchIndex);

    if (itemIndex == -1){
      view.printNotFound();
      return;
    }
    // View Profile
    showProfile(itemIndex);
  }

  private void showProfile(int itemIndex){
    if (!mainObject.isHasBothPermission()) return;

    view.printDetail(mainObject.customers.get(itemIndex));
    view.printText("\n Confirm Order? (y/n)  : ");

    String inputString = scan.nextLine();
    if (!inputString.equalsIgnoreCase("y")) {
      view.printCancel();
      return;
    }

    updateForm(itemIndex);
  }

  private void viewCustomerOrder(){
    if (!mainObject.isHasAdminPermission()) return;

    while (true) {
      showCustomerList();

      view.printCustomerMenu(OperationState.VIEW_ORDER);
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;

      int itemIndex = searchByInputNumber(inputString);
      Customer selectedItem = mainObject.customers.get(itemIndex);

      OrderController orderObject = new OrderController(mainObject);
      orderObject.orderHistory(selectedItem.getCustomerId());
    }
  }

  // ++ Help Methods ++ ------------------------------------------------------------------------------
  private boolean checkIfHasOrder(int customerId) {
    for (int i = 0; i < mainObject.orders.size(); i++) {
      if ( mainObject.orders.get(i).getCustomerId() == customerId)
        return true;
    }
    return false;
  }

  private void rewriteFile() {
    if (!mainObject.isHasBothPermission()) return;

    //set mainState to get filename
    mainObject.mainState = MainState.CUSTOMER;
    // Rewrite file because we remove an item form ArrayList
    String contentLines = "";
    ArrayList<Customer> tempCustomers = new ArrayList<>();
    for (int i = 0; i < mainObject.customers.size(); i++) {
      contentLines = contentLines.concat(mainObject.customers.get(i).objectToLineFormat());
      if (i < mainObject.customers.size()-1) contentLines = contentLines.concat("\n");

      tempCustomers.add(mainObject.customers.get(i));
    }

    mainObject.customers = tempCustomers;
    mainObject.overwriteFile(contentLines);
  }

  private int getCustomerIndexItemById(int customerId){
    for (int i = 0; i < mainObject.customers.size(); i++) {
      if (mainObject.customers.get(i).getCustomerId() == customerId) {
        return i;
      }
    }
    return -1;
  }
  private int getNextId(){
    if (mainObject.customers.isEmpty()) return 1;

    int maxProductId = -1;

    for (Customer customer : mainObject.customers) {
      if (customer.getCustomerId() > maxProductId) {
        maxProductId = customer.getCustomerId();
      }
    }
    maxProductId++;
    return maxProductId;
  }

  private int searchByItemIndex(int itemIndex){
    //search start from 0 ... n
    if (itemIndex < 0 || itemIndex > (mainObject.customers.size())) {
      return -1;
    }
    return itemIndex;
  }

  private int searchByInputNumber(String inputString){
    try {
      int choice = Integer.parseInt(inputString);
      // if choice is not in range then return -1
      if (choice < 0 || choice > (mainObject.customers.size())) {
        return -1;
      }
      // return selected index
      return choice - 1;
    } catch (NumberFormatException ex) {
      // if user input string then return -1
      return  -1;
    }
  }

  private String newItemInputForm(String label){
    view.printText(label + " : ");
    return scan.nextLine();
  }

  private String updateItemInputForm(String oldString, String label) {
    view.printText(label + " : " + oldString + "  > ");
    String inputString = scan.nextLine();
    return (inputString.isEmpty()) ? oldString : inputString;
  }

}
