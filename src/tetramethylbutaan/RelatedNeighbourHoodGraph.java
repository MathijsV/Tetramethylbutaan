package tetramethylbutaan;
import java.util.List;

public class RelatedNeighbourHoodGraph extends Graph
{
    public RelatedNeighbourHoodGraph()
    {

    }

    public RelatedNeighbourHoodGraph(List<GraphPoint> points)
    {
        this.points.addAll(points);
    }

	public boolean isNeighbour(GraphPoint p1, GraphPoint p2)
	{
		if (p1 == p2)
			return false;
		
		for (Point point : points)
		{
			if (point != p1 && point != p2 &&
					p1.euclideanDistance2(p2) >
					Math.max(p1.euclideanDistance2(point), p2.euclideanDistance2(point)))
			{
				return false;
			}
		}
		return true;
	}

}
