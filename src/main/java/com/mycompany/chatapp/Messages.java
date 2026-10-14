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

    // Instance fields (public – same style as your Login class)
    public String messageID;
    public String ReceipientCell;
    public String MessageHash;
    public String SentMessage;          // the actual text the user typed
    public String PrintMessage;
    public int amntOfMessage;           // the sequential number of this message
    public String storeMessage;

    // Constructors
    public Messages() {}

    // Getters (same style as your Login class)
    public String getID() { return messageID; }
    public String getReceipientCell() { return ReceipientCell; }
    public String getMessageHash() { return MessageHash; }
    public String getMessage() { return SentMessage; }
    public String getPrintMessage() { return PrintMessage; }
    public int getamntOfMessage() { return amntOfMessage; }
    public String getstoreMessage() { return storeMessage; }

    //methods
    public boolean checkMessageID() {
        if (messageID == null || messageID.length() != 10) {
            System.out.println("Message ID must be exactly 10 characters.");
            return false;
        }
        return true;
    }
    // Recvier Number
 public String checkRecipientCell(String cell) {
        if (cell == null || cell.trim().isEmpty()) {
            return "Recipient cell number cannot be empty.";
        }
  
        if (!cell.matches("\\+27\\d{9}")) {
            return "Recipient cell number format is invalid.";
        }
        this.ReceipientCell = cell;
        return "Cell number successfully captured.";
    }
 
 //message hash method 
 public String createMessageHash() {
        if (messageID == null || SentMessage == null || SentMessage.trim().isEmpty()) {
            return "00:0:EMPTY";
        }

        String firstTwo = messageID.substring(0,2);
        String[] words = SentMessage.trim().split("\\s+");
        String firstWord = words[0].toUpperCase();
        String lastWord  = words[words.length - 1].toUpperCase();

        // Remove any non-letter characters from the words for a clean hash
        firstWord = firstWord.replaceAll("[^A-Z]", "");
        lastWord  = lastWord.replaceAll("[^A-Z]", "");

        MessageHash = firstTwo + ":" + amntOfMessage + ":" + firstWord + lastWord;
        return MessageHash;
    }



    
}

