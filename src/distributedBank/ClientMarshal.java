package distributedBank;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;



public class ClientMarshal {
	
	public static byte[] marshalAccNumber(int account_number) {
		Integer account_number_object = Integer.valueOf(account_number);
		byte[] output = account_number_object.toString().getBytes();
		return output;
		
	}
	
	public static byte[] marshalName(String name) {
		byte[] output = name.getBytes();
		return output;
	}
	
	public static byte[] marshalPassword(String password_hash) {
		byte[] output = password_hash.getBytes();
		return output;
		
	}
	
	public static byte[] marshalAmount(double amount) {
		Double amount_object = Double.valueOf(amount);
		byte[] output = amount_object.toString().getBytes();
		return output;
		
	}
	
	public static byte[] marshalCurrencyType(String currency) {
		byte[] output = currency.getBytes();
		return output;
	}
	
	public static byte[] getUUID() {
		UUID uuid = UUID.randomUUID();
		String uuid_str = uuid.toString();
		byte[] uuid_byte = uuid_str.getBytes();
		return uuid_byte;
	}
	
	//We will be using polymorphism to deal with different marshaling requirements
	public static byte[] marshal(String name, String password, String currency, double amount) throws IOException {
		int field_count = 4;
		int opcode = 1;
		int len_opcode = 4;
		int len_uuid = 36;
		
		byte[] marshalled_name = marshalName(name);
		byte[] marshalled_password = marshalPassword(password);
		byte[] marshalled_currency = marshalCurrencyType(currency);
		byte[] marshalled_amount = marshalAmount(amount);
		
		List<byte[]> parameter_list = new ArrayList<byte[]>();
		List<byte[]> length_list = new ArrayList<byte[]>();
		
		parameter_list.add(marshalled_name);
		parameter_list.add(marshalled_password);
		parameter_list.add(marshalled_currency);
		parameter_list.add(marshalled_amount);
		
		Iterator<byte[]> iter_par = parameter_list.iterator();
		 //4 byte for all integers in this part
		
		int length_int = 0;
		int length_sum = 0;
		ByteBuffer[] b_arr = new ByteBuffer[field_count];
		
		int i = 0;
		
		
		while (iter_par.hasNext()) {
			b_arr[i] = ByteBuffer.allocate(4);
			byte[] length_byte; 
			length_int = iter_par.next().length;
			
			length_sum += length_int;
			b_arr[i].putInt(length_int);
			length_byte = b_arr[i].array();
			length_list.add(length_byte);
			//b.clear();
			length_int = 0;
			i++;
			
		} //calculates the length for each field in order and store in a byte array
		
		
		
		ByteBuffer c = ByteBuffer.allocate(4);
		c.putInt(opcode);
		byte[] opcode_byte = c.array();
		
		//construct a byte buffer, with length field_count*4 + length_sum + 4 (opcode) + len(uuid)
		int buf_len = field_count * 4 + length_sum + len_opcode + len_uuid; 
		
		ByteBuffer buf = ByteBuffer.allocate(buf_len);
		buf.put(opcode_byte);
		
		//insert uuid
		byte[] uuid = getUUID();
		buf.put(uuid);
		
		iter_par = parameter_list.iterator();
		Iterator<byte[]> iter_len = length_list.iterator();
		while (iter_par.hasNext() && iter_len.hasNext()) {
			buf.put(iter_len.next());
			buf.put(iter_par.next());
		}
		
		byte[] final_message = buf.array();
		buf.clear();
		
		
		//testing
		//byte[] buffer = final_message;
		//String ip_str = "127.0.0.1";
		//InetAddress ip = InetAddress.getByName(ip_str);
		//DatagramPacket dp_send = new DatagramPacket(buffer, buffer.length, ip, 2023);
		//ClientComm client_comm = ClientComm.getClientComm();
		//client_comm.sendMessage(dp_send, new String(uuid));
		return final_message;
		
		
		
		
		
				
	}
	
	public static byte[] marshal(String name, int account_number, String password) {
		int field_count = 3;
		int opcode = 2;
		int len_opcode = 4;
		int len_uuid = 36;
		
		byte[] marshalled_name = marshalName(name);
		byte[] marshalled_acc = marshalAccNumber(account_number);
		byte[] marshalled_password = marshalPassword(password);
		
		
		List<byte[]> parameter_list = new ArrayList<byte[]>();
		List<byte[]> length_list = new ArrayList<byte[]>();
		
		parameter_list.add(marshalled_name);
		parameter_list.add(marshalled_acc);
		parameter_list.add(marshalled_password);
		
		
		Iterator<byte[]> iter_par = parameter_list.iterator();
		 //4 byte for all integers in this part
		
		int length_int = 0;
		int length_sum = 0;
		ByteBuffer[] b_arr = new ByteBuffer[field_count];
		
		int i = 0;
		
		
		while (iter_par.hasNext()) {
			b_arr[i] = ByteBuffer.allocate(4);
			byte[] length_byte; 
			length_int = iter_par.next().length;
			
			length_sum += length_int;
			b_arr[i].putInt(length_int);
			length_byte = b_arr[i].array();
			length_list.add(length_byte);
			
			length_int = 0;
			i++;
			
		} //calculates the length for each field in order and store in a byte array
		
		
		
		ByteBuffer c = ByteBuffer.allocate(4);
		c.putInt(opcode);
		byte[] opcode_byte = c.array();
		
		//construct a byte buffer, with length field_count*4 + length_sum + 4 (opcode) + len(uuid)
		int buf_len = field_count * 4 + length_sum + len_opcode + len_uuid; 
		
		ByteBuffer buf = ByteBuffer.allocate(buf_len);
		buf.put(opcode_byte);
		
		//insert uuid
		byte[] uuid = getUUID();
		buf.put(uuid);
		
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
	
	public static byte[] marshal(String name, int account_number, String password, double deposit_amount) {
		int field_count = 4;
		int opcode = 3;
		int len_opcode = 4;
		int len_uuid = 36;
		
		byte[] marshalled_name = marshalName(name);
		byte[] marshalled_acc = marshalAccNumber(account_number);
		byte[] marshalled_password = marshalPassword(password);
		byte[] marshalled_amount = marshalAmount(deposit_amount);
		
		List<byte[]> parameter_list = new ArrayList<byte[]>();
		List<byte[]> length_list = new ArrayList<byte[]>();
		
		parameter_list.add(marshalled_name);
		parameter_list.add(marshalled_acc);
		parameter_list.add(marshalled_password);
		parameter_list.add(marshalled_amount);
		
		Iterator<byte[]> iter_par = parameter_list.iterator();
		 //4 byte for all integers in this part
		
		int length_int = 0;
		int length_sum = 0;
		ByteBuffer[] b_arr = new ByteBuffer[field_count];
		
		int i = 0;
		
		
		while (iter_par.hasNext()) {
			b_arr[i] = ByteBuffer.allocate(4);
			byte[] length_byte; 
			length_int = iter_par.next().length;
			
			length_sum += length_int;
			b_arr[i].putInt(length_int);
			length_byte = b_arr[i].array();
			length_list.add(length_byte);
			
			length_int = 0;
			i++;
			
		} //calculates the length for each field in order and store in a byte array
		
		
		
		ByteBuffer c = ByteBuffer.allocate(4);
		c.putInt(opcode);
		byte[] opcode_byte = c.array();
		
		//construct a byte buffer, with length field_count*4 + length_sum + 4 (opcode) + len(uuid)
		int buf_len = field_count * 4 + length_sum + len_opcode + len_uuid; 
		
		ByteBuffer buf = ByteBuffer.allocate(buf_len);
		buf.put(opcode_byte);
		
		//insert uuid
		byte[] uuid = getUUID();
		buf.put(uuid);
		
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
	
	public static byte[] marshal(String from_name, int from_account_number, String password, double transfer_amount, String to_name, int to_account_number) {
		int field_count = 6;
		int opcode = 5;
		int len_opcode = 4;
		int len_uuid = 36;
		
		byte[] marshalled_from_name = marshalName(from_name);
		byte[] marshalled_from_acc = marshalAccNumber(from_account_number);
		byte[] marshalled_password = marshalPassword(password);
		byte[] marshalled_amount = marshalAmount(transfer_amount);
		byte[] marshalled_to_name = marshalName(to_name);
		byte[] marshalled_to_acc = marshalAccNumber(to_account_number);
		
		List<byte[]> parameter_list = new ArrayList<byte[]>();
		List<byte[]> length_list = new ArrayList<byte[]>();
		
		parameter_list.add(marshalled_from_name);
		parameter_list.add(marshalled_from_acc);
		parameter_list.add(marshalled_password);
		parameter_list.add(marshalled_amount);
		parameter_list.add(marshalled_to_name);
		parameter_list.add(marshalled_to_acc);
		
		Iterator<byte[]> iter_par = parameter_list.iterator();
		 //4 byte for all integers in this part
		
		int length_int = 0;
		int length_sum = 0;
		ByteBuffer[] b_arr = new ByteBuffer[field_count];
		
		int i = 0;
		
		
		while (iter_par.hasNext()) {
			b_arr[i] = ByteBuffer.allocate(4);
			byte[] length_byte; 
			length_int = iter_par.next().length;
			
			length_sum += length_int;
			b_arr[i].putInt(length_int);
			length_byte = b_arr[i].array();
			length_list.add(length_byte);
			//b.clear();
			length_int = 0;
			i++;
			
		} //calculates the length for each field in order and store in a byte array
		
		
		
		ByteBuffer c = ByteBuffer.allocate(4);
		c.putInt(opcode);
		byte[] opcode_byte = c.array();
		
		//construct a byte buffer, with length field_count*4 + length_sum + 4 (opcode) + len(uuid)
		int buf_len = field_count * 4 + length_sum + len_opcode + len_uuid; 
		
		ByteBuffer buf = ByteBuffer.allocate(buf_len);
		buf.put(opcode_byte);
		
		//insert uuid
		byte[] uuid = getUUID();
		buf.put(uuid);
		
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
	
	public static byte[] marshal(String name, int account_number, String password, String currency) {
		int field_count = 4;
		int opcode = 6;
		int len_opcode = 4;
		int len_uuid = 36;
		
		byte[] marshalled_name = marshalName(name);
		byte[] marshalled_acc = marshalAccNumber(account_number);
		byte[] marshalled_password = marshalPassword(password);
		byte[] marshalled_currency = marshalCurrencyType(currency);
		
		List<byte[]> parameter_list = new ArrayList<byte[]>();
		List<byte[]> length_list = new ArrayList<byte[]>();
		
		parameter_list.add(marshalled_name);
		parameter_list.add(marshalled_acc);
		parameter_list.add(marshalled_password);
		parameter_list.add(marshalled_currency);
		
		Iterator<byte[]> iter_par = parameter_list.iterator();
		 //4 byte for all integers in this part
		
		int length_int = 0;
		int length_sum = 0;
		ByteBuffer[] b_arr = new ByteBuffer[field_count];
		
		int i = 0;
		
		
		while (iter_par.hasNext()) {
			b_arr[i] = ByteBuffer.allocate(4);
			byte[] length_byte; 
			length_int = iter_par.next().length;
			
			length_sum += length_int;
			b_arr[i].putInt(length_int);
			length_byte = b_arr[i].array();
			length_list.add(length_byte);
			//b.clear();
			length_int = 0;
			i++;
			
		} //calculates the length for each field in order and store in a byte array
		
		
		
		ByteBuffer c = ByteBuffer.allocate(4);
		c.putInt(opcode);
		byte[] opcode_byte = c.array();
		
		//construct a byte buffer, with length field_count*4 + length_sum + 4 (opcode) + len(uuid)
		int buf_len = field_count * 4 + length_sum + len_opcode + len_uuid; 
		
		ByteBuffer buf = ByteBuffer.allocate(buf_len);
		buf.put(opcode_byte);
		
		//insert uuid
		byte[] uuid = getUUID();
		buf.put(uuid);
		
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
