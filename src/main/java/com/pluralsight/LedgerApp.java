package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class LedgerApp {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        BufferedReader buffReader = new BufferedReader(new FileReader("transactions.csv"));

        System.out.println("Welcome to ^FinApp^, your personal finance tracker");

        while (running) {
            System.out.println("""
                    ===FinApp===
                    Select an option:
                    1. Record a deposit
                    2. Record a Payment
                    3. Access your Ledger
                    4. Exit FinApp
                    """);

            byte userChoice = (scanner.nextByte());
            scanner.nextLine();
            switch (userChoice) {
                case 1 -> {
                    addDeposit(scanner);
                }
                case 2 -> {
                    addPayment(scanner);
                }
                case 3 -> {
                    System.out.println("""
                            ===Ledger===
                            Select an option:
                            1. Show all Entries
                            2. Show only Deposits
                            3. Show only Payments
                            4. View Reports
                            5. Exit to Main Menu
                            6. Exit FinApp
                            """);
                    byte userLedgerOption = scanner.nextByte();
                    switch (userLedgerOption) {
                        case 1 -> {
                            showAllEntries(buffReader);
                        }
                        case 2 -> {
                            showDeposits();
                        }
                        case 3 -> {
                            showPayments();
                        }
                        case 4 -> {
                            System.out.println("""
                                    ===Reports===
                                    Select an Option to view by:
                                    1. Month to Date
                                    2. Previous Month
                                    3. Year to Date
                                    4. Previous Year
                                    5. Search by Vendor
                                    6. Go Back to Ledger Menu
                                    """);
                            byte userReportInput = scanner.nextByte();
                            scanner.nextLine();

                            switch (userReportInput) {
                                case 1 -> {
                                    ArrayList<Transaction> transactions = loadTransactions();
                                    LocalDate now = LocalDate.now();
                                    LocalDate startOfMonth = now.withDayOfMonth(1); //finds the first day of the current month
                                    boolean transactionFound = false;
                                    int transactionCount = 0;

                                    System.out.printf("===Report for %s to %s\n", startOfMonth, now);
                                    for (Transaction item : transactions) {
                                        LocalDate transactionDate = item.getDate();
                                        if (transactionDate.isAfter(startOfMonth) && transactionDate.isBefore(now)) {
                                            System.out.println(item);
                                            transactionFound = true;
                                            transactionCount ++;
                                        }
                                    }
                                    if (transactionFound) {
                                        System.out.printf("%d transactions found from %s to %s\n", transactionCount, startOfMonth, now);
                                    } else {
                                        System.out.printf("No transactions from from %s to %s\n",startOfMonth,now);
                                    }
                                }
                                case 2 -> {
                                    ArrayList<Transaction> transactions = loadTransactions();
                                    LocalDate now = LocalDate.now();
                                    LocalDate startOfPreviousMonth = now.minusMonths(1).withDayOfMonth(1); //finds the first day of current month minus 1
                                    LocalDate endOfPreviousMonth = now.withDayOfMonth(1).minusDays(1); // finds the last day by subtracting current day by 1
                                    boolean transactionFound = false;
                                    int transactionCount = 0;

                                    System.out.printf("===Report for %s to %s===\n",startOfPreviousMonth,endOfPreviousMonth);
                                    for (Transaction item : transactions) {
                                        if (item.getDate().isAfter(startOfPreviousMonth) && item.getDate().isBefore(endOfPreviousMonth)) {
                                            System.out.println(item);
                                            transactionFound = true;
                                            transactionCount ++;
                                        }
                                    }
                                    if (transactionFound) {
                                        System.out.printf("%d transactions found from %s to %s\n", transactionCount, startOfPreviousMonth, endOfPreviousMonth);
                                    } else {
                                        System.out.printf("No transactions from from %s to %s\n",startOfPreviousMonth,endOfPreviousMonth);
                                    }
                                }
                                case 3 -> {

                                }
                            }
                        }
                        default -> System.out.println("You didn't enter a proper value");
                    }
                }
                case 4 -> {
                    System.out.println("Thanks for using this awesome app! Have a great day and goodbye!");
                    running = false;
                }
                default -> {
                    System.out.println("Number is invalid or out of range");
                }
            }
        }
    }

    private static void showPayments() {
        int paymentLength = 0;
        double paymentSum = 0;
        ArrayList<Transaction> transactions = loadTransactions();
        System.out.println("===Payments===");
        for (Transaction item : transactions) {
            if (item.getAmount() < 0) {
                System.out.println(item);
                paymentLength++;
                paymentSum += Math.abs(item.getAmount());
            }
        }
        System.out.printf("You have %d payments totaling $%.2f\n\n",paymentLength,paymentSum);
    }

    private static void showDeposits() {
        int depositLength = 0;
        double depositSum = 0;
        ArrayList<Transaction> transactions = loadTransactions();
        System.out.println("===Deposits===");
        for (Transaction item : transactions) {
            if (item.getAmount() > 0) {
                System.out.println(item);
                depositLength++;
                depositSum += item.getAmount();
            }
        }
        System.out.printf("You have %d deposits totaling $%.2f.\n\n",depositLength, depositSum);
    }

    private static void showAllEntries(BufferedReader buffReader) throws IOException {
        String line;
        while ((line = buffReader.readLine()) != null) {
            if (line.contains(",")) {
                System.out.println(line);
            } else {
                String commaLine = line.replace("|", ",");
                System.out.println(commaLine);
            }
        }
        buffReader.close();
    }

    private static void addPayment(Scanner scanner) {
        System.out.println("===Add Payment===");
        String userPaymentDescription = "";
        String userPaymentVendor = "";
        double userPaymentAmount = 0;
        Transaction transaction = null;
        try {
            System.out.println("Describe your payment: ");
            userPaymentDescription = scanner.nextLine();
            System.out.println("Who would you like to send the payment to: ");
            userPaymentVendor = scanner.nextLine();
            System.out.println("How much would you like to deposit?");
            userPaymentAmount = scanner.nextDouble();
            scanner.nextLine();
            transaction = new Transaction(LocalDate.now(),
                    LocalTime.now(),
                    userPaymentDescription,
                    userPaymentVendor,
                    -userPaymentAmount
            );
            saveTransaction(transaction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        System.out.printf("$%.2f has been recorded as a payment to %s for '%s'\n",userPaymentAmount,userPaymentVendor,userPaymentDescription);
    }

    private static void addDeposit(Scanner scanner) {
        System.out.println("===Add Deposit===");
        // Get user input
        System.out.println("Enter the description for your deposit: ");
        String userDepositDescription = scanner.nextLine();

        System.out.println("Enter the recipient for your deposit: ");
        String userDepositVendor = scanner.nextLine();

        System.out.println("How much would you like to deposit?");
        double userDepositAmount = scanner.nextDouble();
        scanner.nextLine();

        // Create transaction
        Transaction transaction = new Transaction(
                LocalDate.now(),
                LocalTime.now(),
                userDepositDescription,
                userDepositVendor,
                userDepositAmount
        );

        // Write to file
        saveTransaction(transaction);

        System.out.printf("$%.2f has been saved as a deposit to your account from %s for %s\n", userDepositAmount, userDepositVendor, userDepositDescription);
    }

    private static void saveTransaction(Transaction transaction) {
        try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            buffWriter.write(transaction.toCSV());
            buffWriter.newLine();
        } catch (IOException e) {
            System.out.println("Error saving transaction to CSV");
        }
    }

    private static ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try (BufferedReader buffReader = new BufferedReader(new FileReader("transactions.csv"))) {
            buffReader.readLine();
            String line;
            while ((line = buffReader.readLine()) != null) {
                Transaction transaction = parseTransaction(line);
                transactions.add(transaction);
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions. Try again");
        }
        return transactions;
    }

    private static Transaction parseTransaction(String line) {
        try {
            String[] parts = line.split("\\|");

            LocalDate date = LocalDate.parse(parts[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalTime time = LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("HH:mm:ss"));
            String description = parts[2];
            String vendor = parts[3];
            double amount = Double.parseDouble(parts[4]);

            return new Transaction(date, time, description, vendor, amount);

        } catch (NumberFormatException e) {
            System.out.println("Error parsing transaction. Try again");
            return null;
        }
    }
}