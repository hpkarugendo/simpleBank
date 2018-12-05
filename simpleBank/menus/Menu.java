package simpleBank.menus;

import static java.lang.System.out;
/*
 * The Menu Class
 * By Henry Patrick Karugendo
 * Created 30th Nov 2018
 * Updated 5th Dec 2018
 * This Class is simply used to display the Menus
 */
public class Menu {

	public Menu() {
	}

	public void mainMenu() {
		out.println("------- MAIN MENU --------");
		out.println("1. WITHDRAW \n" + 
				"2. DEPOSIT \n" + 
				"3. APPLY \n" + 
				"4. MANAGE \n" + 
				"5. ACC INFO \n" +
				"6. EXIT \n");
	}

	public void manageMenu() {
		out.println("------- MANAGE MENU --------");
		out.println("1. CHANGE PIN \n" + 
				"2. CHANGE NAME \n" + 
				"3. CHANGE EMAIL \n" + 
				"4. CHANGE PHONE \n" + 
				"5. CLOSE ACCOUNT \n" +
				"6. EXIT \n");
	}

	public void applyMenu() {
		out.println("------- APPLY MENU --------");
		out.println("1. CURRENT \n" + 
				"2. SAVINGS \n" +  
				"3. EXIT \n");
	}

}
