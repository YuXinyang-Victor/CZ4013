package distributedBank;
import java.util.Scanner;

//We should do all input validation checkings in this class

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
		
		while(true) {
			
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
							break; 
						}
						
					}
					System.out.println("Please input the amount you want to deposit:");
					double deposit_amount = scanner.nextDouble();
					
					System.out.println("Processing your request, please wait...");
					//begin to summarize the inputs for message module
					break; 
					
					
				}
				
				case 2: {
					//close account part
					System.out.println("You are about to close an existing bank account. This action cannot be reversed.");
					System.out.println("Please input your name: ");
					String name = scanner.nextLine();
					//check length of name
					
					System.out.println("Please input your account number: ");
					int account_number = scanner.nextInt();
					//validation check of whether the input is an integer
					
					System.out.println("Please input your password: ");
					String password = scanner.nextLine();
					String password_hash = hash_generator.generateHash(password);
					
					System.out.println("Processing your request, please wait...");
					//begin to summarize the inputs for message module
					break;
										
				}
				
				case 3: {
					//deposit part
					System.out.println("You have selected the deposit service. ");
					System.out.println("Please input your name: ");
					String name = scanner.nextLine();
					//check length of name
					
					System.out.println("Please input your account number: ");
					int account_number = scanner.nextInt();
					//validation check of whether the input is an integer
					
					System.out.println("Please input your password: ");
					String password = scanner.nextLine();
					String password_hash = hash_generator.generateHash(password);
					
					System.out.println("Please input the amount you want to deposit:");
					double deposit_amount = scanner.nextDouble();
					
					System.out.println("Processing your request, please wait...");
					//begin to summarize the inputs for message module
					break;
				}
				
				case 4: {
					//withdraw part
					System.out.println("You have selected the cash withdrawal service. ");
					System.out.println("Please input your name: ");
					String name = scanner.nextLine();
					//check length of name
					
					System.out.println("Please input your account number: ");
					int account_number = scanner.nextInt();
					//validation check of whether the input is an integer
					
					System.out.println("Please input your password: ");
					String password = scanner.nextLine();
					String password_hash = hash_generator.generateHash(password);
					
					System.out.println("Please input the amount you want to wihtdraw:");
					double deposit_amount = scanner.nextDouble();
					
					System.out.println("Processing your request, please wait...");
					//begin to summarize the inputs for message module
					break;
				}
				
				case 5: {
					//transfer part
					System.out.println("You have selected the amount transfer service. ");
					System.out.println("Please input your name: ");
					String from_name = scanner.nextLine();
					//check length of name
					
					System.out.println("Please input your account number: ");
					int from_account_number = scanner.nextInt();
					//validation check of whether the input is an integer
					
					System.out.println("Please input your password: ");
					String password = scanner.nextLine();
					String password_hash = hash_generator.generateHash(password);
					
					System.out.println("Please input the amount you want to transfer:");
					double deposit_amount = scanner.nextDouble();
					
					System.out.println("Please input the target account name: ");
					String to_name = scanner.nextLine();
					//check length of name
					
					System.out.println("Please input the target account number: ");
					int to_account_number = scanner.nextInt();
					//validation check of whether the input is an integer
					
					System.out.println("Processing your request, please wait...");
					//begin to summarize the inputs for message module
					break;
				}
				
				case 6: {
					//change currency type part
					System.out.println("You have selected the change currency type service. ");
					System.out.println("Please input your name: ");
					String name = scanner.nextLine();
					//check length of name
					
					System.out.println("Please input your account number: ");
					int account_number = scanner.nextInt();
					//validation check of whether the input is an integer
					
					System.out.println("Please input your password: ");
					String password = scanner.nextLine();
					String password_hash = hash_generator.generateHash(password);
					
					while(true) {
						System.out.println("Please select a currency type for your account.");
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
							break; 
						}
					}
					
					
					System.out.println("Processing your request, please wait...");
					//begin to summarize the inputs for message module
					break;
				}
				
				case 7: {
					//exit part
					System.out.println("Thank you for using our banking system.");
					return;
				}
				
				default: {
					//display error message
					System.out.println("Sorry, your input is invalid. Please reselect your option. Directing back to main page...");
					
				}
			}
		}
		
		
	}
}
