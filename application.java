package application;

import java.io.*; 
import java.lang.*; 
import java.util.*;
import java.time.*;



/* Ticketing system class */
public class application {

	// Create arraylists to hold all user and ticketing information
	private static ArrayList<User> users = new ArrayList<User>();
	private static ArrayList<Ticket> tickets = new ArrayList<Ticket>();
	private static int numTickets = 0;
	private static int TICKET_EXPIRY_TIME = 2; // This is in hours. Set to 0 for testing.

	//Error messages
	private static final String NOT_TECHNICIAN_ERROR = "\nSorry, you must be a qualified technician to complete this action";

	//Shared Scanner which can be used by all helper methods below
	private static Scanner SC = new Scanner(System.in);

	//Various enums to help with setting statuses and types
	private enum TicketStatus{
		CLOSED,
		OPEN, 
		ARCHIVED
	}

	private enum Severity {
		LOW,
		MEDIUM,
		HIGH
	}

	private enum DatabaseType {
		USER,
		TICKET
	}

	//User class
	static class User implements Comparable<User>{

		private String username;
		private String name;
		private String email;
		private String passwd;
		private String role;
		private int num_assigned_tickets;

		private User(final String USERNAME, final String NAME, final String EMAIL, final String PASSWD, final String ROLE, int NUM_ASSIGNED_TICKETS) {
			this.username = USERNAME;
			this.name = NAME;
			this.email = EMAIL;
			this.passwd = PASSWD;
			this.role = ROLE;
			this.num_assigned_tickets = NUM_ASSIGNED_TICKETS;
		}

		@Override
		public int compareTo(User u) {
			int compareAssignedTickets = u.num_assigned_tickets;
			return this.num_assigned_tickets - compareAssignedTickets;
		}

	}

	static class Ticket implements Comparable<Ticket>{

		private String fname; 
		private String lname; 
		private String staffID;
		private String email;
		private String contact;
		private String description;
		private Severity sev;
		private TicketStatus status;
		private String ticketID;
		private String serviceDesk;
		private String ticketAssignedTo;
		private LocalDateTime createdDate;
		private LocalDateTime closedDate;

		private Ticket(final String FNAME, final String LNAME, final String STAFFID, final String EMAIL, final String CONTACT, final String DESCRIPTION, 
				final Severity SEV, final TicketStatus STATUS, final String TICKET_ID, final String SERVICE_DESK, final String TICKET_ASSIGNED_TO,
				final LocalDateTime CREATED_DATE, final LocalDateTime CLOSED_DATE ) {
			this.fname = FNAME;
			this.lname = LNAME;
			this.staffID = STAFFID;
			this.email = EMAIL;
			this.contact = CONTACT;
			this.description = DESCRIPTION;
			this.sev = SEV;
			this.status = STATUS;
			this.ticketID = TICKET_ID;
			this.serviceDesk = SERVICE_DESK;
			this.ticketAssignedTo = TICKET_ASSIGNED_TO;
			this.createdDate = CREATED_DATE;
			this.closedDate = CLOSED_DATE;
		}

		public TicketStatus getStatus() { return this.status; }
		public LocalDateTime getClosedDate() { return this.closedDate; }
		public String getTicketID() { return this.ticketID; }

		private void setSev(Severity sev) {
			this.sev = sev;
		}

		private void setStatus(TicketStatus status) {
			this.status = status;
		}

		private void setTicketAssignedTo(String ticketAssignedTo) {
			this.ticketAssignedTo = ticketAssignedTo;
		}

		@Override
		public int compareTo(Ticket t) {
			if 	(fname.equals(t.fname) && 
				lname.equals(t.lname) &&
				staffID.equals(t.staffID) &&
				email.equals(t.email) &&
				contact.equals(t.contact) && 
				description.equals(t.description) &&
				//skip severity and close date and status && assignee
				ticketID.equals(t.ticketID) &&
				serviceDesk.equals(t.serviceDesk) &&
				createdDate.equals(t.createdDate)) 
					return 1;
				else 
					return 0;
		}	
	}
	// Main class
	public static void main(String[] args) {
		// Insert all technicians and a demo user
		insertDefaultUsers();

		// Menu system for the program
		String selection;

		// Check user input until the exit function is met, i.e. X or x
		do {
			// Display Menu Options
			String WELCOME_BANNER = "Welcome to the IT ticketing system";
			banner(WELCOME_BANNER);
			System.out.println("A. Log in (requires an existing account)");
			System.out.println("B. Create an account");
			System.out.println("C. Reset password");
			System.out.println("X. Exit ticketing system");
			System.out.println();

			// Prompt user to enter selection
			System.out.print("Enter selection: ");
			selection = SC.nextLine();
			System.out.println();

			// Validate selection input length, ensure it is only 1 character in length
			if (selection.length() != 1) {
				System.out.println("Error - invalid selection!");
			}

			// Otherwise, take input and go to appropriate method
			else {
				// process user's selection
				switch (selection.toUpperCase()) {
				case "A":
					attemptLogin();
					break;

				case "B":
					createAccount();
					break;

				case "C":
					resetPassword();
					break;

				case "X":
					System.out.println("Exiting the program...");
					break;

				default:
					System.out.println("Error - invalid selection!");
				}
			}
			System.out.println();

		} while (!selection.equalsIgnoreCase("X"));
	}


	// Create banner title for all menu options
	private static void banner(String bannerName) {
		// Set number of symbols to length of string
		for (int i = 0; i < bannerName.length(); ++i) {
			System.out.print("=");
		}
		System.out.println();

		// Print banner name
		System.out.println(bannerName);

		for (int i = 0; i < bannerName.length(); ++i) {
			System.out.print("=");
		}
		System.out.println();
	}

	// Validate user input to ensure it is semi valid
	public static String get_user_input() {
		String input = "";
		boolean valid = false;
		do {
			input = SC.nextLine();
			if (input.length() < 1) {
				System.out.print("Error, please enter valid input: ");
				valid = false;
			} else {
				valid = true;
			}
		} while (valid == false);
		// Return whether it is valid or not
		return input;
	}

	// Checks whether a user exists in the system
	private static boolean check_login(final String USERNAME, final String PASSWORD) {

		boolean valid = false;
		try {
			for(int i = 0; i < users.size(); ++i){

				// Username and password must match
				if(USERNAME.contentEquals(users.get(i).username)) {
					if(PASSWORD.contentEquals(users.get(i).passwd)) {
						// If user exists, set to true
						valid = true;
						break;
					}
				}
			}
		} catch (NullPointerException npe) {
			System.out.println("\nError! User does not exist\n");
			attemptLogin();
		}
		// If user does not exist, return false
		return valid;
	}

	// Main login
	private static void attemptLogin() {

		// Login banner
		String ATTEMPTLOGIN_BANNER = "Login to Ticketing System";
		banner(ATTEMPTLOGIN_BANNER);

		// Request login credentials
		System.out.println("\nPlease enter login credentials\n");

		System.out.println("Please enter your username: ");
		String username = get_user_input();
		System.out.println("Please enter your password: ");
		String passwd = get_user_input();

		//Check login details
		//		do {
		//			valid = check_login(userDatabase, username, passwd);
		//		} while(!valid);

		// If valid login exists, then start next menu system
		if (check_login(username, passwd))
		{
			// Logged in menu system for the program
			String selection;

			// Check user input until the exit function is met, i.e. X or x
			do {
				// Display Menu Options
				final String MENU_BANNER = "IT ticketing system - Main Menu";
				banner(MENU_BANNER);

				System.out.println("\nA. Create a new ticket");
				System.out.println("B. View all open tickets");
				System.out.println("C. Change ticket priority (Technicians only)");
				System.out.println("D. Close ticket (Technicians only)");
				System.out.println("E. Archive tickets");
				System.out.println("F. View all archived tickets");
				System.out.println("X. Logout\n");

				// Prompt user to enter selection
				System.out.print("Enter selection: ");
				selection = SC.nextLine();

				// Validate selection input length, ensure it is only 1 character in length
				if (selection.length() != 1) {
					System.out.println("Error - invalid selection!");
				}
				// Otherwise, take input and go to appropriate method
				else {

					// process user's selection
					switch (selection.toUpperCase()) {
					case "A":
						createTicket(username);
						break;

					case "B":
						viewTickets();
						break;

					case "C":
						if(isTechnician(username)) {
							changeTicketPriority();
						} else {
							System.out.println(NOT_TECHNICIAN_ERROR);
						}
						break;

					case "D":
						if(isTechnician(username)) {
							closeTicket();
						} else {
							System.out.println(NOT_TECHNICIAN_ERROR);
						}
						break;

					case "E":
						ArrayList<Ticket> readyToArchive = getTicketsForArchiving(tickets);
						int readyToArchiveCount = readyToArchive.size();
						archiveTickets(readyToArchive);
						if(readyToArchiveCount > 0)
							System.out.println("Successfully archived " +  readyToArchiveCount + " tickets.");
						else 
							System.out.println("There are no tickets to be archived.");
						break;
					case "F": 
						viewAllArchivedTickets();
						break;
					case "X":
						System.out.println("Logging out...");
						break;

					default:
						System.out.println("Error - invalid selection!");
					}
				}
				System.out.println();

			} while (!selection.equalsIgnoreCase("X"));
		} else {
			// Error if login is incorrect
			System.out.println("Incorrect Login");
		}
		System.out.println();
	}

	//Function to check if logged in user is a technician
	private static boolean isTechnician(final String USERNAME) {
		boolean valid;
		int index = database_search(DatabaseType.USER, USERNAME);
		if(users.get(index).role.contentEquals("1") || users.get(index).role.contentEquals("2")) {
			valid = true;
		} else {
			valid = false;
		}
		return valid;
	}

	// Create an account for the a new user in the system
	private static void createAccount() {
		// Create account banner
		String CREATEACC_BANNER = "Create account";
		banner(CREATEACC_BANNER);

		// Set function variables
		String username = "";
		String name = "";
		String email = "";
		String passwd = "";

		// Request inputs from user
		System.out.println("\nPlease enter a username: ");
		username = get_user_input();

		System.out.println("\nPlease enter your name: ");
		name = get_user_input();
		
		System.out.println("\nPlease enter your email address: ");
		email = get_user_input();

		System.out.println("\nPlease enter your password: ");
		passwd = get_user_input();

		// All created users are staff not technicians 
		String defaultRole = "0";
		
		// Add user data to userDatabase array, and increase number of users value
		users.add(new User(username, name, email, passwd, defaultRole, 0));
		
		// Inform the user the accounts was created successfully
		System.out.println("Account " + username + " created");
	}

	// Function to reset password - later sprint
	private static void resetPassword() {
		String RESETPWD_BANNER = "Reset Password";
		banner(RESETPWD_BANNER);
		System.out.println("Prompts to reset password");
		System.out.println("!!!FUNCTIONALITY NOT YET COMPLETED!!!");

		// Add code here to reset password

		System.out.println();
	}

	// Function to check severity of ticket is valid
	private static boolean check_severity(final String severity) {
		boolean valid = false;
		if(severity.contentEquals("1") || severity.contentEquals("2") || severity.contentEquals("3")) {
			valid = true;
		} else {
			System.out.println("Error! Please enter correct severity level (1 = Low | 2 = Medium | 3 = High): ");
		}
		return valid;
	}

	private static Severity set_ticket_severity(final String severity) {
		Severity choice = null;
		switch(severity) {
		case "1" :
			choice = Severity.LOW;
			break;
		case "2" :
			choice = Severity.MEDIUM;
			break;
		case "3" :
			choice = Severity.HIGH;
			break;
		}
		return choice;
	}

	// Function to create ticket in the system
	private static void createTicket(final String USERNAME)
	{
		String[] fullName;
		String fname = "";
		String lname = "";
		Severity severity_rating;
		String email = "";
		String severity = "";
		String serviceDesk = "";
		String ticketID = "";
		final int FNAME_INDEX = 0;
		final int LNAME_INDEX = 1;
		final String CREATETICKET_BANNER = "Create a ticket";
		banner(CREATETICKET_BANNER);
		System.out.println("\nEnter details for ticket below");

		//Search for user to pre enter user's details for ticket
		for(int i = 0; i < users.size(); ++i){

			if(USERNAME.contentEquals(users.get(i).username)) {
				fullName = users.get(i).name.split(" ");
				fname = fullName[FNAME_INDEX];
				lname = fullName[LNAME_INDEX];
				email = users.get(i).email;
				break;
			}
		}
		ticketID = "T" + String.valueOf(numTickets);

		//Get remaining data from user
		System.out.print("\nPlease enter your staffID: ");
		String staffID = get_user_input();
		System.out.print("\nPlease enter your contact number: ");
		String contact_number = get_user_input();
		System.out.print("\nPlease enter a brief description of the problem: ");
		String description = get_user_input();
		System.out.print("\nPlease set the severity (1 = Low | 2 = Medium | 3 = High): ");
		//Get severity rating from user
		do {
			severity = get_user_input();
		} while(!check_severity(severity));

		//Set severity rating and service desk
		severity_rating = set_ticket_severity(severity);
		switch(severity_rating) {
		case LOW:
			serviceDesk = "1";
			break;
		case MEDIUM:
			serviceDesk = "1";
			break;
		case HIGH:
			serviceDesk = "2";
			break;
		}

		//Display final ticket to user to check
		System.out.println("\nCheck the final ticket below");
		System.out.println("\nTicket ID: " + ticketID + "\nFirst Name: " + fname + "\nLast Name: " + lname + "\nStaffID: " + staffID + "\nEmail: "
				+ email + "\nContact Number: " + contact_number + "\nDescription: " + description + "\nSeverity: " + severity_rating.toString() 
				+ "\nService Desk Level: " + serviceDesk);
		System.out.print("\nIf the above details are correct type 'Y' else 'N' to restart: ");

		if(get_user_confirmation("")) {

			//Auto route ticket to technician based on lowest workload (tickets are assigned by technician username)
			String assignedTo = autoRouteTicket(serviceDesk);

			//Create ticket
			tickets.add(new Ticket(fname, lname, staffID, email, contact_number, description, severity_rating, TicketStatus.OPEN, 
					ticketID, serviceDesk, assignedTo, LocalDateTime.now(), null));
			++numTickets;

			System.out.println("\nTicket successfully created!");
		} else {
			createTicket(USERNAME);
		}
	}

	//Function to auto route a newly created ticket to the appropriate technician
	private static String autoRouteTicket(final String SERVICE_DESK) {

		int i = 0;
		//Sort num of tickets assigned to each technician
		Collections.sort(users, Collections.reverseOrder());
		//Get user at top of list who works at the same service desk level
		while(!users.get(i).role.contentEquals(SERVICE_DESK)) {
			++i;
		}
		//Iterate number of tickets assigned to chosen technician
		++users.get(i).num_assigned_tickets;
		//Return username of assigned technician
		return users.get(i).username;
	}

	// Function to view all tickets assigned to a person
	private static void viewTickets()
	{
		String VIEWTICKET_BANNER = "View your tickets";
		banner(VIEWTICKET_BANNER);
		System.out.println("Show me all your tickets ");
		
		tickets.forEach((t) -> {
			if(t.getStatus() == TicketStatus.OPEN)
				displayEntry(database_search(DatabaseType.TICKET, t.getTicketID()), DatabaseType.TICKET, "", false);
		});
	}

	//Function to display a single entry from any database
	private static boolean displayEntry(final int INDEX, final DatabaseType TYPE, final String MSG, final boolean MSG_REQUIRED){
		String role = "";
		if(MSG_REQUIRED) {
			System.out.println("\n" + MSG);
		}
		if(TYPE == DatabaseType.TICKET) {
			Ticket tck = tickets.get(INDEX);
			System.out.println("\nTicket ID: " + tck.ticketID + "\nFirst Name: " + 
					tck.fname + "\nLast Name: " + tck.lname + "\nStaffID: " 
					+ tck.staffID+ "\nEmail: " + tck.email 
							+ "\nContact Number: " + tck.contact + "\nDescription: " + 
							tck.description + "\nSeverity: " + tck.sev + "\nService"
									+ " Desk Level: " + tck.serviceDesk + "\nAssigned To: "
							+ tck.ticketAssignedTo);
		} else {
			if(users.get(INDEX).role.contentEquals("1")) {
				role = "Level 1 Technician";
			} else if (users.get(INDEX).role.contentEquals("2")) {
				role = "Level 2 Technician";
			}
			System.out.println("\nUsername: " + users.get(INDEX).username + "\nName: " + users.get(INDEX).email
					+ "\nRole: " + role);
		}
		return true;
	}

	//Function to search either the ticket or user database. The function returns the index of the search result or null
	private static int database_search(final DatabaseType TYPE, final String SEARCH_KEY){
		int index = 0;
		boolean found = false;

		if(TYPE == DatabaseType.USER) {
			for(int i = 0; i < users.size(); ++i) {
				//Search through the database fields to find a match with the key
				if(SEARCH_KEY.contentEquals(users.get(i).username)) {
					index = i;
					found = true;
					break;
				}
			}
		} else {
			for(int i = 0; i < tickets.size(); ++i) {
				//Search through the database fields to find a match with the key
				if(SEARCH_KEY.contentEquals(tickets.get(i).ticketID)) {
					index = i;
					found = true;
					break;
				}
			}
		}

		if(found) {
			return index;
		} else {
			return -1;
		}
	}

	// Function to change ticket priority
	private static void changeTicketPriority()
	{
		int index = 0;
		String CHGTICKETPR_BANNER = "Change ticket priority";
		banner(CHGTICKETPR_BANNER);
		String new_severity = "";
		Severity sev;
		//Retrieve ticket from user
		System.out.println("Enter the ticket ID you would like to change priority for (T<number>): ");
		String ticket_id = get_user_input();
		//Search for ticket and return
		index = database_search(DatabaseType.TICKET, ticket_id);
		if(index == -1) {
			System.out.println("\nSorry, ticket not found!\n");
			return;
		}
		displayEntry(index, DatabaseType.TICKET, "Is the below ticket the correct ticket? (Y/N)", true);
		if(get_user_confirmation("")) {
			System.out.print("\nPlease set the new severity status (1 = Low | 2 = Medium | 3 = High): ");
			do {
				new_severity = get_user_input();
			} while (!check_severity(new_severity)); 
			sev = set_ticket_severity(new_severity);
			tickets.get(index).sev = sev;
			System.out.println("\nTicket severity updated successfully!");
		} else {
			changeTicketPriority();
		}
	}

	//Allows technician to search for and close specified ticket
	private static void closeTicket()
	{
		String CLOSETICKET_BANNER = "Ticket closure";
		banner(CLOSETICKET_BANNER);
		String ticketID = "";
		int index = 0;
		System.out.println("Type the ticket ID of the ticket you'd like to close: ");
		ticketID = get_user_input();
		index = database_search(DatabaseType.TICKET, ticketID);
		if(index == -1) {
			System.out.println("\nSorry, ticket not found!\n");
			return; 
		}
		displayEntry(index, DatabaseType.TICKET, "Is the below ticket the correct ticket? (Y/N)", true);		
		if(get_user_confirmation("")) {
			if(get_user_confirmation("Would you like to close the ticket? (Y/N)")) {
				Ticket ticket = tickets.get(index);
				ticket.setStatus(TicketStatus.CLOSED);
				ticket.closedDate = LocalDateTime.now();
				System.out.println("\nTicket status updated!");
			}
		} else {
			closeTicket();
		}
	}

	//This returns true for 'Y' input and false for 'N' input from the user
	private static boolean get_user_confirmation(final String MSG) {

		System.out.println(MSG);
		boolean valid = false;
		boolean result = false;
		do {
			String confirmation = get_user_input();
			if(confirmation.toUpperCase().contentEquals("Y")) {
				valid = true;
				result = true;
			} else if (confirmation.toUpperCase().contentEquals("N")) {
				valid = true;
				result = false;
			} else {
				System.out.println("Error! Please enter either 'Y' or 'N' as confirmation");
			}
		}while (!valid);
		return result;
	}

	// Function to add users that are not ones manually created by each user
	private static void insertDefaultUsers()
	{
		users.add(new User("hstyles", "Harry Styles",  "hstyles@cinco.com.au", "12345", "1", 0));
		users.add(new User("nhoran", "Niall Horan", "nhoran@cinco.com.au", "12345", "1", 0));
		users.add(new User("ltomlinson", "Louis Tomlinson", "ltomlinson@cinco.com.au", "12345", "2", 0));
		users.add(new User("zmalik", "Zayn Malik", "zmalik@cinco.com.au", "12345", "2", 0));
		users.add(new User("demouser", "Demo User", "demouser@cinco.com.au", "12345", "0", 0));
	}

	// Returns an array of same size as the database with only returned tickets that should be Archived
	// Input: ArrayList<Ticket> TicketDatabase
	// Output: ArrayList<Ticket>
	private static ArrayList<Ticket> getTicketsForArchiving(ArrayList<Ticket> tickets) {
		banner("Getting tickets for archiving");
		// Creates internal structure for holding tickets that will be appropriate for Archiving
		ArrayList<Ticket> filteredTickets = new ArrayList<Ticket>();

		// Loops through all tickets and checks for their status and expiry time
		tickets.forEach((t) -> {
			if (t.getStatus() == TicketStatus.CLOSED && LocalDateTime.now().isAfter(t.getClosedDate().plusHours(TICKET_EXPIRY_TIME)))
				// If fits, it's added to archive list
				filteredTickets.add(t);
		});

		return filteredTickets;
	}

	// Archives all the tickets passed
	// Input: ArrayList<Ticket> tickets to archive
	private static void archiveTickets(ArrayList<Ticket> ticketsToArchive) {
		// Find tickets to be archived in the database and change their status
		ticketsToArchive.forEach((t) -> tickets.get(database_search(DatabaseType.TICKET, t.getTicketID())).setStatus(TicketStatus.ARCHIVED));
	}
	
	// Function to view all tickets that are archived
	private static void viewAllArchivedTickets()
	{
		// Messaging
		String VIEWTICKET_BANNER = "View all archived tickets";
		banner(VIEWTICKET_BANNER);
		System.out.println("Displaying Archived tickets:");

		tickets.forEach((t) -> {
			if(t.getStatus() == TicketStatus.ARCHIVED)
				displayEntry(database_search(DatabaseType.TICKET, t.getTicketID()), DatabaseType.TICKET, "", false);
		});

		System.out.println("End of Archived tickets display.");
	}


}