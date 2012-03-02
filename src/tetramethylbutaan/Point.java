package tetramethylbutaan;

public class Point
{
	public static int nrDimensions = 2;
	public static int nrClasses = 2;
	protected double[] features;
	protected int classification = 0;
	
	public Point()
	{
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
	
	public double euclidianDistance2(Point p)
	{
		double distance = 0;
		for (int i = 0; i < nrDimensions; i++)
		{
			double featureDistance2 = features[i] - p.features[i];
			featureDistance2 *= featureDistance2;
			distance += featureDistance2;
		}
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
