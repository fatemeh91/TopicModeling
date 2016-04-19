
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
		filenumber=0;
	}
	/*
	 * first of all spliting our data to be fit in memory
	 */
	public void split_dataset(Path RDSpath,Path DSpath,Path reviwe_path,boolean splitds,boolean reviews_ds_must_created,String output_path) throws IOException{
		if(splitds){
		Splitingds Sp_DS=new  Splitingds(RDSpath,DSpath);
		Sp_DS.splitting();
		indexDocs(DSpath,reviwe_path,reviews_ds_must_created,output_path);
		}
		else {
			System.out.println("splilit nemishe");
			indexDocs(DSpath,reviwe_path,reviews_ds_must_created,output_path);
		}
		
	}
	
	// reading all the text files from directory 
	public void indexDocs(Path DSpath,final Path reviwe_path,final boolean reviews_ds_must_created,final String output_path) throws IOException {
		if (Files.isDirectory(DSpath)) {
			Files.walkFileTree(DSpath, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.getFileName().toString().endsWith(".txt")){
						filenumber++;
						indexDoc(file,reviwe_path.toString(),reviews_ds_must_created,output_path);

					}
					return FileVisitResult.CONTINUE;
				}
					
			});
		} 
		else {
			if (DSpath.getFileName().toString().endsWith(".txt")){
				filenumber++;
				indexDoc(DSpath,reviwe_path.toString(),reviews_ds_must_created,output_path);
				
				}
		}
	}

	
	// index each text file from directory
	private void indexDoc(Path file, String reviwe_path, boolean reviews_ds_must_created,String output_path) {
		ArrayList<String> Words = new ArrayList<>();
		try {
			int Docnomber =0;
			if(reviews_ds_must_created){
			FileDetector files=new FileDetector();
			Docnomber =files.SepratingDoc(file.toFile(),filenumber,reviwe_path);
			}
			else {
				Docnomber=retrive_docnumber_perfile(filenumber,reviwe_path);
				
			}
			System.out.println("File   "+ filenumber + "    docs number  "+Docnomber);
			File sourceLocation=new File(reviwe_path);
			File targetLocation=new File(output_path);
			 makeacopy_of_reviews(sourceLocation,targetLocation);
			System.out.println(" FILE NUMBER   "+filenumber +"   docs_per_file:  "+Docnomber);
			for(int i=1; i<=Docnomber;i++){
			Path docpath=Paths.get(reviwe_path+"/file"+filenumber+"review"+i+".txt");
			File t=new File(reviwe_path+"/file"+filenumber+"review"+i+".txt");
			if(t.exists()){
			ArrayList<STINT> AllWordsperDoc = rawwords.preprocssingonfile(docpath.toFile()); // pre-processing

			Words.clear();
			
			for (int k = 0; k < AllWordsperDoc.size(); k++) {
						if (!indexed(Words, AllWordsperDoc.get(k).word))
							Words.add(AllWordsperDoc.get(k).word);
				}
			
			new BuildSimGraph(Words,output_path,filenumber,i,util);
			}
			else {
				System.out.println("     File :    "+reviwe_path+"/file"+filenumber+"review"+i+".txt" +"     dose not exist");
			}
			}
			} catch (Exception ex) {
			Logger.getLogger(WordIndexing.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	

	private void makeacopy_of_reviews(File sourceLocation, File targetLocation) throws IOException {
	
		 if (sourceLocation.isDirectory()) {
	            if (!targetLocation.exists()) {
	                targetLocation.mkdir();
	            }

	            String[] children = sourceLocation.list();
	            for (int i=0; i<children.length; i++) {
	            	makeacopy_of_reviews(new File(sourceLocation, children[i]),
	                        new File(targetLocation, children[i]));
	            }
	        } else {
	        	if(sourceLocation.getName().contains("file"+filenumber)){
	            InputStream in = new FileInputStream(sourceLocation);
	            OutputStream out = new FileOutputStream(targetLocation);

	            // Copy the bits from instream to outstream
	            byte[] buf = new byte[1024];
	            int len;
	            while ((len = in.read(buf)) > 0) {
	                out.write(buf, 0, len);
	            }
	            in.close();
	            out.close();
	            }
	        }
		 
		
	}
	private int retrive_docnumber_perfile(int filenumber2, String reviwe_path) {
		int review_number_perfile=0;
		
		try {
			Stream<Path> whole_file = Files.list((Paths.get(reviwe_path)));
			Object[] allfiles = whole_file.toArray();
				for(Object temp:allfiles)
					if(temp.toString().contains("file"+filenumber2))
						review_number_perfile++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return review_number_perfile;
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
