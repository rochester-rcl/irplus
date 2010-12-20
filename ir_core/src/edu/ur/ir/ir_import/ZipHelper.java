package edu.ur.ir.ir_import;

import java.io.File;
import java.io.Serializable;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

/**
 * Helper class to help with basic needs.
 * 
 * @author Nathan Sarr
 *
 */
public interface ZipHelper extends Serializable
{
	/**
	 * Extracts the entry from the specified zip file.
	 * 
	 * @param File f - file to write the data to
	 * @param entry - entry to extract from the zip file
	 * @param zip - zip file 
	 * 
	 */
	public void  getZipEntry(File f, ZipArchiveEntry entry, ZipFile zip);

}
