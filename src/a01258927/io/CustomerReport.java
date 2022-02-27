/**
 * Project: A01258927 Assignment 01
 * File: CustomerReader.java
 */

package a01258927.io;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import a01258927.data.customer.Customer;
import a01258927.util.Common;

/**
 * Prints a formated Customers report.
 * 
 * @author Sam Cirka, A00123456
 *
 */
public class CustomerReport {

	public static final String HORIZONTAL_LINE = "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
	public static final String HEADER_FORMAT = "%-6s %-12s %-12s %-40s %-25s %-12s %-15s %-45s%s%n";
	public static final String CUSTOMER_FORMAT = " %d %-12s %-12s %-40s %-25s %-12s %-15s %-45s%s%n";

	/**
	 * private constructor to prevent instantiation
	 */
	private CustomerReport() {
	}

	/**
	 * Print the report.
	 * 
	 * @param customers
	 */
	public static ArrayList<String> write(List<Customer> customers) {
		ArrayList<String> customerList = new ArrayList<>();
		
		customerList.add(String.format(HEADER_FORMAT, "ID", "First name", "Last name", "Street", "City", "Postal Code", "Phone", "Email", "Join Date"));
		customerList.add(HORIZONTAL_LINE);

		for (Customer customer : customers) {
			LocalDate date = customer.getJoinedDate();
			customerList.add(String.format(CUSTOMER_FORMAT, customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getStreet(),
					customer.getCity(), customer.getPostalCode(), customer.getPhone(), customer.getEmailAddress(), Common.DATE_FORMAT.format(date)));
		}
		
		return customerList;
	}
}
