package commtest;

import java.io.IOException;
import java.util.Scanner;

import distributedBank.ClientComm;

public class ClientDriver {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			System.out.println("client Running");
		
			String dummy = scanner.nextLine();
			if (dummy == "stop") break;
		
			
			String input = "A test string 114514";
			byte[] msg = input.getBytes("UTF-8");
		
			ClientComm client_comm_module = new ClientComm();
			// We first test the part without clientlisten
		
			System.out.println(msg);
		
		
			client_comm_module.clientSend(msg);
		
		}
		
		System.out.println("client ended");
	}
}
