import java.util.ArrayList;

public class BuildSimGraph {

	
	public  BuildSimGraph(ArrayList<String> GraphWords,W2VUtil util) {
	
		
		WordGraph wg = new WordGraph();

		//adding nodes 
		
		for (int n = 0; n < GraphWords.size(); n++){
			wg.addVertex(GraphWords.get(n));
			System.out.println("tedad loghate document 4: "+GraphWords.get(n));
		}

		for (int n = 0; n < GraphWords.size()-1; n++) {
			for (int m = n+1; m < GraphWords.size(); m++) {
			
					String w1 = GraphWords.get(n);
					String w2 = GraphWords.get(m);
					double dist = util.distance(w1, w2);
					wg.addWeightedEdge(w1, w2, dist);
			}
		}
				
		wg.getMST();
		wg.centralityAnalysis();
		
	}
}
