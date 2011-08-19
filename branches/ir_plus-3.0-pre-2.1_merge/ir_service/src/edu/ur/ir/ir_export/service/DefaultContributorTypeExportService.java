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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import edu.ur.ir.ir_export.ContributorTypeExportService;
import edu.ur.ir.person.ContributorType;

/**
 * Export the list of contributor types.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultContributorTypeExportService implements ContributorTypeExportService{


	/** eclipse generated id */
	private static final long serialVersionUID = -1575404776871518177L;

	/** 
	 * Create the xml file for the set of collections.
	 * 
	 * @param xmlFile - file to write the xml to
	 * @param contributor types - set of contributor types to export
	 * 
	 * @throws IOException - if writing to the file fails.
	 */
     public void createXmlFile(File xmlFile,
			Collection<ContributorType> contributorTypes) throws IOException {
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder;
		 
		 String path = FilenameUtils.getPath(xmlFile.getCanonicalPath());
		 if( !path.equals(""))
		 {
			File pathOnly = new File(FilenameUtils.getFullPath(xmlFile.getCanonicalPath()));
		    FileUtils.forceMkdir(pathOnly);
		 }
		 
		 if( !xmlFile.exists() )
		 {
			 if( !xmlFile.createNewFile() )
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

		 Document doc = impl.createDocument(null, "contributor_types", null);
		 Element root = doc.getDocumentElement();
		 
		 FileOutputStream fos;
		 OutputStreamWriter outputStreamWriter;
		 BufferedWriter writer;
		 
		 try {
			fos = new FileOutputStream(xmlFile);
			
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
		 for(ContributorType ct : contributorTypes)
		 {
			 Element contributorType = doc.createElement("contributor_type");
			 
			 this.addIdElement(contributorType, ct.getId().toString(), doc);
             this.addNameElement(contributorType, ct.getName(), doc);
			 this.addDescription(contributorType, ct.getDescription(), doc);
			 this.addSystemCode(contributorType, ct.getUniqueSystemCode(), doc);
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
	 * Add the name element.
	 * 
	 * @param parent - parent element to add the name to
	 * @param name - name value to add
	 * @param doc - document element
	 */
	private void addSystemCode(Element parent, String systemCode, Document doc)
	{
		 Element systemCodeVal = doc.createElement("system_code");
		 Text data = doc.createTextNode(systemCode);
		 systemCodeVal.appendChild(data);
		 parent.appendChild(systemCodeVal);
	}

}
