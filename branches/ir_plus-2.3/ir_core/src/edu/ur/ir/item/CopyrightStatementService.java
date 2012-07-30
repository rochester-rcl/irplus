package edu.ur.ir.item;

import java.io.Serializable;
import java.util.List;

import edu.ur.order.OrderType;

/**
 * Service for getting and updating copyright statements.
 * 
 * @author Nathan Sarr
 *
 */
public interface CopyrightStatementService extends Serializable{
	
	/**
	 * Get content types sorting according to the sort and filter information .  
	 * 
	 * Sort is applied based on the order of sort information in the list (1st to last).
	 * Starts at the specified row start location and stops at specified row end.
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - number of results to get
	 * @param sortType - Order by asc/desc
	 * 
	 * @return List of content types.
	 */
	public List<CopyrightStatement> getCopyrightStatementsOrderByName(int rowStart, 
    		int numberOfResultsToShow, OrderType orderType);

    /**
     * Get a count of copyright statements 
     *  
     * @return - the number of copyright statements found
     */
    public Long getCount();
    
    /**
     * Delete the copyright statement
     * 
     * @param copyrightStatement 
     */
    public void delete(CopyrightStatement copyrightSatement);
    
    
    /**
     * Get a copyright statement by name.
     * 
     * @param name - name of the copyright statement.
     * @return - the found copyright statement or null if the copyright statement is not found.
     */
    public CopyrightStatement get(String name);
    
    /**
     * Get a copyright statement by id
     * 
     * @param id - unique id of the copyright statement.
     * @param lock - upgrade the lock on the data
     * @return - the found copyright statement or null if the copyright statement is not found.
     */
    public CopyrightStatement get(Long id, boolean lock);
    
    /**
     * Save the copyright statement.
     * 
     * @param CopyrightStatement
     */
    public void save(CopyrightStatement CopyrightStatement);
 
	/**
	 * Get all copyright statements.
	 * 
	 * @return List of all copyright statements
	 */
	public List<CopyrightStatement> getAll();
	
	

}
