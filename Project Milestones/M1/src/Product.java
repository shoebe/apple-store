//Student Number: 101155551
/**
 * Stores information about a certain product
 * @author Sebastien Marleau
 * @version Milestone 1
 */
public class Product {
    private final String name;
    private final String id;
    private final double price;

    /**
     * Initialize with a product name, id, and price
     * @param name      The name of the product
     * @param id        The id of the product
     * @param price     The price of the product
     */
    public Product(String name, String id, double price) {
        this.name = name;
        this.id = id;
        this.price = price;
    }

    /**
     * @return the name of the product
     */
    public String getName() {
        return name;
    }
    /**
     * @return the ID of the product
     */
    public String getId() {
        return id;
    }
    /**
     * @return the price of the product
     */
    public double getPrice() {
        return price;
    }
}
