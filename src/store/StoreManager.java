package store;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Manages an app.Inventory and ShoppingCarts.
 * Products are removed from the inventory when they are added to shopping carts, and vice-versa.
 * @author Sebastien Marleau 101155551
 * @version Milestone 4
 */
public class StoreManager {

    private final Inventory inventory;
    private final HashMap<String, ShoppingCart> IDtoShoppingCart;

    /**
     * Constructor for the StoreManager
     */
    public StoreManager() {
        inventory = new Inventory();
        IDtoShoppingCart = new HashMap<String, ShoppingCart>();
    }

    /**
     * Checks whether or not the store has a product in its inventory
     * @param productID the productID associated with the product whose existence in the store is to be verified
     * @return true if the store's inventory has the product
     */
    public boolean hasProductInInventory(String productID) {
        return inventory.hasProduct(productID);
    }

    /**
     * Returns a Product object from the inventory based on a given productID
     * @param productID the productID associated with the product
     * @return the Product object
     */
    public Product getProduct(String productID) {
        return inventory.getProduct(productID);
    }

    /**
     * Returns the stock quantity of a product in the store's inventory.
     * @param productID the ID of the product related to the desired stock quantity
     * @return stock of product
     */
    public int getStockOfProduct(String productID) {
        return inventory.getQuantity(productID);
    }

    /**
     * Gets a stream containing all the product IDs in the inventory. Useful for iteration.
     * @return A stream of product IDs
     */
    public Stream<String> getAllProductIDsInInventory() {
        return  inventory.getAllProductIDs();
    }

    /**
     * Creates a new cart, and returns its associated cartID.
     * @return the associated cartID of the newly created cart
     */
    public String createNewCart() {
        ShoppingCart cart = new ShoppingCart();
        IDtoShoppingCart.put(cart.getCartID(), cart);
        return cart.getCartID();
    }

    /**
     * Returns a app.ShoppingCart instance.
     * Throws an {@link IllegalArgumentException} if the store has no cart associated with the given cartID.
     * @param cartID the cartID associated with the desired app.ShoppingCart instance
     * @return the desired app.ShoppingCart instance
     */
    private ShoppingCart getCart(String cartID) {
        if (IDtoShoppingCart.containsKey(cartID)) {
            return IDtoShoppingCart.get(cartID);
        }
        throw new IllegalArgumentException("Invalid cartID");
    }

    /**
     * Removes the cart from the app.StoreManager instance, and stops keeping track of it
     * @param cartID the cartID associated with the cart
     */
    public void removeCart(String cartID) {
        //getCart(cartID); //to throw an exception on bad ID
        IDtoShoppingCart.remove(cartID);
    }

    /**
     * Uses {@link #removeFromCart} to remove all products from the cart and add them back to the inventory
     * @param cartID the cartID associated with the cart
     */
    public void resetCart(String cartID) {
        ShoppingCart cart = getCart(cartID);
        cart.getAllProductIDs()
                .collect(Collectors.toList()) // make a list first to avoid ConcurrentModificationException
                .forEach( (id) -> {
            removeFromCart(cartID, id, cart.getQuantity(id));
        });
    }

    /**
     * Confirms whether or the store manager is keeping track of a cart
     * @param cartID the cartID associated with the cart
     * @return true if the store manager is keeping track of that cart, false otherwise
     */
    public boolean hasCart(String cartID) {
        return IDtoShoppingCart.containsKey(cartID);
    }

    /**
     * Adds a quantity of a product to a cart while removing this same quantity from the store's inventory.
     * If the store does not have enough quantity of the product, the maximum is transferred to the cart.
     * @param cartID the cartID associated with the cart
     * @param productID the productID associated with the product
     * @param quantity the quantity of the product to be transferred from the inventory to the cart
     */
    public void addToCart(String cartID, String productID, int quantity) {
        int inventoryStock = getStockOfProduct(productID);
        if (inventoryStock == 0) {
            return;
        }
        else if (inventoryStock < quantity) {
            quantity = inventoryStock;
        }

        ShoppingCart cart = getCart(cartID);
        if (!cart.hasProduct(productID)) {
            cart.addProductToCollection(inventory.getProduct(productID), quantity);
        }
        else {
            getCart(cartID).add(productID, quantity);
        }
        inventory.remove(productID, quantity);
    }

    /**
     * Removes a quantity of a product from a cart while adding this same quantity to the store's inventory.
     * If the cart does not have enough quantity of the product, the maximum is transferred to the inventory.
     * @param cartID the cartID associated with the cart
     * @param productID the productID associated with the product
     * @param quantity the quantity of the product to be transferred from the cart to the inventory
     */
    public void removeFromCart(String cartID, String productID, int quantity) {
        ShoppingCart cart = IDtoShoppingCart.get(cartID);
        int cartQuantity = cart.getQuantity(productID);
        if (cartQuantity == 0) {
            return;
        }
        else if (cartQuantity < quantity) {
            quantity = cartQuantity;
        }
        cart.remove(productID, quantity);
        inventory.add(productID, quantity); // assume inventory already has the product
    }

    /**
     * Returns the quantity of a product in a cart
     * @param cartID the cartID associated with the cart
     * @param productID the productID associated with the product
     * @return the quantity of the product in the cart
     */
    public int getQuantityInCart(String cartID, String productID) {
        return getCart(cartID).getQuantity(productID);
    }

    /**
     * Returns the sum of all the items of all products in a cart.
     * @param cartID the cartID associated with the cart
     * @return the sum of all the items of all products in the cart.
     */
    public int getCartItemCount(String cartID) {
        return getCart(cartID).getItemCountInCart();
    }

    /**
     * Returns the net price of all items in a cart
     * @param cartID the cartID associated with the cart
     * @return the net price of all items in the cart
     */
    public double getCartTotalPrice(String cartID) {
        return getCart(cartID).getTotalPriceOfCart();
    }

    /**
     * Returns a stream of productIDs in a cart, useful for iteration.
     * @param cartID the cartID associated with the cart
     * @return the stream of productIDs
     */
    public Stream<String> getAllProductIDsInCart(String cartID) {
        return getCart(cartID).getAllProductIDs();
    }


}
