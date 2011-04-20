package edu.ur.ir.marc;

import edu.ur.persistent.BasePersistent;

/**
 * Represents the marc 06 control Field - Fixed-Length Data Elements - Additional Material Characteristics 
 * 
 * @author Nathan Sarr
 *
 */
public class ControlField006 extends BasePersistent{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 3794581961181282207L;

	// represents the type of material 06/00 character position
	private char formOfMaterial;
	
	// represents the rest of the data 01 - 17 character positions
	private String data;
	
	/**
	 * Default constructor
	 */
	public ControlField006()
	{
		char[] charData = new char[17];
		for (int index = 0; index < charData.length; index++ )
		{
			charData[index] = ' ';
		}
		data = new String(data); 
	}
	
	/**
	 * Represents the type of material 06/00 position.
	 * 
	 * @return
	 */
	public char getFormOfMaterial() {
		return formOfMaterial;
	}

	/**
	 * Represents the type of material 06/00 position.
	 * 
	 * @param formOfMaterial
	 */
	public void setFormOfMaterial(char formOfMaterial) {
		this.formOfMaterial = formOfMaterial;
	}

	/**
	 * Represents the other data positions 01 - 17
	 * 
	 * @return
	 */
	public String getData() {
		return new String(data);
	}

	/**
	 * Represents the other data positions 01 - 17.  the 
	 * data must be 16 characters long exactly otherwise
	 * an IllegalStateException is thrown
	 * 
	 * @param otherData
	 */
	public void setData(char[] charData) {
		
		if( charData.length != 17 )
		{
			throw new IllegalStateException("Character data must be 16 characters long");
		}
		else
		{
			data = new String(charData);
		}
	}

	
	
	

}
