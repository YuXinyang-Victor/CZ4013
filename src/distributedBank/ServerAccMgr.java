package distributedBank;

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
		notifyAllObservers(account_number.intValue());
		return account_number; 
	}
	
	
	//acc closing (including pwd check)
	public boolean closeAccount(int account_number, String name_in, String passwordHash_in) {
		boolean isValid = checkProvidedInfo(account_number, name_in, passwordHash_in); 
		if (isValid) {
			acc_list.remove(Integer.valueOf(account_number));
			return true; 
		}
		else {
			//trigger wrong name pwd acctno combination notification
			return false; 
		}
	}
	
	//check provided information and validate action
	public boolean checkProvidedInfo(int account_number, String name_in, String passwordHash_in) {
		Account to_be_checked = acc_list.get(Integer.valueOf(account_number)); 
		if (to_be_checked == null) {
			return false; 
		}
		return ((account_number == to_be_checked.getAccNumber()) && (name_in == to_be_checked.getName()) && (passwordHash_in == to_be_checked.getHash()) );
	}
	
	public Double updateAccBalance(int account_number, String name_in, String passwordHash_in, Double offset) {
		boolean isValid = checkProvidedInfo(account_number, name_in, passwordHash_in); 
		if (isValid) {
			Double new_balance = acc_list.get(Integer.valueOf(account_number)).updateAccBalance(offset);
			if (new_balance == null) {
				//trigger not enough balance notification
			}
			return new_balance; 
		}
		else {
			//trigger wrong name pwd acctno combination notification
			return null; //The interface calling this manager need to realize that any null means something went wrong, and the error is alr handled by mgr
		}
		
	}
	
	
	//for the two additional parts, we can do change account currency type (use enum exchange rate) and amount transfer
	public boolean changeAccCurrency(int account_number, String name_in, String passwordHash_in, CurrencyType currency_in) {
		boolean isValid = checkProvidedInfo(account_number, name_in, passwordHash_in); 
		if (isValid) {
			acc_list.get(Integer.valueOf(account_number)).updateCurrency(currency_in);
			//do conversion of deposit amount according to rates
			return true; 
		}
		else {
			return false; 
		}
	}
	
	public Double amountTransfer(int from_account_number, String from_name, String from_passwordHash, Double amount, int to_account_number, String to_name) {
		boolean isValid = checkProvidedInfo(from_account_number, from_name, from_passwordHash); 
		if (isValid) {
			
			Account from_account = acc_list.get(Integer.valueOf(from_account_number));
			Account to_account = acc_list.get(Integer.valueOf(to_account_number));
			
			if (to_account != null) {
			
				CurrencyType from_currency = to_account.getCurrency();
				CurrencyType to_currency = to_account.getCurrency(); 
				
				if (from_currency != to_currency) {
					//trigger currency types do not match, transfer not allowed notification
				}
				
			
				Double new_balance_from = from_account.updateAccBalance(-amount);
				
				if(new_balance_from == null) {
					//trigger not enough balance notification
					return null;
				}
				
				Double new_balance_to = to_account.updateAccBalance(amount);
				
				
			
				return new_balance_from; 
			}
			
			else {
				//trigger target account not found notification
				return null;
			}
		}
		else {
			//trigger wrong name pwd acctno combination notification
			return null; 
		}
	}
	
	//triggering function called after any updates to bank accounts (creation, closing, deposit, withdrawal), this function sends out a msg to update observers
	public void notifyAllObservers(int account_number) {
		Account updated_account = acc_list.get(Integer.valueOf(account_number));
		//send out info using list of observers, we need to decide if we send out instance or send out the value of fields. The easier one preferred. 
	}
	
	//error handling functions that send a msg to user indicating the type of error encountered
	//wrong name pwd acctno combination notification
	//not enough balance notification
	//currency types do not match, transfer not allowed notification
	//not enough balance notification
	//target account not found notification
	
	//
	
	
	
}
