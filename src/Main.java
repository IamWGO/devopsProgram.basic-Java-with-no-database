import controller.Navigation;
import manager.ShopManager;

public class Main {
  public static void main(String[] args) {
    ShopManager shopManager = new ShopManager();
    shopManager.setIsAdmin(false);
    Navigation navigation = new Navigation(shopManager);
    navigation.mainMenu();

    // Backend test
//    shopManager.setIsAdmin(true);
//    shopManager.setAdminLogin(true);
//
//    CustomerController customerController = new CustomerController(shopManager);
//    customerController.menu();
//
//    ProductController productController = new ProductController(shopManager);
//    productController.menu();
//
//    OrderController orderController = new OrderController(shopManager);
//    orderController.menu();



  }
}