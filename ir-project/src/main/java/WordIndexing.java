
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.netlib.util.booleanW;

import com.sun.research.ws.wadl.Doc;

/**
 *
 * @author fatemeh
 */
public class WordIndexing {
	/*
	 * in this class we read our data set and do some preprocessing on it.
	 */
	
	public Stopwords rawwords;
	public W2VUtil util; 
	public int filenumber;
	
	//constructor
	public WordIndexing() {
		util = new W2VUtil("GoogleNews-vectors-negative300.bin", W2VUtil.ANGULAR_DIST);
		rawwords = new Stopwords();
		rawwords.Docno = 0;
		filenumber=1;
	}
	/*
	 * first of all spliting our data to be fit in memory
	 */
	public void split_dataset(Path RDSpath,Path DSpath,Path reviwe_path,boolean splitds) throws IOException{
		if(splitds){
		Splitingds Sp_DS=new  Splitingds(RDSpath,DSpath);
		Sp_DS.splitting();
		}
		indexDocs(DSpath,reviwe_path);
	}
	// reading all the text files from directory 
	public void indexDocs(Path DSpath,final Path reviwe_path) throws IOException {
		if (Files.isDirectory(DSpath)) {

			Files.walkFileTree(DSpath, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

					if (file.getFileName().toString().endsWith(".txt"))
						filenumber++;
						indexDoc(file,reviwe_path.toString());

					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			indexDoc(DSpath,reviwe_path.toString());
		}
	}

	
	// index each text file from directory
	private void indexDoc(Path file, String reviwe_path) {
		ArrayList<String> Words = new ArrayList<>();

		try {
			FileDetector files=new FileDetector();
			int Docnomber =files.SepratingDoc(file.toFile(),filenumber,reviwe_path);
			System.out.println(Docnomber);
			for(int i=1; i<=Docnomber;i++){
			Path docpath=Paths.get(reviwe_path+"/file"+filenumber+"review"+i+".txt");
			ArrayList<STINT> AllWordsperDoc = rawwords.preprocssingonfile(docpath.toFile()); // pre-processing

			int totaldocnumberinfile = rawwords.getDocno();
			System.out.println(totaldocnumberinfile);
			Words.clear();
			
			for (int k = 0; k < AllWordsperDoc.size(); k++) {
						if (!indexed(Words, AllWordsperDoc.get(k).word))
							Words.add(AllWordsperDoc.get(k).word);
				}
			
			new BuildSimGraph(Words,reviwe_path,filenumber,i,util);
				
			}
			} catch (Exception ex) {
			Logger.getLogger(WordIndexing.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/*
	 * this method check to avoid indexing same word tow times
	 */
	private static boolean indexed(ArrayList<String> GraphWords, String word) {
		boolean exist = false;
		for (int i = 0; i < GraphWords.size(); i++)
			if (GraphWords.get(i).equals(word))
				exist = true;

		return exist;
	}

}
