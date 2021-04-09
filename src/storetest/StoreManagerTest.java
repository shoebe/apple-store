package storetest;

import store.StoreManager;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sebastien Marleau 101155551
 * @version Milestone 3
 */
public class StoreManagerTest {

    StoreManager sm;

    @BeforeEach
    void setup() {
        sm = new StoreManager();
    }

    @Test
    void createNewCart() {
        String id = sm.createNewCart();
        assertTrue(sm.hasCart(id), "SM does not keep track of newly created cart");
        assertFalse(sm.hasCart("invalid ID"), "SM has a cart with an invalid ID");
        assertEquals(sm.getCartItemCount(id), 0, "new cart already has items in it");
    }

    @Test
    void addToCart() {
        // expect items added to cart to be removed from inventory
        String cID = sm.createNewCart();
        String pID = sm.getAllProductIDsInInventory().findFirst().get();
        int maxQ = sm.getStockOfProduct(pID);

        sm.addToCart(cID, pID, maxQ);
        assertEquals(sm.getStockOfProduct(pID), 0, "wrong quantity in inventory");
        assertEquals(sm.getQuantityInCart(cID, pID), maxQ, "wrong quantity in cart");

        sm.addToCart(cID, pID, 1);
        assertEquals(sm.getStockOfProduct(pID), 0, "wrong quantity after remove of item at 0 stock");
        assertEquals(sm.getQuantityInCart(cID, pID), maxQ, "wrong quantity in cart after add from empty inventory");
    }

    @Test
    void removeFromCart() {
        // expect items removed from the cart to be added to the inventory
        String cID = sm.createNewCart();
        String pID = sm.getAllProductIDsInInventory().findFirst().get();

        int maxQ = sm.getStockOfProduct(pID);
        sm.addToCart(cID, pID, maxQ);
        sm.removeFromCart(cID, pID, maxQ);

        assertEquals(sm.getStockOfProduct(pID), maxQ, "wrong quantity in inventory");
        assertEquals(sm.getQuantityInCart(cID, pID), 0, "wrong quantity in cart");

        sm.removeFromCart(cID, pID, 1);
        assertEquals(sm.getStockOfProduct(pID), maxQ, "wrong quantity after remove of item at 0 stock");
        assertEquals(sm.getQuantityInCart(cID, pID), 0, "wrong quantity in cart after add from empty inventory");
    }

    @Test
    void removeCart() {
        String cID = sm.createNewCart();
        sm.removeCart(cID);
        assertFalse(sm.hasCart(cID));
    }

    @Test
    void resetCart() {
        String cID = sm.createNewCart();
        String pID = sm.getAllProductIDsInInventory().findFirst().get();
        int maxQ = sm.getStockOfProduct(pID);

        sm.addToCart(cID, pID, maxQ);
        sm.resetCart(cID);
        assertEquals(sm.getStockOfProduct(pID), maxQ, "wrong quantity in inventory");
        assertEquals(sm.getQuantityInCart(cID, pID), 0, "wrong quantity in cart");
    }
}
