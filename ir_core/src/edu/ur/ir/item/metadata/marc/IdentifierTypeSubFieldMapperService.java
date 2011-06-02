package edu.ur.ir.item.metadata.marc;

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
	
	/**
	 * Get the list of all identifiers with the specified data field name and indicator settings.
	 * 
	 * @param code - name of the data field (100, 200, etc)
	 * @param indicator1 - first indicator value
	 * @param indicator2 - second indicator value
	 * @param subField - subfield value
	 * 
	 * @return list of identifier sub filed mappings.
	 */
	public List<IdentifierTypeSubFieldMapper> getByDataField(String code, 
			String indicator1, 
			String indicator2, 
			String subField);
	

}
