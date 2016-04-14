import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.io.FilenameUtils;


public class standradizingdataset {
	Path Split_DS;
	Path review_path;
	int filenumber;
	LDA_Mallet mallet;
	public standradizingdataset(String Split_DS,String review_path){
		this.Split_DS=Paths.get(Split_DS);	
		this.review_path=Paths.get(review_path);
		this.filenumber=0;
		try {
			System.out.println(Split_DS); 
			mallet=new LDA_Mallet();
			SepratingDocs();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
public	void SepratingDocs() throws IOException {
	
		if (Files.isDirectory(Split_DS)) {
			Files.walkFileTree(Split_DS, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.getFileName().toString().endsWith(".txt"))
						filenumber++;
					indexDoc(file,review_path.toString(),filenumber);
					System.out.println("line 46");
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			indexDoc(Split_DS,review_path.toString(),filenumber);
		}
	}
	
	private void indexDoc(Path file,String review_path,int filenumber) throws IOException
	{	int doc_p_file=0;
		File f=new File(file.toString());
		
		doc_p_file=WriteDistincfile(f,review_path,filenumber);
		
		for (int i=1;i<=doc_p_file;i++)
			mallet.test(review_path+"/file"+filenumber+"review"+i+".txt");
		
	}
	
	
	
	private int WriteDistincfile(File file,String output_path, int filenumber) throws IOException, IOException {
		
		Writer writer = null;
		String line;
		int docno2 = 0;
		boolean samedoc = false;
		boolean start_writing=false;
		if(! FilenameUtils.isExtension(file.getName(),"DS_Store")){
		BufferedReader reader = new BufferedReader(new FileReader(file));
		while ((line = reader.readLine()) != null) {
			if (line.contains("product") && line.contains("productId")) {
				samedoc = !samedoc;
				start_writing=false;
				docno2++;
			if(samedoc){
				writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(output_path+"/file"+filenumber+"review" + docno2 + ".txt"), "utf-8"));
				writer.write(line+","+" ");
			}
			else{
				writer.close();
				writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(output_path+"/file"+filenumber+"review" + docno2 + ".txt"), "utf-8"));
				samedoc=!samedoc; 
			}
			}
			
			if (samedoc && writer != null &&  line.contains("review") && line.contains("text")) 
				start_writing=true;
			
			if (samedoc && writer != null && start_writing) 
				writer.write(line+" ");	
			
			
			if((!samedoc && writer!=null ) || (samedoc && line.isEmpty())){
				start_writing=false;
				writer.close();	
				}
		}
		}
		return docno2;
	
		}

}
