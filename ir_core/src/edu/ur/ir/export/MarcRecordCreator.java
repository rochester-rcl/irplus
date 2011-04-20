package edu.ur.ir.export;

import org.marc4j.marc.Record;

import edu.ur.ir.institution.InstitutionalItemVersion;



/**
 * Interface to create a marc record.
 * 
 * @author Nathan Sarr
 *
 */
public interface MarcRecordCreator 
{
	
	/**
	 * Public method to export marc record.
	 * 
	 * @param institutionalItemVersion
	 * @return
	 */
	public Record export(InstitutionalItemVersion version);

}
