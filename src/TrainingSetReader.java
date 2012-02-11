import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TrainingSetReader
{	
	private GGgraph data = new GGgraph();
    private int nrPoints = 0;

	public TrainingSetReader(String fileNaam)
    {
        try
        {
            FileReader file = new FileReader(fileNaam);
            Scanner scan = new Scanner(file);
            for ( ; scan.hasNext(); nrPoints += 1)
            {
            	double[] features = new double[GraphPoint.nrDimensions];
            	for (int i = 0; i < Point.nrDimensions; i += 1)
            	{
            		features[i] = scan.nextDouble();
            	}
            	int classification = scan.nextInt();
            	data.add(new GraphPoint(features, classification));
            }
            file.close();
        }
        catch (IOException ioe)
        {
            System.out.println("cannot read file " + ioe.getMessage() );
        }
    }
	
	public GGgraph getData()
	{
		return data;
	}
}
