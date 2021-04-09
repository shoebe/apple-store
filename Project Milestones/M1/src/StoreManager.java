import java.util.List;
//Student Number: 101155551
/**
 * Manages an Inventory with store-related methods
 * @author Sebastien Marleau
 * @version Milestone 1
 */
public class StoreManager {

    private Inventory inventory;

    /**
     * Initializes the StoreManager with a new Inventory instance
     */
    public StoreManager() {
        inventory = new Inventory();
    }

    /**
     * Returns the stock quantity of a product in the current store's inventory.
     * @param product   Product object related to the desired stock quantity
     * @return          Stock of "product" in this instance's Inventory object
     */
    public int getStockOfProduct(Product product) {
        return inventory.getStock(product.getId());
    }

    /**
     * Performs a transaction on a cart of orders, where the products bought are removed from the inventory
     * and the total cost is calculated and returned.
     * Does not modify any stock and returns -1.0 if there is not enough stock of one of the products
     * in the order.
     * @param cart      List of products and associated quantities to be bought
     * @return          The total price of all the products removed from the inventory or -1.0 on failure.
     */
    public double performTransaction(List<ProductInformation> cart) {
        // to avoid removing stock of any product if the order is faulty
        for (ProductInformation purchaseInfo : cart) {
            if (getStockOfProduct(purchaseInfo.product) < purchaseInfo.quantity)
                return -1.0;
        }
        double cost = 0.0;
        for (ProductInformation purchaseInfo : cart) {
            cost += purchaseInfo.product.getPrice() * purchaseInfo.quantity;
            inventory.changeStock(purchaseInfo.product, -purchaseInfo.quantity);
        }
        return cost;
    }
}

/**
 * A class with the sole purpose of fulfilling the requirement set by the Milestone 1 guideline.
 * It will be removed and the performTransactions method modified in future milestones.
 */
class ProductInformation {
    public Product product;
    public int quantity;
}
