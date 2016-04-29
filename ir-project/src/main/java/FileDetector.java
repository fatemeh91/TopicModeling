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
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import io.dropwizard.util.Size;

public class FileDetector {
	 public ArrayList<String> product_lables;
	 public ArrayList<ArrayList<String>> cache;	 
	 
	public FileDetector() {
	cache=new ArrayList<>();
	}

	int SepratingDoc(File file,int filenumber,String reviwe_path) throws IOException {
		System.out.println("sprating ham call mishe");
		int Docnumber=WriteDistincfile(file,reviwe_path,filenumber);
		return Docnumber;
	}
	private int WriteDistincfile(File file,String output_path, int filenumber) throws IOException, IOException {
		String Product_ID=null;
		Writer writer = null;
		String line;
		int docno2 = 0;
		boolean samedoc = false;
		boolean start_writing=false;
		if(! FilenameUtils.isExtension(file.getName(),"DS_Store")){
		BufferedReader reader = new BufferedReader(new FileReader(file));
		while ((line = reader.readLine()) != null) {
			if (line.contains("product") && line.contains("productId")) {
				line=line.replace("product/productId:","");
				Product_ID=line;
				samedoc = !samedoc;
				start_writing=false;
				
				
			}
			
			if (samedoc  &&  line.contains("review") && line.contains("text")){ 
				start_writing=true;
				docno2++;
			}
			
			
			if(samedoc && start_writing && writer == null){
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_path+"/file"+filenumber+"review" + docno2 + ".txt"), "utf-8"));
				ArrayList<String> lables=extract_categories(Product_ID);
				writer.write(Product_ID+"\n");
				for(int i=0;i<lables.size();i++){
					String s_labale=lables.get(i).replace(",","");
					writer.write(s_labale+" ");
					
				}
				writer.write("\n");
				System.out.println("   file number   "+filenumber+"    for review number   "+docno2 + "     product_id     "+Product_ID +"     we have lables of size :  "+lables.size());
				
			}
			
			if (samedoc  &&  start_writing &&  writer!=null){
				
				writer.write(line+"\n");	
				
				}
	
			if((!start_writing && !samedoc && writer!=null ) || (samedoc && line.isEmpty()) ){
				start_writing=false;
				samedoc=false;
				writer.close();	
				writer=null;
				}
		}
		if(reader.readLine() == null){
			writer=null;
		}
		}
		return docno2;
	
		}
	
	private ArrayList<String> extract_categories(String product_ID) throws IOException {
		// TODO Auto-generated method stub
		//product_ID="B000278ADA";
		ArrayList<String> lables=new ArrayList<>();
		if((cache.size()==0) ||(index_incache(product_ID)==-2)){
		String Categories_path="sub-categorize/categories.txt";		
		Pattern pattern = Pattern.compile(".*\\d+.*\\d+.*");
		File file=new File(Categories_path);
		boolean find_cat=false;
		
		if(! FilenameUtils.isExtension(file.getName(),"DS_Store")){
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line=null;
			boolean start_processing=false;
			while ((line = reader.readLine()) != null) {
				Matcher m = pattern.matcher(line);
							
				if( m.matches() ){
					if(line.equals(product_ID.trim()))
						start_processing=true;
					else
						start_processing=false;
						}
				if( find_cat && !m.matches() && start_processing){
					 StringTokenizer st = new StringTokenizer(line);
					while(st.hasMoreTokens())
						lables=extract_unique_labels(st.nextToken(),lables);
							}
				if( start_processing && m.matches())
					find_cat=!find_cat;
				}
			}
 if (cache.size()==0){
	 product_lables=new ArrayList<>();
			product_lables.add(product_ID);
			for(String L:lables)
				product_lables.add(L);
			cache.add(product_lables);
			
		}
else if((cache.size()>0) && (index_incache(product_ID)==-2)){
	product_lables=new ArrayList<>();
	product_lables.add(product_ID);
	for(String L:lables)
		product_lables.add(L);
	cache.remove(0);
	cache.add(product_lables);
	
}
		
		}
		else if((index_incache(product_ID)!=-2)){
			product_lables=new ArrayList<>();
			int index=index_incache(product_ID);
			product_lables=cache.get(index);
			for(int i=1;i<product_lables.size();i++)
				lables.add(product_lables.get(i));
		}
		
		return lables;
	}
	private int index_incache(String Product_ID) {
		int index=-2;
	for(int i=0;i<cache.size();i++)
		if(cache.get(i).get(0).equals(Product_ID))
			index=i;
			
	
	return index;
	}
	

	private ArrayList<String> extract_unique_labels(String nextToken, ArrayList<String> lables) {
	if (lables.size()==0)
		lables.add(nextToken);
	
	else{
		boolean notexist_lables=true;
		for(int i=0;i<lables.size();i++)
			if(lables.get(i).equals(nextToken))
				notexist_lables=false;
		
		if(notexist_lables)
			lables.add(nextToken);
	}
		return lables;
	}
}
