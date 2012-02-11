import java.util.ArrayList;
import java.util.List;


public class GGgraph
{
	private List<GraphPoint> points = new ArrayList<GraphPoint>();
	
	public void add(GraphPoint p)
	{
		points.add(p);
	}
	
	public boolean isNeighbour(GraphPoint p1, GraphPoint p2)
	{
		if (p1.equals(p2))
			return false;
		
		for (Point point : points)
		{
			if (!point.equals(p1) && !point.equals(p2) &&
					p1.euclidianDistance2(p2) >
					p1.euclidianDistance2(point) + p2.euclidianDistance2(point))
			{
				return false;
			}
		}
		return true;
	}
	
	// TODO; de helft van de combinanties wegknippen
	public void createEdges()
	{
		for (GraphPoint p1 : points)
		for (GraphPoint p2 : points)
		{
			if (isNeighbour(p1, p2) && !p1.hasEdge(p2))
				p1.addEdge(p2);
		}
	}
}
