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


package edu.ur.ir.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.news.NewsItem;
import edu.ur.ir.news.NewsService;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Write the news image out to a stream 
 * 
 * @author Nathan Sarr
 *
 */
public class NewsImageDownloader extends ActionSupport 
implements ServletResponseAware, ServletRequestAware{
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = 5077640228407715934L;

	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger( NewsImageDownloader.class);
	
	/**  News service */
	private NewsService newsService;
	
	/**  Id for the news item */
	private Long newsItemId;
	
	/**  Servlet response to write to */
	private HttpServletResponse response;
	
	/**  Servlet request made */
	private HttpServletRequest request;
	
	/** id of the ir file to download */
	private Long irFileId;
	
	private boolean getPrimaryImage = false;
	
	/** Utility for streaming files */
	private WebIoUtils webIoUtils;
	

	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	
    	if( log.isDebugEnabled())
    	{
	        log.debug("Trying to download news picture");
    	}
	    
	    // make sure this is a picture in the repository - otherwise anyone could get
	    // to the files.
		NewsItem newsItem = newsService.getNewsItem(newsItemId, false);
		if( newsItem != null )
		{
			FileInfo fileInfo = null;
			if(getPrimaryImage)
			{
				if( newsItem.getPrimaryPicture() != null )
				{
				    fileInfo = newsItem.getPrimaryPicture().getFileInfo();
				}
			}
			else
			{
				if( newsItem.getPicture(irFileId) != null )
				{	
			        // causes a load form the database;
		            fileInfo = newsItem.getPicture(irFileId).getFileInfo();
				}
			}
        
            
            if( fileInfo != null )
            {
                 webIoUtils.StreamFileInfo(fileInfo.getName(), fileInfo, response, request, (1024*4), true, false);
            }
		}
        return SUCCESS;
    }
	

	/* (non-Javadoc)
	 * @see com.opensymphony.webwork.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	public Long getIrFileId() {
		return irFileId;
	}


	public void setIrFileId(Long irFileId) {
		this.irFileId = irFileId;
	}


	public NewsService getNewsService() {
		return newsService;
	}


	public void setNewsService(NewsService newsService) {
		this.newsService = newsService;
	}


	public Long getNewsItemId() {
		return newsItemId;
	}


	public void setNewsItemId(Long newsItemId) {
		this.newsItemId = newsItemId;
	}


	public boolean isGetPrimaryImage() {
		return getPrimaryImage;
	}

	public void setGetPrimaryImage(boolean getPrimaryImage) {
		this.getPrimaryImage = getPrimaryImage;
	}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}


	public WebIoUtils getWebIoUtils() {
		return webIoUtils;
	}


	public void setWebIoUtils(WebIoUtils webIoUtils) {
		this.webIoUtils = webIoUtils;
	}


}
