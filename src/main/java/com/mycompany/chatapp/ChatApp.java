package com.mycompany.chatapp;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * ChatApp: registration, login and the QuickChat messaging menu.
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
            System.out.println("1. Register\n2. Login\n3. Exit");
            System.out.print("Choose an option: ");

            String option = input.nextLine().trim();

            if (option.equals("1")) {
                register(input);
            } else if (option.equals("2")) {
                login(input);
            } else if (option.equals("3")) {
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
        System.out.println("********************");

        Login obj = new Login();
        boolean valid = false;

        while (!valid) {
            System.out.print("Please enter your name: ");
            valid = obj.name(input.nextLine());
        }

        valid = false;
        while (!valid) {
            System.out.print("Please enter your username (minimum 4 characters, must include an underscore): ");
            valid = obj.checkUsername(input.nextLine());
        }

        valid = false;
        while (!valid) {
            System.out.print("Please enter your password (8+ chars, 1 number, 1 capital, 1 special char): ");
            valid = obj.checkPassword(input.nextLine());
        }

        valid = false;
        while (!valid) {
            System.out.print("Please enter your number (e.g. +27821234567): ");
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
                System.out.println("\nLogin successful! Welcome, " + registeredAccount.getName() + ".\n");
                quickChat(input);
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

    // ==================== QUICKCHAT ====================
    public static void quickChat(Scanner input) {
        System.out.println("Welcome to QuickChat.");

        // Read any previously stored messages from the JSON file into the arrays
        Messages.loadStoredMessages();

        int maxMessages = readPositiveNumber(input);
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n===== QUICKCHAT MENU =====");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");

            String choice = input.nextLine().trim();

            if (choice.equals("1")) {
                sendMessages(input, maxMessages);
            } else if (choice.equals("2")) {
                System.out.println(Messages.sentReport());
            } else if (choice.equals("3")) {
                Messages summary = new Messages();
                System.out.println("\nTotal number of messages sent: " + summary.returnTotalMessages());
                System.out.println(summary.printMessages());
                System.out.println("Thank you for using QuickChat. Goodbye!");
                inMenu = false;
            } else if (choice.equals("4")) {
                storedMessagesMenu(input);
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /** Keeps asking until the user enters a whole number greater than 0. */
    private static int readPositiveNumber(Scanner input) {
        while (true) {
            System.out.print("How many messages would you like to enter? ");
            try {
                int number = Integer.parseInt(input.nextLine().trim());
                if (number > 0) {
                    return number;
                }
                System.out.println("You must enter a number greater than 0. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a whole number greater than 0.");
            }
        }
    }

    private static void sendMessages(Scanner input, int maxMessages) {
        for (int i = 1; i <= maxMessages; i++) {
            System.out.println("\n----- Entering Message " + i + " of " + maxMessages + " -----");

            Messages msg = new Messages();
            msg.generateMessageID();

            String cellResult;
            do {
                System.out.print("Enter recipient cell number (e.g. +27718693002): ");
                cellResult = msg.checkRecipientCell(input.nextLine());
                System.out.println(cellResult);
            } while (!cellResult.equals("Cell phone number successfully captured."));

            String lengthResult;
            do {
                System.out.print("Enter your message (max 250 characters): ");
                lengthResult = msg.checkMessageLength(input.nextLine());
                System.out.println(lengthResult);
            } while (!lengthResult.equals("Message ready to send."));

            String result = msg.sendMessageOption(input);
            System.out.println(result);

            if (result.equals("Message successfully sent.")) {
                printMessageDetails(msg);
            }
        }
    }

    private static void printMessageDetails(Messages msg) {
        System.out.println("\n----- MESSAGE DETAILS -----");
        System.out.println("Message ID   : " + msg.getID());
        System.out.println("Message Hash : " + msg.getMessageHash());
        System.out.println("Recipient    : " + msg.getRecipientCell());
        System.out.println("Message      : " + msg.getMessage());
        System.out.println("---------------------------");
    }

    // ==================== STORED MESSAGES ====================
    private static void storedMessagesMenu(Scanner input) {
        boolean inStoredMenu = true;

        while (inStoredMenu) {
            System.out.println("\n===== STORED MESSAGES =====");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages to a recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display stored messages report");
            System.out.println("7) Back to QuickChat menu");
            System.out.print("Choose an option: ");

            String choice = input.nextLine().trim();

            if (choice.equals("1")) {
                System.out.println(Messages.displayStoredSenderAndRecipient(registeredAccount.getName()));
            } else if (choice.equals("2")) {
                System.out.println("Longest stored message: " + Messages.longestStoredMessage());
            } else if (choice.equals("3")) {
                System.out.print("Enter the message ID: ");
                System.out.println(Messages.searchByMessageID(input.nextLine().trim()));
            } else if (choice.equals("4")) {
                System.out.print("Enter the recipient cell number: ");
                printRecipientMessages(Messages.searchByRecipient(input.nextLine().trim()));
            } else if (choice.equals("5")) {
                System.out.print("Enter the message hash: ");
                System.out.println(Messages.deleteByHash(input.nextLine().trim()));
            } else if (choice.equals("6")) {
                System.out.println(Messages.storedReport());
            } else if (choice.equals("7")) {
                inStoredMenu = false;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void printRecipientMessages(ArrayList<String> messages) {
        if (messages.isEmpty()) {
            System.out.println("No messages found for that recipient.");
            return;
        }
        for (int i = 0; i < messages.size(); i++) {
            System.out.println((i + 1) + ". " + messages.get(i));
        }
    }
}