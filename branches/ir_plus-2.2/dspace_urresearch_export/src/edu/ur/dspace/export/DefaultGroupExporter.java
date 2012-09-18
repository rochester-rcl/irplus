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

import edu.ur.dspace.model.DspaceGroup;

/**
 * Implementation of group exporter.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultGroupExporter implements GroupExporter{
	
	/** template for accessing the database */
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Data source for accessing the database.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
	    this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/**
	 * Map the data to a group
	 * 
	 * @author Nathan Sarr
	 */
	private static final class GroupMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	DspaceGroup dspaceGroup = new DspaceGroup();
	    	dspaceGroup.groupId = rs.getLong("eperson_group_id");
	    	dspaceGroup.groupName = rs.getString("name");
	        return dspaceGroup;
	    }
	}
	
	/**
	 * Get the groups from the database.
	 * 
	 * @see edu.ur.dspace.export.GroupExporter#getGroups()
	 */
	@SuppressWarnings("unchecked")
	public List<DspaceGroup> getGroups() {
		List<DspaceGroup> groups = jdbcTemplate.query( "select * from epersongroup", new GroupMapper());
		for(DspaceGroup g : groups)
		{
			g.dspaceEpersonIds = jdbcTemplate.query( "select * from epersongroup2eperson where eperson_group_id = " + g.groupId, new GroupUserIdMapper());
		}
		
		
		return groups;
		
	}
	
	/**
	 * Map the user ids to a group
	 * 
	 * @author Nathan Sarr
	 */
	private static final class GroupUserIdMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	Long userGroupId = rs.getLong("eperson_id");
	        return userGroupId;
	    }
	}

	
	/**
	 * Generate the group xml file.
	 * 
	 * @see edu.ur.dspace.export.GroupExporter#generateGroupXMLFile(java.io.File, java.util.Collection)
	 */
	public void generateGroupXMLFile(File f, Collection<DspaceGroup> groups)
			throws IOException {
			
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

		 Document doc = impl.createDocument(null, "groups", null);
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
		 for(DspaceGroup g : groups)
		 {
			 Element group = doc.createElement("group");
			 
			 Element id = doc.createElement("id");
			 Text data = doc.createTextNode(g.groupId.toString());
			 id.appendChild(data);
			 group.appendChild(id);
			 			 
			 Element name = doc.createElement("name");
			 data = doc.createTextNode(g.groupName);
			 name.appendChild(data);
			 group.appendChild(name);
			 
			 if( g.dspaceEpersonIds.size() > 0 )
			 {
			     Element users = doc.createElement("user_ids");
			     group.appendChild(users);
			     
			     for(Long userId : g.dspaceEpersonIds)
			     {
			    	 Element user = doc.createElement("user_id");
			    	 data = doc.createTextNode(userId.toString());
					 user.appendChild(data);
					 users.appendChild(user);
			    	 
			     }
			 }
			 root.appendChild(group);
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


}
