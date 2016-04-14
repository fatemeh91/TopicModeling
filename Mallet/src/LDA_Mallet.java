import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.share.upenn.ner.LengthBins;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelSequence;

public class LDA_Mallet {
	public ArrayList<Pipe> pipeList ;
	public ParallelTopicModel model;
	InstanceList instances;
	public LDA_Mallet() throws IOException{
		 pipeList = new ArrayList<Pipe>();
        // Pipes: lowercase, tokenize, remove stopwords, map to features
        pipeList.add( new CharSequenceLowercase() );
        pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
        pipeList.add( new TokenSequenceRemoveStopwords(new File("mallet/stoplists/en.txt"), "UTF-8", false, false, false) );
        pipeList.add( new TokenSequence2FeatureSequence() );
      //  System.out.println("this is a puiplist");
      //  System.out.println(pipeList.get(0));
        instances = new InstanceList (new SerialPipes(pipeList));

        Reader fileReader = new InputStreamReader(new FileInputStream(new File("mallet/sample-data/web/en/trainds.txt")), "UTF-8");
        instances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
                                               3, 2, 1)); // data, label, name fields
       
        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is the parameter for a single dimension of the Dirichlet prior.
        int numTopics = 2;
        model = new ParallelTopicModel(numTopics, 1.0, 0.01);

        model.addInstances(instances);

        // Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
        model.setNumThreads(2);

        // Run the model for 50 iterations and stop (this is for testing only, 
        //  for real applications, use 1000 to 2000 iterations)
        model.setNumIterations(10);
        model.estimate();
        
       // Show the words and topics in the first instance
        // The data alphabet maps word IDs to strings
        Alphabet dataAlphabet = instances.getDataAlphabet();
        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;
    
        Formatter out = new Formatter(new StringBuilder(), Locale.US);
        for (int position = 0; position < tokens.getLength(); position++) {
            out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
        }

       /* 
        // Estimate the topic distribution of the first instance, 
        //  given the current Gibbs state.
        double[] topicDistribution = model.getTopicProbabilities(0);

        // Get an array of sorted sets of word ID/count pairs
        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
        
        // Show top 5 words in topics with proportions for the first document
        for (int topic = 0; topic < numTopics; topic++) {
            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
            
            out = new Formatter(new StringBuilder(), Locale.US);
            out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
            int rank = 0;
            while (iterator.hasNext() && rank < 5) {
                IDSorter idCountPair = iterator.next();
                out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
                rank++;
            }
            
        }*/
	}
	public  void test(String testfile) throws IOException, FileNotFoundException{
		 System.out.println("man in jam");
		  // Create a new instance with high probability of topic 0
		 pipeList = new ArrayList<Pipe>();
		 pipeList.add( new CharSequenceLowercase() );
	        pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
	        pipeList.add( new TokenSequenceRemoveStopwords(new File("mallet/stoplists/en.txt"), "UTF-8", false, false, false) );
	        pipeList.add( new TokenSequence2FeatureSequence() );
		InstanceList testinstances = new InstanceList (new SerialPipes(pipeList));
		Reader fileReader = new InputStreamReader(new FileInputStream(new File(testfile)), "UTF-8");
		testinstances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),3, 2, 1).next());

        // Create a new instance named "test instance" with empty target and source fields.
       // testing = new InstanceList();
       // testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));
		//System.out.println();
       
        
        
        TopicInferencer inferencer = model.getInferencer();
        double dist[]=inferencer.getSampledDistribution(testinstances.get(0), 10, 10,10) ;
        
        System.out.println(dist.length);
        System.out.println("0\t" + dist[0]);
        System.out.println("0\t" + dist[1]);
	}
}
