package atmsimulation;

import java.io.*;

public class BalanceEnquiry extends TransactionOperation
{
    public BalanceEnquiry(String pin, String cardNo) 
    {
        super(pin, cardNo);
    }

    @Override
    public void perform() 
    {
        try
        {
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

            try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) 
            {
                String line = br.readLine();
                boolean userFound = false;
                double balance = 0;

                while ((line = br.readLine()) != null) 
                {
                    String[] parts = line.split(",");
                    if (parts[0].equals(cardNo) && parts[1].equals(pin))
                    {
                        userFound = true;
                        balance = Double.parseDouble(parts[2]);
                        break;
                    }
                }

                if (userFound) 
                {
                   System.out.println("Your Current Account Balance is Rs " + balance);
                }
            } 
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            System.out.println("Something went wrong. Please try again.");
        }
    }
}