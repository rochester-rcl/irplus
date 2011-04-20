package edu.ur.metadata.marc;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.UniqueNameDAO;

public interface MarcRelatorCodeDAO extends CountableDAO, 
CrudDAO<MarcRelatorCode>, UniqueNameDAO<MarcRelatorCode> {

	/**
	 * Get the marc relator code by relator code value.
	 * 
	 * @param relatorCode - value of the relator code
	 * @return - the the marc relator code if found otherwise null
	 */
	public MarcRelatorCode getByRelatorCode(String relatorCode);
}
