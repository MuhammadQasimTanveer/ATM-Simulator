package atmsimulation;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FastCash extends TransactionOperation
{
    public FastCash(String pin, String cardNo) 
    {
        super(pin, cardNo);
    }

    @Override
    public void perform()
    {
        Scanner sc = new Scanner(System.in);
        double[] amounts = {500, 1000, 2000, 5000, 8000, 10000};

        while (true)
        {
            System.out.println("\nSELECT WITHDRAWL AMOUNT:");
            for (int i = 0; i < amounts.length; i++) 
            {
                System.out.println((i + 1) + ". Rs " + (int)amounts[i]);
            }
            System.out.println((amounts.length + 1) + ". Back");

            System.out.print("\nEnter choice: ");
            int choice = sc.nextInt();

            if (choice < 1 || choice > amounts.length + 1) 
            {
                System.out.println("Invalid choice!");
                continue;
            }

            if (choice == amounts.length + 1)
            {
                System.out.println("Going back to main menu...");
                return;
            }

            double withdrawAmount = amounts[choice - 1];
            
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
                boolean userFound = false;

                BufferedReader br = new BufferedReader(new FileReader(usersFile));
                String line = br.readLine(); // skip header line
                while ((line = br.readLine()) != null) 
                {
                    String[] parts = line.split(",");
                    if (parts[0].equals(cardNo) && parts[1].equals(pin)) 
                    {
                        userFound = true;
                        currentBalance = Double.parseDouble(parts[2]);
                        if (currentBalance < withdrawAmount) 
                        {
                            System.out.println("Insufficient balance.\n");
                            br.close();
                            return;
                        }
                        parts[2] = String.valueOf(currentBalance - withdrawAmount);
                    }
                    usersLines.add(String.join(",", parts));
                }
                br.close();

                // Update users.txt
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

                // Append transaction
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
                            String.valueOf(withdrawAmount),
                            "FastCash",
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    );
                    tbw.write(record);
                    tbw.newLine();
                }

                System.out.println("Transaction Successful!\n");
                System.out.println("Amount Debited: Rs " + withdrawAmount);
                System.out.println("New Balance: Rs " + (currentBalance - withdrawAmount));

            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                System.out.println("Transaction failed. Please try again.");
            }
        }
    }
}