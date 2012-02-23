package tetramethylbutaan;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class TestSetReader
{	
	public TestSetReader(String fileName, GabrielGraph g)
    {
        try
        {
        	int numWrong = 0;
            FileReader file = new FileReader(fileName);
            Scanner scan = new Scanner(file);
            int num = 0;
            for(; scan.hasNext(); num++)
            {
            	double[] features = new double[GraphPoint.nrDimensions];
            	for (int i = 0; i < Point.nrDimensions; i += 1)
            	{
            		String attribute = scan.next();
                	features[i] = Double.parseDouble(attribute);
                	//System.out.println(features[i]);
            	}
            	int classification = scan.nextInt();
            	//System.out.println(classification);
            	if(classification != g.test(new GraphPoint(features)))
            	{
            		numWrong++;
            		System.out.println("Wrong prediction for " + num);
            	}
            }
            file.close();
            System.out.println(numWrong + " wrongly predicted (" + (((double) numWrong / num) * 100.0) + "%)");
        }
        catch (IOException ioe)
        {
            System.out.println("cannot read file " + ioe.getMessage() );
        }
    }
}
