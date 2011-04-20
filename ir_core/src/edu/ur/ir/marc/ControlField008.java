package edu.ur.ir.marc;


import edu.ur.persistent.BasePersistent;

/**
 * Represents a marc 008 control field.
 * 
 * @author Nathan Sarr
 *
 */
public class ControlField008 extends BasePersistent {

	/** eclipse generated field*/
	private static final long serialVersionUID = 3476430177478441116L;
	
	// represents the rest of the data 00 - 34 character positions
	private String data;
	
	/**
	 * Default constructor
	 */
	public ControlField008()
	{
		char[] charData = new char[35];
		for (int index = 0; index < charData.length; index++ )
		{
			charData[index] = ' ';
		}
		data = new String(data); 
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	/**
	 * Represents the other data positions 00 - 35.  the 
	 * data must be 16 characters long exactly otherwise
	 * an IllegalStateException is thrown
	 * 
	 * @param otherData
	 */
	public void setData(char[] charData) {
		
		if( charData.length != 35 )
		{
			throw new IllegalStateException("Character data must be 35 characters long");
		}
		else
		{
			data = new String(charData);
		}
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += data == null ? 0 : data.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" data = " );
		sb.append(data);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ControlField008)) return false;

		final ControlField008 other = (ControlField008) o;

		if( ( data != null && !data.equals(other.getData()) ) ||
			( data == null && other.getData() != null ) ) return false;
		
		return true;
	}


}
