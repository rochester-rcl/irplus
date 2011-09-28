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

package edu.ur.dspace.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import edu.ur.dspace.model.DspaceUser;

/**
 * Class to export user data out of a dspace system.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultUserExporter implements UserExporter{
	
	/** template for accessing the database */
	private JdbcTemplate jdbcTemplate;
	
	/** determines if a user is a dspace administrator */
	private DspaceAdminDeterminer dspaceAdminDeterminer;
	
	/** determines if a user is a dspace administrator */
	private DspaceSubmitDeterminer dspaceSubmitDeterminer;
	
	/**
	 * Data source for accessing the database.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	/**
	 * Map the data to a user
	 * 
	 * @author Nathan Sarr
	 *
	 */
	private static final class UserMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	DspaceUser dspaceUser = new DspaceUser();
	    	dspaceUser.dspaceId = rs.getLong("eperson_id");
	    	dspaceUser.email = rs.getString("email");
	    	dspaceUser.firstName = rs.getString("firstname");
	    	dspaceUser.lastName = rs.getString("lastname");
	    	dspaceUser.netId = rs.getString("netid");
	    	dspaceUser.password = rs.getString("password");
	    	dspaceUser.phoneNumber = rs.getString("phone");
	    	dspaceUser.registrationDate = rs.getDate("register_date");
	    	dspaceUser.selfRegistered = rs.getBoolean("self_registered");
	    	dspaceUser.canLogIn = rs.getBoolean("can_log_in");
	        return dspaceUser;
	    }
	}

	
	public void generateUserXMLFile(File f, Collection<DspaceUser> users)
			throws IOException {
		
		 DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder;
		 
		 try {
			builder = factory.newDocumentBuilder();
		 } catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		 }
		 
		 DOMImplementation impl = builder.getDOMImplementation();
		 DOMImplementationLS domLs = (DOMImplementationLS)impl.getFeature("LS" , "3.0");
		 LSSerializer serializer = domLs.createLSSerializer();
		 LSOutput lsOut= domLs.createLSOutput();

		 Document doc = impl.createDocument(null, "users", null);
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
		 
		// create XML for the communities
		 for(DspaceUser u : users)
		 {
			 Element user = doc.createElement("user");
			 Element id = doc.createElement("id");
			 Text data = doc.createTextNode(u.dspaceId.toString());
			 id.appendChild(data);
			 			 
			 Element firstName = doc.createElement("first_name");
			 data = doc.createTextNode(u.firstName);
			 firstName.appendChild(data);
			 
			 Element lastName = doc.createElement("last_name");
			 data = doc.createTextNode(u.lastName);
			 lastName.appendChild(data);
			 			 
			 Element email = doc.createElement("email");
			 data = doc.createTextNode(u.email);
			 email.appendChild(data);
			 
			 Element netid = doc.createElement("netid");
			 data = doc.createTextNode(u.netId);
			 netid.appendChild(data);
			 
			 Element password = doc.createElement("password");
			 data = doc.createTextNode(u.password);
			 password.appendChild(data);
			 
			 Element phoneNumber = doc.createElement("phone_number");
			 data = doc.createTextNode(u.phoneNumber);
			 phoneNumber.appendChild(data);
			 
			 Element registrationDate = null;
			 if( u.registrationDate != null )
			 {
				 registrationDate = doc.createElement("registration_date");
			     data = doc.createTextNode(df.format(u.registrationDate));
			     registrationDate.appendChild(data);
			 }
			
			 Element selfRegistered = doc.createElement("self_registered");
			 data = doc.createTextNode(u.selfRegistered + "");
			 selfRegistered.appendChild(data);
			 
			 Element canLogIn = doc.createElement("can_log_in");
			 data = doc.createTextNode(u.canLogIn + "");
			 canLogIn.appendChild(data); 
			 
			 Element isAdministrator = doc.createElement("is_administrator");
			 data = doc.createTextNode(dspaceAdminDeterminer.isAdmin(u) + ""); 
			 isAdministrator.appendChild(data);
			 
			 Element canSubmit = doc.createElement("can_submit");
			 data = doc.createTextNode(dspaceSubmitDeterminer.canSubmit(u) + ""); 
			 canSubmit.appendChild(data);
			
			 root.appendChild(user);
			 user.appendChild(isAdministrator);
			 user.appendChild(canSubmit);
			 user.appendChild(id);
			 user.appendChild(firstName);
			 user.appendChild(lastName);
			 user.appendChild(email);
			 user.appendChild(netid);
			 user.appendChild(password);
			 user.appendChild(phoneNumber);
			 user.appendChild(canLogIn);
			 if( registrationDate != null)
			 {
			     user.appendChild(registrationDate);
			 }
			 user.appendChild(selfRegistered);
		 }
		 serializer.write(root, lsOut);
		 
		 try
		 {
			  fos.flush();
			  writer.flush();
			  outputStreamWriter.flush();
			  
		      fos.close();
		      writer.close();
		      outputStreamWriter.close();
		 }
		 catch(Exception e)
		 {
			 throw new IllegalStateException(e);
		 }
		
	}

	public void generateUserXMLFile(File f, Collection<DspaceUser> users,
			String testDataPassword) throws IOException {
		// create XML for the communities
		 for(DspaceUser u : users)
		 {
			 u.password = testDataPassword;
		 }
		 for(DspaceUser u : users)
		 {
			 u.netId = "";
		 }
		 this.generateUserXMLFile(f, users);
	}

	@SuppressWarnings("unchecked")
	public List<DspaceUser> getUsers() {
		return jdbcTemplate.query( "select * from eperson", new UserMapper());
	}

	public DspaceAdminDeterminer getDspaceAdminDeterminer() {
		return dspaceAdminDeterminer;
	}

	public void setDspaceAdminDeterminer(DspaceAdminDeterminer dspaceAdminDeterminer) {
		this.dspaceAdminDeterminer = dspaceAdminDeterminer;
	}

	public DspaceSubmitDeterminer getDspaceSubmitDeterminer() {
		return dspaceSubmitDeterminer;
	}

	public void setDspaceSubmitDeterminer(
			DspaceSubmitDeterminer dspaceSubmitDeterminer) {
		this.dspaceSubmitDeterminer = dspaceSubmitDeterminer;
	}

}
