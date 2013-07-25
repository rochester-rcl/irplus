package edu.ur.file.db;

/**
 * Service for checking and updating the checksums.
 * 
 * @author Nathan Sarr
 *
 */
public interface ChecksumCheckerService {
	
	/**
	 * Check the checksum of the file.
	 * 
	 * @param checksumInfo
	 * @return the updated file info checksum
	 */
	public FileInfoChecksum checkChecksum(FileInfoChecksum checksumInfo);
	

}
