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
    private Graph graph;
    private List<GraphPoint> testPoints;
    private List<Integer> results;

    public PointTester(Graph graph, List<GraphPoint> testPoints)
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

        while(testPoint.hasNext())
        {
            GraphPoint p = testPoint.next();
            results.add(graph.test(p, false));
        }
    }

}
