
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The user's view of a store.
 * Each individual user has an instance of a StoreView.
 * @author Sebastien Marleau 10115551
 * @version Milestone 2
 */
public class StoreView {
    private final StoreManager storeManager;
    private final String cartID;

    /**
     * The constructor for StoreView
     * @param storeManager the StoreManager instance who's store is to be viewed
     */
    public StoreView(StoreManager storeManager) {
        this.storeManager = storeManager;
        this.cartID = storeManager.createNewCart();
    }

    /**
     * Displays the GUI
     * @return true if the user wants to discard the cart, false otherwise
     */
    public boolean displayGUI() {
        Scanner input = new Scanner(System.in);
        while (true) {
            storeManager.printInventory();
            storeManager.printCart(this.cartID);
            System.out.println("Commands:\n" +
                    "   (1) add product(s) to cart\n" +
                    "   (2) remove product(s) from cart\n" +
                    "   (3) reset cart\n" +
                    "   (4) checkout\n" +
                    "   (5) save cart and quit\n" +
                    "   (6) quit");
            System.out.print("Enter command: ");
            switch (input.nextLine().strip()) {
                case "1":
                    System.out.print("Enter ID of product: ");
                    String id = input.nextLine().strip();
                    while (!storeManager.hasProductInInventory(id) && !id.equalsIgnoreCase("cancel")) {
                        System.out.print("Please enter a valid product ID, or 'cancel' to cancel: ");
                        id = input.nextLine().strip();
                    }
                    System.out.print("Enter quantity to add to cart: ");
                    int quantity = Integer.parseInt(input.nextLine().strip());
                    while (quantity < 0) {
                        System.out.println("Please enter a non-negative quantity: ");
                        quantity = Integer.parseInt(input.nextLine().strip());
                    }
                    if (quantity > storeManager.getStockOfProduct(id)) {
                        System.out.print("Notice: not enough stock\npress any key to continue");
                        input.nextLine();
                    }
                    storeManager.addToCart(cartID, id, quantity);
                    break;
                case "2":
                    System.out.print("Enter ID of product: ");
                    id = input.nextLine().strip();
                    while (!storeManager.hasProductInInventory(id) && !id.equalsIgnoreCase("cancel")) {
                        System.out.print("Please enter a valid product ID, or 'cancel' to cancel: ");
                        id = input.nextLine().strip();
                    }
                    System.out.print("Enter quantity to remove from cart: ");
                    quantity = Integer.parseInt(input.nextLine().strip());
                    while (quantity < 0) {
                        System.out.println("Please enter a non-negative quantity: ");
                        quantity = Integer.parseInt(input.nextLine().strip());
                    }
                    storeManager.removeFromCart(cartID, id, quantity);
                    break;
                case "3":
                    storeManager.resetCart(cartID);
                    break;
                case "4":
                    storeManager.printCart(cartID);
                    storeManager.printCartSummary(cartID);
                    System.out.print("Continue with checkout? (yes or no): ");
                    if (input.nextLine().strip().equalsIgnoreCase("yes")) {
                        storeManager.removeCart(cartID);
                        System.out.print("Thank you for you purchase!\npress any key to continue");
                        input.nextLine();
                        return true;
                    }
                    break;
                case "5":
                    return false;
                case "6":
                    storeManager.removeCart(cartID);
                    return true;
                default:
                    System.out.print("Invalid command! Please enter a number from 1-6.\npress any key to continue");
                    input.nextLine();
                    break;
            }
        }
    }

    public static void main(String[] args) {
        StoreManager sm = new StoreManager();
        List<StoreView> users = new ArrayList<>();
        users.add(new StoreView(sm));
        users.add(new StoreView(sm));
        users.add(new StoreView(sm));
        Scanner sc = new Scanner(System.in);
        while (users.size() > 0) {
            System.out.printf("CHOOSE YOUR STOREVIEW (%d-%d) >>> ", 0, users.size()-1);
            int choice = sc.nextInt();
            if (choice < users.size() && choice >= 0) {
                boolean deleted = users.get(choice).displayGUI();
                if (deleted) {
                    users.remove(choice);
                }
            } else {
                System.out.printf("MAIN > ERROR > BAD CHOICE\nPLEASE CHOOSE IN RANGE [%d, %d]\n", 0, users.size() - 1);
            }
        }
        System.out.println("ALL STOREVIEWS DEACTIVATED");
    }
}
