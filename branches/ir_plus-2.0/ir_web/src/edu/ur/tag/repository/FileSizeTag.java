
/**  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  

package edu.ur.tag.repository;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * This tag takes a size in bytes an outputs the size
 * in bytes if it is less than 1KB, Size in KB if it
 * is less than 1MB and the size in MB if the size is
 * less than 1GB and in GB otherwise - this continues up
 * to exabytes
 * 
 * @author Nathan Sarr
 *
 */
public class FileSizeTag extends SimpleTagSupport{
	
	/** bytes in a kiloByte */
	public static final long bytesInKilobyte = 1024l;
	
	/** bytes in a megabyte */
	public static final long bytesInMegabyte = 1048576l;
	
	/** bytes in a gigabyte */
	public static final long bytesInGigabyte = 1073741824l;

	/** bytes in a terabyte */
	public static final long bytesInTerabyte = 1099511627776l;

	/** bytes in a terabyte */
	public static final long bytesInPedabyte = 1125899906842624l;

	/** bytes in a terabyte */
	public static final long bytesInExabyte = 1152921504606846976l;
	
	/** size in bytes  */
	private long sizeInBytes = 0l;
	
	/** Bytes symbol */
	private String bytes = "bytes";
	
	/** kilobytes symbol */
	private String kilobytes = "KB";
	
	/** megabytes symbol */
	private String megabytes = "MB";
	
	/** gigabytes symbol */
	private String gigabytes = "GB";

	/** terabytes symbol */
	private String terabytes = "TB";

	/** pedabytes symbol */
	private String pedabytes = "PB";

	/** exabytes symbol */
	private String exabytes = "EX";
	
	/** format to use when formatting the number */
	private String format = "0.00";
	
	public void doTag() throws JspException
	{
		DecimalFormat numberFormat = new DecimalFormat(format);
		JspWriter out = this.getJspContext().getOut();
		String output = "";
		
		if( sizeInBytes < bytesInKilobyte )
		{
			output = sizeInBytes + " " + bytes;
		}
		else if( sizeInBytes >= bytesInKilobyte && sizeInBytes < bytesInMegabyte)
		{
			output = "" + numberFormat.format(((double)sizeInBytes/(double)bytesInKilobyte));
			output = output + " " + kilobytes;
		}
		else if( sizeInBytes >= bytesInMegabyte && sizeInBytes < bytesInGigabyte )
		{
		    output = "" + 	 numberFormat.format(((double)sizeInBytes/(double)bytesInMegabyte));
		    output = output + " " + megabytes;
		}
		else if( sizeInBytes >= bytesInGigabyte && sizeInBytes < bytesInTerabyte )
		{
		    output = "" + 	 numberFormat.format(((double)sizeInBytes/(double)bytesInGigabyte));
		    output = output + " " + gigabytes;
		}
		else if( sizeInBytes >= bytesInTerabyte && sizeInBytes < bytesInPedabyte )
		{
		    output = "" + 	 numberFormat.format(((double)sizeInBytes/(double)bytesInTerabyte));
		    output = output + " " + terabytes;
		}
		else if( sizeInBytes >= bytesInPedabyte && sizeInBytes < bytesInExabyte )
		{
		    output = "" + 	 numberFormat.format(((double)sizeInBytes/(double)bytesInPedabyte));
		    output = output + " " + pedabytes;
		}
		else if( sizeInBytes >= bytesInExabyte)
		{
		    output = "" + 	 numberFormat.format(((double)sizeInBytes/(double)bytesInExabyte));
		    output = output + " " + exabytes;
		}
		
		try {
			out.write(output);
		} catch (IOException e) {
			throw new JspException(e);
		}


	}


	public long getSizeInBytes() {
		return sizeInBytes;
	}


	public void setSizeInBytes(long sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}


	public String getBytes() {
		return bytes;
	}


	public void setBytes(String bytes) {
		this.bytes = bytes;
	}


	public String getKilobytes() {
		return kilobytes;
	}


	public void setKilobytes(String kilobytes) {
		this.kilobytes = kilobytes;
	}


	public String getMegabytes() {
		return megabytes;
	}


	public void setMegabytes(String megabytes) {
		this.megabytes = megabytes;
	}


	public String getGigabytes() {
		return gigabytes;
	}


	public void setGigabytes(String gigabytes) {
		this.gigabytes = gigabytes;
	}


	public String getTerabytes() {
		return terabytes;
	}


	public void setTerabytes(String terabytes) {
		this.terabytes = terabytes;
	}


	public String getPedabytes() {
		return pedabytes;
	}


	public void setPedabytes(String pedabytes) {
		this.pedabytes = pedabytes;
	}


	public String getExabytes() {
		return exabytes;
	}


	public void setExabytes(String exabytes) {
		this.exabytes = exabytes;
	}

}
