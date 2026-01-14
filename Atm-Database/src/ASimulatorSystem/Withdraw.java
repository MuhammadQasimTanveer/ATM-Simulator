package ASimulatorSystem;

import java.util.Scanner;
import java.sql.*;

public class Withdraw 
{
    private String pin;
    private String cardNo;

    public Withdraw(String pin, String cardNo) 
    {
        this.pin = pin;
        this.cardNo = cardNo;
    }

    public void perform() 
    {
        Scanner sc = new Scanner(System.in);
        
        double withdrawAmount = 0.0;
        String moneyRegex = "^[0-9]+(\\.[0-9]{1,2})?$";

        while (true) 
        {
           System.out.print("Enter amount to withdraw: ");
           String input = sc.nextLine().trim();

           if (input.isEmpty() || !input.matches(moneyRegex)) 
           {
               System.out.println("Please enter a valid numeric amount.\n");
               continue;
            }

            withdrawAmount = Double.parseDouble(input);
            if (withdrawAmount <= 0) 
            {
               System.out.println("Withdrawal amount must be greater than zero.\n");
               continue; 
            }
            break; 
        }

        Conn c1 = new Conn();
        
        try 
        {
            c1.c.setAutoCommit(false);

            // 1. Fetch current balance
            String q1 = "SELECT currentBalance FROM users WHERE cardno = ? AND pin = ?";
            PreparedStatement ps1 = c1.c.prepareStatement(q1);
            ps1.setString(1, cardNo);
            ps1.setString(2, pin);
            ResultSet rs = ps1.executeQuery();

            double currentBalance = 0;
            if(rs.next()) {
                currentBalance = rs.getDouble("currentBalance");
            }

            // 2. Check sufficient funds
            if(currentBalance < withdrawAmount) {
                System.out.println("Insufficient balance to perform this transaction.\n");
                return;
            }

            // 3. Calculate new balance
            double newBalance = currentBalance - withdrawAmount;

            // 4. Insert into transactions table
            String q2 = "INSERT INTO transactions(cardno, transactionAmount, type) VALUES (?, ?, ?)";
            PreparedStatement ps2 = c1.c.prepareStatement(q2);
            ps2.setString(1, cardNo);
            ps2.setDouble(2, withdrawAmount);
            ps2.setString(3, "Withdrawal");
            ps2.executeUpdate();

            // 5. Update balance
            String q3 = "UPDATE users SET currentBalance = ? WHERE cardno = ? AND pin = ?";
            PreparedStatement ps3 = c1.c.prepareStatement(q3);
            ps3.setDouble(1, newBalance);
            ps3.setString(2, cardNo);
            ps3.setString(3, pin);
            ps3.executeUpdate();

            c1.c.commit();
            c1.c.setAutoCommit(true);

            System.out.println("\nWithdrawal Successful!");
            System.out.println("Amount Debited: Rs. " + withdrawAmount);
            System.out.println("Updated Balance: Rs. " + newBalance);

        } 
        catch(Exception e) 
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