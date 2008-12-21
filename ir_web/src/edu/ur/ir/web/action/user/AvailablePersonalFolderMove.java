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
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * The action determines the available folders the user can
 * move the information they have selected to.
 * 
 * @author Nathan Sarr
 *
 */
public class AvailablePersonalFolderMove extends ActionSupport implements UserIdAware{

	/** user who owns the folders and files */
	private Long userId;
	
	/** Ids of folder that are selected to be moved */
	private Long[] folderIds;
	
	/** parent folder the current folders are in */
	private Long parentFolderId;
	
	/**  Collection data access  */
	private UserService userService;
	
	/** User file system service. */
	private UserFileSystemService userFileSystemService;
	
	/**  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(AvailablePersonalFolderMove.class);
	
	/** Eclipse generated id */
	private static final long serialVersionUID = -499319317122591637L;
	
	/**  Tree build for display */
	List<PersonalFolderTree> folderTrees;
	
	/** User making the request */
	private IrUser user;
	
	public String execute()
	{
		user = userService.getUser(userId, false);
		List<Long> ids = new LinkedList<Long>();
		
		if( folderIds != null )
		{ 
		    for(Long id : folderIds)
		    {
			    ids.add(id);
		    }
		}
		
		    
		List<PersonalFolder> availableFolders = userFileSystemService.getAllFoldersNotInChildFolders(ids, userId, parentFolderId);
		log.debug("availableFolders="+availableFolders);
	    folderTrees = buildDisplayTree(availableFolders);
	    if(log.isDebugEnabled())
	    {
	    	drawDisplayTree(folderTrees, 0);
	    }
	
		return SUCCESS;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long[] getFolderIds() {
		return folderIds;
	}

	public void setFolderIds(Long[] folderIds) {
		this.folderIds = folderIds;
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

	public Long getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}
	
	/**
	 * Simple class to help with the display of the tree
	 * for moving.
	 * 
	 * @author Nathan Sarr
	 *
	 */
	public class PersonalFolderTree
	{
		private PersonalFolder root;
		
		private List<PersonalFolderTree> children = new LinkedList<PersonalFolderTree>();
		
		public PersonalFolderTree(PersonalFolder root)
		{
			this.root = root;
		}

		public PersonalFolder getRoot() {
			return root;
		}

		public void setRoot(PersonalFolder root) {
			this.root = root;
		}
		
		public void addChild(PersonalFolderTree pft)
		{
			children.add(pft);
		}

		public List<PersonalFolderTree> getChildren() {
			return Collections.unmodifiableList(children);
		}
	}
	
	/**
	 * Build a display tree
	 * 
	 * @param folders
	 * @return
	 */
	private List<PersonalFolderTree> buildDisplayTree(List<PersonalFolder> folders)
	{
		List<PersonalFolderTree> folderTree = new LinkedList<PersonalFolderTree>();
		for(PersonalFolder f : folders)
		{
			if(f.getParent() == null)
			{
				PersonalFolderTree pft = new PersonalFolderTree(f);
				folderTree.add(pft);
				addChildren(pft, folders);
			}
		}
		
		return folderTree;
		
	}
	
	/**
	 * Add the children to the tree.
	 * 
	 * @param root
	 * @param folders
	 */
	private void addChildren(PersonalFolderTree root, List<PersonalFolder> folders)
	{
		for(PersonalFolder f : folders)
		{
			if( f.getParent() != null && f.getParent().getId().equals(root.getRoot().getId()))
			{
				PersonalFolderTree pft = new PersonalFolderTree(f);
				root.addChild(pft);
				addChildren(pft, folders);
			}
		}
	}
	
	/**
	 * Helper method to visualize the tree.
	 * 
	 * @param trees
	 * @param tabs
	 */
	private void drawDisplayTree(List<PersonalFolderTree> trees, int tabs)
	{
		for(PersonalFolderTree tree : trees)
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

	public List<PersonalFolderTree> getFolderTrees() {
		return folderTrees;
	}

	public void setFolderTrees(List<PersonalFolderTree> folderTrees) {
		this.folderTrees = folderTrees;
	}

	public IrUser getUser() {
		return user;
	}

	public void setUser(IrUser user) {
		this.user = user;
	}

	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

}
