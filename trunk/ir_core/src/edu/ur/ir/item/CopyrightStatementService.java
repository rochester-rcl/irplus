package edu.ur.ir.item;

import java.util.List;

/**
 * Service for getting and updating copyright statements.
 * 
 * @author Nathan Sarr
 *
 */
public interface CopyrightStatementService {
	
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
	public List<CopyrightStatement> getCopyrightStatementsOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);

    /**
     * Get a count of copyright statements 
     *  
     * @return - the number of copyright statements found
     */
    public Long getCount();
    
    /**
     * Delete a copyright statement with the specified name.
     * 
     * @param id
     */
    public boolean delete(Long id);
    
    /** 
     * Delete the copyright statement with the specified name.
     * 
     * @param name
     */
    public boolean delete(String name);
    
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
	
	/**
	 * Get all copyright statements by name asc order.
	 * 
	 * @return copyright statement by name order
	 */
	public List<CopyrightStatement> getAllByNameOrder();

}
