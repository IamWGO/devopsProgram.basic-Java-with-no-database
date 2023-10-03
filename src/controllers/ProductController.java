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
  ShopManager shopManager;
  ProductManager productManager;

  public ProductController(ShopManager shopManager) {
    this.shopManager = shopManager;
    orderManager = new OrderManager(shopManager);
    productManager = new ProductManager(shopManager);
  }

  public void menu() {
    if (!shopManager.isHasAdminPermission()) return;

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
      view.printSelectToCartMenu(shopManager.tempOrderItems);
      // select menu
      String choice = scan.nextLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> listAll();
        case "2" -> addProductToCart();
        case "3" -> viewCart();
        case "4" -> {
          if (shopManager.tempOrderItems.isEmpty()) view.printMenuWarning();
          else  deleteOrderItem();
        }
        case "5" -> {
          if (shopManager.tempOrderItems.isEmpty()) view.printMenuWarning();
          else  orderManager.confirmOrder();
        }
        case "Q" ->  run = false;
        default -> view.printMenuWarning();
      }
    }
  }

  private void listAll() {
    view.printHeadLine();
    if (shopManager.products.isEmpty()) {
      view.printNotFound();
      return;
    }
    productManager.showProductList();
  }

  private void newItem() {
    if (!shopManager.isHasAdminPermission()) return;
    //set mainState to get filename
    shopManager.mainState = MainState.PRODUCT;
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
    if (!shopManager.isHasAdminPermission()) return;

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

      Product selectedItem = shopManager.products.get(itemIndex);
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
    orderManager.listOrderItems(shopManager.tempOrderItems);
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
        if (choice > 0 || choice < (shopManager.tempOrderItems.size())) {
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
    if (!shopManager.isHasAdminPermission()) return;

    if (shopManager.products.isEmpty()) {
      view.printEmptyItem();
      return;
    }

    if (!shopManager.isHasAdminPermission()) return;

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

      Product selectedItem = shopManager.products.get(itemIndex);

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
