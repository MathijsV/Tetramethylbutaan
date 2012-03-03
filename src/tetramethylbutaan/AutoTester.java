package tetramethylbutaan;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class AutoTester
{
	public AutoTester(int n) throws IOException
	{
		System.out.println("Starting tests with n = " + n);
		int total[] = new int[5];
		int wrong[] = new int[5];

		for(int i = 1; i <= 10; i++)
		{			
			for(int err = 0; err <= 40; err += 10)
			{
				int numTotal = 0, numWrongTotal = 0;
				
				//System.out.println("Testing with training set " + i + ", error rate " + err);
				//System.out.println("Creating graph...");
				GabrielGraph gabrielGraph = (GabrielGraph) new TrainingSetReader("trainSets/train_set_" + String.format("%03d", i) + "_n" + n + "_err" + err + ".txt").getData();
				gabrielGraph.createEdges();
				//System.out.println("Editing graph...");
				gabrielGraph.edit(GabrielGraph.EDIT_2ND_ORDER);
				//System.out.println("Condensing graph...");
				gabrielGraph.condense();
				//System.out.println("Graph ready. Starting tests...");
				
				for(int j = 1; j <= 40; j++)
				{
					//System.out.println("Testing set " + j);
					//System.out.print(j + " ");
					Iterator<Integer> results = new TestSetReader("testSets/test_data_" + String.format("%03d", j) + "_n" + n + ".txt", gabrielGraph, "testResults/classes.txt")
													.getResults().iterator();
					
					FileReader file = new FileReader("testSets/test_label_" + String.format("%03d", j) + "_n" + n + ".txt");
		            Scanner scan = new Scanner(file);
		            int num = 0, numWrong = 0;
		            for(; scan.hasNext() && results.hasNext(); num++)
		            {
		            	numTotal++;
		            	total[err/10]++;
		            	String classification = scan.next();
		            	if(Double.parseDouble(classification) != results.next())
		            	{
		            		numWrong++;
		            		numWrongTotal++;
		            		wrong[err/10]++;
		            	}
		            }
		            file.close();
		            //System.out.println("Test complete. Items tested: " + num + ", wrong predictions: " + numWrong + " (" + ((double) numWrong / num) * 100 + "%)");
				}
			}
		}
		
		for(int i = 0; i < 5; i++)
		{
			String percentage = String.valueOf(100-(((double) wrong[i] / total[i]) * 100));
			System.out.println(percentage.replace('.', ','));
		}
	}
}
