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

    // Every successfully sent message is stored here
    public static List<String> sentMessages = new ArrayList<>();

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

    // ------------------------------------------------------------------
    // 1. checkMessageID()
    // ------------------------------------------------------------------
    public boolean checkMessageID() {
        if (messageID == null || messageID.length() != 10) {
            System.out.println("Message ID must be exactly 10 characters.");
            return false;
        }
        return true;
    }

    // ------------------------------------------------------------------
    // 2. checkRecipientCell()
    // ------------------------------------------------------------------
    public String checkRecipientCell(String cell) {
        if (cell == null || cell.trim().isEmpty()) {
            return "Recipient cell number cannot be empty.";
        }
        cell = cell.trim();

        if (!cell.matches("\\+27\\d{9}")) {
            return "Invalid recipient number. Use format +27 followed by 9 digits "
                 + "(example: +27821234567).";
        }

        this.ReceipientCell = cell;
        return "Cell number successfully captured.";
    }

    // ------------------------------------------------------------------
    // 3. createMessageHash()
    // ------------------------------------------------------------------
    public String createMessageHash() {
        if (messageID == null || SentMessage == null || SentMessage.trim().isEmpty()) {
            return "00:0:EMPTY";
        }

        String firstTwo = messageID.substring(0, 2);
        String[] words = SentMessage.trim().split("\\s+");
        String firstWord = words[0].toUpperCase().replaceAll("[^A-Z]", "");
        String lastWord  = words[words.length - 1].toUpperCase().replaceAll("[^A-Z]", "");

        MessageHash = firstTwo + ":" + amntOfMessage + ":" + firstWord + lastWord;
        return MessageHash;
    }

    // ------------------------------------------------------------------
    // 4. SentMessage()
    // ------------------------------------------------------------------
    public String SentMessage(Scanner input) {
        System.out.println("\nPlease choose an option:");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
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
            sentMessages.add(details);

            return "Message successfully sent.";
        }
        else if (choice.equals("2") || choice.equals("0")) {
            return "Press 0 to delete the message – Message disregarded.";
        }
        else if (choice.equals("3")) {
            createMessageHash();
            storeMessage();                     // ← simple JSON version
            return "Message successfully stored.";
        }
        else {
            return "Invalid option. Message not sent.";
        }
    }

    // ------------------------------------------------------------------
    // 5. printMessages()
    // ------------------------------------------------------------------
    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== ALL SENT MESSAGES =====\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            sb.append("\n--- Message ").append(i + 1).append(" ---\n");
            sb.append(sentMessages.get(i)).append("\n");
        }
        sb.append("=============================\n");
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // 6. returnTotalMessages()
    // ------------------------------------------------------------------
    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    // ------------------------------------------------------------------
    // Helper: generate a random 10-digit Message ID
    // ------------------------------------------------------------------
    public void generateMessageID() {
        long id = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
        this.messageID = String.valueOf(id);
    }

    // ------------------------------------------------------------------
    // 7. storeMessage() – SIMPLE JSON version
    //    Each message is saved as one line in storedMessages.json
    // ------------------------------------------------------------------
    public void storeMessage() {
        try {
            // Build one JSON object (single line)
            String jsonLine = "{"
                    + "\"messageID\":\"" + messageID + "\","
                    + "\"messageHash\":\"" + MessageHash + "\","
                    + "\"recipient\":\"" + ReceipientCell + "\","
                    + "\"message\":\"" + SentMessage.replace("\"", "\\\"") + "\""
                    + "}";

            // Append to the file (creates the file if it does not exist)
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