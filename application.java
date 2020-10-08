import java.util.NoSuchElementException;
import java.util.Scanner;

public class application {

	private final String LOGIN_MSG = "Welcome to the IT ticketing system";
	private final String USERNAME_PROMPT = "Please enter your username: ";
	private final String PASSWD_PROMT = "Please enter your password: ";
	private final Scanner SC = new Scanner(System.in);

	public application() {
		String username = "";
		String passwd = "";

		display_login_msg();
		display_msg(USERNAME_PROMPT);
		username = get_user_input();
		display_msg(PASSWD_PROMT);
		passwd = get_user_input();
		SC.close();
	}

	public boolean display_login_msg() {

		System.out.println(LOGIN_MSG);
		for(int i = 0; i < LOGIN_MSG.length(); ++i) {
			System.out.print("=");
		}
		System.out.println('\n');
		return true;
	}

	public void display_msg(String msg) {
		System.out.print(msg);
	}

	public String get_user_input() {
		String input = "";
		boolean valid = false;
		do {
			input = SC.nextLine();
			if(input.length() < 1) {
				display_msg("Error, please enter valid input:");
				valid =false;
			} else {
				valid = true;
			}
		}while(valid == false);

		return input;
	}

	public static void main(String[] args) {

		application ap = new application();

	}


}
