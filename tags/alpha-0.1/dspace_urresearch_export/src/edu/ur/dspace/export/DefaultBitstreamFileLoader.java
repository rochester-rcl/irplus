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

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.FilenameUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import edu.ur.dspace.model.BitstreamFileInfo;
import edu.ur.dspace.model.EpersonPermissionMapper;
import edu.ur.dspace.model.GroupPermissionMapper;


/**
 * Default implementation for extracting bitstream file information out of a dspace system.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultBitstreamFileLoader implements BitstreamFileLoader {
	
    /** taken from dspace 1.1 implementation*/
    private int digitsPerLevel = 2;   
    
    /** taken from dspace 1.1 implementation */
    private int directoryLevels = 3;
    
    /** list of asset stores  */
    private List<String> assetStores;
    
	/** jdbc template */
	private JdbcTemplate jdbcTemplate;
	
	/** file loader for bitstream */
	private BitstreamFileLoader bitstreamFileLoader;
	
	/**
	 * Datasource for accessing the database.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

    
	/**
	 * Get the bitstream
	 * 
	 * @see edu.ur.dspace.export.BitstreamFileLoader#getBitstreamFileInfo(long)
	 */
	@SuppressWarnings("unchecked")
	public BitstreamFileInfo getBitstreamFileInfo(long bitstreamId) throws IOException {
		
		String sql = "select * from bitstream where bitstream_id = " + bitstreamId ;
		
		BitstreamFileInfo info = (BitstreamFileInfo)jdbcTemplate.query(sql, (ResultSetExtractor)new BitstreamFileMapper());
		
		
		// get the extension - cannot use table because join on fileextension because it causes multiple results
		if( info.originalFileName != null)
		{
		    String extension = FilenameUtils.getExtension(info.originalFileName);
			if( extension != null && !extension.trim().equals(""))
			{
			    info.extension = extension;
			}
			     
		}
		
		int storeNumber = info.storeNumber;
		
   
        // Default to zero ('assetstore.dir') for backwards compatibility
        if (storeNumber == -1)
        {
            storeNumber = 0;
        }

        File store = new File(assetStores.get(storeNumber));

        // Turn the internal ID into a file path relative to the asset store
        // directory
		
		String  id = info.internal_id;
        
       
         StringBuffer result = new StringBuffer().append(store.getCanonicalPath());

        // Split the id into groups
        for (int i = 0; i < directoryLevels; i++)
        {
            int digits = i * digitsPerLevel;

            result.append(File.separator).append(id.substring(digits, digits + digitsPerLevel));
        }

        String theName = result.append(File.separator).append(id).toString();
        info.dspaceFileName = theName;
        
        String groupPermissionsSelect = "select * from resourcepolicy where resource_type_id = 0 and epersongroup_id is not null and resource_id = " + info.bitstreamId;
        String epersonPermissionsSelect = "select * from resourcepolicy where resource_type_id = 0 and eperson_id is not null and resource_id = " + info.bitstreamId;
	 
        info.groupPermissions = jdbcTemplate.query( groupPermissionsSelect, new GroupPermissionMapper());
        info.epersonPermissions = jdbcTemplate.query( epersonPermissionsSelect, new EpersonPermissionMapper());
       
        return info;

	}
	

	/**
	 * Mapper for bitstream information
	 *  
	 * @author Nathan Sarr 
	 * 	 
	 */
	private static final class BitstreamFileMapper implements RowMapper, ResultSetExtractor {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	return getInfo(rs);
	    }
	    
	    public Object extractData(ResultSet rs) throws SQLException,
		DataAccessException {
	       if( rs.next())
	       {
	           return getInfo(rs);
	       }
	       else
	       {
	    	   return null;
	       }
       }
	    
	   private Object getInfo(ResultSet rs)throws SQLException 
	   {
		   BitstreamFileInfo info = new BitstreamFileInfo();
		   info.bitstreamId = rs.getLong("bitstream_id");
	       info.storeNumber = rs.getInt("store_number");
	       info.internal_id = rs.getString("internal_id");
	       info.originalFileName = rs.getString("name");
           return info;
	   }
	}
	
	


	public List<String> getAssetStores() {
		return assetStores;
	}


	public void setAssetStores(List<String> assetStores) {
		this.assetStores = assetStores;
	}


	public int getDigitsPerLevel() {
		return digitsPerLevel;
	}


	public void setDigitsPerLevel(int digitsPerLevel) {
		this.digitsPerLevel = digitsPerLevel;
	}


	public int getDirectoryLevels() {
		return directoryLevels;
	}


	public void setDirectoryLevels(int directoryLevels) {
		this.directoryLevels = directoryLevels;
	}


	public BitstreamFileLoader getBitstreamFileLoader() {
		return bitstreamFileLoader;
	}


	public void setBitstreamFileLoader(BitstreamFileLoader bitstreamFileLoader) {
		this.bitstreamFileLoader = bitstreamFileLoader;
	}

}
