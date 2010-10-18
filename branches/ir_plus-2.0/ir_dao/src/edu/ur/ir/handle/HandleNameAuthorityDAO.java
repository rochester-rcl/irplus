package edu.ur.ir.handle;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.UniqueNameDAO;



/**
 * Data access for name authority 
 * @author Nathan Sarr
 *
 */
public interface HandleNameAuthorityDAO extends CountableDAO, CrudDAO<HandleNameAuthority>, 
UniqueNameDAO<HandleNameAuthority>{}
