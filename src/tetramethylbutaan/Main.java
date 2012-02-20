package tetramethylbutaan;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GabrielGraph gg = new TrainingSetReader("trainingssetXOR.txt").getData();
		gg.createEdges();
		gg.edit(GabrielGraph.EDIT_2ND_ORDER);

	}

}
