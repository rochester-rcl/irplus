package edu.ur.dao;

import java.util.List;

/**
 * Interface to get a list of all items of a given type 
 * 
 * @author Nathan Sarr
 *
 */
public interface ListAllDAO
{
	/**
	 * Get all of the specified item.
	 * 
	 * @return list of all items for the given type.
	 */
	@SuppressWarnings("unchecked")
	public List getAll();
}
