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

package edu.ur.ir.item;

import java.util.List;

/**
 * Sponsor service interface for creating and getting Sponsor.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface SponsorService {
	
	/**
	 * Get Sponsor sorted according to the sort information .  
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - number of results to get
	 * @param sortType - Order by asc/desc
	 * 
	 * @return List of sponsors
	 */
	public List<Sponsor> getSponsorsOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);
	
    /**
     * Get a count of Sponsor.
     *  
     * @return - the number of sponsors found
     */
    public Long getSponsorsCount();
    
    /**
     * Delete a Sponsor with the specified name.
     * 
     * @param id
     */
    public boolean deleteSponsor(Long id);
    
    /** 
     * Delete the Sponsor with the specified name.
     * 
     * @param name
     */
    public boolean deleteSponsor(String name);
    
    /**
     * Get a Sponsor by name.
     * 
     * @param name - name of the Sponsor .
     * @return - the found Sponsor or null if the Sponsor is not found.
     */
    public Sponsor getSponsor(String name);
    
    /**
     * Get a Sponsor by id
     * 
     * @param id - unique id of the Sponsor .
     * @param lock - upgrade the lock on the data
     * @return - the found Sponsor or null if the Sponsor is not found.
     */
    public Sponsor getSponsor(Long id, boolean lock);
    
    /**
     * Save the Sponsor .
     * 
     * @param sponsor
     */
    public void saveSponsor(Sponsor sponsor );
 
	/**
	 * Get all Sponsor.
	 * 
	 * @return List of all Sponsor 
	 */
	public List<Sponsor> getAllSponsor();

}
