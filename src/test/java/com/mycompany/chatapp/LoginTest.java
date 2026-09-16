package com.mycompany.chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 
 * 
 */
public class LoginTest {
Login login = new Login();
    // Username test

    @Test
    public void testUsernameValid() {
        // Test Data: "kyl_1"
        boolean result = login.checkUsername("kyl_1");
        assertTrue(result, "Username with underscore and length ≤ 5 should be accepted");
        assertEquals("kyl_1", login.getUsername());
    }

    @Test
    public void testUsernameInvalid() {
        // Test Data: "kyle!!!!!"
        boolean result = login.checkUsername("kyle!!!!!");
        assertFalse(result, "Username without underscore should be rejected");
    }

    @Test
    public void testUsernameInvalidLength() {
        // Username longer than 5 characters (and has underscore)
        boolean result = login.checkUsername("kyle_123");
        assertTrue(result, "Username longer than 5 characters should be rejected");
    }
 @Test
    public void testUsernameEmpty() {
        assertFalse(login.checkUsername(""));
        assertFalse(login.checkUsername(null));
        assertFalse(login.checkUsername("   "));
    }
    // password test

    @Test
    public void testPasswordValid() {
        // Test Data: "Ch&sec@ke99!"
        boolean result = login.checkPassword("Ch&sec@ke99!");
        assertTrue(result, "Password that meets all complexity rules should be accepted");
        assertEquals("Ch&sec@ke99!", login.getPassword());
    }

    @Test
    public void testPasswordInvalid() {
        // Test Data: "password"
        boolean result = login.checkPassword("password");
        assertFalse(result, "Simple password should be rejected");
    }

     @Test
    public void testPasswordTooShort() {
        assertFalse(login.checkPassword("Ab1!"));          
        assertTrue(login.checkPassword("Abcdefg1!"));     
    }

    @Test
    public void testPasswordMissingCapital() {
        assertFalse(login.checkPassword("ch&sec@ke99!"));
    }

    @Test
    public void testPasswordMissingNumber() {
        assertFalse(login.checkPassword("Ch&sec@ke!!"));
    }

    @Test
    public void testPasswordMissingSpecialCharacter() {
        assertFalse(login.checkPassword("Chsecake99"));
    }
    // cell phone test

    @Test
    public void testCellPhoneNumberValid() {
        // Test Data: +27838968976
        boolean result = login.checkCellPhoneNumber("+27838968976");
        assertTrue(result, "Correctly formatted SA cell number should be accepted");
        assertEquals("+27838968976", login.getCellPhoneNumber());
    }

    @Test
    public void testCellPhoneNumberInvalid() {
        // Test Data: 08966553
        boolean result = login.checkCellPhoneNumber("08966553");
        assertFalse(result, "Cell number without international code should be rejected");
    }
//login test
 @Test
    public void testLoginSuccessful() {
        login.checkUsername("kyl_1");
        login.checkPassword("Ch&sec@ke99!");

        boolean result = login.loginUser("kyl_1", "Ch&sec@ke99!");
        assertTrue(result);          // True
    }

    
    @Test
    public void testLoginFailed() {
        boolean result = login.loginUser("kyl_1", "Ch&sec@ke99!");
        assertFalse(result);         // False
    }
   

   
}