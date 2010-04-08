package edu.ur.tag.repository;

import edu.ur.ir.item.GenericItem;
import edu.ur.ir.person.PersonName;

/**
 * Functions to deal with generic items.
 * 
 * @author Nathan Sarr
 *
 */
public class GenericItemWebUtilFunctions {
	

	/**
	 * Determine if the user is a contributor of the item
	 * 
	 * @param genericItem - generic item to determine if the user is a contriburor
	 * @param personName - person name to check
	 * @return true if the person name is added as a contributor.
	 */
	public static boolean isContributor(GenericItem genericItem, PersonName personName)
	{
		boolean isContributor = false;
		if(genericItem != null)
		{
			if(genericItem.getContributor(personName) != null)
			{
				  isContributor = true;
			}
		}
		return isContributor;
	}

}
