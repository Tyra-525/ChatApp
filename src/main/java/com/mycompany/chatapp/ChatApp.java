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

while (running){
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
//register
    
   
    public static void register(Scanner input){
     System.out.println("\n********************");
     System.out.println("Account Registration");
     System.out.println("\n********************");
     
     Login obj = new Login();
     boolean valid = false;
     
     while(!valid){
      System.out.print("Please enter your name:");
      valid = obj.name(input.nextLine());
     }
     valid  = false;
     while(!valid){
      System.out.print("Please enter your username (minimum 4 cahracters,must include an underscore):");
      valid = obj.checkUsername(input.nextLine());
     }
     
     valid  = false;
     while(!valid){
      System.out.print("Please enter your password (8+ chars, 1 number, 1 capital, 1 special char):");
       valid = obj.checkPassword(input.nextLine());
 }
     valid  = false;
     while(!valid){
      System.out.print("Please enter your number (e.g. +27821234567):):");
      valid = obj.checkCellPhoneNumber(input.nextLine());
     }
     
     registeredAccount= obj;

       System.out.println("\nRegistration successful! Please login.\n");
       login(input);
    }
// Login 
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
           System.out.println("\nLogin successful! Welcome back to ChatAPP " + registeredAccount.getName() + ".\n");
            menu(input);   // start the QuickChat menu
             return;
        }

        attempts++;
        int remaining = maxAttempts - attempts;

        if (remaining > 0) {
            System.out.println("Incorrect username or password. You have " + remaining + " attempt(s) left.\n");
        } else {
            System.out.println("\nYou've tried too many times. Please try again later.");
           
        }
    }
    }
   public static void menu(Scanner input) {
        System.out.println("Welcome to QuickChat.");
        int limit = readInt(input, "How many messages do you wish to enter? ", 1, Integer.MAX_VALUE);
        int entered = 0;

        while (true) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            int option = readInt(input, "Choose an option: ", 1, 3);

            if (option == 1) {
                if (entered >= limit) {
                    System.out.println("You have already entered all " + limit + " messages.");
                    continue;
                }
                sendOne(input);
                entered++;
                if (entered == limit) {
                    System.out.println("Total messages sent: " + Message.returnTotalMessages());
                }
            } else if (option == 2) {
                System.out.println("Coming Soon.");
            } else {
                System.out.println("Goodbye!");
                return;
            }
        }
    }
    public static void sendOne(Scanner input) {
        // ----- Recipient -----
        String recipient = "";
        boolean validCell = false;

        while (!validCell) {
            System.out.print("Recipient cell number (e.g. +27718693002): ");
            recipient = input.nextLine().trim();

            if (Message.isValidCell(recipient)) {
                validCell = true;
            } else {
                System.out.println("Cell phone number is incorrectly formatted or does not contain an "
                        + "international code. Please correct the number and try again.");
            }
        }
        System.out.println("Cell phone number successfully captured.");

        // ----- Message text -----
        String text = "";
        boolean validText = false;

        while (!validText) {
            System.out.print("Message (max 250 characters): ");
            text = input.nextLine();

            if (text.length() <= 250) {
                validText = true;
                System.out.println("Message sent");
            } else {
                System.out.println("Please enter a message of less than 250 characters.");
            }
        }

        // ----- Create the message and choose what to do with it -----
        Message message = new Message(recipient, text);
        System.out.println(message.getMessageIDConfirmation());

        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        int choice = readInt(input, "Choose an option: ", 1, 3);

        System.out.println(message.sentMessage(choice));
        if (choice == 1) {
            System.out.println(message.getDetails());
        } else if (choice == 2) {
            String answer = input.nextLine().trim();
            System.out.println(answer.equals("0") ? "Message deleted." : "Message not deleted.");
        }
    }
     public static int readInt(Scanner input, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(input.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // fall through to the error message
            }
            System.out.println("Please enter a valid number.");
        }
    }
   }
   
    

