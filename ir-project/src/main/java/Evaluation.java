/*
 * flat and simple Evaluation 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import org.junit.experimental.categories.Categories;

public class Evaluation {
	public Vector<String> label;
	public Vector<String> prediction;
	public double Bscore, Cscore, Dscore, Wscore, Escore, Wscore_wg, Cscore_wg,Mscore;
	public Evaluation()
	{
		this.label = new Vector<String>();
		this.prediction = new Vector<String>();
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
	{  W2VUtil.DEBUG_MODE=false;
	    W2VUtil util = new W2VUtil("GoogleNews-vectors-negative300.bin", W2VUtil.ANGULAR_DIST);
		String dir = "results";
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();
		int n = listOfFiles.length;
		System.err.println(n);
		int count = 0;
		for (File file: listOfFiles)
		{
			if (file.getName().startsWith(".")) continue;
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			reader.readLine(); //read and discard item id
			
			//read category into vector
			String catStr = reader.readLine();
			catStr = catStr.replaceAll("&", "");
			if (catStr.isEmpty())
				continue;
			StringTokenizer tk = new StringTokenizer(catStr, " ");
			this.label.clear();
			while (tk.hasMoreTokens())
			{
				this.label.add(tk.nextToken());
			}
			
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
					Wscore += util.distance(this.label, this.prediction,false);
				}
				
				if (tmp.contains("Mallet"))
				{
					System.out.println("     omadim   to mallet    ");
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ",");
					tk2.nextToken(); //ignoring topic number
					double prob_dist=Double.parseDouble(tk2.nextToken());
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
					
					Mscore +=  util.distance(this.label, this.prediction,false) ;
				}

				if (tmp.contains("MST closeness"))
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
					Cscore += util.distance(this.label, this.prediction,false);
				}

				if (tmp.contains("MST exhaustive"))
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
					Escore += util.distance(this.label, this.prediction,false);
				}

				if (tmp.contains("MST betwennness"))
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
					Bscore += util.distance(this.label, this.prediction,false);
				}
				if (tmp.startsWith(" weighted") && tmp.length() <= 10)
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
					Wscore_wg += util.distance(this.label, this.prediction,false);
				}
				if (tmp.startsWith(" closeness") && tmp.length() <= 10)
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
					Cscore_wg += util.distance(this.label, this.prediction,false);
				}
				if (predStr != null && predStr.isEmpty())
					return;
				tmp = reader.readLine();
			}
			reader.close();
			Formatter expriment= new Formatter(new StringBuilder(), Locale.US);
					
				expriment.format(" %s , %s : \n MST closeness = %f \n MST weighted = %f\n MST betweenness = %f\n MST"
					+ " Exhaustive = %f\n WG closeness = %f\n WG Weighted = %f\n Mallet Score= %f \n","error rate of flat experiments on a random  selected file      ",file.getName(), 
					 Cscore,Wscore, Bscore, Escore, Cscore_wg, Wscore_wg,Mscore);
					
			System.out.printf(expriment.toString());
			write_on_file(expriment.toString());	
			//read prediction into vector
			count ++;
		}
		Formatter out=new Formatter(new StringBuilder(), Locale.US);
		System.err.printf("MST closeness = %f\n MST weighted = %f\n MST betweenness = %f\n MST Exhaustive = %f\n WG closeness = %f\n WG Weighted = %f\n Mallet =%f\n",
				Cscore/n, Wscore/n, Bscore/n, Escore/n, Cscore_wg/n, Wscore_wg/n,Mscore/n);	
		out.format("%s \n MST closeness = %f\n MST weighted = %f\n MST betweenness = %f\n"
				+ " MST Exhaustive = %f\n WG closeness = %f\n WG Weighted = %f\n Mallet =%f\n ", 
				" avg of error rate of flat experiments on 50 random  selected files ",Cscore/n, 
				Wscore/n, Bscore/n, Escore/n, Cscore_wg/n, Wscore_wg/n,Mscore/n);
		write_on_file(out.toString());
		}

	private void write_on_file(String Expriment){
		FileWriter fw;
		String path="Experiments/"+"flat_experiments.txt";
		try {
			fw = new FileWriter(path,true);
			fw.write(Expriment+"\n");
			fw.close();
	
			}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main (String args[]){
		Evaluation resulteval=new Evaluation();
		try {
			resulteval.evaluate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
