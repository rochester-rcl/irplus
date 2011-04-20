package edu.ur.ir.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;
import org.marc4j.marc.Record;

/**
 * Creates an mrc file
 * 
 * @author Nathan Sarr
 *
 */
public class MrcMarcFileWriter implements MarcRecordFileWriter {

	
	public void writeFile(Record marcRecord, File f) throws FileNotFoundException {
		
		FileOutputStream outputStream = null;
		MarcWriter writer = null;
		try {
			outputStream = new FileOutputStream(f);
			writer = new MarcStreamWriter(outputStream);
			writer.write(marcRecord);
		}
		finally{
			try {
				if( outputStream != null )
				{
				    outputStream.close();
				}
			} catch (IOException e) {
				outputStream = null;
				
			}
			writer.close(); 
			writer = null;
		}

		
	}



}
