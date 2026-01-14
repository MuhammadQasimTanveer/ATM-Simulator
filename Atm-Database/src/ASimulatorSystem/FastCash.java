package ASimulatorSystem;

import java.sql.*;
import java.util.Scanner;

public class FastCash{

    private String pin;
    private String cardNo;

    public FastCash(String pin, String cardNo) {
        this.pin = pin;
        this.cardNo = cardNo;
    }

    public void perform()
    {
        Scanner sc = new Scanner(System.in);
        double[] amounts = {100, 500, 1000, 2000, 5000, 10000};

        while (true)
        {
            System.out.println("\nSELECT WITHDRAWL AMOUNT:");
            for (int i = 0; i < amounts.length; i++) 
            {
                System.out.println((i + 1) + ". Rs " + (int)amounts[i]);
            }
            System.out.println((amounts.length + 1) + ". Back");

            System.out.print("\nEnter choice: ");
            int choice = sc.nextInt();

            if (choice < 1 || choice > amounts.length + 1) 
            {
                System.out.println("Invalid choice!");
                continue;
            }

            if (choice == amounts.length + 1)
            {
                System.out.println("Going back to main menu...");
                return;
            }

            double withdrawAmount = amounts[choice - 1];

            Conn c1 = new Conn();
            
            try 
            {
                c1.c.setAutoCommit(false);

                // 1. Fetch current balance
                String balanceQuery = "SELECT currentBalance FROM users WHERE cardno =? AND pin = ?";
                PreparedStatement ps1 = c1.c.prepareStatement(balanceQuery);
                ps1.setString(1, cardNo);
                ps1.setString(2, pin);
                ResultSet rs = ps1.executeQuery();

                double currentBalance = 0;
                if (rs.next()) 
                {
                    currentBalance = rs.getDouble("currentBalance");
                }

                // 2. Check sufficient balance
                if (currentBalance < withdrawAmount) 
                {
                    System.out.println("Insufficient Balance!");
                    System.out.println("Current Balance: Rs " + currentBalance);
                    return;
                }

                // 3. Deduct balance
                String updateBalance = "UPDATE users SET currentBalance = currentBalance - ? WHERE cardno =? AND pin = ?";
                PreparedStatement ps2 = c1.c.prepareStatement(updateBalance);
                ps2.setDouble(1, withdrawAmount);
                ps2.setString(2, cardNo);
                ps2.setString(3, pin);
                ps2.executeUpdate();

                // 4. Insert transaction
                String insertTransaction = "INSERT INTO transactions (cardno, transactionAmount, type) VALUES (?, ?, ?)";
                PreparedStatement ps3 = c1.c.prepareStatement(insertTransaction);
                ps3.setString(1, cardNo);
                ps3.setDouble(2, withdrawAmount);
                ps3.setString(3, "Withdrawal");
                ps3.executeUpdate();

                c1.c.commit();
                c1.c.setAutoCommit(true);

                System.out.println("Transaction Successful!\n");
                System.out.println("Amount Debited: Rs " + withdrawAmount);
                System.out.println("New Balance: Rs " + (currentBalance - withdrawAmount));
            } 
            catch (Exception e) 
            {
               try { 
                   c1.c.rollback(); 
                } 
               catch (SQLException ex) 
               { 
                   ex.printStackTrace(); 
                }
            
               e.printStackTrace();
               System.out.println("Something went wrong. Please try again.");
            }
        }
    }
}