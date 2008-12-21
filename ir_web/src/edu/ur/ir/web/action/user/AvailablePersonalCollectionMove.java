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


package edu.ur.ir.web.action.user;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * The action determines the available collections the user can
 * move the information they have selected to.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class AvailablePersonalCollectionMove extends ActionSupport implements UserIdAware{

	/** Eclipse generated id */
	private static final long serialVersionUID = -8187746706293959529L;

	/** user who owns the collections and items */
	private Long userId;
	
	/** list of collection ids selected*/
	private Long[] collectionIds;	
	
	/** parent collection the current collections are in */
	private Long parentCollectionId;
	
	/**  Collection data access  */
	private UserService userService;
	
	/**  Logger for add personal collection action */
	private static final Logger log = Logger.getLogger(AvailablePersonalCollectionMove.class);
	
	/**  Tree build for display */
	List<PersonalCollectionTree> collectionTrees;
	
	/** User logged in */
	private IrUser user;
	
	/** Service for dealing with user file system. */
	private UserPublishingFileSystemService userPublishingFileSystemService;

	
	/**
	 * Action execute
	 *  
	 */
	public String execute()
	{
		user = userService.getUser(userId, false);
		List<Long> ids = new LinkedList<Long>();
		if( log.isDebugEnabled())
		{
			log.debug("Parent collection id = " + parentCollectionId);
			log.debug("user id = " + userId);
		}
		
		if( collectionIds != null)
		{
		    for(Long id : collectionIds)
		    {
			     ids.add(id);
		    }
		}
		    
		List<PersonalCollection> availableCollections = userPublishingFileSystemService.getAllCollectionsNotInChildCollections(ids, userId, parentCollectionId);

		collectionTrees = buildDisplayTree(availableCollections);
	    if(log.isDebugEnabled())
	    {
	    	drawDisplayTree(collectionTrees, 0);
	    }
	
		return SUCCESS;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}
	
	/**
	 * Simple class to help with the display of the tree
	 * for moving.
	 * 
	 * @author Nathan Sarr
	 *
	 */
	public class PersonalCollectionTree
	{
		private PersonalCollection root;
		
		private List<PersonalCollectionTree> children = new LinkedList<PersonalCollectionTree>();
		
		public PersonalCollectionTree(PersonalCollection root)
		{
			this.root = root;
		}

		public PersonalCollection getRoot() {
			return root;
		}

		public void setRoot(PersonalCollection root) {
			this.root = root;
		}
		
		public void addChild(PersonalCollectionTree pft)
		{
			children.add(pft);
		}

		public List<PersonalCollectionTree> getChildren() {
			return Collections.unmodifiableList(children);
		}
	}
	
	/**
	 * Build a display tree
	 * 
	 * @param collections
	 * @return
	 */
	private List<PersonalCollectionTree> buildDisplayTree(List<PersonalCollection> collections)
	{
		List<PersonalCollectionTree> collectionTree = new LinkedList<PersonalCollectionTree>();
		for(PersonalCollection f : collections)
		{
			if(f.getParent() == null)
			{
				PersonalCollectionTree pft = new PersonalCollectionTree(f);
				collectionTree.add(pft);
				addChildren(pft, collections);
			}
		}
		
		return collectionTree;
		
	}
	
	/**
	 * Add the children to the tree.
	 * 
	 * @param root
	 * @param collections
	 */
	private void addChildren(PersonalCollectionTree root, List<PersonalCollection> collections)
	{
		for(PersonalCollection f : collections)
		{
			if( f.getParent() != null && f.getParent().getId().equals(root.getRoot().getId()))
			{
				PersonalCollectionTree pft = new PersonalCollectionTree(f);
				root.addChild(pft);
				addChildren(pft, collections);
			}
		}
	}
	
	/**
	 * Helper method to visualize the tree.
	 * 
	 * @param trees
	 * @param tabs
	 */
	private void drawDisplayTree(List<PersonalCollectionTree> trees, int tabs)
	{
		for(PersonalCollectionTree tree : trees)
		{
			String tabString = "";
			for( int tabPrint = 0; tabPrint < tabs; tabPrint++)
			{
				tabString += "\t";
			}
			tabString += tree.getRoot().getId() + " - " + tree.getRoot().getName();
			log.debug(tabString);
			
			drawDisplayTree(tree.getChildren(), tabs + 1);
		}
	}

	public List<PersonalCollectionTree> getCollectionTrees() {
		return collectionTrees;
	}

	public void setCollectionTrees(List<PersonalCollectionTree> collectionTrees) {
		this.collectionTrees = collectionTrees;
	}

	public IrUser getUser() {
		return user;
	}

	public void setUser(IrUser user) {
		this.user = user;
	}

	public Long[] getCollectionIds() {
		return collectionIds;
	}

	public void setCollectionIds(Long[] collectionIds) {
		this.collectionIds = collectionIds;
	}

	public UserPublishingFileSystemService getUserPublishingFileSystemService() {
		return userPublishingFileSystemService;
	}

	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}

}
