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
}
