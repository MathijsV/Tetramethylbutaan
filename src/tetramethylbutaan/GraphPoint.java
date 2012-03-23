package tetramethylbutaan;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class GraphPoint extends Point
{
	private List<GraphPoint> edges = Collections.synchronizedList(new ArrayList<GraphPoint>());
	private HashMap<GraphPoint, Double> distances = new  HashMap<GraphPoint, Double>();

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
	
	public void addEdge(GraphPoint p)
	{
        double distance = Math.sqrt(p.euclideanDistance2(this));
		edges.add(p);
        distances.put(p, distance);
		p.edges.add(this);
        p.distances.put(this, distance);
	}

    public double getDistance (GraphPoint p)
    {
        if(distances.get(p) == null)
            System.out.println("d null!");
        return distances.get(p);
    }

	/*public synchronized void copyEdge(GraphPoint p)
	{
		if(!hasEdgeWith(p) && !p.hasEdgeWith(this))
		{
			edges.add(p);
			p.edges.add(this);
		}
	}*/
	
	// Overbodige functie, zelfde als getEdges
    /*public List<GraphPoint> getNeighbours()
    {
        return edges;
    }*/
    
	public boolean hasEdgeWith(GraphPoint point)
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
	
	public boolean removeEdge(GraphPoint p)
	{
        boolean contains = edges.remove(p);
        if (contains)
        {
            distances.remove(p);
        }

		return contains;
		/*if(hasEdgeWith(p))
		{
			edges.remove(p);
			//p.edges.remove(this);
			return true;
		}
		else
			return false;*/
	}

    public void removeEdges()
    {
        edges = new ArrayList<GraphPoint>();
        distances = new  HashMap<GraphPoint, Double>();
    }
}
