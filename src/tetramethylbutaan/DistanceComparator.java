/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetramethylbutaan;

import java.util.Comparator;

/**
 *
 * @author pbos
 */
public class DistanceComparator implements Comparator<Point>
{
    private Point p;

    public DistanceComparator(Point p)
    {
        this.p = p;
    }

    public int compare(Point p1, Point p2)
    {
        double dist1 = p.euclideanDistance2(p1);
        double dist2 = p.euclideanDistance2(p2);

        if(dist1 < dist2)
        {
            return -1;
        }
        else if(dist1 == dist2)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

}
