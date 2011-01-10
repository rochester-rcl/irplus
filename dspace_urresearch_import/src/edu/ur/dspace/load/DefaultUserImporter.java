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
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import edu.ur.dspace.model.DspaceUser;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.RoleService;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserIndexService;
import edu.ur.ir.user.UserService;

/**
 * Import users 
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultUserImporter implements UserImporter{

	/** Date format */
	private DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	
	/**  Service for dealing with users */
	private UserService userService;
	
	/** Service for accessing role information */
	private RoleService roleService;

	/** Repository service for placing information in the repository */
	private RepositoryService repositoryService;
	
	/** Service for indexing users */
	private UserIndexService userIndexService;
	
	/** template for accessing the database */
	private JdbcTemplate jdbcTemplate;

	/**  Logger  */
	private static final Logger log = Logger.getLogger(DefaultUserImporter.class);	
	
	/**
	 * Data source for accessing the database.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
	    this.jdbcTemplate = new JdbcTemplate(dataSource);
	    
	}
	
	public List<DspaceUser> getUsers(File xmlFile) throws DOMException, ParseException
	{
		LinkedList<DspaceUser> users = new LinkedList<DspaceUser>();
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

		 FileInputStream fileInputStream;
		 try {
			fileInputStream = new FileInputStream(xmlFile);
		 } catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		 }
		 InputStreamReader inputStreamReader;
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
	         users.add(getUser(child));
	     }
		 
		 return users;
	}
	
	/**
	 * Import users from the xml file
	 * @throws ParseException 
	 * @throws DOMException 
	 * 
	 * @see edu.ur.dspace.load.UserImporter#importUsers(java.io.File)
	 */
	public void importUsers(File xmlFile) throws DOMException, ParseException {
         List<DspaceUser> users = this.getUsers(xmlFile);
		 
         for(DspaceUser u : users)
         {
        	 System.out.println("Loading user " + u);
        	 
        	 if( !userEmailExists(u.email) && !userNameAlreadyExits(u.email) && !userNetIdExists(u.netId))
        	 {
        		 if( !userAlreadyImported(u.dspaceId))
        		 {	 
        		     log.debug("processing user " + u);
        	         IrUser user = createUser(u);
        	     
        	         jdbcTemplate.execute("insert into dspace_convert.ir_user(dspace_eperson_id, ur_research_user_id) values (" + u.dspaceId + "," + user.getId() + ")");
        		 }
        		 else
        		 {
        			 log.debug( "User " + u + " has already been imported");
        		 }
        	 }
        	 else
        	 {
        		 log.debug("user data " + u + " already exists" );
        	 }
         }		 
	}
	
	/**
	 * Create a new user in the UrResearch system.
	 * 
	 * @param u
	 * @return
	 */
	private IrUser createUser(DspaceUser u)
	{		
		UserEmail defaultEmail = new UserEmail(u.email); 
					
		defaultEmail.setVerified(true);
		IrUser irUser = userService.createUser(u.password, u.email, defaultEmail);

		irUser.setAccountExpired(false);
		irUser.setCredentialsExpired(false);
		irUser.setAccountLocked(!u.canLogIn);
		irUser.setPhoneNumber(u.phoneNumber);
		irUser.setFirstName(u.firstName);
		irUser.setLastName(u.lastName);
		
		// override the original password
		irUser.setPassword(u.password);
				    
		// Force the user to change password after login
		irUser.setPasswordChangeRequired(false);
				    
		userService.makeUserPersistent(irUser); 
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
							false);
		try {
			userIndexService.addToIndex(irUser, 
			    new File( repository.getUserIndexFolder()) );
		} catch (NoIndexFoundException e) {
			log.debug("index does not exist ", e);
		}
		IrRole role = roleService.getRole(IrRole.USER_ROLE);
		irUser.addRole(role);
		
		if(u.canSubmit || u.isAdmin)
		{
			 role = roleService.getRole(IrRole.AUTHOR_ROLE);
			 irUser.addRole(role);
		}
		
		if(u.isAdmin)
		{
			 role = roleService.getRole(IrRole.ADMIN_ROLE);
			 irUser.addRole(role);
		}
		
		userService.makeUserPersistent(irUser);
		return irUser;
	}
	
	
	private boolean userNameAlreadyExits(String username)
	{
		if(username == null || username.trim().equals(""))
		{
			return false;
		}
		return userService.getUser(username) != null;
	}
	
	private boolean userEmailExists(String email)
	{
		if(email == null || email.trim().equals(""))
		{
			return false;
		}
		return userService.getUserByEmail(email) != null;
	}
	
	private boolean userNetIdExists(String netId)
	{
		if( netId  == null || netId.trim().equals(""))
		{
			return false;
		}
		return false;
		//return userService.getUserByLdapUserName(netId) != null;
	}
	
	
	/**
	 * Create a user
	 * 
	 * @param node - root user node
	 * @return
	 * @throws ParseException 
	 * @throws DOMException 
	 */
	private DspaceUser getUser(Node node) throws DOMException, ParseException
	{
		DspaceUser u = new DspaceUser();
		
        NodeList children = node.getChildNodes();
        for( int index = 0; index < children.getLength(); index++)
        {
        	Node child = children.item(index);
            if( child.getNodeName().equals("id"))
            {
            	u.dspaceId = new Long(child.getTextContent());
            }
            else if( child.getNodeName().equals("first_name"))
            {
            	u.firstName = child.getTextContent();
            }
            else if( child.getNodeName().equals("last_name"))
            {
            	u.lastName = child.getTextContent();
            }
            else if( child.getNodeName().equals("email"))
            {
            	u.email = child.getTextContent();
            }
            else if( child.getNodeName().equals("netid"))
            {
            	if( child.getTextContent() == null || child.getTextContent().trim().equals(""))
            	{
            		u.netId = null;
            	}
            	else
            	{
            	    u.netId = child.getTextContent();
            	}
            }
            else if( child.getNodeName().equals("password"))
            {
            	u.password = child.getTextContent();
            }
            else if( child.getNodeName().equals("phone_number"))
            {
            	u.phoneNumber =  child.getTextContent();
            }
            else if( child.getNodeName().equals("registration_date"))
            {
            	u.registrationDate =  df.parse(child.getTextContent());
            }
            else if( child.getNodeName().equals("self_registered"))
            {
            	u.selfRegistered =  new Boolean(child.getTextContent()).booleanValue();
            }
            else if( child.getNodeName().equals("can_log_in"))
            {
            	u.canLogIn =  new Boolean(child.getTextContent()).booleanValue();
            }
            else if( child.getNodeName().equals("is_administrator"))
            {
            	u.isAdmin =  new Boolean(child.getTextContent()).booleanValue();
            }
            else if( child.getNodeName().equals("can_submit"))
            {
            	u.canSubmit =  new Boolean(child.getTextContent()).booleanValue();
            }
        }
		return u;
	}
	
	/**
	 * Determine if the user is already imported
	 * 
	 * @param epersonId
	 * @return true if the user is already imported.
	 */
	public boolean userAlreadyImported(Long epersonId)
	{
		int count = jdbcTemplate.queryForInt("select count(*) from dspace_convert.ir_user where dspace_eperson_id = " + epersonId);
	    if(count == 0)
	    {
	    	return false;
	    }
	    else
	    {
	    	return true;
	    }
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public UserIndexService getUserIndexService() {
		return userIndexService;
	}

	public void setUserIndexService(UserIndexService userIndexService) {
		this.userIndexService = userIndexService;
	}

}
