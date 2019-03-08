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

package edu.ur.ir.web.action;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.repository.Repository;

/**
 * Helper to get the next random picture location.  Then allows to cycle 
 * in order
 * 
 * @author Nathan Sarr
 *
 */
public class RandomRepositoryPictureHelper implements Comparator<IrFile>, Serializable
{
	/** eclipse generated id */
	private static final long serialVersionUID = 1564716557466623811L;

	/**  Current picture location */
	private int currentRepositoryPictureLocation;
	
	/**  number of pictures for the repository*/
	private int numRepositoryPictures;
	
	/**  Ir file that should be shown. */
	private IrFile repositoryImageFile = null;
	
	// this if when the page is first initalized
	public static final String INIT = "INIT";
	
	// get the next picture
	public static final String NEXT = "NEXT";
	
	// get the previous picture
	public static final String PREV = "PREV";
	
	/**  Logger for file upload */
	private static final Logger log = LogManager.getLogger(RandomRepositoryPictureHelper.class);
	
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
    
    public IrFile getNextPicture(String type, Repository repository, int pictureLocation)
    {
    	if( log.isDebugEnabled())
    	{
    		log.debug("Next Repository Picture");
    	}
	  
       
		if( repository != null )
		{
		    LinkedList<IrFile> pictures = new LinkedList<IrFile>(repository.getPictures());
        
		    numRepositoryPictures = pictures.size();
            // sort the pictures to assure order
            Collections.sort(pictures, this);
        
            if( pictures != null && numRepositoryPictures > 0 )
            {
        	    if( pictures.size() == 1 )
        	    {
        	    	pictureLocation = 0;
        	    }
        	    else if ( type.equals(INIT))
        	    {
        		    Random random = new Random();
        		    pictureLocation = random.nextInt(numRepositoryPictures);
        	    }
        	    else if( type.equals(NEXT))
        	    {
        		    if( (pictureLocation + 1) >= numRepositoryPictures)
        		    {
        		    	pictureLocation = 0;
        		    }
        		    else
        		    {
        		    	pictureLocation += 1;
        		    }
        	    }
        	    else if( type.equals(PREV))
        	    {
        		    if( (pictureLocation -1 ) < 0 )
        		    {
        		    	pictureLocation = numRepositoryPictures - 1;
        		    }
        		    else
        		    {
        		    	pictureLocation -= 1;
        		    }
        	    }
        	    currentRepositoryPictureLocation = pictureLocation;
        	    repositoryImageFile = pictures.get(pictureLocation);
            }
           
        }
		
		return repositoryImageFile;
    }

	public int getCurrentRepositoryPictureLocation() {
		return currentRepositoryPictureLocation;
	}

	public void setCurrentRepositoryPictureLocation(
			int currentRepositoryPictureLocation) {
		this.currentRepositoryPictureLocation = currentRepositoryPictureLocation;
	}

	public int getNumRepositoryPictures() {
		return numRepositoryPictures;
	}

	public void setNumRepositoryPictures(int numRepositoryPictures) {
		this.numRepositoryPictures = numRepositoryPictures;
	}

	public IrFile getRepositoryImageFile() {
		return repositoryImageFile;
	}

	public void setRepositoryImageFile(IrFile repositoryImageFile) {
		this.repositoryImageFile = repositoryImageFile;
	}

}
