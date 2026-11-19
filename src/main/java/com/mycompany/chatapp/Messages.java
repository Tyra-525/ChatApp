package com.mycompany.chatapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents one QuickChat message and holds the arrays that store every message.
 *
 * The first five static lists are PARALLEL arrays: index i in each list describes
 * the same message. They contain every message that was processed (sent,
 * disregarded or stored). The three category lists hold only the message text
 * for each flag.
 *
 * Referencing (replace with your institution's referencing format):
 * Oracle (n.d.) Pattern and Matcher, Java Platform SE API documentation. Available at:
 * https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/regex/Pattern.html
 * (Accessed: 19 November 2026).
 * Oracle (n.d.) Files.readString and Files.writeString, Java Platform SE API documentation.
 * Available at: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/file/Files.html
 * (Accessed: 19 November 2026).
 * Regular expressions are used below to read the stored messages JSON file into the arrays.
 */
public class Messages {

    public static final int MAX_LENGTH = 250;

    // Flags for each message
    public static final String SENT = "Sent";
    public static final String DISREGARD = "Disregard";
    public static final String STORED = "Stored";

    // File used to persist stored messages (tests point this at a temporary file)
    public static String storeFile = "storedMessages.json";

    // Parallel arrays: one entry per message, same index = same message
    public static ArrayList<String> messageIDs = new ArrayList<>();
    public static ArrayList<String> messageHashes = new ArrayList<>();
    public static ArrayList<String> recipients = new ArrayList<>();
    public static ArrayList<String> messageTexts = new ArrayList<>();
    public static ArrayList<String> flags = new ArrayList<>();

    // Arrays required by the task: message text for each category
    public static ArrayList<String> sentMessages = new ArrayList<>();
    public static ArrayList<String> disregardedMessages = new ArrayList<>();
    public static ArrayList<String> storedMessages = new ArrayList<>();

    private static int messageCounter = 0;

    // Fields for one message
    private String messageID;
    private String recipientCell;
    private String messageHash;
    private String messageText;
    private int messageNumber;

    // ==================== GETTERS AND SETTERS ====================

    public String getID() { return messageID; }
    public String getRecipientCell() { return recipientCell; }
    public String getMessageHash() { return messageHash; }
    public String getMessage() { return messageText; }
    public int getMessageNumber() { return messageNumber; }

    public void setMessageID(String messageID) { this.messageID = messageID; }
    public void setRecipientCell(String recipientCell) { this.recipientCell = recipientCell; }
    public void setMessageText(String messageText) { this.messageText = messageText; }
    public void setMessageNumber(int messageNumber) { this.messageNumber = messageNumber; }

    // ==================== VALIDATION ====================

    /** A message ID is valid when it is exactly 10 characters long. */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() == 10;
    }

    public String checkRecipientCell(String cell) {
        if (cell == null || !cell.trim().matches("\\+27\\d{9}")) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. "
                 + "Please correct the number and try again.";
        }
        this.recipientCell = cell.trim();
        return "Cell phone number successfully captured.";
    }

    public String checkMessageLength(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Message cannot be empty. Please enter a message.";
        }
        if (message.length() > MAX_LENGTH) {
            int over = message.length() - MAX_LENGTH;
            return "Message exceeds 250 characters by " + over + "; please reduce the size.";
        }
        this.messageText = message;
        return "Message ready to send.";
    }

    // ==================== ID AND HASH ====================

    /** Builds a 10-digit message ID one random digit at a time. */
    public void generateMessageID() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        this.messageID = id.toString();
    }

    /** Hash format: first two ID digits : message number : FIRSTWORDLASTWORD. */
    public String createMessageHash() {
        if (messageID == null || messageText == null || messageText.trim().isEmpty()) {
            return "00:0:EMPTY";
        }

        String message = messageText.trim();
        String firstWord = cleanWord(message.substring(0, firstSpaceOrEnd(message)));
        String lastWord = cleanWord(message.substring(message.lastIndexOf(" ") + 1));

        messageHash = messageID.substring(0, 2) + ":" + messageNumber + ":" + firstWord + lastWord;
        return messageHash;
    }

    private int firstSpaceOrEnd(String message) {
        int space = message.indexOf(" ");
        return space == -1 ? message.length() : space;
    }

    /** Upper-cases a word and strips everything except letters. */
    private String cleanWord(String word) {
        return word.toUpperCase().replaceAll("[^A-Z]", "");
    }

    // ==================== SEND / DISREGARD / STORE ====================

    /** Shows the send/disregard/store menu and processes the user's choice. */
    public String sendMessageOption(Scanner input) {
        System.out.println("\nPlease choose an option:");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message (or press 0)");
        System.out.println("3. Store Message to send later");
        System.out.print("Enter choice: ");
        return processChoice(input.nextLine().trim());
    }

    /**
     * Applies a menu choice to this message. Kept separate from the Scanner so
     * it can be unit tested directly.
     */
    public String processChoice(String choice) {
        String flag;
        String result;

        switch (choice) {
            case "1":
                flag = SENT;
                result = "Message successfully sent.";
                break;
            case "0":
            case "2":
                flag = DISREGARD;
                result = "Message disregarded";
                break;
            case "3":
                flag = STORED;
                result = "Message successfully stored.";
                break;
            default:
                return "Invalid option. Message not sent.";
        }

        messageNumber = nextMessageNumber();
        createMessageHash();
        add(this, flag);

        if (flag.equals(STORED)) {
            saveStoredMessages();
        }
        return result;
    }

     // ==================== POPULATING THE ARRAYS ====================

    /** Returns the next message number (used in the message hash). */
    public static int nextMessageNumber() {
        messageCounter++;
        return messageCounter;
    }

    /** Adds a message object to the arrays under the given flag. */
    public static void add(Messages message, String flag) {
        add(message.getID(), message.getMessageHash(), message.getRecipientCell(),
                message.getMessage(), flag);
    }

    /** Adds one message to the parallel arrays and to its category array. */
    public static void add(String id, String hash, String recipient, String text, String flag) {
        messageIDs.add(id);
        messageHashes.add(hash);
        recipients.add(recipient);
        messageTexts.add(text);
        flags.add(flag);
        categoryList(flag).add(text);
    }

    private static ArrayList<String> categoryList(String flag) {
        if (flag.equals(SENT)) {
            return sentMessages;
        }
        if (flag.equals(DISREGARD)) {
            return disregardedMessages;
        }
        return storedMessages;
    }

    /** Empties every array (used by the unit tests). */
    public static void reset() {
        messageIDs.clear();
        messageHashes.clear();
        recipients.clear();
        messageTexts.clear();
        flags.clear();
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageCounter = 0;

  // ==================== JSON FILE ====================

    /** Writes every stored message to the JSON file as a proper JSON array. */
    public static void saveStoredMessages() {
        StringBuilder json = new StringBuilder("[\n");
        boolean first = true;

        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i).equals(STORED)) {
                if (!first) {
                    json.append(",\n");
                }
                first = false;
                json.append("  {\"messageID\":\"").append(escapeJson(messageIDs.get(i)))
                    .append("\",\"messageHash\":\"").append(escapeJson(messageHashes.get(i)))
                    .append("\",\"recipient\":\"").append(escapeJson(recipients.get(i)))
                    .append("\",\"message\":\"").append(escapeJson(messageTexts.get(i)))
                    .append("\"}");
            }
        }
        json.append("\n]");

        try {
            Files.writeString(Paths.get(storeFile), json.toString());
            System.out.println("Message saved to " + storeFile);
        } catch (IOException e) {
            System.out.println("Error while storing message: " + e.getMessage());
        }
    }

    /** Reads the JSON file into the stored messages array (skips IDs already loaded). */
    public static void loadStoredMessages() {
        if (!Files.exists(Paths.get(storeFile))) {
            return;
        }

        try {
            String json = Files.readString(Paths.get(storeFile));

            // Matches each  "key":"value"  pair; the value may contain escaped characters
            Matcher pairs = Pattern.compile("\"(\\w+)\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"").matcher(json);

            String id = "";
            String hash = "";
            String recipient = "";

            // Each stored message is written as: messageID, messageHash, recipient, message
            while (pairs.find()) {
                String value = unescapeJson(pairs.group(2));
                switch (pairs.group(1)) {
                    case "messageID":
                        id = value;
                        break;
                    case "messageHash":
                        hash = value;
                        break;
                    case "recipient":
                        recipient = value;
                        break;
                    case "message":
                        if (!messageIDs.contains(id)) {
                            add(id, hash, recipient, value, STORED);
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read " + storeFile + ": " + e.getMessage());
        }
    }

    /** Makes text safe to place inside a JSON string. */
    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    /** Reverses escapeJson when reading text back from the file. */
    private static String unescapeJson(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\\' && i + 1 < text.length()) {
                i++;
                char next = text.charAt(i);
                if (next == 'n') {
                    result.append('\n');
                } else if (next == 'r') {
                    result.append('\r');
                } else if (next == 't') {
                    result.append('\t');
                } else {
                    result.append(next);
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
 // ==================== STORED MESSAGES MENU FEATURES ====================

    /** 2a. Sender and recipient of every stored message. */
    public static String displayStoredSenderAndRecipient(String sender) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i).equals(STORED)) {
                output.append("Sender: ").append(sender)
                      .append(" | Recipient: ").append(recipients.get(i)).append("\n");
            }
        }
        return output.length() == 0 ? "No stored messages." : output.toString();
    }

    /** 2b. The longest stored message. */
    public static String longestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        String longest = storedMessages.get(0);
        for (String text : storedMessages) {
            if (text.length() > longest.length()) {
                longest = text;
            }
        }
        return longest;
    }

    /** 2c. Search for a message ID; returns the recipient and message. */
    public static String searchByMessageID(String id) {
        int index = messageIDs.indexOf(id);
        if (index == -1) {
            return "No message found with ID " + id + ".";
        }
        return "Recipient: " + recipients.get(index) + "\nMessage: " + messageTexts.get(index);
    }

    /** 2d. All sent or stored messages for one recipient. */
    public static ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> found = new ArrayList<>();
        for (int i = 0; i < recipients.size(); i++) {
            boolean notDisregarded = !flags.get(i).equals(DISREGARD);
            if (notDisregarded && recipients.get(i).equals(recipient)) {
                found.add(messageTexts.get(i));
            }
        }
        return found;
    }

    /** 2e. Delete a message using its hash. */
    public static String deleteByHash(String hash) {
        int index = messageHashes.indexOf(hash);
        if (index == -1) {
            return "No message found with that hash.";
        }

        String text = messageTexts.get(index);
        String flag = flags.get(index);

        categoryList(flag).remove(text);
        messageIDs.remove(index);
        messageHashes.remove(index);
        recipients.remove(index);
        messageTexts.remove(index);
        flags.remove(index);

        if (flag.equals(STORED)) {
            saveStoredMessages();
        }
        return "Message: \"" + text + "\" successfully deleted.";
    }

    // ==================== REPORTS ====================

    /** Report of every sent message (hash, recipient, message). */
    public static String sentReport() {
        return buildReport(SENT, "SENT MESSAGES REPORT");
    }

    /** 2f. Report of the full details of every stored message. */
    public static String storedReport() {
        return buildReport(STORED, "STORED MESSAGES REPORT");
    }

    private static String buildReport(String flag, String title) {
        StringBuilder report = new StringBuilder("\n===== " + title + " =====\n");
        int count = 0;
        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i).equals(flag)) {
                count++;
                report.append("Message ").append(count).append("\n")
                      .append("  Message ID   : ").append(messageIDs.get(i)).append("\n")
                      .append("  Message Hash : ").append(messageHashes.get(i)).append("\n")
                      .append("  Recipient    : ").append(recipients.get(i)).append("\n")
                      .append("  Message      : ").append(messageTexts.get(i)).append("\n");
            }
        }
        if (count == 0) {
            return "No " + flag.toLowerCase() + " messages.";
        }
        return report.append("================================\n").toString();
    }

