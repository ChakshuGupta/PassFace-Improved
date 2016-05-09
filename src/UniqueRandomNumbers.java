import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class UniqueRandomNumbers {
	
	public int[] Unique_Values(ArrayList<Integer> exclude, ArrayList<Integer> remaining ,int x)
	 {
		int i;
		Random random = new Random();
		int[] rand = new int[9];
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(i=0; i<20; i++)// Adding values 1-20 in the ArrayList
		{
			list.add(new Integer(i+1));
		}
		
		list.removeAll(exclude);//Removing the values of the exclude array from the list
		Collections.shuffle(list);// shuffling the contents of list
		int val = random.nextInt(9);// Choosing a random value from 0-8
		list.add(val, new Integer(remaining.get(x)));// and including one of the elements from exclude in the list
		
		for(i=0; i<9 ; i++)//taking out first 9 elements from the list and storing in the array.
		{
			rand[i] = list.get(i);
		}

		return rand; 	
	 }

}
