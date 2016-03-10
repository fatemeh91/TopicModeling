
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

/**
 *
 * @author Sepehr
 */
public class ReadDataset {
	public static W2VUtil util;

	private static boolean indexed(ArrayList<String> GraphWords, String word) {
		boolean exist = false;
		for (int i = 0; i < GraphWords.size(); i++)
			if (GraphWords.get(i).equals(word))
				exist = true;

		return exist;
	}

	/*
	 * private static void BuildingGraph(ArrayList<String> GraphWords) { for(int
	 * i=0;i<GraphWords.size();i++){ for(int j=0;j<GraphWords.size();j++){
	 * if(i!=j) System.out.println("nothing yet"); // CALL NODE FOR I WORDS //
	 * CALL EDGES FOR ITS SIMILAIRTY
	 * 
	 * } } }
	 */

	public ReadDataset() {
	}

	private static String dspath;

	private static void indexDocs(Path DSpath) throws IOException {

		if (Files.isDirectory(DSpath)) {// is a directory of files or not

			Files.walkFileTree(DSpath, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					//System.out.println("file name: "+file.getFileName());
					if(file.getFileName().toString().endsWith(".txt")){
				
						indexDoc(file, attrs.lastModifiedTime().toMillis());
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			indexDoc(DSpath, Files.getLastModifiedTime(DSpath).toMillis());
		}

	}

	private static void indexDoc(Path file, long toMillis) {
	
		Stopwords restopwords = new Stopwords();
		ArrayList<String> GraphWords = new ArrayList<>();
		try {
			restopwords.Docno = 0;
			ArrayList<STINT> AllWordDoc = restopwords.read(file.toFile()); // pre processing 
			int totaldocnumberinfile = AllWordDoc.get(AllWordDoc.size() - 1).docnumber;
			for (int j = 1; j <totaldocnumberinfile;j++) {
				WordGraph wg = new WordGraph();
				for (int i = 0; i < AllWordDoc.size(); i++) {
					if (AllWordDoc.get(i).docnumber == j) {
						
						if (!indexed(GraphWords, AllWordDoc.get(i).word))
							GraphWords.add(AllWordDoc.get(i).word);
						
						
						
						// BuildingGraph(GraphWords);
						for (int n = 0; n < GraphWords.size() - 1; n++) {
							System.out.println("GraphWords :"+GraphWords.get(n) );
							wg.addVertex(GraphWords.get(n));
						}
						
						for(int n=0;n<GraphWords.size() - 1; n++){
							for(int m=0;m<GraphWords.size() - 1; m++){
								if(n!=m){
									String w1=GraphWords.get(n);
									String w2=GraphWords.get(m);
									double dist = util.distance(w1, w2);
									wg.addWeightedEdge(w1, w2, dist);
								}
							}
						}
					System.out.println("processing for document : "+j);
					wg.getMST();
					wg.centralityAnalysis();
					}
		
				}
			}
		} catch (Exception ex) {
			Logger.getLogger(ReadDataset.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void main(String[] args) throws IOException {
		 util = new W2VUtil(null);
		dspath = "./sample";
		final Path DSDir = Paths.get(dspath);
		if (!Files.isReadable(DSDir)) {
			System.out.println("Data set directory '" + DSDir.toAbsolutePath()
					+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}
		indexDocs(DSDir);// writer + the path of original unindexed documents
	}
}
