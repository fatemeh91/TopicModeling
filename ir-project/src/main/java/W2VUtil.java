/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
//import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.embeddings.wordvectors.*;
//import org.slf4j.LoggerFactory;
/**
 *
 * @author Ayine
 */
public class W2VUtil {
	public static boolean DEBUG_MODE;
    public String command;
    WordVectors vec;
    public static final int ANGULAR_DIST = 1;
    public int distMeasure;
    public W2VUtil(String dictionary, int dm)
    {
    	this.distMeasure = dm;
    	System.out.println("Loading GoogleNews...");
    	if (!DEBUG_MODE)
	        try {
				this.vec = WordVectorSerializer.loadGoogleModel(new File(dictionary), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
    }
    
//    public void generatePyScript()
//    {
//        String dictionary = "GoogleNews-vectors-negative300.bin";
//        BufferedWriter writer;
//        try
//        {
//            writer = new BufferedWriter(new FileWriter(new File("makeSaveModel.py")));  
//            writer.write("import gensim\n");
//            writer.write("word2vecmodel = gensim.models.Word2Vec.load_word2vec_format(\'"
//                    + dictionary + "\', binary=True)\n");
//        } catch (IOException e) 
//        {
//            System.out.println(e.getMessage());
//        }
//    }
//    public void jythonW2V()
//    {
//        //File gModel = new File("./GoogleNews-vectors-negative300.bin.gz");
//        String dictionary = "GoogleNews-vectors-negative300.bin";
//        
//        PythonInterpreter pi = new PythonInterpreter();
//        pi.exec("import sys");        
//        pi.exec("sys.path.insert(0, '/Users/Ayine/anaconda/lib/python2.7/site-packages/')");
//        pi.exec("sys.path.insert(0, '/usr/local/lib')");
//        pi.exec("import gensim");
//        pi.exec("from distance import distance");
//
//        PyObject w2vmodl = pi.eval("gensim.models.Word2Vec.load_word2vec_format(\'"
//                    + dictionary + "\', binary=True)\n");
//        PyFunction pf = (PyFunction)pi.get("distance");
//        PyObject word1 = (PyObject)(new PyString("beauty"));
//        PyObject word2 = (PyObject)(new PyString("cosmetics"));
//        
//        PyInteger distance = (PyInteger)pf.__call__((PyObject)w2vmodl, word1, word2); 
//        System.out.println(distance);
//    }
    public double distance(String word1, String word2)
    {
    	if (!DEBUG_MODE)
    	{
    		try
    		{
	        	if (this.distMeasure == ANGULAR_DIST)
	        	{
	        		return Math.acos(vec.similarity(word1, word2)) * 2 / Math.PI;
	        	}
	            return 1 - vec.similarity(word1, word2);
    		}
            catch(Exception e)
            {
                e.printStackTrace();
                return -1.0;
            }
    	}
    	else return Math.random();
    }
    public double distance(Vector<String> y, Vector<String> p,boolean hierarchical)
    {
    	double distSum = 0;
    	double tmpDist = 0;
    	int word_position=0;
    	float weight_dominator=100;
    	
    	
    	if (!DEBUG_MODE)
    	{
    		for (int i = 0; i < y.size(); i ++)
    		{	word_position++;
    			String wy = y.get(i);
    			for (int j = i + 1; j < p.size(); j ++)
    			{
    				String wp = p.get(j);
    				try
    				{
    					if(!vec.hasWord(wy) || !(vec.hasWord(wp)))
    						continue;
    					if(!hierarchical){
	    				if (this.distMeasure == ANGULAR_DIST)
	    	        		tmpDist =  Math.acos(vec.similarity(wy, wp)) * 2 / Math.PI;
	    				else
	    					tmpDist = 1 - vec.similarity(wy, wp);
    					}
    					else{
    						if (this.distMeasure == ANGULAR_DIST)
    	    	        		tmpDist = ( Math.acos(vec.similarity(wy, wp)) * 2 / Math.PI) * (word_position/weight_dominator);
    	    				else
    	    					tmpDist =( 1 - vec.similarity(wy, wp)) * (word_position/weight_dominator);
        					}
    						
    					
	    				if (tmpDist == Double.NaN)
    	        		{
	    					distSum += Math.random();
	    				}
    	        		else
    	        		{
    	        			distSum +=tmpDist;
    	        		
    	        		}
    				}
    				catch(Exception e)
    	            {
    	                e.printStackTrace();
    	                distSum += Math.random();
    	            }
    			}
    		}
    	}
    	else 
    	{
    		for (String wy: y)
    		{
    			for (String wp: p)
    			{
    				distSum += Math.random();
    			}
    		}
    	}
		
    	distSum /= (y.size()*p.size());
    
    	return distSum;
    }
}
