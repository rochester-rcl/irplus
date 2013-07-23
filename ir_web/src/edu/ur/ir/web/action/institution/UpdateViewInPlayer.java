package edu.ur.ir.web.action.institution;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemService;

public class UpdateViewInPlayer extends ActionSupport implements Preparable{

	/** generated version id*/
	private static final long serialVersionUID = -2103332968950379073L;

	/**  Logger. */
	private static final Logger log = Logger.getLogger(UpdateViewInPlayer.class);
	
	/** Institutional Item holding the files */
	private GenericItem item;

	/** Item file */
	private ItemFile itemFile;
	
	/** id of the item file */
	private Long itemFileId;
	
	/** id of the item */
	private Long itemId;
	
	/** Indicates whether to the  Item file is publicaly viewable or not */
	private boolean playInViewer = false;
	
	/** Item service */
	private ItemService itemService;
	
	public void prepare() throws Exception {
		log.debug("Prepare for Item File permissions:: itemFileId = "+ itemFileId + "  item id=" + itemId);
		if (itemId != null) {
			item = itemService.getGenericItem(itemId, false);
			itemFile = item.getItemFile(itemFileId);
		} else if (itemFileId != null) {
			itemFile = itemService.getItemFile(itemFileId, false);
		}
	}
	
	/**
	 * Updates the view status for item file 
	 * 
	 * @return
	 */
	public String updateViewInPlayer() {
		itemFile.setCanViewInPlayer(playInViewer);
		itemService.saveItemFile(itemFile);
		return SUCCESS;
	}
	

	/**
	 * Set the item file id.
	 * 
	 * @param itemFileId
	 */
	public void setItemFileId(Long itemFileId) {
		this.itemFileId = itemFileId;
	}

	/**
	 * Set the item id.
	 * 
	 * @param itemId
	 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	/**
	 * Set the item service.
	 * 
	 * @param itemService
	 */
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}
	
	/**
	 * Flag to determine if the file can be played in the viewer
	 * 
	 * @return
	 */
	public boolean getPlayInViewer() {
		return playInViewer;
	}

	/**
	 * Flag to determine if the file can be played in the viewer.
	 * 
	 * @param playInViewer
	 */
	public void setPlayInViewer(boolean playInViewer) {
		this.playInViewer = playInViewer;
	}

}
