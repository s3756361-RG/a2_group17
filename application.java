package application;

import java.io.*; 
import java.lang.*; 
import java.util.*; 


/* Appointment Management System class */
public class application {

	private static String [][] userDatabase = 
		{
				{"hstyles","Harry Styles","hstyles@cinco.com.au","12345","1"},
				{"nhoran","Niall Horan","nhoran@cinco.com.au","12345","1"},
				{"ltomlinson","Louis Tomlinson","ltomlinson@cinco.com.au","12345","2"},
				{"zmalik","Zayn Malik","zmalik@cinco.com.au","12345","2"},
				{"demouser","Demo User","demouser@cinco.com.au","12345","0"}
		};	

	private static final int USERNAME_INDEX = 0;
	private static final int NAME_INDEX = 1;
	private static final int EMAIL_INDEX = 2;
	private static final int PASSWD_INDEX = 3;
	private static final int ROLE_INDEX = 4;

	private static String ticketDatabase[][];
	private static int num_tickets = 0;

	private static final int TICKET_FNAME = 0;
	private static final int TICKET_LNAME = 1;
	private static final int TICKET_STAFFID = 2;
	private static final int TICKET_EMAIL = 3;
	private static final int TICKET_CONATACT = 4;
	private static final int TICKET_DESCRIPTION = 5;
	private static final int TICKET_SEVERITY = 6;
	private static final int TICKET_STATUS = 7;


	// shared Scanner which can be used by all helper methods below
	private static Scanner SC = new Scanner(System.in);

	// Main class
	public static void main(String[] args) {


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

			// Blank line for formatting
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


	private static void banner(String bannerName) {
		for (int i = 0; i < bannerName.length(); ++i) {
			System.out.print("=");
		}
		System.out.println();
		System.out.println(bannerName);
		for (int i = 0; i < bannerName.length(); ++i) {
			System.out.print("=");
		}
		System.out.println();
	}


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

		return input;
	}

	private static boolean check_login(final String[][] userDatabase, final String USERNAME, final String PASSWORD) {

		boolean valid = false;

		for(int i = 0; i < userDatabase.length; ++i){

			if(USERNAME.contentEquals(userDatabase[i][USERNAME_INDEX])) {
				if(PASSWORD.contentEquals(userDatabase[i][PASSWD_INDEX])) {
					valid = true;
					break;
				}
			}
		}           
		return valid;
	}

	private static void attemptLogin() {
		boolean valid = false;
		String ATTEMPTLOGIN_BANNER = "Login to Ticketing System";
		banner(ATTEMPTLOGIN_BANNER);

		System.out.println("\nPlease enter login credentials\n");

		System.out.print("Please enter your username: ");
		String username = get_user_input();
		System.out.print("Please enter your password: ");
		String passwd = get_user_input();

		System.out.println("\nYour username is: " + username);
		System.out.println("Your password is: " + passwd + '\n');
		//Check login details
		do {
			valid = check_login(userDatabase, username, passwd);
		} while(!valid);
		// Add more code here

		if (check_login(userDatabase, username, passwd))
		{
			System.out.println("Winner winner chicken dinner\n");

			// Menu system for the program
			String selection;
			// Check user input until the exit function is met, i.e. X or x

			do {
				// Display Menu Options
				final String MENU_BANNER = "IT ticketing system - Main Menu";
				banner(MENU_BANNER);

				System.out.println("\nA. Create an new ticket");
				System.out.println("B. View all open tickets");
				System.out.println("C. Change ticket priority (Technicians only)");
				System.out.println("D. Close ticket (Technicians only)");
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
						// check for being an Technician
						changeTicketPriority();
						break;

					case "D":
						// check for being an Technician
						closeTicket();
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

		}
		else
		{
			System.out.println("Incorrect Login");
		}



		System.out.println();
	}

	private static void createAccount() {
		String CREATEACC_BANNER = "Create account";
		banner(CREATEACC_BANNER);
		System.out.println("Prompts to create an account");

		// Add more code here
		System.out.print("Please enter a username: ");
		String username = get_user_input();

		// Check for uniqueness

		System.out.print("Please enter your name : ");
		String name = get_user_input();

		System.out.print("Please enter your email address: ");
		String email = get_user_input();

		// check validity

		// check for uniqueness

		System.out.print("Please enter your password: ");
		String passwd = get_user_input();

		System.out.print("Total number of users: " + userDatabase.length);

		//userDatabase = Arrays.copyOf(userDatabase, userDatabase.length + 1);

		for(int i=0; i < userDatabase.length; i++)
			for(int j = 0; j < userDatabase[i].length; j++)
				userDatabase[i][j]=userDatabase[i][j];

		//userDatabase[userDatabase.length - 1][0] = username;
		System.out.print("Total number of users: " + userDatabase.length);
		/*userDatabase[userDatabase.length - 1][0] = username;
		userDatabase[userDatabase.length - 1][1] = name;
		userDatabase[userDatabase.length - 1][2] = email;
		userDatabase[userDatabase.length - 1][3] = passwd;
		userDatabase[userDatabase.length - 1][4] = "0";*/

		//userDatabase[userDatabase.length - 1][0] = "sam";

		//System.out.print(userDatabase[userDatabase.length - 1][0]);


		// create account in array with username, email, password and privilege set to 0 as Staff and 1 as Technician 

		System.out.println("Account " + username + " created");
		System.out.println();
		System.out.println();
		for (int i = 0; i < userDatabase.length; i++) {
			System.out.print(userDatabase[i][0] + ": ");
			for (int j = 1; j < userDatabase[i].length; j++) {
				System.out.print(userDatabase[i][j] + " ");
			}
			System.out.println();
		}

		System.out.println();
	}

	private static void resetPassword() {
		String RESETPWD_BANNER = "Reset Password";
		banner(RESETPWD_BANNER);
		System.out.println("Prompts to reset password");

		// Testing printing out all users

		for (int i = 0; i < userDatabase.length; i++) {
			System.out.print(userDatabase[i][0] + ": ");
			for (int j = 1; j < userDatabase[i].length; j++) {
				System.out.print(userDatabase[i][j] + " ");
			}
			System.out.println();
		}

		// Add more code here

		System.out.println();
	}

	private static boolean check_severity(final String severity) {
		boolean valid = false;
		if(severity.contentEquals("1") || severity.contentEquals("2") || severity.contentEquals("3")) {
			valid = true;
		} else {
			System.out.println("Error! Please enter correct severity level (1 = Low | 2 = Medium | 3 = High): ");
		}
		return valid;
	}

	private static void createTicket(final String USERNAME)
	{
		String username = "";
		String[] full_name;
		String fname = "";
		final int FNAME_INDEX = 0;
		String lname = "";
		final int LNAME_INDEX = 1;
		String email = "";
		String contact_number = "";
		String staffID = "";
		String description = "";
		String severity = "";
		String confirmation = "";
		final String CREATETICKET_BANNER = "Create a ticket";
		banner(CREATETICKET_BANNER);
		System.out.println("\nEnter details for ticket below");

		//Search for user to pre enter user's details for ticket
		for(int i = 0; i < userDatabase.length; ++i){

			if(USERNAME.contentEquals(userDatabase[i][USERNAME_INDEX])) {
				username = userDatabase[i][USERNAME_INDEX];
				full_name = userDatabase[i][NAME_INDEX].split(" ");
				fname = full_name[FNAME_INDEX];
				lname = full_name[LNAME_INDEX];
				email = userDatabase[i][EMAIL_INDEX];
				break;
			}
		}
		//Get remaining data from user
		System.out.print("\nPlease enter your staffID: ");
		staffID = get_user_input();
		System.out.print("\nPlease enter your contact number: ");
		contact_number = get_user_input();
		System.out.print("\nPlease enter a brief description of the problem: ");
		description = get_user_input();
		System.out.print("\nPlease set the severity (1 = Low | 2 = Medium | 3 = High): ");
		do {
			severity = get_user_input();
		} while(!check_severity(severity));
		//Translate selection
		switch(severity) {
		case "1" :
			severity = "Low";
			break;
		case "2" :
			severity = "Medium";
			break;
		case "3" :
			severity = "High";
			break;
		}
		//Display final ticket to user to check
		System.out.println("Check the final ticket below");
		System.out.println("\nFirst Name: " + fname + "\nLast Name: " + lname + "\nStaffID: " + staffID + "\nEmail: "
				+ email + "\nContact Number: " + contact_number + "\nDescription: " + description + "\nSeverity: " + severity);
		System.out.print("\nIf the above details are correct type 'Y' else 'N' to restart: ");
		confirmation = get_user_input();
		if(!confirmation.toUpperCase().contentEquals("Y")) {
			createTicket(username);
		}
		//Create ticket
		ticketDatabase[num_tickets][TICKET_FNAME] = fname;
		ticketDatabase[num_tickets][TICKET_LNAME] = lname;
		ticketDatabase[num_tickets][TICKET_STAFFID] = staffID;
		ticketDatabase[num_tickets][TICKET_EMAIL] = email;
		ticketDatabase[num_tickets][TICKET_CONATACT] = contact_number;
		ticketDatabase[num_tickets][TICKET_DESCRIPTION] = description;
		ticketDatabase[num_tickets][TICKET_SEVERITY] = severity;
		ticketDatabase[num_tickets][TICKET_STATUS] = "Open";
		++num_tickets;
		
		System.out.println("\nTicket successfully created!");
	}

	private static void viewTickets()
	{
		String VIEWTICKET_BANNER = "View your tickets";
		banner(VIEWTICKET_BANNER);
		System.out.println("Show me all your tickets");
		// show tickets
		// if tech show closed ones too
	}

	private static void changeTicketPriority()
	{
		String CHGTICKETPR_BANNER = "Change ticket priority";
		banner(CHGTICKETPR_BANNER);
		System.out.println("Which ticket would you like to change priority for: ");
		// code to change ticket priority
	}

	private static void closeTicket()
	{
		String CLOSETICKET_BANNER = "Ticket closure";
		banner(CLOSETICKET_BANNER);
		System.out.println("Which ticket would you like to close: ");
		// code to close ticket
	}

}