package application;
import java.io.*; 
import java.lang.*; 
import java.util.*;
import java.time.*;
import java.time.format.*;

/* Ticketing system class */
public class application {

	// Create arraylists to hold all user and ticketing information
	private static ArrayList<User> users = new ArrayList<User>();
	private static TicketArrayList tickets = new TicketArrayList();
	private static int numTickets = 0;
	public static int TICKET_EXPIRY_TIME = 1; // This is in hours. Set to 0 for testing.
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); //Formatter for Timestamps

	//Error messages
	private static final String NOT_TECHNICIAN_ERROR = "\nSorry, you must be a qualified technician to complete this action";
	//Shared Scanner which can be used by all helper methods below
	private static Scanner SC = new Scanner(System.in);


	//Various enums to help with setting statuses and types
	public enum TicketStatus{
		CLOSED("Closed"),
		OPEN("Open"), 
		ARCHIVED("Archived");

		private final String textRepresentation;

		private TicketStatus(String textRepresentation) {
			this.textRepresentation = textRepresentation;
		}

		@Override public String toString() {
			 return textRepresentation;
		}
	}
	
	public enum Severity {
		LOW("Low"),
		MEDIUM("Medium"),
		HIGH("High");
		
		private final String textRepresentation;

		private Severity(String textRepresentation) {
			this.textRepresentation = textRepresentation;
		}

		@Override public String toString() {
			 return textRepresentation;
		}
	}
	
	public enum DatabaseType {
		USER("User"),
		TICKET("Ticket");
		
		private final String textRepresentation;

		private DatabaseType(String textRepresentation) {
			this.textRepresentation = textRepresentation;
		}

		@Override public String toString() {
			 return textRepresentation;
		}
	}
	
	static class TicketArrayList {
		private ArrayList<Ticket> list;

		public TicketArrayList() { list = new ArrayList<Ticket>(); }

		// This is not very performance focused but will work fine
		public void add(Ticket item) { 
			list.add(item);  
			internalTicketArchive();
		}
		public void remove(Ticket item) { 
			list.remove(item); 
			internalTicketArchive();
		}
		public Ticket get(int index) { 
			internalTicketArchive();
			return list.get(index); 
		}
		//
		
		public int size() { return list.size(); }
		public ArrayList<Ticket> rawList() { return list; }

		private void internalTicketArchive()
		{
			this.list.forEach((t) -> {
				if (t.getStatus() == TicketStatus.CLOSED && 
						LocalDateTime.now().isAfter(t.getClosedDate().plusHours(TICKET_EXPIRY_TIME)))
					t.setStatus(TicketStatus.ARCHIVED);
			});
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

	// Ticket class
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

		public void setSev(Severity sev) {
			this.sev = sev;
		}

		public void setStatus(TicketStatus status) {
			this.status = status;
		}

		public void setTicketAssignedTo(String ticketAssignedTo) {
			this.ticketAssignedTo = ticketAssignedTo;
		}

		public String getSeverity(){
			return this.sev.toString();
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
	public static String getUserInput() {
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
	private static boolean checkLogin(final String USERNAME, final String PASSWORD) {

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
		String username = getUserInput();
		System.out.println("Please enter your password: ");
		String passwd = getUserInput();

		//Check login details
		//		do {
		//			valid = check_login(userDatabase, username, passwd);
		//		} while(!valid);

		// If valid login exists, then start next menu system
		if (checkLogin(username, passwd))
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
				System.out.println("G. View all archived and closed tickets");
				System.out.println("H. View period report");
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
						if(readyToArchiveCount > 0) {
							archiveTickets(readyToArchive);
							System.out.println("Successfully archived " +  readyToArchiveCount + " tickets.");
						}
						else 
							System.out.println("There are no tickets to be archived.");
						break;
					case "F": 
						viewSelectedTickets(getTicketsByStatus(TicketStatus.ARCHIVED, null, null), "archived");
						break;
					case "G":
						ArrayList<Ticket> t = getTicketsByStatus(TicketStatus.ARCHIVED, null, null);
						t.addAll(getTicketsByStatus(TicketStatus.CLOSED, null, null));
						viewSelectedTickets(t, "archived and closed");
						break;
					case "H":
						// Default values for the period
						LocalDateTime from = LocalDateTime.now().plusHours(-24);
						LocalDateTime to = LocalDateTime.now();
						showPeriodReport(from, to);
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

	// Displays menu for Period Report
	private static void showPeriodReport(LocalDateTime from, LocalDateTime to) {
		String selection;

		do {
			// Display Menu Options
			String WELCOME_BANNER = "Period report options";
			banner(WELCOME_BANNER);
			System.out.println("A. Set FROM date");
			System.out.println("B. Set TO date");
			System.out.println("C. Display a Period report");
			System.out.println("X. Exit Period report options");
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
						from = getUserEnteredDateTime();
						break;
					case "B":
						to = getUserEnteredDateTime();
						break;
					case "C":
						printPeriodReport(from, to);
						break;
					case "X":
						System.out.println("Exiting the Period report option menu...");
						break;
					default:
						System.out.println("Error - invalid selection!");
				}
			}
			System.out.println();

		} while (!selection.equalsIgnoreCase("X"));
	}

	// Accepts input for a timestamp
	private static LocalDateTime getUserEnteredDateTime() {
		LocalDateTime dateTime;

		System.out.println("\nPlease enter a valid time stamp (dd/mm/yyyy hh:mm): ");
		String dateTimeString = getUserInput();

		try {
			dateTime = LocalDateTime.parse(dateTimeString, formatter);
		} catch (DateTimeParseException conversionException) {
			System.out.println("\nCould not convert the input. Please try again.");
			dateTime = getUserEnteredDateTime();
		}
		return dateTime;
	}

	// Helper to format Java duration class
	public static String formatDuration(Duration d) {
		long days = d.toDays();
		d = d.minusDays(days);
		long hours = d.toHours();
		d = d.minusHours(hours);
		long minutes = d.toMinutes();
		d = d.minusMinutes(minutes);
		long seconds = d.getSeconds() ;
		return
				(days ==  0?"":days+" days,")+
						(hours == 0?"":hours+" hours,")+
						(minutes ==  0?"":minutes+" minutes,")+
						(seconds == 0?"":seconds+" seconds");
	}

	// Displays Period report
	private static void printPeriodReport(LocalDateTime from, LocalDateTime to) {
		// Get open tickets
		ArrayList<Ticket> openTickets = getTicketsByStatus(TicketStatus.OPEN, from, to);
		// Get all closed+archived tickets
		ArrayList<Ticket> closedOrArchivedTickets = getTicketsByStatus(TicketStatus.CLOSED, from, to);
		closedOrArchivedTickets.addAll(getTicketsByStatus(TicketStatus.ARCHIVED, from, to));
		// Count of closed or archived tickets
		long closedOrArchived = closedOrArchivedTickets.size();
		// Count of all tickets
		long submitted = closedOrArchived + openTickets.size();

		String RESETPWD_BANNER = "Period report from " + from.format(formatter) + " - " + to.format(formatter);
		banner(RESETPWD_BANNER);

		System.out.printf("There were %d created tickets, out of %d have been closed and/or archived and %d remain " +
				"open in selected period.\n", submitted, closedOrArchived, submitted-closedOrArchived);

		System.out.println("Displaying resolved tickets:");
		closedOrArchivedTickets.forEach(t ->
				{
					Duration resolvePeriod = Duration.between(t.createdDate, t.closedDate);
					System.out.println(
							"\nTicket ID: " + t.ticketID +
							"\nFirst Name: " + t.fname +
							"\nLast Name: " + t.lname +
							"\nStaffID: " + t.staffID +
							"\nService Desk Level: " + t.serviceDesk +
							"\nAssigned To: " + t.ticketAssignedTo +
							"\nOpened On: " + t.createdDate.format(formatter) +
							"\nTime to resolve: " + formatDuration(resolvePeriod));});

		System.out.println("");
		System.out.println("Displaying unresolved, open tickets:");
		openTickets.forEach(t ->
				System.out.println(
						"\nTicket ID: " + t.ticketID +
						"\nFirst Name: " + t.fname +
						"\nLast Name: " + t.lname +
						"\nStaffID: " + t.staffID +
						"\nService Desk Level: " + t.serviceDesk +
						"\nAssigned To: " + t.ticketAssignedTo +
						"\nOpened On: " + t.createdDate.format(formatter) +
						"\nSeverity: " + t.getSeverity().toString()));

 		System.out.printf("\nEnd of period report.\n");
	}

	//Function to check if logged in user is a technician
	private static boolean isTechnician(final String USERNAME) {
		boolean valid;
		int index = databaseSearch(DatabaseType.USER, USERNAME);
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
		username = getUserInput();

		System.out.println("\nPlease enter your name: ");
		name = getUserInput();

		System.out.println("\nPlease enter your email address: ");
		email = getUserInput();

		System.out.println("\nPlease enter your password: ");
		passwd = getUserInput();

		// All created users are staff not technicians 
		String defaultRole = "0";

		// Add user data to userDatabase array, and increase number of users value
		users.add(new User(username, name, email, passwd, defaultRole, 0));

		// Inform the user the accounts was created successfully
		System.out.println("Account " + username + " created");
	}

	// Function to reset password
	private static void resetPassword() {
		String RESETPWD_BANNER = "Reset Password";
		banner(RESETPWD_BANNER);

		System.out.println("\nPlease enter a username: ");
		String username = getUserInput();

		System.out.println("\nPlease enter your email address: ");
		String email = getUserInput();

		User match = null;
		for (User u : users)
			if (u.username.equals(username) && u.email.equals(email)) match = u;

		if (null != match) {
			System.out.println("\nPlease enter new password: ");
			String password1 = getUserInput();
			System.out.println("\nPlease re-enter new password: ");
			String password2 = getUserInput();

			if (password1.equals(password2)) {
				match.passwd = password1;
				System.out.println("\nPassword has been reset.");
			}
			else
				System.out.println("\nPasswords did not match. Password has not been reset.");
		}
		else
			System.out.println("\nUser has not been found.");

		System.out.println();
	}

	// Function to check severity of ticket is valid
	private static boolean checkSeverity(final String severity) {
		boolean valid = false;
		if(severity.contentEquals("1") || severity.contentEquals("2") || severity.contentEquals("3")) {
			valid = true;
		} else {
			System.out.println("Error! Please enter correct severity level (1 = Low | 2 = Medium | 3 = High): ");
		}
		return valid;
	}

	private static Severity setTicketSeverity(final String severity) {
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
	private static void createTicket(final String USERNAME) {
		String[] fullName;
		String fname = "";
		String lname = "";
		Severity severityRating;
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
		String staffID = getUserInput();
		System.out.print("\nPlease enter your contact number: ");
		String contact_number = getUserInput();
		System.out.print("\nPlease enter a brief description of the problem: ");
		String description = getUserInput();
		System.out.print("\nPlease set the severity (1 = Low | 2 = Medium | 3 = High): ");
		//Get severity rating from user
		do {
			severity = getUserInput();
		} while(!checkSeverity(severity));

		//Set severity rating and service desk
		severityRating = setTicketSeverity(severity);
		serviceDesk = setServiceDesk(severityRating);

		//Display final ticket to user to check
		System.out.println("\nCheck the final ticket below");
		System.out.println("\nTicket ID: " + ticketID + "\nFirst Name: " + fname + "\nLast Name: " + lname + "\nStaffID: " + staffID + "\nEmail: "
				+ email + "\nContact Number: " + contact_number + "\nDescription: " + description + "\nSeverity: " + severityRating.toString() 
				+ "\nService Desk Level: " + serviceDesk);
		System.out.print("\nIf the above details are correct type 'Y' else 'N' to restart: ");

		if(get_user_confirmation("")) {

			//Auto route ticket to technician based on lowest workload (tickets are assigned by technician username)
			String assignedTo = autoRouteTicket(serviceDesk);

			//Create ticket
			tickets.add(new Ticket(fname, lname, staffID, email, contact_number, description, severityRating, TicketStatus.OPEN, 
					ticketID, serviceDesk, assignedTo, LocalDateTime.now(), null));
			++numTickets;

			System.out.println("\nTicket successfully created!");
		} else {
			createTicket(USERNAME);
		}
	}

	private static String setServiceDesk(Severity SEV) {
		String serviceDesk = "";
		switch(SEV) {
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
		return serviceDesk;
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
	private static void viewTickets() {
		String VIEWTICKET_BANNER = "View your tickets";
		banner(VIEWTICKET_BANNER);
		System.out.println("Show me all your tickets ");

		tickets.rawList().forEach((t) -> {
			if(t.getStatus() == TicketStatus.OPEN)
				displayEntry(databaseSearch(DatabaseType.TICKET, t.getTicketID()), DatabaseType.TICKET, "", false);
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
					+ tck.ticketAssignedTo + "\nStatus: " + tck.status.toString());
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
	private static int databaseSearch(final DatabaseType TYPE, final String SEARCH_KEY){
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
	private static void changeTicketPriority() {
		int index = 0;
		int userIndex = 0;
		String CHGTICKETPR_BANNER = "Change ticket priority";
		banner(CHGTICKETPR_BANNER);
		String newSeverity = "";
		String newServiceDesk = "";
		String newAssignedTo = "";
		Severity sev;
		
		//Retrieve ticket from user
		System.out.println("Enter the ticket ID you would like to change priority for (T<number>): ");
		String ticket_id = getUserInput();
		//Search for ticket and return
		index = databaseSearch(DatabaseType.TICKET, ticket_id);
		if(index == -1) {
			System.out.println("\nSorry, ticket not found!\n");
			return;
		}
		displayEntry(index, DatabaseType.TICKET, "Is the below ticket the correct ticket? (Y/N)", true);
		if(get_user_confirmation("")) {
			System.out.print("\nPlease set the new severity status (1 = Low | 2 = Medium | 3 = High): ");
			do {
				newSeverity = getUserInput();
			} while (!checkSeverity(newSeverity));
			
			//Set the new level of severity and appropriate service desk
			sev = setTicketSeverity(newSeverity);
			newServiceDesk = setServiceDesk(sev);
			tickets.get(index).sev = sev;
			
			//Re route ticket to new technician if new level of service desk is required
			if(!newServiceDesk.contentEquals(tickets.get(index).serviceDesk)) {
				//Search for user that ticket is currently assigned to and de increment num of assigned tickets
				userIndex = databaseSearch(DatabaseType.USER, tickets.get(index).ticketAssignedTo);
				--users.get(userIndex).num_assigned_tickets;
				//Find the new user ticket is reassigned to
				newAssignedTo = autoRouteTicket(newServiceDesk);
				tickets.get(index).ticketAssignedTo = newAssignedTo;
				tickets.get(index).serviceDesk = newServiceDesk;
			}
			
			System.out.println("\nTicket severity updated successfully!");
		} else {
			changeTicketPriority();
		}
	}

	//Allows technician to search for and close specified ticket
	private static void closeTicket() {
		String CLOSETICKET_BANNER = "Ticket closure";
		banner(CLOSETICKET_BANNER);
		String ticketID = "";
		int index = 0;
		System.out.println("Type the ticket ID of the ticket you'd like to close: ");
		ticketID = getUserInput();
		index = databaseSearch(DatabaseType.TICKET, ticketID);
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
			String confirmation = getUserInput();
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
	private static void insertDefaultUsers() {
		users.add(new User("hstyles", "Harry Styles",  "hstyles@cinco.com.au", "12345", "1", 0));
		users.add(new User("nhoran", "Niall Horan", "nhoran@cinco.com.au", "12345", "1", 0));
		users.add(new User("ltomlinson", "Louis Tomlinson", "ltomlinson@cinco.com.au", "12345", "2", 0));
		users.add(new User("zmalik", "Zayn Malik", "zmalik@cinco.com.au", "12345", "2", 0));
		users.add(new User("demouser", "Demo User", "demouser@cinco.com.au", "12345", "0", 0));
	}

	// Returns an array of same size as the database with only returned tickets that should be Archived
	private static ArrayList<Ticket> getTicketsForArchiving(TicketArrayList tickets) {
		banner("Getting tickets for archiving");
		// Creates internal structure for holding tickets that will be appropriate for Archiving
		ArrayList<Ticket> filteredTickets = new ArrayList<Ticket>();

		// Loops through all tickets and checks for their status and expiry time
		tickets.rawList().forEach((t) -> {
			if (t.getStatus() == TicketStatus.CLOSED && LocalDateTime.now().isAfter(t.getClosedDate().plusHours(TICKET_EXPIRY_TIME)))
				// If fits, it's added to archive list
				filteredTickets.add(t);
		});

		return filteredTickets;
	}
	
	// Archives all the tickets passed
	private static void archiveTickets(ArrayList<Ticket> ticketsToArchive) {
		// Find tickets to be archived in the database and change their status
		ticketsToArchive.forEach((t) -> tickets.get(databaseSearch(DatabaseType.TICKET, t.getTicketID())).setStatus(TicketStatus.ARCHIVED));
	}

	// Retrieves tickets by passed status
	private static ArrayList<Ticket> getTicketsByStatus(TicketStatus status, LocalDateTime from, LocalDateTime to) {
		if (null == from) from = LocalDateTime.parse("01/01/1900 00:00", formatter);
		if (null == to) 	to = LocalDateTime.parse("01/01/2099 00:00", formatter);

		ArrayList<Ticket> byStatus = new ArrayList<Ticket>();
		tickets.rawList().forEach((t) -> {
			if(t.getStatus() == status)
				byStatus.add(t);
		});
		return byStatus;
	}

	// Function to view all tickets that are archived
	private static void viewSelectedTickets(ArrayList<Ticket> archivedTickets, String ticketType) {
		// Messaging
		String VIEWTICKET_BANNER = "View all " + ticketType + " tickets";
		banner(VIEWTICKET_BANNER);
		System.out.println("Displaying " + ticketType + " tickets:");

		archivedTickets.forEach((t) -> {
				displayEntry(databaseSearch(DatabaseType.TICKET, t.getTicketID()), DatabaseType.TICKET, "", false);
		});

		System.out.println("End of " + ticketType + " tickets display.");
	}
}
