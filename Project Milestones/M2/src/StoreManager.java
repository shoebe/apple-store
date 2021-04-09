
import java.util.HashMap;
/**
 * Manages an Inventory and ShoppingCarts.
 * Products are removed from the inventory when they are added to shopping carts, and vice-versa.
 * @author Sebastien Marleau 10115551
 * @version Milestone 2
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
     * Returns the stock quantity of a product in the store's inventory.
     * @param productID the ID of the product related to the desired stock quantity
     * @return stock of product
     */
    public int getStockOfProduct(String productID) {
        return inventory.getQuantity(productID);
    }

    /**
     * Returns a ShoppingCart instance.
     * Throws an {@link IllegalArgumentException} if the store has no cart associated with the given cartID.
     * @param cartID the cartID associated with the desired ShoppingCart instance
     * @return the desired ShoppingCart instance
     */
    private ShoppingCart getCart(String cartID) {
        if (IDtoShoppingCart.containsKey(cartID)) {
            return IDtoShoppingCart.get(cartID);
        }
        throw new IllegalArgumentException("Invalid cartID");
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
     * Removes the cart from the StoreManager instance, and stops keeping track of it
     * @param cartID the cartID associated with the cart
     */
    public void removeCart(String cartID) {
        IDtoShoppingCart.remove(cartID);
    }

    /**
     * Uses {@link #removeFromCart} to remove all products from the cart and add them back to the inventory
     * @param cartID the cartID associated with the cart
     */
    public void resetCart(String cartID) {
        ShoppingCart cart = IDtoShoppingCart.get(cartID);
        cart.getAllProductIDs().forEach( (id) -> {
            removeFromCart(cartID, id, cart.getQuantity(id));
        });
    }

    /**
     * Prints a representation of the store's inventory to System.out
     */
    public void printInventory() {
        System.out.println("            <--- STORE INVENTORY --->");
        System.out.println("Product ID | Stock | Product Name    | Unit Price");
        inventory.getAllProductIDs().forEach( (id) -> {
            Product p = inventory.getProduct(id);
            int q = inventory.getQuantity(id);
            System.out.print(String.format("%-11s", id) + "|");
            System.out.print(String.format("%-7s", q) + "|");
            System.out.print(String.format("%-17s", p.getName()) + "|");
            System.out.println(" $" + p.getPrice());
        });
        System.out.println();
    }

    /**
     * Prints a representation of a cart to System.out
     * @param cartID the cartID associated with the cart
     */
    public void printCart(String cartID) {
        ShoppingCart cart = getCart(cartID);
        System.out.println("+--- Cart ---+");
        System.out.println("Product ID | Product Name    | Quantity | Unit Price | Price");
        cart.getAllProductIDs().forEach( (id) -> {
            Product product = cart.getProduct(id);
            int quantity = cart.getQuantity(id);
            System.out.print(String.format("%-11s", id) + "|");
            System.out.print(String.format("%-17s", product.getName()) + "|");
            System.out.print(String.format("%-10s", quantity) + "|");
            System.out.print(String.format("%-12s", " $" + product.getPrice()) + "|");
            System.out.println(" $" + product.getPrice() * quantity);
        });
        if (cart.isEmpty()) {
            System.out.println("Empty :(");
        }
        System.out.println();
    }

    /**
     * Prints a summary of a cart, mainly its item count and total price.
     * @param cartID the cartID associated with the cart
     */
    public void printCartSummary(String cartID) {
        ShoppingCart cart = getCart(cartID);
        System.out.printf("Cart has %s items, for a total of $%s.\n", cart.getTotalItemCount(), cart.getTotalPrice());
    }
}
