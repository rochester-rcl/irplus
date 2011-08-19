package edu.ur.ir.ir_import;

import java.io.File;
import java.io.Serializable;

/**
 * Service to export contributor type information.
 * 
 * @author Nathan Sarr
 *
 */
public interface ContributorTypeImportService extends Serializable{
	
	/**
	 * Import the contributor type information.
	 * 
	 * @param xmlFile
	 */
	public void contributorTypeImport(File xmlFile);

}
