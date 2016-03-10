
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
//import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

//import org.slf4j.LoggerFactory;
/**
 *
 * @author Ayine
 */
public class W2VUtil {
	public String command;
	//public String word1 = "beauty";
	//public String word2 = "cosmetics";
	WordVectors vec;

	public W2VUtil(String dictionary) {
		System.out.println("Loading GoogleNews...");
		try {
			this.vec = WordVectorSerializer.loadGoogleModel(new File("GoogleNews-vectors-negative300.bin"), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// public void generatePyScript()
	// {
	// String dictionary = "GoogleNews-vectors-negative300.bin";
	// BufferedWriter writer;
	// try
	// {
	// writer = new BufferedWriter(new FileWriter(new
	// File("makeSaveModel.py")));
	// writer.write("import gensim\n");
	// writer.write("word2vecmodel =
	// gensim.models.Word2Vec.load_word2vec_format(\'"
	// + dictionary + "\', binary=True)\n");
	// } catch (IOException e)
	// {
	// System.out.println(e.getMessage());
	// }
	// }
	// public void jythonW2V()
	// {
	// //File gModel = new File("./GoogleNews-vectors-negative300.bin.gz");
	// String dictionary = "GoogleNews-vectors-negative300.bin";
	//
	// PythonInterpreter pi = new PythonInterpreter();
	// pi.exec("import sys");
	// pi.exec("sys.path.insert(0,
	// '/Users/Ayine/anaconda/lib/python2.7/site-packages/')");
	// pi.exec("sys.path.insert(0, '/usr/local/lib')");
	// pi.exec("import gensim");
	// pi.exec("from distance import distance");
	//
	// PyObject w2vmodl =
	// pi.eval("gensim.models.Word2Vec.load_word2vec_format(\'"
	// + dictionary + "\', binary=True)\n");
	// PyFunction pf = (PyFunction)pi.get("distance");
	// PyObject word1 = (PyObject)(new PyString("beauty"));
	// PyObject word2 = (PyObject)(new PyString("cosmetics"));
	//
	// PyInteger distance = (PyInteger)pf.__call__((PyObject)w2vmodl, word1,
	// word2);
	// System.out.println(distance);
	// }
	public double distance(String word1, String word2) {
		try {
			return vec.similarity(word1, word2);
		} catch (Exception e) {
			e.printStackTrace();
			return -1.0;
		}
	}

	public double dl4jMimic(String word1, String word2) {
		return Math.random();
	}
}
