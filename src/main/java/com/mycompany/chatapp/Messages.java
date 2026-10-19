/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
/**
 *
 * @author Student
 */
public class Messages {

    public static final int Max_Length = 250;

    // Stores all successfully sent messages as one long String
    public static String allSentMessages = "";

    // Running total of messages that were actually sent
    public static int totalMessagesSent = 0;

    // Instance fields
    public String messageID;
    public String ReceipientCell;
    public String MessageHash;
    public String SentMessage;
    public String PrintMessage;
    public int amntOfMessage;
    public String storeMessage;

    // Constructor
    public Messages() {}

    // Getters
    public String getID() { return messageID; }
    public String getReceipientCell() { return ReceipientCell; }
    public String getMessageHash() { return MessageHash; }
    public String getMessage() { return SentMessage; }
    public String getPrintMessage() { return PrintMessage; }
    public int getamntOfMessage() { return amntOfMessage; }
    public String getstoreMessage() { return storeMessage; }

    //method for message ID
    public boolean checkMessageID() {
        if (messageID == null || messageID.length() != 10) {
            System.out.println("Message ID must be exactly 10 characters.");
            return false;
        }
        return true;
    }
    
  
    // Method for recipient Cell
    public String checkRecipientCell(String cell) {
        if (cell == null || cell.trim().isEmpty() || !cell.matches("\\+27\\d{9}")) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        this.ReceipientCell = cell.trim();
        return "Cell phone number successfully captured.";
    }
    //method for message length
      public String checkMessageLength(String message) {
    if (message == null) {
        return "Message exceeds 250 characters by 0; please reduce the size.";
    }
    if (message.length() > Max_Length) {
        int over = message.length() - Max_Length;
        return "Message exceeds 250 characters by " + over + "; please reduce the size.";
    }
    this.SentMessage = message;
    return "Message ready to send.";
}

    //method for message hash 
    public String createMessageHash() {
        if (messageID == null || SentMessage == null || SentMessage.trim().isEmpty()) {
            return "00:0:EMPTY";
        }

        String firstTwo = messageID.substring(0, 2);
        String message = SentMessage.trim();

        // Get the first word
        String firstWord;
        int firstSpace = message.indexOf(" ");
        if (firstSpace == -1) {
            firstWord = message.toUpperCase().replaceAll("[^A-Z]", "");
        } else {
            firstWord = message.substring(0, firstSpace).toUpperCase().replaceAll("[^A-Z]", "");
        }

        // Get the last word
        String lastWord;
        int lastSpace = message.lastIndexOf(" ");
        if (lastSpace == -1) {
            lastWord = firstWord;
        } else {
            lastWord = message.substring(lastSpace + 1).toUpperCase().replaceAll("[^A-Z]", "");
        }

        MessageHash = firstTwo + ":" + amntOfMessage + ":" + firstWord + lastWord;
        return MessageHash;
    }

    //method for sent message
    public String SentMessage(Scanner input) {
        System.out.println("\nPlease choose an option:");
        System.out.println("1. Send Message");
        System.out.println("2. Press 0 to disregard message");
        System.out.println("3. Store Message to send later");
        System.out.print("Enter choice: ");

        String choice = input.nextLine().trim();

        if (choice.equals("1")) {
            totalMessagesSent++;
            amntOfMessage = totalMessagesSent;
            createMessageHash();

            String details = "Message ID: " + messageID
                           + "\nMessage Hash: " + MessageHash
                           + "\nRecipient: " + ReceipientCell
                           + "\nMessage: " + SentMessage;

            // Add this message to the big String
            allSentMessages = allSentMessages
                    + "\n--- Message " + totalMessagesSent + " ---\n"
                    + details + "\n";

            return "Message successfully sent.";
        }
        else if (choice.equals("2") || choice.equals("0")) {
            return "Message disregarded";
        }
        else if (choice.equals("3")) {
            createMessageHash();
            storeMessage();
            return "Message successfully stored.";
        }
        else {
            return "Invalid option. Message not sent.";
        }
    }

    //print message method 
    public String printMessages() {
        if (allSentMessages.equals("")) {
            return "No messages have been sent yet.";
        }

        return "\n===== ALL SENT MESSAGES =====\n"
             + allSentMessages
             + "=============================\n";
    }

    // return total messages
    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    //  generate a random 10-digit Message ID
    public void generateMessageID() {
        long id = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
        this.messageID = String.valueOf(id);
    }

    // json store message 
    public void storeMessage() {
        try {
            String jsonLine = "{"
                    + "\"messageID\":\"" + messageID + "\","
                    + "\"messageHash\":\"" + MessageHash + "\","
                    + "\"recipient\":\"" + ReceipientCell + "\","
                    + "\"message\":\"" + SentMessage.replace("\"", "\\\"") + "\""
                    + "}";

            java.io.FileWriter writer = new java.io.FileWriter("storedMessages.json", true);
            writer.write(jsonLine + "\n");
            writer.close();

            System.out.println("Message saved to storedMessages.json");
        }
        catch (Exception e) {
            System.out.println("Error while storing message: " + e.getMessage());
        }
    }
}