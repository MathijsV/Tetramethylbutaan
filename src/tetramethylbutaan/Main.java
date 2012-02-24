package tetramethylbutaan;

import java.io.IOException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*RelatedNeighbourHoodGraph gabrielGraph = new TrainingSetReader("trainSets/train_set_001_n1000_err10.txt").getData();
		gabrielGraph.createEdges();
		gabrielGraph.edit(Graph.EDIT_2ND_ORDER);
		gabrielGraph.condense();*/

		//new TestSetReader("trainSets/train_set_001_n1000_err0.txt", gabrielGraph);*/
		//new GabrielVisualiser(gabrielGraph);
		try {
			new AutoTester(100);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
