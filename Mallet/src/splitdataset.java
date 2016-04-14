import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.SpinnerListModel;

public class splitdataset {
String Ds_path;
String Split_Ds_path;
	public splitdataset(String Ds_path,String Split_Ds_path) throws IOException{
		this.Ds_path=Ds_path;
		this.Split_Ds_path=Split_Ds_path;
		splitting();
	}
	
	public void splitting() throws IOException{
		String filepath=Ds_path;
		 RandomAccessFile raf = new RandomAccessFile(filepath, "r");
		    long numSplits = 10; //from user input, extract it from args
		    long sourceSize = raf.length();
		    long bytesPerSplit = sourceSize/numSplits ;
		    long remainingBytes = sourceSize % numSplits;

		    int maxReadBufferSize = 8 * 1024; //8KB
		    String Spath=Split_Ds_path.toString();
		    for(int destIx=1; destIx <= numSplits; destIx++) {
		    	
		        BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(Spath+"/split"+destIx+".txt"));
		        if(bytesPerSplit > maxReadBufferSize) {
		            long numReads = bytesPerSplit/maxReadBufferSize;
		            long numRemainingRead = bytesPerSplit % maxReadBufferSize;
		            for(int i=0; i<numReads; i++) {
		                readWrite(raf, bw, maxReadBufferSize);
		            }
		            if(numRemainingRead > 0) {
		                readWrite(raf, bw, numRemainingRead);
		            }
		        }else {
		            readWrite(raf, bw, bytesPerSplit);
		        }
		        bw.close();
		    }
		    if(remainingBytes > 0) {
		        BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(Spath+"/split"+(numSplits+1)+".txt"));
		        readWrite(raf, bw, remainingBytes);
		        bw.close();
		    }
		        raf.close(); 
	} 
	public void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
	    byte[] buf = new byte[(int) numBytes];
	    int val = raf.read(buf);
	    if(val != -1) {
	        bw.write(buf);
	    }
	}

}
