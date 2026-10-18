import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice=0;

        do {
            System.out.println("\n====================================");
            System.out.println("           LOCALBIZ OS");
            System.out.println("       PRODUCT MANAGEMENT");
            System.out.println("====================================");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Search Product");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("6. Stock Management");
            System.out.println("7. Generate Bill");
            System.out.println("8. Exit");
            System.out.println("====================================");
            System.out.print("Enter your choice: ");

            try {
                String input = sc.nextLine();

                if (input == null || input.isBlank()) {
                    System.out.println("Please enter a valid choice.");
                    continue;
                }

                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Enter a number.");
                continue;
            }

            switch (choice) {

                case 1:
                    ProductManager.main(new String[]{});
                    break;

                case 2:
                    ViewProducts.main(new String[]{});
                    break;

                case 3:
                    SearchProduct.main(new String[]{});
                    break;

                case 4:
                    UpdateProduct.main(new String[]{});
                    break;

                case 5:
                    DeleteProduct.main(new String[]{});
                    break;

                case 6:
                    StockManagement.main(new String[]{});
                    break;

                case 7:
                    BillingSystem.main(new String[]{});
                    break;

                case 8:
                    System.out.println("Thank you for using LocalBiz OS!");
                    break;

                default:
                    System.out.println("Invalid choice! Select 1-8.");
            }

        } while (choice != 8);

        
    }
}