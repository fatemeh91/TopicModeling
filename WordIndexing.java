
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fatemeh
 */
public class WordIndexing {
	/*
	 * in this class we read our data set and do some preprocessing on it.
	 */
	public Stopwords rawwords;

	public WordIndexing() {
		rawwords = new Stopwords();
		rawwords.Docno = 0;
	}

	// reading all the text file from directory via this function
	public void indexDocs(Path DSpath) throws IOException {
		if (Files.isDirectory(DSpath)) {

			Files.walkFileTree(DSpath, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

					if (file.getFileName().toString().endsWith(".txt"))
						indexDoc(file);

					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			indexDoc(DSpath);
		}
	}

	// index each file in directory
	private void indexDoc(Path file) {

		ArrayList<String> Words = new ArrayList<>();

		try {

			ArrayList<STINT> AllWordsperDoc = rawwords.preprocssingonfile(file.toFile()); // pre-processing

			int totaldocnumberinfile = rawwords.getDocno();

			for (int j = 1; j < totaldocnumberinfile; j++) {
				for (int i = 0; i < AllWordsperDoc.size(); i++) {
					if (AllWordsperDoc.get(i).docnumber == j) {

						// build array of distinct words in each
						// document(review)
						if (!indexed(Words, AllWordsperDoc.get(i).word))
							Words.add(AllWordsperDoc.get(i).word);

					}
				}
				new BuildSimGraph(Words);
				//break;
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
