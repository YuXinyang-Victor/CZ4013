package commtest;

import java.util.ArrayList;
import java.util.List;

public class ListTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Integer test = 0;
		int i = 10;
		List<Integer> test_list = new ArrayList<Integer>();
		
		while (i > 0) {
			test = i + 1; 
			test_list.add(test);
			i -= 1;
		} 
		
		System.out.println(test_list);
		
		
	}

}
