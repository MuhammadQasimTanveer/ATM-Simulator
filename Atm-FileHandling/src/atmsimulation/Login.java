package atmsimulation;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Login extends BaseScreen{
    private String cardNo;
    private String pin;
    

    public Login() 
    {
        super("Account Login"); // title for BaseScreen
    }

    // Encapsulated setters/getters
    public void setCardNo(String cardNo) 
    { 
        this.cardNo = cardNo; 
    }
    
    public void setPin(String pin) 
    { 
        this.pin = pin; 
    }
    
    public String getCardNo() 
    { 
        return cardNo; 
    }
    
    public String getPin() 
    { 
        return pin; 
    }

    // Login method
    public boolean login() 
    {
        boolean success = false;
        
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

            BufferedReader br = new BufferedReader(new FileReader(usersFile));
            //String line;
            String line = br.readLine(); // skip header line
            String formNo = null;

            // Check cardNo + pin
            while ((line = br.readLine()) != null) 
            {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(cardNo) && parts[1].equals(pin)) 
                {
                    // Get formNo from signup3.txt
                    File signup3File = new File("signup3.txt");
                    if (!signup3File.exists())
                    {
                        signup3File.createNewFile();
                    }
                    
                    BufferedReader br3 = new BufferedReader(new FileReader(signup3File));
              
                    String line3 = br3.readLine(); // skip header line
                    while ((line3 = br3.readLine()) != null) 
                    {
                        String[] parts3 = line3.split(",");
                        if (parts3.length >= 4 && parts3[2].equals(cardNo)) 
                        {
                            formNo = parts3[0];
                            break;
                        }
                    }
                    br3.close();
                    break;
                }
            }
            br.close();

            if (formNo != null) 
            {
                success = true;

                // Record login time
                File loginFile = new File("login.txt");
                if (!loginFile.exists())
                {
                    loginFile.createNewFile();
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(loginFile))) 
                    {
                       // Write header first
                       bw.write("formNo,cardNo,pin,login_Time");
                       bw.newLine();
                       bw.newLine();
                    }
                }

                List<String> lines = new ArrayList<>();
                boolean updated = false;

                // Read existing login entries
                BufferedReader brLogin = new BufferedReader(new FileReader(loginFile));
                
                String l = brLogin.readLine(); // skip header line
                while ((l = brLogin.readLine()) != null) 
                {
                    String[] parts = l.split(",");
                    if (parts.length >= 3 && parts[1].equals(cardNo) && parts[2].equals(pin)) 
                    {
                        String newLine = String.join(",", parts[0], parts[1], parts[2],
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        lines.add(newLine);
                        updated = true;
                    } 
                    else 
                    {
                        lines.add(l);
                    }
                }
                brLogin.close();

                // If not found, add new entry
                if (!updated) 
                {
                    lines.add(String.join(",", formNo, cardNo, pin,
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
                }

                // Write back all login entries
                BufferedWriter bw = new BufferedWriter(new FileWriter(loginFile));
                bw.write("formNo,cardNo,pin,login_Time"); // write header
                bw.newLine();
                for (String entry : lines) 
                {
                    bw.write(entry);
                    bw.newLine();
                }
                bw.close();

                System.out.println("\nYou have Successfully Login!\n");
            }

        } 
        catch (Exception e) 
        {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return success;
    }
    
    // Run method from BaseScreen
    @Override
    public void run() 
    {
        Scanner sc = new Scanner(System.in);
        boolean loggedIn = false;

        showTitle(); // display screen title
        
        while (!loggedIn) 
        {
            System.out.print("Enter Card Number (or type 'E' to return): ");
            String card = sc.nextLine().trim();
            if (card.equalsIgnoreCase("E")) 
            {
                break;
            }
            setCardNo(card);

            System.out.print("Enter PIN: ");
            String pin = sc.nextLine().trim();
            setPin(pin);

            loggedIn = login();
            if (!loggedIn) 
            {
                System.out.println("\nInvalid Card Number or PIN.\n");
            }
        }

        if (loggedIn) 
        {
            System.out.println("Redirecting to Transactions...");
            new Transactions(getPin(),getCardNo()).run();
        }
    }
}