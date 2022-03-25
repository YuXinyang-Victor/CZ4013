package distributedBank;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;


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
	public byte[] marshal(String name, String password_hash, CurrencyType currency, double amount) {
		int field_count = 4;
		int opcode = 1;
		int len_opcode = 4;
		int len_uuid = 0;
		
		byte[] marshalled_name = marshalName(name);
		byte[] marshalled_password = marshalPassword(password_hash);
		byte[] marshalled_currency = marshalCurrencyType(currency);
		byte[] marshalled_amount = marshalAmount(amount);
		
		List<byte[]> parameter_list = new ArrayList<byte[]>();
		List<byte[]> length_list = new ArrayList<byte[]>();
		
		parameter_list.add(marshalled_name);
		parameter_list.add(marshalled_password);
		parameter_list.add(marshalled_currency);
		parameter_list.add(marshalled_amount);
		
		Iterator<byte[]> iter_par = parameter_list.iterator();
		ByteBuffer b = ByteBuffer.allocate(4); //4 byte for all integers in this part
		
		int length_int = 0;
		int length_sum = 0;
		
		while (iter_par.hasNext()) {
			
			byte[] length_byte; 
			length_int = iter_par.next().length;
			length_sum += length_int;
			b.putInt(length_int);
			length_byte = b.array();
			length_list.add(length_byte);
			b.clear();
			length_int = 0;
			
		} //calculates the length for each field in order and store in a byte array
		
		b.putInt(opcode);
		byte[] opcode_byte = b.array();
		
		//construct a byte buffer, with length field_count*4 + length_sum + 4 (opcode) + len(uuid)
		int buf_len = field_count * 4 + length_sum + len_opcode + len_uuid; 
		
		ByteBuffer buf = ByteBuffer.allocate(buf_len);
		buf.put(opcode_byte);
		//insert uuid
		iter_par = parameter_list.iterator();
		Iterator<byte[]> iter_len = length_list.iterator();
		while (iter_par.hasNext() && iter_len.hasNext()) {
			buf.put(iter_len.next());
			buf.put(iter_par.next());
		}
		
		byte[] final_message = buf.array();
		buf.clear();
		return final_message;
		
		
		
		
		
				
	}

}
