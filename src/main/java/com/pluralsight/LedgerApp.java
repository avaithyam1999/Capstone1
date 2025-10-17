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
        if (getUserPassAndRunApp(scanner)) {
            runMainApplication(scanner);
        }

    }

    private static boolean getUserPassAndRunApp(Scanner scanner) {
        String realPassword = "password123four";
        String passwordHint = "No hints for you";
        int maxAttempts = 3;
        int passwordAttempts = 0;
        boolean accessGranted = false;

        System.out.println("                                       .--.\n" +
                "                _______             .-\"  .'\n" +
                "        .---u\"\"\"       \"\"\"\"---._  .\"    %\n" +
                "      .'                        \"--.    %\n" +
                " __.--'  o                          \"\".. \"\n" +
                "(____.                                  \":\n" +
                " `----.__                                 \".\n" +
                "         `----------__                     \".\n" +
                "               \".   . \"\"--.                 \".\n" +
                "                 \". \". bIt \"\"-.              \".\n" +
                "                   \"-.)        \"\"-.           \".\n" +
                "                                   \"\".         \".\n" +
                "                                      \"\".       \".\n" +
                "                                         \"\".      \".\n" +
                "                                            \"\".    \".\n" +
                "                      ^~^~^~^~^~^~^~^~^~^~^~^~^\"\".  \"^~^~^~^~^\n" +
                "                                            ^~^~^~^  ~^~\n" +
                "                                                 ^~^~^~");
        System.out.println("\nWelcome to ^DolFin^, your personal finance tracker\n");
        while (maxAttempts > passwordAttempts && !accessGranted) {
            System.out.println("Please enter your password(case sensitive). Or ask for a hint: \n");
            String userPasswordEntry = scanner.nextLine();
            if (userPasswordEntry.toLowerCase().contains("hint")) {
                System.out.println(passwordHint);
            } else if (!userPasswordEntry.equals(realPassword)) {
                passwordAttempts++;
                System.out.printf("Incorrect try again. %d attempts remain\n", maxAttempts - passwordAttempts);
            } else {
                System.out.println("Access Granted");
                accessGranted = true;
            }
        }
        if (!accessGranted) {
            System.out.println("Too many failed attempts. Locking down program now");
        }
        return accessGranted;
    }


    private static void runMainApplication(Scanner scanner) {
        mainLoop: while (true) {
            System.out.println("""
                    
                    ===DolFin===
                    Select an option:
                    1. Record a deposit
                    2. Record a Payment
                    3. Access your Ledger
                    4. Exit DolFin
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
                    ledgerLoop: while (true) {
                        System.out.println("""
                            
                            ===Ledger===
                            Select an option:
                            1. Show all Entries
                            2. Show only Deposits
                            3. Show only Payments
                            4. View Reports
                            5. Exit to Main Menu
                            """);
                        byte userLedgerOption = scanner.nextByte();
                        scanner.nextLine();
                        switch (userLedgerOption) {
                            case 1 -> {
                                showAllEntries();
                            }
                            case 2 -> {
                                showDeposits();
                            }
                            case 3 -> {
                                showPayments();
                            }
                            case 4 -> {
                                boolean reportsRunning = true;

                                reportLoop : while (reportsRunning) {
                                    System.out.println("""
                                        
                                        ===Reports===
                                        Select an Option to view by:
                                        1. Month to Date
                                        2. Previous Month
                                        3. Year to Date
                                        4. Previous Year
                                        5. Search by Vendor
                                        6. Custom Search
                                        7. Go Back to Ledger Menu
                                        """);
                                    byte userReportInput = scanner.nextByte();
                                    scanner.nextLine();

                                    switch (userReportInput) {
                                        case 1 -> {
                                            showMonthToDateReport();
                                            reportsRunning = false;
                                        }
                                        case 2 -> {
                                            showPreviousMonthReport();
                                            reportsRunning = false;
                                        }
                                        case 3 -> {
                                            showYTDReport();
                                            reportsRunning = false;
                                        }
                                        case 4 -> {
                                            showPreviousYearReport();
                                            reportsRunning = false;
                                        }
                                        case 5 -> {
                                            getSearchByVendor(scanner);
                                            reportsRunning = false;
                                        }
                                        case 6 -> { // custom search
                                            System.out.println("""
                                                    
                                                    ===Custom Search===
                                                    Select a Search Option
                                                    1. Search by Date Range
                                                    2. Search by Description
                                                    3. Search by Amount
                                                    4. Return to Report Menu
                                                    5. Return to Ledger Menu
                                                    6. Return to Main Menu
                                                    7. Exit Program
                                                    """);
                                            byte userCustomSearchChoice = scanner.nextByte();
                                            scanner.nextLine();
                                            boolean customSearchRunning = true;

                                            customSearchLoop : while (customSearchRunning) {
                                                ArrayList<Transaction> transactions = loadTransactions();
                                                switch (userCustomSearchChoice) {
                                                    case 1 -> {
                                                        searchByDate(scanner, transactions);
                                                        break customSearchLoop;
                                                    }
                                                    case 2 -> {
                                                        searchByDescription(scanner, transactions);
                                                        break customSearchLoop;
                                                    }
                                                    case 3 -> {
                                                        searchByAmount(scanner, transactions);
                                                        break customSearchLoop;
                                                    }
                                                    case 4 -> {
                                                        System.out.println("Returning to Report Menu");
                                                        break customSearchLoop;
                                                    }
                                                    case 5 -> {
                                                        System.out.println("Returning to Ledger Menu");
                                                        break reportLoop;
                                                    }
                                                    case 6 -> {
                                                        System.out.println("Returning to Main Menu");
                                                        break ledgerLoop;
                                                    }
                                                    case 7 -> {
                                                        System.out.println("Exiting DolFin. Thanks for using my App!");
                                                        break mainLoop;
                                                    }
                                                    default -> {
                                                        System.out.println("That's an invalid entry.");
                                                        customSearchRunning = false;
                                                    }
                                                }
                                            }

                                        }
                                        case 7 -> {
                                            break reportLoop;
                                        }
                                        default -> System.out.println("You didn't enter a proper value");
                                    }
                                }
                            }
                            case 5 -> {
                                System.out.println("Going back to DolFin Main Menu");
                                break ledgerLoop;
                            }
                            case 6 -> {
                                System.out.println("Thanks for using this awesome app! Have a great day and goodbye!");
                                break ledgerLoop;
                            }
                        }
                    }
                }
                case 4 -> {
                    System.out.println("Thanks for using this awesome app! Have a great day and goodbye!");
                    break mainLoop;
                }
                default -> {
                    System.out.println("Number is invalid or out of range");
                }
            }
        }
    }

    private static void searchByAmount(Scanner scanner, ArrayList<Transaction> transactions) {
        System.out.println("Enter Amount to search for: ");
        double searchAmount = scanner.nextDouble();
        scanner.nextLine();

        boolean transactionFound = false;
        int transactionCount = 0;

        for (Transaction item : transactions) {
            if (item.getAmount() == searchAmount) {
                System.out.println(item);
                transactionFound = true;
                transactionCount ++;
            }
        }
        if (transactionFound) {
            System.out.printf("\n%d transaction(s) found!\n", transactionCount);
        } else {
            System.out.println("\nNo transactions found for that amount.\n");
        }
    }

    private static void searchByDescription(Scanner scanner, ArrayList<Transaction> transactions) {
        System.out.println("Enter Description Keyword");
        String descriptionKeyWord = scanner.nextLine().toLowerCase();
        boolean transactionFound = false;
        int transactionCount = 0;

        for (Transaction item : transactions) {
            if (item.getDescription().toLowerCase().contains(descriptionKeyWord)) {
                System.out.println(item);
                transactionFound = true;
                transactionCount ++;
            }
        }
        if (transactionFound) {
            System.out.printf("\n%d transaction(s) found!\n", transactionCount);
        } else {
            System.out.println("\nNo transactions found for that description.\n");
        }
    }

    private static void searchByDate(Scanner scanner, ArrayList<Transaction> transactions) {
        System.out.println("Enter Start Date (yyyy-MM-dd): ");
        String startDateString = scanner.nextLine();
        LocalDate startDate = LocalDate.parse(startDateString);
        System.out.println("Enter End Date (yyyy-MM-dd): ");
        String endDateString = scanner.nextLine();
        LocalDate endDate = LocalDate.parse(endDateString);

        boolean transactionFound = false;
        int transactionCount = 0;

        for (Transaction item : transactions) {
            if (item.getDate().isAfter(startDate) && item.getDate().isBefore(endDate)) {
                System.out.println(item);
                transactionFound = true;
                transactionCount ++;
            }
        }
        if (transactionFound) {
            System.out.printf("\n%d transaction(s) found!\n", transactionCount);
        } else {
            System.out.println("\nNo transactions found for that date range.\n");
        }
    }

    private static void getSearchByVendor(Scanner scanner) {
        ArrayList<Transaction> transactions = loadTransactions();
        double transactionTotal = 0;
        int transactionCount = 0;
        boolean transactionFound = false;

        System.out.println("===Search By Vendor===\n");
        System.out.println("Enter vendor name: ");
        String userVendorName = scanner.nextLine().toLowerCase();
        for (Transaction item : transactions) {
            if (item.getVendor().toLowerCase().contains(userVendorName)) {
                System.out.println(item);
                transactionTotal += item.getAmount();
                transactionCount++;
                transactionFound = true;
            }
        }
        if (transactionFound) {
            System.out.printf("\n%d transactions found for vendor: %s \nTotal of transactions: $%.2f\n", transactionCount, userVendorName, transactionTotal);
        } else {
            System.out.printf("\nNo transactions found for vendor: %s\n", userVendorName);
        }
    }

    private static void showPreviousYearReport() {
        ArrayList<Transaction> transactions = loadTransactions();

        LocalDate now = LocalDate.now();
        int previousYear = now.getYear() - 1;
        LocalDate startOfPreviousYear = LocalDate.ofYearDay(previousYear, 1);
        LocalDate endOfPreviousYear = LocalDate.of(previousYear, 12, 31);

        boolean transactionFound = false;
        double transactionTotal = 0;
        int transactionCount = 0;

        System.out.printf("===Report for %d===\n",previousYear);
        for (Transaction item : transactions) {
            if (item.getDate().isAfter(startOfPreviousYear) && item.getDate().isBefore(endOfPreviousYear)) {
                System.out.println(item);
                transactionTotal += item.getAmount();
                transactionCount ++;
                transactionFound = true;
            }
        }
        if (transactionFound) {
            System.out.printf("\n%d transactions found from %s to %s\n", transactionCount, startOfPreviousYear, endOfPreviousYear);
            System.out.printf("Transaction sum during this time period: $%.2f\n", transactionTotal);
        } else {
            System.out.printf("\nNo transactions found from %s - %s\n", startOfPreviousYear,endOfPreviousYear);
        }
    }

    private static void showYTDReport() {
        ArrayList<Transaction> transactions = loadTransactions();
        LocalDate now = LocalDate.now();
        LocalDate startOfYear = now.withDayOfYear(1);
        double transactionTotal = 0;
        int transactionCount = 0;
        boolean transactionFound = false;

        System.out.printf("===Report for %s to %s===",startOfYear,now);
        for (Transaction item : transactions) {
            if (item.getDate().isAfter(startOfYear)) {
                System.out.println(item);
                transactionTotal += item.getAmount();
                transactionCount ++;
                transactionFound = true;
            }
        }
        if (transactionFound) {
            System.out.printf("\n%d transactions found from %s to %s\n", transactionCount, startOfYear, now);
            System.out.printf("Transaction sum during this time period: $%.2f\n", transactionTotal);
        } else {
            System.out.printf("\nNo transactions found from %s - %s\n", startOfYear, now);
        }
    }

    private static void showPreviousMonthReport() {
        ArrayList<Transaction> transactions = loadTransactions();
        LocalDate now = LocalDate.now();
        LocalDate startOfPreviousMonth = now.minusMonths(1).withDayOfMonth(1); //finds the first day of current month minus 1
        LocalDate endOfPreviousMonth = now.withDayOfMonth(1).minusDays(1); // finds the last day by subtracting current day by 1
        boolean transactionFound = false;
        int transactionCount = 0;
        double tranactionTotal = 0;

        System.out.printf("===Report for %s to %s===\n",startOfPreviousMonth,endOfPreviousMonth);
        for (Transaction item : transactions) {
            if (item.getDate().isAfter(startOfPreviousMonth) && item.getDate().isBefore(endOfPreviousMonth)) {
                System.out.println(item);
                transactionFound = true;
                transactionCount ++;
                tranactionTotal += item.getAmount();
            }
        }
        if (transactionFound) {
            System.out.printf("\n%d transactions found from %s - %s\n", transactionCount, startOfPreviousMonth, endOfPreviousMonth);
            System.out.printf("Transaction sum during this time period: $%.2f", tranactionTotal);
        } else {
            System.out.printf("\nNo transactions from from %s - %s\n",startOfPreviousMonth,endOfPreviousMonth);
        }
    }

    private static void showMonthToDateReport() {
        ArrayList<Transaction> transactions = loadTransactions();
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1); //finds the first day of the current month
        boolean transactionFound = false;
        int transactionCount = 0;
        double transactionTotal = 0;

        System.out.printf("===Report for %s to %s\n", startOfMonth, now);
        for (Transaction item : transactions) {
            LocalDate transactionDate = item.getDate();
            if (transactionDate.isAfter(startOfMonth)) {
                System.out.println(item);
                transactionFound = true;
                transactionCount ++;
                transactionTotal += item.getAmount();
            }
        }
        if (transactionFound) {
            System.out.printf("\n%d transactions found from %s - %s\n", transactionCount, startOfMonth, now);
            System.out.printf("Transaction sum during this time period: $%.2f\n",transactionTotal);
        } else {
            System.out.printf("\nNo transactions from from %s - %s\n",startOfMonth,now);
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
        System.out.printf("\nYou have %d payments totaling $%.2f\n\n",paymentLength,paymentSum);
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
        System.out.printf("\nYou have %d deposits totaling $%.2f.\n\n",depositLength, depositSum);
    }

    private static void showAllEntries() {
        int entriesLength = 0;
        double entriesSum = 0;
        ArrayList<Transaction> transactions = loadTransactions();

        if (transactions.isEmpty()) {
            System.out.println("\nNo transactions found.");
            return;
        }

        System.out.println("\n===All Entries===");
        for (Transaction item : transactions) {
            entriesLength ++;
            entriesSum += item.getAmount();
            System.out.println(item);
        }
        System.out.printf("\nYou have %d entries in total, with a net balance of $%.2f\n",entriesLength,entriesSum);
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
            System.out.println("How much would you like to send as a Payment?");
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

        System.out.printf("$%.2f has been saved as a deposit to your account to %s for %s\n", userDepositAmount, userDepositVendor, userDepositDescription);
    }

    private static void saveTransaction(Transaction transaction) {
        try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            buffWriter.write("\n"+ transaction.toCSV());
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