package apps;

import structures.*;
import structures.Vertex.Neighbor;

import java.util.ArrayList;

public class MST {
	
	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		/* COMPLETE THIS METHOD */
		Vertex[] vertices = graph.vertices;
		PartialTreeList L = new PartialTreeList();
		//ArrayList<MinHeap> heaps = new ArrayList<MinHeap>();
		for (int i = 0; i<vertices.length;i++){
			PartialTree obj = new PartialTree(vertices[i]);
			obj.getRoot().parent=obj.getRoot();
			MinHeap<PartialTree.Arc> queue = new MinHeap<PartialTree.Arc>();
			queue=obj.getArcs();
			Neighbor ptr = vertices[i].neighbors;
			while(ptr!=null){
				PartialTree.Arc Edge = new PartialTree.Arc(vertices[i],ptr.vertex, ptr.weight);
				queue.insert(Edge);
				//heaps.add(queue);
				ptr=ptr.next;
			}	
			L.append(obj);
		}	
		return L;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {	
		/* COMPLETE THIS METHOD */
		ArrayList<PartialTree.Arc> returnList = new ArrayList<PartialTree.Arc>();
		while(ptlist.size()>1){
			PartialTree PTX = ptlist.remove();
			MinHeap<PartialTree.Arc> PQX = PTX.getArcs();
			if (PQX.isEmpty()){
				
			}else{
				PartialTree.Arc alpha = PQX.deleteMin();
				while(alpha.v2.getRoot().equals(PTX.getRoot())){
					alpha = PQX.deleteMin();
				}
				returnList.add(alpha);
				PartialTree PTY = ptlist.removeTreeContaining(alpha.v2);
				MinHeap<PartialTree.Arc> PQY = new MinHeap<PartialTree.Arc>();
				if (PTY!=null){
					PQY = PTY.getArcs();
					PTX.merge(PTY);
					Vertex parent = PTY.getRoot().parent;
					parent=PTX.getRoot();
					PQX.merge(PQY);
					ptlist.append(PTX);
				}
			}
		}
		return returnList;
	}
}
