package tetramethylbutaan;
import java.util.ArrayList;
import java.util.List;


public class GraphPoint extends Point
{
	private List<GraphPoint> edges = new ArrayList<GraphPoint>();
	
	public GraphPoint(double[] features)
	{
		super(features);
	}
	
	public GraphPoint(double[] features, int c)
	{
		super(features, c);
	}
	
	public GraphPoint()
	{
		super();
	}
	
	public synchronized void addEdge(GraphPoint p)
	{
		edges.add(p);
		p.edges.add(this);
	}
	
	public synchronized void copyEdge(GraphPoint p)
	{
		if(!hasEdgeWith(p) && !p.hasEdgeWith(this))
		{
			edges.add(p);
			p.edges.add(this);
		}
	}
	
	// Overbodige functie, zelfde als getEdges
    /*public List<GraphPoint> getNeighbours()
    {
        return edges;
    }*/
    
	public synchronized boolean hasEdgeWith(GraphPoint point)
	{
		return edges.contains(point);
		/*for (GraphPoint p : edges)
			if (p == point)
				return true;
		return false;*/
		// Het deel in commentaar veroorzaakt vreemd genoeg een concurrentmodificationexception.
	}
	
	public List<GraphPoint> getEdges()
	{
		return edges;
	}
	
	public synchronized boolean removeEdge(GraphPoint p)
	{
		return edges.remove(p);
		/*if(hasEdgeWith(p))
		{
			edges.remove(p);
			//p.edges.remove(this);
			return true;
		}
		else
			return false;*/
	}

    public synchronized void removeEdges()
    {
        edges = new ArrayList<GraphPoint>();
    }
}
