package edu.ur.ir.web.action.institution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemFileSecurityService;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.util.WebBrowserFileViewerHelper;

/**
 * Allow a user to play an institutional item file.
 * 
 * @author Nathan Sarr
 *
 */
public class PlayInstitutionalItemFile extends ActionSupport implements UserIdAware{
	
	/* serial version id  */
	private static final long serialVersionUID = 5841849019381926633L;
	
	/*  Logger for file upload */
	private static final Logger log = LogManager.getLogger(PlayInstitutionalItemFile.class);

	// id of the user viewing the file
	private Long userId;
	
	/*  File to download */
	private Long itemFileId;
	
	/* generic item */
    private GenericItem genericItem;
    
	/* Id of item version */
	private Long versionNumber;
    


	/* Id of the institutional item being viewed.  */
	private Long institutionalItemId;
   
	/* item that should contain the file */
	private Long itemId;

    /* Service for items */
    private ItemService itemService;
	
	/* User service */
	private UserService userService; 
	
	/* Item file security service */
	private ItemFileSecurityService itemFileSecurityService; 
	
	/* file to be played */
	ItemFile itemFile;
	
	/** file types that can be opened by the browser */
	private WebBrowserFileViewerHelper webBrowserFileViewerHelper;
	
	
	/**
	 * Set the web frowser file viewer helper.
	 * 
	 * @param webBrowserFileViewerHelper
	 */
	public void setWebBrowserFileViewerHelper(
			WebBrowserFileViewerHelper webBrowserFileViewerHelper) {
		this.webBrowserFileViewerHelper = webBrowserFileViewerHelper;
	}



	/**
	 * Loads the institutional item.
	 * 
	 * Prepare for action
	 */
	public String execute(){
		
	    log.debug("Trying to play file for the user");
	    
	    if(!webBrowserFileViewerHelper.getMediaPlayerEnabled()){
	    	return "player_not_enabled";
	    }
	    
        if (itemFileId == null) {
        	log.error("file id is null");
            return "not_found";
        }
        
        if( itemId == null)
        {
        	log.error("item id is null");
        	return "not_found";
        }
         
        genericItem = itemService.getGenericItem(itemId, false);
 
        if( genericItem == null)
        {
        	log.debug("Item  is null");
        	return "not_found";
        }
        
        /* item file to be played */
        itemFile = genericItem.getItemFile(itemFileId);
 
        if( itemFile == null)
        {
        	log.error("Item file is null");
        	return "not_found";
        }
        
        IrUser user = null;
        
        if (userId != null) {
        	user = userService.getUser(userId, false);
        	log.debug(" User " + user + " trying to download file ");
        }
        
         // Check if file can be downloaded by user
        if (genericItem.isPubliclyViewable() && !genericItem.isEmbargoed() && itemFile.isPublic() ) {
        	log.debug("publicly viewable = " + genericItem.isPubliclyViewable() + " is embargoed " + 
        			genericItem.isEmbargoed() +
        			" item file is public = " + itemFile.isPublic());
        	return SUCCESS;
        }
        else if ( user != null)
        {
        	log.debug("is owner = " + genericItem.getOwner().equals(user) + " has permissions " + 
        			itemFileSecurityService.hasPermission(itemFile, user, ItemFileSecurityService.ITEM_FILE_READ_PERMISSION) +
        			" is admin = " + user.hasRole(IrRole.ADMIN_ROLE));
        	if( genericItem.getOwner().equals(user) || 
            	(itemFileSecurityService.hasPermission(itemFile, user, ItemFileSecurityService.ITEM_FILE_READ_PERMISSION) > 0) || 
            	user.hasRole(IrRole.ADMIN_ROLE)
               )
        	{
        		return SUCCESS;
        	}
        	else
        	{
        		log.debug("User does not have access");
            	return INPUT;
        	}
        }
        else
        {
        	log.debug("File is private.");
        	return INPUT;
        }
 	}
	
	@Override
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public void setItemFileId(Long itemFileId) {
		this.itemFileId = itemFileId;
	}
	
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	
	public String getMediaPlayerKey(){
		return webBrowserFileViewerHelper.getMediaPlayerKey();
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setItemFileSecurityService(
			ItemFileSecurityService itemFileSecurityService) {
		this.itemFileSecurityService = itemFileSecurityService;
	}

	public GenericItem getItem() {
		return genericItem;
	}
	

	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}
	
	public ItemFile getItemFile() {
		return itemFile;
	}
	
	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}


}
