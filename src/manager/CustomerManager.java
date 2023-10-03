package manager;

import dataclass.Customer;
import utility.stats.MainState;
import utility.view.CustomerView;

import java.util.ArrayList;
import java.util.Scanner;

public class CustomerManager {
  Scanner scan = new Scanner(System.in);
  CustomerView view = new CustomerView();

  ShopManager mainObject;
  public CustomerManager(ShopManager mainObject) {
    this.mainObject = mainObject;
  }

  public void showCustomerList(){
    if (!mainObject.isHasBothPermission()) return;

    view.printHeadLine();
    for (int i = 0; i < mainObject.customers.size(); i++) {
      view.printRow(i, mainObject.customers.get(i));
    }
    view.printFooter();
  }


  public Customer newItemForm(){
    view.printText("::::: Register Customer ::::: \n");
    String firstname = newItemInputForm("firstname");
    String lastname = newItemInputForm("lastname");
    String username = newItemInputForm("username");
    String password = newItemInputForm("password");
    String email = newItemInputForm("email");
    String address = newItemInputForm("address");
    String zipCode = newItemInputForm("zipCode");
    String country = newItemInputForm("country");

    int customerId = getNextId();
    return  new Customer(customerId,firstname,lastname,username,
            password,email,address,zipCode,country,true
    );
  }

  public void addNewItem(Customer newItem){
    String contentLine = newItem.objectToLineFormat();
    mainObject.customers.add(newItem);
    mainObject.addNewLine(contentLine);
    view.printAddResult(newItem);
  }

  public Customer updateProfile(Customer selectedItem){
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
    return  new Customer(selectedItem.getCustomerId(),firstname,lastname,username,
            password,email,address,zipCode,country,selectedItem.getIsActive()
    );
  }

  public void updateItem(Customer selectedItem, Customer updateItem){
    // Update Customer
    selectedItem.updateItem(updateItem);
    view.printUpdateResult(updateItem);

    // Overwrite file
    rewriteFile();
  }

  public void updateStatus(int itemIndex){
    Customer selectedItem = mainObject.customers.get(itemIndex);
    selectedItem.setActive();
    // Overwrite file
    rewriteFile();
  }

  public void deleteCustomer(int itemIndex){
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

  public boolean checkIfHasOrder(int customerId) {
    for (int i = 0; i < mainObject.orders.size(); i++) {
      if ( mainObject.orders.get(i).getCustomerId() == customerId)
        return true;
    }
    return false;
  }

  public void rewriteFile() {
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

  public int getCustomerIndexItemById(int customerId){
    for (int i = 0; i < mainObject.customers.size(); i++) {
      if (mainObject.customers.get(i).getCustomerId() == customerId) {
        return i;
      }
    }
    return -1;
  }
  public int getNextId(){
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

  public int searchByItemIndex(int itemIndex){
    //search start from 0 ... n
    if (itemIndex < 0 || itemIndex > (mainObject.customers.size())) {
      return -1;
    }
    return itemIndex;
  }

  public int searchByInputNumber(String inputString){
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

  public String newItemInputForm(String label){
    view.printText(label + " : ");
    return scan.nextLine();
  }

  public String updateItemInputForm(String oldString, String label) {
    view.printText(label + " : " + oldString + "  > ");
    String inputString = scan.nextLine();
    return (inputString.isEmpty()) ? oldString : inputString;
  }
}
