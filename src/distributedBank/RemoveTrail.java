package distributedBank;

/**
 *
 */
public class RemoveTrail {
	public static byte[] removeTrail(byte[] bytes) {
        if (bytes.length == 0) return bytes;
        int i = bytes.length - 1;
        while (bytes[i] == 0) {
            i--;
        }
        byte[] copy = new byte[i + 1];
        int j = 0; 
        while (j <= i) {
        	copy[j] = bytes[j];
        	j+=1; 
        }
        return copy;
	}
}
