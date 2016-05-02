import java.util.Arrays;
import java.util.Comparator;

public class sort_by_index {

	 Integer[] idx ;
	 float[] data;
	public void setdata(double [] dist){
		data=new float[dist.length];
		idx=new Integer [dist.length];
		for(int i=0;i< dist.length;i++){
			data[i]=(float)dist[i];		
			idx[i]=i;
		}
		
	}
public Integer [] sort(){ 
	Arrays.sort(idx, new Comparator<Integer>() {
	    @Override public int compare(final Integer o1, final Integer o2) {
	        return Float.compare(data[o1], data[o2]);
	    }
	});
	
return idx;	
}
}
