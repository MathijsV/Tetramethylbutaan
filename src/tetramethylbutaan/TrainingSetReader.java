package tetramethylbutaan;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class TrainingSetReader
{	
	private Graph data;
    private int nrPoints = 0;
    private List<GraphPoint> testSet = new LinkedList<GraphPoint>();
    
    /**
     * Reads a trainingset from a file. Inserts all data into the training set
     * @param fileName name of the file to read
     * @param graph where to insert the data
     */
    public TrainingSetReader(String fileName, Graph graph)
    {
    	this (fileName, graph, 1.0, false);
    }
    
    /**
     * Reads a trainingset from a file, uses a fraction of the data to construct a test set
     * @param fileName name of the file to read
     * @param graph where to insert the data
     * @param trainingFraction the fraction of the amount of data to put into the training set, 
     * the rest will be put into a test set. In the range from 0 to 1, where 1 will put everything
     * into the training set
     * @param sort whether the elements should be randomly added to the training set and test set.
     * Makes the result non-deterministic, but is necessary for sorted training sets
     */
	public TrainingSetReader(String fileName, Graph graph, double trainingFraction, boolean randomDivision)
    {
		data = graph;
		List<GraphPoint> points = new LinkedList<GraphPoint>();
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
            	}
            	
            	int classification = scan.nextInt();
            	
            	points.add(new GraphPoint(features, classification));
            }
            file.close();
            
            if (trainingFraction > 0.0)
            {
            	int trainingSetSize = (int) (trainingFraction * (double)points.size());
	            if (randomDivision)
	            	Collections.shuffle(points);
	            Iterator<GraphPoint> pointIt = points.iterator();
	            for (int i = 0; i < trainingSetSize && pointIt.hasNext(); i++)
	            {
	            	data.add(pointIt.next());
	            }
	            for (int i = trainingSetSize; pointIt.hasNext(); i++)
	            {
	            	testSet.add(pointIt.next());
	            }
            }
        }
        catch (IOException ioe)
        {
            System.out.println("cannot read file " + ioe.getMessage() );
        }
    }
	
	public Graph getData()
	{
		return data;
	}
	
	public List<GraphPoint> getTestSet()
	{
		if (testSet.size() == 0)
			return null;
		else
			return testSet;
	}
}
