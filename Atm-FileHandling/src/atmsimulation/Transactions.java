package atmsimulation;

import java.util.Scanner;

public class Transactions extends BaseScreen{

    private String pin;
    private String cardNo;

    public Transactions(String pin, String cardNo) 
    {
        super("Account Transactions"); // title for BaseScreen
        this.pin = pin;
        this.cardNo = cardNo;
    }

    @Override
    public void run()
    {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        
        showTitle(); // display screen title

        do 
        {
            System.out.println("\n--- ATM Transactions ---");
            System.out.println("1. Deposit");
            System.out.println("2. Cash Withdrawl");
            System.out.println("3. Fast Cash");
            System.out.println("4. Mini Statement");
            System.out.println("5. PIN Change");
            System.out.println("6. Balance Enquiry");
            System.out.println("7. Exit");
            System.out.println("\nSelect an option (1-7): ");

            try 
            {
                choice = Integer.parseInt(sc.nextLine());
            } 
            catch (NumberFormatException e) 
            {
                choice = 0;
            }

            TransactionOperation operation = switch (choice) 
            {
                case 1 -> new Deposit(pin, cardNo);
                case 2 -> new Withdraw(pin, cardNo);
                case 3 -> new FastCash(pin, cardNo);
                case 4 -> new MiniStatement(pin, cardNo);
                case 5 -> new Pin(pin, cardNo);
                case 6 -> new BalanceEnquiry(pin, cardNo);
                default -> null;
            };

            if (operation != null) 
            {
                operation.perform();
            } 
            else if (choice == 7) 
            {
                System.out.println("\nSession ended. Thank you for using our ATM!");
            } 
            else 
            {
                System.out.println("Invalid option. Try again.");
            }

        } while(choice != 7);
    }
}