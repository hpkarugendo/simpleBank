package simpleBank;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.spi.TransactionalWriter;

import simpleBank.entities.Account;
import simpleBank.entities.Customer;
import simpleBank.enums.AccountType;
import simpleBank.menus.Menu;
import simpleBank.storage.Database;
import static java.lang.System.out;

import java.text.NumberFormat;

/*
 * The Main Class
 * By Henry Patrick Karugendo
 * Created 30th Nov 2018
 * Updated 5th Dec 2018
 * This Class is the Logic Class and the most important Class also the most complex
 * It is the Main Class that runs the program
 * Run from this Class!
 */
public class Main {
	private Menu m;
	private Database db;
	private List<Customer> customers;
	private Customer currentCustomer;
	private Account currentAccount;
	private int currentAccIndex;
	private Scanner scan;
	private String op;

	public Main() {
		this.m = new Menu();
		this.db = new Database();
		this.customers = db.load();
		this.scan = new Scanner(System.in);
		this.op = "";
		this.currentCustomer = null;
		this.currentAccount = null;
		this.currentAccIndex = -1;
	}

	/*
	 * This is the most important method after the main method
	 * It runs the main program and keeps it running until a Customer chooses to quit
	 * If the Customer enters an invalid input, the program continues on until the right input is entered
	 * When the right option is selected, the mainLogic() method is called
	 */
	public void run() {
		while (!op.equals("6")) {
			m.mainMenu();
			System.out.println("Choose an option (1-6): ");
			op = scan.nextLine();

			if (validMain()) {
				mainLogic();
			} else {
				while (!validMain()) {
					System.out.println("Please choose a valid option (1-6)...");
					op = scan.nextLine();
				}
				mainLogic();
			}
		}
	}

	//This method checks validity of inputs for the run() method
	private boolean validMain() {
		if (op.equals("1") || op.equals("2") || op.equals("3") || op.equals("4") || op.equals("5") || op.equals("6")) {
			return true;
		}

		return false;
	}

	/*
	 * This method allocated the chosen option to its handler method
	 * If the option was to quit, then the method ends the program
	 */
	private void mainLogic() {
		switch (op) {
		case "1":
			withdraw();
			break;
		case "2":
			deposit();
			break;
		case "3":
			apply();
			break;
		case "4":
			manage();
			break;
		case "5":
			accInfo();
			break;
		case "6":
			db.save(customers);
			System.exit(0);
			break;
		}
	}

	/*
	 * This method displays the Customer information
	 * Their Profile and their Account Details
	 * It required the Customer to be logged in so it calls
	 * The reset() method to clear system and the login() method to login users
	 */
	private void accInfo() {
		reset();
		login();
		if (isLoggedin()) {
			out.println("\n//////////////////////////////////////");
			out.println("=========== CUSTOMER INFO ============");
			out.println(currentCustomer.toString());
			out.println("/////////////////////////////////////\n");
		}
	}

	
	private void manage() {
		reset();

		m.manageMenu();
		out.println("Choose an Option (1-6): ");
		op = scan.nextLine();
		switch (op) {
		case "1":
			change(1);
			break;
		case "2":
			change(2);
			break;
		case "3":
			change(3);
			break;
		case "4":
			change(4);
			break;
		case "5":
			closeAcc();
			break;
		case "6":
			op = "";
			run();
			break;
		default:
			out.println("\n/////////////////////////////////////\n");
			out.println("!!!! Invalid Input !!!!\n");
			out.println("/////////////////////////////////////\n");
			op = "4";
			mainLogic();
			break;
		}
	}

	private void closeAcc() {
		reset();
		login();

		if (isLoggedin()) {
			out.println("\n//////////////////////////////////////");

			if (currentAccount.getBalance() > 0) {
				out.println("!!!**!!! CLOSING YOUR ACCOUNT !!!**!!!");
				out.println("Take Your Balance of "
						+ NumberFormat.getCurrencyInstance().format(currentAccount.getBalance()) + " before closing!");
				try {
					Thread.sleep(5000);
					customers.remove(currentCustomer);
					db.save(customers);
					out.println("===== ACCOUNT NO: " + currentAccount.getAccountNo() + " IS CLOSED =====");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else if (currentAccount.getBalance() < 0) {
				out.println("===== ACCOUNT NO: " + currentAccount.getAccountNo() + " CAN'T BE CLOSED =====");
				out.println("===== PLEASE CLEAR BALANCE FIRST! =====");
			} else if (currentAccount.getBalance() == 0) {
				out.println("!!!**!!! CLOSING YOUR ACCOUNT !!!**!!!");
				customers.remove(currentCustomer);
				db.save(customers);
				out.println("===== ACCOUNT NO: " + currentAccount.getAccountNo() + " IS CLOSED =====");
			}

			out.println("/////////////////////////////////////\n");
		}
	}

	private void change(int i) {
		login();
		if (isLoggedin()) {
			switch (i) {
			case 1:
				out.println("Enter New Pin: ");
				op = scan.nextLine();
				if (!isPinOK(op)) {
					do {
						op = "";
						out.println("Invalid Pin or Pin already in Use: Enter New Pin: ");
						op = scan.nextLine();
					} while (!isPinOK(op));
				}
				customers.get(currentAccIndex).setPin(op);
				db.save(customers);
				out.println("\n/////////////////////////////////////\n");
				out.println("!!!!!! PIN UPDATED !!!!!!\n");
				out.println("/////////////////////////////////////\n");
				break;
			case 2:
				out.println("Enter New Name: ");
				op = scan.nextLine();
				if (!isName()) {
					do {
						op = "";
						out.println("Invalid Name: Enter New Name (First [Middle] Last): ");
						op = scan.nextLine();
					} while (!isName());
				}
				customers.get(currentAccIndex).setName(op);
				db.save(customers);
				out.println("\n/////////////////////////////////////\n");
				out.println("!!!!!! NAME UPDATED !!!!!!\n");
				out.println("/////////////////////////////////////\n");
				break;
			case 3:
				out.println("Enter New Email: ");
				op = scan.nextLine();
				if (!isEmail()) {
					do {
						op = "";
						out.println("Invalid Email: Enter New Email (name@domain.loc): ");
						op = scan.nextLine();
					} while (!isEmail());
				}
				customers.get(currentAccIndex).setEmail(op);
				db.save(customers);
				out.println("\n/////////////////////////////////////\n");
				out.println("!!!!!! EMAIL UPDATED !!!!!!\n");
				out.println("/////////////////////////////////////\n");
				break;
			case 4:
				out.println("Enter New Phone: ");
				op = scan.nextLine();
				if (!isPhone()) {
					do {
						op = "";
						out.println("Invalid Phone: Enter New Phone (003530832233444): ");
						op = scan.nextLine();
					} while (!isPhone());
				}
				customers.get(currentAccIndex).setPhone(op);
				db.save(customers);
				out.println("\n/////////////////////////////////////");
				out.println("!!!!!! PHONE UPDATED !!!!!!");
				out.println("/////////////////////////////////////\n");
				break;
			}
		}
	}

	private void apply() {
		reset();
		transaction(3);
	}

	private void deposit() {
		reset();
		login();

		if (isLoggedin()) {
			transaction(2);
		}

		backup();
	}

	private void withdraw() {
		reset();
		login();

		if (isLoggedin()) {
			transaction(1);
		}

		backup();
	}

	private void transaction(int i) {
		switch (i) {
		case 1:
			out.println("Enter Amount to Withdraw...");
			op = scan.nextLine();
			try {
				double in = Double.parseDouble(op);
				double toDraw = currentAccount.getBalance() - in;
				if (toDraw < -100) {
					out.println("\n/////////////////////////////////////");
					out.println("INSUFFICIENT FUNDS: " + NumberFormat.getCurrencyInstance().format(in)
							+ " REQUESTED, \nWITH "
							+ NumberFormat.getCurrencyInstance().format(currentAccount.getBalance()) + " IN ACCOUNT!");
					out.println("/////////////////////////////////////\n");
				} else {
					if (in == 0 || in < 0) {
						out.println("\n/////////////////////////////////////\n");
						out.println("!!!! NOTHING WITHDRAWN !!!!");
						out.println("/////////////////////////////////////\n");
					} else {
						customers.get(currentAccIndex).getAccount().setBalance(-in);
						if (currentAccount.getAccountType().toString().equals("SAVINGS")) {
							out.println("\n/////////////////////////////////////");
							out.println(NumberFormat.getCurrencyInstance().format(in) + " WITHDRAW REQUEST SUBMITTED!"
									+ "\nGO TO LOCAL BRUNCH IN 7 DAYS TO PICK!");
							out.println("/////////////////////////////////////\n");
						} else {
							out.println("\n/////////////////////////////////////");
							out.println(NumberFormat.getCurrencyInstance().format(in) + " WITHDRAWN FROM ACCOUNT!");
							out.println("/////////////////////////////////////\n");
						}
					}
				}
			} catch (NumberFormatException e) {
				out.println("\n/////////////////////////////////////");
				out.println("!! NOTHING WITHDRAWN !!");
				out.println("/////////////////////////////////////\n");
			}
			break;
		case 2:
			out.println("Enter Amount to Deposit...");
			op = scan.nextLine();
			try {
				double in = Double.parseDouble(op);
				if (in > 0) {
					currentAccount.setBalance(in);
					out.println("\n/////////////////////////////////////");
					out.println(NumberFormat.getCurrencyInstance().format(in) + " DEPOSITED TO ACCOUNT!");
					out.println("/////////////////////////////////////\n");
				} else {
					out.println("\n/////////////////////////////////////");
					out.println(NumberFormat.getCurrencyInstance().format(in) + " NOT DEPOSITED TO ACCOUNT!");
					out.println("CAN'T ACCEPT LESS THAN ZERO AMOUNTS!");
					out.println("/////////////////////////////////////\n");
				}
			} catch (NumberFormatException e) {
				out.println("\n/////////////////////////////////////");
				System.out.println("!! NOTHING DEPOSITED !!");
				out.println("/////////////////////////////////////\n");
			}
			break;
		case 3:
			m.applyMenu();
			out.println("Choose Type of Account...");
			op = scan.nextLine();
			if (op.equalsIgnoreCase("1")) {
				applyFor(1);
			} else if (op.equalsIgnoreCase("2")) {
				applyFor(2);
			} else if (op.equals("3")) {
				run();
			} else {
				out.println("Invalid Option. Try Again...");
				op = "3";
				mainLogic();
			}
			break;
		default:
			break;
		}
	}

	private void applyFor(int i) {
		String name = "";
		String phone = "";
		String email = "";
		Customer cus = new Customer();
		String pin = String.valueOf(new Random().nextInt(9999) + 1000);
		String accNo = String.valueOf(new Random().nextInt(999999) + 100000);

		out.println("Enter your Full Name: ");
		op = scan.nextLine();
		if (isName()) {
			name = op;
		} else {
			op = "";
			do {
				out.println("Enter VALID Full Name: ");
				op = scan.nextLine();
			} while (!isName());

			name = op;
		}

		out.println("Enter your Phone No: ");
		op = scan.nextLine();
		if (isPhone()) {
			phone = op;
		} else {
			op = "";
			do {
				out.println("Enter VALID Phone No: ");
				op = scan.nextLine();
			} while (!isPhone());

			phone = op;
		}

		out.println("Enter your Email: ");
		op = scan.nextLine();
		if (isEmail()) {
			email = op;
		} else {
			op = "";
			do {
				out.println("Enter VALID Email: ");
				op = scan.nextLine();
			} while (!isEmail());

			email = op;
		}

		if (isPinOK(pin)) {

		} else {
			do {
				pin = String.valueOf(new Random().nextInt(9999) + 1000);
			} while (!isPinOK(pin));
		}

		if (isAccNoOK(accNo)) {

		} else {
			do {
				accNo = String.valueOf(new Random().nextInt(999999) + 100000);
			} while (!isAccNoOK(accNo));
		}

		int aaN = Integer.parseInt(accNo);
		Account acc = new Account(aaN, i);
		acc.setAccountType();
		cus.setPin(pin);
		cus.setName(name);
		cus.setEmail(email);
		cus.setPhone(phone);
		cus.setAccount(acc);
		customers.add(cus);
		db.save(customers);
		out.println("Your Account Has Been Created with PIN:" + cus.getPin() + " ! \nTo View Your Details:");
		currentCustomer = null;
		db.load();
		accInfo();
	}

	private boolean isAccNoOK(String p) {
		boolean ans = true;

		for (Customer c : customers) {
			if (c.getPin().equals(p)) {
				ans = false;
			}
		}

		return ans;
	}

	private boolean isPinOK(String p) {
		boolean ans = true;
		int acN = Integer.parseInt(p);

		for (Customer c : customers) {
			if (c.getAccount().getAccountNo() == acN) {
				ans = false;
			}
		}

		if (p.length() < 4 || p.length() > 4) {
			ans = false;
		}

		return ans;
	}

	private boolean isLoggedin() {
		if (currentAccount != null) {
			return true;
		} else {
			return false;
		}
	}

	private void login() {
		System.out.println("Enter Your PIN...");
		op = scan.nextLine();

		if (!isCustomerPin()) {
			out.println("\n/////////////////////////////////////");
			out.println("!!! ACCOUNT NOT FOUND !!!");
			out.println("/////////////////////////////////////\n");
		}
	}

	private void backup() {
		if (currentAccount != null) {
			currentCustomer.setAccount(currentAccount);
			customers.set(currentAccIndex, currentCustomer);
			db.save(customers);
		}
	}

	private void reset() {
		currentCustomer = null;
		currentAccount = null;
		currentAccIndex = -1;
	}

	private boolean isCustomerPin() {

		for (Customer c : customers) {
			if (c.getPin().equalsIgnoreCase(op)) {
				currentCustomer = c;
				currentAccount = currentCustomer.getAccount();
				currentAccIndex = customers.indexOf(c);
			}
		}

		if (currentCustomer != null && currentCustomer.getPin().equalsIgnoreCase(op)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isPhone() {
		boolean ans = false;

		String form1 = "[+][0-9]{10,}";
		String form2 = "[0]{2}[0-9]{10,}";
		Pattern p1 = Pattern.compile(form1);
		Pattern p2 = Pattern.compile(form2);
		Matcher m1 = p1.matcher(op);
		Matcher m2 = p2.matcher(op);

		if (m1.matches() || m2.matches()) {
			ans = true;
		}

		return ans;
	}

	private boolean isName() {
		boolean ans = false;

		String form1 = "([A-Z][a-z]{0,}[ ][A-Z][a-z]{0,}[ ][A-Z][a-z]{0,})|([A-Z][a-z]{0,}[ ][A-Z][a-z]{0,})";
		Pattern p1 = Pattern.compile(form1);
		Matcher m1 = p1.matcher(op);

		if (m1.matches()) {
			ans = true;
		}

		return ans;
	}

	private boolean isEmail() {
		boolean ans = false;

		String form1 = "([A-Z|a-z|0-9|_|.]{3,}[@][A-Z|a-z]{3,}[.][A-Z|a-z]{2,}[.][A-Z|a-z]{2,})|([A-Z|a-z|0-9|_|.]{3,}[@][A-Z|a-z]{3,}[.][A-Z|a-z]{2,})";
		Pattern p1 = Pattern.compile(form1);
		Matcher m1 = p1.matcher(op);

		if (m1.matches()) {
			ans = true;
		}

		return ans;
	}

	private boolean isCode() {
		boolean ans = false;

		String form1 = "[0-9]{4,8}";
		Pattern p1 = Pattern.compile(form1);
		Matcher m1 = p1.matcher(op);

		if (m1.matches()) {
			ans = true;
		}

		return ans;
	}

	private void listAll() {
		out.println("/////////////////////////////////////");
		out.println("::::::::::::: CUSTOMER LIST ::::::::::::::\n");
		for (Customer c : customers) {
			out.println(c.toString());
			out.println("--------------------------------------");
		}
		out.println("/////////////////////////////////////\n");
	}

	private void cleanUp() {
		/*
		 * List<Customer> del = new ArrayList<>(); for (Customer c : customers) { if
		 * (c.getAccount().getAccountNo() == 0 || c.getName().isBlank() ||
		 * c.getEmail().isBlank() || c.getPhone().isBlank()) { del.add(c); } }
		 * 
		 * if (!del.isEmpty()) {
		 * out.println(":::::::::::: Deleting dummy entries ::::::::\n"); for (Customer
		 * c : del) { out.println(c.toString());
		 * out.println("\n--------------------------------------\n"); }
		 * customers.removeAll(del); }
		 */

		customers.clear();
		db.save(customers);
	}

	public static void main(String[] args) {
		Main m = new Main();
		// m.listAll();
		// m.cleanUp();
		m.listAll();
		m.run();
	}

}
