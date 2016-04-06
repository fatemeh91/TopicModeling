/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
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
        try{
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
        		catch(NullPointerException e)
        		{
        			System.exit(1);
        			return 0;
        		}
        	}
        	else return Math.random();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return -1.0;
        }
    }
}
