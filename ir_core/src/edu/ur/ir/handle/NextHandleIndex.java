package edu.ur.ir.handle;

/**
 * This interface is guaranteed to return the next unique handle 
 * index value so long as it is the only object used to return
 * index values.
 * 
 * @author Nathan Sarr
 *
 */
public interface NextHandleIndex {
	
	/**
	 * @return
	 */
	public Long nextIndex();

}
