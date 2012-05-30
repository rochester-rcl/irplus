/**  
   Copyright 2008-2012 University of Rochester

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

package edu.ur.ir.groupspace;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.order.AscendingOrderComparator;
import edu.ur.persistent.BasePersistent;

/**
 * Represents the grouping of information for a group space that can be shown 
 * to the public.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceProjectPage extends BasePersistent {
	
	// eclipse generated id
	private static final long serialVersionUID = -7955753514253869132L;

	// group workspace for this project page
	private GroupWorkspace groupWorkspace;

	//description of the project
	private String description;
	
	//indicates the page is public
	private boolean pagePublic;
	
	// pictures for the group workspace
	private Set<GroupWorkspaceProjectPageImage> images = new HashSet<GroupWorkspaceProjectPageImage>();

	/* date this record was created */
	private Timestamp createdDate;
	
	// list of members for the project page
	private Set<GroupWorkspaceProjectPageMember> members = new HashSet<GroupWorkspaceProjectPageMember>();
	
	// Root folders of the project page 
	private Set<GroupWorkspaceProjectPageFolder> rootFolders  = new HashSet<GroupWorkspaceProjectPageFolder>();
	
	// Root files of the group workspace 
	private Set<GroupWorkspaceProjectPageFile> rootFiles = new HashSet<GroupWorkspaceProjectPageFile>(); 
	
	// Root publications of the group workspace
	private Set<GroupWorkspaceProjectPagePublication> rootPublications  = new HashSet<GroupWorkspaceProjectPagePublication>();

	// Root institutional item of the group workspace page
	private Set<GroupWorkspaceProjectPageInstitutionalItem> rootInstitutionalItems = new HashSet<GroupWorkspaceProjectPageInstitutionalItem>();
	
	/** Root links of the researcher */
	private Set<GroupWorkspaceProjectPageFileSystemLink> rootLinks = new HashSet<GroupWorkspaceProjectPageFileSystemLink>();

	/**
     * Package protected constructor 
     */
    GroupWorkspaceProjectPage(){}
    
    /**
     * Constructor 
     * 
     * @param groupWorkspace - group workspace this project page belongs to.
     * 
     * @param name - name of the group project page.
     */
    GroupWorkspaceProjectPage(GroupWorkspace groupWorkspace)
    {
    	setGroupWorkspace(groupWorkspace);
    	
    	createdDate = new Timestamp(new Date().getTime());
    }
    
    /**
     * Add a member to the group workspace project page.  The member
     * must be a user in the group workspace otherwise an illegal state
     * exception is thrown.
     * 
     * @param groupWorkspaceUser - group workspace user to add.
     */
    public GroupWorkspaceProjectPageMember addMember(GroupWorkspaceUser groupWorkspaceUser)
    {
    	if( !groupWorkspace.getUsers().contains(groupWorkspaceUser) )
    	{
    		throw new IllegalStateException("user " + groupWorkspaceUser 
    				+ " is not a member of the group workspace " + groupWorkspace );
    	}
    	int order = members.size() + 1;
    	GroupWorkspaceProjectPageMember member = new GroupWorkspaceProjectPageMember(this, groupWorkspaceUser);
    	member.setOrder(order);
    	members.add(member);
    	return member;
    }
    
    /**
     * Add an image to the group workspace project page.  
     * 
     * @param irFile - image file
     */
    public GroupWorkspaceProjectPageImage addImage(IrFile irFile)
    {
    	
    	int order = images.size() + 1;
    	GroupWorkspaceProjectPageImage image = new GroupWorkspaceProjectPageImage(this, irFile);
    	image.setImageOrder(order);
    	images.add(image);
    	return image;
    }
    
    /**
     * Get the group worspace project image by the ir file id.
     * 
     * @param id - ir file id
     * @return the image or null if the image is not found
     */
    public GroupWorkspaceProjectPageImage getImageByIrFileId(Long id)
    {
    	for(GroupWorkspaceProjectPageImage image : images)
    	{
    		if( image.getImageFile().getId().equals(id))
    		{
    			return image;
    		}
    	}
    	return null;
    }
    
    /**
     * Get the group workspace project page image by id.
     * 
     * @param imageId - id of the project page image
     * @return 
     */
    public GroupWorkspaceProjectPageImage getById(Long imageId)
    {
    	for(GroupWorkspaceProjectPageImage image : images )
    	{
    		if( image.getId().equals(imageId))
    		{
    			return image;
    		}
    	}
    	
    	return null;
    }
    
    /**
     * Remove the image from the list of images.  This also updates the order 
     * of all images in the list.
     * 
     * @param image
     * @return true if the image was removed
     */
    public boolean remove(GroupWorkspaceProjectPageImage image)
    {
    	int order = image.getOrder();
    	boolean removed = false;
        removed = images.remove(image);	
        if( removed )
        {
        	for(GroupWorkspaceProjectPageImage anImage :images)
        	{
        	    if( anImage.getOrder() > order )
        	    {
        	    	anImage.setImageOrder(anImage.getOrder() - 1 );
        	    }
        	}
        }
        return removed;
    }
    
    
    /**
     * Get the group workspace project page member using the group workspace user.
     * 
     * @param groupWorkspaceUser - workspace user to get.
     * @return group workspace project page member.
     */
    public GroupWorkspaceProjectPageMember getMember(GroupWorkspaceUser groupWorkspaceUser)
    {
    	for(GroupWorkspaceProjectPageMember member : members)
    	{
    		if( member.getGroupWorkspaceUser().equals(groupWorkspaceUser))
    		{
    			return member;
    		}
    	}
    	return null;
    }
    
    /**
     * Remove the member from the list of members.  This also updates the order 
     * of all members in the list.
     * 
     * @param user
     */
    public boolean removeMember(GroupWorkspaceProjectPageMember member)
    {
    	int order = member.getOrder();
    	boolean removed = false;
        removed = members.remove(member);	
        if( removed )
        {
        	for(GroupWorkspaceProjectPageMember aMember : members)
        	{
        	    if( aMember.getOrder() > order )
        	    {
        	    	aMember.setOrder(aMember.getOrder() - 1 );
        	    }
        	}
        }
        return removed;
    }
    
    /**
     * Move a group workspace project page member up.
     * 
     * @param member
     */
    public void moveMemberUp(GroupWorkspaceProjectPageMember member)
    {
    	// member is in the list
    	// number of members is greater than one
    	// and is not the first one in the list already
    	if(members.contains(member) && (members.size() > 1) && (member.getOrder() != 1 ))
    	{
    		for(GroupWorkspaceProjectPageMember aMember : members)
    		{
    			if(aMember.getOrder() == (member.getOrder() - 1) )
    			{
    				aMember.setOrder(aMember.getOrder() + 1);
    			}
    		}
    		member.setOrder(member.getOrder() - 1);
    	}
    	
    }
    
    /**
     * Move a group workspace project page member up - sets the one above down in the list
     * 
     * @param member
     */
    public void moveMemberDown(GroupWorkspaceProjectPageMember member)
    {
    	// member is in the list
    	// number of members is greater than one
    	// and is not the last one in the list already
    	if(members.contains(member) && (members.size() > 1) && (member.getOrder() < members.size() ) )
    	{
    		for(GroupWorkspaceProjectPageMember aMember : members)
    		{
    			if(aMember.getOrder() == (member.getOrder() + 1) )
    			{
    				aMember.setOrder(aMember.getOrder() - 1);
    			}
    		}
    		member.setOrder(member.getOrder() + 1);
    	}
    }
    
    /**
     * Get the group workspace project page image by file id.
     * 
     * @param irFileId - id of the file
     * @return Group Workspace project apge image
     */
    public GroupWorkspaceProjectPageImage getImageByFileId(Long irFileId)
    {
    	for(GroupWorkspaceProjectPageImage image : images)
    	{
    		if( image.getImageFile().getId().equals(irFileId))
    		{
    			return image;
    		}
    	}
    	return null;
    }
    
    
    /**
     * Move a group workspace project page image up.
     * 
     * @param member
     */
    public void moveImageUp(GroupWorkspaceProjectPageImage image)
    {
    	// image is in the list
    	// number of images is greater than one
    	// and is not the first one in the list already
    	if(images.contains(image) && (images.size() > 1) && (image.getOrder() != 1 ))
    	{
    		for(GroupWorkspaceProjectPageImage anImage : images)
    		{
    			if(anImage.getOrder() == (image.getOrder() - 1) )
    			{
    				anImage.setOrder(anImage.getOrder() + 1);
    			}
    		}
    		image.setOrder(image.getOrder() - 1);
    	}
    	
    }
    
    /**
     * Move a group workspace project page image down - sets the one above down in the list
     * 
     * @param member
     */
    public void moveImageDown(GroupWorkspaceProjectPageImage image)
    {
    	// image is in the list
    	// number of images is greater than one
    	// and is not the last one in the list already
    	if(images.contains(image) && (images.size() > 1) && (image.getOrder() < images.size() ) )
    	{
    		for(GroupWorkspaceProjectPageImage anImage : images)
    		{
    			if(anImage.getOrder() == (image.getOrder() + 1) )
    			{
    				anImage.setOrder(anImage.getOrder() - 1);
    			}
    		}
    		image.setOrder(image.getOrder() + 1);
    	}
    }
    
    
	/**
	 * Get the description.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Get the group workspace.
	 * 
	 * @return
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}

	/**
	 * Set the group workspace this project page is for.
	 * 
	 * @param groupWorkspace
	 */
	void setGroupWorkspace(GroupWorkspace groupWorkspace) {
		this.groupWorkspace = groupWorkspace;
	}
	
    /**
     * Get if this page is public.
     * 
     * @return true if the page is public
     */
    public boolean getPagePublic() {
		return pagePublic;
	}
    
    /**
     * Determine if this page is public
     * 
     * @return
     */
    public boolean isPublic()
    {
    	return pagePublic;
    }

	/**
	 * If Set to true if the page is public.
	 * 
	 * @param pagePublic
	 */
	public void setPagePublic(boolean pagePublic) {
		this.pagePublic = pagePublic;
	}
	
	/**
	 * Get the pictures 
	 * 
	 * @return
	 */
	public Set<GroupWorkspaceProjectPageImage> getImages() {
		return Collections.unmodifiableSet(images);
	}

	/**
	 * Set the pictures for the group project page.
	 * 
	 * @param pictures
	 */
	void setImages(Set<GroupWorkspaceProjectPageImage> pictures) {
		this.images = pictures;
	}
	
	/**
	 * Hash code is based on the name of
	 * the group space
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value +=  getGroupWorkspace().getLowerCaseName() == null ? 0 : getGroupWorkspace().getLowerCaseName().hashCode();
		return value;
	}
	
	/**
	 * Equals is tested based on name ignoring case 
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupWorkspaceProjectPage)) return false;

		final GroupWorkspaceProjectPage other = (GroupWorkspaceProjectPage) o;

		if( ( getGroupWorkspace().getLowerCaseName() != null && !getGroupWorkspace().getLowerCaseName().equalsIgnoreCase(other.getGroupWorkspace().getLowerCaseName()) ) ||
			( getGroupWorkspace().getLowerCaseName() == null && other.getGroupWorkspace().getLowerCaseName() != null ) ) return false;

		return true;
	}
	
	/**
	 * Get the date this project page was created.
	 * 
	 * @return
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/**
	 * Set the date this project page was created.
	 * 
	 * @param createdDate
	 */
	void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	
	/**
	 * Get the members for the group workspace. This is an 
	 * unmodifiable list.
	 * 
	 * @return
	 */
	public Set<GroupWorkspaceProjectPageMember> getMembers() {
		return Collections.unmodifiableSet(members);
	}
	
	/**
	 * Get the members by order.
	 * 
	 * @return
	 */
	public List<GroupWorkspaceProjectPageMember> getMembersByOrder()
	{
		List<GroupWorkspaceProjectPageMember> theMembers = new LinkedList<GroupWorkspaceProjectPageMember>();
	    theMembers.addAll(members);
	    Collections.sort(theMembers, new AscendingOrderComparator());
	    return Collections.unmodifiableList(theMembers);
	}
	
	/**
	 * Get the images by order.
	 * 
	 * @return
	 */
	public List<GroupWorkspaceProjectPageImage> getImagesByOrder()
	{
		List<GroupWorkspaceProjectPageImage> theImages = new LinkedList<GroupWorkspaceProjectPageImage>();
	    theImages.addAll(images);
	    Collections.sort(theImages, new AscendingOrderComparator());
	    return Collections.unmodifiableList(theImages);
	}

	/**
	 * Set the members in ghe group workspace project page.
	 * 
	 * @param members
	 */
	void setMembers(Set<GroupWorkspaceProjectPageMember> members) {
		this.members = members;
	}

	
	/**
	 * Adds an existing file to the root of this researcher.
	 * If the file was a child of an existing folder.  It
	 * is removed from the old folders file list.
	 * 
	 * @param file - to add as a root file
	 */
	public void addRootFile(GroupWorkspaceProjectPageFile file) 
	{
		// file already part of this 
		if(!rootFiles.contains(file))
		{
		
		    GroupWorkspaceProjectPageFolder folder = file.getParentFolder();
		    if( folder != null )
		    {
			    folder.remove(file);
			    file.setParentFolder(null);
		    }
		
		    rootFiles.add(file);
		}
	}


	/**
	 * Adds an existing institutional item to the root of this researcher.
	 * If the institutional item was a child of an existing folder.  It
	 * is removed from the old folder list.
	 * 
	 * @param institutionalItem - to add as a root institutional item
	 */
	public void addRootInstitutionalItem(GroupWorkspaceProjectPageInstitutionalItem institutionalItem) 
	{
	
		GroupWorkspaceProjectPageFolder folder = institutionalItem.getParentFolder();
		if( folder != null )
		{
			folder.remove(institutionalItem);
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
	public void addRootLink(GroupWorkspaceProjectPageFileSystemLink link) 
	{
	
		GroupWorkspaceProjectPageFolder folder = link.getParentFolder();
		if( folder != null )
		{
			folder.remove(link);
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
	public GroupWorkspaceProjectPageFile createRootFile(IrFile f, int versionNumber)
	{
		GroupWorkspaceProjectPageFile rf = this.getFile(f);
		if( rf == null )
		{
		    rf = new GroupWorkspaceProjectPageFile(this, f);
		    rf.setVersionNumber(versionNumber);
		    rootFiles.add(rf);
		}
		return rf;
	}
	
	/**
	 * Adds an existing publication to the root of this project page.
	 * If the publication was a child of an existing folder.  It
	 * is removed from the old folder list.
	 * 
	 * @param publication - to add as a root publication
	 */
	public void addRootPublication(GroupWorkspaceProjectPagePublication publication) 
	{
	
		GroupWorkspaceProjectPageFolder folder = publication.getParentFolder();
		if( folder != null )
		{
			folder.remove(publication);
			publication.setParentFolder(null);
		}
		
		rootPublications.add(publication);
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
	public GroupWorkspaceProjectPageFolder createRootFolder(String name) throws DuplicateNameException
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
		
		GroupWorkspaceProjectPageFolder f = new GroupWorkspaceProjectPageFolder(this, name);
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
	public GroupWorkspaceProjectPagePublication createRootPublication(GenericItem publication, int versionNumber)
	{ 
		GroupWorkspaceProjectPagePublication  researcherPublication = this.getPublication(publication);
		if( researcherPublication == null )
		{	
		    researcherPublication = new GroupWorkspaceProjectPagePublication(this, publication, versionNumber);
	        rootPublications.add(researcherPublication);
		}
		return researcherPublication;
	}

	
	/**
	 * Get the researcher file based on the ir file.
	 * 
	 * @param f - IR file the researcher file should contain.
	 * @return - the found researcher file
	 */
	public GroupWorkspaceProjectPageFile getFile(IrFile f)
	{
		for(GroupWorkspaceProjectPageFile file : rootFiles)
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
	public GroupWorkspaceProjectPageFileSystemLink createRootLink(String url, String name, String description)
	{  
		GroupWorkspaceProjectPageFileSystemLink researcherLink = new GroupWorkspaceProjectPageFileSystemLink(this, url);
		researcherLink.setName(name);
		researcherLink.setDescription(description);
	    rootLinks.add(researcherLink);
		
		return researcherLink;
	}
	
	/**
	 * Get a researcher file by name.
	 * 
	 * @param nameWithExtension
	 * @return the found researcher file
	 */
	public GroupWorkspaceProjectPageFile getRootFileByNameExtension(String nameWithExtension)
	{
		for(GroupWorkspaceProjectPageFile rf: rootFiles )
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
	public GroupWorkspaceProjectPageFile getRootFile(String name)
	{
		for(GroupWorkspaceProjectPageFile rf: rootFiles )
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
	public GroupWorkspaceProjectPagePublication getRootPublication(String name)
	{
		for(GroupWorkspaceProjectPagePublication rp: rootPublications)
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
	public GroupWorkspaceProjectPageInstitutionalItem getRootInstitutionalItem(String name)
	{
		for(GroupWorkspaceProjectPageInstitutionalItem rp: rootInstitutionalItems)
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
	public GroupWorkspaceProjectPageFileSystemLink getRootLink(String name)
	{
		for(GroupWorkspaceProjectPageFileSystemLink rl: rootLinks)
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
	public GroupWorkspaceProjectPageFolder getRootFolder(String name)
	{
		for(GroupWorkspaceProjectPageFolder f: rootFolders )
		{
			if( f.getName().equalsIgnoreCase(name.trim()))
			{
				return f;
			}
		}
		return null;
	}
	

	
	
	
	
	
	/**
	 * Remove the root file from this group workspace
	 * 
	 * @param file - group workspace project page file
	 * @return true if the file is removed.
	 */
	public boolean remove(GroupWorkspaceProjectPageFile file)
	{
		return rootFiles.remove(file);
	}

	/**
	 * Remove the root publication from this group workspace project page
	 * 
	 * @param publication - group workspace project page publication
	 * @return true if the publication is removed.
	 */
	public boolean remove(GroupWorkspaceProjectPagePublication publication)
	{
		return rootPublications.remove(publication);
	}

	/**
	 * Remove the root institutional item from this group workspace project page
	 * 
	 * @param institutionalItem - group workspace project page institutional tiem
	 */
	public boolean remove(GroupWorkspaceProjectPageInstitutionalItem institutionalItem)
	{
		return rootInstitutionalItems.remove(institutionalItem);
	}
	
	/**
	 * Remove the root link from this group workspace project page
	 * 
	 * @param link
	 * @return true if the link is removed.
	 */
	public boolean remove(GroupWorkspaceProjectPageFileSystemLink link)
	{
		return rootLinks.remove(link);
	}

	/**
	 * Remove the root folder from this group workspace project apge folder
	 * 
	 * @param folder
	 * @return true if the folder is removed.
	 */
	public boolean remove(GroupWorkspaceProjectPageFolder folder)
	{
		return rootFolders.remove(folder);
	}
	
	
	
	
	
	
	
	/**
	 * Adds an existing folder to the root of this researcher.
	 * If the folder was a child of an existing folder.  It
	 * is removed from its parents list.
	 * 
	 * @param folder - to add as a root
	 * @throws DuplicateNameException 
	 */
	public void addRootFolder(GroupWorkspaceProjectPageFolder folder) throws DuplicateNameException
	{
		//if this folder already is a root folder do not add it
		if( !rootFolders.contains(folder) )
		{
			if(getRootFolder(folder.getName()) != null)
			{
				throw new DuplicateNameException("Folder with name " + folder.getName() +
				" already exists in this folder", folder.getName() );
	        }
			GroupWorkspaceProjectPageFolder parent = folder.getParent();
			parent.removeChild(folder);
			rootFolders.add(folder);
		}
	}


	/**
	 * Creates the root Institutional Item if it does not exist.  The 
	 * name should not be null or the name of an existing root
	 * researcher publication for this researcher.
	 * 
	 * @param institutionalItem institutional Item to add to this researcher.
	 * @return Created researcher institutional Item if it does not already exist
	 */
	public GroupWorkspaceProjectPageInstitutionalItem createRootInstitutionalItem(InstitutionalItem institutionalItem)
	{ 
		GroupWorkspaceProjectPageInstitutionalItem researcherInstitutionalItem = getInstitutionalItem(institutionalItem);
		if( researcherInstitutionalItem == null )
		{
		    researcherInstitutionalItem = new GroupWorkspaceProjectPageInstitutionalItem(this, institutionalItem);
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
	public GroupWorkspaceProjectPageInstitutionalItem getInstitutionalItem(InstitutionalItem institutionalItem)
	{
		for( GroupWorkspaceProjectPageInstitutionalItem item : rootInstitutionalItems)
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
	public GroupWorkspaceProjectPagePublication getPublication(GenericItem publication)
	{
		for(GroupWorkspaceProjectPagePublication pub : rootPublications)
		{
			if( pub.getPublication().equals(publication))
			{
				return pub;
			}
		}
		return null;
	}
	

	/**
	 * Get the root folders for the researcher.  This is an
	 * unmodifiable set.
	 * 
	 * @return set of root folders.
	 */
	public Set<GroupWorkspaceProjectPageFolder> getRootFolders() {
		return Collections.unmodifiableSet(rootFolders);
	}

	/**
	 * Set the root folders for a researcher.
	 * 
	 * @param rootFolders
	 */
	void setRootFolders(Set<GroupWorkspaceProjectPageFolder> rootFolders) {
		this.rootFolders = rootFolders;
	}

	/**
	 * Get the root files for a researcher.  This is
	 * an unmodifiable set.
	 * 
	 * @return
	 */
	public Set<GroupWorkspaceProjectPageFile> getRootFiles() {
		return Collections.unmodifiableSet(rootFiles);
	}

	/**
	 * Se tthe root files.
	 * 
	 * @param rootFiles
	 */
	void setRootFiles(Set<GroupWorkspaceProjectPageFile> rootFiles) {
		this.rootFiles = rootFiles;
	}

	/**
	 * Return the set of root publications.  This is an unmodifiable set.
	 * 
	 * @return unmodifiable set of root publications
	 */
	public Set<GroupWorkspaceProjectPagePublication> getRootPublications() {
		return Collections.unmodifiableSet(rootPublications);
	}

	/**
	 * Set the root publications.
	 * 
	 * @param rootPublications
	 */
	void setRootPublications(Set<GroupWorkspaceProjectPagePublication> rootPublications) {
		this.rootPublications = rootPublications;
	}

	/**
	 * Get the root links for the researcher - this is an unmodifiable set.
	 * 
	 * @return set of root links
	 */
	public Set<GroupWorkspaceProjectPageFileSystemLink> getRootLinks() {
		return Collections.unmodifiableSet(rootLinks);
	}

	/**
	 * Set the root links for the researcher
	 * 
	 * @param rootLinks
	 */
	void setRootLinks(Set<GroupWorkspaceProjectPageFileSystemLink> rootLinks) {
		this.rootLinks = rootLinks;
	}
	
	/**
	 * Get root publication by id.
	 * 
	 * @param id - Id of the researcher root publication to return
	 * @return The Researcher Publication if found otherwise null.
	 */
	public GroupWorkspaceProjectPagePublication getRootPublication(Long id)
	{
		for(GroupWorkspaceProjectPagePublication researcherPublication: rootPublications)
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
	public GroupWorkspaceProjectPageInstitutionalItem getRootInstitutionalItem(Long id)
	{
		for(GroupWorkspaceProjectPageInstitutionalItem researcherInstitutionalItem: rootInstitutionalItems)
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
	public GroupWorkspaceProjectPageFileSystemLink getRootLink(Long id)
	{
		for(GroupWorkspaceProjectPageFileSystemLink researcherLink: rootLinks)
		{
			if( researcherLink.getId().equals(id))
			{
				return researcherLink;
			}
		}
		return null;
	}

	/**
	 * Get the root institutional items for the researcher.  This is 
	 * an unmodifiable set.
	 * 
	 * @return set of root insitutional items.
	 */
	public Set<GroupWorkspaceProjectPageInstitutionalItem> getRootInstitutionalItems() {
		return Collections.unmodifiableSet(rootInstitutionalItems);
	}

	/**
	 * Set the root insitutional items 
	 * @param rootInstitutionalItems
	 */
	void setRootInstitutionalItems(
			Set<GroupWorkspaceProjectPageInstitutionalItem> rootInstitutionalItems) {
		this.rootInstitutionalItems = rootInstitutionalItems;
	}

}
