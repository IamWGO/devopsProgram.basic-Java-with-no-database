package manager;

import dataclass.OrderItem;
import dataclass.Product;
import utility.stats.MainState;
import utility.view.ProductView;

import java.util.ArrayList;
import java.util.Scanner;

public class ProductManager {
  Scanner scan = new Scanner(System.in);
  ProductView view = new ProductView();
  ShopManager shopManager;
  OrderManager orderManager;
  public ProductManager(ShopManager shopManager) {
    this.shopManager = shopManager;
    this.orderManager = new OrderManager(shopManager);
  }

  public void showProductList(){
    for (int i = 0; i < shopManager.products.size(); i++) {
      view.printRow(i, shopManager.products.get(i));
    }
    view.printFooter();
  }

  public void addToCart(int itemIndex){
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
      view.printSuccessAddTempOrderItem(selectedItem);
    }
  }

  public Product newItemForm(){
    view.printText("::::: New Product ::::: \n");
    String productName = newItemInputForm("Product Name");
    String productDetail = newItemInputForm("Product Detail");
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
    return new Product(productId, productName, productDetail, price);
  }

  public void addNewItem(Product newItem){
    String contentLine = newItem.objectToLineFormat();
    shopManager.products.add(newItem);
    shopManager.addNewLine(contentLine);

    view.printSuccessAddProduct(newItem);
  }

  public Product updateItemForm(Product selectedItem){
    view.printText("::::: Update Product ::::: \n");
    view.printText("-- Enter to skip edit --  \n");
    String productName = updateItemInputForm(selectedItem.getProductName(),"Product Name");
    String productDetail = updateItemInputForm(selectedItem.getProductName(),"Product Name");

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

    return new Product(selectedItem.getProductId(), productName, productDetail, price);

  }

  public void updateItem(Product selectedItem, Product updateItem){
    selectedItem.updateItem(updateItem);
    view.printUpdateResult(updateItem);
    // Overwrite file
    rewriteFile();
  }

  public void removeProductItem(int itemIndex) {
    // remove Item
    shopManager.products.remove(itemIndex);
    // Rewrite file because we remove an item form ArrayList
    rewriteFile();
  }

  public void removeTempOrderItem(int itemIndex){
    // remove item
    OrderItem selectedItem = shopManager.tempOrderItems.get(itemIndex);
    shopManager.tempOrderItems.remove(itemIndex);

    // get deleted product
    for (int i = 0; i < shopManager.products.size(); i++) {
      if (shopManager.products.get(i).getProductId() == selectedItem.getProductId()) {
        view.printSuccessDeleteTempOrderItem(shopManager.products.get(i));
        break;
      }
    }
  }

  // ++ Help Methods ++ ------------------------------------------------------------------------------

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

  public int searchByInputNumber(String inputNumber){
    try {
      int choice = Integer.parseInt(inputNumber);
      // if choice is not in range then return -1
      if (choice < 0 || choice > (shopManager.products.size())) {
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
