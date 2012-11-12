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

package edu.ur.dspace.load;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;


import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

import edu.ur.dspace.model.Community;
import edu.ur.dspace.model.EpersonPermission;
import edu.ur.dspace.model.GroupPermission;
import edu.ur.dspace.model.Link;
import edu.ur.dspace.model.PermissionConstants;
import edu.ur.dspace.util.FileZipperUtil;
import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.transformer.ThumbnailTransformerService;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSecurityService;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupService;
import edu.ur.ir.user.UserService;

/**
 * Default community importer.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultCommunityImporter implements CommunityImporter{
	
	
	/**  Service for dealing with institutional collections */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Set of security related services */
	private InstitutionalCollectionSecurityService institutionalCollectionSecurityService;
	
	/** Service for dealing with repositories */
	private RepositoryService repositoryService;
	
	/** Service for dealing with users */
	private UserService userService;
	
	/** service to create thumbnails  */
	private ThumbnailTransformerService thumbnailTransformerService;

	/**  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(DefaultCommunityImporter.class);
	
	/** template for accessing the database */
	private JdbcTemplate jdbcTemplate;
	
	/** Service to deal with user groups */
	private UserGroupService userGroupService;
	
	/**
	 * Data source for accessing the database.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
	    this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	

	/* (non-Javadoc)
	 * @see edu.ur.dspace.load.CommunityImporter#ImportCommunities(java.lang.String)
	 */
	public void ImportCommunities(String zipFile, Repository repository) throws IOException, DuplicateNameException, 
	IllegalFileSystemNameException  {
		 File zip = new File(zipFile);
		 if(!zip.exists())
		 {
			throw new IllegalStateException("File " +  zipFile + " does not exist");
		 }
		 File communityXmlFile = FileZipperUtil.getZipEntry(zip, "community.xml", "loaded-community.xml");
         List<Community> communities = getCommunities(communityXmlFile);
		 loadCommunities(communities, zip, repository);
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.dspace.load.CommunityImporter#getCommunities(java.io.File)
	 */
	public List<Community> getCommunities(File communityXmlFile) throws IOException
	{
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder;
		 LinkedList<Community> communities = new LinkedList<Community>();
		 
		 try {
			builder = factory.newDocumentBuilder();
		 } catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		 }
		 
		 DOMImplementation impl = builder.getDOMImplementation();
		 DOMImplementationLS domLs = (DOMImplementationLS)impl.getFeature("LS" , "3.0");
		 LSInput lsIn = domLs.createLSInput();
		 LSParser parser = domLs.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
		 
		 lsIn.setEncoding("UTF-8");

		 FileInputStream fileInputStream = null;
		 InputStreamReader inputStreamReader = null;
		 
		 try
		 {
		     try {
			    fileInputStream = new FileInputStream(communityXmlFile);
		     } catch (FileNotFoundException e) {
			    throw new IllegalStateException(e);
		     }
		
		     try {
			    inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
		     } catch (UnsupportedEncodingException e) {
			    throw new IllegalStateException(e);
		     }
		     
		     lsIn.setCharacterStream(inputStreamReader);
		     Document doc = parser.parse(lsIn);
		     Element root = doc.getDocumentElement();
		 
		     NodeList nodeList = root.getChildNodes();
		 
		 
		     for( int index = 0; index < nodeList.getLength(); index++)
		     {
	             Node child = nodeList.item(index);
	             communities.add(getCommunity(child));
	         }
		 }
		 finally
		 {
			 if( inputStreamReader != null )
			 {
				 inputStreamReader.close();
				 inputStreamReader = null;
			 }
			 if( fileInputStream != null)
			 {
				 fileInputStream.close();
				 fileInputStream = null;
			 }
		 }
		 
		 return communities;
	}
	
	/**
	 * Create a community from the xml data.
	 * 
	 * @param node - root community node
	 * @return
	 */
	private Community getCommunity(Node node)
	{
		Community c = new Community();
		
        NodeList children = node.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
            System.out.println(child.getNodeName());
            System.out.println(child.getTextContent());
            if( child.getNodeName().equals("name"))
            {
            	c.name = child.getTextContent();
            }
            else if( child.getNodeName().equals("id"))
            {
            	c.id= new Long(child.getTextContent());
            }
            else if( child.getNodeName().equals("introductory_text"))
            {
            	c.introductoryText = child.getTextContent();
            }
            else if( child.getNodeName().equals("side_bar_text"))
            {
            	c.sideBarText = child.getTextContent();
            }
            else if( child.getNodeName().equals("copyright"))
            {
            	c.copyright = child.getTextContent();
            }
            else if( child.getNodeName().equals("logo_file_name"))
            {
            	c.logoFileName = child.getTextContent();
            }
            else if( child.getNodeName().equals("links"))
            {
            	getLinks(c, child);
            }
            else if( child.getNodeName().equals("group_permissions"))
            {
            	getGroupPermissions(c, child);
            }
            else if( child.getNodeName().equals("eperson_permissions"))
            {
            	getEpersonPermissions(c, child);
            }
            
        }
		return c;
	}
	
	/**
	 * Get the links for a community.
	 * 
	 * @param c - community to add the link inforamtion to
	 * @param linksNode - xml link data
	 */
	private void getLinks(Community c, Node linksNode)
	{
		NodeList children = linksNode.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node link = children.item(index);
        	c.links.add(getLink(link));
        }
	}
	
	/**
	 * Get a link from a individual link node
	 * @param linkNode - xml containing link information
	 * @return - created link
	 */
	private Link getLink(Node linkNode)
	{
		Link link = new Link();
		
		NodeList children = linkNode.getChildNodes();
		for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
        	if( child.getNodeName().equals("name"))
        	{
        		link.name = child.getTextContent();
        	}
        	else if(child.getNodeName().equals("url") )
        	{
        		link.value = child.getTextContent();
        	}
        }
		return link;
	}
	
	/**
	 * Get the group permissions for a community.
	 * 
	 * @param c - community that has the permissions
	 * @param groupPermissionsNode - xml node containing the data
	 */
	private void getGroupPermissions(Community c, Node groupPermissionsNode)
	{
		NodeList children = groupPermissionsNode.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node groupPermission = children.item(index);
        	c.groupPermissions.add(getGroupPermission(groupPermission));
        }
	}
	
	/**
	 * Create a group permission from a individual permission node
	 * 
	 * @param linkNode - xml node containing link information
	 * @return the found group permission
	 */
	private GroupPermission getGroupPermission(Node linkNode)
	{
		GroupPermission groupPermission = new GroupPermission();
		
		NodeList children = linkNode.getChildNodes();
		for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
        	if( child.getNodeName().equals("action_id"))
        	{
        		groupPermission.action = new Integer(child.getTextContent()).intValue();
        	}
        	else if(child.getNodeName().equals("group_id") )
        	{
        		groupPermission.groupId = new Long(child.getTextContent());
        	}
        }
		return groupPermission;
	}
	
	/**
	 * Get the group permissions for a community.
	 * 
	 * @param c - community to add the data to
	 * @param epersonPermissionsNode - xml node containing the permission information
	 */
	private void getEpersonPermissions(Community c, Node epersonPermissionsNode)
	{
		NodeList children = epersonPermissionsNode.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node epersonPermission = children.item(index);
        	c.epersonPermissions.add(getEpersonPermission(epersonPermission));
        }
	}
	
	/**
	 * Create a group permission from a individual permission node
	 * @param epersonPermissionNode - eperson data containging permissions.
	 * 
	 * @return - created eperson permissions
	 */
	private EpersonPermission getEpersonPermission(Node epersonPermissionNode)
	{
		EpersonPermission epersonPermission = new EpersonPermission();
		
		NodeList children = epersonPermissionNode.getChildNodes();
		for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
        	if( child.getNodeName().equals("action_id"))
        	{
        		epersonPermission.action = new Integer(child.getTextContent()).intValue();
        	}
        	else if(child.getNodeName().equals("group_id") )
        	{
        		epersonPermission.epersonId = new Long(child.getTextContent());
        	}
        }
		return epersonPermission;
	}
	
	
	/**
	 * Load the communities into the new urresearch system.
	 * 
	 * @param communities - list of communities to load
	 * @param zipFile - zip file containnig the log files
	 * @param repo - repository to add the file information to
	 * 
	 * @throws IOException
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 */
	private void loadCommunities(List<Community> communities, File zipFile, Repository repo) throws IOException, DuplicateNameException, IllegalFileSystemNameException
	{
		for(Community community : communities)
		{
			InstitutionalCollection collection = repo.createInstitutionalCollection(community.name);
			collection.setDescription(community.introductoryText);
			collection.setCopyright(community.copyright);
			
			for( Link link : community.links)
			{
				if( link.name != null )
				{
				    collection.addLink(link.name, link.value);
				}
				else
				{
					System.out.println("could not add link " + link);
				}
			}
			institutionalCollectionService.saveCollection(collection);
			
			this.jdbcTemplate.execute("insert into dspace_convert.community(dspace_community_id, ur_research_collection_id) values (" + community.id + "," + collection.getId() + ")");
			
			if( community.logoFileName != null && !community.logoFileName.trim().equals(""))
			{
			    File f = FileZipperUtil.getZipEntry(zipFile, community.logoFileName, "logoFile" );
			    System.out.println("logo file name = " + community.logoFileName);
			    IrFile logo = repositoryService.createIrFile(repo, f, community.logoFileName, "imported dspace logo");
			    System.out.println("file extension = " + logo.getFileInfo().getExtension());
			
			    collection.setPrimaryPicture(logo);
			    thumbnailTransformerService.transformFile(repo, logo);
			}
			
			loadGroupPermissions(collection, community);
			loadEpersonPermissions(collection, community);
			
		}
	}
	
	/**
	 * Load the group permissions
	 * 
	 * @param collection - urresearch system collection to transfer the permissions to
	 * @param community - related community containing the permissions
	 */
	private void loadGroupPermissions(InstitutionalCollection collection, Community community)
	{
		log.debug("loading group permissions for institutional collection " + collection);
		// colleciton is publicly viewable if the community is 
		collection.setPubliclyViewable(PermissionConstants.hasAnonymousRead(community.groupPermissions));
				
		
		for(GroupPermission groupPermission : community.groupPermissions)
		{
			log.debug("loading groupPermission  " + groupPermission);
			if( !groupPermission.groupId.equals(PermissionConstants.ANONYMOUS_GROUP_ID))
			{
				// get the group we want to add permissions to
			    Long urreseachGroupId= getUrGroupId(groupPermission.groupId);
			    if( urreseachGroupId != null)
			    {
				    IrUserGroup userGroup = userGroupService.get(urreseachGroupId, false);
				    if( userGroup == null)
				    {
				    	throw new IllegalStateException("Could not find user group for id  " + urreseachGroupId);
				    }
				    
				    if( groupPermission.action == PermissionConstants.ADD)
				    {
				        institutionalCollectionSecurityService.givePermission(collection, userGroup, 
				        		InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION);
				    }
				    else if ( groupPermission.action == PermissionConstants.DEFAULT_BITSTREAM_READ)
				    {
				    	// no action
				    }
				    else if ( groupPermission.action == PermissionConstants.DEFAULT_ITEM_READ)
				    {
				    	// no action
				    }
				    else if ( groupPermission.action == PermissionConstants.DELETE)
				    {
				    	institutionalCollectionSecurityService.givePermission(collection, userGroup, 
				        		InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION);
				    }
				    else if ( groupPermission.action == PermissionConstants.READ)
				    {
				    	institutionalCollectionSecurityService.givePermission(collection, userGroup, 
				        		InstitutionalCollectionSecurityService.VIEW_PERMISSION);
				    }
				    else if ( groupPermission.action == PermissionConstants.REMOVE)
				    {
				    	institutionalCollectionSecurityService.givePermission(collection, userGroup, 
				        		InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION);
				    }
				    else if ( groupPermission.action == PermissionConstants.WORKFLOW_ABORT)
				    {
				    	// no action
				    }
				    else if ( groupPermission.action == PermissionConstants.WORKFLOW_STEP_1)
				    {
				    	// no action
				    }
				    else if ( groupPermission.action == PermissionConstants.WORKFLOW_STEP_2)
				    {
				    	// no action
				    }
				    else if ( groupPermission.action == PermissionConstants.WORKFLOW_STEP_3)
				    {
				    	// no action
				    }  
				    else if ( groupPermission.action == PermissionConstants.WRITE)
				    {
				    	institutionalCollectionSecurityService.givePermission(collection, userGroup, 
				        		InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION);
				    }
			    }
			    else
			    {
				    throw new IllegalStateException("Could not find user group for permission " + groupPermission);
			    }
			}
		}
	}
	
	/**
	 * Get the new urresearch group id based on the dspace group id.
	 * 
	 * @param dspaceCollectionId - current dspace collection id
	 * @return id of the urresearch group id
	 */
	private Long getUrGroupId(long dspaceGroupId)
	{
		return jdbcTemplate.queryForLong("select ur_research_group_id from dspace_convert.ir_group where dspace_group_id = " + dspaceGroupId);
	}
	
	/**
	 * Load the eperson permission.
	 * 
	 * @param collection - urresearch system collection to transfer the permissions to
	 * @param community - related community containing the permissions
	 */
	private void loadEpersonPermissions(InstitutionalCollection collection, Community community)
	{
		log.debug("loading eperson permissions for institutional collection " + collection);
		for(EpersonPermission epersonPermission : community.epersonPermissions)
		{
			log.debug("loading epersonPermission  " + epersonPermission);
			Long urresearchUserId = getUrResearchUser(epersonPermission.epersonId);
			
			
			if( urresearchUserId == null )
			{
				throw new IllegalStateException("User for eperson id " +
						epersonPermission.epersonId + " could not be found" );
			}
			
			IrUser user = userService.getUser(urresearchUserId, false);
			
			if( user == null)
			{
				throw new IllegalStateException("Could not find user with id " + urresearchUserId);
			}
			
			String userGroupName = user.getUsername() + "_group";
			
			IrUserGroup userGroup = userGroupService.get(userGroupName);
			
			if( userGroup == null)
			{
			    userGroup = new IrUserGroup(userGroupName);	
			    userGroup.setDescription("Created as user import permssion from dspace");
			    userGroupService.save(userGroup);
			}
			
			if( epersonPermission.action == PermissionConstants.ADD)
		    {
		        institutionalCollectionSecurityService.givePermission(collection, userGroup, 
		        		InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION);
		    }
		    else if ( epersonPermission.action == PermissionConstants.DEFAULT_BITSTREAM_READ)
		    {
		    	// no action
		    }
		    else if ( epersonPermission.action == PermissionConstants.DEFAULT_ITEM_READ)
		    {
		    	// no action
		    }
		    else if ( epersonPermission.action == PermissionConstants.DELETE)
		    {
		    	institutionalCollectionSecurityService.givePermission(collection, userGroup, 
		        		InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION);
		    }
		    else if ( epersonPermission.action == PermissionConstants.READ)
		    {
		    	institutionalCollectionSecurityService.givePermission(collection, userGroup, 
		        		InstitutionalCollectionSecurityService.VIEW_PERMISSION);
		    }
		    else if ( epersonPermission.action == PermissionConstants.REMOVE)
		    {
		    	institutionalCollectionSecurityService.givePermission(collection, userGroup, 
		        		InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION);
		    }
		    else if ( epersonPermission.action == PermissionConstants.WORKFLOW_ABORT)
		    {
		    	// no action
		    }
		    else if ( epersonPermission.action == PermissionConstants.WORKFLOW_STEP_1)
		    {
		    	// no action
		    }
		    else if ( epersonPermission.action == PermissionConstants.WORKFLOW_STEP_2)
		    {
		    	// no action
		    }
		    else if ( epersonPermission.action == PermissionConstants.WORKFLOW_STEP_3)
		    {
		    	// no action
		    }  
		    else if ( epersonPermission.action == PermissionConstants.WRITE)
		    {
		    	institutionalCollectionSecurityService.givePermission(collection, userGroup, 
		        		InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION);
		    }
			
		}
		
		
	}
	

	/**
	 * Get the urresearch user id for the current dspace eperson id.  The takes the old dspace eperson id
	 * 
	 * @param dspaceCommunityParent
	 * @return
	 */
	private Long getUrResearchUser(long epersonId)
	{
		return jdbcTemplate.queryForLong("select ur_research_user_id from dspace_convert.ir_user where dspace_eperson_id = " + epersonId);
	}
	
	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}


	public InstitutionalCollectionSecurityService getInstitutionalCollectionSecurityService() {
		return institutionalCollectionSecurityService;
	}


	public void setInstitutionalCollectionSecurityService(
			InstitutionalCollectionSecurityService institutionalCollectionSecurityService) {
		this.institutionalCollectionSecurityService = institutionalCollectionSecurityService;
	}


	public UserGroupService getUserGroupService() {
		return userGroupService;
	}


	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}


	public UserService getUserService() {
		return userService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public ThumbnailTransformerService getThumbnailTransformerService() {
		return thumbnailTransformerService;
	}


	public void setThumbnailTransformerService(
			ThumbnailTransformerService thumbnailTransformerService) {
		this.thumbnailTransformerService = thumbnailTransformerService;
	}
	
	

}
