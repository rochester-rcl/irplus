package edu.ur.ir.statistics.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import edu.ur.ir.statistics.IgnoreIpAddress;
import edu.ur.ir.statistics.IgnoreIpAddressService;

/**
 * Convert the old ip addresses to new ip addresses.
 * 
 * @author Nathan Sarr
 *
 */
public class OldIpIgnoreConverterService {
	
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
	
	private IgnoreIpAddressService ignoreIpAddressService;
	


	/**
	 * Map the data to a collection
	 * 
	 * @author Nathan Sarr
	 *
	 */
	@SuppressWarnings("unchecked")
	private static final class OldIpAddressMapper implements RowMapper {

	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	OldIpIgnoreAddress oldIgnoreAddress = new OldIpIgnoreAddress();
	    	oldIgnoreAddress.id = new Long(rs.getLong("old_ip_address_ignore_id"));
	    	oldIgnoreAddress.fromAddress1 = rs.getInt("from_ip_address_part1");
	    	oldIgnoreAddress.fromAddress2 = rs.getInt("from_ip_address_part2");
	    	oldIgnoreAddress.fromAddress3 = rs.getInt("from_ip_address_part3");
	    	oldIgnoreAddress.fromAddress4 = rs.getInt("from_ip_address_part4");
	    	oldIgnoreAddress.toAddress4 = rs.getInt("to_ip_address_part4");
	    	oldIgnoreAddress.storeCounts = rs.getBoolean("store_counts");
	    	oldIgnoreAddress.description = rs.getString("description");
	    	oldIgnoreAddress.name = rs.getString("name");
	        return oldIgnoreAddress;
	    }
	}
	
	/**
	 * Iterates through all of the ip addresses and converts them.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OldIpIgnoreAddress> convertToNew()
	{
		 List<OldIpIgnoreAddress> oldIps = jdbcTemplate.query( "select * from ir_statistics.old_ip_address_ignore order by old_ip_address_ignore_id", 
				 new  OldIpAddressMapper());
		 
		 
		 for( OldIpIgnoreAddress oldAddress : oldIps) {
			 for( int x = oldAddress.fromAddress4; x <= oldAddress.toAddress4; x++)
			 {
				 IgnoreIpAddress address = new IgnoreIpAddress(oldAddress.fromAddress1 + "." + 
						 oldAddress.fromAddress2 + "."  + 
						 oldAddress.fromAddress3 + "." +
						 x);
				 address.setDescription(oldAddress.description);
				 address.setStoreCounts(oldAddress.storeCounts);
				 address.setName(oldAddress.name);
				 
				 if( ignoreIpAddressService.getIgnoreIpAddress(address.getAddress()) == null ){
				   ignoreIpAddressService.saveIgnoreIpAddress(address);
				 }
			 }
		 }
		 return oldIps;

	}
	
	public void setIgnoreIpAddressService(
			IgnoreIpAddressService ignoreIpAddressService) {
		this.ignoreIpAddressService = ignoreIpAddressService;
	}

}
