
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<String> lostItems = new ArrayList<>();
    static ArrayList<String> foundItems = new ArrayList<>();

    public static void main(String[] args) {

        int choice = 0;

        do {
            System.out.println("\n==============================");
            System.out.println("       FINDBACK SYSTEM");
            System.out.println("==============================");
            System.out.println("1. Report Lost Item");
            System.out.println("2. Report Found Item");
            System.out.println("3. Search for Matches");
            System.out.println("4. Exit");
            System.out.println("==============================");

            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input! Enter a number.");
                sc.nextLine();
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.println("\n--- REPORT LOST ITEM ---");
                    System.out.print("Enter lost item name: ");
                    String lost = sc.nextLine().trim();

                    if (lost.isEmpty()) {
                        System.out.println("Item name cannot be empty!");
                    } else {
                        lostItems.add(lost);
                        System.out.println("Lost item registered successfully!");
                    }
                    break;

                case 2:
                    System.out.println("\n--- REPORT FOUND ITEM ---");
                    System.out.print("Enter found item name: ");
                    String found = sc.nextLine().trim();

                    if (found.isEmpty()) {
                        System.out.println("Item name cannot be empty!");
                    } else {
                        foundItems.add(found);
                        System.out.println("Found item registered successfully!");
                    }
                    break;

                case 3:
                    System.out.println("\n--- SEARCH FOR MATCHES ---");

                    if (lostItems.isEmpty() || foundItems.isEmpty()) {
                        System.out.println("Not enough items to compare!");
                        break;
                    }

                    boolean matchFound = false;

                    for (String lostItem : lostItems) {
                        for (String foundItem : foundItems) {

                            if (lostItem.equalsIgnoreCase(foundItem)) {
                                System.out.println("MATCH FOUND!");
                                System.out.println("Item: " + lostItem);
                                matchFound = true;
                            }
                        }
                    }

                    if (!matchFound) {
                        System.out.println("No matching items found.");
                    }
                    break;

                case 4:
                    System.out.println("\nThank you for using FindBack!");
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }

        } while (choice != 4);

        sc.close();
    }
}
