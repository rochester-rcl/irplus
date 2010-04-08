package edu.ur.ir.item;

import edu.ur.ir.person.ContributorType;

/**
 * This is a class to get counts of how many
 * times a particular type is used in all generic
 * items within the system.  This is important for  checking
 * to see if particular metadata classifications can be deleted.
 * 
 * 
 * @author Nathan Sarr
 *
 */
public interface GenericItemMetadataCounterService {
	
	/**
	 * Get the count of items this extent type is used by
	 * 
	 * @param extentType - extent type being used
	 * @return - count of items this extent type is used by.
	 */
	public Long getExtentTypeCount(ExtentType extentType);
	
	/**
	 * Get a count of items this identifier is used by.
	 * 
	 * @param identifierType - identifier type being used.
	 * @return - count of items this identifier is used by..
	 */
	public Long getIdentiferTypeCount(IdentifierType identifierType);
	
	/**
	 * Get a count of items this language type is used by.
	 * 
	 * @param languageType - language type to check for.
	 * @return - count of items this language type is used by.
	 */
	public Long getLanguageTypeCount(LanguageType languageType);
	
	/**
	 * Get a count of items this content type is used by.
	 * 
	 * @param contentType - content type to check for
	 * @return count of items this content type is used by.
	 */
	public Long getContentTypeCount(ContentType contentType);
	
	/**
	 * Get a count of items this contributor type is used by.
	 * 
	 * @param contibutorType - contributor type to check for.
	 * @return - count of items this contributor type is used by.
	 */
	public Long getContributorTypeCount (ContributorType contibutorType);
	
	/**
	 * Get a count of items this sponsor is used by 
	 *  
	 * @param sponsor - sponsor to check for
	 * @return - count of items this sponsor is used by 
	 */
	public Long getSponsorCount(Sponsor sponsor);
	
	/**
	 * Get a count of items this publisher is used by
	 * 
	 * @param publisher - publisher to check for 
	 * @return - count of items this publisher is used by
	 */
	public Long getPublisherCount(Publisher publisher);
	
	/**
	 * Get a count of items this series is used by 
	 * 
	 * @param series - series to check for
	 * @return - count of items this series is used by 
	 */
	public Long getSeriesCount(Series series);
	
}
