import java.util.Scanner;

public class application {
	
	private final String login_msg = "Welcome to the IT ticketing system";
	private final String username_prompt = "Please enter your username: ";
	
	public application() {
		String username = "";
		
		display_login_msg();
		display_msg(username_prompt);
		username = get_user_input();
	}
	
	public boolean display_login_msg() {
		
		System.out.println(login_msg);
		for(int i = 0; i < login_msg.length(); ++i) {
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
		Scanner sc = new Scanner(System.in);
		input = sc.nextLine();
		sc.close();
		return input;
	}

	public static void main(String[] args) {
		
		application ap = new application();

	}
	

}
