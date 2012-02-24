package tetramethylbutaan;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class TrainingSetReader
{	
	private /*GabrielGraph*/ RelatedNeighbourHoodGraph data = new RelatedNeighbourHoodGraph();// new GabrielGraph();
    private int nrPoints = 0;

	public TrainingSetReader(String fileName)
    {
        try
        {
            FileReader file = new FileReader(fileName);
            Scanner scan = new Scanner(file);
            for ( ; scan.hasNext(); nrPoints += 1)
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
            	data.add(new GraphPoint(features, classification));
            }
            file.close();
        }
        catch (IOException ioe)
        {
            System.out.println("cannot read file " + ioe.getMessage() );
        }
    }
	
	public /*GabrielGraph*/ RelatedNeighbourHoodGraph getData()
	{
		return data;
	}
}
