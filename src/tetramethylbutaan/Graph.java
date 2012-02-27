package tetramethylbutaan;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class Graph
{
	public final static int EDIT_1ST_ORDER = 1, EDIT_2ND_ORDER = 2;
    public static int K = 1;
	
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
				/*if(!neighbours.contains(point)) */
                neighbours.add(point);

                /*for(GraphPoint neighbour : point.getEdges())
                {
                    if(neighbour != p && !neighbours.contains(neighbour))
                    {
                        neighbours.add(neighbour);
                    }
                }*/
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
	
	// TODO; de helft van de combinaties wegknippen -> Done
	// TODO: is dat eigenlijk wel handig? Elke point moet namelijk weten
	// welke edges hij heeft, nu weet maar ��n van de twee points het
	//Ja, is wel handig: addEdge voegt bij beide punten het andere punt toe als edge
	public void createEdges()
	{
		for (int i = 0; i < points.size(); i++)
		{
			for (int j = i+1; j < points.size(); j++)
			{
				GraphPoint p1 = points.get(i), p2 = points.get(j);
				if (isNeighbour(p1, p2) && !p1.hasEdgeWith(p2))
					p1.addEdge(p2);
			}
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
	public int getFirstOrderClassification(List<GraphPoint> neighbours)
	{
		List<Integer> votes = new ArrayList<Integer>();
		
		for(int i = 0; i < Point.nrClasses +1; i++)
			votes.add(0);
		
		Iterator<GraphPoint> neighbourIterator = neighbours.iterator();
		while(neighbourIterator.hasNext())
		{
			int classnr = neighbourIterator.next().getClassification();
			votes.set(classnr+1, votes.get(classnr+1)+1);	//dit gaat mis als p.getClassification() < -1
		}
		return votes.indexOf(Collections.max(votes)) - 1;
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
				if(getFirstOrderClassification(p.getEdges()) == p.getClassification())
				{
					if(editOrder == EDIT_1ST_ORDER || getSecondOrderClassification(p) == p.getClassification())
					{
						List<GraphPoint> edges = p.getEdges();
						Iterator<GraphPoint> edgeIterator = edges.iterator();
						while(edgeIterator.hasNext())
							edgeIterator.next().removeEdge(p);
                        removePoints.add(p);
					}
				}
				
			}

           // points.removeAll(removePoints);
            Iterator<GraphPoint> it = removePoints.iterator();
            Iterator<GraphPoint> it2 = points.iterator();
            GraphPoint r;
            if(it.hasNext())
            {
                r = it.next();
                while(it2.hasNext())
                {
                    GraphPoint p = it2.next();
                    if(p.equals(r))
                    {
                        it2.remove();
                        if(it.hasNext())
                        {
                            r = it.next();
                        }
                        else
                            break;
                    }
                }
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
		while(iter.hasNext())
		{
			GraphPoint p = iter.next();
			if(!hasNeighbourOfOtherClass(p))
			{
				// This graphpoint has the same type as all its neighbours, so we can safely ignore it.
				List<GraphPoint> neighbours = p.getEdges();
				Iterator<GraphPoint> neighbourIterator = neighbours.iterator();
				while(neighbourIterator.hasNext())
					neighbourIterator.next().removeEdge(p);
				iter.remove();
				recalculateEdges(neighbours);
			}
		}
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
