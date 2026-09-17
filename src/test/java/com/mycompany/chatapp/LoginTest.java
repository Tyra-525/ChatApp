package com.mycompany.chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 
 * @author 
 */
public class LoginTest {
Login login = new Login();

 //***AssertEquals******
    // Username test
 @Test
    public void testUsernameValid() {
        // Test Data: "kyl_1"
        login.checkUsername("kyl_1");
        assertEquals("kyl_1", login.getUsername());
    }
    
 @Test
    public void testUsernameInvalid() {
    // Test Data: "kyle!!!!!"
    login.checkUsername("kyle!!!!!");
    assertEquals(null, login.getUsername());
}
   
    // password test
 @Test
    public void testPasswordValid() {
    // Test Data: "Ch&sec@ke99!"
    login.checkPassword("Ch&sec@ke99!");
    assertEquals("Ch&sec@ke99!", login.getPassword());
}
 @Test
     public void testPasswordInvalid() {
    // Test Data: "password"
    login.checkPassword("password");
    assertEquals(null, login.getPassword());
}

 //cellphone number test
 @Test
    public void testCellPhoneNumberValid() {
    // Test Data: +27838968976
    login.checkCellPhoneNumber("+27838968976");
    assertEquals("+27838968976", login.getCellPhoneNumber());
}
 @Test
    public void testCellPhoneNumberInvalid() {
    // Test Data: "0821234567"
    login.checkCellPhoneNumber("0821234567");
    assertEquals(null, login.getCellPhoneNumber());
}
    
 
//****AssertTrue/False*************
   //login 
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
  //username
@Test
    public void testUsernameValid_returnsTrue() {
        // Test Data: "kyl_1"
        boolean result = login.checkUsername("kyl_1");
        assertTrue(result,"Username with underscore and length ≤ 5 should be accepted");
}
@Test
    public void testUsernameInvalid_returnsFalse() {
    // Test Data: "kyle!!!!!"
    boolean result = login.checkUsername("kyle!!!!!");
    assertFalse(result,"Username without underscore should be rejected");
}
  // password
@Test
    public void testPasswordValid_returnsTrue() {
    // Test Data: "Ch&sec@ke99!"
    boolean result = login.checkPassword("Ch&sec@ke99!");
    assertTrue(result,"Password that meets all complexity rules should be accepted");
}
@Test
    public void testPasswordInvalid_returnsFalse() {
    // Test Data: "password"
    boolean result = login.checkPassword("password");
    assertFalse(result,"Simple password should be rejected");
}
   //cellphone number
@Test
    public void testCellPhoneNumberValid_returnsTrue() {
    // Test Data: +27838968976
    boolean result = login.checkCellPhoneNumber("+27838968976");
    assertTrue(result,"Correctly formatted SA cell number should be accepted" );
}
@Test
   public void testCellPhoneNumberInvalid_returnsFalse() {
    // Test Data: "0821234567"
    boolean result = login.checkCellPhoneNumber("0821234567");
    assertFalse(result,"Cell number missing the +27 international code should be rejected");
}
 
}