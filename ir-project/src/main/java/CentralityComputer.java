
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class CentralityComputer<V, E> {

  private Graph<V, E> G;
  private HashMap<V, Integer> IndexMap;
  private Double[][] DistanceMatrix;
  private Double[][] AdjacencyMatrix;
  private Vector<Vector<Integer>> AdjacencyList;
  private int n;

  private Double[] Cb;

  // ~ Methods ----------------------------------------------------------------

  /** 
  * Class constructor. Computes auxiliary data necessary to compute centrality characteristics.
  */
  public CentralityComputer(Graph<V, E> myGraph) {
    G = myGraph;
    n = G.vertexSet().size();
    System.out.println("n = " + n);
    IndexMap = new HashMap<V, Integer>();
    // fill the index map
    int i = 0;
    int j = 0;
    for (V vertex : G.vertexSet()) {
      IndexMap.put(vertex, i);
      i++;
    }

    DistanceMatrix = new Double[n][n];
    AdjacencyMatrix = new Double[n][n];
    AdjacencyList = new Vector<Vector<Integer>>();
    for (i = 0; i < n; i++) {
      for (j = 0; j < n; j++) {
        DistanceMatrix[i][j] = 0.0;
        AdjacencyMatrix[i][j] = 0.0;
      }
      AdjacencyList.add(new Vector<Integer>());
    }

    // calculate Adjacency matrix
    for (E edge : G.edgeSet()) {
      i = IndexMap.get(G.getEdgeSource(edge));
      j = IndexMap.get(G.getEdgeTarget(edge));
      //System.out.println("in cental = " + G.getEdgeWeight(edge));
      AdjacencyMatrix[i][j] = G.getEdgeWeight(edge);
      AdjacencyMatrix[j][i] = G.getEdgeWeight(edge);

      AdjacencyList.elementAt(i).add(j);
      AdjacencyList.elementAt(j).add(i);
    }
	  

    Cb = new Double[n];
    Double[] sigma = new Double[n];
    Double[] d = new Double[n];
    Double[] delta = new Double[n];
	  

    for (i = 0; i < n; i++) {
      Cb[i] = 0.0;
      sigma[i] = 0.0;
      d[i] = 0.0;
    }
	  

    Vector<Integer> S = new Vector<Integer>();

    LinkedList<Integer> Q = new LinkedList<Integer>();

    Vector<Vector<Integer>> P = new Vector<Vector<Integer>>();
    Vector<Integer> tempVector = new Vector<Integer>();
    Iterator<Integer> It;
	  

    Integer v;
    Integer w;

    Vector<Integer> AdjVec = new Vector<Integer>();
    Iterator<Integer> At;

    for (Integer s = 0; s < n; s++) {
  	  
	
      // initialization
      S.clear();
      P.clear();
      for (i = 0; i < n; i++) {
        sigma[i] = 0.0;
        d[i] = -1.0;
        delta[i] = 0.0;
        P.add(new Vector<Integer>());
      }
      sigma[s] = 1.0;
      d[s] = 0.0;
      Q.clear();
      Q.add(s);

      // perform BFS
      while (!Q.isEmpty()) {
    	  
    	 
        v = Q.remove();
        S.add(v);
        // for all neighbors of v
        AdjVec = AdjacencyList.elementAt(v);
        At = AdjVec.iterator();
        while (At.hasNext()) {
          w = At.next();
          // w found for the first time
          if (d[w] < 0) {
        	d[w] = d[v] + AdjacencyMatrix[w][v];
            Q.add(w);
          }

          // shortest path to w via v?
          if (d[w] == d[v] + AdjacencyMatrix[w][v]) {
            sigma[w] = sigma[w] + sigma[v];
            P.elementAt(w).add(v);
          }
        }
      }
	  

      for (i = 0; i < n; i++) {
    	  //System.out.println(i + " " + s + " " + d[i]);
        DistanceMatrix[s][i] = d[i];
        DistanceMatrix[i][s] = d[i];
      }

      while (!S.isEmpty()) {
        w = S.remove(S.size() - 1);
        tempVector = P.elementAt(w);
        It = tempVector.iterator();

        while (It.hasNext()) {
          v = It.next();
          delta[v] = delta[v] + (sigma[v] / sigma[w]) * (1 + delta[w]);
        }
        if (w != s) {
          Cb[w] = Cb[w] + delta[w];
        }
      }

    }
	  

    // normalize the value of betweenness
    if (n > 2) {
      for (i = 0; i < n; i++) {
        Cb[i] = Cb[i].doubleValue() / ((n - 1) * (n - 2));
      }
    } else {
      for (i = 0; i < n; i++) {
        Cb[i] = 1.0;
      }
    }
    return;
  }

  /**
  * Calculates a (normalized) degree centrality of a vertex. 
  * @param  vertex  the vertex for which degree centrality is computed. 
  * @return  the degree centrality value of the vertex.
  */

  public Double findDegreeOf(V vertex) {
    Set<E> EdgeSet = G.edgesOf(vertex);
    Integer D = EdgeSet.size();
    if (n > 1) {
      return D.doubleValue() / (n - 1);
    } else
      return 0.0;
  }

  /**
  * Calculates a (normalized) closeness centrality of a vertex. 
  * @param  vertex the vertex for which closeness centrality is computed. 
  * @return the closeness centrality value of the vertex.
  */

  public Double findClosenessOf(V vertex) {
    // find a sum of path from vertex to all other vertices
    int m = IndexMap.get(vertex);
    Double sum = 0.0;
    for (int i = 0; i < m; i++) {
      sum = sum + DistanceMatrix[m][i];

    }
    for (int i = m + 1; i < n; i++) {
      sum = sum + DistanceMatrix[m][i];
    }

    if ((sum != 0) && (n > 1)) {
      return (n - 1) / sum;
    } else
      return 0.0;
  }

  /**
  * Calculates a (normalized) betweenness centrality of a vertex. 
  * @param  vertex the vertex for which closeness centrality is computed. 
  * @return the betweenness centrality value of the vertex.
  */
  public Double findBetweennessOf(V vertex) {

    return Cb[IndexMap.get(vertex)];
  }

  /**
  * Calculates a clustering coefficient of a vertex. 
  * @param  vertex the vertex for which clustering coefficient is computed. 
  * @return the clustering coefficient value of the vertex.
  */
  public Double findClusteringOf(V vertex) {

    // find the neighbors of our vertex

    Integer v = IndexMap.get(vertex);
    Vector<Integer> NeighborSet = new Vector<Integer>();
    NeighborSet.add(v);
    int i = 0;
    int j = 0;
    for (i = 0; i < n; i++) {
      if (AdjacencyMatrix[v][i] == 1) {
        NeighborSet.add(i);
      }
    }
    if (NeighborSet.contains(v)) {
      NeighborSet.removeElement(v);
    }

    // compute the number of edges between the neighbors of our vertex
    Integer Sum = 0;
    Integer ivertex, jvertex;
    for (i = 0; i < NeighborSet.size(); i++) {
      for (j = i + 1; j < NeighborSet.size(); j++) {
        ivertex = NeighborSet.elementAt(i);
        jvertex = NeighborSet.elementAt(j);
        if (AdjacencyMatrix[ivertex][jvertex] == 1) {
          Sum++;
        }
      }
    }

    Integer k = NeighborSet.size();
    if (k > 1) {
      Double D = (2 * Sum.doubleValue()) / (k.doubleValue() * (k.doubleValue() - 1));
      return D;
    } else
      return 0.0;
  }

  /**
  * Calculates the number of edges on the shortest path between two vertices. 
  * @param  s source vertex
  * @param  t destination vertex 
  * @return the number of edges on the shortest path between two vertices.
  */
  public Double getDistance(V s, V t) {
    return DistanceMatrix[IndexMap.get(s)][IndexMap.get(t)];
  }

}