package tetramethylbutaan;
import java.util.List;

public class GabrielGraph extends Graph
{
    public GabrielGraph()
    {

    }

    public GabrielGraph(List<GraphPoint> points)
    {
        this.points.addAll(points);
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
	
	protected void recalculateEdges(List<GraphPoint> neighbours) 
	//gebruik maken van dezelfde volgorde edges in sub als in this?
	{
		GabrielGraph sub = new GabrielGraph(neighbours);
        sub.createEdges();
		for(GraphPoint sp: sub.points)
		{
			for(GraphPoint pp: points)
			{
				if(sp.equals(pp))
				{
					List<GraphPoint> spedges = sp.getEdges();
					for(GraphPoint newEdge: spedges)
						pp.copyEdge(newEdge);
				}
			}
		}
	}
}
