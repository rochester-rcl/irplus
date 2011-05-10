package edu.ur.ir.item.service;

import java.util.List;

import edu.ur.ir.item.PlaceOfPublication;
import edu.ur.ir.item.PlaceOfPublicationDAO;
import edu.ur.ir.item.PlaceOfPublicationService;
import edu.ur.order.OrderType;

/**
 * Service to deal with place of publication information
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultPlaceOfPublicationService implements PlaceOfPublicationService{
	
	//Eclipse generated id.
	private static final long serialVersionUID = 1759589927613949759L;
	
	// data access for place of publication
	private PlaceOfPublicationDAO placeOfPublicationDAO;
	

	/**
	 * Get place of publication types sorting according to the sort and filter information .  
	 * 
	 * Sort is applied based on the order of sort information in the list (1st to last).
	 * Starts at the specified row start location and stops at specified row end.
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - number of results to get
	 * @param sortType - Order by asc/desc

	 * @return List of language types.
	 */
	public List<PlaceOfPublication> getOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final OrderType orderType)
    {
	    return placeOfPublicationDAO.getOrderByName(rowStart, numberOfResultsToShow, orderType);	
    }

    /**
     * Get a count of place of publication
     *  
     * @return - the number places
     */
    public Long getCount()
    {
    	return placeOfPublicationDAO.getCount();
    }
    
    /**
     * Delete the specified language type.
     * 
     * @param placeOfPublication
     */
    public void delete(PlaceOfPublication placeOfPublication)
    {
    	placeOfPublicationDAO.makeTransient(placeOfPublication);
    }
    
    /**
     * Get a place of publication by name.
     * 
     * @param name - name of the language type.
     * @return - the found language type or null if the language type is not found.
     */
    public PlaceOfPublication get(String name)
    {
    	return placeOfPublicationDAO.findByUniqueName(name);
    }
    
    /**
     * Get a place of publication by id
     * 
     * @param id - unique id of the language type.
     * @param lock - upgrade the lock on the data
     * @return - the found language type or null if the language type is not found.
     */
    public PlaceOfPublication get(Long id, boolean lock)
    {
    	return placeOfPublicationDAO.getById(id, lock);
    }
    
    /**
     * Save the place of publication
     * 
     * @param placeOfPublication
     */
    public void save(PlaceOfPublication placeOfPublication)
    {
    	placeOfPublicationDAO.makePersistent(placeOfPublication);
    }
	
    /**
     * Get all the places of publication
     * 
     * @return all places of publication
     */
    @SuppressWarnings("unchecked")
	public List<PlaceOfPublication> getAll()
    {
    	return placeOfPublicationDAO.getAll();
    }
    
    
    /**
     * Get the place of publication by letter code.
     * 
     * @param letterCode - letter code for the place of publication
     * @return the place of publication
     */
    public List<PlaceOfPublication> getByLetterCode(String letterCode)
    {
    	return placeOfPublicationDAO.getByLetterCode(letterCode);
    }
    
	/**
	 * Get the place of publication.
	 * 
	 * @return
	 */
	public PlaceOfPublicationDAO getPlaceOfPublicationDAO() {
		return placeOfPublicationDAO;
	}

	/**
	 * Set the place of publication.
	 * 
	 * @param placeOfPublicationDAO
	 */
	public void setPlaceOfPublicationDAO(PlaceOfPublicationDAO placeOfPublicationDAO) {
		this.placeOfPublicationDAO = placeOfPublicationDAO;
	}

  

}
