package edu.ur.ir.marc.service;

import java.util.List;

import edu.ur.ir.marc.MarcDataFieldMapper;
import edu.ur.ir.marc.MarcDataFieldMapperDAO;
import edu.ur.ir.marc.MarcDataFieldMapperService;

/**
 * Default implementation of the data field mapper service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultMarcDataFieldMapperService implements MarcDataFieldMapperService {

	// eclipse generated id.
	private static final long serialVersionUID = 2738482086606104186L;

	private MarcDataFieldMapperDAO marcDataFieldMapperDAO;
	

	/**
	 * Delete the marc data field mapper.
	 * 
	 * @see edu.ur.ir.marc.MarcDataFieldMapperService#delete(edu.ur.ir.marc.MarcDataFieldMapper)
	 */
	public void delete(MarcDataFieldMapper entity) {
		marcDataFieldMapperDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List<MarcDataFieldMapper> getAll() {
		return marcDataFieldMapperDAO.getAll();
	}

	/**
	 * Get the marc data field mapper by data field id.
	 * 
	 * @see edu.ur.ir.marc.MarcDataFieldMapperService#getByDataFieldId(java.lang.Long)
	 */
	public MarcDataFieldMapper getByDataFieldIndicatorsId(Long dataFieldId, String indicator1, String indicator2) {
		return marcDataFieldMapperDAO.getByMarcDataFieldIndicatorsId(dataFieldId, indicator1, indicator2);
	}

	/**
	 * Get the marc data field mapper by id
	 * @see edu.ur.ir.marc.MarcDataFieldMapperService#getById(java.lang.Long, boolean)
	 */
	public MarcDataFieldMapper getById(Long id, boolean lock) {
		return marcDataFieldMapperDAO.getById(id, lock);
	}

	/**
	 * Save the marc data field mapper.
	 * 
	 * @see edu.ur.ir.marc.MarcDataFieldMapperService#save(edu.ur.ir.marc.MarcDataFieldMapper)
	 */
	public void save(MarcDataFieldMapper entity) {
		marcDataFieldMapperDAO.makePersistent(entity);
	}
	
	/**
	 * Set the marc data field mapper data access object.
	 * 
	 * @param marcDataFieldMapperDAO
	 */
	public void setMarcDataFieldMapperDAO(
			MarcDataFieldMapperDAO marcDataFieldMapperDAO) {
		this.marcDataFieldMapperDAO = marcDataFieldMapperDAO;
	}


}
