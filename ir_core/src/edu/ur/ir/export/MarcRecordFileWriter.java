package edu.ur.ir.export;

import java.io.File;
import java.io.FileNotFoundException;

import org.marc4j.marc.Record;

/**
 * @author Nathan Sarr
 *
 */
public interface MarcRecordFileWriter {


	/**
	 * Write the given record to the given file.
	 * 
	 * @param marcRecord - record to write
	 * @param f - file to write to.
	 * @throws FileNotFoundException 
	 */
	public void writeFile(Record marcRecord, File f) throws FileNotFoundException;
}
