package atmsimulation;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MiniStatement extends TransactionOperation
{
    public MiniStatement(String pin, String cardNo) 
    {
        super(pin, cardNo);
    }

    @Override
    public void perform()
    {
        try 
        {
            // 1. Fetch user name 
            String name = "Unknown";

            File signupFile = new File("signup.txt");
            File signup3File = new File("signup3.txt");

            Map<String, String> formNoToName = new HashMap<>();
            try (BufferedReader br = new BufferedReader(new FileReader(signupFile))) 
            {
                String line =br.readLine();
                while ((line = br.readLine()) != null) 
                {
                    if (line.trim().isEmpty()) 
                        continue;
                    
                    String[] parts = line.split(",");
                    
                    if (parts.length < 2) 
                        continue; 
                    
                    formNoToName.put(parts[0], parts[1]);
                }
            }

            try (BufferedReader br3 = new BufferedReader(new FileReader(signup3File))) 
            {
                String line = br3.readLine();
                while ((line = br3.readLine()) != null) 
                {
                    if (line.trim().isEmpty()) 
                            continue; // skip empty lines
                    String[] parts = line.split(",");
                    // parts: formNo,accountType,cardNo,pin
                    if (parts[2].equals(cardNo) && parts[3].equals(pin)) 
                    {
                        name = formNoToName.getOrDefault(parts[0], "Unknown");
                        break;
                    }
                }
            }

            String indent = "                       ";
            System.out.println(indent + "======================================================");
            System.out.println(indent + "                 BANK OF PUNJAB");
            System.out.println(indent + "                 MINI STATEMENT\n");
            System.out.println(indent + "Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            System.out.println();
            System.out.println(indent + "Account Holder: " + name);
            System.out.println(indent + "======================================================");

            // 2. Column headers
            System.out.printf(indent + "%-20s %-15s %15s%n", "DATE & TIME", "TYPE", "AMOUNT (Rs.)");
            System.out.println(indent + "------------------------------------------------------\n");

            // 3. Get last login time
            LocalDateTime lastLogin = null;
            File loginFile = new File("login.txt");
            try (BufferedReader brLogin = new BufferedReader(new FileReader(loginFile))) 
            {
                String line;
                while ((line = brLogin.readLine()) != null) 
                {
                    if (line.trim().isEmpty()) 
                            continue; // skip empty lines
                    String[] parts = line.split(",");
                    // formNo, cardNo, pin, loginTime
                    if (parts[1].equals(cardNo) && parts[2].equals(pin)) 
                    {
                        lastLogin = LocalDateTime.parse(parts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }
                }
            }

            // 4. Fetch transactions after last login
            File transFile = new File("transactions.txt");
            List<String[]> transList = new ArrayList<>();
            try (BufferedReader brTrans = new BufferedReader(new FileReader(transFile))) 
            {
                String line;
                while ((line = brTrans.readLine()) != null) 
                {
                    if (line.trim().isEmpty()) 
                            continue; // skip empty lines
                    String[] parts = line.split(",");
                    // cardNo,dateTime,type,transactionAmount
                    if (parts[0].equals(cardNo)) 
                    {
                        LocalDateTime dt = LocalDateTime.parse(parts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        if (lastLogin == null || !dt.isBefore(lastLogin)) 
                        {
                            transList.add(parts);
                        }
                    }
                }
            }

            if (transList.isEmpty()) 
            {
                System.out.println(indent + "       No transactions found after last login.");
            } 
            else 
            {
                for (String[] t : transList) 
                {
                    LocalDateTime dt = LocalDateTime.parse(t[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String type = t[2];
                    double amount = Double.parseDouble(t[1]);
                    String sign = type.equalsIgnoreCase("Deposit") ? "+" : "-";
                    System.out.printf(indent + "%-20s %-15s %15s%n", dt.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")), type, sign + String.format("%.2f", amount));
                }
            }

            System.out.println("\n" + indent + "------------------------------------------------------");

            // 5. Show current balance
            double balance = 0;
            File usersFile = new File("users.txt");
            try (BufferedReader brUsers = new BufferedReader(new FileReader(usersFile))) 
            {
                String line = brUsers.readLine();
                while ((line = brUsers.readLine()) != null) 
                {
                    if (line.trim().isEmpty()) 
                            continue; // skip empty lines
                    
                    String[] parts = line.split(",");
                    if (parts[0].equals(cardNo) && parts[1].equals(pin)) 
                    {
                        balance = Double.parseDouble(parts[2]);
                        break;
                    }
                }
            }

            System.out.printf(indent + "%-35s %15s%n", "Available Balance:", "Rs. " + String.format("%,.2f", balance));
            System.out.println(indent + "======================================================");

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            System.out.println("Error loading mini statement.");
        }
    }
}