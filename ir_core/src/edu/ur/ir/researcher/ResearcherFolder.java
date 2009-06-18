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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.persistent.LongPersistentId;
import edu.ur.persistent.PersistentVersioned;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;
import edu.ur.tree.PreOrderTreeSetNodeBase;

/**
 * Represents a researcher folder in which researcher can store 
 * their files, publications and links.  A Researcher Folder can have sub 
 * folders.
 * 
 * Files and folders cannot have the same name within a given
 * folder.  This means that two files cannot have the same name
 * nor can a file and folder have the same name within a folder.
 * 
 *  
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
@SuppressWarnings("unchecked")
public class ResearcherFolder extends PreOrderTreeSetNodeBase implements
Serializable,  LongPersistentId, PersistentVersioned,
DescriptionAware, NameAware, Comparable, FileSystem{
	
	/** Logger */
	private static final Logger log = Logger.getLogger(ResearcherFolder.class);
	
	/**  The id of the folder  */
	private Long id;
	
	/**  Name of the researcher folder */
	private String name;
	
	/** Description of the folder */
	private String description;
	
	/**  Version of the data read from the database.  */
	private int version;
	
	/** Root of the entire folder tree. */
	private ResearcherFolder treeRoot;
	
	/** Researcher the folder belongs to. */
	private Researcher researcher;
	
	/**  The type of object this represents */
	private FileSystemType fileSystemType = FileSystemType.RESEARCHER_FOLDER;

	/** Folders of the researcher */
	private Set<ResearcherFolder> children = new HashSet<ResearcherFolder>();
	
	/** Files of the researcher */
	private Set<ResearcherFile> files = new HashSet<ResearcherFile>();
	
	/** Publications of the researcher */
	private Set<ResearcherPublication> publications = new HashSet<ResearcherPublication>();

	/** Institutional items of the researcher */
	private Set<ResearcherInstitutionalItem> institutionalItems = new HashSet<ResearcherInstitutionalItem>();
	
	/** Links of the researcher */
	private Set<ResearcherLink> links = new HashSet<ResearcherLink>();
		
	/**
	 * This is the conceptual path to the folder.
	 * The base path plus the root of the tree 
	 * down to itself.
	 * For example if you have a parent A
	 * the following sub folders  
	 *  B, C and D and A is a parent of B and 
	 * B is a parent of C and C is a parent of D Then the
	 * paths are as follows:
	 * 
	 *  /A/
	 *  /A/B/
	 *  /A/B/C/
	 *  /A/B/C/D/
	 * 
	 */
	private String path;
	
	/**  Eclipse generated id. */
	private static final long serialVersionUID = 5473125788309751410L;
	
	/**
	 * Default Constructor 
	 */
	ResearcherFolder()
	{
		this.treeRoot = this;
	}
	
	/**
	 * Default public constructor.  A valid researcher
	 * and folder name must be passed.
	 * 
	 * @param researcher - researcher who will own the folder
	 * @param folderName - name of the folder
	 * 
	 * @throws IllegalStateException if the folder name or
	 * researcher are null.
	 */
	ResearcherFolder(Researcher researcher, String folderName)
	{
		if(folderName == null)
		{
			throw new IllegalStateException("folder name cannot be null");
		}
		
		if( researcher == null )
		{
			throw new IllegalStateException("Researcher cannot be null");
		}
		
		setTreeRoot(this);
		setResearcher(researcher);
		setName(folderName);
		setPath(PATH_SEPERATOR);
	}
	
	/**
	 * Find a ResearcherFolder based on the name.  If
	 * no ResearcherFolder is found a null object is returned.
	 * 
	 * This is <b>NOT</b>  a recursive operation and only searches
	 * the current list of children.
	 * 
	 * @param name of the child researcher folder.
	 * @return the found researcher folder.
	 */
	public ResearcherFolder getChild(String name)
	{
		if( log.isDebugEnabled() )
		{
			log.debug("Searching for researcher folder " + name);
		}
		ResearcherFolder ResearcherFolder = null;
		boolean found = false;
		Iterator<ResearcherFolder> iter = children.iterator();
		
		while( iter.hasNext() && !found)
		{
			ResearcherFolder c = iter.next();
			if( c.getName().equalsIgnoreCase(name))
			{
				ResearcherFolder = c;
				found = true;
		    }
		}
	
		return ResearcherFolder;
	}	

	/**
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getChildren()
	 */
	@Override
	public Set<ResearcherFolder> getChildren() {
		return Collections.unmodifiableSet(new TreeSet<ResearcherFolder>(children));
	}

	/**
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getParent()
	 */
	@Override
	public ResearcherFolder getParent() {
		return (ResearcherFolder)parent;
	}
	
	/**
	 * @see edu.ur.persistent.LongPersistentId#getId()
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @see edu.ur.persistent.PersistentVersioned#getVersion()
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Get the description of this collection
	 * 
	 * @see edu.ur.common.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Description of the collection.
	 * 
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Get the name of this collection
	 * 
	 * @see edu.ur.common.NameAware#getName()
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Name of the collection.
	 * 
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Add a file to this folder.  this creates a 
	 * new researcher file record.
	 * 
	 * @param irFile
	 */
	public ResearcherFile addFile(IrFile irFile) {
		 ResearcherFile rf = new ResearcherFile(researcher, irFile, this);
		 addResearcherFile(rf);
		 return rf;
	}

	/**
	 * Add a file to this folder.  this creates a 
	 * new researcher file record.
	 * 
	 * @param irFile
	 */
	public ResearcherPublication createPublication(GenericItem item, int versionNumber) {
		 
		ResearcherPublication rp = new ResearcherPublication(researcher, this, item, versionNumber);
		publications.add(rp);
		return rp;
	}

	/**
	 * Add a institutional Item to this folder.  this creates a 
	 * new researcher institutional Item record.
	 * 
	 * @param institutionalItem
	 */
	public ResearcherInstitutionalItem createInstitutionalItem(InstitutionalItem institutionalItem) {
		 
		ResearcherInstitutionalItem ri = new ResearcherInstitutionalItem(researcher, this, institutionalItem);
		institutionalItems.add(ri);
		return ri;
	}
	
	/**
	 * Add a file to this folder.  this creates a 
	 * new researcher file record.
	 * 
	 * @param irFile
	 */
	public ResearcherLink createLink(String name, String url, String description) throws DuplicateNameException
	{
		 ResearcherLink rl = new ResearcherLink(researcher, this, url);
		 rl.setName(name);
		 rl.setDescription(description);
		 
		 if( links.contains( rl))
		 {
			 throw new DuplicateNameException("Link " + rl + " already exists ", name);
		 }
		 links.add(rl);
		 return rl;
	}

	/**
	 * Adds an existing researcher file to this folder.  This allows
	 * a researcher file to be moved from one location to another.
	 * 
	 * @param rf - researcher file to add
	 * @throws DuplicateNameException - if the file already exits
	 */
	public void addResearcherFile(ResearcherFile rf) 
	{
		if(!files.contains(rf))
		{
			if( rf.getParentFolder() != null )
			{
				rf.getParentFolder().removeResearcherFile(rf);
			}
			
			rf.setParentFolder(this);
			files.add(rf);
		}
	}
	
	/**
	 * Remove a file from the researcher folder
	 * 
	 * @param rf - file to be removed
	 * @return true if the file is removed.
	 */
	public boolean removeResearcherFile(ResearcherFile rf)
	{
		boolean removed = false;
		if( files.contains(rf))
		{
			ResearcherFile researcherFile = getResearcherFile(rf.getName());
			removed = files.remove(rf);
			rf.setParentFolder(null);
			researcherFile.setParentFolder(null);
		}
		return removed;
	}
	
	/**
	 * Remove a publication from the researcher folder
	 * 
	 * @param rp
	 * @return true if the publication is removed.
	 */
	public boolean removeResearcherPublication(ResearcherPublication rp)
	{
		boolean removed = false;
		if( publications.contains(rp))
		{
			removed = publications.remove(rp);
			rp.setParentFolder(null);
		}
		return removed;
	}

	/**
	 * Remove a institutional Item from the researcher folder
	 * 
	 * @param rp
	 * @return true if the institutional Item is removed.
	 */
	public boolean removeResearcherInstitutionalItem(ResearcherInstitutionalItem ri)
	{
		boolean removed = false;
		if( institutionalItems.contains(ri))
		{
			removed = institutionalItems.remove(ri);
			ri.setParentFolder(null);
		}
		return removed;
	}
	
	/**
	 * Remove a link from the researcher folder
	 * 
	 * @param rl
	 * @return true if the link is removed.
	 */
	public boolean removeResearcherLink(ResearcherLink rl)
	{
		boolean removed = false;
		if( links.contains(rl))
		{
			removed = links.remove(rl);
			rl.setParentFolder(null);
		}
		return removed;
	}
	
	/**
	 * Find a file based on the name.  If
	 * no file is found a null object is returned.  This
	 * is case in-sensitive
	 * 
	 * @param name of the file including the extension
	 * @return the found file
	 */
	public ResearcherFile getResearcherFile(String name)
	{
		for(ResearcherFile rf : files)
		{
			if( rf.getName().equalsIgnoreCase(name))
			{
				return rf;
			}
		}
		
		return null;
	}

	/**
	 * Find a file based on the name.  If
	 * no file is found a null object is returned.  This
	 * is case in-sensitive
	 * 
	 * @param name of the file including the extension
	 * @return the found file
	 */
	public ResearcherLink getResearcherLink(String name)
	{
		for(ResearcherLink rl : links)
		{
			if( rl.getName().equalsIgnoreCase(name))
			{
				return rl;
			}
		}
		
		return null;
	}
	
	/**
	 * Find a file based on the irFile.  If
	 * no file is found a null object is returned.  
	 * 
	 * @param irFile - the file
	 * @return the found file
	 */
	public ResearcherFile getResearcherFile(IrFile irFile)
	{
		for(ResearcherFile rf : files)
		{
			if( rf.getIrFile().equals(irFile))
			{
				return rf;
			}
		}
		
		return null;
	}

	/**
	 * Find a file based on the irFile.  If
	 * no file is found a null object is returned.  
	 * 
	 * @param irFile - the file
	 * @return the found file
	 */
	public ResearcherPublication getResearcherPublication(GenericItem item)
	{
		for(ResearcherPublication rp : publications)
		{
			if( rp.getPublication().equals(item))
			{
				return rp;
			}
		}
		
		return null;
	}

	/**
	 * Find a researcher Institutional Item based on the Institutional Item.  If
	 * no Institutional Item is found a null object is returned.  
	 * 
	 * @param item - the Institutional Item
	 * @return the found Institutional Item
	 */
	public ResearcherInstitutionalItem getResearcherInstitutionalItem(InstitutionalItem item)
	{
		for(ResearcherInstitutionalItem ri : institutionalItems)
		{
			if( ri.getInstitutionalItem().equals(item))
			{
				return ri;
			}
		}
		
		return null;
	}

	/**
	 * An unmodifiable set of files in this researcher folder
	 * 
	 * 
	 * @return the set of items.
	 */
	public Set<ResearcherFile> getFiles() {
		return Collections.unmodifiableSet(files);
	}

	/**
	 * Files in this folder
	 * 
	 * @param items
	 */
	void setFiles(Set<ResearcherFile> files) {
		this.files = files;
	}
	
	/**
	 * Returns a folder root.
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getRoot()
	 */
	@Override
	public ResearcherFolder getTreeRoot() {
		return treeRoot;
	}

	/**
	 * Remove the child from the set of children. 
	 * Renumbers the tree to be correct.
	 * 
	 * @param child
	 * @return true if the child is removed.
	 */
	public boolean removeChild(ResearcherFolder child)
	{
		boolean removed = false;
		
		if( children.remove(child) )
		{
			log.debug("removing child " + child);
			removed = true;
			
			// re-number the tree
			cleanUpTree(child.getTreeSize(), child.getRightValue());
			log.debug( "child removed tree values = " + this);
			child.setParent(null);
	    }
		return removed;
	}
	
	/**
	 * Add a child folder to this researcher folder's set of children.
	 * 
	 * @param child to add
	 */
	public void addChild(ResearcherFolder child) throws DuplicateNameException
	{

		// return if this already has the child
		if( children.contains(child))
		{
			return;
		}

		if( child.equals(this))
		{
			throw new IllegalStateException("cannot add a researcher folder to " +
					"itself as a child");
		}
		if(!isVaildResearcherFileSystemName(child.getName()))
		{
			throw new DuplicateNameException("Folder with the name " + 
					child.getName() + " already exists ", child.getName());
		}
		if (!child.isRoot()) {
			ResearcherFolder childParent = child.getParent();
			childParent.removeChild(child);
		}

		child.setTreeRoot(null);
		makeRoomInTree(child);
		child.setParent(this);
		child.setResearcher(researcher);
		child.setTreeRoot(getTreeRoot());
		children.add(child);
	}
	
	/**
	 * Create a new child folder for this 
	 * researcher folder.
	 * 
	 * @param name of the child folder.
	 * @return the created folder
	 * 
	 * @throws IllegalArgumentException if a name of a folder that
	 * already exists is passed in.
	 */
	public ResearcherFolder createChild(String name) throws DuplicateNameException
	{
	    ResearcherFolder c = new ResearcherFolder();
		c.setName(name);
		addChild(c);
		return c;
	}
	
	/**
	 * Returns true if the name is ok to add to a file or folder
	 * 
	 * @param name of the file or folder.  If it is a file, it should contain
	 * the extension.
	 * 
	 * @return true if the name does not exist.  This is case insensitive.
	 */
	private boolean isVaildResearcherFileSystemName(String name)
	{
		boolean ok = false;
		if( getChild(name) == null && getResearcherFile(name) == null)
		{
			ok = true;
		}
		return ok;
	}

	/**
	 * Set the children in this folder.
	 * 
	 * @param children
	 */
	void setChildren(Set<ResearcherFolder> children) {
		this.children = children;
	}

	/**
	 * Set the id of this folder.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Set the parent of this researcher folder.  Results
	 * in setting path and all children paths.
	 * 
	 * @param parent
	 */
	void setParent(ResearcherFolder parent) {
		if(parent == null)
		{
			path = ResearcherFolder.PATH_SEPERATOR ;
		}		
		super.setParent(parent);
	}

	/**
	 * Set the root folder for the entire tree.  
	 * This recursively updates the children.
	 * 
	 * @param root
	 */
	void setTreeRoot(ResearcherFolder root) {
		this.treeRoot = root;
		for(ResearcherFolder c: children)
		{
			c.setTreeRoot(root);
		}
	}

	/**
	 * Set the version of data for the database data
	 * of this folder.
	 * 
	 * @param version
	 */
	public void setVersion(int version) {
		this.version = version;
	}
		
	/**
	 * Get the path.  This should be
	 * the researcher name plus the path of all 
	 * parents.
	 * 
	 * @throws IllegalStateException if the folder is the root 
	 * folder and the researcher does not exist.
	 *  
	 * @return the path
	 */
	public String getPath() 
	{
		return path;
	}
	
	/**
	 * Get the full path of this folder.
	 * 
	 * @return the full path
	 */
	public String getFullPath()
	{
		return getPath() + name + PATH_SEPERATOR;
	}

	/**
	 * Set the conceptual path for this folder.
	 * This DOES NOT update children paths.
	 * 
	 * @see updatePaths
	 * 
	 * @param path
	 */
	void setPath(String path) {
		
		path = path.trim();
		
        // verify the path is correct
		verifyPath(path);

		// add the end seperator if it does not exist
		if (!path.substring(path.length()-1, path.length()).equals(PATH_SEPERATOR)) {
			path = path + PATH_SEPERATOR;
		}
		
		this.path = path;
	}
	
	/**
	 * Make sure the path is correct. A root folder always returns true.
	 * 
	 * @param path -
	 *            the path of this object
	 * @return - true if the parent's full path matches this folders path or
	 *         this is the root folder.
	 * 
	 * 
	 */
	private boolean verifyPath(String path) {
		boolean valid = true;
		
        //make sure there is a beginning separator
		if(!path.substring(0,1).equals(PATH_SEPERATOR))
		{
		    throw new IllegalStateException("No beginning path Seperator path = " + path);
		}
		else if (!isRoot()) {
			if (!(path.equals(parent.getFullPath()))) {
				throw new IllegalArgumentException("Path is : " + path
						+ " but should equal parent path "
						+ " plus parents name which = " + parent.getFullPath());
			}
		}
		return valid;
	}
	
	/**
	 * Hash code is based on the path and name of
	 * the folder.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += researcher == null ? 0 : researcher.hashCode();
		value += getFullPath() == null? 0 : getFullPath().hashCode();
		return value;
	}
	
	/**
	 * Equals is tested based on name and path of the
	 * object.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ResearcherFolder)) return false;

		final ResearcherFolder other = (ResearcherFolder) o;

		if( ( researcher != null && !researcher.equals(other.getResearcher()) ) ||
			( researcher == null && other.getResearcher() != null ) ) return false;
		
		if( ( getFullPath() != null && !getFullPath().equals(other.getFullPath()) ) ||
		    ( getFullPath() == null && other.getFullPath() != null ) ) return false;

		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ResearcherFolder id = ");
		sb.append(id);
		sb.append(" name = ");
		sb.append(name);
		sb.append(" path = ");
		sb.append(path);
		sb.append( " left value = ");
		sb.append(leftValue);
		sb.append(" right value = ");
		sb.append(rightValue);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Sets the path and the path of all
	 * children. This should be used when a
	 * path should be passed down to all children.
	 * 
	 * @param path
	 */
	protected void updatePaths(String path)
	{
		setPath(path);
		for(ResearcherFolder c: children)
		{
			c.updatePaths(getFullPath());
		}
	}
	 
	/**
	 * Compares two researcher folders by name.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object other) {
		ResearcherFolder c = (ResearcherFolder)other;
		return this.getName().compareTo(c.getName());
	}


	/* (non-Javadoc)
	 * @see edu.ur.ir.FileSystem#getType()
	 */
	public FileSystemType getFileSystemType() {
		return fileSystemType;
	}

	/**
	 * Always returns -1
	 * 
	 * @see edu.ur.ir.FileSystem#getVersionNumber()
	 */
	public int getVersionNumber() {
		return -1;
	}

	/**
	 * This is not a versioned entity
	 * @see edu.ur.ir.FileSystem#getVersioned()
	 */
	public boolean getVersioned() {
		return false;
	}

	@Override
	protected void setRoot(PreOrderTreeSetNodeBase root) {
		this.treeRoot = (ResearcherFolder)root;
	}

	/**
	 * Get the researcher who owns the folder.
	 * 
	 * @return
	 */
	public Researcher getResearcher() {
		return researcher;
	}

	/**
	 * Set the researcher who owns the folder.
	 * 
	 * @param researcher
	 */	
	void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}

	/**
	 * Get the set of publications for the researcher - this
	 * is an unmodifiable set.
	 * 
	 * @return
	 */
	public Set<ResearcherPublication> getPublications() {
		return Collections.unmodifiableSet(publications);
	}

	/**
	 * Set the publications for the researcher folder.
	 * 
	 * @param publications
	 */
	void setPublications(Set<ResearcherPublication> publications) {
		this.publications = publications;
	}

	/**
	 * Get the researcher links in this folder - this is an unmodifiable set.
	 * 
	 * @return set of reseracher links
	 */
	public Set<ResearcherLink> getLinks() {
		return Collections.unmodifiableSet(links);
	}

	/**
	 * Set the links for the researcher folder.
	 * 
	 * @param links
	 */
	void setLinks(Set<ResearcherLink> links) {
		this.links = links;
	}
	
	/**
	 * Adds publication to a folder
	 * 
	 * @param rp publication to add researcher folder
	 * 
	 */
	public void addPublication(ResearcherPublication rp) {
		if (!publications.contains(rp)) {
			if( rp.getParentFolder() != null )
			{
				rp.getParentFolder().removeResearcherPublication(rp);
			}
			
			rp.setParentFolder(this);
			publications.add(rp);
		}
	}

	/**
	 * Adds Institutional Item to a folder
	 * 
	 * @param ri Institutional Item to add researcher folder
	 * 
	 */
	public void addInstitutionalItem(ResearcherInstitutionalItem ri) {
		if (!institutionalItems.contains(ri)) {
			if( ri.getParentFolder() != null )
			{
				ri.getParentFolder().removeResearcherInstitutionalItem(ri);
			}
			
			ri.setParentFolder(this);
			institutionalItems.add(ri);
		}
	}
	
	/**
	 * Adds link to a folder
	 * 
	 * @param rl link to add researcher folder
	 * 
	 */
	public void addLink(ResearcherLink rl) {
		if (!links.contains(rl)) {
			if( rl.getParentFolder() != null )
			{
				rl.getParentFolder().removeResearcherLink(rl);
			}
			rl.setParentFolder(this);
			links.add(rl);
		}
	}
	
	/**
	 * Creates JSON object
	 * 
	 * @return
	 */
	public JSONObject toJSONObject() {
		log.debug("call getJsonString Folder");
		
		JSONObject jsonObj = new JSONObject();
		
		try {
			jsonObj.put("name",name.replaceAll("'", "&#146;").replaceAll("\"", "&#148;"));
			jsonObj.put("id",id);
			jsonObj.put("type",fileSystemType.getType());
			
			if( description != null )
			{
			    jsonObj.put("description",description.replaceAll("'", "&#146;").replaceAll("\"", "&#148;"));
			}
			else
			{
				jsonObj.put("description", "");
			}
			// Put sub folders
			JSONArray jsonSubFolders = new JSONArray();
		 	for(ResearcherFolder folder: children) {
				jsonSubFolders.add(folder.toJSONObject());
			}

			jsonObj.put("folders",jsonSubFolders);	

			// Put files
			JSONArray jsonFiles = new JSONArray();
			for(ResearcherFile file: files) {
				jsonFiles.add(file.toJSONObject());
			}

			jsonObj.put("files",jsonFiles);	
			
			// Put publications
			JSONArray jsonPublications = new JSONArray();
			for(ResearcherPublication p: publications) {
				jsonPublications.add(p.toJSONObject());
			}

			jsonObj.put("publications",jsonPublications);	

			// Put institutional items
			JSONArray jsonInstitutionalItems = new JSONArray();
			for(ResearcherInstitutionalItem i: institutionalItems) {
				jsonInstitutionalItems.add(i.toJSONObject());
			}

			jsonObj.put("institutionalItems",jsonInstitutionalItems);
			
			// Put files
			JSONArray jsonLinks = new JSONArray();
			for(ResearcherLink link: links) {
				jsonLinks.add(link.toJSONObject());
			}

			jsonObj.put("links",jsonLinks);	

		} catch (Exception e) {
			 log.debug("jsonObj Exception::"+e.getMessage());
		}
		
		log.debug("jsonObj Folder ::"+jsonObj);
		
		return jsonObj;
	}

	/**
	 * Returns an unmodifiable set of institutional items.
	 * 
	 * @return set of institutional items for this researcher.
	 */
	public Set<ResearcherInstitutionalItem> getInstitutionalItems() {
		return Collections.unmodifiableSet(institutionalItems);
	}

	/**
	 * Set the set of institutional items.
	 * 
	 * @param institutionalItems
	 */
	void setInstitutionalItems(
			Set<ResearcherInstitutionalItem> institutionalItems) {
		this.institutionalItems = institutionalItems;
	}

}
