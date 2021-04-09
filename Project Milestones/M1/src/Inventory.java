import java.util.HashMap;
//Student Number: 101155551
/**
 * Contains a collection of products and their current quantities in stock
 * @author Sebastien Marleau
 * @version Milestone 1
 */
public class Inventory {
    private HashMap<String, Integer> IDtoStockMap;
    private HashMap<String, Product> IDtoProductMap;

    /**
     * Initializes Inventory with no registered products and no stock
     */
    public Inventory() {
        IDtoStockMap = new HashMap<>();
        IDtoProductMap = new HashMap<>();
    }

    /**
     * Gets the stock of the product with the associated product ID.
     * Returns 0 if there are no products with that ID in the inventory.
     * @param productID     The ID associated with the product whose stock quantity is desired
     * @return              The amount of the product currently in sotck in this inventory
     */
    public int getStock(String productID) {
        return IDtoStockMap.getOrDefault(productID, 0);
    }

    /**
     * Returns the Product object which has the associated product ID.
     * If no Product in the inventory has that product ID, returns null.
     * @param productID     The ID associated with the desired Product object
     * @return              The Product object with that ID
     */
    public Product getProductWithID(String productID) {
        if (!IDtoProductMap.containsKey(productID)) {
            return null;
        }
        return IDtoProductMap.get(productID);
    }

    /**
     * Changes the stock quantity of a product by a certain amount.
     * If the amount would bring the stock quantity below zero, throws an IllegalArgumentException.
     * If the product is not yet in the inventory, adds it to the inventory.
     * @param product       The product whose stock quantity is to be modified
     * @param amount        The amount by which the stock quantity is to be modified (negative or positive)
     */
    public void changeStock(Product product, int amount) {
        int stock = getStock(product.getId()) + amount;
        if (stock < 0) {
            throw new IllegalArgumentException("amount deducted is greater than product stock");
        }
        IDtoProductMap.put(product.getId(), product);
        IDtoStockMap.put(product.getId(), stock);
    }
}
