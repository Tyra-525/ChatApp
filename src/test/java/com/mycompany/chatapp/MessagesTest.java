package com.mycompany.chatapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Messages class, using the five test messages from the task.
 *
 * @author Student
 */
public class MessagesTest {

    private static final String TEST_FILE = "testStoredMessages.json";

    private static final String MSG1 = "Did you get the cake?";
    private static final String MSG2 = "Where are you? You are late! I have asked you to be on time.";
    private static final String MSG3 = "Yohoooo, I am at your gate.";
    private static final String MSG4 = "It is dinner time!";
    private static final String MSG5 = "Ok, I am leaving without you.";

    @BeforeEach
    public void setUp() {
        // Use a temporary file so the real storedMessages.json is never touched
        Messages.storeFile = TEST_FILE;
        Messages.reset();

        addTestMessage("1000000001", "+27834557896", MSG1, Messages.SENT);
        addTestMessage("1000000002", "+27838884567", MSG2, Messages.STORED);
        addTestMessage("1000000003", "+27834484567", MSG3, Messages.DISREGARD);
        addTestMessage("1000000004", "0838884567", MSG4, Messages.SENT);
        addTestMessage("1000000005", "+27838884567", MSG5, Messages.STORED);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    /** Builds a message from test data and adds it to the arrays. */
    private void addTestMessage(String id, String recipient, String text, String flag) {
        Messages message = new Messages();
        message.setMessageID(id);
        message.setRecipientCell(recipient);
        message.setMessageText(text);
        message.setMessageNumber(Messages.nextMessageNumber());
        message.createMessageHash();
        Messages.add(message, flag);
    }

    /** Builds a valid message that is not yet in the arrays. */
    private Messages newValidMessage() {
        Messages message = new Messages();
        message.generateMessageID();
        message.checkRecipientCell("+27718693002");
        message.checkMessageLength("Hi Mike, can you join us for dinner tonight?");
        return message;
    }

    // ==================== PART 2 TESTS ====================

    @Test
    public void testMessageLength_Success() {
        assertEquals("Message ready to send.",
                new Messages().checkMessageLength("Hi Mike, can you join us for dinner tonight?"));
    }

    @Test
    public void testMessageLength_Failure() {
        String result = new Messages().checkMessageLength("A".repeat(260));
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", result);
    }

    @Test
    public void testRecipientNumber_Success() {
        assertEquals("Cell phone number successfully captured.",
                new Messages().checkRecipientCell("+27718693002"));
    }

    @Test
    public void testRecipientNumber_Failure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. "
                   + "Please correct the number and try again.",
                new Messages().checkRecipientCell("08575975889"));
    }

    @Test
    public void testMessageHash() {
        Messages message = new Messages();
        message.setMessageID("0012345678");
        message.setMessageNumber(0);
        message.setMessageText("Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    @Test
    public void testMessageIDCreated() {
        Messages message = new Messages();
        message.generateMessageID();
        assertTrue(message.checkMessageID());
        assertTrue(message.getID().matches("\\d{10}"));
    }

    @Test
    public void testSendMessageOption() {
        Messages message = newValidMessage();
        int sentBefore = Messages.sentMessages.size();

        assertEquals("Message successfully sent.", message.processChoice("1"));
        assertEquals(sentBefore + 1, Messages.sentMessages.size());
    }

    @Test
    public void testDisregardMessageOption() {
        Messages message = newValidMessage();
        int disregardedBefore = Messages.disregardedMessages.size();

        assertEquals("Message disregarded", message.processChoice("0"));
        assertEquals(disregardedBefore + 1, Messages.disregardedMessages.size());
    }

    @Test
    public void testStoreMessageOption() {
        Messages message = newValidMessage();
        int storedBefore = Messages.storedMessages.size();

        assertEquals("Message successfully stored.", message.processChoice("3"));
        assertEquals(storedBefore + 1, Messages.storedMessages.size());
        assertTrue(Files.exists(Paths.get(TEST_FILE)));
        assertNotEquals(0, message.getMessageNumber());
    }

    @Test
    public void testInvalidMessageOption() {
        assertEquals("Invalid option. Message not sent.", newValidMessage().processChoice("9"));
    }

    @Test
    public void testReturnTotalMessages() {
        assertEquals(2, new Messages().returnTotalMessages());
    }

    // ==================== PART 3 TESTS ====================

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(MSG1, MSG4));
        assertEquals(expected, Messages.sentMessages);
    }

    @Test
    public void testDisregardedAndStoredArraysPopulated() {
        assertEquals(new ArrayList<>(Arrays.asList(MSG3)), Messages.disregardedMessages);
        assertEquals(new ArrayList<>(Arrays.asList(MSG2, MSG5)), Messages.storedMessages);
    }

    @Test
    public void testHashAndIDArraysPopulated() {
        assertEquals(5, Messages.messageIDs.size());
        assertEquals(5, Messages.messageHashes.size());
        assertEquals("1000000004", Messages.messageIDs.get(3));
    }

    @Test
    public void testDisplayLongestMessage() {
        assertEquals(MSG2, Messages.longestStoredMessage());
    }

    @Test
    public void testSearchForMessageID() {
        assertEquals("Recipient: 0838884567\nMessage: " + MSG4,
                Messages.searchByMessageID("1000000004"));
    }

    @Test
    public void testSearchForMessageID_NotFound() {
        assertEquals("No message found with ID 9999999999.",
                Messages.searchByMessageID("9999999999"));
    }

    @Test
    public void testSearchMessagesForRecipient() {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(MSG2, MSG5));
        assertEquals(expected, Messages.searchByRecipient("+27838884567"));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        String hash = Messages.messageHashes.get(1); // Test Message 2

        assertEquals("Message: \"" + MSG2 + "\" successfully deleted.",
                Messages.deleteByHash(hash));
        assertEquals(4, Messages.messageHashes.size());
        assertFalse(Messages.storedMessages.contains(MSG2));
    }

    @Test
    public void testDeleteMessageUsingHash_NotFound() {
        assertEquals("No message found with that hash.", Messages.deleteByHash("no:such:hash"));
    }

    @Test
    public void testDisplayReport() {
        String report = Messages.sentReport();

        assertTrue(report.contains(Messages.messageHashes.get(0)));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains(MSG1));
        assertTrue(report.contains(Messages.messageHashes.get(3)));
        assertTrue(report.contains("0838884567"));
        assertTrue(report.contains(MSG4));
        assertFalse(report.contains(MSG2)); // stored messages are not in the sent report
    }

    @Test
    public void testReadJsonFileIntoArray() {
        Messages.saveStoredMessages();
        Messages.reset();
        Messages.loadStoredMessages();

        ArrayList<String> expected = new ArrayList<>(Arrays.asList(MSG2, MSG5));
        assertEquals(expected, Messages.storedMessages);
        assertEquals("+27838884567", Messages.recipients.get(0));
    }
}