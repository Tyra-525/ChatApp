/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatapp;
import java.util.Scanner;

/**
 *
 * @author Student
 */
public class ChatApp {
static Login registeredAccount;
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
boolean running = true;
System.out.println("******************************");
System.out.println("Welcome to ChatApp");
System.out.println("******************************");
System.out.println("Please select an option below.");
System.out.println("1.Register\n"
                  + "2.Login\n"
                  + "3.Exit");

int option = input.nextInt();
            input.nextLine(); // consume leftover newline

            if (option == 1) {
                register(input);
            } else if (option == 2) {
                login(input);
            } else if (option == 3) {
                System.out.println("\nGoodbye!");
                running = false;
            } else {
                System.out.println("\nInvalid option. Please try again.\n");
            }
    }
//register
    
    /**
     *
     * @param input
     */
    public static void register(Scanner input){
     System.out.println("\n********************");
     System.out.println("Account Registration");
     System.out.println("\n********************");
     
     Login obj = new Login();
     boolean valid = false;
     
     while(!valid){
      System.out.print("Please enter your name:");
      valid = obj.name(input.nextLine());
     }
     valid  = false;
     while(!valid){
      System.out.print("Please enter your username (minimum 4 cahracters,must include an underscore):");
      valid = obj.checkUsername(input.nextLine());
     }
     
     valid  = false;
     while(!valid){
      System.out.print("Please enter your password (8+ chars, 1 number, 1 capital, 1 special char):");
       valid = obj.checkPassword(input.nextLine());
 }
     valid  = false;
     while(!valid){
      System.out.print("Please enter your number (e.g. +27821234567):):");
      valid = obj.checkCellPhoneNumber(input.nextLine());
     }
     
     registeredAccount= obj;

       System.out.println("\nRegistration successful! Please login.\n");
       login(input);
    }

    }

