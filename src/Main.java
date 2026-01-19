import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Mini Banking System ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Check Balance");
            System.out.println("6. Transaction History");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch(choice) {
                case 1:
                    System.out.print("Name: "); String name = sc.next();
                    System.out.print("ID: "); int id = sc.nextInt();
                    System.out.print("Password: "); String pwd = sc.next();
                    Account acc = new Account(name, id, 0, pwd);
                    bank.addAccount(acc);
                    System.out.println("Account created!");
                    break;

                case 2:
                    System.out.print("Account ID: "); int depId = sc.nextInt();
                    Account depAcc = bank.findAccount(depId);
                    if(depAcc != null) {
                        System.out.print("Password: "); String pass1 = sc.next();
                        if(depAcc.checkPassword(pass1)) {
                            System.out.print("Amount to deposit: "); double amt = sc.nextDouble();
                            depAcc.deposit(amt);
                            bank.saveAccounts();
                            System.out.println("Deposited successfully!");
                        } else System.out.println("Wrong password!");
                    } else System.out.println("Account not found.");
                    break;

                case 3:
                    System.out.print("Account ID: "); int witId = sc.nextInt();
                    Account witAcc = bank.findAccount(witId);
                    if(witAcc != null) {
                        System.out.print("Password: "); String pass2 = sc.next();
                        if(witAcc.checkPassword(pass2)) {
                            System.out.print("Amount to withdraw: "); double amt = sc.nextDouble();
                            if(witAcc.withdraw(amt)) {
                                bank.saveAccounts();
                                System.out.println("Withdraw successful!");
                            } else System.out.println("Insufficient balance.");
                        } else System.out.println("Wrong password!");
                    } else System.out.println("Account not found.");
                    break;

                case 4:
                    System.out.print("From Account ID: "); int fromId = sc.nextInt();
                    System.out.print("Password: "); String pass3 = sc.next();
                    System.out.print("To Account ID: "); int toId = sc.nextInt();
                    System.out.print("Amount: "); double amt = sc.nextDouble();
                    Account fromAcc = bank.findAccount(fromId);
                    if(fromAcc != null && fromAcc.checkPassword(pass3)) {
                        if(bank.transfer(fromId, toId, amt)) System.out.println("Transfer successful!");
                        else System.out.println("Transfer failed!");
                    } else System.out.println("Account not found or wrong password.");
                    break;

                case 5:
                    System.out.print("Account ID: "); int balId = sc.nextInt();
                    Account balAcc = bank.findAccount(balId);
                    if(balAcc != null) System.out.println(balAcc.getName() + " Balance: " + balAcc.getBalance());
                    else System.out.println("Account not found.");
                    break;

                case 6:
                    System.out.print("Account ID: "); int trId = sc.nextInt();
                    Account trAcc = bank.findAccount(trId);
                    if(trAcc != null) {
                        System.out.println("Transaction History:");
                        for(String s : trAcc.getTransactionHistory()) System.out.println(s);
                    } else System.out.println("Account not found.");
                    break;

                case 7: System.out.println("Exiting..."); break;
                default: System.out.println("Invalid choice.");
            }
        } while(choice != 7);

        sc.close();
    }
}


