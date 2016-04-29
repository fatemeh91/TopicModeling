	import java.io.BufferedReader;
	import java.io.BufferedWriter;
	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileOutputStream;
	import java.io.FileReader;
	import java.io.IOException;
	import java.io.OutputStreamWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
	import java.util.StringTokenizer;
	import java.util.regex.Matcher;
	import java.util.regex.Pattern;

	public class lableextraction {
		int lables;
		
		ArrayList<String> visited_labels;

		ArrayList<Label> labels_of_whole_files;
		
		public lableextraction(final String files_path) {
			lables=0;		
			labels_of_whole_files=new ArrayList<>();
			if (Files.isDirectory(Paths.get(files_path))) {
				try {
					Files.walkFileTree(Paths.get(files_path), new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
							if (file.getFileName().toString().endsWith(".txt")){
								lable_coutn(files_path+"/"+file.getFileName().toString());
							}
							return FileVisitResult.CONTINUE;
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				System.out.println("there is no directory");
				}	
//			for(int i=0;i<visited_labels.size();i++)
//			System.out.println(visited_labels.get(i));
			System.out.println("overall label counts    "+lables);
			System.out.println("lables count less than < 50 "+labels_of_whole_files.size());
			
		}
		public void lable_coutn(String categires_file_path)throws IOException{			
			visited_labels=new ArrayList<>();
	    	 Label labels_of_file=new Label();
	    	 ArrayList<String> temp_array_labels=new ArrayList<>();
			Pattern pattern = Pattern.compile(".*\\d+[A-Z]*\\d+[A-Z]*\\d*");
			File file=new File(categires_file_path);
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line=null;
				boolean newcat=false;
			
				while ((line = reader.readLine()) != null) {
					Matcher m = pattern.matcher(line);
					
					if(line.contains("review/text") || line.isEmpty())
						newcat=false;
					
					if(newcat){
						 StringTokenizer st = new StringTokenizer(line.trim());
					     while (st.hasMoreTokens()) {
					    	 String tmplab=st.nextToken();
					    	 if(!isindex(tmplab))
					    		 lables++;
					    	 temp_array_labels.add(tmplab);
					    	 
					     			}
					     }
					
					if( m.matches())
					newcat=true;
					
				}
				if(!label_existance(temp_array_labels)){
					labels_of_file.setlables(temp_array_labels);
					labels_of_whole_files.add(labels_of_file);
					}
		}
		private boolean label_existance(ArrayList<String> temp_array_labels) {
			boolean isexist=false;
			for(Label labels : labels_of_whole_files)
				if(labels.getlables() == temp_array_labels)
					isexist=true;
			return isexist;
		}
		private boolean isindex(String words) {	
			System.out.println(words);
			boolean exist=false;
			if(visited_labels.size()==0){
				visited_labels.add(words);
				exist=false;
			}
			else{
				for(String v_word : visited_labels)
					if(v_word.equals(words))
					exist=true;
					if(!exist)
					visited_labels.add(words);					
			}
			return exist;
		}
		public static void main (String args[]){
			String categires_file_path="selected_reviews";
			lableextraction no_lab;
			no_lab = new lableextraction(categires_file_path);
			
		}
	}