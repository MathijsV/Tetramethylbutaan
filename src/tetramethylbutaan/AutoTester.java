package tetramethylbutaan;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class AutoTester
{
	public AutoTester()
	{
		int n = 100;
		System.out.println("n = " + n + ", 1st order");
		test(n, Graph.EDIT_1ST_ORDER);
		System.out.println("n = " + n + ", 2nd order");
		test(n, Graph.EDIT_2ND_ORDER);
	}
	
	public AutoTester(int n)
	{
		test(n, Graph.EDIT_2ND_ORDER);
	}
	
	public void test(int n, int order)
	{
		int total[] = new int[5];
		int wrong[] = new int[5];

		for(int i = 1; i <= 10; i++)
		{			
			for(int err = 0; err <= 40; err += 10)
			{
				int numTotal = 0, numWrongTotal = 0;
				Graph graph = new TrainingSetReader("trainSets/train_set_" + String.format("%03d", i) +
														"_n" + n + "_err" + err + ".txt", new RelatedNeighbourHoodGraph()).getData();
				graph.createEdges();
				graph.edit(order);
				graph.condense();
				
				for(int j = 1; j <= 40; j++)
				{
					Iterator<Integer> results = new TestSetReader("testSets/test_data_" + String.format("%03d", j)
																	+ "_n" + n + ".txt", graph, "testResults/classes.txt")
													.getResults().iterator();
					
					FileReader file = null;
					try
					{
						file = new FileReader("testSets/test_label_" + String.format("%03d", j) + "_n" + n + ".txt");
					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}
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
		            try
		            {
						file.close();
					}
		            catch (IOException e)
		            {
						e.printStackTrace();
					}
				}
			}
		}
		
		for(int i = 0; i < 5; i++)
		{
			System.out.print("error " + (i*10) + "%: ");
			String percentage = String.valueOf(100-(((double) wrong[i] / total[i]) * 100));
			System.out.println(percentage.replace('.', ','));
		}
	}
}
