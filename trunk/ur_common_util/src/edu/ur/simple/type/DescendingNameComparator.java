package edu.ur.simple.type;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Return the name aware objects in descending order.
 * 
 * @author Nathan Sarr
 *
 */
public class DescendingNameComparator implements Comparator<NameAware>, Serializable{

	/** eclipse genarated id */
	private static final long serialVersionUID = -2166294396178883394L;

	public int compare(NameAware o1, NameAware o2) {
		return o2.getName().compareToIgnoreCase((o1.getName()));
	}

}
