package edu.ur.ir.item;

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.ListAllDAO;
import edu.ur.dao.UniqueNameDAO;
import edu.ur.order.OrderType;

/**
 * Interface for dealing with copyright statements.
 * 
 * @author Nathan Sarr
 *
 */
public interface CopyrightStatementDAO extends CountableDAO, 
CrudDAO<CopyrightStatement>, UniqueNameDAO<CopyrightStatement>, ListAllDAO{
	
	/**
	 * Get the list of copyright statements ordered by name.
	 * 
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param orderType - Order (Desc/Asc) 
	 * 
	 * @return list of copyright statements found.
	 */
	public List<CopyrightStatement> getCopyrightStatementsOrderByName(
			int rowStart, int numberOfResultsToShow,  OrderType orderType);

}
