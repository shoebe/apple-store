package storetest;

import store.Product;
import store.ShoppingCart;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sebastien Marleau 101155551
 * @version Milestone 3
 */
public class ShoppingCartTest {

    private static ShoppingCart cart;
    private static final List<Product> products = Arrays.asList(
            new Product("Potato", 3.0),
            new Product("Chicken", 5.0),
            new Product("blueberries", 0.012312)
    );

    @BeforeEach
    void init() {
         cart = new ShoppingCart();
    }

    @Test
    void getProduct() {
        cart.addNewProduct(products.get(0), 5);
        assertEquals(cart.getProduct(products.get(0).getID()), products.get(0), "Wrong product instance returned");
    }

    @Test
    void hasProduct() {
        cart.addNewProduct(products.get(0), 5);
        assertEquals(cart.hasProduct(products.get(0).getID()), true, "hasProduct returning false for an item that was added to the cart");
        assertEquals(cart.hasProduct("@gibberish|invalid ID"), false, "Cart says it has items it does not");
    }

    @Test
    void getProductQuantity() {
        cart.addNewProduct(products.get(0), 5);
        assertEquals(cart.getProductQuantity(products.get(0).getID()), 5, "wrong quantity reported by getQuantity");
        assertEquals(cart.getProductQuantity("(╯°□°）╯︵ ┻━┻|invalid ID"), 0, "getQuanitty should report 0 if the cart does not have a certain product");
    }

    @Test
    void addNewProduct() {
        int size = cart.getNumOfProducts();
        Product newProd1 = products.get(0);
        cart.addNewProduct(newProd1, 123);
        assertEquals(cart.hasProduct(newProd1.getID()), true, "Cart does not have product after adding it");
        assertEquals(cart.getProductQuantity(newProd1.getID()), 123, "Returns wrong quantity of newly added product");
        assertEquals(cart.getNumOfProducts(), size + 1, "product count did not increase after a product add");

        Product newProd2 = products.get(1);
        cart.addNewProduct(newProd2, 0);
        assertEquals(cart.hasProduct(newProd2.getID()), false, "Item added to cart even though quantity was 0");

        Product newProd3 = products.get(2);
        cart.addNewProduct(newProd3, -5);
        assertEquals(cart.hasProduct(newProd3.getID()), false, "Item added to cart even though quantity was less that 0");

        assertThrows(IllegalArgumentException.class, () -> cart.addNewProduct(newProd1, 5),
                "Does not throw an exception on add of product already in the cart");
    }

    @Test
    void addProductQuantity() {
        Product prod1 = products.get(0);
        cart.addNewProduct(prod1, 1);
        cart.addProductQuantity(prod1.getID(), 5);
        assertEquals(cart.getProductQuantity(prod1.getID()), 6, "wrong quantity added to product");

        Product prod2 = products.get(1);
        cart.addNewProduct(prod2, 1);
        assertThrows(IllegalArgumentException.class, () -> cart.addProductQuantity(prod2.getID(), -1) , "does not throw exception on negative argument passed in");

        assertThrows(IllegalArgumentException.class, () -> cart.addProductQuantity("invalid ID", 10) , "does not throw exception on invalid ID");
    }

    @Test
    void removeProductQuantity() {
        Product prod1 = products.get(0);
        cart.addNewProduct(prod1, 2);
        cart.removeProductQuantity(prod1.getID(), 1);
        assertEquals(cart.getProductQuantity(prod1.getID()), 1, "wrong quantity removed of product");

        Product prod2 = products.get(1);
        cart.addNewProduct(prod2, 1);
        assertThrows(IllegalArgumentException.class, () -> cart.removeProductQuantity(prod2.getID(), -1) , "does not throw exception on negative argument passed in");

        Product prod3 = products.get(2);
        cart.addNewProduct(prod3, 5);
        cart.removeProductQuantity(prod3.getID(), 5);
        assertEquals(cart.hasProduct(prod3.getID()), false, "Cart still has product after removing all its quantity");

        cart.addNewProduct(prod3, 5);
        cart.removeProductQuantity(prod3.getID(), 10);
        assertEquals(cart.hasProduct(prod3.getID()), false, "Cart still has product after removing more than all its quantity");
    }

    @Test
    void setProductQuantity() {
        assertThrows(IllegalArgumentException.class, () -> cart.setProductQuantity("invalid ID", 123) , "does not throw exception on invalid ID");

        Product p1 = products.get(0);
        cart.addNewProduct(p1, 10);
        cart.setProductQuantity(p1.getID(), 5);
        assertEquals(cart.getProductQuantity(p1.getID()), 5, "Wrong quantity set");

        cart.setProductQuantity(p1.getID(), 0);
        assertEquals(cart.hasProduct(p1.getID()), false, "product not remove from cart after having its quantity set to 0");

        cart.addNewProduct(p1, 10);
        cart.setProductQuantity(p1.getID(), -10);
        assertEquals(cart.hasProduct(p1.getID()), false, "product not remove from cart after having its quantity set to below 0");
    }

    @Test
    void getTotalProductQuantity(){
        cart.addNewProduct(products.get(0), 5);
        assertEquals(cart.getTotalProductQuantity(), 5, "wrong item count returned");
        cart.addNewProduct(products.get(1), 5);
        assertEquals(cart.getTotalProductQuantity(), 10, "wrong item count returned");
    }

    @Test
    void getTotalPriceOfCart(){
        Product p = new Product("Empire apple", 0.01);
        cart.addNewProduct(p, 15);
        assertEquals(cart.getTotalPriceOfCart(), 0.15, "wrong price returned");

        Product p2 = new Product("Fuji apple", 1.00);
        cart.addNewProduct(p2, 15);
        assertEquals(cart.getTotalPriceOfCart(), 15.15, "wrong price returned");
    }

}
