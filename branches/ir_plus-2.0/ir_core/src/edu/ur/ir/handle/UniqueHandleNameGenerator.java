package edu.ur.ir.handle;

import java.io.Serializable;

/**
 * This interface is guaranteed to return the next unique handle 
 * value so long as it is the only object used to return
 * values.
 * 
 * @author Nathan Sarr
 *
 */
public interface UniqueHandleNameGenerator extends Serializable{
	
	/**
	 * Gets the next unique handle name.
	 * 
	 * @return next unique handle name
	 */
	public String nextName();

}
