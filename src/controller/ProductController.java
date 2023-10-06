package controller;

import dataclass.Product;
import manager.OrderManager;
import manager.ProductManager;
import manager.ShopManager;
import utility.stats.MainState;
import view.ProductView;
import java.util.Scanner;


public class ProductController {
  Scanner scan = new Scanner(System.in);
  ProductView outPut = new ProductView();
  ShopManager shopManager;
  ProductManager productManager;
  OrderManager orderManager;

  public ProductController(ShopManager shopManager) {
    this.shopManager = shopManager;
    productManager = new ProductManager(shopManager);
    orderManager = new OrderManager(shopManager);
  }

  public void menu() {
    if (!shopManager.isHasAdminPermission()) return;

    boolean run = true;
    while (run) {
      outPut.printBackendMenu();
      // select menu
      String choice = scan.nextLine();
      outPut.printEmptyLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> listAll();
        case "2" -> newItem();
        case "3" -> update();
        case "4" -> delete();
        case "Q" -> run = false; // quit while loop
        default -> outPut.printMenuWarning();
      }
    }
  }

  private void listAll() {
    if (shopManager.products.isEmpty()) {
      outPut.printHeadLine();
      outPut.printNotFound();
      return;
    }
    productManager.showProductList();
  }

  private void newItem() {
    if (!shopManager.isHasAdminPermission()) return;

    //Add new item
    Product newItem = productManager.newItemForm();

    // optional
    outPut.print("\n Confirm to add? (y/n)  : ");
    String inputString1 = scan.nextLine();

    if (!inputString1.equalsIgnoreCase("y")) {
      outPut.printCancel();
      return;
    }

    productManager.addNewItem(newItem);

  }

  private void update() {
    if (!shopManager.isHasAdminPermission()) return;

    while (true) {
      listAll();

      outPut.printUpdateProductMenu();
      // select menu
      String inputString = scan.nextLine();
      outPut.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;

      int itemIndex = productManager.searchByInputNumber(inputString);

      if (itemIndex == -1){
        outPut.printNotFound();
        return;
      }

      Product selectedItem = shopManager.products.get(itemIndex);
      Product updateItem = productManager.updateItemForm(selectedItem);

      // Confirm Update
      outPut.print("\n Confirm to update? (y/n)  : ");
      String inputString1 = scan.nextLine();

      if (!inputString1.equalsIgnoreCase("y")) {
        outPut.printCancel();
        return;
      }

      // Do update product
      productManager.updateItem(selectedItem, updateItem);

    }
  }

  private void delete() {
    if (!shopManager.isHasAdminPermission()) return;

    if (shopManager.products.isEmpty()) {
      outPut.printEmptyItem();
      return;
    }

    if (!shopManager.isHasAdminPermission()) return;

    while (true) {
      listAll();

      outPut.printUpdateProductMenu();
      // select menu
      String inputString = scan.nextLine();
      outPut.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;

      int itemIndex = productManager.searchByInputNumber(inputString);

      if (itemIndex == -1){
        outPut.printNotFound();
        return;
      }

      // Confirm Update
      outPut.print("\n Confirm to update? (y/n)  : ");
      String inputString1 = scan.nextLine();

      if (!inputString1.equalsIgnoreCase("y")) {
        outPut.printCancel();
        return;
      }

      // remove Item
      productManager.removeProductItem(itemIndex);

    }

  }

}
