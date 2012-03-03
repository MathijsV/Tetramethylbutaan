package tetramethylbutaan;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GabrielGraph gabrielGraph = (GabrielGraph) new TrainingSetReader("mnist_train.txt").getData();
		System.out.println("Creating edges...");
		gabrielGraph.createEdges();
		System.out.println("Editing graph...");
		gabrielGraph.edit(Graph.EDIT_2ND_ORDER);
		System.out.println("Condensing graph...");
		gabrielGraph.condense();
		
		System.out.println("Collecting results...");
        new TestSetReader("mnist_test.txt", gabrielGraph, "testResults/classes_k1_2ndorderedit_2ndordertest.txt");
        gabrielGraph.K = 3;
        new TestSetReader("mnist_test.txt", gabrielGraph, "testResults/classes_k3_2ndorderedit_2ndordertest.txt");
        gabrielGraph.K = 5;
        new TestSetReader("mnist_test.txt", gabrielGraph, "testResults/classes_k5_2ndorderedit_2ndordertest.txt");
        try
        {
            // Create file
            FileWriter fstream = new FileWriter("testResults/prototypes_2ndorderedit.txt");
            BufferedWriter out = new BufferedWriter(fstream);

            for(GraphPoint p : gabrielGraph.getPoints())
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


		/*Iterator<Integer> result = new TestSetReader("mnist_train.txt", gabrielGraph).getResults().iterator();
		while(result.hasNext())
			System.out.println(result.next());*/
		//new TestSetReader("trainSets/train_set_001_n1000_err0.txt", gabrielGraph);*/
		//new GabrielVisualiser(gabrielGraph);
		/*try {
			new AutoTester(100);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
