import java.io.*;
import java.util.*;

class Product implements Serializable {
    int id;
    String name;
    double price;
    int quantity;

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public void display() {
        System.out.printf("%-5d %-20s %-10.2f %-10d\n", id, name, price, quantity);
    }
}

public class MobileStore {
    static ArrayList<Product> products = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        loadFromFile();

        do {
            System.out.println("\n===== MOBILE STORE MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Search Product");
            System.out.println("6. Generate Bill");
            System.out.println("7. Save & Exit");
            System.out.print("Enter your choice: ");
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                sc.nextLine(); // clear buffer
                continue;
            }

            switch (choice) {
                case 1 -> addProduct();
                case 2 -> viewProducts();
                case 3 -> updateProduct();
                case 4 -> deleteProduct();
                case 5 -> searchProduct();
                case 6 -> generateBill();
                case 7 -> saveToFile();
                default -> System.out.println("Invalid choice!");
            }
        } while (true);
    }

    static void addProduct() {
        try {
            System.out.print("Enter Product ID: ");
            int id = sc.nextInt();
            for (Product p : products) {
                if (p.id == id) {
                    System.out.println("Product ID already exists. Try a different one.");
                    return;
                }
            }
            sc.nextLine(); // consume newline
            System.out.print("Enter Product Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Price: ");
            double price = sc.nextDouble();
            System.out.print("Enter Quantity: ");
            int quantity = sc.nextInt();

            products.add(new Product(id, name, price, quantity));
            System.out.println("Product added successfully.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please try again.");
            sc.nextLine(); // clear buffer
        }
    }

    static void viewProducts() {
    if (products.isEmpty()) {
        System.out.println("No products available.");
        return;
    }

    System.out.println("\n================================================================================");
    System.out.printf("%-5s %-35s %-15s %-10s\n", "ID", "Name", "Price", "Quantity");
    System.out.println("================================================================================");

    for (Product p : products) {
        System.out.printf("%-5d %-35s Rs.%-13.2f %-10d\n",
                p.id,
                (p.name.length() > 33 ? p.name.substring(0, 32) + "…" : p.name),
                p.price,
                p.quantity);
    }

    System.out.println("================================================================================");
}

    static void updateProduct() {
        System.out.print("Enter Product ID to update: ");
        int id = sc.nextInt();
        for (Product p : products) {
            if (p.id == id) {
                System.out.print("Enter New Price: ");
                p.price = sc.nextDouble();
                System.out.print("Enter New Quantity: ");
                p.quantity = sc.nextInt();
                System.out.println("Product updated successfully.");
                return;
            }
        }
        System.out.println("Product not found.");
    }

    static void deleteProduct() {
        System.out.print("Enter Product ID to delete: ");
        int id = sc.nextInt();
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            Product p = iterator.next();
            if (p.id == id) {
                iterator.remove();
                System.out.println("Product deleted successfully.");
                return;
            }
        }
        System.out.println("Product not found.");
    }

    static void searchProduct() {
        sc.nextLine(); // consume newline
        System.out.print("Enter product name or part of it: ");
        String keyword = sc.nextLine().toLowerCase();
        boolean found = false;

        for (Product p : products) {
            if (p.name.toLowerCase().contains(keyword)) {
                if (!found) {
                    System.out.printf("%-5s %-20s %-10s %-10s\n", "ID", "Name", "Price", "Quantity");
                    found = true;
                }
                p.display();
            }
        }
        if (!found) {
            System.out.println("No products match your search.");
        }
    }

    static void generateBill() {
    if (products.isEmpty()) {
        System.out.println("No products available.");
        return;
    }

    double total = 0;
    viewProducts();
    System.out.print("Enter number of products to purchase: ");
    int n = sc.nextInt();

    StringBuilder bill = new StringBuilder();
    bill.append("=========== BILL RECEIPT ===========\n");
    bill.append(String.format("%-20s %-10s %-10s\n", "Product", "Qty", "Subtotal"));

    for (int i = 0; i < n; i++) {
        System.out.print("Enter Product ID: ");
        int id = sc.nextInt();
        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();

        boolean found = false;
        for (Product p : products) {
            if (p.id == id) {
                found = true;
                if (p.quantity >= qty) {
                    double cost = p.price * qty;
                    total += cost;
                    p.quantity -= qty;
                    bill.append(String.format("%-20s %-10d ₹%-10.2f\n", p.name, qty, cost));
                    if (p.quantity <= 3) {
                        System.out.println("⚠️ Low stock alert: " + p.name + " has only " + p.quantity + " units left.");
                    }
                } else {
                    System.out.println("Only " + p.quantity + " units available for " + p.name);
                }
                break;
            }
        }
        if (!found) {
            System.out.println("Product ID " + id + " not found.");
        }
    }

    double tax = total * 0.18;
    double finalAmount = total + tax;
    bill.append("\nSubtotal: Rs.").append(String.format("%.2f", total));
    bill.append("\nGST (18%): Rs").append(String.format("%.2f", tax));
    bill.append("\nTotal Amount: Rs").append(String.format("%.2f", finalAmount));
    bill.append("\n====================================");

    System.out.println("\n" + bill);

    // ✅ Write bill to file in append mode with separator
    try (FileWriter fw = new FileWriter("bill.txt", true);
         PrintWriter out = new PrintWriter(fw)) {
        out.println("\n------------------------------------------");
        out.println("         NEW BILL - Mobile Store");
        out.println("------------------------------------------");
        out.println(bill);
        out.println("------------------------------------------\n");
        System.out.println("Bill saved to 'bill.txt'");
    } catch (IOException e) {
        System.out.println("Error writing bill to file.");
    }
}

    static void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("products.dat"))) {
            out.writeObject(products);
            System.out.println("Data saved successfully.");
            System.exit(0); // Exit after saving
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    static void loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("products.dat"))) {
            products = (ArrayList<Product>) in.readObject();
            System.out.println("Data loaded successfully.");
        } catch (Exception e) {
            System.out.println("No saved data found.");
        }
    }
}