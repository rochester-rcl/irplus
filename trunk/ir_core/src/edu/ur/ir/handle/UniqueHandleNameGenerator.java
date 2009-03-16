package edu.ur.ir.handle;

/**
 * This interface is guaranteed to return the next unique handle 
 * value so long as it is the only object used to return
 * values.
 * 
 * @author Nathan Sarr
 *
 */
public interface UniqueHandleNameGenerator {
	
	/**
	 * Gets the next unique handle name.
	 * 
	 * @return next unique handle name
	 */
	public String nextName();

}
