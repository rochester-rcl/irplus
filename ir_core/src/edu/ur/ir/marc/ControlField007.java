package edu.ur.ir.marc;

import edu.ur.persistent.BasePersistent;

/**
 * Represents the 007 field data.  The first field defines the 
 * category of the material - the other data is dependent on this 
 * field.
 * 
 * @author Nathan Sarr
 *
 */
public class ControlField007 extends BasePersistent {
	
	/** eclipse generated id */
	private static final long serialVersionUID = 6889434029710158061L;

	private char categoryOfMaterial;
	
	private String data;
	
	public ControlField007()
	{
		char[] charData = new char[21];
		for (int index = 0; index < charData.length; index++ )
		{
			charData[index] = '|';
		}
		data = new String(data); 
	}
	
	/**
	 * Category of the material - 00 position
	 * @return
	 */
	public char getCategoryOfMaterial() {
		return categoryOfMaterial;
	}

	/**
	 * Category of the material - 00 position
	 * 
	 * @param categoryOfMaterial
	 */
	public void setCategoryOfMaterial(char categoryOfMaterial) {
		this.categoryOfMaterial = categoryOfMaterial;
	}

	/**
	 * Other data material positions 01 - 22
	 * 
	 * @return
	 */
	public String getData() {
		return data;
	}

	/**
	 * Represents the other data positions 01 - 17.  the 
	 * data must be 16 characters long exactly otherwise
	 * an IllegalStateException is thrown
	 * 
	 * @param otherData
	 */
	public void setData(char[] charData) {
		
		if( charData.length != 21 )
		{
			throw new IllegalStateException("Character data must be 16 characters long");
		}
		else
		{
			data = new String(charData);
		}
	}

}
