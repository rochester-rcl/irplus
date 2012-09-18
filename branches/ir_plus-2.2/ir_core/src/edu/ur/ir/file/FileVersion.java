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

import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;

/**
 *  This class links the IrFile and VerionedFile and represents a
 *  single version of the ir file in a set of versions.
 *  
 * @author Nathan Sarr
 *
 */
public class FileVersion extends BasePersistent{

	/** Eclipse generated id */
	private static final long serialVersionUID = -6634509820993498037L;
	
	/** Specific Ir file which is held  */
	private IrFile irFile;
	
	/** Parent versioned file  */
	private VersionedFile versionedFile;
	
	/** Particular version number in the versioned file */
	private int versionNumber;
	
	/** User who creted this versioned file */
	private IrUser versionCreator;	
	
	/**
	 * Package protected constructor
	 */
	FileVersion(){}
	
	/**
	 * Package protected constructor
	 * 
	 * @param irFile
	 * @param versionedFile
	 */
	FileVersion(IrFile irFile, VersionedFile versionedFile, int versionNumber, IrUser versionCreator)
	{
		if( versionNumber <= 0 )
		{
			throw new IllegalStateException("Version number must be greater than 0 version = " + versionNumber);
		}
		setIrFile(irFile);
		setVersionedFile(versionedFile);
		setVersionNumber(versionNumber);
		setVersionCreator(versionCreator);
	}

	/**
	 * Returns Ir file
	 * @return
	 */
	public IrFile getIrFile() {
		return irFile;
	}
	
	/**
	 * Sets IRFile
	 * @param irFile
	 */
	public void setIrFile(IrFile irFile) {
		this.irFile = irFile;
	}

	/**
	 * Returns VersionedFile
	 * @return
	 */
	public VersionedFile getVersionedFile() {
		return versionedFile;
	}

	/**
	 * Sets VersionedFile
	 * @param versionedFile
	 */
	public void setVersionedFile(VersionedFile versionedFile) {
		this.versionedFile = versionedFile;
	}

	/**
	 * Returns version number of file
	 * @return
	 */
	public int getVersionNumber() {
		return versionNumber;
	}

	/**
	 * Sets the version number for file
	 * 
	 * @param versionNumber
	 */
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ File Version id = ");
		sb.append(id);
		sb.append( " version number = ");
		sb.append(versionNumber);
		sb.append( " VersionedFile = " );
		sb.append(versionedFile);
		sb.append( " irFile = " );
		sb.append(irFile);
		sb.append("]");
		return sb.toString();
	}
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof FileVersion)) return false;

		final FileVersion other = (FileVersion) o;
		if( (versionedFile != null && !versionedFile.equals(other.getVersionedFile()) ) ||
			(versionedFile == null && other.getVersionedFile() != null ) ) return false;
		if(getVersionNumber() != other.getVersionNumber()) return false;
		return true;
	}
	
	public int hashCode()
	{
		int value = 0;
		value += getVersionNumber();
		value += getVersionedFile() == null ? 0 : getVersionedFile().hashCode();
		return value;
	}

	/**
	 * Returns the user who created this version
	 * 
	 * @return version creator
	 */
	public IrUser getVersionCreator() {
		return versionCreator;
	}

	/**
	 * Sets the version creator 
	 * 
	 * @param versionCreator creator of this version
	 */
	public void setVersionCreator(IrUser versionCreator) {
		this.versionCreator = versionCreator;
	}

}
