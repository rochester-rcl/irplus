/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.ir.web.action.user;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.importer.BadMarcFileException;
import edu.ur.ir.importer.MarcFileToVersionedItemImporter;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action to import the selected marc records.
 * 
 * @author Nathan Sarr
 *
 */
public class ImportMarcRecords extends ActionSupport implements UserIdAware{
	
	// eclipse generated id
	private static final long serialVersionUID = -7893338962235179048L;

	// User trying to upload the file */
	private Long userId;
	
	// actual set of files uploaded */
	private File file;
	
	// id of the collection to place the records in.
	private Long parentCollectionId = 0l;
	
	// parent collection to upload the files to
	private PersonalCollection parentCollection;
	
	// marc personal file importer service.
	private MarcFileToVersionedItemImporter marcFileToVersionedItemImporter;
	
	// user publishing file systerm service
    private UserPublishingFileSystemService userPublishingFileSystemService;
    
    // service to deal with user information
    private UserService userService;

	//  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(ImportMarcRecords.class);
	
	private List<VersionedItem> items;
	
	/**
	 * Uploads a new image to the system.
	 * 
	 * @return
	 * @throws IllegalFileSystemNameException 
	 * @throws NoIndexFoundException 
	 * @throws IOException 
	 * @throws BadMarcFileException 
	 */
	public String uploadMarcFile() throws IllegalFileSystemNameException, NoIndexFoundException, IOException, BadMarcFileException
	{
		log.debug("upload marc file");
				
		IrUser user = userService.getUser(userId, false);
		if( file != null && file.exists() )
		{
			try
			{
			    items = marcFileToVersionedItemImporter.importMarc(file, user);
			    log.debug("importing " + items.size() + " items ");
				PersonalCollection personalCollection = userPublishingFileSystemService.getPersonalCollection(parentCollectionId, false);

		        if( personalCollection == null )
			    {
			        for(VersionedItem item : items)
			        {
			        	user.createRootPersonalItem(item);
			        }
			    }
			    else if(personalCollection.getOwner().getId().equals(userId))
			    {
			    	for(VersionedItem item : items)
			        {
			    	     personalCollection.addVersionedItem(item);
			        }
			    }
			    else
			    {
				    return "accessDenied";
			    }
			}
			catch(BadMarcFileException bmfe)
			{
			    return "importFailed";	
			}
	        finally
	        {
		        file.delete();
	        }
		}
		else
		{
			if( file == null )
			{
				log.debug("file is null ");
			}
			else
			{
			    log.debug("File does not exist");
			}
		}
	    
	    return SUCCESS;
	}
	
	/**
	 * Get the list of versioned items produced.
	 * 
	 * @return
	 */
	public List<VersionedItem> getItems()
	{
		return items;
	}
	
	/**
	 * View the upload page.
	 * 
	 * @return
	 */
	public String viewMarcFileUploadPage()
	{
		if( parentCollectionId != null && parentCollectionId > 0 )
		{
			parentCollection = userPublishingFileSystemService.getPersonalCollection(parentCollectionId, false);
			if( !parentCollection.getOwner().getId().equals(userId))
			{
				return "accessDenied";
			}
		}
		
		return SUCCESS;
	}
	
	
	/**
	 * Get the user id uploading the image.
	 * 
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Set the user id uploading the image.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}


	/**
	 * Set the file to be added.
	 * 
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	
	/**
	 * Set the marc personal file importer.
	 * 
	 * @param marcPersonalFileImporter
	 */
	public void setMarcFileToVersionedItemImporter(
			MarcFileToVersionedItemImporter marcPersonalFileImporter) {
		this.marcFileToVersionedItemImporter = marcPersonalFileImporter;
	}

	/**
	 * Set the user publishing file system service.
	 * 
	 * @param userPublishingFileSystemService
	 */
	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}
	
	/**
	 * Get the parent collection id.
	 * 
	 * @return
	 */
	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	/**
	 * Set the parent collection id.
	 * 
	 * @param parentCollectionId
	 */
	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}
	
	/**
	 * Get the parent collection.
	 * 
	 * @return
	 */
	public PersonalCollection getParentCollection() {
		return parentCollection;
	}
	
	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
