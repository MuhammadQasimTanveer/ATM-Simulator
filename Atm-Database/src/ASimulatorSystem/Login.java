package ASimulatorSystem;

import java.sql.*;
import java.util.Scanner;

public class Login extends BaseScreen{
    private String cardNo;
    private String pin;
    
    private Conn conn;

    public Login() 
    {
        super("Account Login"); // title for BaseScreen
        conn = new Conn(); 
    }

    // Encapsulated setters/getters
    public void setCardNo(String cardNo) 
    { 
        this.cardNo = cardNo; 
    }
    
    public void setPin(String pin) 
    { 
        this.pin = pin; 
    }
    
    public String getCardNo() 
    { 
        return cardNo; 
    }
    
    public String getPin() 
    { 
        return pin; 
    }

    // Login method
    public boolean login() 
    {
        boolean success = false;

        try 
        {
            conn.c.setAutoCommit(false);

            String query = "SELECT * FROM signup3 WHERE cardno = ? AND pin = ?";
            PreparedStatement ps = conn.c.prepareStatement(query);
            ps.setString(1, getCardNo());
            ps.setString(2, getPin());

            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                // Insert/update login table
                String checkLogin = "SELECT * FROM login WHERE cardno=? AND pin=?";
                PreparedStatement psLogin = conn.c.prepareStatement(checkLogin);
                psLogin.setString(1, getCardNo());
                psLogin.setString(2, getPin());
                ResultSet rsLogin = psLogin.executeQuery();

                if(rsLogin.next()) 
                {
                    String updateLogin = "UPDATE login SET login_time=CURRENT_TIMESTAMP WHERE cardno=? AND pin=?";
                    PreparedStatement psUpdate = conn.c.prepareStatement(updateLogin);
                    psUpdate.setString(1, getCardNo());
                    psUpdate.setString(2, getPin());
                    psUpdate.executeUpdate();
                } 
                else 
                {
                    String insertLogin = "INSERT INTO login(formno, cardno, pin) VALUES (?, ?, ?)";
                    PreparedStatement psInsert = conn.c.prepareStatement(insertLogin);
                    psInsert.setString(1, rs.getString("formNo"));
                    psInsert.setString(2, getCardNo());
                    psInsert.setString(3, getPin());
                    psInsert.executeUpdate();
                }

                conn.c.commit();
                conn.c.setAutoCommit(true);

                System.out.println("\nYou have Successfully Login!\n");
                success = true;
            }
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
        }

        return success;
    }
    
    // Run method from BaseScreen
    @Override
    public void run() 
    {
        Scanner sc = new Scanner(System.in);
        boolean loggedIn = false;

        showTitle(); // display screen title
        
        while (!loggedIn) 
        {
            System.out.print("Enter Card Number (or type 'E' to return): ");
            String card = sc.nextLine().trim();
            if (card.equalsIgnoreCase("E")) 
            {
                break;
            }
            setCardNo(card);

            System.out.print("Enter PIN: ");
            String pin = sc.nextLine().trim();
            setPin(pin);

            loggedIn = login();
            if (!loggedIn) 
            {
                System.out.println("\nInvalid Card Number or PIN.\n");
            }
        }

        if (loggedIn) 
        {
            System.out.println("Redirecting to Transactions...");
            new Transactions(getPin(),getCardNo()).run();
        }
    }
}