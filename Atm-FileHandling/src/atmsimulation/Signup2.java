package atmsimulation;

import java.util.*;
import java.io.*;

public class Signup2 
{
    private String formNo;
    private String religion, nationality, income, education, occupation, CNIC, seniorCitizen, existingAccount;

    public Signup2(String formNo) 
    {
        System.out.println("\nProceed to next step (**Additional Info**).\n\n");
        this.formNo = formNo;
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
            File file = new File("signup2.txt");
            if (!file.exists()) 
            {
                file.createNewFile();
                
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) 
                {
                  // Write header first
                  bw.write("formNo,religion, nationality, income, education,occupation, CNIC,seniorCitizen, existingAccount");
                  bw.newLine();
                  bw.newLine();
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) 
            {
                String record = String.join(",",
                        formNo, religion, nationality, income, education,
                        occupation, CNIC, seniorCitizen, existingAccount
                );
                bw.write(record);
                bw.newLine();
            }

            System.out.println("\nStep 2 completed successfully. Continue to next step.\n");
        } 
        catch(Exception e) 
        {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}