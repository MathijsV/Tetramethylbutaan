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
	
	public void addEdge(GraphPoint p)
	{
		edges.add(p);
		p.edges.add(this);
	}
	
	public void copyEdge(GraphPoint p)
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
    
	public boolean hasEdgeWith(GraphPoint point)
	{
		return edges.contains(point);
		/*for (GraphPoint p : edges)
		{
			if (p.equals(point))
			{
				return true;
			}
		}
		return false;*/
	}
	
	public List<GraphPoint> getEdges()
	{
		return edges;
	}
	
	public boolean removeEdge(GraphPoint p)
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
}
