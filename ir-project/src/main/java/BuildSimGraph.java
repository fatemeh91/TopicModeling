import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BuildSimGraph {

	
	public  BuildSimGraph(ArrayList<String> GraphWords,String reviwe_path,int filenumber,int docno,W2VUtil util) throws FileNotFoundException, IOException {
		
		WordGraph wg = new WordGraph();
		//adding nodes 
		for (int n = 0; n < GraphWords.size(); n++){
			wg.addVertex(GraphWords.get(n));
		}
		for (int n = 0; n < GraphWords.size()-1; n++) {
			for (int m = n+1; m < GraphWords.size(); m++) {
			
					String w1 = GraphWords.get(n);
					String w2 = GraphWords.get(m);
					double dist = util.distance(w1, w2);
					wg.addWeightedEdge(w1, w2, dist);
			}
		}
				
		//wg.getMST();
		String filename=reviwe_path+"/file"+filenumber+"review"+docno+".txt";
		FileWriter fw=new FileWriter(filename,true);
		wg.getMST();
    	wg.centralityAnalysis(wg.getWordGraph(),"exhaustive");
    	wg.filterTopics(-1.0);    	
		//fw.write(wg.topicToString());		
		fw.flush();	
		fw.write("\n"+wg.topicToString());
		fw.close();
		//wg.centralityAnalysis();
		
	}
}
