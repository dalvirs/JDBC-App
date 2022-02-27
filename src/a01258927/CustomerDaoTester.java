/**
 * Project: A01258927 Assignment 01
 * File: CustomerReader.java
 */

package a01258927;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01258927.data.customer.Customer;
import a01258927.data.customer.CustomerDao;

/**
 * @author Sam Cirka, A00123456
 *
 */
public class CustomerDaoTester {

	private static Logger LOG = LogManager.getLogger();
	private CustomerDao customerDao;

	public CustomerDaoTester(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	public void test() {
		try {
			LOG.info("Getting the customer IDs");
			List<Long> ids = customerDao.getCustomerIds();
			LOG.info("Customer IDs: " + Arrays.toString(ids.toArray()));
			for (Long id : ids) {
				LOG.info(id);
				Customer customer = customerDao.getCustomer(id);
				LOG.info(customer);
			}
			long count = customerDao.countAllCustomers();
			LOG.info(count);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

	}

}
