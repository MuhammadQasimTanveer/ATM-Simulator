package ASimulatorSystem;

import java.sql.*;
import java.util.*;

public class Signup3 
{
    private String formNo;
    private String accountType;
    private String cardNo;
    private String pin;
    private Conn conn;

    public Signup3(String formNo) 
    {
        System.out.println("\nProceed to next step (**Account Details**).");
        
        this.formNo = formNo;
        conn = new Conn();
    }

    public void promptUserInput() 
    {
        Scanner sc = new Scanner(System.in);

        String[] accountTypes = {"Saving Account", "Fixed Deposit Account", "Current Account", "Recurring Deposit Account"};
        int choice = -1;
        
        do 
        {
            System.out.println("Select Account Type:");
            for (int i = 0; i < accountTypes.length; i++) 
            {
                System.out.println((i+1) + ". " + accountTypes[i]);
            }
            System.out.print("\nEnter choice (1-4): ");
            try 
            {
                choice = Integer.parseInt(sc.nextLine());
                if (choice < 1 || choice > accountTypes.length) {
                    System.out.println("Invalid choice. Try again.");
                    choice = -1;
                }
            } 
            catch (NumberFormatException e) 
            {
                System.out.println("Invalid input. Enter a number.");
                choice = -1;
            }
        } 
        while(choice == -1);
        
        accountType = accountTypes[choice-1];

        // Declaration
        String declaration = "";
        do 
        {
            System.out.println("Declaration: I declare that the above details are correct to the best of my knowledge.");
            System.out.print("Type 'yes' to accept: ");
            declaration = sc.nextLine().trim().toLowerCase();
        } 
        while(!declaration.equals("yes"));
    }

    private void generateCardAndPin() 
    {
        Random ran = new Random();
        
        long first7 = (ran.nextLong() % 90000000L) + 5040936000000000L;
        cardNo = String.valueOf(Math.abs(first7));
        
        long first3 = (ran.nextLong() % 9000L) + 1000L;
        pin = String.valueOf(Math.abs(first3));
    }

    public void saveToDatabase() 
    {
        generateCardAndPin();
        try 
        {
            conn.c.setAutoCommit(false);

            // Insert into signup3
            String q1 = "INSERT INTO signup3(formNo, accountType, cardno, pin) VALUES (?, ?, ?, ?)";
            PreparedStatement ps1 = conn.c.prepareStatement(q1);
            ps1.setString(1, formNo);
            ps1.setString(2, accountType);
            ps1.setString(3, cardNo);
            ps1.setString(4, pin);
            ps1.executeUpdate();

            // Insert services
            String[] services = {"ATM Card", "Mobile Banking", "Transaction Notifications"};
            for (String service : services) 
            {
                String q2 = "INSERT INTO account_services(formNo, serviceName) VALUES (?, ?)";
                PreparedStatement ps2 = conn.c.prepareStatement(q2);
                ps2.setString(1, formNo);
                ps2.setString(2, service);
                ps2.executeUpdate();
            }

            // Insert into users table
            String q3 = "INSERT INTO users(cardno, pin) VALUES (?, ?)";
            PreparedStatement psUsers = conn.c.prepareStatement(q3);
            psUsers.setString(1, cardNo);
            psUsers.setString(2, pin);
            psUsers.executeUpdate();

            conn.c.commit();
            conn.c.setAutoCommit(true);

            System.out.println("\nAccount Created Successfully!\n");
            System.out.println("Card Number: " + cardNo);
            System.out.println("PIN: " + pin);

        } 
        catch(Exception e) 
        {
            try { 
                conn.c.rollback(); 
            } 
            catch(SQLException ex) 
            { 
                ex.printStackTrace(); 
            }
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
