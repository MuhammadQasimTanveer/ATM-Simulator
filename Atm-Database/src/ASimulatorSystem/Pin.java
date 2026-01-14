package ASimulatorSystem;

import java.util.Scanner;
import java.sql.*;

public class Pin {

    private String oldPin;
    private String cardNo;

    public Pin(String oldPin, String cardNo) 
    {
        this.oldPin = oldPin;
        this.cardNo = cardNo;
    }

    public void changePin()
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter New PIN: ");
        String newPin1, newPin2;
         String pinRegex = "^[0-9]{4}$";

        while (true) 
        {
           // 1️⃣ Enter new PIN
           System.out.print("Enter New PIN: ");
           newPin1 = sc.nextLine().trim();

           if (newPin1.isEmpty()) 
           {
               System.out.println("PIN cannot be empty. Try again.\n");
               continue; 
           }
           
           if (!newPin1.matches(pinRegex)) 
           {
               System.out.println("Invalid PIN. Only 4 digits allowed\n");
               continue;
           }

           System.out.print("Re-enter New PIN: ");
           newPin2 = sc.nextLine().trim();

           if (newPin2.isEmpty()) 
           {
               System.out.println("Re-entered PIN cannot be empty. Try again.\n");
               continue; 
            }
           
           if (!newPin2.matches(pinRegex)) 
           {
               System.out.println("Invalid PIN. Only 4 digits allowed\n");
               continue;
           }

           if (!newPin1.equals(newPin2)) 
           {
               System.out.println("PINs do not match. Please try again.\n");
               continue; 
            }

           break;
        }

        Conn c1 = new Conn();

        try {
            c1.c.setAutoCommit(false);

            String[] tables = {"users", "login", "signup3"};

            for(String table : tables) 
            {
                String query = "UPDATE " + table + " SET pin = ? WHERE pin = ?";
                PreparedStatement ps = c1.c.prepareStatement(query);
                ps.setString(1, newPin1);
                ps.setString(2, oldPin);
                ps.executeUpdate();
            }

            c1.c.commit();
            c1.c.setAutoCommit(true);

            System.out.println("\nPIN changed successfully!");

            // Update oldPin to newPin for further operations
            oldPin = newPin1;

        } 
        catch(Exception e)
        {
            try { 
                c1.c.rollback(); 
            } 
            catch(SQLException ex) 
            { 
                ex.printStackTrace(); 
            }
            e.printStackTrace();
            System.out.println("Something went wrong. Please try again.");
        }
    }
}