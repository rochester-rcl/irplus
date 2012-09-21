package edu.ur.metadata.marc.service;

import java.util.List;

import edu.ur.metadata.marc.MarcRelatorCode;
import edu.ur.metadata.marc.MarcRelatorCodeDAO;
import edu.ur.metadata.marc.MarcRelatorCodeService;

/**
 * Marc relator code service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultMarcRelatorCodeService implements MarcRelatorCodeService{

	// Eclipse generated id
	private static final long serialVersionUID = -7757935865178019917L;

	//Data access class for marc relator codes.
	private MarcRelatorCodeDAO marcRelatorCodeDAO;
	
	/**
	 * Delete marc relator code
	 * @see edu.ur.metadata.marc.MarcRelatorCodeService#delete(edu.ur.metadata.marc.MarcRelatorCode)
	 */
	public void delete(MarcRelatorCode entity) {
		marcRelatorCodeDAO.makeTransient(entity);
	}

	/**
	 * Get the marc relator code by name.
	 * 
	 * @see edu.ur.metadata.marc.MarcRelatorCodeService#findByName(java.lang.String)
	 */
	public MarcRelatorCode findByName(String name) {
		return marcRelatorCodeDAO.findByUniqueName(name);
	}

	/**
	 * Get all of the marc relator codes.
	 * 
	 * @see edu.ur.metadata.marc.MarcRelatorCodeService#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<MarcRelatorCode> getAll() {
		return marcRelatorCodeDAO.getAll();
	}

	/**
	 * Get the marc relator code by id.
	 * 
	 * @see edu.ur.metadata.marc.MarcRelatorCodeService#getById(java.lang.Long, boolean)
	 */
	public MarcRelatorCode getById(Long id, boolean lock) {
		return marcRelatorCodeDAO.getById(id, lock);
	}

	/**
	 * Get a count of the marc relator codes.
	 * 
	 * @see edu.ur.metadata.marc.MarcRelatorCodeService#getCount()
	 */
	public Long getCount() {
		return marcRelatorCodeDAO.getCount();
	}

	/**
	 * Save the marc relator code
	 * 
	 * @see edu.ur.metadata.marc.MarcRelatorCodeService#save(edu.ur.metadata.marc.MarcRelatorCode)
	 */
	public void save(MarcRelatorCode entity) {
		marcRelatorCodeDAO.makePersistent(entity);
	}

	/**
	 * Set the data access for the marc relator code.
	 * 
	 * @param marcRelatorCodeDAO
	 */
	public void setMarcRelatorCodeDAO(MarcRelatorCodeDAO marcRelatorCodeDAO) {
		this.marcRelatorCodeDAO = marcRelatorCodeDAO;
	}
}
