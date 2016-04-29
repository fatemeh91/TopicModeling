import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BuildSimGraph {

	
	public  BuildSimGraph(ArrayList<String> GraphWords,String output_path,int filenumber,int docno,W2VUtil util) throws FileNotFoundException, IOException {
		
		WordGraph wg = new WordGraph();
		//adding nodes 
		for (int n = 0; n < GraphWords.size(); n++){
			if (util.vec.hasWord(GraphWords.get(n))) wg.addVertex(GraphWords.get(n));
		}
		for (int n = 0; n < GraphWords.size()-1; n++) {
			for (int m = n+1; m < GraphWords.size(); m++) {
			
					String w1 = GraphWords.get(n);
					String w2 = GraphWords.get(m);
					if(wg.getWordGraph().containsVertex(w1) && wg.getWordGraph().containsVertex(w2))
					{
						double dist = util.distance(w1, w2);
						wg.addWeightedEdge(w1, w2, dist);
					}
			}
		}
				
		
		String filename=output_path+"/file"+filenumber+"review"+docno+".txt";
		FileWriter fw=new FileWriter(filename,true);
		wg.getMST();
    	
		wg.centralityAnalysis(wg.getMST(),"weighted");
    	wg.filterTopics(-1.0);    			
		fw.flush();	
		fw.write("MST weighted ");
		fw.write("\n"+wg.topicToString()+"\n");
		
		wg.centralityAnalysis(wg.getMST(),"closeness");
		wg.filterTopics(-1.0);    			
		fw.flush();	
		fw.write("MST closeness");
		fw.write("\n"+wg.topicToString()+"\n");
		
		wg.centralityAnalysis(wg.getMST(),"exhaustive");
		wg.filterTopics(-1.0);    			
		fw.flush();	
		fw.write("MST exhaustive");
		fw.write("\n"+wg.topicToString()+"\n");
		
		wg.centralityAnalysis(wg.getMST(),"betweenness");
		wg.filterTopics(-1.0);    			
		fw.flush();	
		fw.write("MST betwennness");
		fw.write("\n"+wg.topicToString()+"\n");
		
		
		wg.centralityAnalysis(wg.getWordGraph(),"weighted");
    	wg.filterTopics(-1.0);    			
		fw.flush();	
		fw.write(" weighted ");
		fw.write("\n"+wg.topicToString()+"\n");
		
		wg.centralityAnalysis(wg.getWordGraph(),"closeness");
		wg.filterTopics(-1.0);    			
		fw.flush();	
		fw.write(" closeness");
		fw.write("\n"+wg.topicToString()+"\n");
				
		fw.close();
		//wg.centralityAnalysis();
		
	}
}
