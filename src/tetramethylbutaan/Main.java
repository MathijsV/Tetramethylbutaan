package tetramethylbutaan;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GabrielGraph gabrielGraph = new TrainingSetReader("trainingssetXOR.txt").getData();
		gabrielGraph.createEdges();
		gabrielGraph.edit(GabrielGraph.EDIT_1ST_ORDER);
		//gabrielGraph.condense();

		new TestSetReader("trainSets/train_set_009_n100_err0.txt", gabrielGraph);
		//new GabrielVisualiser(gabrielGraph);
	}

}
