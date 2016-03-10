
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ayine
 */
public class Main {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {

		// for every two words w1 and w2

//		W2VUtil util = new W2VUtil(null);
	//	double dist = util.distance(word1, word2);

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
		System.out.println("it's over!");

	}

}
