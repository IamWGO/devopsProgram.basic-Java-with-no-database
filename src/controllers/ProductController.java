package controllers;

import dataclass.OrderItem;
import dataclass.Product;
import shop.Frontend;
import utility.stats.MainState;
import utility.stats.OperationState;
import utility.view.ProductView;

import java.util.ArrayList;
import java.util.Scanner;

public class ProductController {
  Scanner scan = new Scanner(System.in);
  ProductView view = new ProductView();

  OrderController orderObject;
  MainController mainObject;
  public ProductController(MainController mainObject) {
    this.mainObject = mainObject;
    orderObject = new OrderController(mainObject);
  }

  public void menu() {
    if (!mainObject.isHasAdminPermission()) return;

    boolean run = true;
    while (run) {
      view.printBackendMenu();
      // select menu
      String choice = scan.nextLine();
      view.printEmptyLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> listAll();
        case "3" -> newItem();
        case "4" -> update();
        case "5" -> delete();
        case "Q" -> run = false; // quit while loop
        default -> view.printMenuWarning();
      }
    }
  }

  // ++ Frontend ++ ------------------------------------------------------------------------------
  public void frontendProductList(){
    listAll();

    boolean run = true;
    while (run) {
      view.printSelectToCartMenu(mainObject.tempOrderItems);
      // select menu
      String choice = scan.nextLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> listAll();
        case "2" -> addProductToCart();
        case "3" -> viewCart();
        case "4" -> {
          if (mainObject.tempOrderItems.isEmpty()) view.printMenuWarning();
          else  deleteOrderItem();
        }
        case "5" -> {
          if (mainObject.tempOrderItems.isEmpty()) view.printMenuWarning();
          else  confirmOrder();
        }
        case "Q" ->  run = false;
        default -> view.printMenuWarning();
      }
    }
  }

  private void addProductToCart() {
    while (true) {
      listAll();

      view.printAddToCartMenu(OperationState.ADD);
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;

      int itemIndex = searchByInputNumber(inputString);
      if (itemIndex == -1){
        view.printNotFound();
        return;
      }

      //TODO : Check add product more than once
      boolean isNewItem = true;
      Product selectedItem = mainObject.products.get(itemIndex);
      for (int i = 0; i < mainObject.tempOrderItems.size(); i++) {
        if (mainObject.tempOrderItems.get(i).getProductId() == selectedItem.getProductId()){
          // amount + 1 if already added to cart
          int amount = mainObject.tempOrderItems.get(i).getAmount() + 1;
          mainObject.tempOrderItems.get(i).setAmount(amount);
          isNewItem = false;
          break;
        }
      }

      if (isNewItem) {
        // Add to cart if new product
        orderObject.addTempOrderItem(selectedItem, 1);
        view.printSuccessAddTempOrderItem(selectedItem);
      }

    }

  }

  private void viewCart() {
    // List selected product items (order items)
    orderObject.listOrderItems(mainObject.tempOrderItems);
  }
  private void deleteOrderItem() {

    while (true) {
      viewCart();

      view.printAddToCartMenu(OperationState.DELETE);
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();
      if (inputString.equalsIgnoreCase("q")) break;

      int itemIndex = -1;
      try {
        int choice = Integer.parseInt(inputString);
        // if choice is not in range then return -1
        if (choice > 0 || choice < (mainObject.tempOrderItems.size())) {
          itemIndex = choice-1;
        }
      } catch (NumberFormatException ex) {
        view.printNotFound();
        return;
      }

      if (itemIndex == -1) {
        view.printNotFound();
        return;
      }

      // remove item
      OrderItem selectedItem = mainObject.tempOrderItems.get(itemIndex);
      mainObject.tempOrderItems.remove(itemIndex);

      // get deleted product
      for (int i = 0; i < mainObject.products.size(); i++) {
        if (mainObject.products.get(i).getProductId() == selectedItem.getProductId()) {
          view.printSuccessDeleteTempOrderItem(mainObject.products.get(i));
          break;
        }
      }

    }
  }

  private void confirmOrder(){
    if (!mainObject.isHasCustomerPermission()) {
      Frontend frontend = new Frontend(mainObject, mainObject.authObject);
      frontend.authenticationMenu();
      //mainObject.authObject.customerLogin();
      return;
    }

    if (mainObject.tempOrderItems.isEmpty()) {
      view.printEmptyItem();
      return;
    }

    // confirm order and clear temp order items
    orderObject.confirmOrder();
    mainObject.tempOrderItems = new ArrayList<>();

  }

  // ++ Backend ++ ------------------------------------------------------------------------------
  private void listAll() {
    view.printHeadLine();
    if (mainObject.products.isEmpty()) {
      view.printNotFound();
      return;
    }

    for (int i = 0; i < mainObject.products.size(); i++) {
      view.printRow(i, mainObject.products.get(i));
    }
    view.printFooter();
  }

  private void newItem() {
    if (!mainObject.isHasAdminPermission()) return;
    //set mainState to get filename
    mainObject.mainState = MainState.PRODUCT;

    view.printText("::::: New Product ::::: \n");
    String productName = newItemInputForm("Product Name");
    double price;
    do {
      String priceString = newItemInputForm("Price");
      try {
        price = Double.parseDouble(priceString);
        break;
      } catch (NumberFormatException e) {
        view.printText("Price have to be number!! \n");
      }

    } while(true);

    int productId = getNextId();
    Product newItem = new Product(productId, productName, price);
    String contentLine = newItem.objectToLineFormat();
    mainObject.products.add(newItem);
    mainObject.addNewLine(contentLine);

    view.printSuccessAddProduct(newItem);

  }

  //for backend
  private void update() {
    if (!mainObject.isHasAdminPermission()) return;

    while (true) {
      listAll();

      view.printUpdateProductMenu();
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;

      int itemIndex = searchByInputNumber(inputString);

      if (itemIndex == -1){
        view.printNotFound();
        return;
      }

      Product selectedItem = mainObject.products.get(itemIndex);
      view.printText("::::: Update Product ::::: \n");
      view.printText("-- Enter to skip edit --  \n");
      String productName = updateItemInputForm(selectedItem.getProductName(),"Product Name");

      double price;
      do {
        String priceString = updateItemInputForm(selectedItem.getPriceString(),"price");
        try {
          price = Double.parseDouble(priceString);
          break;
        } catch (NumberFormatException e) {
          view.printText("Price have to be number!! \n");
        }

      } while(true);

      // Confirm Update
      view.printText("\n Confirm to update? (y/n)  : ");
      String inputString1 = scan.nextLine();

      if (!inputString1.equalsIgnoreCase("y")) {
        view.printCancel();
        return;
      }

      // Do update product
      mainObject.products.get(itemIndex).setProductInfo(productName, price);
      view.printUpdateResult(selectedItem);

      // Rewrite file because we remove an item form ArrayList
      rewriteFile();

    }
  }

  //TODO : Product delete for backend
  private void delete() {
    if (!mainObject.isHasAdminPermission()) return;

    if (mainObject.products.isEmpty()) {
      view.printEmptyItem();
      return;
    }


    if (!mainObject.isHasAdminPermission()) return;

    while (true) {
      listAll();

      view.printUpdateProductMenu();
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;

      int itemIndex = searchByInputNumber(inputString);

      if (itemIndex == -1){
        view.printNotFound();
        return;
      }

      Product selectedItem = mainObject.products.get(itemIndex);

      // Confirm Update
      view.printText("\n Confirm to update? (y/n)  : ");
      String inputString1 = scan.nextLine();

      if (!inputString1.equalsIgnoreCase("y")) {
        view.printCancel();
        return;
      }

      // remove Item
      mainObject.products.remove(itemIndex);
      view.printDeleteResult(selectedItem);

      // Rewrite file because we remove an item form ArrayList
      rewriteFile();

    }

  }

  // ++ HelpFunction ++ ------------------------------------------------------------------------------
  private void rewriteFile() {
    if (!mainObject.isHasAdminPermission()) return;

    //set mainState to get filename
    mainObject.mainState = MainState.PRODUCT;

    // Rewrite file because we remove an item form ArrayList
    String contentLines = "";
    ArrayList<Product> tempProducts = new ArrayList<>();
    for (int i = 0; i < mainObject.products.size(); i++) {
      contentLines = contentLines.concat(mainObject.products.get(i).objectToLineFormat());
      if (i < mainObject.products.size()-1) contentLines = contentLines.concat("\n");

      tempProducts.add(mainObject.products.get(i));
    }

    mainObject.products = tempProducts;
    mainObject.overwriteFile(contentLines);
  }

  private int getNextId(){
    if (mainObject.products.isEmpty()) return 1;

    int maxProductId = -1;

    for (Product product : mainObject.products) {
      if (product.getProductId() > maxProductId) {
        maxProductId = product.getProductId();
      }
    }
    maxProductId++;
    return maxProductId;
  }

  private int searchByItemIndex(int itemIndex){
    //search start from 0 ... n
    if (itemIndex < 0 || itemIndex > (mainObject.products.size())) {
      return -1;
    }
    return itemIndex;
  }

  private int searchByInputNumber(String inputNumber){
    try {
      int choice = Integer.parseInt(inputNumber);
      // if choice is not in range then return -1
      if (choice < 0 || choice > (mainObject.products.size())) {
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
