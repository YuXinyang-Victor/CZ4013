package distributedBank;

import java.io.IOException;
import java.net.SocketException;

public class ServerMain {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//initialize server communication module
		ServerComm server_comm = ServerComm.getServerComm();
		server_comm.serverListen();
		//initialize serveraccountmanager
	}
	
	public static void mainHandler(byte[] message_in) throws IOException {
		int opcode = ServerUnmarshal.peekOpcode(message_in);
		String uuid_str = ServerUnmarshal.getUUID(message_in);
		
		//check uuid and handle with appropriate invocation semantics
		
		//handle according to opcode
		String message = new String();
		switch (opcode) {
			case 1: 
				System.out.println("mainhandler called in case 1");
				message = typeOneHandler(message_in); break;
			
			default: break;
		}
		
		ServerComm server_comm = ServerComm.getServerComm();
		byte[] msg_byte = message.getBytes();
		server_comm.serverSend(msg_byte);
		
		
	}
	
	//account creation
	public static String typeOneHandler(byte[] message_in) {
		//we do not separate this to another class as we want servermain to ideally be the only one to call serveraccmgr
		
		//Because of predetermined protocol, server knows that for opcode 1, it is a request for account creation
		//The first field after uuid is going to be name, followed by password, currency, and amount
		
		System.out.println("type one handler called");
		int curr_pos = 4;
		
		int name_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String name = ServerUnmarshal.getValue(curr_pos, name_length, message_in);
		curr_pos += name_length; 
		
		int password_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String password = ServerUnmarshal.getValue(curr_pos, password_length, message_in); 
		curr_pos += password_length;
		
		int currency_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String currency = ServerUnmarshal.getValue(curr_pos, currency_length, message_in);
		curr_pos += currency_length;
		
		int amount_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String amount_str = ServerUnmarshal.getValue(curr_pos, currency_length, message_in);
		curr_pos += amount_length;
		
		//start converting strings to the correct var type
		CurrencyType currency_type = CurrencyType.valueOf(currency);
		
		Double init_amount = Double.valueOf(amount_str);
		double amount = init_amount.doubleValue();		
		ServerAccMgr account_manager = ServerAccMgr.getManager();
		
		int account_number = account_manager.createAccount(name, password, currency_type, amount);
		String message = "Your account has been created with account number" + Integer.toString(account_number);
		System.out.println(message);
		
		return message;
		
		
		
		
		
		
		
		//Maybe it is simpler to have length. 
	}
}
