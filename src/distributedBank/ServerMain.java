package distributedBank;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import java.util.UUID;

import distributedBank.ServerUnmarshal;


public class ServerMain {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//initialize server communication module
		ServerComm server_comm = ServerComm.getServerComm();
		server_comm.serverListen();
		
	}
	
	public static void mainHandler(byte[] message_in) throws IOException {
		int opcode = ServerUnmarshal.peekOpcode(message_in);
		UUID uuid = ServerUnmarshal.getUUID(message_in);
		
		//check uuid and handle with appropriate invocation semantics
		ServerComm server_comm = ServerComm.getServerComm();
		boolean should_proceed = server_comm.invoSemProcess(message_in);
		if(!should_proceed) return;
		
		
		//handle according to opcode
		String message = new String();
		message = "error in opcode.";
		switch (opcode) {
			case 0: 
				System.out.println("mainhandler called in case 0");
				message = registerHandler(); break;
			case 1: 
				System.out.println("mainhandler called in case 1");
				message = typeOneHandler(message_in); break;
			case 2: 
				System.out.println("mainhandler called in case 2");
				message = typeTwoHandler(message_in); break;
			case 3: 
				System.out.println("mainhandler called in case 3");
				message = typeThreeHandler(message_in); break;
			case 5: 
				System.out.println("mainhandler called in case 5");
				message = typeFiveHandler(message_in); break;
			case 6: 
				System.out.println("mainhandler called in case 6");
				message = typeSixHandler(message_in); break;
			default: break;
		}
		
		
		byte[] msg_byte = message.getBytes();
		byte[] uuid_byte = uuid.toString().getBytes(); //consider UTF-8? Unicode? on different machines?
		byte[] reply = ServerComm.serverAddUUID(msg_byte, uuid_byte);
		server_comm.serverSend(reply, true);
		
		server_comm.addMsgToCache(reply, uuid);
		
		
	}
	
	//register callback
	public static String registerHandler() throws SocketException {
		ServerComm server_comm = ServerComm.getServerComm();
		InetAddress client_address = server_comm.client_address;
		int client_port = server_comm.client_port;
		
		CallbackMgr cb_mgr = CallbackMgr.getCallbackMgr();
		
		cb_mgr.registerClient(client_address, client_port);
		
		String message = "register successful";
		return message;
		
	}
	
	//account creation
	public static String typeOneHandler(byte[] message_in) {
		//we do not separate this to another class as we want servermain to ideally be the only one to call serveraccmgr
		
		//Because of predetermined protocol, server knows that for opcode 1, it is a request for account creation
		//The first field after uuid is going to be name, followed by password, currency, and amount
		
		System.out.println("type one handler called");
		int curr_pos = 40;
		
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
		String amount_str = ServerUnmarshal.getValue(curr_pos, amount_length, message_in);
		curr_pos += amount_length;
		
		//start converting strings to the correct var type
		CurrencyType currency_type = CurrencyType.valueOf(currency);
		
		Double init_amount = Double.valueOf(amount_str);
		double amount = init_amount.doubleValue();		
		ServerAccMgr account_manager = ServerAccMgr.getManager();
		
		int account_number = account_manager.createAccount(name, password, currency_type, amount);
		String message = "Your account has been created with account number " + Integer.toString(account_number);
		System.out.println(message);
		
		return message;
		//Maybe it is simpler to have length. 
	}
	
	public static String typeTwoHandler(byte[] message_in) throws IOException {	
		//Because of predetermined protocol, server knows that for opcode 2, it is a request for account close
		//The first field after uuid is going to be name, followed by account_number and password
		
		System.out.println("type two handler called");
		int curr_pos = 40;
		
		int name_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String name = ServerUnmarshal.getValue(curr_pos, name_length, message_in);
		curr_pos += name_length; 
		
		int acc_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String acc = ServerUnmarshal.getValue(curr_pos, acc_length, message_in); 
		curr_pos += acc_length;
		
		int password_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String password = ServerUnmarshal.getValue(curr_pos, password_length, message_in); 
		curr_pos += password_length;
		
		int account_number = Integer.valueOf(acc);
		
		ServerAccMgr account_manager = ServerAccMgr.getManager();
		
		boolean closed = false;
		try {
			closed = account_manager.closeAccount(account_number, name, password);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message;
		if (closed) {
			message = "Your account is successfully closed. ";
		}
		else {message = "Please check the details you have provided";}
		System.out.println(message);
		
		return message;
	}
	
	public static String typeThreeHandler(byte[] message_in) throws IOException {	
		//Because of predetermined protocol, server knows that for opcode 3, it is a request for deposit / withdrawal
		//The first field after uuid is going to be name, followed by account_number, password, and amount
		
		System.out.println("type three handler called");
		int curr_pos = 40;
		
		int name_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String name = ServerUnmarshal.getValue(curr_pos, name_length, message_in);
		curr_pos += name_length; 
		
		int acc_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String acc = ServerUnmarshal.getValue(curr_pos, acc_length, message_in); 
		curr_pos += acc_length;
		
		int password_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String password = ServerUnmarshal.getValue(curr_pos, password_length, message_in); 
		curr_pos += password_length;
		
		int amount_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String amount_str = ServerUnmarshal.getValue(curr_pos, amount_length, message_in);
		curr_pos += amount_length;
		
		int account_number = Integer.valueOf(acc);
		Double obj_amount = Double.valueOf(amount_str);
		double amount = obj_amount.doubleValue();	
		ServerAccMgr account_manager = ServerAccMgr.getManager();
		
		Double new_balance = null;
		try {
			new_balance = account_manager.updateAccBalance(account_number, name, password, amount);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message;
		if (new_balance != null) {
			message = "Your new balance is " + new_balance.toString();
		}
		else {message = "Please check the details you have provided";}
		System.out.println(message);
		
		return message;
	}
	
	public static String typeFiveHandler(byte[] message_in) throws IOException {	
		//Because of predetermined protocol, server knows that for opcode 5, it is a request for transfer
		//The first field after uuid is going to be name, followed by account_number, password, transfer amount, to name, and to account_number
		
		System.out.println("type five handler called");
		int curr_pos = 40;
		
		int from_name_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String from_name = ServerUnmarshal.getValue(curr_pos, from_name_length, message_in);
		curr_pos += from_name_length; 
		
		int from_acc_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String from_acc = ServerUnmarshal.getValue(curr_pos, from_acc_length, message_in); 
		curr_pos += from_acc_length;
		
		int password_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String password = ServerUnmarshal.getValue(curr_pos, password_length, message_in); 
		curr_pos += password_length;
		
		int amount_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String amount_str = ServerUnmarshal.getValue(curr_pos, amount_length, message_in);
		curr_pos += amount_length;
		
		int to_name_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String to_name = ServerUnmarshal.getValue(curr_pos, to_name_length, message_in);
		curr_pos += to_name_length; 
		
		int to_acc_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		curr_pos += 4;
		String to_acc = ServerUnmarshal.getValue(curr_pos, to_acc_length, message_in); 
		curr_pos += to_acc_length;
		
		int from_account_number = Integer.valueOf(from_acc);
		int to_account_number = Integer.valueOf(to_acc);
		Double obj_amount = Double.valueOf(amount_str);
		double amount = obj_amount.doubleValue();	
		ServerAccMgr account_manager = ServerAccMgr.getManager();
		
		Double new_balance = null;
		try {
			new_balance = account_manager.amountTransfer(from_account_number, from_name, password, amount, to_account_number, to_name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message;
		if (new_balance != null) {
			message = "Transfer success. Your new balance is " + new_balance.toString();
		}
		else {message = "Please check the details you have provided. Notice that you cannot transfer between account of different currency types.";}
		System.out.println(message);
		
		return message;
	}
	
	public static String typeSixHandler(byte[] message_in) throws IOException {
				//we do not separate this to another class as we want servermain to ideally be the only one to call serveraccmgr
		
				//Because of predetermined protocol, server knows that for opcode 6, it is a request for account currency type change
				//The first field after uuid is going to be name, followed by acc_number, password, and currency
				
				System.out.println("type six handler called");
				int curr_pos = 40;
				
				int name_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
				curr_pos += 4;
				String name = ServerUnmarshal.getValue(curr_pos, name_length, message_in);
				curr_pos += name_length; 
				
				int acc_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
				curr_pos += 4;
				String acc = ServerUnmarshal.getValue(curr_pos, acc_length, message_in); 
				curr_pos += acc_length;
				
				int password_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
				curr_pos += 4;
				String password = ServerUnmarshal.getValue(curr_pos, password_length, message_in); 
				curr_pos += password_length;
				
				int currency_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
				curr_pos += 4;
				String currency = ServerUnmarshal.getValue(curr_pos, currency_length, message_in);
				curr_pos += currency_length;
				
				//start converting strings to the correct var type
				CurrencyType currency_type = CurrencyType.valueOf(currency);
				int account_number = Integer.valueOf(acc);
						
				ServerAccMgr account_manager = ServerAccMgr.getManager();
				String message = new String(); 
				
				boolean status = false;
				try {
					status = account_manager.changeAccCurrency(account_number, name, password, currency_type);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (status) {
					message = "Your account currency type is successfully changed.";
				}
				else {
					message = "Please check the details you have provided.";
				}
				System.out.println(message);
				
				return message;
	}
	
	public static void sendError(String err_msg) throws IOException {
		ServerComm server_comm = ServerComm.getServerComm();
		byte[] msg_byte = err_msg.getBytes();
		server_comm.serverSend(msg_byte, false);
	}
	
	public static void updateHandler(Account acc) throws IOException {
		CallbackMgr cb_mgr = CallbackMgr.getCallbackMgr();
		String msg_base = "This account is updated: \n";
		String acc_id = "Account ID: " + String.valueOf(acc.accNumber) + "\n";
		String acc_name = "Account name: " + acc.getName() + "\n";
		String currency = "Currency type: " + acc.getCurrency().name() + "\n";
		String deposit = "Deposit: " + String.valueOf(acc.getAccBalance());
		String msg = msg_base + acc_id + acc_name + currency + deposit;
		byte[] msg_byte = msg.getBytes();
		try {
			cb_mgr.callClient(msg_byte);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sendUpdate(InetAddress ip, int port, byte[] msg) throws IOException {
		ServerComm server_comm = ServerComm.getServerComm();
		server_comm.serverSend(msg, ip, port);
	}
	
}
