import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

		String dspath = "./sample";
		final Path DSDir = Paths.get(dspath);
		
		if (!Files.isReadable(DSDir)) {
			System.out.println("Data set directory '" + DSDir.toAbsolutePath()
					+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}
		
		
		
		WordIndexing loadds=new WordIndexing();
		loadds.indexDocs(DSDir);// writer + the path of original unindexed documents
	
	}
	

}
