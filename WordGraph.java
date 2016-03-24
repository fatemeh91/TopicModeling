
import java.net.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
public class WordGraph {
	private WeightedGraph<String, DefaultWeightedEdge> wordGraph;
	private WeightedGraph<String, DefaultWeightedEdge> wordMST;
	HashMap<String, Double> centralities;
	List<Map.Entry<String, Double>> centralityList ;
	public WordGraph() {
		wordGraph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		wordMST = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	}
    public void addWeightedEdge(String w1, String w2, double distance)
    {

        // add the vertices
    	wordGraph.addVertex(w1);
    	wordGraph.addVertex(w2); 
        // add edges to create a circuit
    	DefaultWeightedEdge ed = wordGraph.addEdge(w1, w2);
    	if (ed != null)
    		wordGraph.setEdgeWeight(ed, distance);
    }
    public boolean addVertex(String w)
    {
    	return wordGraph.addVertex(w);
    }
    public WeightedGraph<String, DefaultWeightedEdge> getMST()
    {
    	KruskalMinimumSpanningTree<String, DefaultWeightedEdge> kmst;
    	kmst =  new KruskalMinimumSpanningTree<String, DefaultWeightedEdge>(wordGraph);
    	Set<DefaultWeightedEdge> edgeSet = kmst.getMinimumSpanningTreeEdgeSet();
    	
    	
    	for(String w : wordGraph.vertexSet())
    	{
    		wordMST.addVertex(w);
    	}
    	for (DefaultWeightedEdge e: edgeSet)
    	{
    		if(!wordMST.addEdge(wordGraph.getEdgeSource(e), wordGraph.getEdgeTarget(e), e))
    		{
    			System.out.println("edge add fail");
    		}
    		//wordMST.setEdgeWeight(e, wordGraph.getEdgeWeight(e));
    		//System.out.println("mst making: " + wordMST.getEdgeWeight(e));
    		
    	}
    	
    	System.out.println(wordMST.toString());
    	return wordMST;
    }
    public void centralityAnalysis()
    {
    	this.centralities =  new HashMap<String, Double>();
    	CentralityComputer<String, DefaultWeightedEdge> cental = new CentralityComputer<String, DefaultWeightedEdge>(this.wordMST);
    	for(String w : this.wordMST.vertexSet())
    		centralities.put(w, cental.findClosenessOf(w)); 
    	centralities = sortByValues(centralities);
    	centralities = filterTopics(centralities, getThreshold(centralities, "mean"));
    	for(String k : centralities.keySet())
    	{
    		System.out.println(k + " = " + centralities.get(k));
    	}
    	
    }
    private double getThreshold(HashMap<String, Double> centralities, String string) {
    	double sum = 0;
    	for(String k : centralities.keySet())
    	{
    		sum += centralities.get(k);
    	}		
    	System.out.println(sum/centralities.size());
    	return sum/centralities.size();
	}
	private HashMap<String, Double> sortByValues(HashMap<String, Double> centralities) {
    	
                this.centralityList = new LinkedList<Map.Entry<String, Double>>( centralities.entrySet() );
    	Collections.sort( centralityList, new Comparator<Map.Entry<String, Double>>()
        {
            public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        centralities = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : centralityList)
        {
            centralities.put( entry.getKey(), entry.getValue() );
        }
    	return centralities;
    }
    public HashMap<String, Double> filterTopics(HashMap<String, Double> centralities, double threshold) 
    {
        for (Map.Entry<String, Double> entry : this.centralityList)
        {
        	if (entry.getValue() < threshold)
        	{
        		centralities.remove(entry.getKey());
        	}
        }
    	return centralities;
    }
}