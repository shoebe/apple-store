
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Contains a collection of products and their related current quantities
 * @author Sebastien Marleau 101155551
 * @version Milestone 2
 */
public class ProductQuantityCollection {

    private final HashMap<String, ProductQuantityPair> IDtoPQPair;

    /**
     * Constructor for ProductQuantityCollection
     */
    public ProductQuantityCollection() {
        IDtoPQPair = new HashMap<String, ProductQuantityPair>();
    }

    /**
     * Gets the quantity of a product in this collection.
     * Returns 0 if there are no products with that ID in the collection.
     * @param productID the productID associated with the product
     * @return the quantity of the product in this collection
     */
    public int getQuantity(String productID) {
        if (IDtoPQPair.containsKey(productID)) {
            return IDtoPQPair.get(productID).getQuantity();
        }
        return 0;
    }

    /**
     * Returns a Product object which has the associated product ID.
     * If no Product in the collection has that product ID, returns null.
     * @param productID the ID associated with the desired Product object
     * @return the Product object
     */
    public Product getProduct(String productID) {
        if (IDtoPQPair.containsKey(productID)) {
            return IDtoPQPair.get(productID).getProduct();
        }
        return null;
    }

    /**
     * Checks whether or not a Product in this collection is associated with the given productID
     * @param productID the productID of the product
     * @return true if the productID is associated with a Product in this collection
     */
    public boolean hasProduct(String productID){
        return IDtoPQPair.containsKey(productID);
    }

    /**
     * Adds a product to the collection.
     * If the product was already a part of the collection, an {@link IllegalArgumentException} is thrown.
     * Uses setQuantity to set the quantity.
     * @param product the product to be added to the collection
     * @param quantity the quantity associated with the product
     */
    public void addProductToCollection(Product product, int quantity) {
        if (IDtoPQPair.containsKey(product.getId())) {
            throw new IllegalArgumentException("Collection already contains a product with that ID");
        }
        IDtoPQPair.put(product.getId(), new ProductQuantityPair(product));
        setQuantity(product.getId(), quantity);
    }

    /**
     * Removes a product and its associated quantity from the collection.
     * The product must be added again with {@link #addProductToCollection} to use other functions.
     * @param productID the productID of the product to be removed
     */
    public void removeProductFromCollection(String productID) {
        IDtoPQPair.remove(productID);
    }

    /**
     * Sets the quantity of a product.
     * If the productID is not associated with any product in the collection, throws an {@link IllegalArgumentException}.
     * @param productID the productID associated with the product.
     * @param quantity the quantity to set the product to
     */
    public void setQuantity(String productID, int quantity) {
        if (!IDtoPQPair.containsKey(productID)) {
            throw new IllegalArgumentException("productID does not match any products in the inventory");
        }
        IDtoPQPair.get(productID).setQuantity(quantity);
    }

    /**
     * Uses {@link #setQuantity} to change the quantity of a product by a relative amount.
     * @param productID     The productID of the product whose stock quantity is to be modified
     * @param amount        The amount by which the stock quantity is to be modified (negative or positive)
     */
    private void changeQuantity(String productID, int amount) {
        int quant = getQuantity(productID) + amount;
        setQuantity(productID, quant);
    }

    /**
     * Uses {@link #setQuantity} to add a relative amount of quantity to a product.
     * Throws an {@link IllegalArgumentException} if the amount entered is negative
     * @param productID the productID associated with the product whose quantity is to be increased
     * @param amount the amount by which it is to be increased
     */
    public void add(String productID, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot add a negative amount of quantity to a product. Please use the 'remove' function instead.");
        }
        changeQuantity(productID, amount);
    }

    /**
     * Uses {@link #setQuantity} to remove a relative amount of quantity to the product.
     * Throws an {@link IllegalArgumentException} if the amount entered is negative
     * @param productID the productID associated with the product whose quantity is to be increased
     * @param amount the amount by which it is to be increased
     */
    public void remove(String productID, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot remove a negative amount of quantity to a product. Please use the 'add' function instead.");
        }
        changeQuantity(productID, -amount);
    }

    /**
     * Returns a stream containing the productID of all products registered in this collection
     * @return a stream of productIDs
     */
    public Stream<String> getAllProductIDs() {
        return IDtoPQPair.keySet().stream();
    }

    /**
     * Returns true if there are no products registered in this collection.
     * Will return true with products registered, even if they have quantities of 0.
     * @return true if there are no products registered in the collection, false otherwise.
     */
    public boolean isEmpty() {
        return IDtoPQPair.isEmpty();
    }
}
