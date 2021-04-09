
/**
 * A type of ProductQuantityCollection where Products are removed once the reach a non-positive quantity.
 * They are also assigned a unique cartID on creation
 * @author Sebastien Marleau 101155551
 * @version Milestone 2
 */
public class ShoppingCart extends ProductQuantityCollection {
    private static int cartCount = 0;
    private final String cartID;

    /**
     * Constructor for ShoppingCart
     */
    public ShoppingCart() {
        super();
        cartID = String.valueOf(cartCount++);
    }

    /**
     * Returns the cartID associated with this cart instance
     * @return the cartID
     */
    public String getCartID() {
        return cartID;
    }

    /**
     * {@inheritDoc}
     * If quantity is 0 or less, uses {@link #removeProductFromCollection} to remove the product from the collection.
     * @param productID the productID associated with the product
     * @param quantity the quantity to set the product to
     */
    @Override
    public void setQuantity(String productID, int quantity) {
        if (quantity <= 0) {
            removeProductFromCollection(productID);
            return;
        }
        super.setQuantity(productID, quantity);
    }

    /**
     * Returns what it would cost to buy everything in the shopping cart
     * @return the total value of everything currently in the shopping cart
     */
    public double getTotalPrice() {
        return getAllProductIDs()
                .mapToDouble( (id) -> getProduct(id).getPrice() * getQuantity(id) )
                .sum();
    }

    /**
     * Returns the summed quantity of every product in the shopping cart
     * @return the total number of items in the shopping cart
     */
    public int getTotalItemCount() {
        return getAllProductIDs()
                .mapToInt( this::getQuantity )
                .sum();
    }
}
