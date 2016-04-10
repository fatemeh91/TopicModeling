import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.netlib.util.booleanW;

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
	public static void main(String[] args) throws IOException {
		W2VUtil.DEBUG_MODE = true;

		String dspath = "sample/Health.txt";
		String splitppath="spilitdata";
		String reviewpath="review";
		final Path DSDir = Paths.get(dspath);
		final Path SDir = Paths.get(splitppath);
		final Path reviwe_path=Paths.get(reviewpath);
		final boolean splitds=true;
		
		if (!Files.isReadable(DSDir)) {
			System.out.println("Data set directory '" + DSDir.toAbsolutePath()
					+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}
		
		
		
		WordIndexing loadds=new WordIndexing();
		loadds.split_dataset(DSDir,SDir,reviwe_path,splitds);
		//loadds.indexDocs(DSDir);// writer + the path of original unindexed documents
	
	}
	

}
