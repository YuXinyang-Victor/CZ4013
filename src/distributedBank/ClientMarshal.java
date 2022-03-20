package distributedBank;

import java.util.ArrayList;

public class ClientMarshal {
	
	public byte[] marshalAccNumber(int account_number) {
		Integer account_number_object = Integer.valueOf(account_number);
		byte[] output = account_number_object.toString().getBytes();
		return output;
		
	}
	
	public byte[] marshalName(String name) {
		byte[] output = name.getBytes();
		return output;
	}
	
	public byte[] marshalPassword(String password_hash) {
		byte[] output = password_hash.getBytes();
		return output;
		
	}
	
	public byte[] marshalAmount(double amount) {
		Double amount_object = Double.valueOf(amount);
		byte[] output = amount_object.toString().getBytes();
		return output;
		
	}
	
	public byte[] marshalCurrencyType(CurrencyType currency) {
		byte[] output = currency.name().getBytes();
		return output;
	}
	
	//We will be using polymorphism to deal with different marshaling requirements
	public byte[] marshal1(String name, String password_hash, CurrencyType currency, double amount) {
		int field_count = 4;
		int opcode = 1;
		
		byte[] marshalled_name = marshalName(name);
		byte[] marshalled_password = marshalPassword(password_hash);
		byte[] marshalled_currency = marshalCurrencyType(currency);
		byte[] marshalled_amount = marshalAmount(amount);
		
		int name_length = marshalled_name.length;
		int pwd_length  = marshalled_password.length;
		int currency_length = marshalled_currency.length;
		int amount_length = marshalled_amount.length;
		
		ArrayList<Byte> output_list = new ArrayList<Byte>();
		
		//convert byte arrays to Bytes, add the length before each byte array. After all is done, convert arraylist back to byte array.
		
		
		byte[] output = new byte[name_length + pwd_length + currency_length + amount_length];
		
	}

}
