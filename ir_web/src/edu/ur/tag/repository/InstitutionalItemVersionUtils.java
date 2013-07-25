package edu.ur.tag.repository;

import java.text.SimpleDateFormat;

import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.FirstAvailableDate;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.OriginalItemCreationDate;
import edu.ur.ir.item.PublishedDate;

public class InstitutionalItemVersionUtils {
	
	/**
	 * Get the best date to use for google scholar information.  Will try dates in the
	 * following order.  Uses the format yyyy/MM/dd if all are available otherwise only
	 * year is returned.
	 * 
	 * 1.  External Publication Date
	 * 2.  First Available Date
	 * 3.  Original Creation Date
	 * 4.  Date of deposit
	 * 
	 * @param item
	 * @return the date to be used for google scholar otherwise empty string
	 */
	public static String getGoogleScholarDate(InstitutionalItemVersion versionedItem){
		
		GenericItem item = versionedItem.getItem();
		String date = "";
		ExternalPublishedItem externalPub = item.getExternalPublishedItem();
		PublishedDate publishedDate = null;
		FirstAvailableDate firstAvailableDate = item.getFirstAvailableDate();
		OriginalItemCreationDate originalCreationDate = item.getOriginalItemCreationDate();
		
		if(  externalPub != null )
	    {
			 publishedDate= externalPub.getPublishedDate();	
		}
		
		if( publishedDate != null && publishedDate.getYear() != 0 )
		{
			date = date + publishedDate.getYear();
			if( publishedDate.getMonth() != 0 && publishedDate.getDay() != 0)
			{
				date = date + "/" + publishedDate.getMonth() + "/" + publishedDate.getDay();
			}
		}
		else if ( firstAvailableDate != null && firstAvailableDate.getYear() != 0)
		{
			date = date + firstAvailableDate.getYear();
			if( firstAvailableDate.getMonth() != 0 && firstAvailableDate.getDay() != 0)
			{
				date = date + "/" + firstAvailableDate.getMonth() + "/" + firstAvailableDate.getDay();
			}
		}
		else if ( originalCreationDate != null && originalCreationDate.getYear() != 0)
		{
			date = date + originalCreationDate.getYear();
			if( originalCreationDate.getMonth() != 0 && originalCreationDate.getDay() != 0)
			{
				date = date + "/" + originalCreationDate.getMonth() + "/" + originalCreationDate.getDay();
			}
		}
		else if( versionedItem.getDateOfDeposit() != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			date = sdf.format(versionedItem.getDateOfDeposit());
		}
		
		return date;
		
	}

}
