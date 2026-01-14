package atmsimulation;

import java.io.*;
import java.util.*;

public class Pin extends TransactionOperation
{
    public Pin(String pin, String cardNo) 
    {
        super(pin,cardNo);
    }

    @Override
    public void perform()
    {
        Scanner sc = new Scanner(System.in);
        String newPin1, newPin2;
         String pinRegex = "^[0-9]{4}$";

        while (true) 
        {
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
        
        Map<String, String> fileHeaders = new HashMap<>();
        fileHeaders.put("users.txt", "cardNo,pin,currentBalance");
        fileHeaders.put("login.txt", "formNo,cardNo,pin,login_Time");
        fileHeaders.put("signup3.txt", "formNo,accountType,cardNo,pin");

        // Tables to update: users, login, signup3
        String[] files = {"users.txt", "login.txt", "signup3.txt"};

        try 
        {
            for (String fileName : files) 
            {
                File file = new File(fileName);
                if (!file.exists())
                {
                    continue;
                } // skip if file doesn't exist

                List<String> lines = new ArrayList<>();
                String headerLine = fileHeaders.get(fileName); // get the correct header for this file
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();

                while ((line = br.readLine()) != null) 
                {
                    String[] parts = line.split(",");
                    // Find pin column
                    for (int i = 0; i < parts.length; i++) 
                    {
                        if (parts[i].equals(pin)) 
                        {
                            parts[i] = newPin1; // update oldPin to newPin
                        }
                    }
                    lines.add(String.join(",", parts));
                }
                br.close();

                // Write back updated lines
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write(headerLine); // write header specific to this file
                bw.newLine();
                bw.newLine();
                for (String l : lines) 
                {
                    bw.write(l);
                    bw.newLine();
                }
                bw.close();
            }

            System.out.println("\nPIN changed successfully!");
            pin = newPin1; // update oldPin for further operations

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            System.out.println("Something went wrong. Please try again.");
        }
    }
}