package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class ScratchPaper {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        BufferedReader buffReader = new BufferedReader(new FileReader("transactions.csv"));

        System.out.println("Welcome to the Ledger App");

        while (running) {
            System.out.println("""
                    Select an option:
                    1. Add a deposit
                    2. Make a Payment
                    3. Access your Ledger
                    4. Exit Application
                    """);

            byte userChoice = (scanner.nextByte());
            switch (userChoice) {
                case 1 -> {
                    try {
                        BufferedWriter buffWriter = new BufferedWriter(new FileWriter("transactions.csv", true));
                        System.out.println("How much would you like to deposit?");
                        double userDepositAmount = 0;
                        Transaction transaction = null;
                        try {
                            userDepositAmount = scanner.nextDouble();
                            transaction = new Transaction(LocalDate.now(),
                                    LocalTime.now(),
                                    "Deposit",
                                    "User",
                                    userDepositAmount
                            );
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        buffWriter.write(transaction.toString());
                        buffWriter.newLine();
                        buffWriter.flush();
                        System.out.printf("$%.2f has been added to your account\n",userDepositAmount);

                        buffWriter.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 2 -> {
                    System.out.println("enter");
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
                            while ((line = buffReader.readLine())!= null) {
                                System.out.println(line.toString());
                            }
                        }
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
}
