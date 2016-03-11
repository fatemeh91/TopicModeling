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
	HashMap<String, Double> cc;
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
    			System.out.println(wordGraph.getEdgeSource(e) + " " +wordGraph.getEdgeTarget(e) + " **edge add fail**");
    		}
    		else System.out.println(wordGraph.getEdgeSource(e) + " " +wordGraph.getEdgeTarget(e) + " **success!**");
    		//wordMST.setEdgeWeight(e, wordGraph.getEdgeWeight(e));
    		//System.out.println("mst making: " + wordMST.getEdgeWeight(e));
    		
    	}
    	
    	System.out.println(wordMST.toString());
    	return wordMST;
    }
    public void centralityAnalysis()
    {
    	System.out.println("here1");
    	this.cc =  new HashMap<String, Double>();
    	CentralityComputer<String, DefaultWeightedEdge> cental = new CentralityComputer<String, DefaultWeightedEdge>(this.wordMST);
    	System.out.println("here2");
    	for(String w : this.wordMST.vertexSet())
    	{
        	System.out.println("getting cc: " + w);
    		cc.put(w, cental.findClosenessOf(w)); 
    	}
    	System.out.println("here3");

    	cc = sortByValues(cc);
    	System.out.println("here4");

    	for(String k : cc.keySet())
    	{
    		System.out.println(k + " = " + cc.get(k));
    	}
    	
    }
    private static HashMap<String, Double> sortByValues(HashMap<String, Double> map) {
    	List<Map.Entry<String, Double>> list =
                new LinkedList<Map.Entry<String, Double>>( map.entrySet() );
    	Collections.sort( list, new Comparator<Map.Entry<String, Double>>()
        {
            public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );
        map = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : list)
        {
            map.put( entry.getKey(), entry.getValue() );
        }
    	return map;
    }
}