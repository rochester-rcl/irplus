package edu.ur.ir.groupspace;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.UniqueNameDAO;

/**
 * Data access Interface for dealing with group space information.
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupSpaceDAO extends CrudDAO<GroupSpace>, CountableDAO, UniqueNameDAO<GroupSpace>{}
