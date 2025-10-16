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

        System.out.println("Welcome to FinApp, your personal finance tracker");

        while (running) {
            System.out.println("""
                    Select an option:
                    1. Add a deposit
                    2. Make a Payment
                    3. Access your Ledger
                    4. Exit Application
                    """);

            byte userChoice = (scanner.nextByte());
            scanner.nextLine();
            switch (userChoice) {
                case 1 -> {
                    System.out.println("===Add Deposit===");
                    try {
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
                        BufferedWriter buffWriter = new BufferedWriter(new FileWriter("transactions.csv", true));
                        buffWriter.write(transaction.toCSV());
                        buffWriter.newLine();
                        buffWriter.close();

                        System.out.printf("$%.2f has been added to your account\n", userDepositAmount);

                    } catch (IOException e) {
                        System.out.println("Error saving deposit: " + e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.println("===Add Payment===");
                    try {
                        BufferedWriter buffWriter = new BufferedWriter(new FileWriter("transactions.csv", true));

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
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        buffWriter.write(transaction.toCSV());
                        buffWriter.newLine();
                        buffWriter.flush();
                        buffWriter.close();

                        System.out.printf("$%.2f has been sent as a payment to %s for '%s'\n",userPaymentAmount,userPaymentVendor,userPaymentDescription);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 3 -> {
                    System.out.println("""
                            Ledger Options:
                            1. Show all Entries
                            2. Show only Deposits
                            3. Show only Payments
                            4. View Reports
                            5. Exit to Main Menu
                            6. Exit Program
                            """);
                    byte userLedgerOption = scanner.nextByte();
                    switch (userLedgerOption) {
                        case 1 -> {
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
                        case 2 -> {
//                            ArrayList<Transaction> transactions = loadTransactions();
//                            for (Transaction item : transactions) {
//                                if()
//                            }
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
            String line;
            while ((line = buffReader.readLine()) != null) {
                Transaction transaction = parseTransaction(line);
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