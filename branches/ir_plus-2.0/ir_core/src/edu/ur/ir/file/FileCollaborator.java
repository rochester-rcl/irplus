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
 * Collaborator for the file
 * 
 * @author Sharmila Ranganathan
 *
 */
public class FileCollaborator extends BasePersistent {
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = -4750503053257696195L;

	/**  Collaborator for the file */
	private IrUser collaborator;
	
	/**  File that is shared */
	private VersionedFile versionedFile;

	/**  Package protected constructor */
	FileCollaborator(){};
	
	
	/**
	 * Constructor.
	 *  
	 * @param 
	 */
	public FileCollaborator(IrUser collaborator, VersionedFile versionedFile)
	{
		this.setCollaborator(collaborator);
		this.setVersionedFile(versionedFile);
	}


	/**
	 * Get the collaborator for file
	 * 
	 * @return Collaborator
	 */
	public IrUser getCollaborator() {
		return collaborator;
	}

	/**
	 * Set the collaborator
	 * 
	 * @param collaborator collaborator
	 */
	void setCollaborator(IrUser collaborator) {
		this.collaborator = collaborator;
	}

	/**
	 * Get file that is collaborated
	 * 
	 * @return versioned file
	 */
	public VersionedFile getVersionedFile() {
		return versionedFile;
	}

	/**
	 * Set the verioned file 
	 * 
	 * @param versionedFile file to collaborate
	 */
	void setVersionedFile(VersionedFile versionedFile) {
		this.versionedFile = versionedFile;
	}
	
    /**
     * Get the hash code.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
    	int hash = 0;
    	hash += versionedFile == null ? 0 : versionedFile.hashCode();
    	hash += collaborator == null ? 0 : collaborator.hashCode();
    	return hash;
    }
    
    /**
     * File Collaborator Equals.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
    	if (this == o) return true;
		if (!(o instanceof FileCollaborator)) return false;

		final FileCollaborator other = (FileCollaborator) o;

		if( ( collaborator != null && !collaborator.equals(other.getCollaborator()) ) ||
			( collaborator == null && other.getCollaborator() != null ) ) return false;
		
		if( ( versionedFile != null && !versionedFile.equals(other.getVersionedFile())) ||
			( versionedFile == null && other.getVersionedFile() != null ) ) return false;
		
		return true;
    }
	
	/**
	 * To string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[ Id = " );
		sb.append(id);
		sb.append(" IrUser = ");
		sb.append(collaborator);
		sb.append(" VersionedFile = ");
		sb.append(versionedFile);
		sb.append("]");
		return sb.toString();
	}

	

}
