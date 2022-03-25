package distributedBank;

public class ServerMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//initialize server communication module
		
	}
	
	public static void mainHandler(byte[] message_in) {
		int opcode = ServerUnmarshal.peekOpcode(message_in);
		String uuid_str = ServerUnmarshal.getUUID(message_in);
		
		//check uuid and handle with appropriate invocation semantics
		
		//handle according to opcode
		switch (opcode) {
			case 1: typeOneHandler(message_in); break;
			
			default: break;
		}
	}
	
	//account creation
	public static void typeOneHandler(byte[] message_in) {
		//we do not separate this to another class as we want servermain to ideally be the only one to call serveraccmgr
		
		//Because of predetermined protocal, server knows that for opcode 1, it is a request for account creation
		//The first field after uuid is going to be name, followed by password, currency, and amount
		int curr_pos = 5;
		int name_length = ServerUnmarshal.getFieldLength(curr_pos, message_in);
		
	}
}
