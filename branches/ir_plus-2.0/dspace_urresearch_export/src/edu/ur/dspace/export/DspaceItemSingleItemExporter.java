package edu.ur.dspace.export;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Exports a single item from dspace
 * 
 * @author Nathan Sarr
 *
 */
public class DspaceItemSingleItemExporter {

	public static void main(String[] args) throws IOException
	{
		// name of the zip file to create for example "items" this will then
		// use this to crate files like 
		String zipFileName = args[0];
	     
		// path where the xml file will be created
		String filePath = args[1];
		
		// batch size
		int dspaceItemId = new Integer(args[2]).intValue();
		
		System.out.println("zip file name = " + zipFileName + " xmlFilePath = " + filePath + " dspace item id = " + dspaceItemId);
		
		/** get the application context */
		ApplicationContext ctx  = new ClassPathXmlApplicationContext(
		"applicationContext.xml");
		
		ItemExporter itemExporter = (ItemExporter)ctx.getBean("itemExporter");
		String newZipFileName = filePath + zipFileName +"_" + dspaceItemId + ".zip";
		System.out.println( " Writing file to " + newZipFileName);
		itemExporter.exportItems( newZipFileName, filePath, itemExporter.getItems(dspaceItemId, dspaceItemId));
	}
}
