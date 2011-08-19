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

package edu.ur.ir.ir_export.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TemporaryFileCreator;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionLink;
import edu.ur.ir.ir_export.CollectionExportService;
import edu.ur.ir.repository.Repository;

/**
 * Service to deal with exporting collection information.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultCollectionExportService implements CollectionExportService
{
	
	/** eclipse generated  */
	private static final long serialVersionUID = 8719108626468548078L;

	/** temporary file creator  */
	private TemporaryFileCreator temporaryFileCreator;
	
	/** collection file extension*/
	private String extension = "xml";
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultCollectionExportService.class);

	
	/**
	 * Export the specified institutional collection.
	 * 
	 * @param collection - collection to export
	 * @param includeChildren - if true children should be exported
	 * @param zipFileDestination - zip file destination to store the collection information
	 * @throws IOException 
	 */
	public void export(InstitutionalCollection collection, boolean includeChildren, File zipFileDestination) throws IOException
	{
		
		// create the path if it doesn't exist
		String path = FilenameUtils.getPath(zipFileDestination.getCanonicalPath());
		if( !path.equals(""))
		{
			File pathOnly = new File(FilenameUtils.getFullPath(zipFileDestination.getCanonicalPath()));
		    FileUtils.forceMkdir(pathOnly);
		}
		 
	    List<InstitutionalCollection> collections = new LinkedList<InstitutionalCollection>();
	    collections.add(collection);
	    File collectionXmlFile = temporaryFileCreator.createTemporaryFile(extension);
	    Set<FileInfo> allPictures = createXmlFile(collectionXmlFile, collections, includeChildren);
	    
	    FileOutputStream out = new FileOutputStream(zipFileDestination); 
	    ArchiveOutputStream os = null;
		try 
		{
			os = new ArchiveStreamFactory().createArchiveOutputStream("zip", out);
		    os.putArchiveEntry(new ZipArchiveEntry("collection.xml")); 
		    
		    FileInputStream fis = null;
		    try
		    {
		    	log.debug("adding xml file");
		        fis = new FileInputStream(collectionXmlFile);
		        IOUtils.copy(fis, os);
		    }
		    finally
		    {
		    	if( fis != null)
		    	{
		            fis.close();
		            fis = null;
		    	}
		    }
		    
		    log.debug("adding pictures size " + allPictures.size());
		    for(FileInfo fileInfo : allPictures)
		    {
		    	File f = new File(fileInfo.getFullPath());
		    	String name = FilenameUtils.getName(fileInfo.getFullPath());
		    	name = name + '.' + fileInfo.getExtension();
		    	log.debug(" adding name " + name);
		    	os.putArchiveEntry(new ZipArchiveEntry(name));
		    	try
		    	{
		    		log.debug("adding input stream");
		    		fis = new FileInputStream(f);
		    	    IOUtils.copy(fis, os); 
		    	}
		    	finally
		    	{
		    		if( fis != null )
		    		{
		    		    fis.close();
		    		    fis = null;
		    		}
		    	}
		    }
		    
		    os.closeArchiveEntry(); 
	        out.flush(); 
		} catch (ArchiveException e) {
			throw new IOException(e);
		} 
		finally
		{
			if( os != null )
			{
	            os.close();
	            os = null;
			}
		}
	    
	    FileUtils.deleteQuietly(collectionXmlFile);

	}
	
	/**
	 * Export all collections in the repository.
	 * 
	 * @param repository - repository to export
	 * @throws IOException 
	 */
	public void export(Repository repository, File zipFileDestination) throws IOException
	{
		// create the path if it doesn't exist
		String path = FilenameUtils.getPath(zipFileDestination.getCanonicalPath());
		if( !path.equals(""))
		{
			File pathOnly = new File(FilenameUtils.getFullPath(zipFileDestination.getCanonicalPath()));
		    FileUtils.forceMkdir(pathOnly);
		}
		 
	    File collectionXmlFile = temporaryFileCreator.createTemporaryFile(extension);
	    Set<FileInfo> allPictures = createXmlFile(collectionXmlFile, repository.getInstitutionalCollections(), true);
	    
	    FileOutputStream out = new FileOutputStream(zipFileDestination); 
	    ArchiveOutputStream os = null;
		try 
		{
			os = new ArchiveStreamFactory().createArchiveOutputStream("zip", out);
		    os.putArchiveEntry(new ZipArchiveEntry("collection.xml")); 
		    
		    FileInputStream fis = null;
		    try
		    {
		    	log.debug("adding xml file");
		        fis = new FileInputStream(collectionXmlFile);
		        IOUtils.copy(fis, os);
		    }
		    finally
		    {
		    	if( fis != null)
		    	{
		            fis.close();
		            fis = null;
		    	}
		    }
		    
		    log.debug("adding pictures size " + allPictures.size());
		    for(FileInfo fileInfo : allPictures)
		    {
		    	File f = new File(fileInfo.getFullPath());
		    	String name = FilenameUtils.getName(fileInfo.getFullPath());
		    	name = name + '.' + fileInfo.getExtension();
		    	log.debug(" adding name " + name);
		    	os.putArchiveEntry(new ZipArchiveEntry(name));
		    	try
		    	{
		    		log.debug("adding input stream");
		    		fis = new FileInputStream(f);
		    	    IOUtils.copy(fis, os); 
		    	}
		    	finally
		    	{
		    		if( fis != null )
		    		{
		    		    fis.close();
		    		    fis = null;
		    		}
		    	}
		    }
		    
		    os.closeArchiveEntry(); 
	        out.flush(); 
		} catch (ArchiveException e) {
			throw new IOException(e);
		} 
		finally
		{
			if( os != null )
			{
	            os.close();
	            os = null;
			}
		}
	    
	    FileUtils.deleteQuietly(collectionXmlFile);

	}
	
	/**
	 * Generate an xml file with the specified collections.
	 * 
	 * @see edu.ur.dspace.export.CollectionExporter#generateCollectionXMLFile(java.io.File, java.util.Collection)
	 */
	public Set<FileInfo> createXmlFile(File f,
			Collection<InstitutionalCollection> collections, boolean includeChildren) throws IOException {
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder;
		 
		 Set<FileInfo> allPictures = new HashSet<FileInfo>();		 
		 String path = FilenameUtils.getPath(f.getCanonicalPath());
		 if( !path.equals(""))
		 {
			File pathOnly = new File(FilenameUtils.getFullPath(f.getCanonicalPath()));
		    FileUtils.forceMkdir(pathOnly);
		 }
		 
		 if( !f.exists() )
		 {
			 if( !f.createNewFile() )
			 {
				 throw new IllegalStateException("could not create file");
			 }
		 }
		 
		 try {
			builder = factory.newDocumentBuilder();
		 } catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		 }
		 
		 DOMImplementation impl = builder.getDOMImplementation();
		 DOMImplementationLS domLs = (DOMImplementationLS)impl.getFeature("LS" , "3.0");
		 LSSerializer serializer = domLs.createLSSerializer();
		 LSOutput lsOut= domLs.createLSOutput();

		 Document doc = impl.createDocument(null, "institutionalCollections", null);
		 Element root = doc.getDocumentElement();
		 
		 FileOutputStream fos;
		 OutputStreamWriter outputStreamWriter;
		 BufferedWriter writer;
		 
		 try {
			fos = new FileOutputStream(f);
			
			try {
				outputStreamWriter = new OutputStreamWriter(fos, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new IllegalStateException(e);
			}
			writer = new BufferedWriter(outputStreamWriter);
			lsOut.setCharacterStream(writer);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
		 
		// create XML for the child collections
		 for(InstitutionalCollection c : collections)
		 {
			 Element collection = doc.createElement("collection");
			 
			 this.addIdElement(collection, c.getId().toString(), doc);
             this.addNameElement(collection, c.getName(), doc);
			 this.addDescription(collection, c.getDescription(), doc);
			 this.addCopyright(collection, c.getCopyright(), doc);
			 
			 if( c.getPrimaryPicture() != null)
			 {
				 this.addPrimaryImage(collection, c.getPrimaryPicture().getFileInfo().getNameWithExtension(), doc);
				 allPictures.add(c.getPrimaryPicture().getFileInfo());
			 }
			 Set<IrFile> pictures = c.getPictures();
			 
			 if( pictures.size() > 0 )
			 {
				 Element pics  = doc.createElement("pictures");
			     for(IrFile irFile : pictures)
			     {
			    	 Element picture  = doc.createElement("picture");
			    	 this.addImage(picture, irFile.getFileInfo().getNameWithExtension(), doc);
				     pics.appendChild(picture);
				     allPictures.add(irFile.getFileInfo());
			     }
			     collection.appendChild(pics);
			 }
			 
			 if( c.getLinks().size() > 0 )
			 {
				 Element links = doc.createElement("links");
				 for(InstitutionalCollectionLink l : c.getLinks())
				 {
					 this.addLink(links, l, doc);
				 }
				 collection.appendChild(links);
			 }
			 
			 if( includeChildren )
			 {
			     for(InstitutionalCollection child : c.getChildren())
			     { 
				     addChild(child, collection, doc, allPictures);
			     }
			 }
			 root.appendChild(collection);
		 }
		 serializer.write(root, lsOut);
		 
		 try
		 {
		      fos.close();
		      writer.close();
		      outputStreamWriter.close();
		 }
		 catch(Exception e)
		 {
			 throw new IllegalStateException(e);
		 }
		 return allPictures;
	}
	
	
	/**
	 * Add the children collection of the specified parent.
	 * 
	 * @param collection
	 */
	private void addChild(InstitutionalCollection child, Element parentElement, Document doc, Set<FileInfo> allPictures)
	{
		 Element children = doc.createElement("children");
		 parentElement.appendChild(children);
		 Element collection = doc.createElement("collection");
		 		 
		 this.addIdElement(collection, child.getId().toString(), doc);
		 this.addNameElement(collection, child.getName(), doc);
		 this.addDescription(collection, child.getDescription(), doc);
		 this.addCopyright(collection, child.getCopyright(), doc);
		 
		 
		 if( child.getPrimaryPicture() != null)
		 {
			 this.addPrimaryImage(collection, child.getPrimaryPicture().getFileInfo().getNameWithExtension(), doc);
			 allPictures.add(child.getPrimaryPicture().getFileInfo());
		 }
		 Set<IrFile> pictures = child.getPictures();
		 
		 if( pictures.size() > 0 )
		 {
			 Element pics  = doc.createElement("pictures");
		     for(IrFile irFile : pictures)
		     {
			     Element picture  = doc.createElement("picture");
			     this.addImage(picture, irFile.getFileInfo().getName(), doc);
			     allPictures.add(irFile.getFileInfo());
		     }
		     collection.appendChild(pics);
		 }
		 
		 if( child.getLinks().size() > 0 )
		 {
			 Element links = doc.createElement("links");
			 for(InstitutionalCollectionLink l : child.getLinks())
			 {
				 this.addLink(links, l, doc);
			 }
			 collection.appendChild(links);
		 }
		 
		 for(InstitutionalCollection achild : child.getChildren())
		 {
			 addChild(achild, collection, doc, allPictures);
		 }
		 children.appendChild(collection);
	}
	
	/**
	 * Add the name element.
	 * 
	 * @param parent - parent element to add the name to
	 * @param name - name value to add
	 * @param doc - document element
	 */
	private void addNameElement(Element parent, String name, Document doc)
	{
		 Element nameVal = doc.createElement("name");
		 Text data = doc.createTextNode(name);
		 nameVal.appendChild(data);
		 parent.appendChild(nameVal);
	}
	
	/**
	 * Add the id element.
	 * 
	 * @param parent - parent element
	 * @param id - id value
	 * @param doc - document element
	 */
	private void addIdElement(Element parent, String id, Document doc)
	{
		 Element idVal = doc.createElement("id");
		 Text data = doc.createTextNode(id);
		 idVal.appendChild(data);
		 parent.appendChild(idVal);
	}
	
	/**
	 * Add the description
	 * 
	 * @param parent
	 * @param introText
	 * @param doc
	 */
	private void addDescription(Element parent, String introText, Document doc)
	{
		 Element introductoryText = doc.createElement("description");
		 Text data = doc.createTextNode(introText);
		 introductoryText.appendChild(data);
		 parent.appendChild(introductoryText);
	}
	
	/**
	 * Add the copyright value.
	 * 
	 * @param parent
	 * @param copyrightVal
	 * @param doc
	 */
	private void addCopyright(Element parent, String copyrightVal, Document doc)
	{
		 Element copyright = doc.createElement("copyright");
		 Text data = doc.createTextNode(copyrightVal);
		 copyright.appendChild(data);
		 parent.appendChild(copyright);
	}
	
	/**
	 * Add the primary image element
	 * 
	 * @param parent
	 * @param imageName
	 * @param doc
	 */
	private void addPrimaryImage(Element parent, String imageName, Document doc)
	{
		 Element primaryPicture  = doc.createElement("primary_picture");
		 Text data = doc.createTextNode(imageName);
		 primaryPicture.appendChild(data);
		 parent.appendChild(primaryPicture);
	}
	
	/**
	 * Add the image.
	 * 
	 * @param parent
	 * @param imageName
	 * @param doc
	 */
	private void addImage(Element parent, String imageName, Document doc)
	{
		 Element picture  = doc.createElement("picture");
	     Text data = doc.createTextNode(imageName);
	     picture.appendChild(data);
	     parent.appendChild(picture);
	}
	
	/**
	 * Add the link 
	 * 
	 * @param parent - parent element
	 * @param l - link to add
	 * @param doc - document 
	 */
	private void addLink(Element parent, InstitutionalCollectionLink l, Document doc)
	{
		 Element link = doc.createElement("link");
		 
		 //name
		 Element linkName = doc.createElement("name");
	     Text data = doc.createTextNode(l.getName());
	     linkName.appendChild(data);
	     link.appendChild(linkName);
	     
	     // url
	     Element linkUrl = doc.createElement("url");
	     data = doc.createTextNode(l.getUrl());
	     linkUrl.appendChild(data);
	     link.appendChild(linkUrl);
	     
	     //desc
	     Element linkDescription = doc.createElement("description");
	     data = doc.createTextNode(l.getDescription());
	     linkDescription.appendChild(data);
	     link.appendChild(linkDescription);
	     
	     Element linkId = doc.createElement("id");
	     data = doc.createTextNode(l.getId().toString());
	     linkId.appendChild(data);
	     link.appendChild(linkId);
	     
	     parent.appendChild(link);
	}
	
	/**
	 * Set the temporary file creator.
	 * 
	 * @param temporaryFileCreator - temporary file creator
	 */
	public void setTemporaryFileCreator(TemporaryFileCreator temporaryFileCreator) {
		this.temporaryFileCreator = temporaryFileCreator;
	}

}
