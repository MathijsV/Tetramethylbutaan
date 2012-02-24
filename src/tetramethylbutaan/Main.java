package tetramethylbutaan;

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
		Iterator<Integer> result = new TestSetReader("mnist_train.txt", gabrielGraph).getResults().iterator();
		/*while(result.hasNext())
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
