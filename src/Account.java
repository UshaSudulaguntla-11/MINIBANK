import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {
    private String name;
    private int id;
    private double balance;
    private String password;
    private ArrayList<String> transactionHistory;

    public Account(String name, int id, double balance, String password) {
        this.name = name;
        this.id = id;
        this.balance = balance;
        this.password = password;
        transactionHistory = new ArrayList<>();
        transactionHistory.add("Account created with balance: " + balance);
    }

    // Getters
    public String getName() { return name; }
    public int getId() { return id; }
    public double getBalance() { return balance; }
    public boolean checkPassword(String input) { return password.equals(input); }
    public ArrayList<String> getTransactionHistory() { return transactionHistory; }

    // Deposit
    public void deposit(double amount) {
        if(amount > 0) {
            balance += amount;
            transactionHistory.add("Deposited: " + amount);
        }
    }

    // Withdraw
    public boolean withdraw(double amount) {
        if(amount > 0 && amount <= balance) {
            balance -= amount;
            transactionHistory.add("Withdrew: " + amount);
            return true;
        }
        return false;
    }
}
