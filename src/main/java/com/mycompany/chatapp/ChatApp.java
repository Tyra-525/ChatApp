/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatapp;
import java.util.Scanner;

/**
 *
 * @author Student
 */

public class ChatApp {

    static Login registeredAccount;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("******************************");
            System.out.println("Welcome to ChatApp");
            System.out.println("******************************");
            System.out.println("Please select an option below.");
            System.out.println("1.Register\n"
                             + "2.Login\n"
                             + "3.Exit");
            int option = input.nextInt();
            input.nextLine(); // consume leftover newline

            if (option == 1) {
                register(input);
            } else if (option == 2) {
                login(input);
            } else if (option == 3) {
                System.out.println("\nGoodbye!");
                running = false;
            } else {
                System.out.println("\nInvalid option. Please try again.\n");
            }
        }
    }

    // ==================== REGISTER ====================
    public static void register(Scanner input) {
        System.out.println("\n********************");
        System.out.println("Account Registration");
        System.out.println("\n********************");

        Login obj = new Login();
        boolean valid = false;

        while (!valid) {
            System.out.print("Please enter your name:");
            valid = obj.name(input.nextLine());
        }
        valid = false;
        while (!valid) {
            System.out.print("Please enter your username (minimum 4 characters, must include an underscore):");
            valid = obj.checkUsername(input.nextLine());
        }

        valid = false;
        while (!valid) {
            System.out.print("Please enter your password (8+ chars, 1 number, 1 capital, 1 special char):");
            valid = obj.checkPassword(input.nextLine());
        }
        valid = false;
        while (!valid) {
            System.out.print("Please enter your number (e.g. +27821234567):");
            valid = obj.checkCellPhoneNumber(input.nextLine());
        }

        registeredAccount = obj;
        System.out.println("\nRegistration successful! Please login.\n");
        login(input);
    }

    // ==================== LOGIN ====================
    public static void login(Scanner input) {
        System.out.println("\n******************************");
        System.out.println("            Login");
        System.out.println("******************************");

        if (registeredAccount == null) {
            System.out.println("No account found. You need to register first.");
            System.out.print("Would you like to register now? (yes/no): ");
            String choice = input.nextLine().trim().toLowerCase();
            if (choice.equals("yes") || choice.equals("y")) {
                register(input);
            } else {
                System.out.println("Returning to main menu.\n");
            }
            return;
        }

        int attempts = 0;
        int maxAttempts = 3;

        while (attempts < maxAttempts) {
            System.out.print("Enter your username: ");
            String username = input.nextLine().trim();
            System.out.print("Enter your password: ");
            String password = input.nextLine();

            if (registeredAccount.getUsername().equals(username)
                    && registeredAccount.getPassword().equals(password)) {

                System.out.println("\nLogin successful! Welcome back to ChatAPP "
                                   + registeredAccount.getName() + ".\n");

                // Start Part 2 – QuickChat
                QuickChat(input);
                return;
            }

            attempts++;
            int remaining = maxAttempts - attempts;
            if (remaining > 0) {
                System.out.println("Incorrect username or password. You have "
                                   + remaining + " attempt(s) left.\n");
            } else {
                System.out.println("\nYou've tried too many times. Please try again later.");
            }
        }
    }

    // ==================== Part 2 ====================
   public static void QuickChat(Scanner input) {

    System.out.println("Welcome to QuickChat.");

    // Ask how many messages the user wants to send
    System.out.print("How many messages would you like to enter? ");
    int maxMessages;
    try {
        maxMessages = Integer.parseInt(input.nextLine().trim());
        if (maxMessages <= 0) {
            System.out.println("You must enter at least 1 message.");
            return;
        }
    } catch (NumberFormatException e) {
        System.out.println("Invalid number.");
        return;
    }

    boolean inMenu = true;

    while (inMenu) {   // WHILE LOOP – required by rubric
        System.out.println("\n===== QUICKCHAT MENU =====");
        System.out.println("1) Send Messages");
        System.out.println("2) Show recently sent messages");
        System.out.println("3) Quit");
        System.out.print("Choose an option: ");

        String choice = input.nextLine().trim();

        if (choice.equals("1")) {

            // FOR LOOP – required by rubric (uses loop counter)
            for (int i = 1; i <= maxMessages; i++) {
                System.out.println("\n----- Entering Message " + i + " of " + maxMessages + " -----");

                Messages msg = new Messages();
                msg.generateMessageID();
                msg.checkMessageID();

                // Recipient
                String cellResult;
                do {
                    System.out.print("Enter recipient cell number (e.g. +27718693002): ");
                    cellResult = msg.checkRecipientCell(input.nextLine());
                    System.out.println(cellResult);
                } while (!cellResult.equals("Cell phone number successfully captured."));

                // Message text
                String text;
                String lengthResult;
                do {
                    System.out.print("Enter your message (max 250 characters): ");
                    text = input.nextLine();
                    lengthResult = msg.checkMessageLength(text);
                    System.out.println(lengthResult);
                } while (!lengthResult.equals("Message ready to send."));

                // Send / Disregard / Store
                String result = msg.SentMessage(input);
                System.out.println(result);

                if (result.equals("Message successfully sent.")) {
                    // Display full details (required by rubric)
                    System.out.println("\n----- MESSAGE DETAILS -----");
                    System.out.println("Message ID   : " + msg.getID());
                    System.out.println("Message Hash : " + msg.getMessageHash());
                    System.out.println("Recipient    : " + msg.getReceipientCell());
                    System.out.println("Message      : " + msg.getMessage());
                    System.out.println("---------------------------");
                }
            }

        }
        else if (choice.equals("2")) {
            System.out.println("Coming Soon.");
        }
        else if (choice.equals("3") || choice.equalsIgnoreCase("quit")) {
            Messages dummy = new Messages();
            System.out.println("\nTotal number of messages sent: " + dummy.returnTotalMessages());
            System.out.println(dummy.printMessages());
            System.out.println("Thank you for using QuickChat. Goodbye!");
            inMenu = false;
        }
        else {
            System.out.println("Invalid option. Please try again.");
        }
    }
   }
}