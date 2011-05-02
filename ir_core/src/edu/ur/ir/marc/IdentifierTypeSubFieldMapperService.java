package edu.ur.ir.marc;

import java.util.List;

/**
 * @author Nathan Sarr
 *
 */
public interface IdentifierTypeSubFieldMapperService {
	
	/**
	 * Get all identifier type sub field mappers.
	 * 
	 * @return all identifier type sub filed mappers
	 */
	public List<IdentifierTypeSubFieldMapper> getAll();

	/**
	 * Get identifier type sub field mapper by id.
	 * 
	 * @param id - id of the sub field mapper
	 * @param lock - upgrade the lock mode
	 * 
	 * @return the found identifier type otherwise null.
	 */
	public IdentifierTypeSubFieldMapper getById(Long id, boolean lock);

	/**
	 * Save the identifier type sub field mapper.
	 * 
	 * @param entity
	 */
	public void save(IdentifierTypeSubFieldMapper entity);

	/**
	 * Delete the identifier type sub field mapper from persistence.
	 * 
	 * @param entity
	 */
	public void delete(IdentifierTypeSubFieldMapper entity);
	
	/**
	 * Get by the identifier type id.
	 * 
	 * @param id - identifier type id.
	 * @return the mapper if found otherwise null
	 */
	public List<IdentifierTypeSubFieldMapper> getByIdentifierTypeId(Long id);
	

}
