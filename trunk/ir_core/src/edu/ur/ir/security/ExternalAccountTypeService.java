package edu.ur.ir.security;

import java.util.List;

import edu.ur.order.OrderType;


/**
 * Service for dealing with external account types.
 * 
 * @author Nathan Sarr
 *
 */
public interface ExternalAccountTypeService {
	
	/**
	 * Get external account types sorted according to the sort information .  
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - number of results to get
	 * @param orderType - Order by asc/desc
	 * 
	 * @return List of external account types.
	 */
	public List<ExternalAccountType> getExternalAccountTypesOrderByName( final int rowStart, 
    		final int numberOfResultsToShow, final OrderType orderType);

    /**
     * Get a count of external account types.
     *  
     * @return - the number of external account types found
     */
    public Long getCount();
    
    /**
     * Delete the external account type
     * 
     * @param external account type
     */
    public void delete(ExternalAccountType externalAccountType);
    
    /**
     * Get an external account type by name.
     * 
     * @param name - name of the external account type.
     * @return - the found external account type or null if the external account type is not found.
     */
    public ExternalAccountType get(String name);
    
    /**
     * Get an external account type by id
     * 
     * @param id - unique id of the external account type.
     * @param lock - upgrade the lock on the data
     * @return - the found external account type or null if the external account type is not found.
     */
    public ExternalAccountType get(Long id, boolean lock);
    
    /**
     * Save the external account type.
     * 
     * @param external account type
     */
    public void save(ExternalAccountType externalAccountType);
    
 
    /**
     * Get all external account types
     * 
     * @return List of all external account types
     */
    public List<ExternalAccountType> getAll();

}
