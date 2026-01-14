package ASimulatorSystem;

import java.util.Scanner;

public class MainMenu 
{
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) 
        {
            System.out.println("\n===== Welcome to ATM =====");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("\nEnter choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) 
            {
                case "1":
                    Login login = new Login();
                    login.run();
                    break;

                case "2":
                    Signup s1 = new Signup();
                    s1.run();
                    break;

                case "3":
                    System.out.println("\n                 Thank you for using our ATM. Goodbye!\n\n");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
