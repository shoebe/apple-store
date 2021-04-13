package store;

/**
 * A type of ProductQuantityCollection where Products can never have negative quantity
 * Products are also not removed when they reach 0 quantity.
 * @author Sebastien Marleau 101155551
 * @version Milestone 4
 */
public class Inventory extends ProductStockContainer {

    /**
     * Initializes Inventory with a preset of items
     */
    public Inventory() {
        super();
        Product p0 = new Product("Opal", 1.2);
        Product p1 = new Product("Jazz", 0.08);
        Product p2 = new Product("Red delicious", 0.17);
        Product p3 = new Product("Ambrosia", 4.11);
        Product p4 = new Product("Mcintosh", 0.46);
        Product p5 = new Product("Fuji", 1.34);
        Product p7 = new Product("Pink lady", 2.2);
        Product p8 = new Product("Envy", 0.81);
        Product p9 = new Product("Honeycrisp", 1.27);
        Product p10 = new Product("Kiku", 5.74);
        Product p11 = new Product("Gala", 9.43);
        Product p12 = new Product("Sweetie", 3.94);
        Product p13 = new Product("Golden delicious", 4.08);
        Product p14 = new Product("Granny smith", 5.63);

        addNewProduct(p0, 65);
        addNewProduct(p1, 56);
        addNewProduct(p2, 35);
        addNewProduct(p3, 71);
        addNewProduct(p4, 31);
        addNewProduct(p5, 19);
        addNewProduct(p7, 44);
        addNewProduct(p8, 77);
        addNewProduct(p9, 47);
        addNewProduct(p10, 69);
        addNewProduct(p11, 88);
        addNewProduct(p12, 99);
        addNewProduct(p13, 26);
        addNewProduct(p14, 80);
    }

    /**
     * {@inheritDoc}
     * If quantity is a negative value, throws an {@link IllegalArgumentException}.
     * @param productID the productID associated with the product
     * @param quantity the quantity to set the product to
     */
    @Override
    public void setProductQuantity(String productID, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Products cannot have a negative quantity in the inventory");
        }
        super.setProductQuantity(productID, quantity);
    }

}
