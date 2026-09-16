/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author Student
 */
public class Login {
    public String name;
   public String username; 
   public String password;
   public String cellPhoneNumber;
//Getters
public Login(){}
public String getName(){return name;}
public String getUsername(){return username;}
public String getPassword(){return password;}
public String getCellPhoneNumber(){return cellPhoneNumber;}

//method for name
public boolean name(String name){ //if theres a problem its because you exlcded the name cannot be empty 
if (!name.trim().matches("[ a-zA-Z]+")){
System.out.println("First name must contain letters only");
return false;
}
this.name = name.trim();
return true;
}

//method for username
public boolean checkUsername(String username) {
    if (username == null || username.trim().isEmpty()) {
        System.out.println("Username cannot be empty. Try again.\n");
        return false;
    }
    if (username.trim().length() < 4) {
        System.out.println("Username must be at least 4 characters. Try again.\n");
        return false;
    }
    if (!username.trim().contains("_")) {
        System.out.println("Username must contain an underscore (_). Try again.\n");
        return false;
    }
    this.username = username.trim();
    return true;
}
//method for password 


    // Rules: at least 8 characters, 1 capital, 1 number, 1 special character
    public boolean checkPassword(String password) {
        if (password == null || password.length() < 8) {
            System.out.println("Password must be at least 8 characters. Try again.\n");
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        String specialChars = "!@#$%^&*()_+-=[]{};':\"\\|,.<>/?";

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (c >= 'A' && c <= 'Z') {
                hasCapital = true;
            } else if (c >= '0' && c <= '9') {
                hasNumber = true;
            } else if (specialChars.indexOf(c) != -1) {
                hasSpecial = true;
            }
        }if (!hasCapital) {
            System.out.println("Password must contain at least one capital letter. Try again.\n");
            return false;
        }
        if (!hasNumber) {
            System.out.println("Password must contain at least one number. Try again.\n");
            return false;
        }
        if (!hasSpecial) {
            System.out.println("Password must contain at least one special character. Try again.\n");
            return false;
        }

        this.password = password;
        return true;
    }
    // method for cell phone
public boolean checkCellPhoneNumber(String cellNumber) {
    if (cellNumber == null || !cellNumber.trim().matches("\\+27\\d{9}")) {
        System.out.println("Invalid SA cell number format. Use +27 and add 9 numbers. Try again.\n");
        return false;
    }
    this.cellPhoneNumber = cellNumber.trim();
    return true;
}
//login method
public boolean loginUser(String enteredUsername, String enteredPassword) {
    if (this.username == null || this.password == null) {
        System.out.println("No user registered yet. Please register first.");
        return false;
    }

    if (this.username.equals(enteredUsername) && this.password.equals(enteredPassword)) {
        System.out.println("Login successful. Welcome back!");
        return true;
    } else {
        System.out.println("Username or password incorrect. Please try again.");
        return false;
    }
}
}    


