package store;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A GUI for an apple store
 * @author Sebastien Marleau 101155551
 * @version Milestone 4
 */
public class StoreView {
    private final StoreManager storeManager;
    private String cartID; // new one on checkout
    private final JFrame frame;
    private final JTextArea cartContents;

    /**
     * The constructor, initializes all GUI elements
     * @param storeManager the StoreManager to view using this GUI
     */
    public StoreView(StoreManager storeManager) {
        this.storeManager = storeManager;
        this.cartID = storeManager.createNewCart();

        frame = new JFrame();
        frame.setTitle("Apple Store");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit?")
                        == JOptionPane.OK_OPTION) {
                    // close it down!
                    frame.setVisible(false);
                    frame.dispose();
                }
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Left side of the screen, the list of all products
        JPanel productsPanel = new JPanel(new FlowLayout());
        productsPanel.setPreferredSize(new Dimension(1000,2000));
        JScrollPane productsPanelScroll = new JScrollPane(productsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        productsPanelScroll.setPreferredSize(new Dimension(1000, 900));
        mainPanel.add(productsPanelScroll, BorderLayout.CENTER);

        // Make a panel for each product and put it into productsPanel
        storeManager.getAllProductIDsInInventory().forEach( (id) -> {
            Product product = storeManager.getProduct(id);
            JPanel productPanel = new JPanel();
            productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

            JLabel imgLabel = new JLabel(new ImageIcon("images/" + product.getName().toLowerCase() + ".jpg"));
            imgLabel.setPreferredSize(new Dimension(250, 250));
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            productPanel.add(imgLabel);

            JLabel name = new JLabel(product.getName());
            JLabel info = new JLabel(String.format("($%s) - %s available", product.getPrice(), storeManager.getStockOfProduct(id)));
            name.setAlignmentX(Component.CENTER_ALIGNMENT);
            info.setAlignmentX(Component.CENTER_ALIGNMENT);
            productPanel.add(name);
            productPanel.add(info);

            JPanel cartControl = new JPanel(new FlowLayout()); // panel with the buttons and number box
            JTextField field = new JTextField("5",4);
            // code taken from https://stackoverflow.com/questions/20541230/allow-only-numbers-in-jtextfield
            // allow only number to be typed into the text field
            ((AbstractDocument)field.getDocument()).setDocumentFilter(new DocumentFilter(){
                Pattern regEx = Pattern.compile("\\d*");
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    Matcher matcher = regEx.matcher(text);
                    if(!matcher.matches()){
                        return;
                    }
                    super.replace(fb, offset, length, text, attrs);
                }
            });
            cartControl.add(field);

            JPanel buttons = new JPanel();
            buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS)); // panel to place the buttons one on top of the other
            JButton addToCart = new JButton("Add to cart");
            addToCart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    storeManager.addToCart(cartID, id, Integer.parseInt(field.getText()));
                    info.setText(String.format("($%s) - %s available", product.getPrice(), storeManager.getStockOfProduct(id)));
                    updateCartContents();
                }
            });

            JButton removeFromCart = (new JButton("Remove from cart"));
            removeFromCart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    storeManager.removeFromCart(cartID, id, Integer.parseInt(field.getText()));
                    info.setText(String.format("($%s) - %s available", product.getPrice(), storeManager.getStockOfProduct(id)));
                    updateCartContents();
                }
            });

            addToCart.setAlignmentX(Component.CENTER_ALIGNMENT);
            removeFromCart.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttons.add(addToCart);
            buttons.add(removeFromCart);

            cartControl.add(buttons);
            cartControl.setAlignmentX(Component.CENTER_ALIGNMENT);
            productPanel.add(cartControl);
            productsPanel.add(productPanel);
        });

        // The right side of the screen, the cart
        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        mainPanel.add(cartPanel, BorderLayout.LINE_END);

        JLabel yourCart = new JLabel("Your cart:");
        yourCart.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        yourCart.setBorder(new LineBorder(Color.BLACK, 2));
        yourCart.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartPanel.add(yourCart);

        Font monospace = new Font(Font.MONOSPACED, Font.PLAIN, 16);
        JLabel cartDescription = new JLabel("Product Name    | Quantity | Unit Price | Price   ");
        cartDescription.setFont(monospace);
        cartPanel.add(cartDescription);

        cartContents = new JTextArea();
        cartContents.setFont(monospace);
        cartContents.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartContents.setEditable(false);
        updateCartContents();
        cartPanel.add(cartContents);

        JButton checkout = new JButton("Checkout");
        checkout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (storeManager.getCartItemCount(cartID) == 0) {
                    return;
                }
                Object[] options = {"Confirm and pay", "Cancel and return to store"};
                DecimalFormat twoDecimal = new DecimalFormat("#0.00");
                int choice = JOptionPane.showOptionDialog(frame,
                        String.format("Cart has %s items, for a total of $%s.", storeManager.getCartItemCount(cartID),
                                twoDecimal.format(storeManager.getCartTotalPrice(cartID))),
                        "Confirm checkout",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if (choice == JOptionPane.OK_OPTION) {
                    storeManager.removeCart(cartID);
                    cartID = storeManager.createNewCart();
                    updateCartContents();
                }
            }
        });

        cartPanel.add(checkout);
        frame.add(mainPanel);
        frame.pack();
    }

    /**
     * Updates the cartContents instance variable with a text-based representation of the cart.
     * Regenerates the entire string on each call.
     */
    private void updateCartContents() {
        StringBuilder str = new StringBuilder();
        DecimalFormat twoDecimal = new DecimalFormat("#0.00");
        storeManager.getAllProductIDsInCart(cartID).forEach( (pid) -> {
            Product product = storeManager.getProduct(pid);
            int quantity = storeManager.getQuantityInCart(cartID, pid);
            str.append(String.format("%-16s", product.getName())).append("| ")
                    .append(String.format("%-9s", quantity)).append("| ")
                    .append(String.format("%-11s", " $" + product.getPrice())).append("| ")
                    .append("$").append(twoDecimal.format(product.getPrice() * quantity)).append("\n");
        });
        if (storeManager.getAllProductIDsInCart(cartID).count() == 0) {
            str.append("Empty :(");
        }
        else {
            str.append("-------------------------------------------------\n")
               .append("                                        | $").append(twoDecimal.format(storeManager.getCartTotalPrice(cartID)));
        }
        cartContents.setText(str.toString());
    }

    /**
     * Makes the GUI visible.
     */
    public void displayGUI() {
        frame.setVisible(true);
    }

    /**
     * Initializes a StoreManager and an associated StoreView, then displays the storeview.
     * @param args ignored
     */
    public static void main(String[] args) {
        StoreManager mng = new StoreManager();
        StoreView sv = new StoreView(mng);
        sv.displayGUI();
    }

}
