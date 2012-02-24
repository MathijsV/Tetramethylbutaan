package tetramethylbutaan;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TestSetReader
{	
	private List<Integer> results = new ArrayList<Integer>();
	
	public TestSetReader(String fileName, Graph g)
    {
        try
        {
            FileReader file = new FileReader(fileName);
            Scanner scan = new Scanner(file);
            int num = 0, numWrong = 0;
            for(; scan.hasNext(); num++)
            {
            	double[] features = new double[GraphPoint.nrDimensions];
            	for (int i = 0; i < Point.nrDimensions; i += 1)
            	{
            		String attribute = scan.next();
                	features[i] = Double.parseDouble(attribute);// - 0.000001;
                	//System.out.println(features[i]);
            	}
            	int classification = scan.nextInt();
            	System.out.println(num);
            	int testRes = g.test(new GraphPoint(features));
            	if(testRes != classification)
            	{
            		numWrong++;
            	}
            	//results.add(g.test(new GraphPoint(features)));
            }
            file.close();
            System.out.println(((double) numWrong / num) * 100);
        }
        catch (IOException ioe)
        {
            System.out.println("cannot read file " + ioe.getMessage() );
        }
    }
	
	public List<Integer> getResults()
	{
		return results;
	}
}
