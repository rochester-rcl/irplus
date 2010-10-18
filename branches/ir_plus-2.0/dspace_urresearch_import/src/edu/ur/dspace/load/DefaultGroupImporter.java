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
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

import edu.ur.dspace.model.DspaceGroup;

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupService;
import edu.ur.ir.user.UserService;

/**
 * Import groups from the dspace system.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultGroupImporter implements GroupImporter{
	
	
	/**  Service for dealing with institutional collections */
	private UserService userService;
	
	/** Service for dealing with groups */
	private UserGroupService userGroupService;
	
	/** template for accessing the database */
	private JdbcTemplate jdbcTemplate;

	/**  Logger  */
	private static final Logger log = Logger.getLogger(DefaultGroupImporter.class);	
	
	/**
	 * Data source for accessing the database.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
	    this.jdbcTemplate = new JdbcTemplate(dataSource);
	    
	}
	
	public List<DspaceGroup> getGroups(File xmlFile) throws DOMException, ParseException, IOException
	{
		 LinkedList<DspaceGroup> groups = new LinkedList<DspaceGroup>();
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder;
		 
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
			    fileInputStream = new FileInputStream(xmlFile);
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
	             groups.add(getGroup(child));
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
		 
		 return groups;
	}
	
	/**
	 * Import users from the xml file
	 * @throws ParseException 
	 * @throws DOMException 
	 * @throws IOException 
	 * 
	 * @see edu.ur.dspace.load.UserImporter#importUsers(java.io.File)
	 */
	public void importGroups(File xmlFile) throws DOMException, ParseException, IOException {
		List<DspaceGroup> groups = this.getGroups(xmlFile);

		for (DspaceGroup dspaceGroup : groups) {
			System.out
					.println("processing user group " + dspaceGroup.groupName);
			log.debug("number of people in group is "
					+ dspaceGroup.dspaceEpersonIds.size());
			IrUserGroup irUserGroup = new IrUserGroup(dspaceGroup.groupName);

			for (Long epersonId : dspaceGroup.dspaceEpersonIds) 
			{
				Long urresearchUserId = this.getUrResearchUser(epersonId);

				if (urresearchUserId != null) {
					IrUser user = userService.getUser(urresearchUserId, false);
					if (user != null) 
					{
						irUserGroup.addUser(user);
					} else 
					{
						log.debug("could not find user with urresearch id "
								+ urresearchUserId);
					}
				} 
				else 
				{
					log.debug("urresearch user id is null for eperson id "
							+ epersonId);
				}
				
			}
			userGroupService.save(irUserGroup);
			jdbcTemplate
					.execute("insert into dspace_convert.ir_group(dspace_group_id, ur_research_group_id) values ("
							+ dspaceGroup.groupId
							+ ","
							+ irUserGroup.getId()
							+ ")");
		}
	}
	
	
	
	/**
	 * Create a group from the node
	 * 
	 * @param node -
	 *            root groups node
	 * @return
	 * @throws ParseException
	 * @throws DOMException
	 */
	private DspaceGroup getGroup(Node node) throws DOMException, ParseException
	{
		DspaceGroup group = new DspaceGroup();
		
        NodeList children = node.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
            if( child.getNodeName().equals("id"))
            {
            	group.groupId = new Long(child.getTextContent());
            }
            else if( child.getNodeName().equals("name"))
            {
            	group.groupName = child.getTextContent();
            }
            else if( child.getNodeName().equals("user_ids"))
            {
            	group.dspaceEpersonIds = processEperonsIds(child);
            }
   
        }
		return group;
	}
	
	/**
	 * Get the eperson ids for the group.
	 * 
	 * @param node - the pearent user_ids node
	 * @return the list of eperson ids.
	 */
	private List<Long> processEperonsIds(Node node)
	{
		LinkedList<Long> epersonIds = new LinkedList<Long>();
		
        NodeList children = node.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
            if( child.getNodeName().equals("user_id"))
            {
            	Long epersonId = new Long(child.getTextContent());
            	epersonIds.add(epersonId);
            }
        }
		return epersonIds;
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

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserGroupService getUserGroupService() {
		return userGroupService;
	}

	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}
}
