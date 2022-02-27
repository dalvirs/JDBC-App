/**
 * Project: A01258927 Assignment 01
 * File: CustomerReader.java
 */

package a01258927.data.customer;

import java.util.Comparator;

/**
 * @author Sam Cirka, A00123456
 *
 */
public class CustomerSorter implements Comparator<Customer> {
		@Override
		public int compare(Customer customer1, Customer customer2) {
			return customer1.getJoinedDate().compareTo(customer2.getJoinedDate());
		}
	}

