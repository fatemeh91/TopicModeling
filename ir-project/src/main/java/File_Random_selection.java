import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class File_Random_selection {
ArrayList< String> randome_sel_file_name;
int array_file_index;
int file_index;
boolean findallfile;
String targetpath;
int [] rand_file_slec_number;
public File_Random_selection() {
	System.out.println("kollan to constructor miad");
	randome_sel_file_name=new ArrayList<>();
	file_index=0;
	array_file_index=0;
	findallfile=false;
	targetpath="selected_reviews";
	filerandomeselection();
	
	for(int i=0;i<randome_sel_file_name.size();i++)
		System.out.println(randome_sel_file_name.get(i));
	
}

private void filerandomeselection(){
	
	String  path= "reviews"; 
	 try {
		int Files_number = (int)Files.list(Paths.get(path)).count();
		 rand_file_slec_number=new int [50];
		for (int i=0;i<50;i++){
			Random rn = new Random();
			rand_file_slec_number[i] = rn.nextInt(Files_number) + 1;
			}
		// sort array's value
		Arrays.sort(rand_file_slec_number);
		
		// selecting randome files 
		
		if (Files.isDirectory(Paths.get(path))) {
			 
			Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.getFileName().toString().endsWith(".txt")){
						file_index++;
						if(file_index == rand_file_slec_number[array_file_index] && !findallfile){
							randome_sel_file_name.add(file.getFileName().toString());
							if(array_file_index <49){
							array_file_index++;
							String sourcepath="reviews/"+file.getFileName().toString();
							String newfilename="file1review"+(array_file_index)+".txt";
							Files.copy(Paths.get(sourcepath), (new File(targetpath+"/" +newfilename)).toPath(),
									StandardCopyOption.REPLACE_EXISTING);
							}
							else 
								findallfile=true;
						}
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			System.out.println("there is no directory");
			}
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
	public static void main(String args []){
		File_Random_selection frs=new File_Random_selection();
			} 
}
