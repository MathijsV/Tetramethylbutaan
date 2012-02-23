package tetramethylbutaan;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GabrielGraph
{
	public final static int EDIT_1ST_ORDER = 1, EDIT_2ND_ORDER = 2;
	
	private List<GraphPoint> points = new ArrayList<GraphPoint>();
	
	public GabrielGraph(List<GraphPoint> points)
	{
		this.points = points;
		createEdges();
	}
	public GabrielGraph()
	{
		for(GraphPoint p: points)
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
	
	/**
	 * @param neighbours: the neighbours of a point or subgraph
	 * @return the most occuring class in neighbours 
	 */
	public int getFirstOrderClassification(List<GraphPoint> neighbours)
	{
		List<Integer> votes = new ArrayList<Integer>();
		for(int i = 0; i < Point.nrClasses +1; i++)
			votes.add(0);
		for(int i = 0; i < neighbours.size(); i++)
		{
			int classnr = neighbours.get(i).getClassification();
			votes.set(classnr+1, votes.get(classnr+1)+1);	//dit gaat mis als p.getClassification() < -1
		}
		return votes.indexOf(Collections.max(votes)) - 1;
	}
	
	/**
	 * @param p: the point to be classified
	 * @return the classification of p based on the neighbours of p and the neighbours' neighbours
	 */
	public int getSecondOrderClassification(GraphPoint p)
	{
		List<GraphPoint> neighbourhood = new ArrayList<GraphPoint>();
		for(GraphPoint neighbour: p.getEdges())
		{
			if(neighbour.getClassification() == p.getClassification())
			{
				for(GraphPoint edge: neighbour.getEdges())
				{
					if(!neighbourhood.contains(edge) && !p.getEdges().contains(edge))
						neighbourhood.add(edge);
				}
			}
		}
		return getFirstOrderClassification(neighbourhood);
	}
	
	/**
	 * calculates the (new) edges between the neighbours
	 * @param neighbours: the neighbours of already a removed point
	 */
	private void recalculateEdges(List<GraphPoint> neighbours) 
	//gebruik maken van dezelfde volgorde edges in sub als in this?
	{
		GabrielGraph sub = new GabrielGraph(neighbours);
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
	
	/**
	 * removes graphpoints from a graph which are misclassified by their neighbours
	 * @param editOrder: EDIT_1ST_ORDER or EDIT_2ND_ORDER expected
	 */
	public void edit(int editOrder)
	{
		if(editOrder != 1 && editOrder != 2)
			return;
		else
		{
			Iterator<GraphPoint> iter = points.iterator();
			while(iter.hasNext())
			{
				GraphPoint p = iter.next();
				if(getFirstOrderClassification(p.getEdges()) == p.getClassification())
				{
					if(editOrder == 1 || getSecondOrderClassification(p) == p.getClassification())
					{
						List<GraphPoint> edges = p.getEdges();
						for(int i = 0; i < edges.size(); i++)
							edges.get(i).removeEdge(p);
						iter.remove();
						recalculateEdges(edges);
					}
				}
				
			}
		}
	}
	
}
