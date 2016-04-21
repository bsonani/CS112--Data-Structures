package search;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Driver {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LittleSearchEngine test = new LittleSearchEngine();	
		try {
			test.makeIndex("docs2.txt", "noisewords.txt");
			String example = test.getKeyWord(null);
			
			ArrayList<String> print = new ArrayList<String>();
			print = test.top5search("fortinbras","lands");
			for (int i = 0;i<=print.size()-1;i++){
				System.out.println(print.get(i));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
