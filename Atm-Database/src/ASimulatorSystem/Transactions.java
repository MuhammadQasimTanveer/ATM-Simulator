package ASimulatorSystem;

// package ASimulatorSystem;
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

            switch(choice) 
            {
                case 1:
                    new Deposit(pin, cardNo).perform();
                    break;
                    
                case 2:
                    new Withdraw(pin, cardNo).perform();
                    break;
                    
                case 3:
                    new FastCash(pin, cardNo).perform();
                    break;
                    
                case 4:
                    new MiniStatement(pin, cardNo).show();
                    break;
                    
                case 5:
                    new Pin(pin, cardNo).changePin();
                    break;
                    
                case 6:
                    new BalanceEnquiry(pin, cardNo).perform();
                    break;
                    
                case 7:
                    new MiniStatement(pin, cardNo).show();
                    System.out.println("\nYour transaction session has ended.");
                    System.out.println("Thank you!\n");
                    break;
                    
                default:
                    System.out.println("Invalid option. Try again.");
            }

        } while(choice != 7);
    }

//    public static void main(String[] args) 
//    {
//        Scanner sc = new Scanner(System.in);
//        System.out.print("Enter Card Number: ");
//        String cardNo = sc.nextLine().trim();
//        System.out.print("Enter PIN: ");
//        String pin = sc.nextLine().trim();
//
//        Transactions atm = new Transactions(pin, cardNo);
//        atm.showMenu();
//    }
}
