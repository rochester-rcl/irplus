package edu.ur.ir.web.action.researcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.researcher.ResearcherPublication;
import edu.ur.ir.statistics.DownloadStatisticsService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.action.item.GenericItemFileDownload;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Allows a user to download a researcher file. NOT - this checks
 * to see if the RESEARCHER PAGE is public - this ignores the
 * current private settings on the generic item files.
 * 
 * @author Nathan Sarr
 *
 */
public class DownloadResearcherPublicationFile extends ActionSupport implements ServletResponseAware, ServletRequestAware, UserIdAware
{

	/** Eclipse generated id. */
	private static final long serialVersionUID = 5430030320610916010L;

	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(GenericItemFileDownload.class);
	
	/**  File to download */
	private Long itemFileId;

	/** researcher publication id. */
	private Long publicationId;
    
	/** Service for dealing with researcher file system information */
	private ResearcherFileSystemService researcherFileSystemService;
	
	/**  Servlet response to write to */
	private HttpServletResponse response;
	
	/**  Servlet request made */
	private HttpServletRequest request;
	
	/** Utility for streaming file */
	private WebIoUtils webIoUtils;

	/** Download statistics service */
	private DownloadStatisticsService downloadStatisticsService;
	
	/** service for dealing with sending out errors */
	private ErrorEmailService errorEmailService;
	
	/** Id of user logged in */
	private Long userId;
	
	
	
    /**
     * Checks for user permission and then downloads the file 
     * 
     * @return
     * @throws Exception
     */
    public String fileDownloadWithPermissionCheck() throws Exception {

	    log.debug("Trying to download the file to user");
	    
        if (itemFileId == null) {
        	log.debug("file id is null");
            return INPUT;
        }
        
        if( publicationId == null)
        {
        	log.debug("item id is null");
        	return INPUT;
        }
        
        ResearcherPublication researcherPublication = researcherFileSystemService.getResearcherPublication(publicationId, false);
        
        if( researcherPublication == null )
        {
        	log.debug("resercher publication is null");
        	return INPUT;
        }
        
        Researcher researcher = researcherPublication.getResearcher();
        
        if( researcher.isPublic()  || researcher.getUser().getId().equals(userId))
        {
        	 ItemFile itemFile  = researcherPublication.getPublication().getItemFile(itemFileId);
        	  if( itemFile == null)
              {
              	log.debug("Item file is null");
              	return INPUT;
              }
        	  // only count downloads if we don't know owner
        	  if(  !researcher.getUser().getId().equals(userId))
        	  {
        		  // statistics should not cause a download failure
        		  try
        		  {
        	        downloadStatisticsService.processFileDownload(request.getRemoteAddr(),
        	        		itemFile.getIrFile());
        		  }
        		  catch(Exception e)
        		  {
        			  log.error(e);
        			  errorEmailService.sendError(e);
        		  }
        	  }
        	  downloadFile(itemFile);
        }
        else
        {
        	log.debug("user does not have access");
        	return "accessDenied";
        }
    
        return SUCCESS;

    }

    /*
     * Downloads the file
     */
    private void downloadFile(ItemFile itemFile) throws Exception {
    	
    	
        
        String fileName = itemFile.getIrFile().getName();
        FileInfo fileInfo =  itemFile.getIrFile().getFileInfo();
        webIoUtils.StreamFileInfo(fileName, fileInfo, response, request, (1024*4), false, true);
        
    }
    
	/* (non-Javadoc)
	 * @see com.opensymphony.webwork.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
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


	public Long getItemFileId() {
		return itemFileId;
	}


	public void setItemFileId(Long itemFileId) {
		this.itemFileId = itemFileId;
	}




	public void setDownloadStatisticsService(
			DownloadStatisticsService downloadStatisticsService) {
		this.downloadStatisticsService = downloadStatisticsService;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public ResearcherFileSystemService getResearcherFileSystemService() {
		return researcherFileSystemService;
	}

	public void setResearcherFileSystemService(
			ResearcherFileSystemService researcherFileSystemService) {
		this.researcherFileSystemService = researcherFileSystemService;
	}

	public Long getPublicationId() {
		return publicationId;
	}

	public void setPublicationId(Long publicationId) {
		this.publicationId = publicationId;
	}

	public void setErrorEmailService(ErrorEmailService errorEmailService) {
		this.errorEmailService = errorEmailService;
	}


}
