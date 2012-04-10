package edu.ur.ir.groupspace;

import java.util.List;

import edu.ur.dao.CrudDAO;

public interface GroupWorkspaceProjectPagePublicationDAO extends CrudDAO<GroupWorkspaceProjectPagePublication>
{
	/**
	 * Get the root group workspace project page publications for given project page
	 * 
     * @param project page Id - the id of the project page
	 * @return List of root publications found .
	 */
	public List<GroupWorkspaceProjectPagePublication> getRootPublications(final Long projectPageId);
   
    
	/**
	 * Get group workspace project page publications for specified group workspace project page
	 * and specified parent folder
	 * 
	 * @param projectPageId - the id of the project page
     * @param parentFolderId - the id of the parent folder 
     * 
	 * @return List of publications found.
	 */
	public List<GroupWorkspaceProjectPagePublication> getPublications(final Long projectId, final Long parentFolderId);
	
	
    /**
	 * Find the specified publications for the given group workspace project page.
	 * 
	 * @param projectPageId group workspace project page of the publication
	 * @param publicationIds Ids of  the publication
	 * 
	 * @return List of publications found
	 */
	public List<GroupWorkspaceProjectPagePublication> getPublications(final Long researcherId, final List<Long> publicationIds);

	/**
	 * Get a count of the group workspace project page publications containing this item
	 * 
	 * @param itemId Item id to search for in the group workspace project page publications
	 * 
	 * @return Count of generic item found in researcher
	 */
	public Long getCount(Long itemId);

}
