/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetramethylbutaan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author mvos
 */
public class PointTester implements Runnable
{
    private GabrielGraph graph;
    private List<GraphPoint> testPoints;
    private List<Integer> results;

    public PointTester(GabrielGraph graph, List<GraphPoint> testPoints)
    {
        this.graph = graph;
        this.testPoints = testPoints;
        this.results = new ArrayList<Integer>();
    }

    public List<Integer> getResults()
    {
        return results;
    }

    public void run()
    {
        Iterator<GraphPoint> testPoint = testPoints.iterator();

        AWESOMELOOP : while(testPoint.hasNext())
        {
            List<GraphPoint> neighbours = new ArrayList<GraphPoint>();
            GraphPoint p = testPoint.next();

            Iterator<GraphPoint> iter = graph.getPoints().iterator();
            while(iter.hasNext())
            {
                GraphPoint point = iter.next();
                if(p.equals(point))
                {
                    results.add(point.getClassification());
                    continue AWESOMELOOP;
                }

                if(graph.isNeighbour(p, point))
                {
                    if(!neighbours.contains(point))
                        neighbours.add(point);

                    /*for(GraphPoint neighbour : point.getEdges())
                    {
                        if(neighbour != p && !neighbours.contains(neighbour))
                        {
                            neighbours.add(neighbour);
                        }
                    }*/
                }
            }

            Collections.sort(neighbours, new DistanceComparator(p));
            results.add(Graph.getFirstOrderClassification(neighbours.subList(0, (Math.min(Graph.K, neighbours.size())))));
        }
    }

}
