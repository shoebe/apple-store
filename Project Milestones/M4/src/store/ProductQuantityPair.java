package store;

/**
 * A pair containing a app.Product object and an int representing the quantity of that product
 * @author Sebastien Marleau 101155551
 * @version Milestone 4
 */
public class ProductQuantityPair {
    private final Product product;
    private int quantity;

    /**
     * Constructor for app.ProductQuantityPair
     * @param product the product
     * @param quantity the quantity associated with the product
     */
    public ProductQuantityPair(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    public ProductQuantityPair(Product product) {
        this(product, 0);
    }

    /**
     * Gets the quantity associated with the product in this pair
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity associated with the product in this pair
     * @param quantity the quantity to be set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the product associated with the quantity in this pair
     * @return the product contained in this pair
     */
    public Product getProduct() {
        return product;
    }
}
