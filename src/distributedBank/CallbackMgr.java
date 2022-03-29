package distributedBank;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CallbackMgr {
	private static CallbackMgr callback_mgr = null;
	HashMap<Integer, ClientInfo> call_list; 
	int call_count;
	long life = 6000000;
	
	public static CallbackMgr getCallbackMgr() {
		if (callback_mgr == null) {
			callback_mgr = new CallbackMgr();
		}
		return callback_mgr;
	}
	
	private CallbackMgr() {
		call_list = new HashMap<Integer, ClientInfo>();
		call_count = 0;
	}
	
	public void registerClient(InetAddress ip, int port) {
		long epoch = System.currentTimeMillis();
		ClientInfo client = new ClientInfo(ip, port, epoch);
		call_list.put(call_count, client);
	}
	
	public void callClient(byte[] msg) throws IOException {
		Iterator client_iter = call_list.entrySet().iterator();
		while(client_iter.hasNext()) {
			Map.Entry curr = (Entry) client_iter.next();
			Integer key = (Integer) curr.getKey();
			ClientInfo value = (ClientInfo) curr.getValue();
			long curr_epoch = System.currentTimeMillis();
			long init_epoch = value.created_time;
			
			if ((curr_epoch - init_epoch) <= life) {
				InetAddress ip = value.getIP();
				int port = value.getPort();
				try {
					ServerMain.sendUpdate(ip, port, msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				call_list.remove(key);
			}
		}
	}
}
