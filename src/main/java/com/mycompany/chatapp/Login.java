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

public boolean checkPassword(String password) {
    if (password == null || password.length() <= 8) {
        System.out.println("Password must be more than 8 characters. Try again.\n");
        return false;
    }
    if (!password.matches(".*[A-Z].*")) {
        System.out.println("Password must contain at least one capital letter. Try again.\n");
        return false;
    }
    if (!password.matches(".*\\d.*")) {
        System.out.println("Password must contain at least one number. Try again.\n");
        return false;
    }
    if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
        System.out.println("Password must contain at least one special character. Try again.\n");
        return false;
    }
    this.password = password.trim();
    return true;

    // method for cell phone
public boolean checkCellPhoneNumber(String cellNumber) {
    if (cellNumber == null || !cellNumber.trim().matches("\\+27\\d{9}")) {
        System.out.println("Invalid SA cell number format. Use +27 and add 9 numbers. Try again.\n");
        return false;
    }
    this.cellPhoneNumber = cellNumber.trim();
    return true;
}

    
}

