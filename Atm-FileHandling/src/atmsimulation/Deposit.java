package atmsimulation;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deposit extends TransactionOperation
{
    public Deposit(String pin, String cardNo) 
    {
        super(pin, cardNo);
    }

    @Override
    public void perform() 
    {
        Scanner sc = new Scanner(System.in);
        double amount = 0;
        String moneyRegex = "^[0-9]+(\\.[0-9]{1,2})?$";

        while(true) 
        {
            System.out.print("Enter amount to deposit: ");
            String input = sc.nextLine().trim();

            if (input.isEmpty() || !input.matches(moneyRegex)) 
            {
               System.out.println("Please enter a valid numeric amount.\n");
               continue; 
            }

           amount = Double.parseDouble(input);
           if (amount <= 0) 
           {
               System.out.println("Amount must be greater than zero.\n");
               continue;
            }
           
            if (amount > 100000) 
            {
                System.out.println("You cannot deposit more than Rs. 100,000 at a time.\n");
                continue;
            }
            break; 
        }
        
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

            List<String> usersLines = new ArrayList<>();
            double currentBalance = 0;

            // 1. Read users file & find balance
            BufferedReader br = new BufferedReader(new FileReader(usersFile));
            String line = br.readLine(); // skip header line
            while ((line = br.readLine()) != null) 
            {
                String[] parts = line.split(",");
                if (parts[0].equals(cardNo) && parts[1].equals(pin)) 
                {
                    currentBalance = Double.parseDouble(parts[2]);
                    currentBalance += amount;
                    parts[2] = String.valueOf(currentBalance);
                }
                usersLines.add(String.join(",", parts));
            }
            br.close();

            // 2. Write updated balance back to users.txt
            BufferedWriter bw = new BufferedWriter(new FileWriter(usersFile));
            bw.write("cardNo,pin,currentBalance"); // write header
            bw.newLine();
            bw.newLine();
            for (String uline : usersLines) 
            {
                bw.write(uline);
                bw.newLine();
            }
            bw.close();

             // 3. Record transaction
            File transactionsFile = new File("transactions.txt");
            if (!transactionsFile.exists())
            {
                transactionsFile.createNewFile();
                try (BufferedWriter bw2 = new BufferedWriter(new FileWriter(transactionsFile))) 
                {
                   // Write header first
                   bw2.write("cardNo,transactionAmout, type,transaction_time");
                   bw2.newLine();
                   bw2.newLine();
                }
            }

            try (BufferedWriter tbw = new BufferedWriter(new FileWriter(transactionsFile, true))) 
            {
                String record = String.join(",",
                        cardNo,
                        String.valueOf(amount),
                        "Deposit",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                );
                tbw.write(record);
                tbw.newLine();
            }

            System.out.println("\nDeposit Successful!\n");
            System.out.println("Amount Deposited: Rs. " + amount);
            System.out.println("Updated Balance: Rs. " + currentBalance);

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            System.out.println("Transaction failed. Please try again.");
        }
    }
}