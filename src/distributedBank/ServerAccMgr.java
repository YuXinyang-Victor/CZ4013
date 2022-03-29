package distributedBank;

import java.io.IOException;
import java.util.HashMap;

//change this class into a singleton class.

public class ServerAccMgr {
	
	private static ServerAccMgr server_account_manager = null;
	
	HashMap<Integer, Account> acc_list; 
	int next_acc_number; 
	//List of observers
	
	public static ServerAccMgr getManager() {
		if (server_account_manager == null)
			server_account_manager = new ServerAccMgr();
 
        return server_account_manager;
	}
	
	private ServerAccMgr() {
		next_acc_number = 0;
		
		acc_list = new HashMap<Integer, Account>();
	}

	//acc creation
	public int createAccount(String name_in, String passwordHash_in, CurrencyType currency_in, Double initAccBalance_in) {
		Integer account_number = next_acc_number; 
		next_acc_number += 1; //as early as possible to reserve the number
		account_number = acc_list.size();
		Account new_acc = new Account(account_number.intValue(), name_in, passwordHash_in, currency_in, initAccBalance_in);//call the constructor to create a new object
		acc_list.put(account_number, new_acc); //reference to the object from the acclist 
		//notifyAllObservers(account_number.intValue());
		System.out.println(acc_list);
		try {
			startCallback(account_number.intValue());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return account_number; 
	}
	
	
	//acc closing (including pwd check)
	public boolean closeAccount(int account_number, String name_in, String passwordHash_in) throws IOException {
		boolean isValid = checkProvidedInfo(account_number, name_in, passwordHash_in); 
		System.out.println("checking if details are valid");
		if (isValid) {
			System.out.println("details are valid");
			acc_list.remove(Integer.valueOf(account_number));
			try {
				startCallback(account_number);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true; 
		}
		else {
			//trigger wrong name pwd acctno combination notification
			System.out.println("details are not valid");
			sendErrorMessage(1);
			return false; 
		}
	}
	
	//check provided information and validate action
	public boolean checkProvidedInfo(int account_number, String name_in, String passwordHash_in) {
		Account to_be_checked = acc_list.get(Integer.valueOf(account_number)); 
		if (to_be_checked == null) {
			return false; 
		}
		return ((account_number == to_be_checked.getAccNumber()) && (name_in.equals(to_be_checked.getName())) && (passwordHash_in.equals(to_be_checked.getHash())) );
	}
	
	public Double updateAccBalance(int account_number, String name_in, String passwordHash_in, Double offset) throws IOException {
		boolean isValid = checkProvidedInfo(account_number, name_in, passwordHash_in); 
		if (isValid) {
			Double new_balance = acc_list.get(Integer.valueOf(account_number)).updateAccBalance(offset);
			if (new_balance == null) {
				//trigger not enough balance notification
				sendErrorMessage(2);
			}
			else {
				try {
					startCallback(account_number);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return new_balance; 
		}
		else {
			//trigger wrong name pwd acctno combination notification
			sendErrorMessage(1);
			return null; //The interface calling this manager need to realize that any null means something went wrong, and the error is alr handled by mgr
		}
		
	}
	
	
	//for the two additional parts, we can do change account currency type (use enum exchange rate) and amount transfer
	public boolean changeAccCurrency(int account_number, String name_in, String passwordHash_in, CurrencyType currency_in) throws IOException {
		boolean isValid = checkProvidedInfo(account_number, name_in, passwordHash_in); 
		if (isValid) {
			acc_list.get(Integer.valueOf(account_number)).updateCurrency(currency_in);
			try {
				startCallback(account_number);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//do conversion of deposit amount according to rates
			return true; 
		}
		else {
			sendErrorMessage(1);
			return false; 
		}
	}
	
	public Double amountTransfer(int from_account_number, String from_name, String from_passwordHash, Double amount, int to_account_number, String to_name) throws IOException {
		boolean isValid = checkProvidedInfo(from_account_number, from_name, from_passwordHash); 
		if (isValid) {
			
			Account from_account = acc_list.get(Integer.valueOf(from_account_number));
			Account to_account = acc_list.get(Integer.valueOf(to_account_number));
			
			if (to_account != null) {
			
				CurrencyType from_currency = to_account.getCurrency();
				CurrencyType to_currency = to_account.getCurrency(); 
				
				if (from_currency != to_currency) {
					//trigger currency types do not match, transfer not allowed notification
					sendErrorMessage(3);
				}
				
			
				Double new_balance_from = from_account.updateAccBalance(-amount);
				
				if(new_balance_from == null) {
					//trigger not enough balance notification
					sendErrorMessage(2);
					return null;
				}
				
				Double new_balance_to = to_account.updateAccBalance(amount);
				try {
					startCallback(to_account_number);
					startCallback(from_account_number);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
				return new_balance_from; 
			}
			
			else {
				//trigger target account not found notification
				sendErrorMessage(4);
				return null;
			}
		}
		else {
			//trigger wrong name pwd acctno combination notification
			sendErrorMessage(1);
			return null; 
		}
	}
	
	
	//error handling functions that send a msg to user indicating the type of error encountered
	public void sendErrorMessage(int err_type) throws IOException {
		String err_msg = new String();
		switch(err_type) {
		case 1 : err_msg = "Wrong name, password, or account number"; break;
		case 2 : err_msg = "Not enough balance in account"; break;
		case 3 : err_msg = "The currency types are different"; break;
		case 4 : err_msg = "The target account is not found"; break;
		}
		
		ServerMain.sendError(err_msg);
	}
	//wrong name pwd acctno combination notification - 1
	//not enough balance notification - 2
	//currency types do not match, transfer not allowed notification - 3
	//target account not found notification - 4
	
	//update callback
	//triggering function called after any updates to bank accounts (creation, closing, deposit, withdrawal), this function sends out a msg to update observers
	public void startCallback(int acc_number) throws IOException {
		Account updated_acc = acc_list.get(acc_number);
		if (updated_acc == null) {
			String msg = "Account " + acc_number + " has been closed";
			byte[] msg_byte = msg.getBytes();
			try {
				CallbackMgr cb_mgr = CallbackMgr.getCallbackMgr();
				cb_mgr.callClient(msg_byte);
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
			ServerMain.updateHandler(updated_acc);
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	
	
	
}
