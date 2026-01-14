package atmsimulation;

import java.util.*;
import java.io.*;

public class Signup3 
{
    private String formNo, accountType, cardNo, pin;
    
    public Signup3(String formNo) 
    {
        System.out.println("Proceed to next step (**Account Details**).");
        
        this.formNo = formNo;
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
            System.out.println("\nDeclaration: I declare that the above details are correct to the best of my knowledge.");
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

        try {
            // signup3.txt stores account info
            File signup3File = new File("signup3.txt");
            if (!signup3File.exists())
            {
                signup3File.createNewFile();
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(signup3File))) 
                {
                  // Write header first
                  bw.write("formNo, accountType, cardNo, pin");
                  bw.newLine();
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(signup3File, true))) 
            {
                String record = String.join(",", formNo, accountType, cardNo, pin);
                bw.write(record);
                bw.newLine();
            }

            // account_services.txt stores default services
            File servicesFile = new File("account_services.txt");
            if (!servicesFile.exists())
            {
                servicesFile.createNewFile();
                
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(servicesFile))) 
                {
                  // Write header first
                  bw.write("formNo,service");
                  bw.newLine();
                  bw.newLine();
                }
            }

            String[] services = {"ATM Card", "Mobile Banking", "Transaction Notifications"};
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(servicesFile, true))) 
            {
                for (String service : services) 
                {
                    String record = String.join(",", formNo, service);
                    bw.write(record);
                    bw.newLine();
                }
            }

            // users.txt stores cardNo + pin for login
            File usersFile = new File("users.txt");
            if (!usersFile.exists())
            {
                usersFile.createNewFile();
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(usersFile))) 
                {
                   // Write header first
                   bw.write("cardNo,pin,currentBalance");
                   bw.newLine();
                   bw.newLine();
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(usersFile, true))) 
            {
                String record = String.join(",", cardNo, pin, "0.0");
                bw.write(record);
                bw.newLine();
            }

            System.out.println("\nAccount Created Successfully!\n");
            System.out.println("Card Number: " + cardNo);
            System.out.println("PIN: " + pin);

        } 
        catch(Exception e) 
        {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
