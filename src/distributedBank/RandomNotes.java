package distributedBank;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class RandomNotes {
	int name_length = marshalled_name.length;
	int pwd_length  = marshalled_password.length;
	int currency_length = marshalled_currency.length;
	int amount_length = marshalled_amount.length;
	
	ByteBuffer d = ByteBuffer.allocate(4); //4 byte for all integers in this part
	b.putInt(opcode);
	byte[] opcode_Byte = b.array();
	
	b.clear();
	b.putInt(name_length);
	byte[] name_len_byte = b.array();
	b.clear();
	b.putInt(pwd_length);
	byte[] pwd_len_byte = b.array();
	b.clear();
	b.putInt(currency_length);
	byte[] currency_len_byte = b.array();
	b.clear();
	b.putInt(amount_length);
	byte[] amount_len_byte = b.array();
	
	
	ArrayList<Byte> output_list = new ArrayList<Byte>();
	
	
	Byte[] opcodeObject = ArrayUtils.toObject(opcode_byte);
	Byte[] nameObject = ArrayUtils.toObject(marshalled_name);
	Byte[] pwdObject = ArrayUtils.toObject(marshalled_password);
	Byte[] currencyObject = ArrayUtils.toObject(marshalled_currency);
	Byte[] amountObject = ArrayUtils.toObject(marshalled_amount);
	
	List<Byte> name_list = Arrays.asList(nameObject);
	List<Byte> pwd_list = Arrays.asList(pwdObject);
	List<Byte> currency_list = Arrays.asList(currencyObject);
	List<Byte> amount_list = Arrays.asList(amountObject);

	
	
	output_list.addAll(name_list);
	
	output_list.addAll(pwd_list);
	output_list.addAll(currency_list);
	output_list.addAll(amount_list);
	
	
	
	
	
	//convert byte arrays to Bytes, add the length before each byte array. After all is done, convert arraylist back to byte array.
	
	
	byte[] output = new byte[name_length + pwd_length + currency_length + amount_length];
	
	return output;

}
