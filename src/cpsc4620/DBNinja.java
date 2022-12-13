package cpsc4620;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

/*
 * This file is where most of your code changes will occur You will write the code to retrieve
 * information from the database, or save information to the database
 * 
 * The class has several hard coded static variables used for the connection, you will need to
 * change those to your connection information
 * 
 * This class also has static string variables for pickup, delivery and dine-in. If your database
 * stores the strings differently (i.e "pick-up" vs "pickup") changing these static variables will
 * ensure that the comparison is checking for the right string in other places in the program. You
 * will also need to use these strings if you store this as boolean fields or an integer.
 * 
 * 
 */

/**
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
	private static Connection conn;

	// Change these variables to however you record dine-in, pick-up and delivery,
	// and sizes and
	// crusts
	public final static String pickup = "Pickup";
	public final static String delivery = "Delivery";
	public final static String dine_in = "Dine In";

	public final static String size_s = "Small";
	public final static String size_m = "Medium";
	public final static String size_l = "Large";
	public final static String size_xl = "X-Large";

	public final static String crust_thin = "Thin";
	public final static String crust_orig = "Original";
	public final static String crust_pan = "Pan";
	public final static String crust_gf = "Gluten-free";

	/**
	 * This function will handle the connection to the database
	 * 
	 * @return true if the connection was successfully made
	 * @throws SQLException
	 * @throws IOException
	 */
	private static boolean connect_to_db() throws SQLException, IOException {

		try {
			conn = cpsc4620.DBConnector.make_connection();
			return true;
		} catch (SQLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	/**
	 *
	 * @param o order that needs to be saved to the database
	 * @throws SQLException
	 * @throws IOException
	 * @requires o is not NULL. o's ID is -1, as it has not been assigned yet. The
	 *           pizzas do not exist in the database yet, and the topping inventory
	 *           will allow for these pizzas to be made
	 * @ensures o will be assigned an id and added to the database, along with all
	 *          of it's pizzas. Inventory levels will be updated appropriately
	 */
	public static void addOrder(cpsc4620.Order o) throws SQLException, IOException {
		connect_to_db();
		/*
		 * add code to add the order to the DB. Remember that we're not just
		 * adding the order to the order DB table, but we're also recording
		 * the necessary data for the delivery, dinein, and pickup tables
		 */
		Statement stmt = conn.createStatement();


		if(o instanceof DineinOrder) {
			stmt.executeUpdate("insert into the_order values(" + o.getOrderID() + ",'" + o.getDate() + "'," + o.getCustID() + ", " + o.getCustPrice() + ", " + o.getBusPrice() + ",'" + o.getOrderType() + "'," + o.getIsComplete() + ");");
			stmt.executeUpdate("insert into dine_in values(" + o.getOrderID() + "," + ((DineinOrder) o).getTableNum() + ");");
		}

		if(o instanceof PickupOrder) {
			stmt.executeUpdate("insert into the_order values(" + o.getOrderID() + ",'" + o.getDate() + "'," + o.getCustID() + ", " + o.getCustPrice() + ", " + o.getBusPrice() + ",'" + o.getOrderType() + "'," + o.getIsComplete() + ");");
			stmt.executeUpdate("insert into pickup values(" + o.getOrderID() + ");");
		}

		if(o instanceof DeliveryOrder) {
			stmt.executeUpdate("insert into the_order values(" + o.getOrderID() + ",'" + o.getDate() + "'," + o.getCustID() + ", " + o.getCustPrice() + ", " + o.getBusPrice() + ",'" + o.getOrderType() + "'," + o.getIsComplete() + ");");
			stmt.executeUpdate("insert into delivery values(" + o.getOrderID() + ",'" + ((DeliveryOrder) o).getAddress() + "');");
		}

		conn.close();
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void addPizza(cpsc4620.Pizza p) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Add the code needed to insert the pizza into the database.
		 * Keep in mind adding pizza discounts to that bridge table and 
		 * instance of topping usage to that bridge table if you have't accounted
		 * for that somewhere else.
		 */

		Statement stmt = conn.createStatement();

		stmt.executeUpdate("insert into pizza values(" + p.getPizzaID() + ",'" + p.getSize() + "','" + p.getCrustType() + "','" + p.getPizzaState() + "','" + p.getPizzaDate() + "'," + p.getCustPrice() + "," + p.getBusPrice() + "," + p.getOrderID() + ")");


		conn.close();
	}

	public static int getMaxCustID() throws SQLException, IOException
	{
		connect_to_db();
		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("select max(CustomerID) as CustomerID from customer");
		rs.next();
		int res = rs.getInt("CustomerID");

		conn.close();
		return res;
	}

	public static int getMaxOrderID() throws SQLException, IOException
	{
		connect_to_db();
		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("select max(OrderID) as OrderID from the_order");
		rs.next();
		int res = rs.getInt("OrderID");

		conn.close();
		return res;
	}

	public static int getMaxPizzaID() throws SQLException, IOException
	{
		connect_to_db();
		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("select max(PizzaID) as PizzaID from pizza");
		rs.next();
		int res = rs.getInt("PizzaID");

		conn.close();
		return res;
	}


	
	public static void useTopping(cpsc4620.Pizza p, cpsc4620.Topping t, boolean isDoubled) throws SQLException, IOException //this function will update toppings inventory in SQL and add entities to the Pizzatops table. Pass in the p pizza that is using t topping
	{
		connect_to_db();
		/*
		 * This function should 2 two things.
		 * We need to update the topping inventory every time we use t topping (accounting for extra toppings as well)
		 * and we need to add that instance of topping usage to the pizza-topping bridge if we haven't done that elsewhere
		 * Ideally, you should't let toppings go negative. If someone tries to use toppings that you don't have, just print
		 * that you've run out of that topping.
		 */

		int ToppingAmount;

		if(isDoubled)
			ToppingAmount = 2;
		else
			ToppingAmount = 1;

		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("select ToppingCurrentInventory - " + ToppingAmount + " as ToppingCurrentInventory from topping where ToppingID = " + t.getTopID());

		rs.next();

		if(rs.getInt("ToppingCurrentInventory")<0){
			System.out.println("Topping insufficient - please select another");
		}
		else{
			stmt.executeUpdate("update topping set ToppingCurrentInventory = ToppingCurrentInventory - " + ToppingAmount + " where ToppingID = " + t.getTopID());

			stmt.executeUpdate("insert into pizza_topping values (" + p.getPizzaID() + ", " + t.getTopID() + ", " + isDoubled + ")");
		}

		conn.close();
	}
	

	public static void updatePrice(cpsc4620.Order o)throws SQLException, IOException
	{
		connect_to_db();
		Statement stmt = conn.createStatement();

		stmt.executeUpdate("update the_order set TotalOrderPrice = " + o.getCustPrice() + ", TotalOrderCost = " + o.getBusPrice() + "where OrderID = " + o.getOrderID() + ";");
		conn.close();
	}

	public static void usePizzaDiscount(cpsc4620.Pizza p, cpsc4620.Discount d) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Helper function I used to update the pizza-discount bridge table. 
		 * You might use this, you might not depending on where / how to want to update
		 * this table
		 */
		Statement stmt = conn.createStatement();

		stmt.executeUpdate("insert into discount_pizza values(" + p.getPizzaID() + "," + d.getDiscountID() + ")");
		stmt.executeUpdate("update pizza set PizzaPrice = " + p.getCustPrice() + ", PizzaCost = " + p.getBusPrice()+ " where PizzaID = " + p.getPizzaID() + ";");

		conn.close();
	}
	
	public static void useOrderDiscount(cpsc4620.Order o, cpsc4620.Discount d) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Helper function I used to update the pizza-discount bridge table. 
		 * You might use this, you might not depending on where / how to want to update
		 * this table
		 */
		Statement stmt = conn.createStatement();

		stmt.executeUpdate("insert into discount_order values(" + o.getOrderID() + "," + d.getDiscountID() + ")");
		stmt.executeUpdate("update the_order set TotalOrderPrice = " + o.getCustPrice() + ", TotalOrderCost = " + o.getBusPrice() + " where OrderID = " + o.getOrderID() + ";");

		conn.close();
	}
	


	
	public static void addCustomer(cpsc4620.Customer c) throws SQLException, IOException {
		connect_to_db();
		/*
		 * This should add a customer to the database
		 */

		String query = "insert into customer (CustomerID, CustomerName, CustomerPhoneNumber) values (?, ?, ?)";

		PreparedStatement ps = conn.prepareStatement(query);
		ps.setInt(1, c.getCustID());
		ps.setString(2, c.getName());
		ps.setString(3, c.getPhone());

		ps.executeUpdate();

		conn.close();
	}


	
	public static void CompleteOrder(cpsc4620.Order o) throws SQLException, IOException {
		connect_to_db();
		/*
		 * add code to mark an order as complete in the DB. You may have a boolean field
		 * for this, or maybe a completed time timestamp. However you have it.
		 */

		Statement stmt = conn.createStatement();

		stmt.executeUpdate("update the_order set OrderState = 1 where OrderID = " + o.getOrderID());

		conn.close();
	}


	
	
	public static void AddToInventory(int id, int toAdd) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Adds toAdd amount of topping to topping t.
		 */

		Statement stmt = conn.createStatement();

		stmt.executeUpdate("update topping set ToppingCurrentInventory = ToppingCurrentInventory + " + toAdd + " where ToppingID = " + id);

		conn.close();
	}

	

	public static void printInventory() throws SQLException, IOException {
		connect_to_db();
		
		/*
		 * I used this function to PRINT (not return) the inventory list.
		 * When you print the inventory (either here or somewhere else)
		 * be sure that you print it in a way that is readable.
		 * 
		 * The topping list should also print in alphabetical order
		 */
		
		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("select ToppingID, ToppingName, ToppingCurrentInventory from topping order by ToppingName");

		System.out.println("ID\tName\tCurINVT");

		while(rs.next()){
			System.out.println(rs.getInt("ToppingID") + "\t" + rs.getString("ToppingName") + "\t" + rs.getInt("ToppingCurrentInventory"));
		}

		conn.close();
	}

	public static ArrayList<cpsc4620.Topping> getInventory() throws SQLException, IOException {
		connect_to_db();
		/*
		 * This function actually returns the toppings. The toppings
		 * should be returned in alphabetical order if you don't
		 * plan on using a printInventory function
		 */

		ArrayList<cpsc4620.Topping> tops = new ArrayList<>();
		connect_to_db();

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from topping");
		while (rs.next()){
			int TopID = rs.getInt("ToppingID");
			String TopName = rs.getString("ToppingName");
			double PerAMT = rs.getDouble("ToppingAmountSmall");
			double MedAMT = rs.getDouble("ToppingAmountMedium");
			double LgAMT = rs.getDouble("ToppingAmountLarge");
			double XLAMT = rs.getDouble("ToppingAmountXLarge");
			double CustPrice = rs.getDouble("ToppingPrice");
			double BusPrice = rs.getDouble("ToppingCost");
			int MinINVT = rs.getInt("ToppingMinimumInventory");
			int CurINVT = rs.getInt("ToppingCurrentInventory");
			cpsc4620.Topping t = new cpsc4620.Topping(TopID, TopName, PerAMT, MedAMT, LgAMT, XLAMT, CustPrice, BusPrice, MinINVT, CurINVT);
			tops.add(t);
		}

		conn.close();
		return tops;
	}


	public static ArrayList<cpsc4620.Order> getCurrentOrders() throws SQLException, IOException {
		connect_to_db();
		/*
		 * This function should return an arraylist of all of the orders.
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 * 
		 * Also, like toppings, whenever we print out the orders using menu function 4 and 5
		 * these orders should print in order from newest to oldest.
		 */

		ArrayList<cpsc4620.Order> orders = new ArrayList<>();

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from the_order order by OrderDate");

		Statement stmt2 = conn.createStatement();
		ResultSet rs2;

		while (rs.next()){
			int OrderID = rs.getInt("OrderID");
			int CustID = rs.getInt("CustomerID");
			String OrderType = rs.getString("OrderType");
			String Date = rs.getString("OrderDate");
			double CustPrice = rs.getDouble("TotalOrderPrice");
			double BusPrice = rs.getDouble("TotalOrderCost");
			int isComplete = rs.getInt("OrderState");

			int oi = rs.getInt("OrderID");

			if(OrderType.equals("Dine In")){
				rs2 = stmt2.executeQuery("select * from dine_in where OrderID = " + oi);
				rs2.next();
				int tableNum = rs2.getInt("DineInTable");
				DineinOrder di = new DineinOrder(OrderID, CustID, Date, CustPrice, BusPrice, isComplete, tableNum);
				orders.add(di);
			}
			else if(OrderType.equals("Pickup")){
				rs2 = stmt2.executeQuery("select * from pickup where OrderID = " + oi);
				rs2.next();
				PickupOrder p = new PickupOrder(OrderID, CustID, Date, CustPrice, BusPrice, 1, isComplete);
				orders.add(p);
			}
			else if(OrderType.equals("Delivery")){
				rs2 = stmt2.executeQuery("select * from delivery where OrderID = " + oi);
				rs2.next();
				String address = rs2.getString("CustomerAddress");
				DeliveryOrder dor = new DeliveryOrder(OrderID, CustID, Date, CustPrice, BusPrice, isComplete, address);
				orders.add(dor);
			}
		}

		conn.close();
		return orders;
	}

	public static ArrayList<cpsc4620.Order> getCurrentOrders(String fromDate) throws SQLException, IOException {
		connect_to_db();

		ArrayList<cpsc4620.Order> orders = new ArrayList<>();

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from the_order where OrderDate > '" + fromDate + "' order by OrderDate");

		Statement stmt2 = conn.createStatement();
		ResultSet rs2;

		while (rs.next()){
			int OrderID = rs.getInt("OrderID");
			int CustID = rs.getInt("CustomerID");
			String OrderType = rs.getString("OrderType");
			String Date = rs.getString("OrderDate");
			double CustPrice = rs.getDouble("TotalOrderPrice");
			double BusPrice = rs.getDouble("TotalOrderCost");
			int isComplete = rs.getInt("OrderState");

			int oi = rs.getInt("OrderID");

			if(OrderType.equals("Dine In")){
				rs2 = stmt2.executeQuery("select * from dine_in where OrderID = " + oi);
				rs2.next();
				int tableNum = rs2.getInt("DineInTable");
				DineinOrder di = new DineinOrder(OrderID, CustID, Date, CustPrice, BusPrice, isComplete, tableNum);
				orders.add(di);
			}
			else if(OrderType.equals("Pickup")){
				rs2 = stmt2.executeQuery("select * from pickup where OrderID = " + oi);
				rs2.next();
				PickupOrder p = new PickupOrder(OrderID, CustID, Date, CustPrice, BusPrice, 1, isComplete);
				orders.add(p);
			}
			else if(OrderType.equals("Delivery")){
				rs2 = stmt2.executeQuery("select * from delivery where OrderID = " + oi);
				rs2.next();
				String address = rs2.getString("CustomerAddress");
				DeliveryOrder dor = new DeliveryOrder(OrderID, CustID, Date, CustPrice, BusPrice, isComplete, address);
				orders.add(dor);
			}
		}

		conn.close();
		return orders;
	}



	
	
	
	public static double getBaseCustPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		double bp = 0.0;
		// add code to get the base price (for the customer) for that size and crust pizza Depending on how
		// you store size & crust in your database, you may have to do a conversion

		String query = "select BasePrice from base_price where PizzaSize = ? and PizzaCrust = ?";

		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, size);
		ps.setString(2, crust);

		ResultSet rs = ps.executeQuery();
		rs.next();

		bp = rs.getDouble("BasePrice");

		conn.close();
		return bp;
	}

	
	public static double getBaseBusPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		double bp = 0.0;
		// add code to get the base cost (for the business) for that size and crust pizza Depending on how
		// you store size and crust in your database, you may have to do a conversion

		String query = "select BaseCost from base_price where PizzaSize = ? and PizzaCrust = ?";

		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, size);
		ps.setString(2, crust);

		ResultSet rs = ps.executeQuery();
		rs.next();

		bp = rs.getDouble("BaseCost");
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		conn.close();
		return bp;
	}

	public static String getCustomerName(int CustID) throws SQLException, IOException
	{
		/*
		 *This is a helper function I used to fetch the name of a customer
		 *based on a customer ID. It actually gets called in the Order class
		 *so I'll keep the implementation here. You're welcome to change
		 *how the order print statements work so that you don't need this function.
		 */
		connect_to_db();
		String ret = "";
		String query = "Select CustomerName From customer WHERE CustomerID= " + CustID + ";";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		while(rs.next())
		{
			ret = rs.getString(1);
		}
		conn.close();
		return ret;
	}

	
	public static ArrayList<cpsc4620.Discount> getDiscountList() throws SQLException, IOException {
		ArrayList<cpsc4620.Discount> discs = new ArrayList<>();
		connect_to_db();
		//returns a list of all the discounts.

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from discount");
		while (rs.next()){
			int DiscountID = rs.getInt("DiscountID");
			String DiscountName = rs.getString("DiscountName");
			double Amount = rs.getDouble("DiscountAmount");
			boolean isPercent = rs.getBoolean("isPercent");
			cpsc4620.Discount d = new cpsc4620.Discount(DiscountID, DiscountName, Amount, isPercent);
			discs.add(d);
		}

		conn.close();
		return discs;
	}


	public static ArrayList<cpsc4620.Customer> getCustomerList() throws SQLException, IOException {
		ArrayList<cpsc4620.Customer> custs = new ArrayList<>();
		connect_to_db();
		/*
		 * return an arrayList of all the customers. These customers should
		 *print in alphabetical order, so account for that as you see fit.
		*/
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from customer");
		while (rs.next()){
			int CustID = rs.getInt("CustomerID");
			String Name = rs.getString("CustomerName");
			String Phone = rs.getString("CustomerPhoneNumber");
			cpsc4620.Customer c = new cpsc4620.Customer(CustID, Name, Phone);
			custs.add(c);
	}
		conn.close();
		return custs;
	}

	
	public static void printToppingPopReport() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ToppingPopularity view. Remember that these views
		 * need to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * I'm not picky about how they print (other than that it should
		 * be in alphabetical order by name), just make sure it's readable.
		 */

		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT * from ToppingPopularity;");

		System.out.println("Topping\t\t" + "ToppingCount");
		while(rs.next()){
			System.out.println(rs.getString("Topping") + "\t\t" + rs.getInt("ToppingCount"));
		}
		
		conn.close();
	}
	
	public static void printProfitByPizzaReport() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ProfitByPizza view. Remember that these views
		 * need to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * I'm not picky about how they print, just make sure it's readable.
		 */
		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT * from ProfitByPizza;");

		System.out.println("PizzaSize\t\t" + "PizzaCrust\t\t" + "Profit\t\t" + "LastOrderDate");
		while(rs.next()){
			System.out.println(rs.getString("PizzaSize") + "\t\t" + rs.getString("PizzaCrust") + "\t\t" + rs.getDouble("Profit") + "\t\t" + rs.getDate("LastOrderDate"));
		}
		
		conn.close();
	}
	
	public static void printProfitByOrderType() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ProfitByOrderType view. Remember that these views
		 * need to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * I'm not picky about how they print, just make sure it's readable.
		 */
		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT * from ProfitByOrderType;");

		System.out.println("CustomerType\t" + "OrderMonth\t" + "TotalOrderPrice\t" + "TotalOrderCost\t" + "Profit");
		while(rs.next()){
			System.out.println(rs.getString("CustomerType") + "\t\t" + rs.getString("OrderMonth") + "\t\t" + rs.getDouble("TotalOrderPrice") + "\t\t" + rs.getDouble("TotalOrderCost") + "\t\t" + rs.getDouble("Profit"));
		}
		conn.close();
	}
}
