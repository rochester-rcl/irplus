package edu.ur.ir.user;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;

/**
 * Interface for data access dealing with personal file delete records.
 * 
 * @author Nathan Sarr
 *
 */
public interface PersonalFileDeleteRecordDAO extends CountableDAO, 
CrudDAO<PersonalFileDeleteRecord>
{
	
	/**
	 * Delete all personal file delete records.  This should be used with EXTREAM CAUTION.  This 
	 * deletes all personal file delete records.
	 * 
	 * 
	 * @return
	 */
	public int deleteAll();
}
