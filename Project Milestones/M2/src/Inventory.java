
import java.util.HashMap;
/**
 * A type of ProductQuantityCollection where Products can never have negative quantity
 * Products are also not removed when they reach 0 quantity.
 * @author Sebastien Marleau 101155551
 * @version Milestone 2
 */
public class Inventory extends ProductQuantityCollection {

    /**
     * Initializes Inventory with a preset of items
     */
    public Inventory() {
        super();
        Product p1 = new Product("Fuji apple", 1.00);
        Product p2 = new Product("Empire apple", 0.75);
        Product p3 = new Product("Ambrosia apple", 1.15);
        addProductToCollection(p1, 30);
        addProductToCollection(p2, 5);
        addProductToCollection(p3, 15);
    }

    /**
     * {@inheritDoc}
     * If quantity is a negative value, throws an {@link IllegalArgumentException}.
     * @param productID the productID associated with the product
     * @param quantity the quantity to set the product to
     */
    @Override
    public void setQuantity(String productID, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Products cannot have a negative quantity in the inventory");
        }
        super.setQuantity(productID, quantity);
    }

}
