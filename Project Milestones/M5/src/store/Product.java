package store;

/**
 * Stores information about a certain product
 * @author Sebastien Marleau 101155551
 * @version Milestone 4
 */
public class Product {
    private static int count = 0;
    private final String name;
    private final String id;
    private final double price;

    /**
     * Initialize with a product name, id, and price
     * @param name the name of the product
     * @param price the price of the product
     */
    public Product(String name, double price) {
        this.name = name;
        this.id = String.valueOf(count++);
        this.price = price;
    }

    /**
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * @return the unique ID of the product
     */
    public String getID() {
        return id;
    }

    /**
     * @return the price of the product
     */
    public double getPrice() {
        return price;
    }
}
