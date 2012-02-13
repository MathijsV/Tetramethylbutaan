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
		p.edges.add(p);
	}
	
	// nodig?
	public boolean hasEdge(GraphPoint point)
	{
		for (GraphPoint p : edges)
		{
			if (p.equals(point))
			{
				return true;
			}
		}
		return false;
	}
	
	public List<GraphPoint> getEdges()
	{
		return edges;
	}
	
	public boolean removeEdge(GraphPoint p)
	{
		if(hasEdge(p))
		{
			edges.remove(p);
			return true;
		}
		else
			return false;
	}
}
