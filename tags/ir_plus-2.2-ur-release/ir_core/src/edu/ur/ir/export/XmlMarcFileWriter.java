package edu.ur.ir.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.Record;

public class XmlMarcFileWriter implements MarcRecordFileWriter {

	
	public void writeFile(Record marcRecord, File f) throws FileNotFoundException {
		
		FileOutputStream outputStream = null;
		MarcWriter writer = null;
		try {
			outputStream = new FileOutputStream(f);
			writer = new  MarcXmlWriter(outputStream);
			writer.write(marcRecord);
			writer.close();
		}
		finally{
			try{
				if( writer != null )
				{
				    writer.close();
				}
			}
			catch(Exception e)
			{
				writer = null;
			}
			try {
				if( outputStream != null )
				{
					outputStream.flush();
				    outputStream.close();
				}
			} catch (IOException e) {
				outputStream = null;
			}
			
		}

		
	}


}
