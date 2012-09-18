package edu.ur.ir.web.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Lists the file types that should be shown in
 * the browser
 * 
 * @author Nathan Sarr
 *
 */
public class WebBrowserFileViewerHelper implements Serializable {

    /* eclipse generated id */
	private static final long serialVersionUID = 1181476904257554731L;

	/*  The extensions without the period */
    private List<String> browserFileTypes = new LinkedList<String>();
    
    /* maximum file size to show in the browser */
    private Long maximumFileSize;
    

	/**
     * Returns true if the file type can be shown in the browser.
     *  
     * @param extension - file extension
     * @return true if the file can be shown in the browser.
     */
    public boolean canShowFileTypeInBrowser(String extension)
    {
    	for(String value : browserFileTypes)
    	{
    		if( value.equalsIgnoreCase(extension))
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Get the list of file borwser types.
     * 
     * @return list of file types.
     */
    public List<String> getBrowserFileTypes() {
		return Collections.unmodifiableList(browserFileTypes);
	}

	/**
	 * Set the list of browser file types.
	 * 
	 * @param browserFileTypes
	 */
	public void setBrowserFileTypes(List<String> browserFileTypes) {
		this.browserFileTypes = browserFileTypes;
	}
	
	/**
	 * Get the maximum file size to allow to be shown in the browser.
	 * 
	 * @return
	 */
	public Long getMaximumFileSize() {
		return maximumFileSize;
	}

	/**
	 * Set the maximum file size to be shown in the browser.
	 * 
	 * @param maximumFileSize
	 */
	public void setMaximumFileSize(Long maximumFileSize) {
		this.maximumFileSize = maximumFileSize;
	}

	
}
