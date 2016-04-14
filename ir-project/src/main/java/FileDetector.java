import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;

public class FileDetector {

	public FileDetector() {
	}

	int SepratingDoc(File file,int filenumber,String reviwe_path) throws IOException {
		
		int Docnumber=WriteDistincfile(file,reviwe_path,filenumber);
		return Docnumber;
	}
	private int WriteDistincfile(File file,String output_path, int filenumber) throws IOException, IOException {
		
		Writer writer = null;
		String line;
		int docno2 = 0;
		boolean samedoc = false;
		if(! FilenameUtils.isExtension(file.getName(),"DS_Store")){
		BufferedReader reader =new BufferedReader(new FileReader(file));
		while ((line = reader.readLine()) != null) {
			if (line.contains("product") && line.contains("productId")) {
				samedoc = !samedoc;
				docno2++;
			if(samedoc)
				writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(output_path+"/file"+filenumber+"review" + docno2 + ".txt"), "utf-8"));
			else{
				writer.close();
				writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(output_path+"/file"+filenumber+"review" + docno2 + ".txt"), "utf-8"));
				samedoc=!samedoc; 
			}
			}
			
		
			if (samedoc && writer != null) {
				
				writer.write(line + "\n");	
				
			}
			
			if((!samedoc && writer!=null ) || (samedoc && line.isEmpty()))
				writer.close();	
		}
		}
		return docno2;
	
		}
}
