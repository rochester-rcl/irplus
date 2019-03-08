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
package edu.ur.file.db;

import java.io.File;
import java.io.Serializable;
import java.io.IOException;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ur.file.IllegalFileSystemNameException;

/**
 * Deals with creating and writing files
 * and folders to the file system.  Returns
 * folder and file information objects.  
 * 
 * @author Nathan Sarr
 *
 */
public class FileSystemManager implements Serializable{
	
	/**
	 * Logger for the file system manager
	 */
	private static final Logger log = LogManager.getLogger(FileSystemManager.class);
	
	/**
	 * Eclipse generated id.
	 */
	private static final long serialVersionUID = 4768917487636488944L;
	
	/**
	 * Create a new folder in the file system with the 
	 * specified path.  Boot strap method to create
	 * a root folder.
	 * 
	 * @param path
	 * @return a new Folder info object.
	 * @throws LocationAlreadyExistsException 
	 */
	public static TreeFolderInfo createFolder( String folderName, DefaultFileDatabase fd ) throws LocationAlreadyExistsException
	{
		TreeFolderInfo info = null;
		
		if( createDirectory(fd.getFullPath() + folderName))
		{
			info = new TreeFolderInfo();
			info.setFileDatabase(fd);
			info.updatePaths(fd.getFullPath());
			info.setName(folderName);
			info.setExists(true);
		}
		else
		{
			throw new IllegalStateException("Folder could not be created");
		}
		
		return info;
	}
	
	/**
	 * Delete the folder recursively from the file system.
	 * 
	 * @param folderInfo the folder to delete
     *
	 * @return true if the folder path is deleted
	 */
	static boolean deleteFolder(TreeFolderInfo folderInfo)
	{
		boolean deleted = false;
		
		if( deleteDirectory(folderInfo.getFullPath()) )
		{
			folderInfo.setExists(false);
			folderInfo.setParent(null);
			folderInfo.setFileDatabase(null);
			deleted = true;
		}
		else
		{
			throw new IllegalStateException("Folder " + 
					folderInfo.getFullPath() 
					+ " could not be deleted");
		}
		return deleted;
	}
	
	/**
	 * Move a folder to the top level of a file database.
	 * 
	 * @param current
	 * @param destination
	 * @return true if the folder is moved.
	 */
	public static boolean moveFolder(TreeFolderInfo current, DefaultFileDatabase destination)
	{
		if(log.isDebugEnabled())
		{
			log.debug("Moving folder " + current.toString() );
			log.debug("To file database " + destination.toString());
		}
		boolean moved = false;
		String beforeMovePath = current.getFullPath();
		String newChildPath = destination.getFullPath() + 
		    current.getName() + IOUtils.DIR_SEPARATOR;
		
		if( renameFile(new File(beforeMovePath), new File(newChildPath)) )
		{
			destination.addExistingFolder(current);
			moved = true;
		}
		else
		{
			throw new IllegalStateException("Could not move directory to " +
					newChildPath);
		}
		
		if(log.isDebugEnabled())
		{
			log.debug("Move Complete");
		}
		return moved;
	}
	
	/**
	 * Add a child folder to the parent folder.
	 * 
	 * @param parent to add child to
	 * @param childName name of the child to create.
	 * @return the child folder created.
	 * @throws LocationAlreadyExistsException 
	 */
	static TreeFolderInfo createChildFolder(TreeFolderInfo parent, String childName) throws LocationAlreadyExistsException
	{
		TreeFolderInfo info = null;
		
		if( createDirectory(parent.getFullPath() + childName))
		{
			info = new TreeFolderInfo();
		}
		else
		{
			throw new IllegalStateException("Child Folder " + 
					parent.getFullPath() + childName + 
					" could not be created");				
		}
		return info;
	}
	
	/**
	 * Move a file from one folder to another.  Once the move is
	 * complete the old files are deleted.
	 * 
	 * @param current directory the file is in
	 * @param destination directory to move the file to.
	 * 
	 * @param fileInfo the file information.
	 */
	public static void moveFile(TreeFolderInfo destination, DefaultFileInfo fileInfo)
	{
		File fileToMove = new File(fileInfo.getFullPath());
		File destDirectory = new File(destination.getFullPath());
		File newFileDestName = new File(destination.getFullPath() 
				+ IOUtils.DIR_SEPARATOR + fileInfo.getName());
			
		if(!fileToMove.exists())
		{
			throw new IllegalStateException("The file " + fileToMove.getAbsolutePath() + 
					" does not exist ");
		}
		
		if(!destDirectory.exists())
		{
			throw new IllegalStateException("The directory " + destDirectory.getAbsolutePath() + 
			" does not exist ");
		}
		
		//copy the file.
		if( renameFile(fileToMove, newFileDestName) )
		{
		    destination.addFileInfo(fileInfo);
		}
		else
		{
			throw new IllegalStateException( "Could not move " + fileToMove.getAbsolutePath() 
					+ " to " + newFileDestName.getAbsolutePath());
		}
	}
	
	/**
	 * Add a file to the parent folder.  This copies the file
	 * from it's current location.  The file must exist.   This
	 * does not delete the file once the copy is complete.
	 *
	 * @param parent folder to add the file to
	 * @param File to save 
	 * @param uniqueName name in the file system to save the file to.
	 * 
	 * @return the information of the created file.
	 * @throws IllegalFileSystemNameException 
	 */
	static DefaultFileInfo addFile(TreeFolderInfo parent, File file, String uniqueName) throws IllegalFileSystemNameException
	{
		DefaultFileInfo fileInfo = null;
		
		if( !file.isFile())
		{
			throw new IllegalStateException("The file to add must be a file  " + file.getAbsolutePath());
		}
		
		if( !file.exists() )
		{
			throw new IllegalStateException("the file " + file.getAbsolutePath() + " does not exist ");
		}
		
		if(!file.canRead())
		{
			throw new IllegalStateException("The file " + file.getAbsolutePath() + " cannot be read ");
		}
		
	    File newFile = new File(parent.getFullPath() + uniqueName);
	    
	    try
	    {
	    	fileInfo = new DefaultFileInfo();
	    	fileInfo.setFolderInfo(parent);
	    	fileInfo.setName(uniqueName);
			fileInfo.setModifiedDate(new Timestamp(new Date().getTime()));
			fileInfo.setCreatedDate(new Timestamp(new Date().getTime()));
		    FileUtils.copyFile(file, newFile);
		    fileInfo.setExists(true);
		    fileInfo.setSize(newFile.length());
		   
	    }
	    catch( IOException ioe)
	    {
	    	throw new RuntimeException(ioe);
	    }
		return fileInfo;
	}
	
	/**
	 * Add a file to the parent folder.  This creates an empty
	 * file with the specified name.  This is good for creating 
	 * an empty file that has a size of 0.
	 *
	 * @param parent folder to add the file to
	 * @param uniqueName name in the file system to save the file to.
	 * 
	 * @return the information of the created file.
	 * @throws IllegalFileSystemNameException 
	 */
	static DefaultFileInfo addFile(TreeFolderInfo parent, String uniqueName) throws IllegalFileSystemNameException
	{
		DefaultFileInfo fileInfo = null;
		
	    File newFile = new File(parent.getFullPath() + uniqueName);
	    
	    try
	    {
	    	fileInfo = new DefaultFileInfo();
	    	fileInfo.setFolderInfo(parent);
	    	fileInfo.setName(uniqueName);
			fileInfo.setModifiedDate(new Timestamp(new Date().getTime()));
			fileInfo.setCreatedDate(new Timestamp(new Date().getTime()));
		    FileUtils.touch(newFile);
		    fileInfo.setExists(true);
		    fileInfo.setSize(newFile.length());
		   
	    }
	    catch( IOException ioe)
	    {
	    	throw new RuntimeException(ioe);
	    }
		return fileInfo;
	}
	
	/**
	 * Delete a file from the file system.
	 * Updates file info with the correct information.
	 * 
	 * @param file to be deleted
	 * @return true if the file is deleted
	 */
	static boolean deleteFile(DefaultFileInfo file)
	{
		boolean deleted = false;
		File f = new File(file.getFullPath());
		if(deleteFile(f))
		{
			deleted = true;
			file.setExists(false);
			file.setModifiedDate(new Timestamp(new Date().getTime()));
		}
		
		return deleted;
	}
	
	/**
	 * Delete the diectory recusively specified by the
	 * path.  Throws a runtime exception if the path
	 * cannot be deleted.
	 * 
	 * @param path
	 * @return true if the path is deleted
	 */
	static boolean deleteDirectory(String path)
	{
		File f = new File(path);
		boolean deleted = false;
		
		if( f.exists() )
		{
			try
			{
			    FileUtils.deleteDirectory(f);
			    deleted = !f.exists();
			}
			catch( IOException io)
			{
				log.error("failed to delete", io);
				throw new RuntimeException("Trying to delete directory path = " + path, io);
			}
		}
		else
		{
			deleted = true;
		}
		
		return deleted;
	}
	
	/**
	 * Delete the directory recursively specified by the
	 * path.  Throws a runtime exception if the path
	 * cannot be deleted.
	 * 
	 * @param path
	 * @return true if the path is deleted
	 * @throws LocationAlreadyExistsException - if the location already exits
	 * @throws Runtime exception if an IO error occurs.
	 */
	static boolean createDirectory(String path) throws LocationAlreadyExistsException
	{
		File f = new File(path);
		boolean created = false;
				
		if( f.exists() )
		{
			throw new LocationAlreadyExistsException("Folder path already " + f.getAbsolutePath() + " exists", f.getAbsolutePath());
		}
		try
		{
		    FileUtils.forceMkdir(f);
		    created = f.exists();
		    
		}
		catch( IOException io)
		{
			throw new RuntimeException("trying to create directory path = " + path, io);
		}
		return created;
	}
	
	/**
	 * Move the directory from one location to another.  It currently
	 * does this by a copy and delete.
	 * 
	 * @param currentPath path including the directory name
	 * @param destPath path including the new directory name.
	 * 
	 * @return true if the directory is moved
	 * 
	 * @throws Illegal state exception if the currentPath does not exist
	 * @throws Illegal state exception if the destintation path exists
	 *         meaning an overwrite is occuring.
	 * @throws RuntimeExcpetion if the file cannot be moved
	 */
	static boolean copyDirectory(String currentPath, String destPath)
	{
		if( log.isDebugEnabled())
		{
		    log.debug("Copying " + currentPath + " to " + destPath);
		}
		
		boolean copied = false;
		File current = new File(currentPath);
		
		if( !current.exists() )
		{
			throw new IllegalStateException("Current directory does not exist " + current.getAbsolutePath());
		}
		
		File dest = new File(destPath);
		
		if( dest.exists() )
		{
			throw new IllegalStateException("Destination " + dest.getAbsolutePath() + 
					" already exists cannot overwrite. " +
					" current directory must be copied to a non-existent directory");
		}
		
		try
		{
		    FileUtils.copyDirectory(current, dest);
		    copied = dest.exists();
		}
		catch( IOException io)
		{
			throw new RuntimeException(io);
		}
		
		return copied;
	}
	
	
	/**
	 * Copy the file to the specified directory.
	 * 
	 * @param file the file to copy
	 * @param directory to copy the file to
	 * 
	 * @return true if the file is copied.
	 */
	static boolean copyFileToDirectory(File file, File directory )
	{
		try
		{
		    FileUtils.copyFileToDirectory(file, directory);
		}
		catch( IOException io)
		{
			throw new RuntimeException("Trying to copy file " + file.getAbsolutePath() +
					" to directory " + directory.getAbsolutePath(), io);
		}
		
		return true;
	}
	
	/**
	 * Delete a file from the file system.
	 * 
	 * @param file
	 * @return true if the file is deleted from the file system.
	 */
	static boolean deleteFile(File file)
	{
		if( !file.exists())
		{
			return true;
		}
		try
		{
		    FileUtils.forceDelete(file);
		}
		catch( IOException io)
		{
			throw new RuntimeException("Could not delete file " + file.getAbsolutePath(), io);
		}
		
		return true;
	}
	
	/**
	 * Rename the file to the new file.
	 * 
	 * @param start the file to move
	 * @param destination destination to move to
	 * @return true if the file is renamed to.
	 */
	static boolean renameFile(File start, File destination)
	{
		return start.renameTo(destination);
	}
	
}
