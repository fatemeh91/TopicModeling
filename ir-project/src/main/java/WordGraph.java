import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
public class WordGraph {
	private WeightedGraph<String, DefaultWeightedEdge> wordGraph;
	private WeightedGraph<String, DefaultWeightedEdge> wordMST;
	public Vector<String> topicWords;

	List<Map.Entry<String, Double>> centralityList ;
	private HashMap<String, Double> bCentralities;
	private HashMap<String, Double> dCentralities;
	private HashMap<String, Double> cCentralities;
	private HashMap<String, Double> neighborCentralities;
	private HashMap<String, Double> centralities;

	public WordGraph(){
		wordGraph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		topicWords = new Vector<String>();
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
    public WeightedGraph<String, DefaultWeightedEdge> getWordGraph()
    {
    	return this.wordGraph;
    }
    public WeightedGraph<String, DefaultWeightedEdge> getMST()
    {
    	if (wordMST != null)
    		return wordMST;
    	
    	wordMST = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
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
    	return wordMST;
    }
    public void centralityAnalysis(WeightedGraph<String, DefaultWeightedEdge> graph, String mode)
    {
    	this.cCentralities =  new HashMap<String, Double>();
    	this.bCentralities =  new HashMap<String, Double>();
    	this.dCentralities =  new HashMap<String, Double>();
    	this.centralities =  new HashMap<String, Double>();
    	double bc, dc, cc;
    	
    	CentralityComputer<String, DefaultWeightedEdge> cental = new CentralityComputer<String, DefaultWeightedEdge>(graph);
    	
    	for(String w : graph.vertexSet())
    	{
    		cc = cental.findClosenessOf(w);
    		dc = cental.findDegreeOf(w);
    		bc = cental.findBetweennessOf(w);
    		this.cCentralities.put(w, cc);
    		this.dCentralities.put(w, dc);
    		this.bCentralities.put(w, bc);
    		if (mode.equals("exhaustive"))
    			this.centralities.put(w, cc + dc + bc);
    		else if (mode.equals("betweenness"))
    			this.centralities.put(w, bc);
    		else this.centralities.put(w, cc);
    	}
    	System.out.println(this.centralities);

    	this.centralities = sortByValues(this.centralities);
    }
    private double getThreshold(String string) {
    	double sum = 0;
    	for(String k : this.centralities.keySet())
    	{
    		sum += this.centralities.get(k);
    	}		
    	System.out.println("Threshold = " + sum/this.centralities.size());
    	return sum/this.centralities.size();
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
    public HashMap<String, Double> filterTopics(double threshold) 
    {
    	if (threshold == -1)
    		threshold = this.getThreshold("mean");
    		
        for (Map.Entry<String, Double> entry : this.centralityList)
        {
        	if (entry.getValue() < threshold)
        	{
        		centralities.remove(entry.getKey());
        	}
        }
    	return centralities;
    }
    public String topicToString()
    {
    	if (topicWords.isEmpty())
    		this.setTopicWords();
    	String topicsStr = "";
    	for(String k : topicWords)
    	{
    		topicsStr += (k + ", ");
    	}
    	return topicsStr;
    }
    private void setTopicWords()
    {
    	if (!topicWords.isEmpty())
    		topicWords.clear();
    	for(String k : this.centralities.keySet())
    	{
    		topicWords.add(k);
    	}   
    }
}
    
