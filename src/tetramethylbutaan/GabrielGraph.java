package tetramethylbutaan;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GabrielGraph
{
	private List<GraphPoint> points = new ArrayList<GraphPoint>();
	
	public GabrielGraph(List<GraphPoint> points)
	{
		this.points = points;
		createEdges();
	}
	public GabrielGraph()
	{
		for(GraphPoint p: points) //gaat hier iets fout?
			p = new GraphPoint();
	}
	
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
	
	// TODO; de helft van de combinanties wegknippen -> Done
	public void createEdges()
	{
		for (int i = 0; i < points.size(); i++)
			for (int j = i+1; j < points.size(); j++)
			{
				GraphPoint p1 = points.get(i), p2 = points.get(j);
				if (isNeighbour(p1, p2) && !p1.hasEdge(p2))
					p1.addEdge(p2);
			}
	}
	
	public void edit()
	{
		for(GraphPoint p: points)
		{
			List<Integer> votes = new ArrayList<Integer>();
			for(int i = 0; i < Point.nrClasses +1; i++)
				votes.set(i, 0);
			List<GraphPoint> neighbours = p.getEdges();
			for(int i = 0; i < neighbours.size(); i++)
			{
				votes.set(neighbours.get(i).getClassification()+1, votes.get(i)+1);
			}
			int most = Collections.max(votes);
			if(most-1 != p.getClassification())
			{
				removeEdgesOf(p);
			}
			
		}
	}
	
	public void removeEdgesOf(GraphPoint p) 
	//gebruik maken van dezelfde volgorde edges in sub als in this?
	//nog niet gecheckt!
	{
		List<GraphPoint> edges = p.getEdges();
		for(int i = 0; i < edges.size(); i++)
			edges.get(i).removeEdge(p);
		points.remove(p);
		GabrielGraph sub = new GabrielGraph(edges);
		for(GraphPoint sp: sub.points)
		{
			for(GraphPoint pp: points)
			{
				if(sp.equals(pp))
				{
					for(GraphPoint newEdge: sp.getEdges())
						pp.addEdge(newEdge);
				}
			}
		}
	}
}
