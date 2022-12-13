package cpsc4620;

public class  Customer
{
	
	/*
	 * 
	 * Standard Java object class. 
	 *  
	 * This file can be modified to match your design, or you can keep it as-is.
	 * 
	 * */
	
	private int CustID;
	private String Name;
	private String Phone;
	private String Address;
	
	
	public Customer(int custID, String name, String phone) {
		CustID = custID;
		Name = name;
		Phone = phone;
	}

	public int getCustID() {
		return CustID;
	}

	public String getName() {
		return Name;
	}

	public String getPhone() {
		return Phone;
	}

	public void setCustID(int custID) {
		CustID = custID;
	}

	public void setName(String name) {
		Name = name;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}
	

	
	@Override
	public String toString() {
		return "CustID=" + CustID + " | Name= " + Name + ", Phone= " + Phone;
	}
	
	
}
