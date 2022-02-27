/**
 * Project: A01258927 Assignment 01
 * File: CustomerReader.java
 */

package a01258927;

/**
 * @author Dalvir Chiount, A01258927
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import a01258927.data.customer.Customer;
import a01258927.data.customer.CustomerDao;
import a01258927.data.customer.CustomerSorter;
import a01258927.db.Database;
import a01258927.io.CustomerReport;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;
import java.awt.Button;
import java.awt.Font;

public class AssignmentFrame {

	private JFrame frameAssignment;
	private JScrollPane scrollPane;
	private Button button;
	private JCheckBoxMenuItem byJoinDate;
	private CustomerDao customerDao;
	private static final String DB_PROPERTIES_FILENAME = "db.properties";

	/**
	 * Create the application.
	 */
	public AssignmentFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	public JFrame getFrame() {
		return frameAssignment;
	}

	private void initialize() {

		Database db;
		try {
			db = connect();
			customerDao = loadCustomers(db);
		} catch (IOException | SQLException | ApplicationException e) {
			e.printStackTrace();
		}

		frameAssignment = new JFrame();
		frameAssignment.setTitle("A01258927 Assignment 01");
		frameAssignment.setBounds(100, 100, 567, 594);
		frameAssignment.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frameAssignment.setJMenuBar(menuBar);

		JMenu file = fileMenu();
		JMenu customer = customerMenu();
		JMenu help = helpMenu();

		menuBar.add(file);
		menuBar.add(customer);
		menuBar.add(help);

		frameAssignment.getContentPane().setLayout(new MigLayout("", "[667px]", "[502px][]"));

	}

	private JMenu fileMenu() {
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

		JMenuItem Drop = new JMenuItem("Drop");
		Drop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure?", "WARNING",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					try {
						customerDao.drop();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "Table droped. Exiting Aplication");
					System.exit(0);
				}
			}
		});
		file.add(Drop);

		file.addSeparator();

		JMenuItem Quit = new JMenuItem("Quit");
		Quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
		Quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		file.add(Quit);
		return file;
	}

	private JMenu customerMenu() {
		JMenu customer = new JMenu("Customers");
		customer.setMnemonic(KeyEvent.VK_C);

		JMenuItem count = new JMenuItem("Count");
		count.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JOptionPane.showMessageDialog(null, customerDao.countAllCustomers());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		customer.add(count);

		byJoinDate = new JCheckBoxMenuItem("By Join Date");
		customer.add(byJoinDate);

		customer.addSeparator();

		JMenuItem list = new JMenuItem("List");
		list.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (scrollPane != null && button != null) {
					frameAssignment.remove(scrollPane);
					frameAssignment.remove(button);
					frameAssignment.revalidate();
				}

				ArrayList<Customer> customerList = new ArrayList<>();
				ArrayList<String> customerListString = new ArrayList<>();

				{
					try {
						for (Long id : customerDao.getCustomerIds())
							customerList.add(customerDao.getCustomer(id));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

				if (byJoinDate.isSelected()) {
					customerListString = CustomerReport.write(sortedList(customerList));
				} else {
					customerListString = CustomerReport.write(customerList);
				}

				JList<String> listOfCustomers = new JList<String>(customerListString.toArray(new String[customerListString.size()]));
				listOfCustomers.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
				scrollPane = new JScrollPane();

				listOfCustomers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				scrollPane.setViewportView(listOfCustomers);
				listOfCustomers.addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						String customerId = getCustomerId((String) listOfCustomers.getSelectedValue());
						if (Pattern.compile("^[0-9]+$").matcher(customerId).matches()) {
						try {
							Customer customer = customerDao.getCustomer(Long.parseLong(customerId));
							
							editCustomer(Long.parseLong(customerId), customer.getFirstName(), customer.getLastName(), customer.getStreet(), 
									customer.getCity(), customer.getPostalCode(), customer.getPhone(), customer.getEmailAddress(), String.valueOf(customer.getJoinedDate()));
														
						} catch (NumberFormatException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						}

					}

					@Override
					public void mousePressed(MouseEvent e) {
					
					}

					@Override
					public void mouseReleased(MouseEvent e) {
					
					}

					@Override
					public void mouseEntered(MouseEvent e) {
					
					}

					@Override
					public void mouseExited(MouseEvent e) {
					
					}

				});
				frameAssignment.getContentPane().add(scrollPane, "cell 0 0,alignx center,growy");

				button = new Button("OK");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						scrollPane.setVisible(false);
						button.setVisible(false);
						frameAssignment.revalidate();
					}
				});

				button.setFont(new Font("Dialog", Font.PLAIN, 18));
				frameAssignment.getContentPane().add(button, "cell 0 1,alignx center");

				scrollPane.setVisible(true);
				button.setVisible(true);

				frameAssignment.revalidate();
			}
		});
		customer.add(list);

		return customer;
	}

	private JMenu helpMenu() {
		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);

		JMenuItem about = new JMenuItem("About");
		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frameAbout = new JFrame();
				JOptionPane.showMessageDialog(frameAbout, "A01258927 Assignment 01");
			}
		});
		help.add(about);

		return help;
	}

	private List<Customer> sortedList(List<Customer> customer) {
		ArrayList<Customer> sortedList = new ArrayList<>(customer);
		Collections.sort(sortedList, new CustomerSorter());
		return sortedList;
	}

	private String getCustomerId(String str) {
		String getId = str.substring(1, 5);
		return getId;
	}

	private static CustomerDao loadCustomers(Database db) throws ApplicationException {
		CustomerDao customerDao = new CustomerDao(db);
		return customerDao;
	}

	private static Database connect() throws IOException, SQLException, ApplicationException {
		Properties dbProperties = new Properties();
		dbProperties.load(new FileInputStream(DB_PROPERTIES_FILENAME));
		Database db = new Database(dbProperties);

		return db;
	}
	
	private void editCustomer(Long id, String firstName, String lastName, String street, String city, String postalCode,
			String phone, String email, String joinedDate) {
		
		JFrame contentPane;
		JTextField idField;
		JTextField firstNameField;
		JTextField lastNameField;
		JTextField streetField;
		JTextField cityField;
		JTextField postalCodeField;
		JTextField phoneField;
		JTextField emailField;
		JFormattedTextField joinedDateField;
		JLabel lblNewLabel;
		JLabel lblNewLabel_1;
		JLabel lblNewLabel_2;
		JLabel lblNewLabel_3;
		JLabel lblNewLabel_4;
		JLabel lblNewLabel_5;
		JLabel lblNewLabel_6;
		JLabel lblNewLabel_7;
		JLabel lblNewLabel_8;
		JButton btnNewButton;
		JButton btnNewButton_1;
		
		contentPane = new JFrame();
		contentPane.setTitle("Customer Information");
		contentPane.setBounds(100, 100, 567, 357);
		contentPane.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane.getContentPane().setLayout(new MigLayout("", "[][][][][][grow]", "[][][][][][][][][][][]"));
		contentPane.setVisible(true);
		
		lblNewLabel = new JLabel("ID");
		contentPane.getContentPane().add(lblNewLabel, "cell 1 0,alignx trailing");
		
		idField = new JTextField();
		idField.setEnabled(false);
		idField.setText(String.valueOf(id));
		contentPane.getContentPane().add(idField, "cell 2 0 4 1,growx");
		idField.setColumns(10);
		
		lblNewLabel_1 = new JLabel("First name");
		contentPane.getContentPane().add(lblNewLabel_1, "cell 1 1,alignx right,aligny top");
		
		firstNameField = new JTextField();
		firstNameField.setText(firstName);
		contentPane.getContentPane().add(firstNameField, "cell 2 1 4 1,growx");
		firstNameField.setColumns(10);
		
		lblNewLabel_2 = new JLabel("Last Name");
		contentPane.getContentPane().add(lblNewLabel_2, "cell 1 2,alignx trailing");
		
		lastNameField = new JTextField();
		lastNameField.setText(lastName);
		contentPane.getContentPane().add(lastNameField, "cell 2 2 4 1,growx");
		lastNameField.setColumns(10);
		
		lblNewLabel_3 = new JLabel("Street");
		contentPane.getContentPane().add(lblNewLabel_3, "cell 1 3,alignx trailing");
		
		streetField = new JTextField();
		streetField.setText(street);
		contentPane.getContentPane().add(streetField, "cell 2 3 4 1,growx");
		streetField.setColumns(10);
		
		lblNewLabel_4 = new JLabel("City");
		contentPane.getContentPane().add(lblNewLabel_4, "cell 1 4,alignx trailing");
		
		cityField = new JTextField();
		cityField.setText(city);
		contentPane.getContentPane().add(cityField, "cell 2 4 4 1,growx");
		cityField.setColumns(10);
		
		lblNewLabel_5 = new JLabel("Postal Code");
		contentPane.getContentPane().add(lblNewLabel_5, "cell 1 5,alignx trailing");
		
		postalCodeField = new JTextField();
		postalCodeField.setText(postalCode);
		contentPane.getContentPane().add(postalCodeField, "cell 2 5 4 1,growx");
		postalCodeField.setColumns(10);
		
		lblNewLabel_6 = new JLabel("Phone");
		contentPane.getContentPane().add(lblNewLabel_6, "cell 1 6,alignx trailing");
		
		phoneField = new JTextField();
		phoneField.setText(phone);
		contentPane.getContentPane().add(phoneField, "cell 2 6 4 1,growx");
		phoneField.setColumns(10);
		
		lblNewLabel_7 = new JLabel("Email");
		contentPane.getContentPane().add(lblNewLabel_7, "cell 1 7,alignx trailing");
		
		emailField = new JTextField();
		emailField.setText(email);
		contentPane.getContentPane().add(emailField, "cell 2 7 4 1,growx");
		emailField.setColumns(10);
		
		lblNewLabel_8 = new JLabel("Joined Date");
		contentPane.getContentPane().add(lblNewLabel_8, "cell 1 8,alignx trailing");
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		joinedDateField = new JFormattedTextField(format);
		joinedDateField.setText(String.valueOf(joinedDate));
		contentPane.getContentPane().add(joinedDateField, "cell 2 8 4 1,growx");
		joinedDateField.setColumns(10);
		
		btnNewButton = new JButton("Cancel");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPane.dispose();
			}
		});
		
		contentPane.getContentPane().add(btnNewButton, "flowx,cell 5 10,alignx right");
		
		btnNewButton_1 = new JButton("Save");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newFirstName = firstNameField.getText();
				String newLastName = lastNameField.getText();
				String newStreet = streetField.getText();
				String newCity = cityField.getText();
				String newPostalCode = postalCodeField.getText();
				String newPhone = phoneField.getText();
				String newEmail = emailField.getText();
				String newJoinedDate = joinedDateField.getText();
				
				String[] dateFormat = newJoinedDate.split("-");
				int year = Integer.parseInt(dateFormat[0]);
				int month = Integer.parseInt(dateFormat[1]);
				int day = Integer.parseInt(dateFormat[2]);
				
				Customer updateCustomer = new Customer.Builder(id, newPhone).
						setFirstName(newFirstName).
						setLastName(newLastName).
						setStreet(newStreet).
						setCity(newCity).
						setPostalCode(newPostalCode).
						setEmailAddress(newEmail).
						setJoinedDate(year, month, day).build();
				try {
					customerDao.update(updateCustomer);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				contentPane.setVisible(false);
				
			}
		});
		contentPane.getContentPane().add(btnNewButton_1, "cell 5 10,alignx right");
	}
	
}
