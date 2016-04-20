import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BuildSimGraph {

	
	public  BuildSimGraph(ArrayList<String> GraphWords,String output_path,int filenumber,int docno,W2VUtil util) throws FileNotFoundException, IOException {
		
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
				
		
		String filename=output_path+"/file"+filenumber+"review"+docno+".txt";
		FileWriter fw=new FileWriter(filename,true);
		wg.getMST();
    	
		wg.centralityAnalysis(wg.getWordGraph(),"weighted");
    	wg.filterTopics(-1.0);    			
		fw.flush();	
		fw.write("MST weighted ");
		fw.write("\n"+wg.topicToString()+"\n");
		
		wg.centralityAnalysis(wg.getWordGraph(),"closeness");
		wg.filterTopics(-1.0);    			
		fw.flush();	
		fw.write("MST closeness");
		fw.write("\n"+wg.topicToString()+"\n");
		
		wg.centralityAnalysis(wg.getWordGraph(),"exhaustive");
		wg.filterTopics(-1.0);    			
		fw.flush();	
		fw.write("MST exhaustive");
		fw.write("\n"+wg.topicToString()+"\n");
		
		wg.centralityAnalysis(wg.getWordGraph(),"betweenness");
		wg.filterTopics(-1.0);    			
		fw.flush();	
		fw.write("MST betwennness");
		fw.write("\n"+wg.topicToString()+"\n");
		
		WordGraph secwg = new WordGraph();
		//adding nodes 
		for (int n = 0; n < GraphWords.size(); n++){
			secwg.addVertex(GraphWords.get(n));
		}
		for (int n = 0; n < GraphWords.size()-1; n++) {
			for (int m = n+1; m < GraphWords.size(); m++) {
			
					String w1 = GraphWords.get(n);
					String w2 = GraphWords.get(m);
					double dist = util.distance(w1, w2);
					secwg.addWeightedEdge(w1, w2, dist);
			}
		}
		
		
		secwg.centralityAnalysis(secwg.getWordGraph(),"weighted");
    	secwg.filterTopics(-1.0);    			
		fw.flush();	
		fw.write(" weighted ");
		fw.write("\n"+secwg.topicToString()+"\n");
		
		secwg.centralityAnalysis(secwg.getWordGraph(),"closeness");
		secwg.filterTopics(-1.0);    			
		fw.flush();	
		fw.write(" closeness");
		fw.write("\n"+secwg.topicToString()+"\n");
				
		fw.close();
		//wg.centralityAnalysis();
		
	}
}
