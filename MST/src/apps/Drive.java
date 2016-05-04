package apps;

import java.io.IOException;
import java.util.ArrayList;

import structures.Graph;

public class Drive {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MST test = new MST();
		try {
			Graph test2 = new Graph("graph9.txt");
			PartialTreeList test3 = new PartialTreeList();
			test3 = MST.initialize(test2);
			ArrayList<PartialTree.Arc> test4 = new ArrayList<PartialTree.Arc>();
			test4 = MST.execute(test3);
			for (int i = 0;i<=test4.size()-1;i++){
				System.out.println(test4.get(i));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
