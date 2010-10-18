/**  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  


package edu.ur.dao;

import java.util.LinkedList;
import java.util.List;

/**
 * Information to help build criteria.
 * 
 * @author Nathan Sarr
 *
 */
@SuppressWarnings("unchecked")
public class CriteriaHelper implements Comparable{
	
	/**  Ascending sort order  */
	public final static String ASC = "asc";
	
	/**  Desending sort order  */
	public final static String DESC = "desc";
	
	/**  String property on the object to filter on */
    private final String property;
    
    /** indicates if the case should be ignored on sorting*/
    private boolean ignoreCaseOnSort;
    
    /**  The actual object  */
    private Object value; 
    
	/** The sort order  */
	private int order = 1000;
	
	/**  Sort type to apply */
	private String sortType;
	
	/** Indicates if a sort should be applied  */
	private boolean sort = false;
	
	/** Indicates if a filter should be applied  */
	private boolean filter = false;
    
    /**  List of associated objects from the root object */
	private List<String> associationPath = new LinkedList<String>();

    /**
     * Default construtor.
     * 
     * @param property
     * @param value
     */
    public CriteriaHelper(String property) {
        this.property = property;
    }

    /**
     * Get the property to filter on.
     * 
     * @see edu.ur.dao.FilterHelper#getProperty()
     */
    public String getProperty() {
        return property;
    }

    /**
     * The value the property should have.
     * 
     * @see edu.ur.dao.FilterHelper#getValue()
     */
    public Object getValue() {
        return value;
    }
    
	/**
	 * This adds a path element to access values
	 * within an associated class.
	 * 
	 * @param pathElement
	 */
	public void addAssocationPathElement(String pathElement)
	{
		associationPath.add(pathElement);
	}
	
	/**
	 * Get the list of associated objects in order
	 * 
	 * @return
	 */
	public List<String> getAssociationPathElements() {
		return associationPath;
	}
    
    /**
     * To string prints out values.
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
    	StringBuffer sb = new StringBuffer("[property = ");
    	sb.append(property);
    	sb.append(" value = ");
    	sb.append(value);
    	sb.append(" property = ");
    	sb.append(property );
    	sb.append(" sort = " );
    	sb.append(sort);
    	sb.append(" filter = ");
    	sb.append(filter);
    	sb.append(" sortType = ");
    	sb.append(sortType);
    	sb.append(" order = ");
    	sb.append(order);
    	sb.append("]");
    	return sb.toString();
    }

	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isSort() {
		return sort;
	}

	public void setSort(boolean sort) {
		this.sort = sort;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * Larger sort order is considered larger.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		if (!(o instanceof CriteriaHelper)) return 0;
		
		CriteriaHelper other = (CriteriaHelper)o;
		
		if( other.getOrder() == order) return 0;
		if( other.getOrder() > order ) return -1;
		else return 1;
			
	}

	public boolean isIgnoreCaseOnSort() {
		return ignoreCaseOnSort;
	}
	
	public boolean getIgnoreCaseOnSort() {
		return ignoreCaseOnSort;
	}

	public void setIgnoreCaseOnSort(boolean ignoreCase) {
		this.ignoreCaseOnSort = ignoreCase;
	}

}
