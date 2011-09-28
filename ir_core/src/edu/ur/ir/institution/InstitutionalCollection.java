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

package edu.ur.ir.institution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.order.AscendingOrderComparator;
import edu.ur.persistent.LongPersistentId;
import edu.ur.persistent.PersistentVersioned;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;
import edu.ur.tree.PreOrderTreeSetNodeBase;


/**
 * Represents an institutional collection in the repository.  Institutional 
 * collections are owned by the institution.  Once items are added to institutional
 * collections institutional rules and restrictions can be applied.
 * 
 * @author Nathan Sarr
 *
 */
@SuppressWarnings("unchecked")
public class InstitutionalCollection extends PreOrderTreeSetNodeBase implements
Serializable, LongPersistentId, PersistentVersioned,
DescriptionAware, NameAware, Comparable, FileSystem
{
	/** Logger */
	private static final Logger log = Logger.getLogger(InstitutionalCollection.class);
	
	/**  Children of this IrCollection */
	private Set<InstitutionalCollection> children = new HashSet<InstitutionalCollection>();
	
	/**  The repository this collection belongs to */
	private Repository repository;
	
	/**  The id of the collection  */
	private Long id;
	
	/**  Name of the collection */
	private String name;
	
	/**  Description of the collection */
	private String description;
	
	/** copy right text for the collection */
	private String copyright;
	
	/**  Version of the database data read from the database. */
	private int version;
	
	/**  Items in this collection. */
	private Set<InstitutionalItem> items = new HashSet<InstitutionalItem>();
	
	/**  Subscriptions to this collection  */
	private Set<InstitutionalCollectionSubscription> subscriptions = new HashSet<InstitutionalCollectionSubscription>();
	
	/** Set of links for a collection */
	private List<InstitutionalCollectionLink> links = new LinkedList<InstitutionalCollectionLink>();
	
	/**  Root of the entire tree. */
	private InstitutionalCollection treeRoot;
		
	/**
	 * This is the conceptual path to the item.
	 * The base path plus the root of the tree 
	 * down to itself.
	 * For example if you have a repository named repo and
	 * the following collections 
	 * A, B, C and D and A is a parent of B and 
	 * B is a parent of C and C is a parent of D Then the
	 * paths are as follows:
	 * 
	 *  /A/
	 *  /A/B
	 *  /A/B/C
	 *  /A/B/C/D
	 * 
	 */
	private String path;
	
	/**  pictures for the collection */
	private Set<IrFile> pictures = new HashSet<IrFile>();
	
	/** main picture to be shown as a thumb-nail to users */
	private IrFile primaryPicture;
	
	/**  Indicates if this collection allows items. Defaults is true */
	private boolean allowsItems = true;

	/**  Generated version id. */
	private static final long serialVersionUID = -6434282254966436616L;
	
	/** represents the file system type for this personal item */
	private FileSystemType fileSystemType = FileSystemType.INSTITUTIONAL_COLLECTION;
	
	/** determine if this collection is publicly viewable*/
	private boolean publiclyViewable = true;
	
	/** Set of items to be reviewed */
	private Set<ReviewableItem> reviewableItems = new HashSet<ReviewableItem>();

	/**
	 * Package protected default Constructor 
	 */
	InstitutionalCollection()
	{
		this.treeRoot = this;
	}
	
	/**
	 * Create a root level institutional collection with the given name.
	 * 
	 * @param name
	 */
	public InstitutionalCollection(Repository repository, String name)
	{
		if( name == null )
		{
			throw new IllegalStateException("name cannot be null");
		}
		
		if( repository == null )
		{
			throw new IllegalStateException("repository cannot be null");
		}
		
		setRepository(repository);
		setTreeRoot(this);
		setName(name);
		setPath( PATH_SEPERATOR );
	}
	
	
	/**
	 * Find a collection based on the name.  This is case 
	 * in-sensitive. If
	 * no collection is found a null is returned.
	 * 
	 * @param name of the collection.
	 * @return the found collection.
	 */
	public InstitutionalCollection getChild(String name)
	{
		if( log.isDebugEnabled() )
		{
			log.debug("Searching for collection " + name);
		}
		
		for(InstitutionalCollection c : children)
		{
			if( c.getName().equalsIgnoreCase(name))
			{
				return c;
		    }
		}
	
		return null;
	}
	
	/**
     * Creates institutional item
	 *
	 * @param item item to publish
	 * @return Institutional item
     */
	public InstitutionalItem createInstitutionalItem(GenericItem item) throws  CollectionDoesNotAcceptItemsException{
		item.setPublishedToSystem(true);
		InstitutionalItem institutionalItem = new InstitutionalItem(this, item);
		addItem(institutionalItem);
		return institutionalItem;
	}
	
	/**
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getChildren()
	 */
	@Override
	public Set<InstitutionalCollection> getChildren() {
		return Collections.unmodifiableSet(new TreeSet<InstitutionalCollection>(children));
	}

	/**
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getParent()
	 */
	@Override
	public InstitutionalCollection getParent() {
		return (InstitutionalCollection)parent;
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
	void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Re-name the institutional collection.  This updates
	 * the path of all child collections.
	 * 
	 * @param name - name to give the collection
	 * 
	 * @throws DuplicateNameException - if the collection name already exists at the given level
	 */
	public void reName(String name) throws DuplicateNameException
	{
		InstitutionalCollection parent = getParent();
		if(parent == null)
    	{
    		if(getRepository().getInstitutionalCollection(name) == null)
    		{
    			setName(name);
    			updatePaths(getPath());
    		}
    		else
    		{
    			throw new DuplicateNameException("A collection with the name " + name +
						" already exists at the root", name );
    		}
    	}
    	else
    	{
    		if( parent.getChild(name) == null )
    		{
    			setName(name);
    			updatePaths(getPath());
    		}
    		else
    		{
    			throw new DuplicateNameException("A collection with the name " + name +
						" already exists", name );
    		}
    	}
		
	}
	
	/**
	 * Add an item to this collection.  If the item exists
	 * in a different collection, it is removed from that 
	 * collection and a new parent is set.
	 * 
	 * @throws  CollectionDoesNotAcceptItemsException if allowsItems = false
	 * 
	 * @param item - item to add
	 */
	public void addItem(InstitutionalItem item) throws  CollectionDoesNotAcceptItemsException
	{
		if(!allowsItems)
		{
			throw new CollectionDoesNotAcceptItemsException("This collection does not allow items");
		}
		
		if( item.getInstitutionalCollection() != null && !item.getInstitutionalCollection().equals(this) )
		{
			InstitutionalCollection oldParent = item.getInstitutionalCollection();
			oldParent.removeItem(item);
		}
		item.setInstitutionalCollection(this);
		items.add(item);
	}
	
	/**
	 * Remove and item from the collection.  This does NOT
	 * change the parent collection of the item.  This is the
	 * callers responsibility.
	 * 
	 * 
	 * @param item
	 * @return true if the item is removed.
	 */
	public boolean removeItem(InstitutionalItem item)
	{
		return items.remove(item);
	}
	
	
	/**
	 * Find an item based on the name.  If
	 * no item is found a null object is returned.
	 * 
	 * @param name of the item.
	 * @return the found item.
	 */
	public List<InstitutionalItem> getItems(String name)
	{
		if( log.isDebugEnabled() )
		{
			log.debug("Searching for item " + name);
		}
		List<InstitutionalItem> itemsFound = new ArrayList<InstitutionalItem>();

		Iterator<InstitutionalItem> iter = items.iterator();
		
		while( iter.hasNext())
		{
			InstitutionalItem i = iter.next();
			if( i.getVersionedInstitutionalItem().getCurrentVersion().getItem().getName().equals(name))
			{
				itemsFound.add(i);
		    }
		}
	
		return itemsFound;
	}

	/**
	 * An unmodifiable set of items in this collection.
	 * 
	 * 
	 * @return the set of items.
	 */
	public Set<InstitutionalItem> getItems() {
		return Collections.unmodifiableSet(items);
	}

	/**
	 * Items in this collection.
	 * 
	 * @param items
	 */
	void setItems(Set<InstitutionalItem> items) {
		this.items = items;
	}
	
	/**
	 * Returns a collection root.
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getRoot()
	 */
	@Override
	public InstitutionalCollection getTreeRoot() {
		return treeRoot;
	}

	/**
	 * Boolean that indicates if items can be added
	 * to this collection.
	 * 
	 * @return
	 */
	public boolean getAllowsItems() {
		return allowsItems;
	}

	/**
	 * Boolean that indicates that items can be added to
	 * this collection.
	 * 
	 * @param allowsItems
	 * @throws IllegalStateException if setting allows items to false and items.size > 0.
	 */
	public void setAllowsItems(boolean allowsItems) {
		if((items.size() > 0) && !allowsItems )
		{
			throw new IllegalStateException("Must remove all items before setting allowsItems to false");
		}
		this.allowsItems = allowsItems;
	}
	
	/**
	 * Remove the child from the set of children. 
	 * Renumbers the tree to be correct.
	 * 
	 * @param child
	 * @return true if the child is removed.
	 */
	public boolean removeChild(InstitutionalCollection child)
	{
		boolean removed = false;
		
		if( children.remove(child) )
		{
			removed = true;
			
			// re-number the tree
			cleanUpTree(child.getTreeSize(), child.getRightValue());
			child.setParent(null);
	    }
		return removed;
	}
	
	/**
	 * Add a child to this collections set of children.
	 * 
	 * @param child to add
	 * @throws DuplicateNameException 
	 */
	public void addChild(InstitutionalCollection child) throws DuplicateNameException
	{
		if( log.isDebugEnabled() )
		{
		    log.debug( "\n\n------------------------------------\n\n");
		    log.debug( "Adding child " + child.toString());
		}
		
		if( child.equals(this))
		{
			throw new IllegalStateException("cannot add a collection to " +
					"itself as a child");
		}
		if(getChild(child.getName()) != null)
		{
			throw new DuplicateNameException("Collection with the name " + 
					child.getName() + " already exists ", child.getName());
		}
		

		if (!child.isRoot()) {
			InstitutionalCollection childParent = child.getParent();
			childParent.removeChild(child);
		}

		child.setTreeRoot(null);
		makeRoomInTree(child);
		child.setRepository(getRepository());
		child.setParent(this);
		child.setTreeRoot(getTreeRoot());
		children.add(child);
		
		if ( log.isDebugEnabled() )
		{
		    log.debug( "Done Adding child " + child.toString());
		    log.debug( "\n\n------------------------------------\n\n");
		}
	}
	
	
	/**
	 * Create a new child collection for this 
	 * collection.
	 * 
	 * @param name of the child collection.
	 * @return the created collection
	 * @throws DuplicateNameException 
	 */
	public InstitutionalCollection createChild(String name) throws DuplicateNameException
	{
	    InstitutionalCollection c = new InstitutionalCollection();
		c.setName(name);
		addChild(c);
		return c;
	}

	/**
	 * Set the children in this collection.
	 * 
	 * @param children
	 */
	void setChildren(Set<InstitutionalCollection> children) {
		this.children = children;
	}

	/**
	 * Set the id of this collection.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Set the parent of this collection.  Results
	 * in setting path and all children paths.
	 * 
	 * @param parent
	 */
	void setParent(InstitutionalCollection parent) {
		if(parent == null)
		{
			path = PATH_SEPERATOR ;
		}
		super.setParent(parent);
	}

	/**
	 * Set the root folder for the entire tree.
	 * 
	 * @param root
	 */
	public void setTreeRoot(InstitutionalCollection root) {
		setRoot(root);
		for(InstitutionalCollection c: children)
		{
			c.setTreeRoot(root);
		}
	}

	/**
	 * Set the version of data for the database data
	 * of this collection.
	 * 
	 * @param version
	 */
	public void setVersion(int version) {
		this.version = version;
	}
		
	/**
	 * Get the path.  This should be
	 * the repository name plus the path of all 
	 * parents.
	 * 
	 * collection and the repository does not exist.
	 *  
	 * @return the path
	 */
	public String getPath() 
	{
		return path;
	}
	
	/**
	 * Get the full path of this collection.
	 * 
	 * @return the full path
	 */
	public String getFullPath()
	{
		return getPath() + name + PATH_SEPERATOR;
	}

	/**
	 * Set the conceptual path for this collection.
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

		// add the end seperator
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
		
        //make sure there is a beginning seperator
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
	 * the collection.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		value += getPath() == null? 0 : getPath().hashCode();
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
		if (!(o instanceof InstitutionalCollection)) return false;

		final InstitutionalCollection other = (InstitutionalCollection) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		if( ( repository != null && !repository.equals(other.getRepository()) ) ||
			( repository == null && other.getRepository() != null ) ) return false;
		
		if( ( getPath() != null && !getPath().equals(other.getPath()) ) ||
		    ( getPath() == null && other.getPath() != null ) ) return false;

		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ InstiutionalCollection id = ");
		sb.append(id);
		sb.append(" left value = ");
		sb.append(leftValue);
		sb.append(" right value = ");
		sb.append(rightValue);
		sb.append(" publicly viewable = ");
		sb.append(publiclyViewable);
		sb.append( " name = ");
		sb.append(name);
		sb.append(" description = ");
		sb.append(description);
		sb.append( " path = ");
		sb.append(path);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * The repository this collection belongs to.
	 * 
	 * @return the repository
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * Set the repository this collection belongs to.
	 * @param repository
	 */
	public void setRepository(Repository repository) {
		this.repository = repository;
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
		for(InstitutionalCollection c: children)
		{
			c.updatePaths(getFullPath());
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#updateLeftRightValues(java.lang.Long, java.lang.Long)
	 */
	protected void updateLeftRightValues(Long change, Long minValue)
	{
		super.updateLeftRightValues(change, minValue);
	}

	/**
	 * Compares two collections by name.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object other) {
		InstitutionalCollection c = (InstitutionalCollection)other;
		return this.getName().compareTo(c.getName());
	}

	/**
	 * Set of subscriptions for this institutional collection
	 * @return
	 */
	public Set<InstitutionalCollectionSubscription> getSubscriptions() {
		return Collections.unmodifiableSet(subscriptions);
	}

	/**
	 * Set the subscriptions for this institutional collection
	 * @param subscriptions
	 */
	void setSubscriptions(
			Set<InstitutionalCollectionSubscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.FileSystem#getFileSystemType()
	 */
	public FileSystemType getFileSystemType() {
		return fileSystemType;
	}

	/**
	 * @param fileSystemType
	 */
	public void setFileSystemType(FileSystemType fileSystemType) {
		this.fileSystemType = fileSystemType;
	}

	/**
	 * Set the tree root.
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#setRoot(edu.ur.tree.PreOrderTreeSetNodeBase)
	 */
	protected void setRoot(PreOrderTreeSetNodeBase root) {
		this.treeRoot = (InstitutionalCollection)root;
	}

	/**
	 * Set of pictures for this collection.
	 * 
	 * @return
	 */
	public Set<IrFile> getPictures() {
		return Collections.unmodifiableSet(pictures);
	}
	
	/**
	 * Add the picture to this institutional collection.
	 * 
	 * @param file - picture to add
	 */
	public void addPicture(IrFile file)
	{
		pictures.add(file);
	}
	
	/**
	 * Get the picture with the specified id.  This includes looking
	 * at the primary picture.
	 * 
	 * @param id - ir file id
	 * @return the found file otherwise null.
	 */
	public IrFile getPicture(Long id)
	{
		if( primaryPicture != null && primaryPicture.getId().equals(id))
		{
			return primaryPicture;
		}
		else
		{
		    for(IrFile f : pictures)
		    {
			    if( f.getId().equals(id))
			    {
				    return f;
			    }
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
	 * Set the pictures for this institutional collection 
	 * @param pictures
	 */
	void setPictures(Set<IrFile> pictures) {
		this.pictures = pictures;
	}

	/**
	 * Primary picture for this collection.
	 * 
	 * @return
	 */
	public IrFile getPrimaryPicture() {
		return primaryPicture;
	}

	/**
	 * Primary picture for this collection.
	 * 
	 * @param primaryPicture
	 */
	public void setPrimaryPicture(IrFile primaryPicture) {
		this.primaryPicture = primaryPicture;
	}

	/**
	 * Return true if this collection is publicly viewable.
	 * 
	 * @return
	 */
	public boolean isPubliclyViewable() {
		return publiclyViewable;
	}
	
	/**
	 * Returns true if this collection is publicly viewable.
	 * 
	 * @return
	 */
	public boolean getPubliclyViewable()
	{
		return publiclyViewable;
	}

	/**
	 * Set to true if this is publicly viewable.
	 * 
	 * @param publiclyViewable
	 */
	public void setPubliclyViewable(boolean publiclyViewable) {
		this.publiclyViewable = publiclyViewable;
	}

	public Set<ReviewableItem> getReviewableItems() {
		return reviewableItems;
	}

	public void setReviewableItems(Set<ReviewableItem> reviewableItems) {
		this.reviewableItems = reviewableItems;
	}
	
	public ReviewableItem addReviewableItem(GenericItem item) throws CollectionDoesNotAcceptItemsException { 
		item.setLocked(true);
		ReviewableItem reviewableItem = new ReviewableItem(item, this);
		reviewableItems.add(reviewableItem);
		
		return reviewableItem;
	}
	
	/**
     * Get reviewable item by item version
     *
     * @param item Item that is pending review submission
     *
     * @return Reviewableitem if found
     */
	public ReviewableItem getReviewableItem(GenericItem item) {

		ReviewableItem reviewableItem = null;
		
		Iterator<ReviewableItem> iter = reviewableItems.iterator();
		boolean found = false;
		
		while( iter.hasNext() && !found)
		{
			ReviewableItem i = iter.next();
			if( i.getItem().equals(item))
			{
				reviewableItem = i;
				found = true;
		    }
		}


		return reviewableItem; 
	}

	/**
     * Get reviewable item by item version
     *
     * @param item Item that is pending review submission
     *
     * @return Reviewableitem if found
     */
	public ReviewableItem getReviewPendingItem(GenericItem item) {

		ReviewableItem reviewableItem = null;
		
		Iterator<ReviewableItem> iter = reviewableItems.iterator();
		boolean found = false;
		
		while( iter.hasNext() && !found)
		{
			ReviewableItem i = iter.next();
			if( i.getItem().equals(item) && (i.getReviewStatus().equals(ReviewableItem.PENDING_REVIEW)))
			{
				reviewableItem = i;
				found = true;
		    }
		}


		return reviewableItem; 
	}
	

	/**
	 * Subscribe a user to this collection - user is added to the set of subscriptions.
	 * 
	 * @param user - user to subscribe to this collection
	 * @return the subscription object created.
	 */
	public InstitutionalCollectionSubscription addSuscriber(IrUser user) {
		InstitutionalCollectionSubscription subscription = new InstitutionalCollectionSubscription(this, user); 
		subscriptions.add(subscription);
		return subscription;
	}

	/**
	 * Remove subscription
	 * 
	 * @param subscriber
	 */
	public void removeSubscriber(InstitutionalCollectionSubscription subscription) {
		subscriptions.remove(subscription);
	}
	
	/**
	 * Remove subscription
	 * 
	 * @param subscriber
	 */
	public void removeSubscriber(IrUser user) {
		InstitutionalCollectionSubscription s = getSubscription(user);
		removeSubscriber(s);
	}
	
	/**
	 * Get the institutional collection subscription for the user.
	 * 
	 * @param user
	 * @return the subscription if found otherwise null.
	 */
	public InstitutionalCollectionSubscription getSubscription(IrUser user)
	{
		for( InstitutionalCollectionSubscription subscription : subscriptions)
		{
			if( subscription.getUser().equals(user))
			{
				return subscription;
			}
		}
		return null;
	}
	
	/**
	 * Check if user has subscribed to this collection
	 * 
	 * @param user
	 * @return true if the user is subscribed to the collection
	 */
	public boolean hasSubscriber(IrUser user) {
		for( InstitutionalCollectionSubscription subscription : subscriptions)
		{
			if( subscription.getUser().equals(user))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Copyright for the collection.
	 * 
	 * @return
	 */
	public String getCopyright() {
		return copyright;
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
    public boolean moveLink(InstitutionalCollectionLink link, int position)
    {
    	InstitutionalCollectionLink theLink = this.getLink(link.getName());
    	if( theLink == null)
    	{
    		return false;
    	}
    	
    	int currentLinkPosition = theLink.getOrder();
    	
    	// if the position is greater than the size of the list put it at the end
		if(position > (links.size() - 1))
		{
			position = links.size() - 1;
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
		

		for(InstitutionalCollectionLink aLink : links)
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
    	Collections.sort(links , new AscendingOrderComparator());
    	return true;
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
	public InstitutionalCollectionLink addLink(String name, String url) throws DuplicateNameException
	{
		
		if( this.getLink(name) != null)
		{
			throw new DuplicateNameException("The link name already exists for link " + this.getLink(name), name );
		}
		InstitutionalCollectionLink max = null;
		if( links.size() > 0 )
		{
		    max = Collections.max(links, new AscendingOrderComparator());
		}
		
		int order = 0;
		if( max != null )
		{
			order = max.getOrder() + 1;
		}
		InstitutionalCollectionLink link = new InstitutionalCollectionLink(name, url, order, this);
		links.add(link);
		return link;
	}
	
	/**
	 * Get the link by the specified name, if it does not exist null is returned.
	 * 
	 * @param name - name of the link
	 * @return - the link found or null if not found
	 */
	public InstitutionalCollectionLink getLink(String name)
	{
		for(InstitutionalCollectionLink l : links)
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
	public InstitutionalCollectionLink getLink(Long id)
	{
		for(InstitutionalCollectionLink l : links)
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
	public boolean removLink(InstitutionalCollectionLink link)
	{
		boolean removed = links.remove(link);
		
		if( removed )
		{
		    
		   Collections.sort(links, new AscendingOrderComparator());
		   int index = 0;
		   for(InstitutionalCollectionLink l : links)
		   {
			   l.setOrder(index);
			   index = index + 1;
		   }
		}
		
		return removed;
	}
	


	/**
	 * Copyright for the collection.
	 * 
	 * @param copyright
	 */
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public List<InstitutionalCollectionLink> getLinks() {
		return Collections.unmodifiableList(links);
	}

	void setLinks(List<InstitutionalCollectionLink> links) {
		this.links = links;
	}

}
