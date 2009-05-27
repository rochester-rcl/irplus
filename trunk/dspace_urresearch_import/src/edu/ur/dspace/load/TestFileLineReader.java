package edu.ur.dspace.load;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class TestFileLineReader {
	
	public static void main(String[] args) throws IOException
	{
		File f = new File("C:\\license.txt");
		
		if( !f.canRead())
		{
			throw new IllegalStateException("cannot read file " + f.getAbsolutePath());
		}
		
		FileReader reader = new FileReader(f);
		LineNumberReader lineNumberReader = new LineNumberReader(reader);
		StringBuffer sb = new StringBuffer();
	    String lineText = lineNumberReader.readLine();
		while(lineText != null)
		{
			if( lineNumberReader.getLineNumber() != 1 )
			{
			    sb.append(lineText + "\n");
			   
			}
			lineText = lineNumberReader.readLine();
		}
		lineNumberReader.close();
		reader.close();
		
		f = new File("C:\\license2.txt");
		if( !f.canRead())
		{
			throw new IllegalStateException("cannot read file " + f.getAbsolutePath());
		}
		
		reader = new FileReader(f);
		lineNumberReader = new LineNumberReader(reader);
		StringBuffer sb2 = new StringBuffer();
	    lineText = lineNumberReader.readLine();
		while(lineText != null)
		{
			if( lineNumberReader.getLineNumber() != 1 )
			{
			    sb2.append(lineText + "\n");
			   
			}
			lineText = lineNumberReader.readLine();
		}
		lineNumberReader.close();
		
		System.out.println("sb equals sb2 " + sb.toString().equals(sb2.toString()));
		
	}

}
