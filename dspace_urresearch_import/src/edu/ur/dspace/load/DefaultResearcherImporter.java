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

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

import edu.ur.dspace.model.DspaceResearcher;
import edu.ur.dspace.model.DspaceResearcherFolder;
import edu.ur.dspace.model.Link;
import edu.ur.dspace.model.ResearcherFolderLink;
import edu.ur.dspace.util.FileZipperUtil;
import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TemporaryFileCreator;
import edu.ur.ir.file.transformer.BasicThumbnailTransformer;
import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.handle.HandleService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.Field;
import edu.ur.ir.researcher.FieldService;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherInstitutionalItem;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.researcher.ResearcherIndexService;
import edu.ur.ir.user.Department;
import edu.ur.ir.user.DepartmentService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.RoleService;
import edu.ur.ir.user.UserService;

/**
 * Class for importing researchers.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultResearcherImporter implements ResearcherImporter{
	
	/**  Service for dealing with institutional collections */
	private UserService userService;
	
	/** Service for dealing with repositories */
	private RepositoryService repositoryService;
	
	/** Thumb-nailer  for logo files*/
	private BasicThumbnailTransformer defaultThumbnailTransformer;
	
	/** Temporary file creator to allow a temporary file to be created for processing */
	private TemporaryFileCreator temporaryFileCreator;
	
	/** Service for dealing with researchers in the system */
	private ResearcherService researcherService;
	
	/** Service for indexing researchers */
	private ResearcherIndexService researcherIndexService;
	
	/** Department service class */
	private DepartmentService departmentService;
	
	/** Researcher field service class */
	private FieldService researcherFieldService;

	/**  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(DefaultResearcherImporter.class);
	
	/** template for accessing the database */
	private JdbcTemplate jdbcTemplate;
	
	/** Service for accessing role information */
	private RoleService roleService;
	
	/** Service for dealing with handle based link data */
	private HandleService handleService;

	/** Service for dealing with items */
	private InstitutionalItemService institutionalItemService;


	/**
	 * Data source for accessing the database.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	/* (non-Javadoc)
	 * @see edu.ur.dspace.load.ResearcherImporter#importResearchers(java.lang.String, edu.ur.ir.repository.Repository)
	 */
	public void importResearchers(String zipFile, Repository repository) throws IOException, DuplicateNameException, 
	IllegalFileSystemNameException 
	{
		 File zip = new File(zipFile);
		 if(!zip.exists())
		 {
			throw new IllegalStateException("File " +  zipFile + " does not exist");
		 }
		 
		 File researcherXmlFile = FileZipperUtil.getZipEntry(zip, "researcher.xml", "loaded-researcher.xml");
		 
         List<DspaceResearcher> researchers = getResearchers(researcherXmlFile);
		 
		 loadResearchers(researchers, zip, repository);
		
	}
 
	
	public List<DspaceResearcher> getResearchers(File communityXmlFile)
			throws IOException {
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder;
		 LinkedList<DspaceResearcher> researchers = new LinkedList<DspaceResearcher>();
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
		     } 
		     catch (FileNotFoundException e) 
		     {
			    throw new IllegalStateException(e);
		     }
		 
		     try 
		     {
			    inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
		     } 
		     catch (UnsupportedEncodingException e) 
		     {
			    throw new IllegalStateException(e);
		     }
		     lsIn.setCharacterStream(inputStreamReader);
		     Document doc = parser.parse(lsIn);
		     Element root = doc.getDocumentElement();
		 
		     NodeList nodeList = root.getChildNodes();
		     
		 
		     for( int index = 0; index < nodeList.getLength(); index++)
		     {
	             Node child = nodeList.item(index);
	             researchers.add(getResearcher(child));
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
		 
		 return researchers;
	}
	
	/**
	 * Create a researcher from the xml data.
	 * 
	 * @param node - root researcher node
	 * @return
	 */
	private DspaceResearcher getResearcher(Node node)
	{
		DspaceResearcher r = new DspaceResearcher();
		
        NodeList children = node.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	
        	Node child = children.item(index);
            if( child.getNodeName().equals("dspace_user_id"))
            {
            	r.dspaceUserId = new Long(child.getTextContent()).intValue();
            }
            else if( child.getNodeName().equals("researcher_id"))
            {
            	r.researcherId = new Long(child.getTextContent()).intValue();
            }
            else if( child.getNodeName().equals("researcher_interests"))
            {
            	r.researchInterests = child.getTextContent();
            }
            else if( child.getNodeName().equals("campus_location"))
            {
            	r.campusLocation = child.getTextContent();
            }
            else if( child.getNodeName().equals("teaching_interests"))
            {
            	r.teachingInterests = child.getTextContent();
            }
            else if( child.getNodeName().equals("title"))
            {
            	r.title = child.getTextContent();
            }
            else if( child.getNodeName().equals("department"))
            {
            	r.department = child.getTextContent();
            }
            else if( child.getNodeName().equals("field"))
            {
            	r.field = child.getTextContent();
            }
            else if( child.getNodeName().equals("fax"))
            {
            	r.fax = child.getTextContent();
            }
            else if( child.getNodeName().equals("page_public"))
            {
            	r.pagePublic = new Boolean(child.getTextContent());
            }
            else if( child.getNodeName().equals("logo_file_name"))
            {
            	r.logoFileName = child.getTextContent();
            }
            else if( child.getNodeName().equals("links"))
            {
            	getLinks(r, child);
            }
            else if( child.getNodeName().equals("folders"))
            {
            	getFolders(r, child);
            }
           
            
        }
		return r;
	}
	
	/**
	 * Load the researchers into the new urresearch system.
	 * 
	 * @param collections
	 * @throws IOException 
	 * @throws DuplicateNameException 
	 * @throws IllegalFileSystemNameException 
	 * @throws DuplicateNameException 
	
	 */
	private void loadResearchers(List<DspaceResearcher> researchers, File zipFile, Repository repo) throws IOException, IllegalFileSystemNameException
	{

		for(DspaceResearcher r : researchers)
		{
			long urresearchUserId = this.getUrResearchUser( r.dspaceUserId);
			IrUser user = userService.getUser(urresearchUserId, false);
			
			if(user == null)
			{
				System.out.println("could not import dspace researcher " + r);
			}
			else
			{
				IrRole role = roleService.getRole(IrRole.RESEARCHER_ROLE);
				user.addRole(role);
				Researcher urResearcher = new Researcher(user);
				urResearcher.setCampusLocation(r.campusLocation);
				urResearcher.setFax(r.fax);
				Field field = null;
				urResearcher.setPublic(r.pagePublic);
				urResearcher.setTeachingInterest(r.teachingInterests);
				urResearcher.setResearchInterest(r.researchInterests);
				urResearcher.setTitle(r.title);
				
				
				
				if( r.field != null && !r.field.trim().equals("") )
				{
					field = researcherFieldService.getField(r.field);
					if( field == null )
					{
						field = new Field(r.field);
						researcherFieldService.makeFieldPersistent(field);
					}
					urResearcher.addField(field);
				}
				
				urResearcher.setPublic(r.pagePublic);
				
				if( r.department != null && !r.department.trim().equals("") )
				{
					Department department = departmentService.getDepartment(r.department);
					if( department == null )
					{
						department = new Department(r.department);
						departmentService.makeDepartmentPersistent(department);
					}
					urResearcher.getUser().addDepartment(department);
				}
				jdbcTemplate.execute("insert into dspace_convert.researcher(dspace_researcher_id, ur_research_researcher_id) values (" + r.researcherId + "," + urResearcher.getId() + ")");
			    if( r.logoFileName != null && !r.logoFileName.trim().equals(""))
			    {
			        File f = FileZipperUtil.getZipEntry(zipFile, r.logoFileName, "researcherPictureFile" );
			        System.out.println("researcher file name = " + r.logoFileName);
			        IrFile logo = repositoryService.createIrFile(repo, f, r.logoFileName, "imported researcher picture");
			        System.out.println("file extension = " + logo.getFileInfo().getExtension());
			
			        urResearcher.setPrimaryPicture(logo);
			        
			        try {
					    ThumbnailHelper.thumbnailFile(logo, repo, defaultThumbnailTransformer, 
					    		temporaryFileCreator, repositoryService, "researcher thumbnail");
				    } catch (Exception e) {
					    log.debug("could not create thumbnail for researcher " + urResearcher, e);
				    }
			    }
			    
			    if( r.links != null )
			    {
			        log.debug("links size = " + r.links.size());
			    }
			    for(Link l : r.links)
			    {
			    	log.debug("adding personal link " + l);
			    	try {
						urResearcher.addPersonalLink(l.name, l.value);
					} catch (DuplicateNameException e) {
						log.debug(" could not add link with name " + l.name  + " because a link with that name already exists");
					}
			    }
			    
			    if(r.folder != null)
			    {
			    	processFolders(urResearcher, r.folder, null);
			    }
			    
			    
			    printResearcher(urResearcher);
			    researcherService.saveResearcher(urResearcher);
			    try {
					researcherIndexService.addToIndex(urResearcher,  new File(repo.getResearcherIndexFolder()) );
				} catch (NoIndexFoundException e) {
					log.error("Could not index researcher " + urResearcher, e);
				} 
			}
		}
	}
	
	private void printResearcher(Researcher urResearcher)
	{
		log.debug("Printing researcher " + urResearcher);
		for(ResearcherFolder f : urResearcher.getRootFolders())
		{
			printResearcherFolderTree(f);
		}
	}
	
	private void printResearcherFolderTree(ResearcherFolder f)
	{
		log.debug("Found folder " + f);
		log.debug("Children are : " );
		for( ResearcherFolder folder : f.getChildren())
		{
			printResearcherFolderTree(folder);
		}
	}
	
	/**
	 * Load all the folders for the researcher.
	 * 
	 * @param urResearcher
	 * @param tree
	 * @param currentParent
	 * @throws DuplicateNameException
	 */
	private void processFolders(Researcher urResearcher, DspaceResearcherFolder tree, ResearcherFolder currentParent) 
	{
		ResearcherFolder f = null;
		if( currentParent == null)
		{
			try {
				f = urResearcher.createRootFolder(tree.title);
			} catch (DuplicateNameException e) {
				log.debug("could not add folder " + tree.title + " because a folder with that name already exists");
			}
		}
		else
		{
			try {
				f = currentParent.createChild(tree.title);
			} catch (DuplicateNameException e) {
				log.debug("could not add folder " + tree.title + " because a folder with that name already exists");
			}
		}
		
		f.setDescription(tree.description);
		
		
		if( tree.links != null)
		{
			for(ResearcherFolderLink l : tree.links)
			{
				log.debug("Processing REG_LINK ");
				if( l.linkType.equals("REG_LINK"))
				{
				    try {
						f.createLink(l.title, l.url, l.description);
					} catch (DuplicateNameException e) {
						log.debug("could not add link " + l.title + " because a link with the name already exists");
					}
				}
				else if(l.linkType.equals("DSPACE_LINK"))
				{
					log.debug("Processing DSPACE_LINK " + l.url);
					
					
					String prefixLocalName = l.url.replaceAll("/handle/", "");
					String[] prefixLocalNameParts = prefixLocalName.split("/");
					
					if( prefixLocalNameParts.length == 2)
					{
						String prefix = prefixLocalNameParts[0];
						String localName = prefixLocalNameParts[1];
						log.debug("Found prefix = " + prefix + " and local name = " + localName);
						
						HandleInfo info = handleService.getHandleInfo(prefix.trim() +"/" + localName.trim());
						log.debug( "info = " + info);
						if( info != null )
						{
							InstitutionalItemVersion version = this.institutionalItemService.getInstitutionalItemByHandleId(info.getId());
						    if( version != null )
						    {
						    	InstitutionalItem item = institutionalItemService.getInstitutionalItemByVersionId(version.getVersionedInstitutionalItem().getId());
						    	ResearcherInstitutionalItem ri = new ResearcherInstitutionalItem(f.getResearcher(), item);
						    	ri.setDescription(l.description);
						    	f.addInstitutionalItem(ri);
						    }
						}
						else
						{
							log.debug(" info was null ");
						}
					}
				}
					
			}
		}
			
		for(DspaceResearcherFolder folder : tree.children)
		{
			processFolders(urResearcher, folder, f);
		}
	
	}
	
	
	/**
	 * Get a link from a individual link node
	 * @param linkNode
	 * @return
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
	 * Get a set of folders
	 * 
	 * @param r - researcher 
	 * @param foldersNode - the folders node
	 */
	private void getFolders(DspaceResearcher r, Node foldersNode)
	{
		NodeList children = foldersNode.getChildNodes();
	
		for( int index = 0; index < children.getLength(); index++)
		{
			
			Node child = children.item(index);
			if( child.getNodeName().equals("folder"))
			{
				r.folder = getFolder(r, child);
			}
		}
	}
	
	/**
	 * Deal with a folder.
	 * 
	 * @param r - researcher the folder belongs to
	 * @param folderNode - the folder node
	 * @return
	 */
	private DspaceResearcherFolder getFolder(DspaceResearcher r, Node folderNode)
	{
		DspaceResearcherFolder f = new DspaceResearcherFolder();
		NodeList children = folderNode.getChildNodes();
		for( int index = 0; index < children.getLength(); index++)
		{
			Node child = children.item(index);
			if( child.getNodeName().equals("folder"))
			{
				DspaceResearcherFolder childFolder = getFolder(r, child);
				f.children.add(childFolder);
			}
			else if( child.getNodeName().equals("title"))
			{
				f.title = child.getTextContent();
			}
			else if( child.getNodeName().equals("description"))
			{
				f.description = child.getTextContent();
			}
			else if( child.getNodeName().equals("folder_id"))
			{
				f.folderId = new Long(child.getTextContent());
			}
			else if( child.getNodeName().equals("researcher_id"))
			{
				f.researcherId = new Long(child.getTextContent());
			}
			else if( child.getNodeName().equals("parent_folder_id"))
			{
				f.parentId = new Long(child.getTextContent());
			}
			else if( child.getNodeName().equals("links"))
			{
				NodeList links = child.getChildNodes();
				for( int linkIndex = 0; linkIndex < links.getLength(); linkIndex++)
				{
					log.debug("Trying to process links " + links.item(linkIndex).getNodeName());
					f.links.add(getResearcherFolderLink(links.item(linkIndex)));
				}
				
			}
			
		}
		
		return f;
	}
	
	/**
	 * Get a researcher folder link
	 * 
	 * @param linkNode
	 * @return
	 */
	private ResearcherFolderLink getResearcherFolderLink(Node linkNode)
	{
		ResearcherFolderLink link = new ResearcherFolderLink();
		
		NodeList children = linkNode.getChildNodes();
		for( int index = 0; index < children.getLength(); index++)
        {
			Node child = children.item(index);
			log.debug("Trying to process link node " + child.getNodeName());
        	
        	if( child.getNodeName().equals("title"))
        	{
        		link.title = child.getTextContent();
        	}
        	else if(child.getNodeName().equals("description") )
        	{
        		link.description = child.getTextContent();
        	}
        	else if(child.getNodeName().equals("link_type") )
        	{
        		link.linkType = child.getTextContent();
        	}
        	else if(child.getNodeName().equals("url") )
        	{
        	    link.url = 	child.getTextContent();
        	}
        	else if(child.getNodeName().equals("folder_id") )
        	{
        		link.folderId = new Long(child.getTextContent());
        	}
        	else if(child.getNodeName().equals("link_id") )
        	{
        		link.linkId = new Long(child.getTextContent());
        	}
        	
        }
		log.debug("Returning link " + link);
		return link;
	}
	
	
	/**
	 * Get the links for a researcher.
	 * 
	 * @param r
	 * @param linksNode
	 */
	private void getLinks(DspaceResearcher r, Node linksNode)
	{
		NodeList children = linksNode.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	
        	Node link = children.item(index);
        	r.links.add(getLink(link));
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




	public RepositoryService getRepositoryService() {
		return repositoryService;
	}



	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}



	public BasicThumbnailTransformer getDefaultThumbnailTransformer() {
		return defaultThumbnailTransformer;
	}



	public void setDefaultThumbnailTransformer(
			BasicThumbnailTransformer defaultThumbnailTransformer) {
		this.defaultThumbnailTransformer = defaultThumbnailTransformer;
	}



	public TemporaryFileCreator getTemporaryFileCreator() {
		return temporaryFileCreator;
	}



	public void setTemporaryFileCreator(TemporaryFileCreator temporaryFileCreator) {
		this.temporaryFileCreator = temporaryFileCreator;
	}



	public UserService getUserService() {
		return userService;
	}



	public void setUserService(UserService userService) {
		this.userService = userService;
	}



	public ResearcherService getResearcherService() {
		return researcherService;
	}



	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}



	public DepartmentService getDepartmentService() {
		return departmentService;
	}



	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}



	public FieldService getResearcherFieldService() {
		return researcherFieldService;
	}



	public void setResearcherFieldService(
			FieldService researcherFieldService) {
		this.researcherFieldService = researcherFieldService;
	}



	public RoleService getRoleService() {
		return roleService;
	}



	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}


	public ResearcherIndexService getResearcherIndexService() {
		return researcherIndexService;
	}


	public void setResearcherIndexService(
			ResearcherIndexService researcherIndexService) {
		this.researcherIndexService = researcherIndexService;
	}
	
	public HandleService getHandleService() {
		return handleService;
	}


	public void setHandleService(HandleService handleService) {
		this.handleService = handleService;
	}
	
	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}


	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}


}
