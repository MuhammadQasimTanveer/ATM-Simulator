package atmsimulation;

import java.util.*;
import java.io.*;

public class Signup extends BaseScreen
{
    private String formNo;
    private String name, fatherName, dob, gender, email, maritalStatus, address, city, state;

    public Signup() 
    {
        super("New Account Registration"); 
        
        // Generate random form number
        Random ran = new Random();
        long first4 = (ran.nextLong() % 9000L) + 1000L;
        formNo = "" + Math.abs(first4);
  
    }
    
    public String getFormNo() 
    {
        return formNo;
    }

    public void promptUserInput() 
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Proceed to 1st step ( **Personal Details**).");
        
        // Name
        do 
        {
            System.out.print("\nEnter Name: ");
            name = sc.nextLine().trim();
            if(name.isEmpty() || !name.matches("^[a-zA-Z ]+$")) 
            {
                System.out.println("Invalid name. Only letters allowed.\n");
            }
        } 
        while(name.isEmpty() || !name.matches("^[a-zA-Z ]+$"));

        // Father's Name
        do 
        {
            System.out.print("Enter Father's Name: ");
            fatherName = sc.nextLine().trim();
            if(fatherName.isEmpty() || !fatherName.matches("^[a-zA-Z ]+$")) 
            {
                System.out.println("Invalid father's name. Only letters allowed.\n");
            }
        } 
        while(fatherName.isEmpty() || !fatherName.matches("^[a-zA-Z ]+$"));

        // Date of Birth
        do 
        {
            System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
            dob = sc.nextLine().trim();
            if(dob.isEmpty()) 
            {
                System.out.println("DOB cannot be empty.");
            }
        } 
        while(dob.isEmpty());

        // Gender
        do 
        {
            System.out.print("Enter Gender (Male/Female): ");
            gender = sc.nextLine().trim();
            if(!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) 
            {
                System.out.println("Invalid gender.\n");
                gender = null;
            }
        } 
        while(gender == null);

        // Email
        do 
        {
            System.out.print("Enter Email: ");
            email = sc.nextLine().trim();
            if(!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) 
            {
                System.out.println("Invalid email address.\n");
            }
        } 
        while(!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$"));

        // Marital Status
        do {
            System.out.print("Enter Marital Status (Married/Unmarried): ");
            maritalStatus = sc.nextLine().trim();
            if(!maritalStatus.equalsIgnoreCase("Married") && 
               !maritalStatus.equalsIgnoreCase("Unmarried")) 
            {
                System.out.println("Invalid marital status.\n");
                maritalStatus = null;
            }
        } while(maritalStatus == null);

        // Address
        do 
        {
            System.out.print("Enter Address: ");
            address = sc.nextLine().trim();
            if(address.isEmpty()) 
            {
                System.out.println("Address cannot be empty.\n");
            }
        } 
        while(address.isEmpty());

        // City
        do 
        {
            System.out.print("Enter City: ");
            city = sc.nextLine().trim();
            if(city.isEmpty() || !city.matches("^[a-zA-Z ]+$")) 
            {
                System.out.println("Invalid city. Only letters allowed.\n");
            }
        } 
        while(city.isEmpty() || !city.matches("^[a-zA-Z ]+$"));

        // State
        do 
        {
            System.out.print("Enter State: ");
            state = sc.nextLine().trim();
            if(state.isEmpty() || !state.matches("^[a-zA-Z ]+$")) 
            {
                System.out.println("Invalid state. Only letters allowed.\n");
            }
        } 
        while(state.isEmpty() || !state.matches("^[a-zA-Z ]+$"));
    }

    public void saveToDatabase() 
    {
        try 
        {
            File file = new File("signup.txt");
            if (!file.exists()) 
            {
                file.createNewFile(); // create file if not exists
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) 
                {
                  // Write header first
                  bw.write("formNo,name,fathername,dob,gender,email,maritalstatus,address,city,state");
                  bw.newLine();
                  bw.newLine();
                }
                
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) 
            {
                String record = String.join(",",
                    formNo, name, fatherName, dob, gender, email,
                    maritalStatus, address, city, state
                );
                bw.write(record);
                bw.newLine();
            }

            System.out.println("Step 1 completed successfully. Continue to next step.\n");
        } 
        catch(Exception e) 
        {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() 
    {
        showTitle(); 
        promptUserInput();
        saveToDatabase();
                    
        Signup2 s2 = new Signup2(getFormNo());
        s2.promptUserInput();
        s2.saveToDatabase();

        Signup3 s3 = new Signup3(getFormNo());
        s3.promptUserInput();
        s3.saveToDatabase();

        System.out.println("Signup complete! You can now login.");
    }
}