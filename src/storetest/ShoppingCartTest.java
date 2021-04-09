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
        cart.addProductToCollection(products.get(0), 5);
        assertEquals(cart.getProduct(products.get(0).getId()), products.get(0), "Wrong product instance returned");
    }

    @Test
    void hasProduct() {
        cart.addProductToCollection(products.get(0), 5);
        assertEquals(cart.hasProduct(products.get(0).getId()), true, "hasProduct returning false for an item that was added to the cart");
        assertEquals(cart.hasProduct("@gibberish|invalid ID"), false, "Cart says it has items it does not");
    }

    @Test
    void getQuantity() {
        cart.addProductToCollection(products.get(0), 5);
        assertEquals(cart.getQuantity(products.get(0).getId()), 5, "wrong quantity reported by getQuantity");
        assertEquals(cart.getQuantity("(╯°□°）╯︵ ┻━┻|invalid ID"), 0, "getQuanitty should report 0 if the cart does not have a certain product");
    }

    @Test
    void addProductToCollection() {
        int size = cart.getTotalProductCount();
        Product newProd1 = products.get(0);
        cart.addProductToCollection(newProd1, 123);
        assertEquals(cart.hasProduct(newProd1.getId()), true, "Cart does not have product after adding it");
        assertEquals(cart.getQuantity(newProd1.getId()), 123, "Returns wrong quantity of newly added product");
        assertEquals(cart.getTotalProductCount(), size + 1, "product count did not increase after a product add");

        Product newProd2 = products.get(1);
        cart.addProductToCollection(newProd2, 0);
        assertEquals(cart.hasProduct(newProd2.getId()), false, "Item added to cart even though quantity was 0");

        Product newProd3 = products.get(2);
        cart.addProductToCollection(newProd3, -5);
        assertEquals(cart.hasProduct(newProd3.getId()), false, "Item added to cart even though quantity was less that 0");

        assertThrows(IllegalArgumentException.class, () -> cart.addProductToCollection(newProd1, 5),
                "Does not throw an exception on add of product already in the cart");
    }

    @Test
    void add() {
        Product prod1 = products.get(0);
        cart.addProductToCollection(prod1, 1);
        cart.add(prod1.getId(), 5);
        assertEquals(cart.getQuantity(prod1.getId()), 6, "wrong quantity added to product");

        Product prod2 = products.get(1);
        cart.addProductToCollection(prod2, 1);
        assertThrows(IllegalArgumentException.class, () -> cart.add(prod2.getId(), -1) , "does not throw exception on negative argument passed in");

        assertThrows(IllegalArgumentException.class, () -> cart.add("invalid ID", 10) , "does not throw exception on invalid ID");
    }

    @Test
    void remove() {
        Product prod1 = products.get(0);
        cart.addProductToCollection(prod1, 2);
        cart.remove(prod1.getId(), 1);
        assertEquals(cart.getQuantity(prod1.getId()), 1, "wrong quantity removed of product");

        Product prod2 = products.get(1);
        cart.addProductToCollection(prod2, 1);
        assertThrows(IllegalArgumentException.class, () -> cart.remove(prod2.getId(), -1) , "does not throw exception on negative argument passed in");

        Product prod3 = products.get(2);
        cart.addProductToCollection(prod3, 5);
        cart.remove(prod3.getId(), 5);
        assertEquals(cart.hasProduct(prod3.getId()), false, "Cart still has product after removing all its quantity");

        cart.addProductToCollection(prod3, 5);
        cart.remove(prod3.getId(), 10);
        assertEquals(cart.hasProduct(prod3.getId()), false, "Cart still has product after removing more than all its quantity");
    }

    @Test
    void setQuantity() {
        assertThrows(IllegalArgumentException.class, () -> cart.setQuantity("invalid ID", 123) , "does not throw exception on invalid ID");

        Product p1 = products.get(0);
        cart.addProductToCollection(p1, 10);
        cart.setQuantity(p1.getId(), 5);
        assertEquals(cart.getQuantity(p1.getId()), 5, "Wrong quantity set");

        cart.setQuantity(p1.getId(), 0);
        assertEquals(cart.hasProduct(p1.getId()), false, "product not remove from cart after having its quantity set to 0");

        cart.addProductToCollection(p1, 10);
        cart.setQuantity(p1.getId(), -10);
        assertEquals(cart.hasProduct(p1.getId()), false, "product not remove from cart after having its quantity set to below 0");
    }

    @Test
    void getItemCountInCart(){
        cart.addProductToCollection(products.get(0), 5);
        assertEquals(cart.getItemCountInCart(), 5, "wrong item count returned");
        cart.addProductToCollection(products.get(1), 5);
        assertEquals(cart.getItemCountInCart(), 10, "wrong item count returned");
    }

    @Test
    void getTotalPriceOfCart(){
        Product p = new Product("Empire apple", 0.01);
        cart.addProductToCollection(p, 15);
        assertEquals(cart.getTotalPriceOfCart(), 0.15, "wrong price returned");

        Product p2 = new Product("Fuji apple", 1.00);
        cart.addProductToCollection(p2, 15);
        assertEquals(cart.getTotalPriceOfCart(), 15.15, "wrong price returned");
    }

}
