package tetramethylbutaan;

public class Point
{
	public static int nrDimensions = 20;
	public static int nrClasses = 4;
	protected double[] features;
	protected int classification = 0;
	//public static int numEuclCalled = 0;
	//public static long euclTime;
	
	public Point()
	{
		features = new double[nrDimensions];
		for(int i = 0; i< features.length; i++)
			features[i] = 0;
	}
	
	public Point(double[] features)
	{
		this.features = features;
	}
	
	public Point(double[] features, int c)
	{
		this(features);
		setClassification(c);
	}

    public double[] getFeatures()
    {
        return features;
    }
	
	public double euclideanDistance2(Point p)
	{
		//numEuclCalled++;
		//long start = System.currentTimeMillis();
		double distance = 0;
		for (int i = 0; i < nrDimensions; i++)
		{
			double featureDistance2 = features[i] - p.features[i];
			featureDistance2 *= featureDistance2;
			distance += featureDistance2;
		}
		//euclTime += System.currentTimeMillis()-start;
		return distance;
	}
	
	public void setClassification(int c)
	{
		classification = c;
	}
	
	public boolean equals(Point p)
	{
		for (int i = 0; i < nrDimensions; i++)
		{
			if (features[i] != p.features[i])
				return false;
		}
		return true;
	}
	
	public int getClassification()
	{
		return classification;
	}
}
