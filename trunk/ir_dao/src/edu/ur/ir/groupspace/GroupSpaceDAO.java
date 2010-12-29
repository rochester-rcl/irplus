package edu.ur.ir.groupspace;

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.UniqueNameDAO;
import edu.ur.order.OrderType;

/**
 * Data access Interface for dealing with group space information.
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupSpaceDAO extends CrudDAO<GroupSpace>, CountableDAO, UniqueNameDAO<GroupSpace>
{
	/**
	 * Get the list of group spaces.
	 * 
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param sortType - Order (Desc/Asc) 
	 * 
	 * @return list of groupspaces found.
	 */
	public List<GroupSpace> getGroupspacesNameOrder(int rowStart, int numberOfResultsToShow, OrderType orderType);
}
