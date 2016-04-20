import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

public class Evaluation {
	public Vector<String> label;
	public Vector<String> prediction;
	public double Bscore, Cscore, Dscore, Wscore, Escore, Wscore_wg, Cscore_wg;
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
	{
	    W2VUtil util = new W2VUtil("GoogleNews-vectors-negative300.bin", W2VUtil.ANGULAR_DIST);
		String dir = "results";
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();
		int n = listOfFiles.length;
		System.err.println(n);
		for (File file: listOfFiles)
		{
			if (file.getName().startsWith(".")) continue;
			BufferedReader reader = new BufferedReader(new FileReader(file));
			reader.readLine(); //read and discard item id
			
			//read category into vector
			String catStr = reader.readLine();
			catStr = catStr.replaceAll("&", "");
			StringTokenizer tk = new StringTokenizer(catStr, " ");
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
					Wscore += util.distance(this.label, this.prediction);
				}

				if (tmp.contains("MST closeness"))
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
					Cscore += util.distance(this.label, this.prediction);
				}

				if (tmp.contains("MST exhaustive"))
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
					Escore += util.distance(this.label, this.prediction);
				}

				if (tmp.contains("MST betwennness"))
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
					Bscore += util.distance(this.label, this.prediction);
				}
				if (tmp.startsWith(" weighted") && tmp.length() <= 10)
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
					Wscore_wg += util.distance(this.label, this.prediction);
				}
				if (tmp.startsWith(" closeness") && tmp.length() <= 10)
				{
					predStr = reader.readLine();
					StringTokenizer tk2 = new StringTokenizer(predStr, ", ");
					while (tk2.hasMoreTokens())
					{
						this.prediction.add(tk2.nextToken());
					}
					Wscore_wg += util.distance(this.label, this.prediction);
				}
				tmp = reader.readLine();
			}
			reader.close();
			System.out.printf("%s: MST closeness = %f\n MST weighted = %f\n MST betweenness = %f\n MST Exhaustive = %f\n WG closeness = %f\n WG Weighted = %f\n",
					file.getName(), Cscore, Wscore, Bscore, Escore, Cscore_wg, Wscore_wg);
			//read prediction into vector
		}
		System.err.printf("MST closeness = %f\n MST weighted = %f\n MST betweenness = %f\n MST Exhaustive = %f\n WG closeness = %f\n WG Weighted = %f\n",
				Cscore/n, Wscore/n, Bscore/n, Escore/n, Cscore_wg/n, Wscore_wg/n);	}

}
