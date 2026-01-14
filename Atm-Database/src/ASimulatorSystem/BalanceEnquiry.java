package ASimulatorSystem;

import java.sql.*;

public class BalanceEnquiry {

    private String pin;
    private String cardNo;

    public BalanceEnquiry(String pin, String cardNo) 
    {
        this.pin = pin;
        this.cardNo = cardNo;
    }

    public void perform() 
    {
        Conn c1 = new Conn();
        try 
        {
            String query = "SELECT currentBalance FROM users WHERE cardno = ? AND pin = ?";
            PreparedStatement ps = c1.c.prepareStatement(query);
            ps.setString(1, cardNo);
            ps.setString(2, pin);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) 
            {
                double balance = rs.getDouble("currentBalance");
                System.out.println("Your Current Account Balance is Rs " + balance);
            }
        } 
        catch(Exception e) 
        {
            e.printStackTrace();
            System.out.println("Something went wrong. Please try again.");
        }
    }
}