package edu.ur.file.db.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import edu.ur.file.checksum.ChecksumCalculator;
import edu.ur.file.checksum.ChecksumService;
import edu.ur.file.db.ChecksumCheckerService;
import edu.ur.file.db.FileInfoChecksum;
import edu.ur.file.db.FileInfoChecksumService;

public class DefaultChecksumCheckerService implements ChecksumCheckerService {

	private ChecksumService checksumService;
	

	/* service to deal with file info checksum data */
	private FileInfoChecksumService fileInfoChecksumService;


	/**
	 * Check the checksum of the file.
	 * 
	 * @param checksumInfo
	 * @return the updated file info checksum
	 */
	public FileInfoChecksum checkChecksum(FileInfoChecksum checksumInfo)
	{
		boolean ok = true;
		if( checksumInfo != null )
		{
		    ChecksumCalculator calculator = checksumService.getChecksumCalculator(checksumInfo.getAlgorithmType());
		    if( calculator != null )
		    {
		    	File f = new File(checksumInfo.getFileInfo().getFullPath());
		    	String checksum = calculator.calculate(f);
		    	ok = checksumInfo.getChecksum().equals(checksum);
		    	checksumInfo.setReCalculatedPassed(ok);
		    	checksumInfo.setDateReCalculated(new Timestamp(new Date().getTime()));
		    	checksumInfo.setReCalculatedValue(checksum);
		    	
		    	if(!ok)
		    	{
		    		checksumInfo.setReCalculateChecksum(false);
		    		checksumInfo.setReCalculatedPassed(false);
		    	}
		    	else
		    	{
		    		checksumInfo.setReCalculateChecksum(true);
		    		checksumInfo.setReCalculatedPassed(true);
		    		checksumInfo.setDateLastCheckPassed(new Timestamp(new Date().getTime()));
		    	}
		    		
		    	fileInfoChecksumService.save(checksumInfo);
		    }
		    else
		    {
		    	throw new IllegalStateException("Could not find calculator for checksum info " + checksumInfo);
		    }
		}
		return checksumInfo;
	}
	
	/**
	 * Get the file info checksum service.
	 * 
	 * @return
	 */
	public FileInfoChecksumService getFileInfoChecksumService() {
		return fileInfoChecksumService;
	}

	/**
	 * Set the file info checksum service.
	 * 
	 * @param fileInfoChecksumService
	 */
	public void setFileInfoChecksumService(
			FileInfoChecksumService fileInfoChecksumService) {
		this.fileInfoChecksumService = fileInfoChecksumService;
	}
	
	/**
	 * Get the checksum service.
	 * 
	 * @return
	 */
	public ChecksumService getChecksumService() {
		return checksumService;
	}

	/**
	 * Set the checksum service.
	 * 
	 * @param checksumService
	 */
	public void setChecksumService(ChecksumService checksumService) {
		this.checksumService = checksumService;
	}

	
}
