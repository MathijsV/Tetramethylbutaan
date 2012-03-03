/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetramethylbutaan;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author mvos
 */
public class EdgeCreator implements Runnable
{
    private Graph graph;
    private int modulo = 0, start = 0;

    public EdgeCreator(Graph g, int start, int modulo)
    {
        this.graph = g;
        this.modulo = modulo;
        this.start = start;
    }

    public void run()
    {
        List<GraphPoint> points = graph.getPoints();
        Iterator<GraphPoint> pointIt = points.iterator();
        
        for(int i = 0; i < start; i++)
        {
        	if(pointIt.hasNext()) pointIt.next();
        }

        int num = 0;
        for (int i = start; i < points.size(); i += modulo)
		{
            num++;
            
            GraphPoint p1 = pointIt.next();
            Iterator<GraphPoint> pointIt2 = points.iterator();

            for(int j = 0; j < i+1; j++)
            {
                if(pointIt2.hasNext()) pointIt2.next();
            }

			for (int j = i+1; j < points.size(); j++)
			{
				GraphPoint p2 = pointIt2.next();
				if(graph.isNeighbour(p1, p2) && !p1.hasEdgeWith(p2))
					p1.addEdge(p2);
			}

            for(int j = 0; j < modulo-1; j++)
            {
                if(pointIt.hasNext()) pointIt.next();
            }
		}
        //System.out.println("Thread ready " + modulo + " (Processed " + num + " items)");
    }

}
