package simpleBank.storage;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import simpleBank.entities.Account;
import simpleBank.entities.Customer;

public class Database {
	private final File db = new File("cabinet/data.xml");

	public Database() {
		
	}
	
	public void save(List<Customer> customers) {
		FileOutputStream fos;
		XMLEncoder encoder;
		try {
			fos = new FileOutputStream(db);
			encoder = new XMLEncoder(fos);
			encoder.writeObject(customers);
			encoder.close();
			fos.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR ::: FILE NOT FOUND!");
		} catch (IOException e) {
			System.out.println("ERROR ::: CAN'T WRITE TO FILE!");
		}
	}
	
	public List<Customer> load(){
		List<Customer> cs = new ArrayList<>();
		List<Customer> css = new ArrayList<>();
		
		if(db.length() > 1) {
			FileInputStream fis;
			XMLDecoder decoder;
			try {
				fis = new FileInputStream(db);
				decoder = new XMLDecoder(fis);
				css = (List<Customer>) decoder.readObject();
				decoder.close();
				fis.close();
			} catch (FileNotFoundException e) {
				System.out.println("ERROR ::: FILE NOT FOUND!");
			} catch (IOException e) {
				System.out.println("ERROR ::: CAN'T READ FROM FILE!");
			}
		}
		
		for(Customer c: css) {
			Account na = new Account(c.getAccount().getAccountNo(), c.getAccount().getT());
			na.setAccountType();
			na.setBalance(c.getAccount().getBalance());
			Customer nc = new Customer(c.getPin(), c.getName(), c.getEmail(), c.getPhone(), na);
			cs.add(nc);
		}
		
		return cs;
	}
	
	
}
