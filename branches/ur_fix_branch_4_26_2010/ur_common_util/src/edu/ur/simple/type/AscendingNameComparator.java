package edu.ur.simple.type;

import java.io.Serializable;
import java.util.Comparator;


/**
 * Return the name aware objects in ascending order.
 * 
 * @author Nathan Sarr
 *
 */
public class AscendingNameComparator implements Comparator<NameAware>, Serializable{

	/** eclipse generated id */
	private static final long serialVersionUID = -1653266444762644052L;

	
	/**
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(NameAware o1, NameAware o2) {
		return o1.getName().compareToIgnoreCase(o2.getName());
	}

}
