package simpleBank.entities;

import java.text.NumberFormat;

import simpleBank.enums.AccountType;

/*
 * The Account Class
 * By Henry Patrick Karugendo
 * Created 30th Nov 2018
 * Updated 5th Dec 2018
 * This Class creates a bank Account for Customers
 * Has a special method to set the Account Type as Enums are not saved
 * And also displays the Account Details
 */
public class Account {
	private int accountNo;
	private double balance;
	private AccountType accountType;
	private int t;
	public Account() {
		this.accountNo = 0;
		this.balance = 0;
		this.t = 1;
	}
	public Account(int accountNo, int t) {
		this.accountNo = accountNo;
		this.balance = 0;
		this.t= t;
	}
	
	public int getT() {
		return t;
	}
	public void setT(int t) {
		this.t = t;
	}
	public int getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(int accountNo) {
		this.accountNo = accountNo;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = getBalance() + balance;
	}
	public String getAccountType() {
		return accountType.toString();
	}
	public void setAccountType() {
		if(getT() == 1) {
			this.accountType = AccountType.CURRENT;
		} else if (getT() == 2) {
			this.accountType = AccountType.SAVINGS;
		}
	}
	@Override
	public String toString() {
		NumberFormat f = NumberFormat.getCurrencyInstance();
		return "Account No: " + getAccountNo() + "\nBalance: " + f.format(getBalance()) + "\nAccount Type: " + getAccountType();
	}
	
	
}
