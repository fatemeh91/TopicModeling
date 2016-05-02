/*
 * 
 * this class is used to rank the exist label 
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import org.junit.experimental.categories.Categories;

public class Rank_based_function {
	public Vector<String> label;
	public Vector<String> prediction;
	public Vector<TopicVecotr> labelsvector;
	public Vector<String> cur_file_topic;
	public double Bscore, Cscore, Dscore, Wscore, Escore, Wscore_wg, Cscore_wg,Mscore;
	public Rank_based_function()
	{	
		this.labelsvector=new  Vector<>();
		labels_extraction();
		try {
			evaluate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void labels_extraction(){
		String categires_file_path="selected_reviews";
		lableextraction label_detect;
		label_detect = new lableextraction(categires_file_path);
		ArrayList<Label> vec_vec_labels=new ArrayList<>();
		vec_vec_labels=label_detect.getlebel_of_whole_files();
		// for each label in category 
		for(Label one_label: vec_vec_labels)
		{
			 Vector<String> templabel=new Vector<String>();
			 //get new label 
			 for(String temp : one_label.getlables())
				 templabel.add(temp);
			 
			 TopicVecotr temptopvect=new TopicVecotr();
			 temptopvect.setlabelVector(templabel);
			 
			 labelsvector.add(temptopvect);
			 
				
		}
	}
	public void setLabel(Vector<String> label)
	{
		this.label = label;
	}
	public void setPrediction(Vector<String> prediction)
	{
		this.prediction = prediction;
	}
	public void evaluate() throws IOException
	{  	W2VUtil.DEBUG_MODE=false;
	    W2VUtil util = new W2VUtil("GoogleNews-vectors-negative300.bin", W2VUtil.ANGULAR_DIST);
		String dir = "results";
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();
		int n = listOfFiles.length;
		System.err.println(n);
		int count = 0;
		int labelcount;
		for (File file: listOfFiles)
		{
			cur_file_topic=new Vector<>();
			this.prediction = new Vector<String>();
			if (file.getName().startsWith(".")) continue;
			double [] MST_weighted_Topic_probability= new double[labelsvector.size()];
			double [] MST_closeness_Topic_probability= new double[labelsvector.size()];
			double [] MST_exhustive_Topic_probability= new double[labelsvector.size()];
			double [] MST_betweenness_Topic_probability= new double[labelsvector.size()];
			double [] Mallet_Topic_probability= new double[labelsvector.size()];
			double [] weighted_Topic_probability= new double[labelsvector.size()];
			double [] closeness_Topic_probability= new double[labelsvector.size()];
			int MST_weighted_rank=0;
			int MST_closeness_rank=0;
			int MST_exhustive_rank=0;
			int MST_betweenness_rank=0;
			int weighted_rank=0;
			int closeness_rank=0;
			int mallet_rank=0;
			
			
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			reader.readLine(); //read and discard item id
			
			//read category into vector
			cur_file_topic.clear();
			String catStr = reader.readLine();
			catStr = catStr.replaceAll("&", "");
			StringTokenizer ltk = new StringTokenizer(catStr, ", ");
			while (ltk.hasMoreTokens())
			{
				this.cur_file_topic.add(ltk.nextToken());
			}
			
			
			if (catStr.isEmpty())
				continue;
			
			
			
			String predStr = null;
			String tmp = reader.readLine();
			while(tmp != null)
			{
				this.prediction.clear();
				if (tmp.contains("MST weighted"))
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
				//	Wscore += util.distance(this.label, this.prediction,false);
					labelcount=0;
					for(TopicVecotr curlab : labelsvector){
						this.label = new Vector<String>();
						setLabel(curlab.getlabelvector());
					MST_weighted_Topic_probability[labelcount]= 1 / (util.distance(this.label, this.prediction,false));
					labelcount++;
					}
					sort_by_index s_dist_a=new sort_by_index();
					s_dist_a.setdata(MST_weighted_Topic_probability);
					Integer[] sorted_indx = s_dist_a.sort();
					int cur_file_lab_ind=find_label_ind(cur_file_topic);
					MST_weighted_rank=find_lable_rank(sorted_indx,cur_file_lab_ind);
					
				}
				
				if (tmp.contains("Mallet"))
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ",");
					System.out.println("token chera nadare "+tk2.countTokens()+"tofile: "+file.getName());
					tk2.nextToken(); //ignoring topic number
					double prob_dist=Double.parseDouble(tk2.nextToken());
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
					
					labelcount=0;
					for(TopicVecotr curlab : labelsvector){
						this.label = new Vector<String>();
						setLabel(curlab.getlabelvector());
					Mallet_Topic_probability[labelcount]= 1 / (util.distance(this.label, this.prediction,false));
					labelcount++;
					}
					sort_by_index s_dist_a=new sort_by_index();
					s_dist_a.setdata(Mallet_Topic_probability);
					Integer[] sorted_indx = s_dist_a.sort();
					int cur_file_lab_ind=find_label_ind(cur_file_topic);
					mallet_rank=find_lable_rank(sorted_indx,cur_file_lab_ind); 
				
				}

				if (tmp.contains("MST closeness"))
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
			//		Cscore += util.distance(this.label, this.prediction,false);
					labelcount=0;
					for(TopicVecotr curlab : labelsvector){
						this.label = new Vector<String>();
						setLabel(curlab.getlabelvector());
					MST_closeness_Topic_probability[labelcount]= 1/ util.distance(this.label, this.prediction,false);
					labelcount++;
					}
					sort_by_index s_dist_a=new sort_by_index();
					s_dist_a.setdata(MST_closeness_Topic_probability);
					Integer[] sorted_indx = s_dist_a.sort();
					int cur_file_lab_ind=find_label_ind(cur_file_topic);
					MST_closeness_rank=find_lable_rank(sorted_indx,cur_file_lab_ind);
		//			System.out.println("MST Closeness Rank:    "+MST_closeness_rank);
				}

				if (tmp.contains("MST exhaustive"))
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
		//			Escore += util.distance(this.label, this.prediction,false);
					labelcount=0;
					for(TopicVecotr curlab : labelsvector){
						this.label = new Vector<String>();
						setLabel(curlab.getlabelvector());
					MST_exhustive_Topic_probability[labelcount]=1/ util.distance(this.label, this.prediction,false);
					labelcount++;
					}
					sort_by_index s_dist_a=new sort_by_index();
					s_dist_a.setdata(MST_exhustive_Topic_probability);
					Integer[] sorted_indx = s_dist_a.sort();
					int cur_file_lab_ind=find_label_ind(cur_file_topic);
					MST_exhustive_rank=find_lable_rank(sorted_indx,cur_file_lab_ind);
		//			System.out.println("MST Exuhstive Rank:    "+MST_exhustive_rank);
				}

				if (tmp.contains("MST betwennness"))
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
//					Bscore += util.distance(this.label, this.prediction,false);
					labelcount=0;
					for(TopicVecotr curlab : labelsvector){
						this.label = new Vector<String>();
						setLabel(curlab.getlabelvector());
					MST_betweenness_Topic_probability[labelcount]= 1/ util.distance(this.label, this.prediction,false);
					labelcount++;
					}
					
					sort_by_index s_dist_a=new sort_by_index();
					s_dist_a.setdata(MST_betweenness_Topic_probability);
					Integer[] sorted_indx = s_dist_a.sort();
					int cur_file_lab_ind=find_label_ind(cur_file_topic);
					MST_betweenness_rank=find_lable_rank(sorted_indx,cur_file_lab_ind);
	//				System.out.println("MST betweenness Rank:    "+MST_betweenness_rank);
				}
				if (tmp.startsWith(" weighted") && tmp.length() <= 10)
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
		//			Wscore_wg += util.distance(this.label, this.prediction,false);
					labelcount=0;
					for(TopicVecotr curlab : labelsvector){
						this.label = new Vector<String>();
						setLabel(curlab.getlabelvector());
					weighted_Topic_probability[labelcount]= 1/ util.distance(this.label, this.prediction,false);
					labelcount++;
					}
					sort_by_index s_dist_a=new sort_by_index();
					s_dist_a.setdata(weighted_Topic_probability);
					Integer[] sorted_indx = s_dist_a.sort();
					int cur_file_lab_ind=find_label_ind(cur_file_topic);
					weighted_rank=find_lable_rank(sorted_indx,cur_file_lab_ind);
		//			System.out.println("wordgraph weighted Rank:    "+weighted_rank);
				}
				if (tmp.startsWith(" closeness") && tmp.length() <= 10)
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
		//			Cscore_wg += util.distance(this.label, this.prediction,false);
					labelcount=0;
					for(TopicVecotr curlab : labelsvector){
						this.label = new Vector<String>();
						setLabel(curlab.getlabelvector());
					closeness_Topic_probability[labelcount]= 1/  util.distance(this.label, this.prediction,false);
					labelcount++;
					}
					sort_by_index s_dist_a=new sort_by_index();
					s_dist_a.setdata(closeness_Topic_probability);
					Integer[] sorted_indx = s_dist_a.sort();
					int cur_file_lab_ind=find_label_ind(cur_file_topic);
					closeness_rank=find_lable_rank(sorted_indx,cur_file_lab_ind);
				}
				if (predStr != null && predStr.isEmpty())
					return;
				tmp = reader.readLine();
			}

		
			reader.close();
			Formatter  expr_perfile=new Formatter(new StringBuilder(), Locale.US);
			 expr_perfile.format("%s \n %s %d  \t \n  %s \t %d \n %s \t %d \n %s \t %d \n %s \t %d \n %s \t %d \n %s \t %d \n ",
					"ranked_based_evaluation","MST Weighted rank:",MST_weighted_rank,
					"MST betweenness rank: ",MST_betweenness_rank,
					"MST closeness rank:",MST_closeness_rank,
					"MST exhustive rank: ",MST_exhustive_rank,
					"Mallet rank: ", mallet_rank, 
					"weighted rank: ",weighted_rank,
					"closeness rank : ",closeness_rank);
			
			
			System.out.println(file.getPath());
			write_on_file(expr_perfile.toString(),file.getPath());
			
			
			count ++;
		}
		
	}
	
	private void write_on_file(String Expriment,String path){
		FileWriter fw;
		
		try {
			fw = new FileWriter(path,true);
			fw.write(Expriment+"\n");
			fw.close();
	
			}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	private int find_lable_rank(Integer[] sorted_indx, int cur_file_lab_ind) {
		int rank =0;
	for(int i=0;i<sorted_indx.length;i++)
		if(sorted_indx[i]==cur_file_lab_ind)
			rank=i;
	return rank;
	}

	private int find_label_ind(Vector<String> cur_file_topic2) {
		int ind=0;
		for (int i=0;i< labelsvector.size();i++){
			if( cur_file_topic2.equals(labelsvector.get(i).getlabelvector() )  ){
				ind=i;
			
			}
		
		
		}
	
		return ind;
	}

	

		public static void main (String args[]){
		
		Rank_based_function resulteval=new Rank_based_function();
	
	}
}
