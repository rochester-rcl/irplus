package edu.ur.ir.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.user.IrUser;

/**
 * This is the interface for importing marc file information.
 * 
 * @author Nathan Sarr
 *
 */
public interface MarcFileToVersionedItemImporter {
	
	
	/**
	 * Import the marc file into a list of versioned items
	 * 
	 * @param f - marc file to import
	 * @param owner - user who will own the versioned items
	 * @return list of versioned items
	 * @throws FileNotFoundException
	 * @throws NoIndexFoundException 
	 */
	public List<VersionedItem> importMarc(File f, IrUser owner) throws FileNotFoundException, NoIndexFoundException;

}
