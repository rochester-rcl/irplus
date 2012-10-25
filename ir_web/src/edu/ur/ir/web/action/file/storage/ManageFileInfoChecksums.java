package edu.ur.ir.web.action.file.storage;


import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import edu.ur.file.checksum.ChecksumCalculator;
import edu.ur.file.checksum.ChecksumService;
import edu.ur.file.db.ChecksumCheckerService;
import edu.ur.file.db.FileInfoChecksumService;
import edu.ur.file.db.FileInfoChecksum;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserPublishingFileSystemService;
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
    
    /* Checksum service to check checksums */
    private ChecksumService checksumService;
    
    private ChecksumCheckerService checksumCheckerService;

    private ResearcherFileSystemService researcherFileSystemService;

	/** Service for dealing with items. */
	private ItemService itemService;
	
	/** service for dealing with user file system */
	private UserFileSystemService userFileSystemService;
	
	/** set the repository service */
	private RepositoryService repositoryService;
    
    private InstitutionalItemService institutionalItemService;

    private List<InstitutionalItem> institutionalItems = new LinkedList<InstitutionalItem>();
    
    private UserPublishingFileSystemService userPublishingFileSystemService;
    


	// actually only one but using list for display in table
    private List<IrFile> irFiles = new LinkedList<IrFile>();

    // list of personal files who use the IrFile
    private List<PersonalFile> personalFiles = new LinkedList<PersonalFile>();
    
    // list of researcher files who use the IrFile
    private List<ResearcherFile> researcherFiles = new LinkedList<ResearcherFile>();

    private List<PersonalItem> personalItems = new LinkedList<PersonalItem>();
    


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
	
	public String viewFileInfoChecksum()
	{
		if( checksumId != null )
		{
			fileInfoChecksum = fileInfoChecksumService.getById(checksumId, false);
			getIrFileInfo();
			
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
	
	public String resetFileInfoChecksum()
	{
		if( checksumId != null )
		{
			fileInfoChecksum = fileInfoChecksumService.getById(checksumId, false);
			ChecksumCalculator calc = checksumService.getChecksumCalculator(fileInfoChecksum.getAlgorithmType());
			String checksum = calc.calculate(new File(fileInfoChecksum.getFileInfo().getFullPath()));
			fileInfoChecksum.setChecksum(checksum);
			fileInfoChecksum.setReCalculateChecksum(true);
			checksumCheckerService.checkChecksum(fileInfoChecksum);
			getIrFileInfo();
		}
		return SUCCESS;
	}
	
	
	public String checkFileInfoChecksum()
	{
		if( checksumId != null )
		{
			fileInfoChecksum = fileInfoChecksumService.getById(checksumId, false);
			checksumCheckerService.checkChecksum(fileInfoChecksum);
			getIrFileInfo();
		}
		return SUCCESS;
	}
	
	private void getIrFileInfo()
	{
		IrFile irFile = repositoryService.getIrFileByFileInfoId(fileInfoChecksum.getFileInfo().getId());
		if( irFile != null )
		{
		    irFiles.add(irFile);
		    // get the item ids to show
		    List<ItemFile> files = itemService.getItemFilesWithIrFile(irFile);
		    HashSet<Long> itemIds = new HashSet<Long>();
		    for(ItemFile f : files ){
			    itemIds.add(f.getItem().getId());
		    }
		    if( itemIds.size() > 0 ){
			    institutionalItems = institutionalItemService.getInstitutionalItemsByGenericItemIds(new LinkedList<Long>(itemIds));
			    personalItems = userPublishingFileSystemService.getAllPersonalItemsByGenericItemIds(new LinkedList<Long>(itemIds));
		    }
		    personalFiles = userFileSystemService.getPersonalFilesWithIrFile(irFile);
		    researcherFiles = researcherFileSystemService.getResearcherFilesWithIrFile(irFile);
		   
		}
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

	/**
	 * Set the checksum service.
	 * 
	 * @param checksumService
	 */
	public void setChecksumService(ChecksumService checksumService) {
		this.checksumService = checksumService;
	}

	public void setChecksumCheckerService(
			ChecksumCheckerService checksumCheckerService) {
		this.checksumCheckerService = checksumCheckerService;
	}
	
	public void setResearcherFileSystemService(
			ResearcherFileSystemService researcherFileSystemService) {
		this.researcherFileSystemService = researcherFileSystemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}
	
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}
	
	public List<InstitutionalItem> getInstitutionalItems() {
		return institutionalItems;
	}
	
	public List<IrFile> getIrFiles() {
		return irFiles;
	}
	
	public List<PersonalFile> getPersonalFiles() {
		return personalFiles;
	}

	public List<ResearcherFile> getResearcherFiles() {
		return researcherFiles;
	}

    public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}
    
	public List<PersonalItem> getPersonalItems() {
		return personalItems;
	}
}
