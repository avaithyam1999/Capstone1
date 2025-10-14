import java.util.Scanner;

public class LedgerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double accountTotal = 0;
        boolean running = true;


        System.out.println("Welcome to your personal Accounting App!");
        while (running) {
            System.out.println("""
                    Please select an action:
                    1. Add Deposit
                    2. Make Payment
                    3. View Ledger
                    4. Exit
                    """);
            byte userOption = scanner.nextByte();
            switch (userOption) {
                case 1 -> {
                    System.out.println("Please enter the amount for your deposit: ");
                    double userDepositAmount = scanner.nextDouble();

                    accountTotal += userDepositAmount;
                    System.out.println("Your updated deposit amount is: $" + accountTotal);
                }
                case 2 -> {
                    System.out.println("you selected make payment");
                }
                case 3 -> {
                    System.out.println("u wanna see deez(ledger)");
                }
                case 4 -> {
                    System.out.println("Thank you for using your Personal Accounting App.\nGoodBye!");
                    running = false;
                }
            }
        }
    }
}
