package edu.ur.ir.item;

import edu.ur.persistent.BasePersistent;

/**
 * Represents an item matched with a content type.
 * 
 * 
 * @author Nathan Sarr
 *
 */
public class ItemContentType extends BasePersistent{

	/** eclipse generated id */
	private static final long serialVersionUID = -2332309332192722116L;
	
	/** generic item */
	private GenericItem item;
	
	/** the content type  */
	private ContentType contentType;
	
	/** true indicates if this is the primary content type  */
	private boolean primary = false;

	/** Package protected constructor  */
	ItemContentType(){};
	
	/**
	 * Create an item content with the generic item and content type.
	 * 
	 * @param item
	 * @param contentType
	 */
	public ItemContentType(GenericItem item, ContentType contentType)
	{
		setItem(item);
		setContentType(contentType);
	}
	
	/**
	 * Get the generic item
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
	void setItem(GenericItem item)
	{
		this.item = item;
	}

	/**
	 * Get the content type.
	 * 
	 * @return content type
	 */
	public ContentType getContentType() {
		return contentType;
	}
	
	/**
	 * Set the content type.
	 * 
	 * @param contentType
	 */
	void setContentType(ContentType contentType)
	{
		this.contentType = contentType;
	}

	/**
	 * Set the indicator to determine if this is the primary content type.
	 * 
	 * @param primary
	 */
	void setPrimary(boolean primary) {
		this.primary = primary;
	}
	
	/**
	 * Returns true if this is the primary content type.
	 * 
	 * @return
	 */
	public boolean isPrimary()
	{
		return getPrimary();
	}
	
	/**
	 * Returns true if this is the primary content type.
	 * 
	 * @return
	 */
	public boolean getPrimary()
	{
		return primary;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += contentType == null ? 0 : contentType.hashCode();
		value += item == null ? 0 : item.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" contentType = " );
		sb.append(contentType);
		sb.append(" item = ");
		sb.append(item);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ItemContentType)) return false;

		final ItemContentType other = (ItemContentType) o;

		if( ( contentType != null && !contentType.equals(other.getContentType()) ) ||
			( item == null && other.getItem() != null ) ) return false;
		
		return true;
	}
	
	
}
