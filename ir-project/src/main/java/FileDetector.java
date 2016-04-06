import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileDetector {

	public FileDetector() {
	}

	int SepratingDoc(File file) throws IOException {
		
		int Docnumber=WriteDistincfile(new BufferedReader(new FileReader(file)));
		return Docnumber;
	}
	private int WriteDistincfile(BufferedReader reader) throws IOException, IOException {
		
		Writer writer = null;
		String line;
		int docno2 = 0;
		boolean samedoc = false;
		while ((line = reader.readLine()) != null) {
			if (line.contains("product") && line.contains("productId")) {
				samedoc = !samedoc;
				docno2++;
			if(samedoc)
				writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream("review" + docno2 + ".txt"), "utf-8"));
			else{
				writer.close();
				writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream("review" + docno2 + ".txt"), "utf-8"));
				samedoc=!samedoc; 
			}
			}
			
			if (samedoc) {
				writer.write(line + "\n");	
				System.out.println("writam mikone");
				
			}
			
			if(line.contains("eof"))
				writer.close();
			
		}
		return docno2;
	}
}
