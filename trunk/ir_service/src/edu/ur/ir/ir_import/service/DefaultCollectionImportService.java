/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.ir_import.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.ir_import.CollectionImportService;
import edu.ur.ir.ir_import.ZipHelper;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;

/**
 * 
 * Collection import service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultCollectionImportService implements CollectionImportService {

	/** eclipse generated id */
	private static final long serialVersionUID = 5266602057958429492L;
	
	// helper to extract information from the zip file
	private ZipHelper zipHelper;
	
	/* service for dealing with repository information */
	private RepositoryService repositoryService;
	
	/* service to deal with institutional collection information */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/*  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultCollectionImportService.class);

	/**
	 * Import the collections in the zip file.
	 * 
	 * @see edu.ur.ir.ir_import.CollectionImportService#importCollections(java.io.File)
	 */
	public void importCollections(File zipFile, Repository repository) {
		if( log.isDebugEnabled() )
		{
			log.debug("import collections");
		}
		try 
		{
			ZipFile zip = new ZipFile(zipFile);
			ZipArchiveEntry entry = zip.getEntry("collection.xml");
			File f = new File("collection.xml");
			zipHelper.getZipEntry(f, entry, zip);
			if( f != null )
			{
				try 
				{
					getCollections(f, repository, zip);
				} 
				catch (DuplicateNameException e) {
					log.error(e);
				}
				FileUtils.deleteQuietly(f);
			}

		} 
		catch (IOException e) 
		{
			log.error(e);
		}

	}
	
	/**
	 * Load the dspace collection information from the xml file.
	 * @throws DuplicateNameException 
	 * 
	 * @see edu.ur.dspace.load.CollectionImporter#getCollections(java.io.File)
	 */
	private void getCollections(File communityXmlFile, Repository repo, ZipFile zip)
			throws IOException, DuplicateNameException {
		 if( log.isDebugEnabled())
		 {
			 log.debug("get collections");
		 }
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder;
		 
		 try 
		 {
			builder = factory.newDocumentBuilder();
		 } 
		 catch (ParserConfigurationException e) 
		 {
			throw new IllegalStateException(e);
		 }
		 
		 DOMImplementation impl = builder.getDOMImplementation();
		 DOMImplementationLS domLs = (DOMImplementationLS)impl.getFeature("LS" , "3.0");
		 LSInput lsIn = domLs.createLSInput();
		 LSParser parser = domLs.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
		 
		 lsIn.setEncoding("UTF-8");

		 FileInputStream fileInputStream;
		 try 
		 {
			fileInputStream = new FileInputStream(communityXmlFile);
		 } catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		 }
		 InputStreamReader inputStreamReader;
		 try 
		 {
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
		 } catch (UnsupportedEncodingException e) 
		 {
			throw new IllegalStateException(e);
		 }
		 lsIn.setCharacterStream(inputStreamReader);
		 
		 Document doc = parser.parse(lsIn);
		 Element root = doc.getDocumentElement();
		 
		 NodeList nodeList = root.getChildNodes();
		 
		 log.debug("node list length = " + nodeList.getLength());
		 for( int index = 0; index < nodeList.getLength(); index++)
		 {
	         Node child = nodeList.item(index);
	         importCollection(child, repo,  zip);
	     }
	}
	
	/**
	 * Create a collection from the xml data.
	 * 
	 * @param node - root collection node
	 * @return
	 * @throws DuplicateNameException 
	 */
	private void importCollection(Node node, Repository repository, ZipFile zip) throws DuplicateNameException
	{
		if( log.isDebugEnabled() )
		{
		   log.debug("import collections");
		}
		String name = null;
		String id = null;
		String description = null;
		String copyright = null;
		Node links = null;
		Node collectionChildren = null;
		Node pictures = null;
		String primaryPicture = null;
		
        NodeList children = node.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
            if( child.getNodeName().equals("name"))
            {
            	name = child.getTextContent();
            }
            else if( child.getNodeName().equals("id"))
            {
            	id = child.getTextContent();
            }
            else if( child.getNodeName().equals("description"))
            {
            	description = child.getTextContent();
            }
            else if( child.getNodeName().equals("copyright"))
            {
            	copyright = child.getTextContent();
            }
            else if( child.getNodeName().equals("links"))
            {
            	links = child;
            }
            else if( child.getNodeName().equals("children"))
            {
            	collectionChildren = child;
            }
            else if( child.getNodeName().equals("pictures"))
            {
            	pictures = child;
            }
            else if( child.getNodeName().equals("primary_picture"))
            {
            	primaryPicture = child.getTextContent();
            }
        }
        
        if( name != null && !name.trim().equals(""))
        {
        	log.debug("creating collection with name " + name + " to repo " + repository);
        	InstitutionalCollection c = new InstitutionalCollection(repository, name);
        	institutionalCollectionService.saveCollection(c);
        	c.setDescription(description);
            c.setCopyright(copyright);
            repositoryService.saveRepository(repository);
            
            if(primaryPicture != null )
            {
            	this.addPrimaryPicture(primaryPicture, c, repository, zip);
	        }
            if( links != null )
            {
            	this.addLinks(links, c);
            }
            if( pictures != null )
            {
            	addPictures(pictures, c, repository, zip);
            }
            if( collectionChildren != null )
            {
            	this.addChildren(collectionChildren, c, repository, zip);
            }
            if( id != null )
            {
            	// procsss id
            }
            institutionalCollectionService.saveCollection(c);
        }
        
	}
	
	/**
	 * Add the primary picture to the collection.
	 * 
	 * @param primaryPicture
	 * @param c
	 * @param repository
	 * @param zip
	 */
	private void addPrimaryPicture(String primaryPicture, InstitutionalCollection c, Repository repository, ZipFile zip)
	{
		File f = new File(primaryPicture);
    	ZipArchiveEntry entry = zip.getEntry(primaryPicture);
    	zipHelper.getZipEntry(f, entry, zip);
        IrFile picture;
		try {
			picture = repositoryService.createIrFile(repository, f, primaryPicture, "primary news picture for collection id = " 
					+ c.getId());
			c.setPrimaryPicture(picture);
		} catch (IllegalFileSystemNameException e) {
			log.error(e);
		}
    	
		if( !FileUtils.deleteQuietly(f) )
		{
			log.error("file " + f + " not deleted");
		}
	}
	
	/**
	 * Add the pictures to the collection.
	 * 
	 * @param picturesNode
	 * @param c
	 * @param repository
	 * @param zip
	 */
	private void addPictures(Node picturesNode, InstitutionalCollection c, Repository repository, ZipFile zip )
	{
		NodeList children = picturesNode.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node picture = children.item(index);
        	if( picture != null )
        	{
        	    addPicture(picture.getTextContent(), c, repository, zip);
        	}
        }
	}
	
	/**
	 * Add a single picture to the collection.
	 * 
	 * @param picture
	 * @param c
	 * @param repository
	 * @param zip
	 */
	private void addPicture(String picture, InstitutionalCollection c, Repository repository, ZipFile zip)
	{
		if( picture != null )
		{
		    File f = new File(picture);
    	    ZipArchiveEntry entry = zip.getEntry(picture);
     	    zipHelper.getZipEntry(f, entry, zip);
            IrFile thePicture;
		    try {
			    thePicture = repositoryService.createIrFile(repository, f, picture, "primary news picture for collection id = " 
					+ c.getId());
					c.addPicture(thePicture);
		    } catch (IllegalFileSystemNameException e) {
			    log.error(e);
		    }
    	
			if( !FileUtils.deleteQuietly(f) )
			{
				log.error("file " + f + " not deleted");
			}
		}
	}

	/**
	 * Get the links for a community.
	 * 
	 * @param c - community to add the link inforamtion to
	 * @param linksNode - xml link data
	 */
	private void addLinks(Node linksNode, InstitutionalCollection c)
	{
		NodeList children = linksNode.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node link = children.item(index);
        	addLink(link, c);
        }
	}
	
	/**
	 * Get a link from a individual link node
	 * @param linkNode - xml containing link information
	 * @return - created link
	 */
	private void addLink(Node linkNode, InstitutionalCollection c)
	{
		NodeList children = linkNode.getChildNodes();
		String name = null;
		String url = null;
		for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
        	if( child.getNodeName().equals("name"))
        	{
        		name = child.getTextContent();
        	}
        	else if(child.getNodeName().equals("url") )
        	{
        		url = child.getTextContent();
        	}
        }
		try {
			c.addLink(name, url);
		} catch (DuplicateNameException e) {
			log.error(e);
		}
	}
	
	/**
	 * Add the children to the collection.
	 * 
	 * @param childrenNode
	 * @param parent
	 * @throws DuplicateNameException 
	 */
	private void addChildren(Node childrenNode, 
			InstitutionalCollection parent, 
			Repository repository, 
			ZipFile zip) throws DuplicateNameException
	{
		NodeList children = childrenNode.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
        	addChild(child, parent, repository, zip);
        }
	}
	
	
	/**
	 * Add the child collection.
	 * 
	 * @param childCollectionNode
	 * @param parent
	 * @param repository
	 * @param zip
	 * @throws DuplicateNameException
	 */
	private void addChild(Node childCollectionNode, 
			InstitutionalCollection parent, 
			Repository repository, 
			ZipFile zip) throws DuplicateNameException
	{
		if( log.isDebugEnabled() )
		{
		   log.debug("import child collection");
		}
		String name = null;
		String id = null;
		String description = null;
		String copyright = null;
		Node links = null;
		Node collectionChildren = null;
		Node pictures = null;
		String primaryPicture = null;
		
        NodeList children = childCollectionNode.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
            if( child.getNodeName().equals("name"))
            {
            	name = child.getTextContent();
            }
            else if( child.getNodeName().equals("id"))
            {
            	id = child.getTextContent();
            }
            else if( child.getNodeName().equals("description"))
            {
            	description = child.getTextContent();
            }
            else if( child.getNodeName().equals("copyright"))
            {
            	copyright = child.getTextContent();
            }
            else if( child.getNodeName().equals("links"))
            {
            	links = child;
            }
            else if( child.getNodeName().equals("children"))
            {
            	collectionChildren = child;
            }
            else if( child.getNodeName().equals("pictures"))
            {
            	pictures = child;
            }
            else if( child.getNodeName().equals("primary_picture"))
            {
            	primaryPicture = child.getTextContent();
            }
        }
        
        if( name != null && !name.trim().equals(""))
        {
        	if( log.isDebugEnabled())
        	{
        	   log.debug("creating collection with name " + name + " to parent collection " + parent);
        	}
        	InstitutionalCollection c = parent.createChild(name);
        	c.setDescription(description);
            c.setCopyright(copyright);
        	institutionalCollectionService.saveCollection(c);
            
            if(primaryPicture != null )
            {
            	this.addPrimaryPicture(primaryPicture, c, repository, zip);
	        }
            if( links != null )
            {
            	this.addLinks(links, c);
            }
            if( pictures != null )
            {
            	addPictures(pictures, c, repository, zip);
            }
            if( collectionChildren != null )
            {
            	this.addChildren(collectionChildren, c, repository, zip);
            }
            if( id != null )
            {
            	// procsss id
            }
            institutionalCollectionService.saveCollection(c);
        }
	}
	
	/**
	 * Set the zip helper.
	 * 
	 * @param zipHelper
	 */
	public void setZipHelper(ZipHelper zipHelper) {
		this.zipHelper = zipHelper;
	}
	
	/**
	 * Set the repository service.
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	/**
	 * Set the institutional collection service.
	 * 
	 * @param institutionalCollectionService
	 */
	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}


}
