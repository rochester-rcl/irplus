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

package edu.ur.ir.item;

import edu.ur.persistent.CommonPersistent;
import edu.ur.ir.file.IrFile;
/**
 * A license saved in the IR.
 * 
 * @author Nathan Sarr.
 *
 */
public class License extends CommonPersistent{

	/**
	 * Generated id.
	 */
	private static final long serialVersionUID = 2380384082339838487L;
	
	/**
	 * The version of the license
	 */
	private String licenseVersion;
	
	/**
	 * The file information for the license. 
	 */
	private IrFile irFile;
	
	
	/**
	 * Package protected constructor
	 */
	License(){}
	
	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param licenseVersion
	 */
	public License(String name, String licenseVersion)
	{
		setName(name);
		setLicenseVersion(licenseVersion);
	}
	
	/**
	 * Create a license with the name, version and specified file.
	 * 
	 * @param name - Name of the license
	 * @param licenseVersion - Version of the license
	 * @param file - file that holds the actual license
	 */
	public License(String name, String licenseVersion, IrFile irFile)
	{
		setIrFile(irFile);
		setName(name);
		setLicenseVersion(licenseVersion);
	}

	/**
	 * File information for the license.
	 * 
	 * @return
	 */
	public IrFile getIrFile() {
		return irFile;
	}

	/**
	 * File information for the license.
	 * 
	 * @param fileInfo
	 */
	public void setIrFile(IrFile irFile) {
		this.irFile = irFile;
	}

	/**
	 * Version of the license
	 * 
	 * @return
	 */
	public String getLicenseVersion() {
		return licenseVersion;
	}

	/**
	 * Version of the license.
	 * 
	 * @param licenseVersion
	 */
	public void setLicenseVersion(String licenseVersion) {
		this.licenseVersion = licenseVersion;
	}
	
	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += getName() == null ? 0 : getName().hashCode();
		value += getLicenseVersion() == null? 0 : getLicenseVersion().hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof License)) return false;

		final License other = (License) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		if( ( getLicenseVersion() != null && !getLicenseVersion().equals(other.getLicenseVersion()) ) ||
		    ( getLicenseVersion() == null && other.getLicenseVersion() != null ) ) return false;

		return true;
	}

	/** 
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "License id = " + id + " version = " + version + " name = " + name
		+ " licenseVersion = " + licenseVersion ;
	}

}
