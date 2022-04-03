package distributedBank;
import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Main UI, client entry
 */

public class distributedBank extends Thread{
	
	public static void welcomeDisplay() {
		System.out.println("Welcome to distributed banking system, please follow the instructions and select your service.");
		System.out.println("1: Create an account");
		System.out.println("2: Close an account");
		System.out.println("3: Deposit cash");
		System.out.println("4: Withdraw cash");
		System.out.println("5: Transfer");
		System.out.println("6: Change currency type of account");
		System.out.println("0: Register for account monitoring");
		System.out.println("7: Exit");
		
		System.out.println("Please select a type of service: ");
	}
	
	public static void displayMsg(String message) {
		System.out.println(message);
		
	}
	
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		ClientComm client_comm = ClientComm.getClientComm();
		SendDriver send_driver = SendDriver.getDriver();
		//client_comm.clientListen();
		
		distributedBank thread = new distributedBank();
	    thread.start();
	    
		while(true) {
			
			welcomeDisplay();
			
			int service_type;
			while (true) {
			String service_type_str = scanner.nextLine();
			try {
				service_type = Integer.parseInt(service_type_str);break;
			}
			catch (NumberFormatException e) {
				System.out.println("Please enter a number of your choice");
			}
			}
			
			switch(service_type) {
				case 0: {
					System.out.println("you are registering for the account monitoring system.");
					client_comm.clientRegister();
					
					System.out.println("Processing your request, please wait...");
					break;
				}
				case 1: {
					//create account part
					String name; 
					String password; 
					double deposit_amount = 0; 
					
					while (true) {
						System.out.println("You are about to create a new bank account. ");
						System.out.println("Please input your name (no longer than 32 characters): ");
						name = scanner.nextLine();
						//check length of name
						if (name.length() <= 32) break;
						System.out.println("name should be no longer than 32 characters");
					}
					
					
					while (true) {
						System.out.println("Please input your password, the password should be 6 characters in length: ");
						password = scanner.nextLine();
						if(password.length() == 6) break;
						System.out.println("The password must be 6 characters long. ");
					}
					
					int currency_type = 0;
					String[] types = new String[4];
					
					while(true) {
						System.out.println("Please select a currency type for your account. This can be changed later.");
						int count = 1;
						for (CurrencyType type: CurrencyType.values()) {
							types[count-1] = type.name();
							System.out.println(count + ": " + type);
							count ++;
						}
						String input_type = scanner.nextLine();
						try {
							currency_type = Integer.parseInt(input_type);
							if (currency_type < 1 || currency_type >= count) {
								System.out.println("Sorry, you have selected a currency type that we do not support. Please choose from the list. ");
							}
							else {
								break; 
							}
						}
						catch  (NumberFormatException e) {
							System.out.println("Sorry, please enter a valid number");
							
						}
						
						
						
						
					}
					
					String type_str = types[currency_type-1];
					
					while(true) {
						System.out.println("Please input the amount you want to deposit:");
						String deposit_amount_str = scanner.nextLine();
						try {
							deposit_amount = Double.parseDouble(deposit_amount_str);
							if(deposit_amount < 0) {
								System.out.println("You must provide a deposit amount >= 0");
							}
							else break;
						}
						catch (NumberFormatException e) {
							System.out.println("Sorry, please enter a valid number");
						}
					}
					
					
					System.out.println("Processing your request, please wait...");
					
					//begin to summarize the inputs for message module
					byte[] msg_byte = ClientMarshal.marshal(name, password, type_str, deposit_amount);
					
					//send_driver.send(msg_byte);  This line is for simulation retransmission
					client_comm.clientSend(msg_byte);
					
					break; 
					
					
				}
				
				case 2: {
					//close account part
					System.out.println("You are about to close an existing bank account. This action cannot be reversed.");
					String name; 
					while (true) {
						System.out.println("Please input your name: ");
						name = scanner.nextLine();
						//check length of name
						if(name.length() <= 32) break;
						System.out.println("name should be no longer than 32 characters");
					}
					
					int account_number;
					while(true) {
						System.out.println("Please input your account number: ");
						String acc_no_str = scanner.nextLine();
						try {//validation check of whether the input is an integer
							account_number = Integer.parseInt(acc_no_str);
							break;
					}
						catch (NumberFormatException e) {
							System.out.println("Please input a valid account number");
							
					}
					}
					
					
					System.out.println("Please input your password: ");
					String password = scanner.nextLine();
					
					
					System.out.println("Processing your request, please wait...");
					
					//begin to summarize the inputs for message module
					byte[] msg_byte = ClientMarshal.marshal(name, account_number, password);
					
					//send_driver.send(msg_byte);  This line is for simulation retransmission
					client_comm.clientSend(msg_byte);
					
					break;
										
				}
				
				case 3: {
					//deposit part
					double deposit_amount = 0;
					
					System.out.println("You have selected the deposit service. ");
					String name; 
					while (true) {
						System.out.println("Please input your name: ");
						name = scanner.nextLine();
						//check length of name
						if(name.length() <= 32) break;
						System.out.println("name should be no longer than 32 characters");
					}
					//check length of name
					
					int account_number;
					while(true) {
						System.out.println("Please input your account number: ");
						String acc_no_str = scanner.nextLine();
						try {//validation check of whether the input is an integer
							account_number = Integer.parseInt(acc_no_str);
							break;
					}
						catch (NumberFormatException e) {
							System.out.println("Please input a valid account number");
							
					}
					}
					//validation check of whether the input is an integer
					
					System.out.println("Please input your password: ");
					String password = scanner.nextLine();
					
					while(true) {
						System.out.println("Please input the amount you want to deposit:");
						String deposit_amount_str = scanner.nextLine();
						try {
							deposit_amount = Double.parseDouble(deposit_amount_str);
							if(deposit_amount < 0) {
								System.out.println("You must provide a deposit amount >= 0");
							}
							else break;
						}
						catch (NumberFormatException e) {
							System.out.println("Sorry, please enter a valid number");
						}
					}
					
					System.out.println("Processing your request, please wait...");
					
					//begin to summarize the inputs for message module
					byte[] msg_byte = ClientMarshal.marshal(name, account_number, password, deposit_amount);
					
					//send_driver.send(msg_byte);  This line is for simulation retransmission
					client_comm.clientSend(msg_byte);
					
					break;
				}
				
				case 4: {
					//withdraw part
					double withdraw_amount = 0;
					
					System.out.println("You have selected the cash withdrawal service. ");
					String name; 
					while (true) {
						System.out.println("Please input your name: ");
						name = scanner.nextLine();
						//check length of name
						if(name.length() <= 32) break;
						System.out.println("name should be no longer than 32 characters");
					}
					//check length of name
					
					System.out.println("Please input your account number: ");
					int account_number;
					while(true) {
						System.out.println("Please input your account number: ");
						String acc_no_str = scanner.nextLine();
						try {//validation check of whether the input is an integer
							account_number = Integer.parseInt(acc_no_str);
							 break;
					}
						catch (NumberFormatException e) {
							System.out.println("Please input a valid account number");
							
					}
					}
					//validation check of whether the input is an integer
					
					System.out.println("Please input your password: ");
					String password = scanner.nextLine();
					
					
					while(true) {
						System.out.println("Please input the amount you want to withdraw:");
						String withdraw_amount_str = scanner.nextLine();
						try {
							withdraw_amount = Double.parseDouble(withdraw_amount_str);
							if(withdraw_amount < 0) {
								System.out.println("You must provide a withdrawal amount >= 0");
							}
							else break;
						}
						catch (NumberFormatException e) {
							System.out.println("Sorry, please enter a valid number");
						}
					}
					
					System.out.println("Processing your request, please wait...");
					
					//begin to summarize the inputs for message module
					byte[] msg_byte = ClientMarshal.marshal(name, account_number, password, -withdraw_amount); 
					
					//send_driver.send(msg_byte);  This line is for simulation retransmission
					client_comm.clientSend(msg_byte);
					
					break;
				}
				
				case 5: {
					//transfer part
					double transfer_amount = 0;
					
					System.out.println("You have selected the amount transfer service. ");
					String from_name; 
					while (true) {
						System.out.println("Please input your name: ");
						from_name = scanner.nextLine();
						//check length of name
						if(from_name.length() <= 32) break;
						System.out.println("name should be no longer than 32 characters");
					}
					//check length of name
					
					System.out.println("Please input your account number: ");
					int from_account_number;
					while(true) {
						System.out.println("Please input your account number: ");
						String acc_no_str = scanner.nextLine();
						try {//validation check of whether the input is an integer
							from_account_number = Integer.parseInt(acc_no_str);
							break;
					}
						catch (NumberFormatException e) {
							System.out.println("Please input a valid account number");
							
					}
					}
					//validation check of whether the input is an integer
					
					System.out.println("Please input your password: ");
					String password = scanner.nextLine();
					
					while(true) {
						System.out.println("Please input the amount you want to transfer:");
						String transfer_amount_str = scanner.nextLine();
						try {
							transfer_amount = Double.parseDouble(transfer_amount_str);
							if(transfer_amount < 0) {
								System.out.println("You must provide a transfer amount >= 0");
							}
							else break;
						}
						catch (NumberFormatException e) {
							System.out.println("Sorry, please enter a valid number");
						}
					}
					
					String to_name; 
					while (true) {
						System.out.println("Please input the target account name: ");
						to_name = scanner.nextLine();
						//check length of name
						if(to_name.length() <= 32) break;
						System.out.println("name should be no longer than 32 characters");
					}
					
					
					System.out.println("Please input the target account number: ");
					int to_account_number;
					while(true) {
						System.out.println("Please input your account number: ");
						String acc_no_str = scanner.nextLine();
						try {//validation check of whether the input is an integer
							to_account_number = Integer.parseInt(acc_no_str);
							break;
					}
						catch (NumberFormatException e) {
							System.out.println("Please input a valid account number");
							
					}
					}
					//validation check of whether the input is an integer
					
					System.out.println("Processing your request, please wait...");
					
					//begin to summarize the inputs for message module
					byte[] msg_byte = ClientMarshal.marshal(from_name, from_account_number, password, transfer_amount, to_name, to_account_number);
					
					//send_driver.send(msg_byte);  This line is for simulation retransmission
					client_comm.clientSend(msg_byte);
					
					break;
				}
				
				case 6: {
					//change currency type part
					System.out.println("You have selected the change currency type service. ");
					String name; 
					while (true) {
						System.out.println("Please input your name: ");
						name = scanner.nextLine();
						//check length of name
						if(name.length() <= 32) break;
						System.out.println("name should be no longer than 32 characters");
					}
					//check length of name

					int account_number;
					while(true) {
						System.out.println("Please input your account number: ");
						String acc_no_str = scanner.nextLine();
						try {//validation check of whether the input is an integer
							account_number = Integer.parseInt(acc_no_str);
							break;
					}
						catch (NumberFormatException e) {
							System.out.println("Please input a valid account number");
							
					}
					}
					//validation check of whether the input is an integer
					
					System.out.println("Please input your password: ");
					String password = scanner.nextLine();
					
					int currency_type = 0;
					String[] types = new String[4];
					
					while(true) {
						System.out.println("Please select a currency type for your account.");
						int count = 1;
						for (CurrencyType type: CurrencyType.values()) {
							types[count-1] = type.name();
							System.out.println(count + ": " + type);
							count ++;
						}
						String input_type = scanner.nextLine();
						try {
							currency_type = Integer.parseInt(input_type);
							if (currency_type < 1 || currency_type >= count) {
								System.out.println("Sorry, you have selected a currency type that we do not support. Please choose from the list. ");
							}
							else {
								break; 
							}
						}
						catch  (NumberFormatException e) {
							System.out.println("Sorry, please enter a valid number");
							
						}
					}
					String type_str = types[currency_type-1];
					
					
					System.out.println("Processing your request, please wait...");
					
					//begin to summarize the inputs for message module
					byte[] msg_byte = ClientMarshal.marshal(name, account_number, password, type_str);
					
					//send_driver.send(msg_byte);  This line is for simulation retransmission
					client_comm.clientSend(msg_byte);
					
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
			
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void run() {
		ClientComm client_comm;
		try {
			client_comm = ClientComm.getClientComm();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();return;
		}
		try {
			client_comm.clientListen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
