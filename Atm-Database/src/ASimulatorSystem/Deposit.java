package ASimulatorSystem;

import java.util.Scanner;
import java.sql.*;

public class Deposit 
{
    private String pin;
    private String cardNo;

    public Deposit(String pin, String cardNo) 
    {
        this.pin = pin;
        this.cardNo = cardNo;
    }

    public void perform() 
    {
        Scanner sc = new Scanner(System.in);
        double amount = 0;
        String moneyRegex = "^[0-9]+(\\.[0-9]{1,2})?$";

        while(true) 
        {
            System.out.print("Enter amount to deposit: ");
            String input = sc.nextLine().trim();

            if (input.isEmpty() || !input.matches(moneyRegex)) 
            {
               System.out.println("Please enter a valid numeric amount.\n");
               continue; 
            }

           amount = Double.parseDouble(input);
           if (amount <= 0) 
           {
               System.out.println("Amount must be greater than zero.\n");
               continue;
            }
            break; 
        }
        

        Conn c1 = new Conn();
        
        try 
        {
            c1.c.setAutoCommit(false);

            // Get current balance
            String balanceQuery = "SELECT currentBalance FROM users WHERE cardno = ? AND pin = ?";
            PreparedStatement ps1 = c1.c.prepareStatement(balanceQuery);
            ps1.setString(1, cardNo);
            ps1.setString(2, pin);
            ResultSet rs = ps1.executeQuery();

            double currentBalance = 0;
            if (rs.next())
            {
                currentBalance = rs.getDouble("currentBalance");
            }

            double newBalance = currentBalance + amount;

            // Insert transaction
            String insertQuery = "INSERT INTO transactions (cardno, transactionAmount, type) VALUES (?, ?, ?)";
            PreparedStatement ps2 = c1.c.prepareStatement(insertQuery);
            ps2.setString(1, cardNo);
            ps2.setDouble(2, amount);
            ps2.setString(3, "Deposit");
            ps2.executeUpdate();

            // Update balance
            String updateQuery = "UPDATE users SET currentBalance = ? WHERE cardno = ? AND pin = ?";
            PreparedStatement ps3 = c1.c.prepareStatement(updateQuery);
            ps3.setDouble(1, newBalance);
            ps3.setString(2, cardNo);
            ps3.setString(3, pin);
            ps3.executeUpdate();

            c1.c.commit();
            c1.c.setAutoCommit(true);

            System.out.println("\nDeposit Successful!\n");
            System.out.println("Amount Deposited: Rs. " + amount);
            System.out.println("Updated Balance: Rs. " + newBalance);

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
            System.out.println("Transaction failed. Please try again.");
        }
    }
}