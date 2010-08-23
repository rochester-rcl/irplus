package edu.ur.ir.item;

/**
 * Class to help with counting for a content type.
 * 
 * @author Nathan Sarr
 *
 */
public class ContentTypeCount {
	
	/** content type  */
	private ContentType contentType;
	
	/** count value for the content type */
	private Long count;
	
	/**
	 * Create a count for content types..
	 * 
	 * @param contentType - content type
	 * @param count - count value
	 */
	public ContentTypeCount(ContentType contentType, Long count)
	{
		this.count = count;
		this.contentType = contentType;
	}
	
	/**
	 * Get the content type.
	 * 
	 * @return the content type
	 */
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * Get the count for this content type count.
	 * 
	 * @return count for the content type
	 */
	public Long getCount() {
		return count;
	}

}
