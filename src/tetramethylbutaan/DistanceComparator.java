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
public class DistanceComparator implements Comparator
{
    private Point p;

    public DistanceComparator(Point p)
    {
        this.p = p;
    }

    public int compare(Object o1, Object o2)
    {
        Point p1 = (Point) o1;
        Point p2 = (Point) o2;
        
        if(p.euclidianDistance2(p1) < p.euclidianDistance2(p2))
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }

}
