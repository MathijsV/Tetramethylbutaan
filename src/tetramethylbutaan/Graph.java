package tetramethylbutaan;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Graph
{
	public final static int EDIT_1ST_ORDER = 1, EDIT_2ND_ORDER = 2, EDIT_DYNAMIC = 3;
    public static int K = 0; //K = 0 := adaptive nearest neighbour
    public final static int NUM_THREADS = 4;
    public static double NOISE_TRESHOLD = 0.5 ;//+ 0.5/Point.classes.length;
    private static int[] totalVotes = null;
	protected List<GraphPoint> points = new ArrayList<GraphPoint>();

    public static void setNoiseTreshold(double f)
    {
        NOISE_TRESHOLD = f + (1.0 - f)/((double)Point.classes.length);
    }
    
	public void add(GraphPoint p)
	{
		points.add(p);
	}
	
	/**
	 * Tests which classification a given point p will get according to this graph.
	 * @param p The point to test.
	 * @return An integer indicating the expected classification.
	 */
	public int test(GraphPoint p, boolean weighted)
	{
		// Start building a list of neighbours of this point
		List<GraphPoint> neighbours = new ArrayList<GraphPoint>();
		
		Iterator<GraphPoint> iter = points.iterator();
		while(iter.hasNext())
		{
			GraphPoint point = iter.next();
			if(p.equals(point)) 
				return point.getClassification();
			if(isNeighbour(p, point))
				neighbours.add(point);
		}
        List<GraphPoint> toTest = new ArrayList<GraphPoint>();
        if (K == 0) // adaptive!
            toTest = neighbours;
        else
        {
            Collections.sort(neighbours, new DistanceComparator(p));
            toTest = neighbours.subList(0, (Math.min(K, neighbours.size())));
        }
        if (weighted)
        {
            return getWeightedClassification(p, toTest);
        }
        else
        {
            return getFirstOrderClassification(toTest);
        }

    }

    private int getWeightedClassification(GraphPoint point, List<GraphPoint> neighbours)
    {
        double votes[] = new double[Point.classes.length];

		// Count the classes of all the neighbours
		Iterator<GraphPoint> neighbourIterator = neighbours.iterator();
		while(neighbourIterator.hasNext())
		{
            GraphPoint neighbour = neighbourIterator.next();
			int classIndex = Point.getClassIndex(neighbour.getClassification());
			votes[classIndex] += 1/(Math.sqrt(neighbour.euclideanDistance2(point)) + 1);
		}

        // Find the class with the most votes
        double maxVotes = 0;
        int maxClass = 0;
        for(int i = 0; i < (Point.classes.length); i++)
        {
        	if(votes[i] > maxVotes)
            {
                maxVotes = votes[i];
                maxClass = Point.classes[i];
            }
        }

        return maxClass;
    }

    public List<GraphPoint> getPoints()
    {
        return points;
    }
    
	public abstract boolean isNeighbour(GraphPoint p1, GraphPoint p2);

	public void createEdges()
	{
        // Count all classes
        totalVotes = new int[Point.classes.length];
        for(Point p : points)
        {
            totalVotes[Point.getClassIndex(p.getClassification())]++;
		}
		
        ExecutorService executor = Executors.newCachedThreadPool();
        for(int i = 0; i < NUM_THREADS; i++)
        {
            EdgeCreator e = new EdgeCreator(this, i, NUM_THREADS);
            executor.execute(e);
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
	}

    private void removeEdges()
    {
        for (GraphPoint p : points)
        {
            p.removeEdges();
        }
    }

    public static boolean isNoise(GraphPoint p)
    {
        List<GraphPoint> neighbours = p.getEdges();
        int nrEqualClass = 0;
        for (GraphPoint neighbour : neighbours)
        {
            int nClass = neighbour.getClassification();
            if (nClass == p.getClassification())
                nrEqualClass++;
        }
        boolean noise = (double) nrEqualClass < NOISE_TRESHOLD * (double)neighbours.size();

        return noise;
    }

	/**
	 * @param neighbours: the neighbours of a point or subgraph
	 * @return the most occuring class in neighbours 
	 */
	public static int getFirstOrderClassification(List<GraphPoint> neighbours)
	{
		int votes[] = new int[Point.classes.length];
		
		// Count the classes of all the neighbours
		Iterator<GraphPoint> neighbourIterator = neighbours.iterator();
		while(neighbourIterator.hasNext())
		{
			int classIndex = Point.getClassIndex(neighbourIterator.next().getClassification());
			votes[classIndex]++;
		}

		// Build a list of possible classes
        List<Integer> possibleClasses = new ArrayList<Integer>();
        int maxVotes = 0;
        
        // Find the class with the most votes
        for(int i = 0; i < (Point.classes.length); i++)
        {
        	if(votes[i] > maxVotes) maxVotes = votes[i];
        }

        // Find the class(es) with the most votes.
        for (int i = 0; i < votes.length; i++)
        {
            if (votes[i] == maxVotes)
                possibleClasses.add(Point.classes[i]);
        }
        
        // If more than one class is possible, use the most common one
        if(possibleClasses.size() == 1)
        	return possibleClasses.get(0);
        else
        {
        	int mostCommonClass = 0, mostCommonClassVotes = 0;
        	for(Integer i : possibleClasses)
        	{
        		if(totalVotes[Point.getClassIndex(i)] > mostCommonClassVotes)
        		{
        			mostCommonClassVotes = totalVotes[Point.getClassIndex(i)];
        			mostCommonClass = i;
        		}
        	}
        	
        	return mostCommonClass;
        }
	}
	
	/**
	 * @param p: the point to be classified
	 * @return the classification of p based on the neighbours of p and the neighbours' neighbours
	 */
	public int getSecondOrderClassification(GraphPoint p)
	{
		List<GraphPoint> neighbourhood = new ArrayList<GraphPoint>();
		for(GraphPoint neighbour: p.getEdges())
		{
			if(neighbour.getClassification() == p.getClassification())
			{
				for(GraphPoint edge: neighbour.getEdges())
				{
					if(edge != neighbour && !neighbourhood.contains(edge))
						neighbourhood.add(edge);
				}
			}
		}
		return getFirstOrderClassification(neighbourhood);
	}
	
	/**
	 * removes graphpoints from a graph which are misclassified by their neighbours
	 * @param editOrder: EDIT_1ST_ORDER or EDIT_2ND_ORDER expected
	 */
	public void edit(int editOrder)
	{
		List<GraphPoint> removePoints = new LinkedList<GraphPoint>();
        if(editOrder != EDIT_1ST_ORDER && editOrder != EDIT_2ND_ORDER && editOrder != EDIT_DYNAMIC)
			return;
		else
		{
			Iterator<GraphPoint> iter = points.iterator();
			while(iter.hasNext())
			{
				GraphPoint p = iter.next();
				if((editOrder == EDIT_1ST_ORDER && getFirstOrderClassification(p.getEdges()) != p.getClassification())
                || (editOrder == EDIT_2ND_ORDER && getSecondOrderClassification(p) != p.getClassification())
                || (editOrder == EDIT_DYNAMIC   && isNoise(p)))
                        removePoints.add(p);
			}
            Iterator<GraphPoint> removeIt = removePoints.iterator();
            Iterator<GraphPoint> allPointsIt = points.iterator();

            while (removeIt.hasNext())
            {
                GraphPoint pointToRemove = removeIt.next();
                GraphPoint lastPoint = allPointsIt.next();

                // Loop through the points until we encounter a point that needs to be removed
                while (lastPoint != pointToRemove)
                {
                    lastPoint = allPointsIt.next();
                }

                // Remove the edges between the point and its neighbours
                List<GraphPoint> neighbours = lastPoint.getEdges();
                Iterator<GraphPoint> neighbourIterator = neighbours.iterator();
    			while(neighbourIterator.hasNext())
    				neighbourIterator.next().removeEdge(lastPoint);

                // Remove the point
                allPointsIt.remove();
            }
            removeEdges();
            createEdges();
		}
	}
	
	/**
	 * Condenses the graph, meaning it removes every point that has the same classification
	 * as all the neighbours of that point.
	 * @return void
	 */
	public void condense()
	{
		Iterator<GraphPoint> iter = points.iterator();
        List<GraphPoint> pointsToRemove = new ArrayList<GraphPoint>();

		while(iter.hasNext())
		{
			GraphPoint p = iter.next();
			if(!hasNeighbourOfOtherClass(p))
			{
                pointsToRemove.add(p);
			}
		}

        Iterator<GraphPoint> allPointsIt = points.iterator();
        Iterator<GraphPoint> removeIt = pointsToRemove.iterator();
        while (removeIt.hasNext())
        {
            GraphPoint pointToRemove = removeIt.next();
            GraphPoint lastPoint = allPointsIt.next();

            // Loop through the points until we encounter a point that needs to be removed
            while (lastPoint != pointToRemove)
            {
                lastPoint = allPointsIt.next();
            }

            // Remove the edges between the point and its neighbours
            List<GraphPoint> neighbours = lastPoint.getEdges();
            Iterator<GraphPoint> neighbourIterator = neighbours.iterator();
			while(neighbourIterator.hasNext())
				neighbourIterator.next().removeEdge(lastPoint);

            // Remove the point
            allPointsIt.remove();
        }

        removeEdges();
        createEdges();
	}
	
	/**
	 * Tests if the given point has a neighbour with a different class.
	 * @param p The point whose neighbours will be checked.
	 * @return True iff for any neighbour of p, class(p) != class(neighbour)
	 */
	private boolean hasNeighbourOfOtherClass(GraphPoint p)
	{
		Iterator<GraphPoint> neighbours = p.getEdges().iterator();
		while(neighbours.hasNext())
			if(p.getClassification() != neighbours.next().getClassification())
				return true;
		return false;
	}
}
