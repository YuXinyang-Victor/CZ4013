package distributedBank;


//The current design is to keep an array of opened accounts for reference, each account is uniquely identified with the name and accnumber, 
//but the accnumber is used as unique identification
public class Account {
	int accNumber; 
	String name; //name must be provided in lower case characters, e.g. john_smith
	String passwordHash; 
	CurrencyType currency; //we only allow deposit withdraw in the same currency. We do not provide currency exchange service directly
	Double accBalance; 
	
	public Account(int accNumber_in, String name_in, String passwordHash_in, CurrencyType currency_in, double initAccBalance_in) { 	//generator for class, used for opening accounts
		// assign accNumber line
		accNumber = accNumber_in; 
		
		name = name_in; 
		
		passwordHash = passwordHash_in; //The hash should be done at client side after input and before transmission for safety 
		
		currency = currency_in; 
		
		accBalance = initAccBalance_in; //cannot be less than 0, check user input at client side! 
		
		//For the constructor function, we do not return anything. The return account number part should be done by the interface by calling getAccNumber()
	}
	
	public int getAccNumber() {
		return accNumber; 
	}
	
	//for close account, just remove it from the array of bank accounts
	
	public String getName() {
		return name; 
	}
	
	public String getHash() {
		return passwordHash; 
	}
	
	public CurrencyType getCurrency() {
		return currency; 
	}
	
	public Double getAccBalance() {
		return accBalance; 
	}
	
	public Double updateAccBalance(Double offset) {
		if ((accBalance + offset) >= 0) {
		accBalance += offset; 
		return accBalance; //return updated account balance to user
		}
		else {
			return null; 
		}
	}
	
	public void updateCurrency(CurrencyType currency_in) {
		currency = currency_in; 
	}
}
