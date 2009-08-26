package edu.ur.ir.web.action.repository;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.statistics.DownloadStatisticsService;

/**
 * Set all roll up counts to be updated.
 * 
 * @author Nathan Sarr
 *
 */
public class UpdateAllIrFileRollUpCounts extends ActionSupport{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -106512169864773214L;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(UpdateAllIrFileRollUpCounts.class);
	
	/** service for dealing with statistics information */
	private DownloadStatisticsService downloadStatisticsService;
	
	
	public String execute()
	{
		log.debug("re index institutional items called");
		downloadStatisticsService.updateAllRepositoryFileRollUpCounts();
		return SUCCESS;
	}


	public DownloadStatisticsService getDownloadStatisticsService() {
		return downloadStatisticsService;
	}

	public void setDownloadStatisticsService(
			DownloadStatisticsService downloadStatisticsService) {
		this.downloadStatisticsService = downloadStatisticsService;
	}

}