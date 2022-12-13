package cpsc4620;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/*
 * This file is where the front end magic happens.
 * 
 * You will have to write the functionality of each of these menu options' respective functions.
 * 
 * This file should not need to access your DB directly at all, it should make calls to the DBNinja that will do all the connections.
 * 
 * Your program shouldn't crash. Use exceptions, or if statements, or whatever it is you need to do to keep your program from breaking.
 */

public class Menu {
	public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	public static cpsc4620.DBNinja dbn = new cpsc4620.DBNinja();
	public static int MaxCustID;
	public static int MaxOrderID;
	public static int MaxPizzaID;

	static {
		try {
			MaxCustID = dbn.getMaxCustID();
			MaxOrderID = dbn.getMaxOrderID();
			MaxPizzaID = dbn.getMaxPizzaID();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	public static Scanner sc = new Scanner(System.in);


	public static void main(String[] args) throws SQLException, IOException {

		System.out.println("Welcome to Taylor's Pizzeria!");

		int menu_option = 0;

		// present a menu of options and take their selection
		PrintMenu();
		try{
			menu_option = sc.nextInt();
		}
		catch (InputMismatchException e){
			System.out.println("Invalid choice. Please enter again.");
			sc.next();
		};

		while (menu_option != 9) {
			switch (menu_option) {
			case 1:// enter order
				EnterOrder();
				break;
			case 2:// view customers
				viewCustomers();
				break;
			case 3:// enter customer
				EnterCustomer();
				break;
			case 4:// view order
				// open/closed/date
				ViewOrders();
				break;
			case 5:// mark order as complete
				MarkOrderAsComplete();
				break;
			case 6:// view inventory levels
				ViewInventoryLevels();
				break;
			case 7:// add to inventory
				AddInventory();
				break;
			case 8:// view reports
				PrintReports();
				break;
			}
			PrintMenu();
			try{
				menu_option = sc.nextInt();
			}
			catch (InputMismatchException e){
				System.out.println("Invalid choice. Please enter again.");
				sc.next();
			}
		}

	}

	public static void PrintMenu() {
		System.out.println("\n\nPlease enter a menu option:");
		System.out.println("1. Enter a new order");
		System.out.println("2. View Customers ");
		System.out.println("3. Enter a new Customer ");
		System.out.println("4. View orders");
		System.out.println("5. Mark an order as completed");
		System.out.println("6. View Inventory Levels");
		System.out.println("7. Add Inventory");
		System.out.println("8. View Reports");
		System.out.println("9. Exit\n\n");
		System.out.println("Enter your option: ");
	}

	// allow for a new order to be placed
	public static void EnterOrder() throws SQLException, IOException 
	{
		/*
		 * EnterOrder should do the following:
		 * Ask if the order is for an existing customer -> If yes, select the customer. If no -> create the customer (as if the menu option 2 was selected).
		 * 
		 * Ask if the order is delivery, pickup, or dinein (ask for orderType specific information when needed)
		 * 
		 * Build the pizza (there's a function for this)
		 * 
		 * ask if more pizzas should be be created. if yes, go back to building your pizza. 
		 * 
		 * Apply order discounts as needed (including to the DB)
		 * 
		 * apply the pizza to the order (including to the DB)
		 * 
		 * return to menu
		 */

		System.out.println("Is this order for an existing customer? Answer y/n:");
		String option = sc.next();
		int id = 0;

		if(option.equals("y")){
			System.out.println("Here's a list of current customers:");
			viewCustomers();
			System.out.println("Which customer is this order for? Please enter CustID number: ");
			try{
				id = sc.nextInt();
			}
			catch (InputMismatchException e){
				System.out.println("Invalid choice. Please enter again.");
				sc.next();
				EnterOrder();
			}
		}
		else{
			EnterCustomer();
			id = MaxCustID;
		}

		System.out.println("Is this order for \n1.) Dine In\n2.) Pick-up\n3.) Delivery\nPlease enter the number of your choice");
		int ot = 0;
		try{
			ot = sc.nextInt();
		}
		catch (InputMismatchException e){
			System.out.println("Invalid choice. Please enter again.");
			sc.next();
			EnterOrder();
		}
		String OrderType = null;

		++MaxOrderID;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String dateNow = dateFormat.format(date);
		
		Order o = null;

		if(ot == 1){
			System.out.println("What is the dine in table number?");
			int tableNo = sc.nextInt();
			o = new DineinOrder(MaxOrderID, id, dateNow, 0.0, 0.0, 0, tableNo);
			dbn.addOrder(o);
		}
		else if (ot == 2){
			System.out.println("Has the order been picked up? Input 1 for yes 0 for no");
			int isPickedUp = sc.nextInt();
			o = new PickupOrder(MaxOrderID, id, dateNow, 0.0, 0.0, isPickedUp, 0);
			dbn.addOrder(o);
		}
		else if (ot == 3){
			System.out.println("What is the customer address?");
			String address = reader.readLine();
			o = new DeliveryOrder(MaxOrderID, id, dateNow, 0.0, 0.0, 0, address);
			dbn.addOrder(o);
		}
		else
			System.out.println("Invalid choice. Please enter again.");



		int pizzaOption = 0;

		while(pizzaOption != -1){
		cpsc4620.Pizza p = buildPizza(MaxOrderID);

		o.addPizza(p);

		System.out.println("Enter -1 to stop adding pizzas. Enter any other number to continue adding more pizzas to the order");
			try{
				pizzaOption = sc.nextInt();
			}
			catch (InputMismatchException e){
				System.out.println("Invalid choice. Please enter again.");
				sc.next();
				EnterOrder();
			}
	}
		double TotalOrderPrice = 0.0;
		double TotalOrderCost = 0.0;
		for(int x=0; x<o.getPizzaList().size(); x++) {
			TotalOrderPrice += o.getPizzaList().get(x).getCustPrice();
			TotalOrderCost += o.getPizzaList().get(x).getBusPrice();
		}
		o.setCustPrice(TotalOrderPrice);
		o.setBusPrice(TotalOrderCost);

		dbn.updatePrice(o);

		int discountOption = 0;
		System.out.println("Do you want to add discounts to this order? Enter y/n");
		option = reader.readLine();
		if(option.equals("y")){
			System.out.println("Getting discount list...");
			while (discountOption != -1) {
				try {
					ArrayList<cpsc4620.Discount> d = new ArrayList<>(dbn.getDiscountList());
					for (int x = 0; x < d.size(); x++) {
						System.out.println("DiscountID = " + d.get(x).getDiscountID() + " | " + d.get(x).getDiscountName() + ", Amount = " + d.get(x).getAmount() + ", isPercent = " + d.get(x).isPercent());
					}
					System.out.println("Which Order Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding discounts:");
					discountOption = sc.nextInt();
					if (discountOption != -1) {
						o.addDiscount(d.get(discountOption-1));
						dbn.useOrderDiscount(o, d.get(discountOption-1));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Finished adding order...Returning to menu...");
	}
	
	
	public static void viewCustomers() {
		/* Simply print out all of the customers from the database. */

		try {
			ArrayList<cpsc4620.Customer> cl = new ArrayList<>(dbn.getCustomerList());
		for(int x = 0; x<cl.size(); x++) {
			System.out.println("CustID = " + cl.get(x).getCustID() + " | Name = " + cl.get(x).getName() + ", Phone No = " + cl.get(x).getPhone());
		}
		} catch (SQLException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	

	// Enter a new customer in the database
	public static void EnterCustomer() throws SQLException, IOException 
	{

		System.out.println("Please enter the customer name (First name <space> Last name):");
		String CustName = reader.readLine();
		System.out.println("What is the customer's phone number? (###-###-####) (please include with dash)");
		String CustPNo = reader.readLine();

		cpsc4620.Customer c = new cpsc4620.Customer(++MaxCustID, CustName, CustPNo);
		dbn.addCustomer(c);
	}

	// View any orders that are not marked as completed
	public static void ViewOrders() throws SQLException, IOException 
	{
	/*
	 * This should be subdivided into two options: print all orders (using simplified view) and print all orders (using simplified view) since a specific date.
	 * 
	 * Once you print the orders (using either sub option) you should then ask which order I want to see in detail
	 * 
	 * When I enter the order, print out all the information about that order, not just the simplified view.
	 * 
	 */
		System.out.println("Would you like to: \n(a) display all orders \n(b) display orders since a specific date\n");
		String option  = sc.next();
		ArrayList<cpsc4620.Order> o = new ArrayList<>();

		if (option.equals("a")){
			o = dbn.getCurrentOrders();
		}
		else if (option.equals("b")){
			System.out.println("What is the date you want to restrict by? (Format = YYYY-MM-DD)");
			String fromDate = reader.readLine();
			o = dbn.getCurrentOrders(fromDate);
		}
		for(int x = 0; x<o.size(); x++){
			System.out.println(o.get(x).toSimplePrint());
		}

		System.out.println("Which order would you like to see in detail? Enter the number:");
		int choice = sc.nextInt();
		o = dbn.getCurrentOrders();
		for(int x = 0; x<o.size(); x++){
			Order ord = o.get(x);
			if(ord.getOrderID() == choice) {
				if (ord instanceof DineinOrder) {
					DineinOrder d = (DineinOrder) ord;
					System.out.println(d.toString());
				}
				else if (ord instanceof PickupOrder) {
					PickupOrder p = (PickupOrder) ord;
					System.out.println(p.toString());
				}
				else if (ord instanceof DeliveryOrder) {
					DeliveryOrder dor = (DeliveryOrder) ord;
					System.out.println(dor.toString());
				}
			}
		}
	}

	
	// When an order is completed, we need to make sure it is marked as complete
	public static void MarkOrderAsComplete() throws SQLException, IOException 
	{
		/* When this function is called, you should print all of the orders marked as complete
		 * and allow the user to choose which of the incomplete orders they wish to mark as complete */

		ArrayList<cpsc4620.Order> o = new ArrayList<>(dbn.getCurrentOrders());
		for(int x = 0; x<o.size(); x++) {
			if (o.get(x).getIsComplete() == 0)
				System.out.println(o.get(x).toSimplePrint());
		}
			System.out.println("Which order would you like to mark as complete? Enter the OrderID");
			int choice = sc.nextInt();
			dbn.CompleteOrder(o.get(choice-1));
	}

	// See the list of inventory and it's current level
	public static void ViewInventoryLevels() throws SQLException, IOException 
	{
		//print the inventory. I am really just concerned with the ID, the name, and the current inventory

		dbn.printInventory();
		
	}

	// Select an inventory item and add more to the inventory level to re-stock the
	// inventory
	public static void AddInventory() throws SQLException, IOException 
	{
		/*
		 * This should print the current inventory and then ask the user which topping they want to add more to and how much to add
		 */

		dbn.printInventory();

		System.out.println("Which topping do you want to add inventory to? Enter the number");
		int choice = sc.nextInt();
		System.out.println("How many units would you like to add?");
		int units = sc.nextInt();
		dbn.AddToInventory(choice, units);
	}

	// A function that builds a pizza. Used in our add new order function
	public static cpsc4620.Pizza buildPizza(int orderID) throws SQLException, IOException
	{
		
		/*
		 * This is a helper function for first menu option.
		 * 
		 * It should ask which size pizza the user wants and the crustType.
		 * 
		 * Once the pizza is created, it should be added to the DB.
		 * 
		 * We also need to add toppings to the pizza. (Which means we not only need to add toppings here, but also our bridge table)
		 * 
		 * We then need to add pizza discounts (again, to here and to the database)
		 * 
		 * Once the discounts are added, we can return the pizza
		 */

		System.out.println("Let's build a pizza!");

		System.out.println("What size is the pizza? \n1.) Small \n2.) Medium \n3.) Large \n4.) X-Large \nPlease enter the corresponding number");
		int size = sc.nextInt();
		String psize = null;
		if(size == 1) psize = "Small";
		else if(size == 2) psize = "Medium";
		else if(size == 3) psize = "Large";
		else if(size == 4) psize = "X-Large";

		System.out.println("What crust for this pizza? \n1.) Thin \n2.) Original \n3.) Pan \n4.) Gluten-free \nPlease enter the corresponding number");
		int crust = sc.nextInt();
		String pcrust = null;
		if(crust == 1) pcrust = "Thin";
		else if(crust == 2) pcrust = "Original";
		else if(crust == 3) pcrust = "Pan";
		else if(crust == 4) pcrust = "Gluten-free";

		double bcp = dbn.getBaseCustPrice(psize, pcrust);
		double bbp = dbn.getBaseBusPrice(psize, pcrust);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String dateNow = dateFormat.format(date);

		cpsc4620.Pizza p = new cpsc4620.Pizza(++MaxPizzaID, psize, pcrust, orderID, "Completed", dateNow, bcp, bbp);

		dbn.addPizza(p);

		int toppingOption = 0;
		boolean isDouble = false;
		ArrayList<cpsc4620.Topping> topping = new ArrayList<>(dbn.getInventory());

		while(toppingOption != -1) {
			dbn.printInventory();
			System.out.println("Which topping do you want to add? Please enter the ToppingID. Enter -1 to stop adding toppings:");
			toppingOption = sc.nextInt();
			if (toppingOption != -1) {
				System.out.println("Do you want to add extra topping? Enter y/n");
				String option = reader.readLine();
				if (option.equals("y")) {
					isDouble = true;
				}
				dbn.useTopping(p, topping.get(toppingOption-1), isDouble);
				p.addToppings(topping.get(toppingOption-1), isDouble);
			}
		}

		System.out.println("Do you want to add discounts to this pizza? Enter y/n");
		String option  = reader.readLine();

		while(option.equals("y")) {
			int discountOption = 0;
			System.out.println("Getting discount list...");
			while (discountOption != -1) {
				try {
					ArrayList<cpsc4620.Discount> d = new ArrayList<>(dbn.getDiscountList());
					for (int x = 0; x < d.size(); x++) {
						System.out.println("DiscountID = " + d.get(x).getDiscountID() + " | " + d.get(x).getDiscountName() + ", Amount = " + d.get(x).getAmount() + ", isPercent = " + d.get(x).isPercent());
					}
					System.out.println("Which Pizza Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding discounts:");
					discountOption = sc.nextInt();
					if (discountOption != -1) {
						p.addDiscounts(d.get(discountOption-1));
						dbn.usePizzaDiscount(p, d.get(discountOption-1));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Do you want to add more discounts to this Pizza? Enter y/n");
			option  = reader.readLine();
		}
		return p;
	}
	
	
	public static void PrintReports() throws SQLException, NumberFormatException, IOException
	{
		/*
		 * This function calls the DBNinja functions to print the three reports.
		 * 
		 * You should ask the user which report to print
		 */
		System.out.println("Which report do you wish to print? Enter \n1.) Topping Popularity \n2.) ProfitByPizza \n3.) ProfitByOrderType ");
		int option = sc.nextInt();
		if(option == 1)
			dbn.printToppingPopReport();
		else if(option == 2)
			dbn.printProfitByPizzaReport();
		else if(option == 3)
			dbn.printProfitByOrderType();
	}
}
