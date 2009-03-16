package edu.ur.ir.web.action.institution;


import edu.ur.ir.institution.InstitutionalItem;

/**
 * This class will generate a web url for a versioned institutional item
 * that looks something like the following:
 * 
 * https://urresearch3.lib.rochester.edu/institutionalPublicationPublicView.action?institutionalItemId=5002&versionNumber=1
 * @author Nathan Sarr
 *
 */
public class InstitutionalItemVersionUrlGenerator 
{
	
	/** should already contain the trailing slash(/) */
	private String baseWebPath;
	

	/** action to view the institutional publication */
	private String action = "institutionalPublicationPublicView.action?";
	
	/** item id identifier  */
	private String itemIdIdentifier = "institutionalItemId=";
	
	/** version number identifier  */
	private String versionNumberIdentifier = "versionNumber=";
	
	/**
	 * Create the url for the institutional item version.
	 * 
	 * @param version
	 * @return
	 */
	public String createUrl(InstitutionalItem institutionalItem,  int version)
	{
		String url = baseWebPath;
		url = baseWebPath + action + itemIdIdentifier + institutionalItem.getId() + versionNumberIdentifier + version;
		
		return url;
	}
	
	public String getBaseWebPath() {
		return baseWebPath;
	}


	public void setBaseWebPath(String baseWebPath) {
		this.baseWebPath = baseWebPath;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getItemIdIdentifier() {
		return itemIdIdentifier;
	}

	public void setItemIdIdentifier(String itemIdIdentifier) {
		this.itemIdIdentifier = itemIdIdentifier;
	}

	public String getVersionNumberIdentifier() {
		return versionNumberIdentifier;
	}

	public void setVersionNumberIdentifier(String versionNumberIdentifier) {
		this.versionNumberIdentifier = versionNumberIdentifier;
	}


}
