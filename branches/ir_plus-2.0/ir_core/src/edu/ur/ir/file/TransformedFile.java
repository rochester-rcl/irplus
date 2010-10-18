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

package edu.ur.ir.file;

import edu.ur.file.db.FileInfo;
import edu.ur.persistent.BasePersistent;

/**
 * Represents a file that has been transformed from another file.  For example, 
 * a thumbnail.  This derived file can be given it's own name and 
 * description.
 * 
 * @author Nathan Sarr
 *
 */
public class TransformedFile extends BasePersistent{
	
	/**  Eclipse generated id. */
	private static final long serialVersionUID = 2357742407438001047L;

	/** The File derived from the original */
	private FileInfo transformedFile;
	
	/** Information about the derived file */
	private TransformedFileType transformedFileType;
	
	/** The file the derived file was created from */
	private IrFile actualFile;
	
	/**  Indicates the file  can be viewed by the public*/
	private boolean publicViewable = false;
	
	
	/**
	 * Package protected constructor
	 */
	TransformedFile(){}
	
	/**
	 * Public constructor.
	 * 
	 * @param actualFile - the file used to create the derivation.
	 * @param derivedFile - the derived file.
	 * @param derivedFileType - the type of derived file created.
	 */
	public TransformedFile(IrFile actualFile, 
			FileInfo transformedFile, 
			TransformedFileType derivedFileType )
	{
		setActualFile(actualFile);
		setTransformedFile(transformedFile);
		setTransformedFileType(derivedFileType);
	}

	/**
	 * Get the actual file used to create the derived file.
	 * 
	 * @return
	 */
	public IrFile getActualFile() {
		return actualFile;
	}

	/**
	 * Set the actual file.
	 * 
	 * @param actualFile
	 */
	void setActualFile(IrFile actualFile) {
		this.actualFile = actualFile;
	}

	/**
	 * Get the derived file
	 * 
	 * @return
	 */
	public FileInfo getTransformedFile() {
		return transformedFile;
	}

	/**
	 * Set the derived file.
	 * 
	 * @param transformedFile
	 */
	void setTransformedFile(FileInfo transformedFile) {
		this.transformedFile = transformedFile;
	}

	/**
	 * Type of derived file.
	 * 
	 * @return
	 */
	public TransformedFileType getTransformedFileType() {
		return transformedFileType;
	}

	/**
	 * Set the type of the transformed file.
	 * 
	 * @param derivedFileType
	 */
	void setTransformedFileType(TransformedFileType transformedFileType) {
		this.transformedFileType = transformedFileType;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append( " publicViewable = " + publicViewable);
		sb.append(" version = ");
		sb.append(version);
		sb.append("]");
		return sb.toString();
		
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += transformedFile == null ? 0 : transformedFile.hashCode();
		value += actualFile == null ? 0 : actualFile.hashCode();
		value += transformedFileType == null ? 0 : transformedFileType.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof TransformedFile)) return false;

		final TransformedFile other = (TransformedFile) o;
		
		if( ( transformedFile != null && !transformedFile.equals(other.getTransformedFile()) ) ||
			( transformedFile == null && other.getTransformedFile() != null ) ) return false;
		
		if( ( actualFile != null && !actualFile.equals(other.getActualFile()) ) ||
			( actualFile == null && other.getActualFile() != null ) ) return false;
	
		if( ( transformedFileType != null && !transformedFileType.equals(other.getTransformedFileType()) ) ||
			( transformedFileType == null && other.getTransformedFileType() != null ) ) return false;
		
		return true;
	}

	/**
	 * Returns true if the file can be viewed by the public.
	 * 
	 * @return
	 */
	public boolean isPublicViewable() {
		return publicViewable;
	}
	
	/**
	 * Determines if the file can be viewed by the public.
	 * 
	 * @return
	 */
	public boolean getPublicViewable()
	{
		return isPublicViewable();
	}

	/**
	 * Set to true if the file can be viewed by the public.
	 * 
	 * @param publicViewable
	 */
	public void setPublicViewable(boolean publicViewable) {
		this.publicViewable = publicViewable;
	}

}
