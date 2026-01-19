import java.util.ArrayList;
import java.io.*;

public class Bank {
    private ArrayList<Account> accounts;

    public Bank() {
        loadAccounts();
    }

    public void addAccount(Account account) {
        accounts.add(account);
        saveAccounts();
    }

    public void removeAccount(int id) {
        accounts.removeIf(acc -> acc.getId() == id);
        saveAccounts();
    }

    public Account findAccount(int id) {
        for(Account acc : accounts) {
            if(acc.getId() == id) return acc;
        }
        return null;
    }

    // Transfer money between accounts
    public boolean transfer(int fromId, int toId, double amount) {
        Account from = findAccount(fromId);
        Account to = findAccount(toId);
        if(from != null && to != null && from.withdraw(amount)) {
            to.deposit(amount);
            from.getTransactionHistory().add("Transferred " + amount + " to ID " + toId);
            to.getTransactionHistory().add("Received " + amount + " from ID " + fromId);
            saveAccounts();
            return true;
        }
        return false;
    }

    // Save and Load accounts
    public void saveAccounts() {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("accounts.dat"))) {
            out.writeObject(accounts);
        } catch(IOException e) {
            System.out.println("Error saving accounts.");
        }
    }

    @SuppressWarnings("unchecked")
    public void loadAccounts() {
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream("accounts.dat"))) {
            accounts = (ArrayList<Account>) in.readObject();
        } catch(Exception e) {
            accounts = new ArrayList<>();
        }
    }
}

