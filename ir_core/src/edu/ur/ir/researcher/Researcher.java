/**  
   Copyright 2008 University of Rochester

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

package edu.ur.ir.researcher;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.user.IrUser;
import edu.ur.order.AscendingOrderComparator;
import edu.ur.persistent.BasePersistent;
import edu.ur.simple.type.AscendingNameComparator;

/**
 * A user who does research and contributes a large amount
 * of information to the system.
 * 
 * 
 * @author Nathan Sarr
 * @author Sharmila Ranganathan
 *
 */
public class Researcher extends BasePersistent{

	/** Generated serial id. */
	private static final long serialVersionUID = 1391737909500803512L;
	
	/** Logger */
	private static final Logger log = Logger.getLogger(Researcher.class);
	
	/** Title of the Research */
	private String title;
	
	/** Campus the researcher is in */
	private String campusLocation;
	
	/** Phone number of researcher*/
	private String phoneNumber;
	
	/** Email of the researcher */
	private String email;
	
	/** Fax number of researcher */
	private String fax;
	
	/**  Logo used for the collection */
	private Set<IrFile> pictures = new HashSet<IrFile>();
	
	/** main picture to be shown as a thumbnail */
	private IrFile primaryPicture;
	
	/** Research interest for the researcher */
	private String researchInterest;
	
	/** Teaching interest for the researcher */
	private String teachingInterest;

	/** Keywords to search for the researcher */
	private String keywords;
	
	/** User */
	private IrUser user;
	
	/** Indicates whether the researcher page is publicly visible or hidden */
	private boolean isPublic = false;

	/** Field the researcher is in. */
	private Set<Field> fields = new HashSet<Field>();

	/** Root folders of the researcher */
	private Set<ResearcherFolder> rootFolders  = new HashSet<ResearcherFolder>();
	
	/** Root files of the researcher */
	private Set<ResearcherFile> rootFiles = new HashSet<ResearcherFile>(); 
	
	/** Root publications of the researcher */
	private Set<ResearcherPublication> rootPublications  = new HashSet<ResearcherPublication>();

	/** Root institutional item of the researcher */
	private Set<ResearcherInstitutionalItem> rootInstitutionalItems = new HashSet<ResearcherInstitutionalItem>();
	
	/** Root links of the researcher */
	private Set<ResearcherLink> rootLinks = new HashSet<ResearcherLink>();
	
	/** List of personal links created by the researcher*/
	private List<ResearcherPersonalLink> personalLinks = new LinkedList<ResearcherPersonalLink>();

	
	/** Default constructor */
	Researcher() {}
	
	/**
	 * Constructor
	 * 
	 * @param user
	 */
	public Researcher(IrUser user) {
		this.user = user;
		this.isPublic = false;
	}
	
	/**
	 * Get a researcher file by name.
	 * 
	 * @param nameWithExtension
	 * @return the found researcher file
	 */
	public ResearcherFile getRootFileByNameExtension(String nameWithExtension)
	{
		for(ResearcherFile rf: rootFiles )
		{
			if( rf.getNameWithExtension().equalsIgnoreCase(nameWithExtension))
			{
				return rf;
			}
		}
		return null;
	}

	/**
	 * Get a researcher file by name.
	 * 
	 * @param nameWithExtension
	 * @return the found researcher file
	 */
	public ResearcherFile getRootFile(String name)
	{
		for(ResearcherFile rf: rootFiles )
		{
			if( rf.getName().equalsIgnoreCase(name))
			{
				return rf;
			}
		}
		return null;
	}
	
	/**
	 * Get a researcher publication by name.
	 * 
	 * @param name
	 * @return the found researcher publication 
	 */
	public ResearcherPublication getRootPublication(String name)
	{
		for(ResearcherPublication rp: rootPublications)
		{
			if( rp.getName().equalsIgnoreCase(name))
			{
				return rp;
			}
		}
		return null;
	}

	/**
	 * Get a researcher publication by name.
	 * 
	 * @param name
	 * @return the found researcher publication 
	 */
	public ResearcherInstitutionalItem getRootInstitutionalItem(String name)
	{
		for(ResearcherInstitutionalItem rp: rootInstitutionalItems)
		{
			if( rp.getName().equalsIgnoreCase(name))
			{
				return rp;
			}
		}
		return null;
	}
	
	/**
	 * Get a researcher link by name.
	 * 
	 * @param name
	 * @return the found researcher link
	 */
	public ResearcherLink getRootLink(String name)
	{
		for(ResearcherLink rl: rootLinks)
		{
			if( rl.getName().equalsIgnoreCase(name))
			{
				return rl;
			}
		}
		return null;
	}
	
	/**
	 * Get a researcher root folder by name.  The comparison
	 * is case insensitive.
	 * 
	 * @param name - name of the folder to return
	 * @return The folder if found otherwise null.
	 */
	public ResearcherFolder getRootFolder(String name)
	{
		for(ResearcherFolder f: rootFolders )
		{
			if( f.getName().equalsIgnoreCase(name))
			{
				return f;
			}
		}
		return null;
	}
	
	/**
	 * Adds an existing file to the root of this researcher.
	 * If the file was a child of an existing folder.  It
	 * is removed from the old folders file list.
	 * 
	 * @param file - to add as a root file
	 */
	public void addRootFile(ResearcherFile file) 
	{
		// file already part of this 
		if(!rootFiles.contains(file))
		{
		
		    ResearcherFolder folder = file.getParentFolder();
		    if( folder != null )
		    {
			    folder.removeResearcherFile(file);
			    file.setParentFolder(null);
		    }
		
		    rootFiles.add(file);
		}
	}
	
	/**
	 * Adds an existing folder to the root of this researcher.
	 * If the folder was a child of an existing folder.  It
	 * is removed from its parents list.
	 * 
	 * @param folder - to add as a root
	 * @throws DuplicateNameException 
	 */
	public void addRootFolder(ResearcherFolder folder) throws DuplicateNameException
	{
		//if this folder already is a root folder do not add it
		if( !rootFolders.contains(folder) )
		{
			if(getRootFolder(folder.getName()) != null)
			{
				throw new DuplicateNameException("Folder with name " + folder.getName() +
				" already exists in this folder", folder.getName() );
	        }
			ResearcherFolder parent = folder.getParent();
			parent.removeChild(folder);
			rootFolders.add(folder);
		}
	}
	
	/**
	 * Adds an existing publication to the root of this researcher.
	 * If the publication was a child of an existing folder.  It
	 * is removed from the old folder list.
	 * 
	 * @param publication - to add as a root publication
	 */
	public void addRootPublication(ResearcherPublication publication) 
	{
	
		ResearcherFolder folder = publication.getParentFolder();
		if( folder != null )
		{
			folder.removeResearcherPublication(publication);
			publication.setParentFolder(null);
		}
		
		rootPublications.add(publication);
	}

	/**
	 * Adds an existing institutional item to the root of this researcher.
	 * If the institutional item was a child of an existing folder.  It
	 * is removed from the old folder list.
	 * 
	 * @param institutionalItem - to add as a root institutional item
	 */
	public void addRootInstitutionalItem(ResearcherInstitutionalItem institutionalItem) 
	{
	
		ResearcherFolder folder = institutionalItem.getParentFolder();
		if( folder != null )
		{
			folder.removeResearcherInstitutionalItem(institutionalItem);
			institutionalItem.setParentFolder(null);
		}
		
		rootInstitutionalItems.add(institutionalItem);
	}
	
	/**
	 * Adds an existing link to the root of this researcher.
	 * If the item was a child of an existing folder.  It
	 * is removed from the old folder list.
	 * 
	 * @param link - to add as a root link
	 */
	public void addRootLink(ResearcherLink link) 
	{
	
		ResearcherFolder folder = link.getParentFolder();
		if( folder != null )
		{
			folder.removeResearcherLink(link);
			link.setParentFolder(null);
		}
		
		rootLinks.add(link);
	}
	
	/**
	 * Create a root researcher file for this researcher.  
	 * 
	 * @param f the ir file to add to the root.	
	 * @return the created researcher file
	 * 
	 */
	public ResearcherFile createRootFile(IrFile f, int versionNumber)
	{
		ResearcherFile rf = this.getFile(f);
		if( rf == null )
		{
		    rf = new ResearcherFile(this, f);
		    rf.setVersionNumber(versionNumber);
		    rootFiles.add(rf);
		}
		return rf;
	}
	

	
	/**
	 * Creates the root folder by name if it does not exist.  The 
	 * name should not be null or the name of an existing root
	 * folder for this researcher.
	 * 
	 * @param name of root folder to create.
	 * @return Created Folder if it does not already exist
	 * @throws DuplicateNameException 
	 * 
	 * @throws IllegalArgumentException if the name of the folder 
	 * already exists or the name is null.
	 */
	public ResearcherFolder createRootFolder(String name) throws DuplicateNameException
	{
		if( name == null)
		{
			throw new IllegalArgumentException("Name cannot be null");
		}
		
		if(getRootFolder(name) != null)
		{
			throw new DuplicateNameException("Folder with name " + name +
			" already exists ", name );
        }
		
		ResearcherFolder f = new ResearcherFolder(this, name);
		rootFolders.add(f);
		
		return f;
	}
	
	/**
	 * Creates the root publication if it does not exist.  The 
	 * name should not be null or the name of an existing root
	 * researcher publication for this researcher.
	 * 
	 * @param publication publication to add to this researcher.
	 * @return Created researcher publication if it does not already exist
	 */
	public ResearcherPublication createRootPublication(GenericItem publication, int versionNumber)
	{ 
		ResearcherPublication  researcherPublication = this.getPublication(publication);
		if( researcherPublication == null )
		{	
		    researcherPublication = new ResearcherPublication(this, publication, versionNumber);
	        rootPublications.add(researcherPublication);
		}
		return researcherPublication;
	}

	/**
	 * Creates the root Institutional Item if it does not exist.  The 
	 * name should not be null or the name of an existing root
	 * researcher publication for this researcher.
	 * 
	 * @param institutionalItem institutional Item to add to this researcher.
	 * @return Created researcher institutional Item if it does not already exist
	 */
	public ResearcherInstitutionalItem createRootInstitutionalItem(InstitutionalItem institutionalItem)
	{ 
		ResearcherInstitutionalItem researcherInstitutionalItem = getInstitutionalItem(institutionalItem);
		if( researcherInstitutionalItem == null )
		{
		    researcherInstitutionalItem = new ResearcherInstitutionalItem(this, institutionalItem);
	        rootInstitutionalItems.add(researcherInstitutionalItem);
		}
		return researcherInstitutionalItem;
	}
	
	/**
	 * Get the researcher institutional item based on the institutional item
	 * 
	 * @param institutionalItem - institutional item to check for
	 * @return - the researcher institutional item or null if not found
	 */
	public ResearcherInstitutionalItem getInstitutionalItem(InstitutionalItem institutionalItem)
	{
		for( ResearcherInstitutionalItem item : rootInstitutionalItems)
		{
			if(item.getInstitutionalItem().equals(institutionalItem))
			{
				return item;
			}
		}
		return null;
	}
	
	/**
	 * Get a publication based on the generic item.
	 * 
	 * @param publication - publication in the researcher publication
	 * @return the found researcher publication or null
	 */
	public ResearcherPublication getPublication(GenericItem publication)
	{
		for(ResearcherPublication pub : rootPublications)
		{
			if( pub.getPublication().equals(publication))
			{
				return pub;
			}
		}
		return null;
	}
	
	/**
	 * Get the researcher file based on the ir file.
	 * 
	 * @param f - IR file the researcher file should contain.
	 * @return - the found researcher file
	 */
	public ResearcherFile getFile(IrFile f)
	{
		for(ResearcherFile file : rootFiles)
		{
			if( file.getIrFile().equals(f ))
			{
				return file;
			}
		}
		
		return null;
	}
	
	/**
	 * Creates the root link if it does not exist.  The 
	 * name should not be null or the name of an existing root
	 * researcher link for this researcher.
	 * 
	 * @param url link to add to the researcher.
	 * @param name Name of the link
	 * @param description Description for the link
	 * 
	 * @return Created researcher link if it does not already exist
	 * 
	 */
	public ResearcherLink createRootLink(String url, String name, String description)
	{  
		ResearcherLink researcherLink = new ResearcherLink(this, url);
		researcherLink.setName(name);
		researcherLink.setDescription(description);
	    rootLinks.add(researcherLink);
		
		return researcherLink;
	}
	


	/**
	 * Get the researcher's fields
	 * 
	 * @return
	 */
	public Set<Field> getFields() {
		return fields;
	}

	/**
	 * Set the researcher's fields.
	 * 
	 * @param fields
	 */
	public void setFields(Set<Field> fields) {
		this.fields = fields;
	}

	/**
	 * Title Used by the researcher.
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Title used by the researcher
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("[Researcher id = ");
		sb.append(id);
		sb.append(" title = ");
		sb.append(title);
		sb.append(" field = ");
		sb.append(fields);
		sb.append("]");
		return sb.toString();
	}

    /**
     * Researcher Equals.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
    	if (this == o) return true;
		if (!(o instanceof Researcher)) return false;

		final Researcher other = (Researcher) o;

		if( ( user != null && !user.equals(other.getUser()) ) ||
			( user == null && other.getUser() != null ) ) return false;
		
		
		return true;
    }
    
    /**
     * Get the hash code.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
    	int hash = 0;
    	hash += user == null ? 0 : user.hashCode();
    	return hash;
    }

	/**
	 * Get the pictures for the researcher.  Returns an unmodifiable set.
	 * 
	 * @return - unmodifiable set of pictures
	 */
	public Set<IrFile> getPictures() {
		return Collections.unmodifiableSet(pictures);
	}

	/**
	 * Set the pictures for the researcher
	 * 
	 * @param pictures
	 */
	void setPictures(Set<IrFile> pictures) {
		this.pictures = pictures;
	}

	/**
	 * Get the primary picture for the researcher.
	 * 
	 * @return - primary picture for the researcher.
	 */
	public IrFile getPrimaryPicture() {
		return primaryPicture;
	}

	/**
	 * Set the primary picture for the researcher.
	 * 
	 * @param primaryPicture
	 */
	public void setPrimaryPicture(IrFile primaryPicture) {
		this.primaryPicture = primaryPicture;
	}    

	/**
	 * Add the picture to this researcher.
	 * 
	 * @param file - picture to add
	 */
	public void addPicture(IrFile file)
	{
		if( !pictures.contains(file))
		{
		    pictures.add(file);
		}
	}
	
	/**
	 * Get the picture with the specified id.  This also looks
	 * at the primary picture.
	 * 
	 * @param id - ir file id
	 * @return the found file otherwise null.
	 */
	public IrFile getPicture(Long id)
	{
		if(primaryPicture != null && primaryPicture.getId().equals(id))
		{
			return primaryPicture;
		}
		
		for(IrFile f : pictures)
		{
			if( f.getId().equals(id))
			{
				return f;
			}
		}
		return null;
	}
	
	
	/**
	 * Remove the picture from the set of pictures.
	 * 
	 * @param picture - picture to be removed
	 * @return - true if the picture is removed.
	 */
	public boolean removePicture(IrFile picture)
	{
		return pictures.remove(picture);
	}

	/**
	 * Remove the root file from this researcher.
	 * 
	 * @param researcherFile
	 * @return true if the file is removed.
	 */
	public boolean removeRootFile(ResearcherFile researcherFile)
	{
		return rootFiles.remove(researcherFile);
	}

	/**
	 * Remove the root publication from this researcher.
	 * 
	 * @param researcherPublication
	 * @return true if the publication is removed.
	 */
	public boolean removeRootPublication(ResearcherPublication researcherPublication)
	{
		return rootPublications.remove(researcherPublication);
	}

	/**
	 * Remove the root institutional item from this researcher.
	 * 
	 * @param researcherInstitutionalItem
	 */
	public boolean removeRootInstitutionalItem(ResearcherInstitutionalItem researcherInstitutionalItem)
	{
		return rootInstitutionalItems.remove(researcherInstitutionalItem);
	}
	
	/**
	 * Remove the root link from this researcher.
	 * 
	 * @param researcherLink
	 * @return true if the link is removed.
	 */
	public boolean removeRootLink(ResearcherLink researcherLink)
	{
		return rootLinks.remove(researcherLink);
	}

	/**
	 * Remove the root folder from this researcher.
	 * 
	 * @param researcherFolder
	 * @return true if the folder is removed.
	 */
	public boolean removeRootFolder(ResearcherFolder researcherFolder)
	{
		return rootFolders.remove(researcherFolder);
	}

	
	/**
	 * Get the researchers teaching interests.
	 * 
	 * @return
	 */
	public String getTeachingInterest() {
		return teachingInterest;
	}

	/**
	 * Set the researcher teaching instrests.
	 * 
	 * @param teachingInterest
	 */
	public void setTeachingInterest(String teachingInterest) {
		this.teachingInterest = teachingInterest;
	}

	/**
	 * Get the researcher interests.
	 * 
	 * @return - researcher insterests.
	 */
	public String getResearchInterest() {
		return researchInterest;
	}

	/**
	 * Set the researcher interests.
	 * 
	 * @param researchInterest
	 */
	public void setResearchInterest(String researchInterest) {
		this.researchInterest = researchInterest;
	}

	/**
	 * Get the user who owns this researcher.
	 * 
	 * @return
	 */
	public IrUser getUser() {
		return user;
	}

	/**
	 * Set the user who owns this researcher object.
	 * 
	 * @param user
	 */
	public void setUser(IrUser user) {
		this.user = user;
	}

	/**
	 * Get the campus location for the researcher.
	 * 
	 * @return campus location for the researcher.
	 */
	public String getCampusLocation() {
		return campusLocation;
	}

	/**
	 * Set the campus location for the researcher.
	 * 
	 * @param campusLocation
	 */
	public void setCampusLocation(String campusLocation) {
		this.campusLocation = campusLocation;
	}

	/**
	 * Get the phone number for a researcher.
	 * 
	 * @return phone number for the researcher.
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Set the phone number for a researcher.
	 * 
	 * @param phoneNumber
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Get the email for a researcher.
	 * 
	 * @return email for the researcher.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set the email for a researcher.
	 * 
	 * @param email for the researcher.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get the fax number for a researcher.
	 * 
	 * @return fax number for the researcher.
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * Set the fax number for a researcher.
	 * 
	 * @param fax
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * Determine if the researche rpage is public.
	 * 
	 * @return
	 */
	public boolean isPublic() {
		return isPublic;
	}

	/**
	 * Determines if the researcher page is public.
	 * 
	 * @param isPublic - if true the researcher page is public
	 */
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	/**
	 * Get the root folders for the researcher.  This is an
	 * unmodifiable set.
	 * 
	 * @return set of root folders.
	 */
	public Set<ResearcherFolder> getRootFolders() {
		return Collections.unmodifiableSet(rootFolders);
	}

	/**
	 * Set the root folders for a researcher.
	 * 
	 * @param rootFolders
	 */
	void setRootFolders(Set<ResearcherFolder> rootFolders) {
		this.rootFolders = rootFolders;
	}

	/**
	 * Get the root files for a researcher.  This is
	 * an unmodifiable set.
	 * 
	 * @return
	 */
	public Set<ResearcherFile> getRootFiles() {
		return Collections.unmodifiableSet(rootFiles);
	}

	/**
	 * Se tthe root files.
	 * 
	 * @param rootFiles
	 */
	void setRootFiles(Set<ResearcherFile> rootFiles) {
		this.rootFiles = rootFiles;
	}

	/**
	 * Return the set of root publications.  This is an unmodifiable set.
	 * 
	 * @return unmodifiable set of root publications
	 */
	public Set<ResearcherPublication> getRootPublications() {
		return Collections.unmodifiableSet(rootPublications);
	}

	/**
	 * Set the root publications.
	 * 
	 * @param rootPublications
	 */
	void setRootPublications(Set<ResearcherPublication> rootPublications) {
		this.rootPublications = rootPublications;
	}

	/**
	 * Get the root links for the researcher - this is an unmodifiable set.
	 * 
	 * @return set of root links
	 */
	public Set<ResearcherLink> getRootLinks() {
		return Collections.unmodifiableSet(rootLinks);
	}

	/**
	 * Set the root links for the researcher
	 * 
	 * @param rootLinks
	 */
	void setRootLinks(Set<ResearcherLink> rootLinks) {
		this.rootLinks = rootLinks;
	}
	
	/**
	 * Add a link to the set of links - order value is defaulted to last value
	 * 
	 * @param name - name to give the link
	 * @param url - url value
	 * 
	 * @return created link
	 * @throws DuplicateNameException - if the name already exists in the collection links
	 */
	public ResearcherPersonalLink addPersonalLink(String name, String url) throws DuplicateNameException
	{
		
		if( this.getPersonalLink(name) != null)
		{
			throw new DuplicateNameException("The link name already exists for link " + this.getPersonalLink(name), name );
		}
		ResearcherPersonalLink max = null;
		if( personalLinks.size() > 0 )
		{
		    max = Collections.max(personalLinks, new AscendingOrderComparator());
		}
		
		int order = 0;
		if( max != null )
		{
			order = max.getOrder() + 1;
		}
		ResearcherPersonalLink link = new ResearcherPersonalLink(this, name, url, order);
		personalLinks.add(link);
		return link;
	}
	
	/**
	 * Rename the specified link.
	 * 
	 * @param oldName
	 * @param newName
	 * @throws DuplicateNameException
	 */
	public void renamePersonalLink(String oldName, String newName) throws DuplicateNameException
	{
		if( oldName == null || newName == null || oldName.equals(newName))
		{
			return;
		}
		else
		{
			ResearcherPersonalLink link = getPersonalLink(newName);
			if( link != null )
			{
				throw new DuplicateNameException("The link name already exists for link " + link, newName );				
			}
			
			link = getPersonalLink(oldName);
			link.setName(newName);
		}
		
	}
	
	/**
	 * Get the link by the specified name, if it does not exist null is returned.
	 * 
	 * @param name - name of the link
	 * @return - the link found or null if not found
	 */
	public ResearcherPersonalLink getPersonalLink(String name)
	{
		for(ResearcherPersonalLink l : personalLinks)
		{
			if(l.getName().equals(name))
			{
				return l;
			}
		}
		return null;
	}
	
	/**
	 * Get the link by the specified id
	 * 
	 * @param name - name of the link
	 * @return - the link found or null if not found
	 */
	public ResearcherPersonalLink getPersonalLink(Long id)
	{
		for(ResearcherPersonalLink l : personalLinks)
		{
			if(l.getId().equals(id))
			{
				return l;
			}
		}
		return null;
	}
	
	/**
	 * Remove the specified link.
	 * 
	 * @param link - link to remove
	 * @return true if the link is removed
	 */
	public boolean removPersonalLink(ResearcherPersonalLink link)
	{
		boolean removed = personalLinks.remove(link);
		
		if( removed )
		{
		    
		   Collections.sort(personalLinks, new AscendingOrderComparator());
		   int index = 0;
		   for(ResearcherPersonalLink l : personalLinks)
		   {
			   l.setOrder(index);
			   index = index + 1;
		   }
		}
		
		return removed;
	}
	
    /**
     * Move the link to the specified order position.  If the order is less than 1
     * it is moved to the beginning of the list.  If the order is larger than the 
     * size of the list it is moved to the end.  The list is re-ordered
 
     * @param link
     * @param order
     * @return - true if the link is moved
     * 
     */
    public boolean movePersonalLink(ResearcherPersonalLink link, int position) 
    {
    	ResearcherPersonalLink theLink = this.getPersonalLink(link.getName());
    	if( theLink == null)
    	{
    		return false;
    	}
    	
    	int currentLinkPosition = theLink.getOrder();
    	
    	// if the position is greater than the size of the list put it at the end
		if(position > (personalLinks.size() - 1))
		{
			position = personalLinks.size() - 1;
		}
		
		if( position < 0 )
		{
			position = 0;
		}
		
		int change = 0;
		
		
		// moving up in list
		if( (currentLinkPosition - position) > 0 )
		{
			change = 1;
		}
		else // moving down in list
		{
			change = -1;
		}
		

		for(ResearcherPersonalLink aLink : personalLinks)
		{
		    if(!aLink.equals(link))
			{
		    	// move up
		    	if( change == 1)
		    	{
		    		if( aLink.getOrder() < currentLinkPosition && aLink.getOrder() >= position)
		    		{
		    			aLink.setOrder(aLink.getOrder() + change);
		    		}
		    	}
		    	else if( change == -1)
		    	{
		    		if( aLink.getOrder() <= position && aLink.getOrder() > currentLinkPosition)
		    		{
		    			aLink.setOrder(aLink.getOrder() + change);
		    		}
		    	}
			}
		    else
		    {
		    	aLink.setOrder(position);
		    }
		   
		}
		
		// order the links
    	Collections.sort(personalLinks , new AscendingOrderComparator());
    	return true;
    }
    
    /**
     * Get the personal links - this is an unmodifiable list.
     * 
     * @return the list of researcher personal links
     */
    public List<ResearcherPersonalLink> getPersonalLinks()
    {
    	return Collections.unmodifiableList(personalLinks);
    }

	/**
	 * Get root publication by id.
	 * 
	 * @param id - Id of the researcher root publication to return
	 * @return The Researcher Publication if found otherwise null.
	 */
	public ResearcherPublication getRootPublication(Long id)
	{
		for(ResearcherPublication researcherPublication: rootPublications)
		{
			if( researcherPublication.getId().equals(id))
			{
				return researcherPublication;
			}
		}
		return null;
	}

	/**
	 * Get root Institutional item by id.
	 * 
	 * @param id - Id of the researcher root Institutional item to return
	 * @return The Researcher Institutional item if found otherwise null.
	 */
	public ResearcherInstitutionalItem getRootInstitutionalItem(Long id)
	{
		for(ResearcherInstitutionalItem researcherInstitutionalItem: rootInstitutionalItems)
		{
			if( researcherInstitutionalItem.getId().equals(id))
			{
				return researcherInstitutionalItem;
			}
		}
		return null;
	}
	
	/**
	 * Get root link by id.
	 * 
	 * @param id - Id of the researcher root link to return
	 * @return The Researcher link if found otherwise null.
	 */
	public ResearcherLink getRootLink(Long id)
	{
		for(ResearcherLink researcherLink: rootLinks)
		{
			if( researcherLink.getId().equals(id))
			{
				return researcherLink;
			}
		}
		return null;
	}
	
	/**
	 * Creates JSON object
	 * 
	 * @return
	 */
	public JSONObject toJSONObject() {
		log.debug("call getJsonString Researcher");
		
		JSONObject jsonObj = new JSONObject();
		
		try {
			jsonObj.put("id",id);
			
			// Put sub folders
			JSONArray jsonSubFolders = new JSONArray();
			
			List <ResearcherFolder> folders = new LinkedList<ResearcherFolder> (rootFolders);
			Collections.sort( folders, new AscendingNameComparator());
			
		 	for(ResearcherFolder folder: folders) {
				jsonSubFolders.add(folder.toJSONObject());
			}

			jsonObj.put("folders",jsonSubFolders);	

			// Put files
			JSONArray jsonFiles = new JSONArray();
			List <ResearcherFile> files = new LinkedList<ResearcherFile> (rootFiles);
			Collections.sort( files, new AscendingNameComparator());
			for(ResearcherFile file: files) {
				jsonFiles.add(file.toJSONObject());
			}

			jsonObj.put("files",jsonFiles);	
			
			// Put publications
			JSONArray jsonPublications = new JSONArray();
			List <ResearcherPublication> publications = new LinkedList<ResearcherPublication> (rootPublications);
			Collections.sort( publications, new AscendingNameComparator());
			for(ResearcherPublication p: publications) {
				jsonPublications.add(p.toJSONObject());
			}

			jsonObj.put("publications",jsonPublications);	

			// Put institutional item
			JSONArray jsonInstitutionalItems = new JSONArray();
			List <ResearcherInstitutionalItem> institutionalItems = new LinkedList<ResearcherInstitutionalItem> (rootInstitutionalItems);
			Collections.sort( institutionalItems, new AscendingNameComparator());

			for(ResearcherInstitutionalItem i: institutionalItems) {
				jsonInstitutionalItems.add(i.toJSONObject());
			}

			jsonObj.put("institutionalItems",jsonInstitutionalItems);	

			
			// Put links
			JSONArray jsonLinks = new JSONArray();
			List <ResearcherLink> links = new LinkedList<ResearcherLink> (rootLinks);
			Collections.sort( links, new AscendingNameComparator());

			for(ResearcherLink link: links) {
				jsonLinks.add(link.toJSONObject());
			}

			jsonObj.put("links",jsonLinks);	

		} catch (Exception e) {
			 log.debug("jsonObj Exception::"+e.getMessage());
		}
		
		log.debug("jsonObj Researcher ::"+jsonObj);
		
		return jsonObj;
	}

	/**
	 * Get the root institutional items for the researcher.  This is 
	 * an unmodifiable set.
	 * 
	 * @return set of root insitutional items.
	 */
	public Set<ResearcherInstitutionalItem> getRootInstitutionalItems() {
		return Collections.unmodifiableSet(rootInstitutionalItems);
	}

	/**
	 * Set the root insitutional items 
	 * @param rootInstitutionalItems
	 */
	void setRootInstitutionalItems(
			Set<ResearcherInstitutionalItem> rootInstitutionalItems) {
		this.rootInstitutionalItems = rootInstitutionalItems;
	}

	/**
	 * Get the keywords for the researcher.
	 * 
	 * @return
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * Set the key words for this researcher.
	 * 
	 * @param keywords
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * Add a field to this researcher.
	 * 
	 * @param field
	 */
	public void addField(Field field)
	{
		fields.add(field);
	}
	
	/**
	 * Remove a field from this researcher.
	 * 
	 * @param field
	 * @return true if the field is removed.
	 */
	public boolean removeField(Field field)
	{
		return fields.remove(field);
	}

	/**
	 * Returns field names for indexing
	 * 
	 * @return
	 */
	public String getAllFieldNames() {
		
		StringBuffer fieldNames = new StringBuffer();
		
		for (Field f : fields) {
			fieldNames.append(f.getName());
			fieldNames.append("|");
		}
		
		return fieldNames.toString();
	}

	/**
	 * Remove all fields
	 */
	public void removeAllFields() {
		fields.clear();
	}


	

}
