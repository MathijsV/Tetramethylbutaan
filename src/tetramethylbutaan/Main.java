package tetramethylbutaan;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GabrielGraph gabrielGraph = new TrainingSetReader("trainingssetXOR.txt").getData();
		gabrielGraph.createEdges();
		gabrielGraph.edit(GabrielGraph.EDIT_2ND_ORDER);

		new GabrielVisualiser(gabrielGraph);
	}

}
