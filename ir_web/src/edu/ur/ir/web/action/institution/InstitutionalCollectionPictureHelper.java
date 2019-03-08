/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.web.action.institution;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;

/**
 * Helper to deal with institutional collection picture 
 * 
 * @author Nathan Sarr
 *
 */
public class InstitutionalCollectionPictureHelper implements
Comparator<IrFile>{
	
	// this if when the page is first initalized
	public static final String INIT = "INIT";
	
	// get the next picture
	public static final String NEXT = "NEXT";
	
	// get the previous picture
	public static final String PREV = "PREV";
	
	/**  Logger for file upload */
	private static final Logger log = LogManager.getLogger(InstitutionalCollectionPictureHelper.class);
	
	/**
     * Get the next picture after the given location.
     * 
     * @param institutionalCollection - collection to get the picture out of
     * @param location - current location in the list
     * @param type - current state of the list
     * 
     * @return the picture file location or null if the institutional collection is null.
     */
    public PictureFileLocation nextPicture(InstitutionalCollection institutionalCollection, int location, String type )
    {
    	int currentLocation = location;
    	if( log.isDebugEnabled())
    	{
    		log.debug("Next Collection Picture");
    	}
	  
       
		if( institutionalCollection != null )
		{
		    LinkedList<IrFile> pictures = new LinkedList<IrFile>(institutionalCollection.getPictures());
		    
            // sort the pictures to assure order
            Collections.sort(pictures, this);
        
            // always put the primary picture first
            IrFile primaryPicture = institutionalCollection.getPrimaryPicture();
		    if(primaryPicture!= null)
		    {
		        pictures.addFirst(primaryPicture);
		    }
		    
            if( pictures != null && pictures.size() > 0 )
            {
        	    if( pictures.size() == 1 )
        	    {
        		    currentLocation = 0;
        	    }
        	    else if( type.equals(NEXT))
        	    {
        		    if( (currentLocation + 1) >= pictures.size())
        		    {
        			    currentLocation = 0;
        		    }
        		    else
        		    {
        			    currentLocation += 1;
        		    }
        	    }
        	    else if( type.equals(PREV))
        	    {
        		    if( (currentLocation -1 ) < 0 )
        		    {
        			    currentLocation = pictures.size() - 1;
        		    }
        		    else
        		    {
        			    currentLocation -= 1;
        		    }
        	    }
        	    
        	    IrFile irFile = pictures.get(currentLocation);
        	    
        	    return new PictureFileLocation(irFile, currentLocation, pictures.size());
            }
           
        }
		return null;
        
    }
    
    
    /**
     * Class to hold the picture file and the current location.
     * 
     * @author Nathan Sarr
     *
     */
    protected static class PictureFileLocation
    {
    	// picture file
    	private IrFile irFile;
    	
    	// current location
    	private int currentLocation;
    	
    	// number of pictures
    	private int numPictures;
    	
 
		protected PictureFileLocation(IrFile irFile, int currentLocation, int numPictures)
    	{
    		this.irFile = irFile;
    		this.currentLocation = currentLocation;
    		this.numPictures = numPictures;
    	}

		public IrFile getIrFile() {
			return irFile;
		}

		public int getCurrentLocation() {
			return currentLocation;
		}
		
	   	public int getNumPictures() {
			return numPictures;
		}
    }
    
    /**
     * Simple comparison to assure order.
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(IrFile o1, IrFile o2) {
    	if( o1.getId().equals(o2.getId())) return 0;
    	else if( o1.getId() > o2.getId() ) return 1;
    	else  return -1;
    		
    }
	
}
