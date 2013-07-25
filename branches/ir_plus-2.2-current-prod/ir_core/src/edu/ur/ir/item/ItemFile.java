/**  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  

package edu.ur.ir.item;

import edu.ur.ir.file.IrFile;
import edu.ur.persistent.CommonPersistent;


/**
 * Represents a link between an item and a particular ir file.
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class ItemFile extends CommonPersistent implements ItemObject {
	
	/** Generated id */
	private static final long serialVersionUID = -1898323099861772946L;

	/**  Item   */
	private GenericItem item;
	
	/**  IrFile for this item  */
	private IrFile irFile;
	
	/**  ordering of the file with respect to other item files */
	private int order;
	
	/** Type of the object */
	public static final String TYPE = "FILE";
	
	/** Version number of the IrFile */
	private int versionNumber;
	
	/** Indicates whether the file is publicaly viewable or not */
	private boolean isPublic = false;
	
	/**
     * Package protected constructor 
     */
    ItemFile(){}
    
    /**
     * Create a link between an item and item file.
     * 
     * @param item item to link 
     * @param irFile ir file to link
     */
    public ItemFile(GenericItem item, IrFile irFile)
    {
    	setItem(item);
    	setIrFile(irFile);
    }
    
    /**
	 * Get the type of the object
	 * 
	 * @return type
	 */
	public String getType() {
		return TYPE;
	}
	
	/**
	 * Get the item
	 * 
	 * @return
	 */
	public GenericItem getItem() {
		return item;
	}

	/**
	 * Set the item
	 * 
	 * @param item
	 */
	void setItem(GenericItem item) {
		this.item = item;
	}

	/**
	 * Order with respect to other item files
	 * 
	 * @return int value
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Order with respect to other item files
	 * 
	 * @param order
	 */
	public void setOrder(int order) {
		this.order = order;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Item File id = ");
		sb.append(id);
		sb.append(" item = ");
		sb.append(item);
		sb.append(" order = ");
		sb.append(order);
		sb.append(" irFile = ");
		sb.append(irFile);
		sb.append(" ]");
		return sb.toString();
	}
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ItemFile)) return false;

		final ItemFile other = (ItemFile) o;

		if( getItem() != null && other.getItem() == null) return false;
		if(!getItem().equals(other.getItem())) return false;
		
		if( getIrFile() != null && other.getIrFile() == null) return false;
		if(!getIrFile().equals(other.getIrFile())) return false;
		
		return true;
	}
	
	public int hashCode()
	{
		int value = 0;
		value += irFile != null ? irFile.hashCode() : 0;
		value += item != null ? item.hashCode() : 0;
		return value;
	}

	/**
	 * Get the ir file
	 * 
	 * @return
	 */
	public IrFile getIrFile() {
		return irFile;
	}

	/**
	 * Set the ir file
	 * 
	 * @param irFile
	 */
	public void setIrFile(IrFile irFile) {
		this.irFile = irFile;
	}

	/**
     * Get version number for the file
	 *
	 * @return version number
     */	
    public int getVersionNumber() {
		return versionNumber;
	}

	/**
     * Set version for the file
     *
     *@param versionNumber
     */
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
     * Get description for the file
	 *
	 * @return description
     */
	public String getDescription() {
		return description;
	}

	/**
     * Set description for the file
     *
     *@param description
     */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Determine if this file is public.
	 * 
	 * @return - true if this item file is public
	 */
	public boolean isPublic() {
		return isPublic;
	}
	
	/**
	 * Determine if the researcher page is public.
	 * 
	 * @return
	 */
	public boolean getIsPublic()
	{
		return isPublic;
	}

	/**
	 * Set this item file as public.
	 * 
	 * @param isPublic
	 */
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
}
