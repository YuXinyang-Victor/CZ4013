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
        System.out.println(i);
        byte[] copy = new byte[i + 1];
        int j = 0; 
        while (j <= i) {
        	System.out.println(j);
        	copy[j] = bytes[j];
        	j+=1; 
        }
        System.out.println(copy);
        return copy;
	}
}
