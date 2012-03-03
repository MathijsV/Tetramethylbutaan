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
	public final static int EDIT_1ST_ORDER = 1, EDIT_2ND_ORDER = 2;
    public final static int K = 3;
    public final static int NUM_THREADS = 4;
    
    private static int[] totalVotes = null;
	
	protected List<GraphPoint> points = new ArrayList<GraphPoint>();
    
	public void add(GraphPoint p)
	{
		points.add(p);
	}
	
	public void makeGraph(List<GraphPoint> ps)
    {
        for(GraphPoint p: ps)
        {
            addPointToGraph(p, p.getClassification());
        }
    }
	
	public void addPointToGraph(GraphPoint p, int classification)
	{
		List<GraphPoint> neighbours = new ArrayList<GraphPoint>();
		/*for(GraphPoint gp: points)
		{
			if(isNeighbour(p, gp))
			{
				neighbours.add(gp);
			}
		}*/
		for(GraphPoint gp: points)
        {
            if(isNeighbour(p, gp))
            {
                neighbours.add(gp);
                neighbours.addAll(gp.getEdges());
                break;
            }
        }
		for(GraphPoint nb1: neighbours)
		{
			for(GraphPoint nb2: neighbours)
			{
				if(nb1.hasEdgeWith(nb2))
				{
					nb1.removeEdge(nb2);
					nb2.removeEdge(nb1);
				}
			}
		}
		p.setClassification(classification);
		points.add(p);
		neighbours.add(p);
		recalculateEdges(neighbours);
	}
	
	/**
	 * Tests which classification a given point p will get according to this graph.
	 * @param p The point to test.
	 * @return An integer indicating the expected classification.
	 */
	public int test(GraphPoint p)
	{
		// Start building a list of neighbours of this point
		List<GraphPoint> neighbours = new ArrayList<GraphPoint>();
		
		Iterator<GraphPoint> iter = points.iterator();
		while(iter.hasNext())
		{
			GraphPoint point = iter.next();
			if(p.equals(point)) return point.getClassification();
			if(isNeighbour(p, point))
            {
				if(!neighbours.contains(point))
					neighbours.add(point);

                for(GraphPoint neighbour : point.getEdges())
                {
                    if(neighbour != p && !neighbours.contains(neighbour))
                    {
                        neighbours.add(neighbour);
                    }
                }
            }
		}

        Collections.sort(neighbours, new DistanceComparator(p));
		return getFirstOrderClassification(neighbours.subList(0, (Math.min(K, neighbours.size()))));
	}
	
    public List<GraphPoint> getPoints()
    {
        return points;
    }
    
	public abstract boolean isNeighbour(GraphPoint p1, GraphPoint p2);

	public void createEdges()
	{
		if(totalVotes == null)
		{
			// Count all classes
			totalVotes = new int[Point.nrClasses+1];
			for(Point p : points)
			{
				totalVotes[p.getClassification()+1]++;
			}
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

	/**
	 * @param neighbours: the neighbours of a point or subgraph
	 * @return the most occuring class in neighbours 
	 */
	public static int getFirstOrderClassification(List<GraphPoint> neighbours)
	{
		int votes[] = new int[Point.nrClasses+1];
		
		// Count the classes of all the neighbours
		Iterator<GraphPoint> neighbourIterator = neighbours.iterator();
		while(neighbourIterator.hasNext())
		{
			int classnr = neighbourIterator.next().getClassification();
			votes[classnr+1] = votes[classnr+1] + 1;
		}

		// Build a list of possible classes
        List<Integer> possibleClasses = new ArrayList<Integer>();
        int maxVotes = 0, counter = 0;
        
        // Find the class with the most votes
        for(int i = 0; i < (Point.nrClasses + 1); i++)
        {
        	if(votes[i] > maxVotes) maxVotes = votes[i];
        }

        // Find the class(es) with the most votes.
        for(Integer i : votes)
        {
            if(i == maxVotes)
                possibleClasses.add(counter-1);
            counter++;
        }
        
        // If more than one class is possible, use the most common one
        if(possibleClasses.size() == 1)
        	return possibleClasses.get(0);
        else
        {
        	int mostCommonClass = 0, mostCommonClassVotes = 0;
        	for(Integer i : possibleClasses)
        	{
        		if(totalVotes[i+1] > mostCommonClassVotes)
        		{
        			mostCommonClassVotes = totalVotes[i+1];
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
					if(!neighbourhood.contains(edge) && !p.getEdges().contains(edge))
						neighbourhood.add(edge);
				}
			}
		}
		return getFirstOrderClassification(neighbourhood);
	}
	
	/**
	 * calculates the (new) edges between the neighbours
	 * @param neighbours: the neighbours of already a removed point
	 */
	protected abstract void recalculateEdges(List<GraphPoint> neighbours);
	
	/**
	 * removes graphpoints from a graph which are misclassified by their neighbours
	 * @param editOrder: EDIT_1ST_ORDER or EDIT_2ND_ORDER expected
	 */
	public void edit(int editOrder)
	{
		List<GraphPoint> removePoints = new LinkedList<GraphPoint>();
        if(editOrder != EDIT_1ST_ORDER && editOrder != EDIT_2ND_ORDER)
			return;
		else
		{
			Iterator<GraphPoint> iter = points.iterator();
			while(iter.hasNext())
			{
				GraphPoint p = iter.next();
				if(getFirstOrderClassification(p.getEdges()) != p.getClassification())
				{
					if(editOrder == EDIT_1ST_ORDER || getSecondOrderClassification(p) != p.getClassification())
					{
						List<GraphPoint> edges = p.getEdges();
						Iterator<GraphPoint> edgeIterator = edges.iterator();
						while(edgeIterator.hasNext())
							edgeIterator.next().removeEdge(p);
                        removePoints.add(p);
					}
				}
				
			}

            //System.out.println("Editing: removing " + removePoints.size() + " points");
           // points.removeAll(removePoints);
            Iterator<GraphPoint> removeIt = removePoints.iterator();
            Iterator<GraphPoint> allPointsIt = points.iterator();
            

            int outputCounter = 0;    // for testing only
            int showOutputCounter = 1;// for testing only

            while (removeIt.hasNext())
            {
                Point pointToRemove = removeIt.next();
                while (allPointsIt.next() != pointToRemove);
                allPointsIt.remove();

                //for testing only
                outputCounter++;
                if (outputCounter == showOutputCounter)
                {
                    //System.out.println (outputCounter + " point removed");
                    showOutputCounter*=2;
                    showOutputCounter = Math.min(outputCounter, 50);
                }
            }
            /*
            GraphPoint pointToRemove;
            if(removeIt.hasNext())
            {
                while(allPointsIt.hasNext())
                {
                    GraphPoint p = allPointsIt.next();
                    if(p == pointToRemove)
                    {
                        allPointsIt.remove();
                        if(removeIt.hasNext())
                        {
                            pointToRemove = removeIt.next();
                        }
                        else
                            break;
                    }
                }
             }
             */
            
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
				// This graphpoint has the same type as all its neighbours, so we can safely ignore it.
				/*List<GraphPoint> neighbours = p.getEdges();
				Iterator<GraphPoint> neighbourIterator = neighbours.iterator();
				while(neighbourIterator.hasNext())
					neighbourIterator.next().removeEdge(p);
				iter.remove();
				recalculateEdges(neighbours);*/
			}
		}

        Iterator<GraphPoint> allPointsIt = points.iterator();
        Iterator<GraphPoint> removeIt = pointsToRemove.iterator();
        //System.out.println("Condensing: removing " + pointsToRemove.size() + " points");
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
