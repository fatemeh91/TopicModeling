
import cc.mallet.util.*;
import cc.mallet.types.*;
import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.topics.*;

import java.util.*;
import java.util.regex.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws Exception {
    	String DS_path="sample/Health.txt";
    	String Sp_path="spilitdata";
    	String Rev_path="reviews";
    	splitdataset S_DS=new splitdataset(DS_path,Sp_path); 
    	
    	standradizingdataset St_DS=new standradizingdataset(Sp_path,Rev_path);
    	
}
}
