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


package edu.ur.tag.repository;

import java.util.Collection;

import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;

public class CollectionWebUtilFunctions {
	
	/**
	 * Determine if the contents can be moved into the specified location.  This is true 
	 * if the destination is not equal to the current destination.  A collection cannot be 
	 * moved into itself;
	 * 
	 * @param objectsToMove - set of information to be moved
	 * @param destination - destination  to move to
	 * @return true if the set of information can be moved into the specified location
	 */
	public static boolean canMoveToPersonalCollection(Collection<FileSystem> objectsToMove, FileSystem destination)
	{
		
		if( !destination.getFileSystemType().equals(FileSystemType.PERSONAL_COLLECTION))
	    {
		    return false;	
		}
		
				
		boolean canMove = true;
		
		for(FileSystem fileSystemObject : objectsToMove)
		{
			if( fileSystemObject.getFileSystemType().equals(FileSystemType.PERSONAL_COLLECTION))
			{
			    if( fileSystemObject.getId().equals(destination.getId()))
			    {
			    	canMove = false;
			    }
			}
		}
		
		return canMove;
	}

	
	/**
	 * Determine if the destination is one of the item that has to be moved
	 * 
	 * @param objectsToMove - set of information to be moved
	 * @param destination - destination  to move to
	 * @return true if the destination item is one of the items to be moved
	 */
	public static boolean isPublicationToBeMoved(Collection<FileSystem> objectsToMove, FileSystem destination)
	{
		
		if( !destination.getFileSystemType().equals(FileSystemType.PERSONAL_ITEM))
	    {
		    return false;	
		}
		
				
		boolean isItemToMove = false;
		
		for(FileSystem fileSystemObject : objectsToMove)
		{
			if( fileSystemObject.getFileSystemType().equals(FileSystemType.PERSONAL_ITEM))
			{
			    if( fileSystemObject.getId().equals(destination.getId()))
			    {
			    	isItemToMove = true;
			    }
			}
		}
		
		return isItemToMove;
	}
}
