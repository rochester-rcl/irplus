package edu.ur.ir.groupspace;

import java.util.List;

import edu.ur.dao.CrudDAO;

public interface GroupWorkspaceProjectPageFileSystemLinkDAO extends CrudDAO<GroupWorkspaceProjectPageFileSystemLink>
{
	/**
	 * Get the root links for given group workspace project page
	 * 
     * @param projectPageId - the id of the project page
     * 
	 * @return List of root links found.
	 */	
	public List<GroupWorkspaceProjectPageFileSystemLink> getRootLinks(final Long researcherId);
    
	/**
	 * Get r links for specified group workspace project page and specified parent folder
	 * 
	 * @param projectPageId - the id of the project page
     * @param parentFolderId - the id of the parent folder id for the folders
     * 
	 * @return List of links found.
	 */
	public List<GroupWorkspaceProjectPageFileSystemLink> getLinks(final Long projectPageId, final Long parentFolderId);


    /**
	 * Find the specified links for the given researcher.
	 * 
	 * @param projectPageId the link
	 * @param linkIds Ids of the link
	 * 
	 * @return List of links found
	 */
	public List<GroupWorkspaceProjectPageFileSystemLink> getLinks(final Long projectPageId, final List<Long> linkIds);

}
