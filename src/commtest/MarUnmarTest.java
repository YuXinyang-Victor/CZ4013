package commtest;

import distributedBank.ClientMarshal;
import distributedBank.CurrencyType;
import distributedBank.ServerUnmarshal;

public class MarUnmarTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String name = "john-smith";
		String password_hash = "79400f341e44cd9be5fc91c000186786f2ac2e2a";
		CurrencyType currency = CurrencyType.SGD;
		double amount = 1145.14;
		
		ClientMarshal tobytes = new ClientMarshal();
		byte[] byte_msg = tobytes.marshal(name, password_hash, currency, amount);
		ServerUnmarshal decoder = new ServerUnmarshal();
		int opcode = decoder.peekOpcode(byte_msg);
		System.out.println(opcode);
		
	}

}
