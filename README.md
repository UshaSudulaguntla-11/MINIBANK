# Visionary Banking OS v9.0

A **Java-based digital banking system** with a polished GUI, animations, and secure account management.  
Designed as a desktop application, fully implemented in Java Swing.

---

## 💡 Project Overview

**Visionary Banking OS** simulates a real-world banking system with the following features:

- **Secure Login / Signup** with account PIN verification.
- **Dashboard UI** showing live account balance with smooth animations.
- **Deposit / Withdraw / Transfer** functionality.
- **Transaction History** with detailed logs.
- **Account Deletion** with password confirmation.
- **Modern, visually appealing UI** using custom colors, fonts, and animations.
- Safety-first design to simulate secure handling of account data.

This project demonstrates **Java OOP**, **Swing GUI**, and **event-driven programming** in a professional desktop environment.

---

## 🖥️ Screenshots
Image1:https://github.com/UshaSudulaguntla-11/MINIBANK/blob/main/assets/image1.png

Image2:https://github.com/UshaSudulaguntla-11/MINIBANK/blob/main/assets/image2.png

## ⚙️ Features in Detail

1. **Login & Signup Flow**
   - Users can create new accounts with ID, Name, and PIN.
   - Existing users can log in securely.
   
2. **Dashboard**
   - Real-time account balance animation.
   - Quick access to Deposit, Withdraw, and Transfer functions.
   
3. **Transaction History**
   - Displays all deposits, withdrawals, and transfers with timestamps.
   
4. **Account Deletion**
   - Confirm password before permanent deletion.
   
5. **Safety & Security**
   - Validates input for numerical IDs and transaction amounts.
   - Prevents overdrafts during withdrawal or transfer.

---

## 🛠️ Tech Stack

- **Language:** Java  
- **GUI Framework:** Swing  
- **Data Handling:** Object Serialization (`accounts.dat`)  
- **Design Patterns:** OOP, Event-driven Programming  

---
📂 Folder Structure
MiniBank/
├── src/                  # All Java source files
│   ├── Account.java
│   ├── Bank.java
│   ├── BankGUI.java
│   └── Main.java
├── README.md             # Project documentation
└── .gitignore            # Ignored files (compiled classes, IDE config)

## 📝 How to Run

1. Clone this repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/MiniBank.git
Navigate to the src folder:
cd MiniBank/src

Compile all Java files:
javac *.java

Run the GUI:
java BankGUI

All account data will be stored locally in accounts.dat.


