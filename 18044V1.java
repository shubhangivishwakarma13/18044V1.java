import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Product {
    private String name;
    private int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPriceInINR() {
        return price;
    }

    public String toString() {
        return name + ": " + formatCurrency(getPriceInINR());
    }

    private String formatCurrency(int amount) {
        return "Rupees " + String.format("%,d", amount);
    }
}

class Customer {
    private String name;

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public void updateQuantity(int delta) {
        this.quantity += delta;
    }

    public int getTotalPriceInINR() {
        return product.getPriceInINR() * quantity;
    }

    public String toString() {
        return product.getName() + " (x" + quantity + "): " + formatCurrency(getTotalPriceInINR());
    }

    private String formatCurrency(int amount) {
        return "Rupees " + String.format("%,d", amount);
    }
}

class Order {
    private Customer customer;
    private List<OrderItem> orderItems;
    private int totalAmount = 0;

    public Order(Customer customer) {
        this.customer = customer;
        this.orderItems = new ArrayList<>();
    }

    public void addProduct(Product product, int quantity) {
        boolean found = false;
        for (OrderItem item : orderItems) {
            if (item.getProduct().equals(product)) {
                item.updateQuantity(quantity);
                totalAmount += product.getPriceInINR() * quantity;

                if (item.getQuantity() <= 0) {
                    orderItems.remove(item);
                    System.out.println("Removed " + product.getName() + " from the order.");
                }
                found = true;
                break;
            }
        }

        if (!found && quantity > 0) {
            orderItems.add(new OrderItem(product, quantity));
            totalAmount += product.getPriceInINR() * quantity;
        }
    }

    public void printOrder() {
        System.out.println("Order for " + customer.getName() + ":");
        for (OrderItem item : orderItems) {
            System.out.println(item);
        }
        System.out.println("Total Amount: " + formatCurrency(totalAmount));
    }

    private String formatCurrency(int amount) {
        return "Rupees " + String.format("%,d", amount);
    }
}

class ProductService {
    private static Product[] products = {
        new Product("Pen", 10),
        new Product("Notebook", 50),
        new Product("Eraser", 5),
        new Product("Marker", 15),
        new Product("Folder", 20),
        new Product("Pencil", 5),
        new Product("Highlighter", 20),
        new Product("Stapler", 55),
        new Product("Glue", 25),
        new Product("Scissors", 60)
    };

    public static Product[] getProducts() {
        return products;
    }

    public static void displayProducts() {
        System.out.println("Available products:");
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ". " + products[i]);
        }
    }

    public static int askForProductChoice(Scanner scanner) {
        System.out.print("Select a product by number (or type 0 to finish): ");
        return scanner.nextInt();
    }

    public static int askForQuantity(Scanner scanner, int choice) {
        System.out.print("Enter quantity for " + getProduct(choice).getName() + ": ");
        return scanner.nextInt();
    }

    public static void addProductToOrder(Order order, int choice, int quantity) {
        order.addProduct(getProduct(choice), quantity);
    }

    public static Product getProduct(int choice) {
        if (choice > 0 && choice <= products.length) {
            return products[choice - 1];
        }
        return null;
    }
}

public class StationeryShopA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();
        Customer customer = new Customer(customerName);
        Order order = new Order(customer);

        ProductService.displayProducts();
        boolean finished = false;
        int productLimit = 50;
        int selectedProducts = 0;

        while (!finished && selectedProducts < productLimit) {
            System.out.println("\nOptions:");
            System.out.println("1. Add a product");
            System.out.println("2. Remove a product");
            System.out.println("0. Finish order");
            System.out.print("Enter your choice: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1: // Add a product
                    ProductService.displayProducts();
                    int addChoice = ProductService.askForProductChoice(scanner);

                    if (addChoice > 0 && addChoice <= ProductService.getProducts().length) {
                        int addQuantity = ProductService.askForQuantity(scanner, addChoice);
                        if (addQuantity > 0) {
                            ProductService.addProductToOrder(order, addChoice, addQuantity);
                            System.out.println("Added " + addQuantity + " x " + ProductService.getProduct(addChoice).getName() + " to your order.");
                            selectedProducts++;
                        } else {
                            System.out.println("Quantity must be greater than 0.");
                        }
                    } else {
                        System.out.println("Invalid product selection.");
                    }
                    break;

                case 2: // Remove a product
                    System.out.println("\nYour current order:");
                    order.printOrder();
                    System.out.print("Select a product by number to remove: ");
                    int removeChoice = scanner.nextInt();

                    if (removeChoice > 0 && removeChoice <= ProductService.getProducts().length) {
                        System.out.print("Enter quantity to remove for " + ProductService.getProduct(removeChoice).getName() + ": ");
                        int removeQuantity = scanner.nextInt();
                        if (removeQuantity > 0) {
                            ProductService.addProductToOrder(order, removeChoice, -removeQuantity);
                            System.out.println("Removed " + removeQuantity + " x " + ProductService.getProduct(removeChoice).getName() + " from your order.");
                        } else {
                            System.out.println("Quantity must be greater than 0.");
                        }
                    } else {
                        System.out.println("Invalid product selection.");
                    }
                    break;

                case 0: // Finish order
                    finished = true;
                    break;

                default:
                    System.out.println("Invalid option. Please choose again.");
            }

            if (selectedProducts == productLimit) {
                System.out.println("Product limit reached. You can select up to " + productLimit + " products.");
                finished = true;
            }
        }

        order.printOrder();
        scanner.close();
    }
}
