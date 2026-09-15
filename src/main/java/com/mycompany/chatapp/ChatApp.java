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

    }

