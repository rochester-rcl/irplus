package edu.ur.order;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Sorts an orderable in descending order.
 * 
 * @author Nathan Sarr
 *
 */
public class DescendingOrderComparator implements Comparator<Orderable>, Serializable
{

	/** Eclipse generated id */
	private static final long serialVersionUID = 7063513127512541880L;

    /**
     * Compare the two values.
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Orderable arg0, Orderable arg1)
    {
        if (arg0.getOrder() > arg1.getOrder())
        {
            return -1;
        } 
        else if (arg0.getOrder() < arg1.getOrder())
        {
            return 1;
        } 
        else
        {
            return 0;
        }
    }

}
