import java.util.*;

class Product {
    private int id;
    private String name;
    private int price;

   
    public Product(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return id + ". " + name + " (Rupees " + price + ")";
    }
}

class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void reduceQuantity(int amount) {
        this.quantity -= amount;
    }

    public int getTotalPriceInINR() {
        return product.getPrice() * quantity;
    }

    public String toString() {
        return product.getName() + " (x" + quantity + "): Rupees " + getTotalPriceInINR();
    }
}

class StationeryShopA {
    private static List<Product> products = new ArrayList<>();
    private static List<OrderItem> orderItems = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Add some products to the list
        addProducts();

        // Display available products
        displayProducts();

        // Prompt and validate customer name
        String customerName = "";
        while (true) {
            System.out.print("Enter customer name: ");
            customerName = scanner.nextLine();
            if (isValidCustomerName(customerName)) {
                break;
            } else {
                System.out.println("Invalid name! Please enter a name using only alphabets and spaces.");
            }
        }

        boolean finished = false;
        while (!finished) {
            System.out.print("Select a product by ID (0 to finish, -1 to remove an item, -2 to reduce quantity): ");
            int productId = scanner.nextInt();

            if (productId == 0) {
                finished = true;
            } else if (productId == -1) {
                removeItem(scanner);
            } else if (productId == -2) {
                reduceQuantity(scanner);
            } else {
                Product product = getProductById(productId);
                if (product != null) {
                    System.out.print("Enter quantity (must be greater than zero): ");
                    int quantity = scanner.nextInt();
                    if (quantity <= 0) {
                        System.out.println("Invalid quantity. Quantity must be greater than zero.");
                    } else {
                        orderItems.add(new OrderItem(product, quantity));
                        System.out.println("Added " + quantity + " x " + product.getName() + " to your order.");
                    }
                } else {
                    System.out.println("Invalid product ID. Please try again.");
                }
            }
        }

        printOrderSummary();
        scanner.close();
    }

   private static void addProducts() {
    // Adding products with correct constructor parameters
    products.add(new Product(1, "Pen", 10));
    products.add(new Product(2, "Notebook", 50));
    products.add(new Product(3, "Eraser", 5));
    products.add(new Product(4, "Marker", 15));
    products.add(new Product(5, "Folder", 20));
    products.add(new Product(6, "Pencil", 5));
    products.add(new Product(7, "Highlighter", 20));
    products.add(new Product(8, "Stapler", 55));
    products.add(new Product(9, "Glue", 25));
    products.add(new Product(10, "Scissors", 60));
}

    private static void displayProducts() {
        System.out.println("Available products:");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private static boolean isValidCustomerName(String name) {
        return name.matches("^[a-zA-Z\\s]+$"); 
    }

    private static Product getProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null; 
    }

    private static void printOrderSummary() {
        System.out.println("Order Summary:");
        int totalAmount = 0;

        for (OrderItem item : orderItems) {
            String productName = item.getProduct().getName();
            int quantity = item.getQuantity();
            int totalPrice = item.getTotalPriceInINR();
            totalAmount += totalPrice;

            System.out.println(productName + " (x" + quantity + "): Rupees " + totalPrice);
        }

        System.out.println("Total Amount: Rupees " + totalAmount);
    }

    private static void removeItem(Scanner scanner) {
        if (orderItems.isEmpty()) {
            System.out.println("No items in the order to remove.");
            return;
        }

        System.out.println("Current items in your order:");
        for (int i = 0; i < orderItems.size(); i++) {
            System.out.println((i + 1) + ". " + orderItems.get(i));
        }

        System.out.print("Select the item number to remove: ");
        int itemNumber = scanner.nextInt();
        if (itemNumber > 0 && itemNumber <= orderItems.size()) {
            orderItems.remove(itemNumber - 1);
            System.out.println("Item removed from the order.");
        } else {
            System.out.println("Invalid item number.");
        }
    }

    private static void reduceQuantity(Scanner scanner) {
        if (orderItems.isEmpty()) {
            System.out.println("No items in the order to reduce quantity.");
            return;
        }

        System.out.println("Current items in your order:");
        for (int i = 0; i < orderItems.size(); i++) {
            System.out.println((i + 1) + ". " + orderItems.get(i));
        }

        System.out.print("Select the item number to reduce quantity: ");
        int itemNumber = scanner.nextInt();
        if (itemNumber > 0 && itemNumber <= orderItems.size()) {
            OrderItem item = orderItems.get(itemNumber - 1);
            System.out.print("Enter quantity to reduce: ");
            int reduceQuantity = scanner.nextInt();
            if (reduceQuantity > 0 && reduceQuantity <= item.getQuantity()) {
                item.reduceQuantity(reduceQuantity);
                if (item.getQuantity() == 0) {
                    orderItems.remove(itemNumber - 1);
                }
                System.out.println("Reduced quantity successfully.");
            } else {
                System.out.println("Invalid quantity to reduce.");
            }
        } else {
            System.out.println("Invalid item number.");
        }
    }
}
