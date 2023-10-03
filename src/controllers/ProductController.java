package controllers;

import dataclass.Product;
import manager.OrderManager;
import manager.ProductManager;
import manager.ShopManager;
import utility.stats.MainState;
import utility.stats.OperationState;
import utility.view.ProductView;

import java.util.Scanner;

public class ProductController {
  Scanner scan = new Scanner(System.in);
  ProductView view = new ProductView();

  OrderManager orderManager;
  ProductManager productManager;
  ShopManager mainObject;
  public ProductController(ShopManager mainObject) {
    this.mainObject = mainObject;
    orderManager = new OrderManager(mainObject);
    productManager = new ProductManager(mainObject);
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
        case "2" -> newItem();
        case "3" -> update();
        case "4" -> delete();
        case "Q" -> run = false; // quit while loop
        default -> view.printMenuWarning();
      }
    }
  }

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
          else  orderManager.confirmOrder();
        }
        case "Q" ->  run = false;
        default -> view.printMenuWarning();
      }
    }
  }

  private void listAll() {
    view.printHeadLine();
    if (mainObject.products.isEmpty()) {
      view.printNotFound();
      return;
    }
    productManager.showProductList();
  }

  private void newItem() {
    if (!mainObject.isHasAdminPermission()) return;
    //set mainState to get filename
    mainObject.mainState = MainState.PRODUCT;
    //Add new item
    Product newItem = productManager.newItemForm();

    // optional
    view.printText("\n Confirm to add? (y/n)  : ");
    String inputString1 = scan.nextLine();

    if (!inputString1.equalsIgnoreCase("y")) {
      view.printCancel();
      return;
    }

    productManager.addNewItem(newItem);

  }

  private void update() {
    if (!mainObject.isHasAdminPermission()) return;

    while (true) {
      listAll();

      view.printUpdateProductMenu();
      // select menu
      String inputString = scan.nextLine();
      view.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;

      int itemIndex = productManager.searchByInputNumber(inputString);

      if (itemIndex == -1){
        view.printNotFound();
        return;
      }

      Product selectedItem = mainObject.products.get(itemIndex);
      Product updateItem = productManager.updateItemForm(selectedItem);

      // Confirm Update
      view.printText("\n Confirm to update? (y/n)  : ");
      String inputString1 = scan.nextLine();

      if (!inputString1.equalsIgnoreCase("y")) {
        view.printCancel();
        return;
      }

      // Do update product
      productManager.updateItem(selectedItem, updateItem);

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

      int itemIndex = productManager.searchByInputNumber(inputString);
      if (itemIndex == -1) {
        view.printNotFound();
        return;
      }
      //Add to cart
      productManager.addToCart(itemIndex);
    }

  }

  private void viewCart() {
    // List selected product items (order items)
    orderManager.listOrderItems(mainObject.tempOrderItems);
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
      productManager.removeTempOrderItem(itemIndex);

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

      int itemIndex = productManager.searchByInputNumber(inputString);

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
      productManager.removeProductItem(itemIndex);
      view.printDeleteResult(selectedItem);
    }

  }

}
