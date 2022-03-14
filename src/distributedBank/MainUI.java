package distributedBank;
import java.util.Scanner;

public class MainUI {
	
	public static void welcomeDisplay() {
		System.out.println("Welcome to distributed banking system, please follow the instructions and select your service.");
		System.out.println("1: Create an account");
		System.out.println("2: Close an account");
		System.out.println("3: Deposit cash");
		System.out.println("4: Withdraw cash");
		System.out.println("5: Transfer");
		System.out.println("6: Change currency type of account");
		System.out.println("7: Exit");
		System.out.println("Please select a type of service: ");
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		GenerateHash hash_generator = new GenerateHash();
		
		welcomeDisplay();
		
		int service_type = scanner.nextInt(); 
		
		switch(service_type) {
			case 1: {
				//create account part
				System.out.println("You are about to create a new bank account. ");
				System.out.println("Please input your name (no longer than 32 characters): ");
				String name = scanner.nextLine();
				//check length of name
				
				System.out.println("Please input your password: ");
				String password = scanner.nextLine();
				String password_hash = hash_generator.generateHash(password);
				
				while(true) {
					System.out.println("Please select a currency type for your account. This can be changed later.");
					int count = 1;
					for (CurrencyType type: CurrencyType.values()) {
						System.out.println(count + ": " + type);
						count ++;
					}
					int currency_type = scanner.nextInt();
					if (currency_type < 1 || currency_type > count) {
						System.out.println("Sorry, you have selected a currency type that we do not support. Please choose from the list. ");
					}
					else {
						//begin to summarize the inputs for message module
						break; 
					}
				}
				break; 
				
				
			}
			
			case 2: {
				//close account part
			}
			
			case 3: {
				//deposit part
			}
			
			case 4: {
				//withdraw part
			}
			
			case 5: {
				//transfer part
			}
			
			case 6: {
				//change currency type part
			}
			
			case 7: {
				//exit part
			}
			
			default: {
				//display error message
			}
		}
		
	}
}
