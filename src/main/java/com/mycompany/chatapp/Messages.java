/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
/**
 *
 * @author Student
 */
public class Messages {
    public static final int Max_Length=250;
    //Every sent message is stored here as text
    public static List<String> sentMessages = new ArrayList<>();
     public String messageID;
     public String ReceipientCell; 
     public String MessageHash;
     public String SentMessage;
     public String PrintMessage;
     public int amntOfMessage;
      public String storeMessage;
//Getters
public Messages(){}
public String getID(){return messageID;}
public String getReceipientCell(){return ReceipientCell ;}
public String getMessageHash(){return MessageHash ;}
public String getMessage(){return SentMessage ;}
public String getPrintMessage(){return PrintMessage ;}
public int getamntOfMessage(){return amntOfMessage ;}
public String getstoreMessage(){return storeMessage ;}


   // get the APP AND ID THINGIR 
  
//methods 

    
}

