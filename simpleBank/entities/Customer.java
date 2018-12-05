package simpleBank.entities;

import java.io.Serializable;

/*
 * The Customer Class
 * By Henry Patrick Karugendo
 * Created 30th Nov 2018
 * Updated 5th Dec 2018
 * This Class is used to create new Bank Customers 
 * And to display their information including their Account Details
 */
public class Customer implements Serializable {
	private String pin, name, email, phone;
	private Account account;
	public Customer() {
		this.pin = "";
		this.name = "";
		this.email = "";
		this.phone = "";
		this.account = null;
	}
	public Customer(String pin, String name, String email, String phone, Account account) {
		this.pin = pin;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.account = account;
	}

	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	@Override
	public String toString() {
		return "CUSTOMER: \nName: " + name + "\nPin: " + pin + "\nEmail: " + email + "\nPhone: " + phone + 
				"\nACCOUNT: \n" + account.toString();
	}
	
	
	

}
