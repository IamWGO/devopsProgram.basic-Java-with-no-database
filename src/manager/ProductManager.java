package manager;

import dataclass.OrderItem;
import dataclass.Product;
import utility.stats.MainState;
import utility.stats.OperationState;
import view.ProductView;

import java.util.ArrayList;
import java.util.Scanner;

public class ProductManager {
  Scanner scan = new Scanner(System.in);
  ProductView outPut = new ProductView();

  ShopManager shopManager;
  OrderManager orderManager;
  public ProductManager(ShopManager shopManager) {
    this.shopManager = shopManager;
    this.orderManager = new OrderManager(shopManager);
  }

  //TODO : Frontend
  public void frontendProductList(){
    showProductList();

    boolean run = true;
    while (run) {
      outPut.printSelectToCartMenu(shopManager.tempOrderItems);
      // select menu
      String choice = scan.nextLine();

      // toUpperCase to check Q command
      switch (choice.toUpperCase()) {
        case "1" -> showProductList();
        case "2" -> addProductToCart();
        case "3" -> viewCart();
        case "4" -> {
          if (shopManager.tempOrderItems.isEmpty()) outPut.printMenuWarning();
          else  deleteOrderItem();
        }
        case "5" -> {
          if (shopManager.tempOrderItems.isEmpty()) outPut.printMenuWarning();
          else  orderManager.confirmOrder();
        }
        case "Q" ->  run = false;
        default -> outPut.printMenuWarning();
      }
    }
  }

  private void addProductToCart() {
    while (true) {
      showProductList();

      outPut.printAddToCartMenu(OperationState.ADD);
      // select menu
      String inputString = scan.nextLine();
      outPut.printEmptyLine();

      if (inputString.equalsIgnoreCase("q")) break;

      int itemIndex = searchByInputNumber(inputString);
      if (itemIndex == -1) {
        outPut.printNotFound();
        return;
      }
      //Add to cart
      addToCart(itemIndex);
    }

  }

  private void addToCart(int itemIndex){
    boolean isNewItem = true;
    Product selectedItem = shopManager.products.get(itemIndex);
    for (int i = 0; i < shopManager.tempOrderItems.size(); i++) {
      if (shopManager.tempOrderItems.get(i).getProductId() == selectedItem.getProductId()){
        // amount + 1 if already added to cart
        int amount = shopManager.tempOrderItems.get(i).getAmount() + 1;
        shopManager.tempOrderItems.get(i).setAmount(amount);
        isNewItem = false;
        break;
      }
    }

    if (isNewItem) {
      // Add to cart if new product
      orderManager.addTempOrderItem(selectedItem, 1);
      outPut.printSuccessAddTempOrderItem(selectedItem);
    }
  }

  private void viewCart() {
    // List selected product items (order items)
    orderManager.listOrderItems(shopManager.tempOrderItems);
  }
  private void deleteOrderItem() {

    while (true) {
      viewCart();

      outPut.printAddToCartMenu(OperationState.DELETE);
      // select menu
      String inputString = scan.nextLine();
      outPut.printEmptyLine();
      if (inputString.equalsIgnoreCase("q")) break;

      int itemIndex = -1;
      try {
        int choice = Integer.parseInt(inputString);
        // if choice is not in range then return -1
        if (choice > 0 || choice < (shopManager.tempOrderItems.size())) {
          itemIndex = choice-1;
        }
      } catch (NumberFormatException ex) {
        outPut.printNotFound();
        return;
      }

      if (itemIndex == -1) {
        outPut.printNotFound();
        return;
      }

      // remove item
      removeTempOrderItem(itemIndex);

    }
  }

  public void removeTempOrderItem(int itemIndex){
    // remove item
    OrderItem selectedItem = shopManager.tempOrderItems.get(itemIndex);
    shopManager.tempOrderItems.remove(itemIndex);

    // get deleted product
    for (int i = 0; i < shopManager.products.size(); i++) {
      if (shopManager.products.get(i).getProductId() == selectedItem.getProductId()) {
        outPut.printSuccessDeleteTempOrderItem(shopManager.products.get(i));
        break;
      }
    }
  }

  //---------------------------------------------------
  public void showProductList(){
    outPut.printHeadLine();
    for (int i = 0; i < shopManager.products.size(); i++) {
      outPut.printRow(i, shopManager.products.get(i));
    }
    outPut.printEndOfList();
  }

  public Product newItemForm(){
    outPut.println("::::: New Product ::::: \n");
    String productName = newItemInputForm("Product Name");
    String productDetail = newItemInputForm("Product Detail");
    double price;
    do {
      String priceString = newItemInputForm("Price");
      try {
        price = Double.parseDouble(priceString);
        break;
      } catch (NumberFormatException e) {
        outPut.println("Price have to be number!! \n");
      }

    } while(true);

    int productId = getNextId();
    return new Product(productId, productName, productDetail, price);
  }

  public int getNextId(){
    if (shopManager.products.isEmpty()) return 1;

    int maxProductId = -1;

    for (Product product : shopManager.products) {
      if (product.getProductId() > maxProductId) {
        maxProductId = product.getProductId();
      }
    }
    maxProductId++;
    return maxProductId;
  }

  public void addNewItem(Product newItem){
    //set mainState to get filename
    shopManager.mainState = MainState.PRODUCT;

    String contentLine = newItem.objectToLineFormat();
    shopManager.products.add(newItem);
    shopManager.addNewLine(contentLine);

    outPut.printAddedResult();
    outPut.println(" >> " + newItem.getProductName());
  }

  public Product updateItemForm(Product selectedItem){
    outPut.println("::::: Update Product ::::: \n");
    outPut.println("-- Enter to skip edit --  \n");
    String productName = updateItemInputForm(selectedItem.getProductName(),"Product Name");
    String productDetail = updateItemInputForm(selectedItem.getProductName(),"Product Detail");

    double price;
    do {
      String priceString = updateItemInputForm(selectedItem.getPriceString(),"price");
      try {
        price = Double.parseDouble(priceString);
        break;
      } catch (NumberFormatException e) {
        outPut.print("Price have to be number!! \n");
      }

    } while(true);

    return new Product(selectedItem.getProductId(), productName, productDetail, price);

  }

  public void updateItem(Product selectedItem, Product updateItem){
    selectedItem.updateItem(updateItem);
    outPut.printUpdatedResult();
    // Overwrite file
    rewriteFile();

    outPut.printUpdatedResult();
    outPut.println(" >> " + updateItem.getProductName());
  }

  public void removeProductItem(int itemIndex) {
    // remove Item
    shopManager.products.remove(itemIndex);
    // Rewrite file because we remove an item form ArrayList
    rewriteFile();

    Product selectedItem = shopManager.products.get(itemIndex);
    outPut.printDeletedResult();
    outPut.println(" >> " + selectedItem.getProductName());
  }

  public void rewriteFile() {
    if (!shopManager.isHasAdminPermission()) return;

    //set mainState to get filename
    shopManager.mainState = MainState.PRODUCT;

    // Rewrite file because we remove an item form ArrayList
    String contentLines = "";
    ArrayList<Product> tempProducts = new ArrayList<>();
    for (int i = 0; i < shopManager.products.size(); i++) {
      contentLines = contentLines.concat(shopManager.products.get(i).objectToLineFormat());
      if (i < shopManager.products.size()-1) contentLines = contentLines.concat("\n");

      tempProducts.add(shopManager.products.get(i));
    }

    shopManager.products = tempProducts;
    shopManager.overwriteFile(contentLines);
  }

  public int searchByInputNumber(String inputNumber){
    try {
      int choice = Integer.parseInt(inputNumber);
      return (choice < 0 || choice > (shopManager.products.size())) ? -1 : choice - 1;
    } catch (NumberFormatException ex) {
      // if user input string then return -1
      return  -1;
    }
  }

  public String newItemInputForm(String label){
    outPut.print(label + " : ");
    return scan.nextLine();
  }

  public String updateItemInputForm(String oldString, String label) {
    outPut.print(label + " : " + oldString + "  > ");
    String inputString = scan.nextLine();

    return (inputString.isEmpty()) ? oldString : inputString;
  }

}
