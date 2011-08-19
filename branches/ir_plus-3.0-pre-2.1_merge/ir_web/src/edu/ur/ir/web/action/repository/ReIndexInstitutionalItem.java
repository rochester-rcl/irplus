package edu.ur.ir.web.action.repository;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;

import org.apache.log4j.Logger;
/**
 * This will schedule the immediate action of re-indexing institutional items.
 * 
 * @author Nathan Sarr
 *
 */
public class ReIndexInstitutionalItem extends ActionSupport{
	

	/** eclipse generated id */
	private static final long serialVersionUID = 673513182573887635L;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(ReIndexInstitutionalItem.class);
	
	/** processing service for indexing institutional items */
	private InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService;
	
	/** Service for dealing with index processing types */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	public String execute()
	{
		log.debug("re index institutional items called");
		IndexProcessingType updateType = indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE);
		institutionalItemIndexProcessingRecordService.processItemsInRepository(updateType, true);
		return SUCCESS;
	}

	public InstitutionalItemIndexProcessingRecordService getInstitutionalItemIndexProcessingRecordService() {
		return institutionalItemIndexProcessingRecordService;
	}

	public void setInstitutionalItemIndexProcessingRecordService(
			InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService) {
		this.institutionalItemIndexProcessingRecordService = institutionalItemIndexProcessingRecordService;
	}

	public IndexProcessingTypeService getIndexProcessingTypeService() {
		return indexProcessingTypeService;
	}

	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

}
