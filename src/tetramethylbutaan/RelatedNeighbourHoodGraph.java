package tetramethylbutaan;
import java.util.List;

public class RelatedNeighbourHoodGraph extends Graph
{
	public RelatedNeighbourHoodGraph(List<GraphPoint> points)
	{
		this.points = points;
		createEdges();
	}
	
	// TODO: is deze nodig, zo ja, dan moet hij eerst zelf een nieuwe points aanmaken, welke grootte?
	// zo doet hij niet veel
	//Maar wel iets: TrainingSetReader gebruikt deze constructor om nullpointerexception te voorkomen en zodat createEdges niet wordt aangeroepen voordat alle punten zijn toegevoegd
	public RelatedNeighbourHoodGraph()
	{
		// Deze code doet niet zo veel. 
		//points = new ArrayList<GraphPoint>();
		//for(GraphPoint p: points)
		//	p = new GraphPoint();
		// En nu helemaal niet, want hij staat in comments.
	}
	
	public boolean isNeighbour(GraphPoint p1, GraphPoint p2)
	{
		if (p1.equals(p2))
			return false;
		
		for (Point point : points)
		{
			if (!point.equals(p1) && !point.equals(p2) &&
					Math.sqrt(p1.euclidianDistance2(p2)) >
					Math.max(Math.sqrt(p1.euclidianDistance2(point)), Math.sqrt(p2.euclidianDistance2(point))))
			{
				return false;
			}
		}
		return true;
	}
	
	protected void recalculateEdges(List<GraphPoint> neighbours) 
	//gebruik maken van dezelfde volgorde edges in sub als in this?
	{
		RelatedNeighbourHoodGraph sub = new RelatedNeighbourHoodGraph(neighbours);
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
