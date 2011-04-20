package edu.ur.ir.marc;

import edu.ur.dao.CrudDAO;

/**
 * Data access for the mapping between a contributor type and relator code.
 * 
 * @author Nathan Sarr
 *
 */
public interface MarcContributorTypeRelatorCodeDAO extends CrudDAO<MarcContributorTypeRelatorCode>
{
	/**
	 * Get the mapping by contributor id.
	 * 
	 * @param contributorId - id of the contributor
	 * @return - the mapping otherwise null
	 */
	public MarcContributorTypeRelatorCode getByContributorType(Long contributorId);
	
	/**
	 * Get the mapping by relator code.
	 * 
	 * @param relatorCodeId - relator code to look for
	 * @return the mapping otherwise null.
	 */
	public MarcContributorTypeRelatorCode getByRelatorCode( Long relatorCodeId);
	
}
