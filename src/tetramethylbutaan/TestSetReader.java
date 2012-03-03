package tetramethylbutaan;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TestSetReader
{
    private List<GraphPoint> pointsToTest = new ArrayList<GraphPoint>();
	private List<Integer> results = new ArrayList<Integer>();
    private final static int NUM_THREADS = 4;
	
	public TestSetReader(String fileName, Graph g)
    {
        int num = 0;
        try
        {
            FileReader file = new FileReader(fileName);
            Scanner scan = new Scanner(file);
            for(; scan.hasNext(); num++)
            {
            	//if(num > 800)
            	{
	            	double[] features = new double[GraphPoint.nrDimensions];
	            	for (int i = 0; i < Point.nrDimensions; i += 1)
	            	{
	            		String attribute = scan.next();
	                	features[i] = Double.parseDouble(attribute);// - 0.000001;
	                	//System.out.println(features[i]);
	            	}
	            	//int classification = scan.nextInt();
	                pointsToTest.add(new GraphPoint(features));
	            	//expectedResults.add(classification);
            	}
            }
            file.close();
        }
        catch (IOException ioe)
        {
            System.out.println("cannot read file " + ioe.getMessage());
        }

        ExecutorService executor = Executors.newCachedThreadPool();
        List<PointTester> testers = new ArrayList<PointTester>();

        for(int i = 0 ; i < NUM_THREADS; i++)
        {
            PointTester pt = new PointTester((GabrielGraph) g, pointsToTest.subList(i*(pointsToTest.size() / NUM_THREADS), (i+1)*(pointsToTest.size() / NUM_THREADS)));
            testers.add(pt);
            executor.execute(pt);
        }

        try
        {
        	executor.shutdown();
            executor.awaitTermination(10, TimeUnit.HOURS);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("Klaar");

        //Iterator<Integer> e = expectedResults.iterator();
        //int numWrong = 0;

        BufferedWriter out = null;
        
        try
        {
            // Create file
            FileWriter fstream = new FileWriter("testResults/classes2ndorder.txt");
            out = new BufferedWriter(fstream);
        }
        catch (Exception ex)
        {
            System.err.println("Error: " + ex.getMessage());
        }
    

        for(PointTester t : testers)
        {
            Iterator<Integer> i1 = t.getResults().iterator();
            while(i1.hasNext()/* && e.hasNext()*/)
            {
                try
                {
                    int classification = i1.next();
                    results.add(classification);
                    out.write(classification + "\n");
                    /*if (classification != e.next())
                        numWrong++;*/
                }
                catch (IOException ex)
                {
                    Logger.getLogger(TestSetReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        try
        {
            out.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(TestSetReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println(((double) numWrong / num)*100 + "%");
    }
	
	public List<Integer> getResults()
	{
		return results;
	}
}
