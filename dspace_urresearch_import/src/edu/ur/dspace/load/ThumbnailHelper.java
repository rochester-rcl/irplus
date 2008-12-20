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

package edu.ur.dspace.load;

import java.io.File;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TemporaryFileCreator;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.transformer.BasicThumbnailTransformer;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;

/**
 * Class to help with thumb nailing files
 * 
 * @author NathanS
 *
 */
public class ThumbnailHelper {
	
	
	/**
	 * Thumbnail the specified ir file and save to the repository.
	 * 
	 * @param logo
	 * @param repo
	 * @param basicThumbnailTransformer
	 * @param temporaryFileCreator
	 * @param repositoryService
	 * @param description
	 * @throws Exception
	 */
	public static void thumbnailFile(IrFile logo, Repository repo, 
			BasicThumbnailTransformer basicThumbnailTransformer, 
			TemporaryFileCreator temporaryFileCreator,
			RepositoryService repositoryService, String description) throws Exception
	{
		String extension = logo.getFileInfo().getExtension();
		
		System.out.println("Extension = " + extension);
		System.out.println("Checking thumbnail " + basicThumbnailTransformer  + " can transform = " + basicThumbnailTransformer.canTransform(extension));
		
	    if(basicThumbnailTransformer.canTransform(extension))
        {
	            File tempFile = temporaryFileCreator.createTemporaryFile(extension);
	             basicThumbnailTransformer.transformFile(new File(logo.getFileInfo().getFullPath()), extension, tempFile);
	             TransformedFileType transformedFileType = repositoryService.getTransformedFileTypeBySystemCode("PRIMARY_THUMBNAIL");
	    
	            repositoryService.addTransformedFile(repo, 
	    		logo, 
	    		tempFile, 
	    		description, 
	    		basicThumbnailTransformer.getFileExtension(), 
	    		transformedFileType);
	            
	            repositoryService.saveRepository(repo);
	    
        }
	}

}
