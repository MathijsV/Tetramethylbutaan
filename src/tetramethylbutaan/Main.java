package tetramethylbutaan;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Mathijs Vos (4024443), Ramon Janssen (0711667), Petra van den Bos (4064453)
 *
 */
public class Main
{
	public static void main(String[] args)
	{
		new Main();
	}
	
	public Main()
	{
		//customTest();
		//autoTest();
        String[] types = {"xor","diabetes","nieuws"};
        Graph.K = 1;
        massTrainingSetTest(types, 0.0, 1, 0.1, Graph.EDIT_1ST_ORDER, 0.8, false);
	}

	private void setDimensions(String dataType)
	{
		if (dataType.equals("xor"))
        {
			Point.nrDimensions = 2;
            Point.setClasses(new int[]{-1,1});
        }
		else if (dataType.equals("diabetes"))
        {
			Point.nrDimensions = 8;
            Point.setClasses(new int[]{0,1});
        }
		else if (dataType.equals("mnist"))
        {
            int[] classes = new int[10];
            for (int i = 0; i < 10; i++)
            {
                classes[i]=i;
            }
			Point.nrDimensions = 196;
            Point.setClasses(classes);
        }
        else if (dataType.equals("nieuws"))
        {
			Point.nrDimensions = 20;
            Point.setClasses(new int[]{1,2,3});
        }
        else if (dataType.equals("karakter"))
        {
            int[] classes = new int[400];
            for (int i = 1; i <= 400; i++)
            {
                classes[i-1]=i;
            }
			Point.nrDimensions = 400;
            Point.setClasses(classes);
        }
	}

    private void massTrainingSetTest(String[] types, double minTresh, double maxTresh, double deltaTresh, int editOrder, double fraction, boolean weighted)
    {
        for (String type : types)
        {
            DecimalFormat f = new DecimalFormat("#.##");
            System.out.println("\n\n" + type + ":");
            setDimensions(type);
            if(editOrder == Graph.EDIT_DYNAMIC)
            {
	            for (double i = minTresh; i <= maxTresh; i += deltaTresh)
	            {
	                Graph.setNoiseTreshold(i);
	                System.out.println("" + f.format(Graph.NOISE_TRESHOLD) + "");
	                trainingSetTest(type + "_train.txt", new GabrielGraph(), editOrder, 0.8, weighted);
	            }
            }
            else
            {
            	trainingSetTest(type + "_train.txt", new GabrielGraph(), editOrder, 0.8, weighted);
            }
        }
    }

	/**
	 * tests on a given training set and prints the results
	 * @param filename the training set
	 * @param graph the graph to test on, empty graph expected
	 * @param fraction how much data should be used to construct the training set, between 0 and 1.
	 * the rest of the data will be used to test.
	 */
	private void trainingSetTest(String filename, Graph graph, int order, double fraction, boolean weighted)
	{
		TrainingSetReader reader = new TrainingSetReader(filename, graph, fraction, false);
		List<GraphPoint> testSet = reader.getTestSet();


		double sizeBefore = (double) graph.getPoints().size();
		constructGraph(graph, order, false);
		double reduction = (double) graph.getPoints().size() / sizeBefore;
		
		int nrTests = testSet.size();
		int nrCorrect = 0;
		for (GraphPoint point : testSet)
		{
			int classification = point.getClassification();
			if (classification == graph.test(point, weighted))
				nrCorrect++;
		}
        
        DecimalFormat f = new DecimalFormat("#.##");

		System.out.println(f.format(100.0 * (double)nrCorrect/(double)nrTests) + "");
		System.out.println(f.format(100.0 * (1 - reduction)) + "\n");
	}
	
	private void constructGraph(Graph graph, int order)
	{
		constructGraph(graph, order, true);
	}
	
	private void constructGraph(Graph graph, int order, boolean printStatus)
	{
		if (printStatus)
			System.out.println("Creating edges...");
		graph.createEdges();
		if (printStatus)
			System.out.println("Editing graph...");
		graph.edit(order);
		if (printStatus)
			System.out.println("Condensing graph...");
		graph.condense();
	}
	
	private void customTest()
	{
		String fileType = "karakter";
        setDimensions(fileType);
		
		Graph graph = new TrainingSetReader(fileType + "_train.txt", new RelatedNeighbourHoodGraph()).getData();
		constructGraph(graph, Graph.EDIT_1ST_ORDER);
		
		String testFile = fileType + "_test.txt";
		System.out.println("Collecting results...");
        new TestSetReader(testFile, graph, "testResults/nieuw/classes_RNG.txt");
        try
        {
            // Create file
            FileWriter fstream = new FileWriter("testResults/nieuw/prototypes_RNG.txt");
            BufferedWriter out = new BufferedWriter(fstream);

            for(GraphPoint p : graph.getPoints())
            {
                double[] features = p.getFeatures();
                for(int i = 0; i < features.length; i++)
                {
                    out.write(features[i] + " ");
                }

                out.write(p.getClassification() + "\n");
            }

            out.close();
        }
        catch (Exception ex)
        {
            System.err.println("Error: " + ex.getMessage());
        }
		//new GabrielVisualiser(graph);
	}
	
	private void autoTest()
	{
		new AutoTester();
	}
}
