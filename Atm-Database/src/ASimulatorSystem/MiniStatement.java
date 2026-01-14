package ASimulatorSystem;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class MiniStatement
{
    private String pin;
    private String cardNo;

    public MiniStatement(String pin, String cardNo) 
    {
        this.pin = pin;
        this.cardNo = cardNo;
    }
    public void show() 
    {
        try 
        {
            Conn c = new Conn();
            // 1. Fetch user name & masked card
            String q1 = "SELECT s.name, s3.cardno FROM signup3 s3 JOIN signup s ON s3.formNo = s.formNo WHERE s3.cardno=? AND s3.pin = ?";
            PreparedStatement psUser = c.c.prepareStatement(q1);
            psUser.setString(1, cardNo);
            psUser.setString(2, pin);
            ResultSet rsUser = psUser.executeQuery();
            String name = "Unknown";
            if(rsUser.next()) 
            {
                name = rsUser.getString("name");
            }

            String indent = "                       "; 
            System.out.println(indent +"======================================================");
            System.out.println(indent +"                   Aurora BANK");
            System.out.println(indent +"                 MINI STATEMENT\n");
            System.out.println(indent +"Generated: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()));
            System.out.println();
            System.out.println(indent +"Account Holder: " + name);
            System.out.println(indent +"======================================================");
            
            // 2. Column headers
            System.out.printf(indent +"%-20s %-15s %15s%n", "DATE & TIME", "TYPE", "AMOUNT (Rs.)");
            System.out.println(indent +"------------------------------------------------------\n");
            
            // 3. Get last login time
            Timestamp lastLogin = null;
            String loginQuery = "SELECT login_time FROM login WHERE cardno =? AND pin = ? ORDER BY login_time DESC LIMIT 1";
            PreparedStatement psLogin = c.c.prepareStatement(loginQuery);
            psLogin.setString(1, cardNo);
            psLogin.setString(2, pin);
            ResultSet rsLogin = psLogin.executeQuery();
            if(rsLogin.next()) 
            {
                lastLogin = rsLogin.getTimestamp("login_time");
            }

            // 4. Fetch transactions after last login
            String tranQuery = "SELECT datetime, type, transactionAmount FROM transactions WHERE cardno = ? AND datetime >= ? ORDER BY datetime DESC";
            PreparedStatement psTran = c.c.prepareStatement(tranQuery);
            psTran.setString(1, cardNo);
            psTran.setTimestamp(2, lastLogin);
            ResultSet rsTran = psTran.executeQuery();

            boolean hasTransactions = false;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
            
            while(rsTran.next()) 
            {
                hasTransactions = true;
                Timestamp dt = rsTran.getTimestamp("datetime");
                String type = rsTran.getString("type");
                double amount = rsTran.getDouble("transactionAmount");
                String sign = type.equalsIgnoreCase("Deposit") ? "+" : "-";
                System.out.printf(indent +"%-20s %-15s %15s%n", sdf.format(dt), type, sign + String.format("%.2f", amount));
            }

            if(!hasTransactions) 
            {
                System.out.println(indent +"       No transactions found after last login.");
            }

            System.out.println("\n" + indent +"------------------------------------------------------");

            // 5. Show current balance
            String balanceQuery = "SELECT currentBalance FROM users WHERE cardno =? AND pin = ?";
            PreparedStatement psBal = c.c.prepareStatement(balanceQuery);
            psBal.setString(1, cardNo);
            psBal.setString(2, pin);
            ResultSet rsBal = psBal.executeQuery();
            if(rsBal.next()) {
                double balance = rsBal.getDouble("currentBalance");
                System.out.printf(indent +"%-35s %15s%n", "Available Balance:", "Rs. " + String.format("%,.2f", balance));
            }

            System.out.println(indent +"======================================================");
            
        } 
        catch(Exception e) 
        {
            e.printStackTrace();
            System.out.println("Error loading mini statement.");
        }
    }
}