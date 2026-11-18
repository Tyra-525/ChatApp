package com.mycompany.chatapp;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Messages {

    public static final int Max_Length = 250;

    // Stores all successfully sent messages as one long String
    public static String allSentMessages = "";

    // Running total of messages that were actually sent
    public static int totalMessagesSent = 0;

    // Running total of messages that were processed (sent OR stored)
    // This is what goes into the message hash so stored messages also get a proper number
    public static int totalMessagesProcessed = 0;

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

    // Check that the Message ID is exactly 10 characters
    public boolean checkMessageID() {
        if (messageID == null || messageID.length() != 10) {
            System.out.println("Message ID must be exactly 10 characters.");
            return false;
        }
        return true;
    }

    // Validate recipient cell number
    public String checkRecipientCell(String cell) {
        if (cell == null || cell.trim().isEmpty() || !cell.matches("\\+27\\d{9}")) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        this.ReceipientCell = cell.trim();
        return "Cell phone number successfully captured.";
    }

    // Validate message length
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

    // Create the message hash (punctuation is stripped)
    public String createMessageHash() {
        if (messageID == null || SentMessage == null || SentMessage.trim().isEmpty()) {
            return "00:0:EMPTY";
        }

        String firstTwo = messageID.substring(0, 2);
        String message = SentMessage.trim();

        // First word (letters only)
        String firstWord;
        int firstSpace = message.indexOf(" ");
        if (firstSpace == -1) {
            firstWord = message.toUpperCase().replaceAll("[^A-Z]", "");
        } else {
            firstWord = message.substring(0, firstSpace).toUpperCase().replaceAll("[^A-Z]", "");
        }

        // Last word (letters only)
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

    // Handle Send / Disregard / Store
    public String SentMessage(Scanner input) {
        System.out.println("\nPlease choose an option:");
        System.out.println("1. Send Message");
        System.out.println("2. Press 0 to disregard message");
        System.out.println("3. Store Message to send later");
        System.out.print("Enter choice: ");

        String choice = input.nextLine().trim();

        if (choice.equals("1")) {
            // This message is being processed → give it the next number
            totalMessagesProcessed++;
            amntOfMessage = totalMessagesProcessed;
            createMessageHash();

            totalMessagesSent++;   // only count actual sends for the final total

            String details = "Message ID: " + messageID
                           + "\nMessage Hash: " + MessageHash
                           + "\nRecipient: " + ReceipientCell
                           + "\nMessage: " + SentMessage;

            allSentMessages = allSentMessages
                    + "\n--- Message " + totalMessagesSent + " ---\n"
                    + details + "\n";

            return "Message successfully sent.";
        }
        else if (choice.equals("2") || choice.equals("0")) {
            return "Message disregarded";
        }
        else if (choice.equals("3")) {
            // Stored messages also receive a proper sequential number
            totalMessagesProcessed++;
            amntOfMessage = totalMessagesProcessed;
            createMessageHash();
            storeMessage();
            return "Message successfully stored.";
        }
        else {
            return "Invalid option. Message not sent.";
        }
    }

    public String printMessages() {
        if (allSentMessages.equals("")) {
            return "No messages have been sent yet.";
        }
        return "\n===== ALL SENT MESSAGES =====\n"
             + allSentMessages
             + "=============================\n";
    }

    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    /**
     * Generate a 10-digit Message ID by building it digit-by-digit
     * (as required by the feedback).
     */
    public void generateMessageID() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        this.messageID = id.toString();
    }

    /**
     * Store the message in a proper JSON array in storedMessages.json
     */
    public void storeMessage() {
        try {
            File file = new File("storedMessages.json");
            String existingContent = "";

            if (file.exists() && file.length() > 0) {
                existingContent = new String(Files.readAllBytes(Paths.get("storedMessages.json"))).trim();
            }

            // Build the new message object
            String newObject = "{"
                    + "\"messageID\":\"" + messageID + "\","
                    + "\"messageHash\":\"" + MessageHash + "\","
                    + "\"recipient\":\"" + ReceipientCell + "\","
                    + "\"message\":\"" + SentMessage.replace("\"", "\\\"") + "\""
                    + "}";

            String updatedJson;

            if (existingContent.isEmpty()) {
                // First message → create a new array
                updatedJson = "[\n  " + newObject + "\n]";
            } else {
                // Append to existing array
                // Remove the closing ]
                if (existingContent.endsWith("]")) {
                    existingContent = existingContent.substring(0, existingContent.length() - 1).trim();
                }
                // If the array already has content, add a comma
                if (!existingContent.equals("[")) {
                    existingContent = existingContent + ",";
                }
                updatedJson = existingContent + "\n  " + newObject + "\n]";
            }

            // Overwrite the file with the complete valid JSON array
            try (FileWriter writer = new FileWriter("storedMessages.json", false)) {
                writer.write(updatedJson);
            }

            System.out.println("Message saved to storedMessages.json");
        }
        catch (IOException e) {
            System.out.println("Error while storing message: " + e.getMessage());
        }
    }
}