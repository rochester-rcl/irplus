package edu.ur.ir.web.action.file.storage;


import java.util.Collection;

import edu.ur.file.db.FileInfoChecksumService;
import edu.ur.file.db.FileInfoChecksum;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * Allow administrators to view and deal with file checksums.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageFileInfoChecksums extends Pager {

	/* eclipse generated id  */
	private static final long serialVersionUID = -4604714764397456045L;
	
	/* Total number of user groups  */
	private int totalHits;
	
	/* Row End */
	private int rowEnd;
	
	/* Service to deal with file info checksums */
    private FileInfoChecksumService fileInfoChecksumService;
    
    /* only show the checksums that have failed */
    private boolean onlyFails = false;
    
    /* id of the checksum */
    private Long checksumId;
    
    /* file info for the checksum */
    private FileInfoChecksum fileInfoChecksum;




	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";


	/* list of checksums found */
	private Collection<FileInfoChecksum> checksums;
	
	/* Default constructor */
	public ManageFileInfoChecksums() 
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}
	
	public String viewChecksum()
	{
		if( checksumId != null )
		{
			fileInfoChecksum = fileInfoChecksumService.getById(checksumId, false);
		}
		return SUCCESS;
	}
	
	/**
	 * Get the content types table data.
	 * 
	 * @return
	 */
	public String viewChecksumInfos()
	{
	
		rowEnd = rowStart + numberOfResultsToShow;
	    
		checksums = fileInfoChecksumService.getChecksumInfosDateOrder(rowStart,
				numberOfResultsToShow, 
				onlyFails, 
				OrderType.getOrderType(sortType));
		
		if( !onlyFails )
		{
		    totalHits = fileInfoChecksumService.getCount().intValue();
		}
		else
		{
			totalHits = fileInfoChecksumService.getChecksumInfoFailsCount().intValue();
		}
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;

	}
	
	public int getTotalHits() {
		return totalHits;
	}
	
	public int getRowEnd() {
		return rowEnd;
	}

	/**
	 * Set the the file info checksum service.
	 * 
	 * @param fileInfoChecksumService
	 */
	public void setFileInfoChecksumService(
			FileInfoChecksumService fileInfoChecksumService) {
		this.fileInfoChecksumService = fileInfoChecksumService;
	}

	/**
	 * Get only the checksums that have failed.
	 * 
	 * @return
	 */
	public boolean getOnlyFails() {
		return onlyFails;
	}

	/**
	 * Set to true if only fails are to be viewed.
	 * 
	 * @param onlyFails
	 */
	public void setOnlyFails(boolean onlyFails) {
		this.onlyFails = onlyFails;
	}
	
	/**
	 * Get the sort type of the table.
	 * 
	 * @return
	 */
	public String getSortType() {
		return sortType;
	}

	/**
	 * Set the sort type of the table.
	 * 
	 * @param sortType
	 */
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	/**
	 * Get the list of checksums.
	 * 
	 * @return
	 */
	public Collection<FileInfoChecksum> getChecksums() {
		return checksums;
	}
	
	/**
	 * Get the checksum id of the file.
	 * 
	 * @return
	 */
	public Long getChecksumId() {
		return checksumId;
	}

	/**
	 * Set the checksum id of the file.
	 * 
	 * @param checksumId
	 */
	public void setChecksumId(Long checksumId) {
		this.checksumId = checksumId;
	}
	
	/**
	 * Get the file info checksum.
	 * 
	 * @return
	 */
	public FileInfoChecksum getFileInfoChecksum() {
		return fileInfoChecksum;
	}


}
