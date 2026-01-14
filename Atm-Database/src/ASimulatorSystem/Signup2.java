package ASimulatorSystem;

import java.sql.*;
import java.util.*;

public class Signup2 
{
    private String formNo;
    private String religion, nationality, income, education, occupation, CNIC, seniorCitizen, existingAccount;
    private Conn conn;

    public Signup2(String formNo) 
    {
        System.out.println("\nProceed to next step (**Additional Info**).\n\n");
        
        this.formNo = formNo;
        conn = new Conn();
    }

    public void promptUserInput() 
    {
        Scanner sc = new Scanner(System.in);

        // Religion
        String[] religions = {"Muslim","Christian","Hindu","Sikh","Other"};
        religion = selectOption(sc, "Select Religion", religions);

        // Nationality
        String[] nationalities = {"Pakistani", "Other"};
        nationality = selectOption(sc, "Select Nationality", nationalities);

        // Income
        String[] incomes = {
            "Below 100,000",
            "100,000 - 250,000",
            "250,000 - 500,000",
            "500,000 - 1,000,000",
            "Above 1,000,000"
        };
        income = selectOption(sc, "Select Income", incomes);

        // Education
        String[] educations = {"BelowGraduate","Graduate","PostGraduate","Doctorate / PhD","Others"};
        education = selectOption(sc, "Select Education", educations);

        // Occupation
        String[] occupations = {"Salaried","Self-Employmed","Businessman","Student","Retired","Others"};
        occupation = selectOption(sc, "Select Occupation", occupations);

        // CNIC / ID
        do 
        {
            System.out.print("\nEnter CNIC/ID: ");
            CNIC = sc.nextLine().trim();
            if(nationality.equals("Pakistani")) 
            {
                if(!CNIC.matches("\\d{5}-\\d{7}-\\d{1}")) 
                {
                    System.out.println("Invalid CNIC format (xxxxx-xxxxxxx-x)\n");
                    CNIC = null;
                }
            } 
            else 
            {
                if(!CNIC.matches("[a-zA-Z0-9]{5,20}")) 
                {
                    System.out.println("Invalid ID (5–20 characters).\n");
                    CNIC = null;
                }
            }
        } while(CNIC == null);

        // Senior Citizen
        String[] yesNo = {"Yes", "No"};
        seniorCitizen = selectOption(sc, "\nAre you a Senior Citizen?", yesNo);

        // Existing Account
        existingAccount = selectOption(sc, "\nAlready have an account?", yesNo);
    }

    private String selectOption(Scanner sc, String prompt, String[] options) 
    {
        int choice = -1;
        do 
        {
            System.out.println(prompt + ":");
            for(int i=0; i<options.length; i++) 
            {
                System.out.println((i+1) + ". " + options[i]);
            }
            System.out.print("\nEnter choice (1-" + options.length + "): ");
            try 
            {
                choice = Integer.parseInt(sc.nextLine());
                if(choice < 1 || choice > options.length) 
                {
                    System.out.println("Invalid selection. Try again.\n");
                    choice = -1;
                }
            } 
            catch(NumberFormatException e) 
            {
                System.out.println("Invalid input. Enter a number.\n");
                choice = -1;
            }
        } 
        while(choice == -1);
        
        return options[choice-1];
    }

    public void saveToDatabase() 
    {
        try 
        {
            conn.c.setAutoCommit(false);

            String query = "INSERT INTO signup2 VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.c.prepareStatement(query);
            ps.setString(1, formNo);
            ps.setString(2, religion);
            ps.setString(3, nationality);
            ps.setString(4, income);
            ps.setString(5, education);
            ps.setString(6, occupation);
            ps.setString(7, CNIC);
            ps.setString(8, seniorCitizen);
            ps.setString(9, existingAccount);

            ps.executeUpdate();
            conn.c.commit();
            conn.c.setAutoCommit(true);

            System.out.println("\nStep 2 completed successfully. Continue to next step.\n\n");
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
