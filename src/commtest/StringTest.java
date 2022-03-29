package commtest;
import java.io.IOException;

import distributedBank.CurrencyType;
import distributedBank.ServerAccMgr;

public class StringTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerAccMgr mgr = ServerAccMgr.getManager();
		String name = "john";
		String password = "114514";
		CurrencyType currency_in = CurrencyType.SGD;
		double deposit_amount = 114.5;
		mgr.createAccount(name, password, currency_in, deposit_amount);
		
		mgr.closeAccount(0, name, password);
	}

}
