/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ayine
 */
public class SafooraDemo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	
    	W2VUtil.DEBUG_MODE = true;
        W2VUtil util = new W2VUtil("GoogleNews-vectors-negative300.bin", W2VUtil.ANGULAR_DIST);
        WordGraph wg = new WordGraph();
    	String w1 = "beauty";
    	String w2 = "finance";
    	String w3 = "skincare";
    	String w4 = "makeup";
    	
    	
    	wg.addVertex(w1);
    	wg.addVertex(w2);
    	wg.addVertex(w3);
    	wg.addVertex(w4);
    	
    	wg.addWeightedEdge(w1, w2, .9);
    	wg.addWeightedEdge(w2, w3, .9);
    	wg.addWeightedEdge(w3, w4, .5);
    	wg.addWeightedEdge(w4, w1, .5);
    	wg.addWeightedEdge(w1, w3, .5);
    	wg.addWeightedEdge(w2, w4, .9);
    	
    	wg.getMST();
    	wg.centralityAnalysis();
    	System.out.println("it's over!");
    	
    }
    
}
