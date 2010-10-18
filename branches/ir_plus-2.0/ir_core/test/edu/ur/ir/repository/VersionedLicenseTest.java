package edu.ur.ir.repository;

import org.testng.annotations.Test;

import edu.ur.ir.user.IrUser;


/**
 * Test the license version
 * 
 * @author Nathan Sarr
 * 
 */

@Test(groups = { "baseTests" }, enabled = true)
public class VersionedLicenseTest 
{
	
	/**
	 * Test creating a versioned license
	 */
	public void createVersionedLicenseTest()
	{
		IrUser user = new IrUser();
		user.setLastName("familyName");
		user.setFirstName("forename");
		
		VersionedLicense versionedLicense = new VersionedLicense(user, "License text", "my license");
		
		LicenseVersion licenseVersion = versionedLicense.getCurrentVersion();
		assert licenseVersion.getVersionNumber() == 1 : "The version should equal 1 but equals " + 
		licenseVersion.getVersionNumber();
		
		assert licenseVersion.getLicense().getName().equals("my license") : "license name should equal my license but equals " + licenseVersion.getLicense();
	    assert licenseVersion.getLicense().getText().equals("License text") : "license text should equal License text but equals " + licenseVersion.getLicense().getText(); 
	    
	    
	    versionedLicense.addNewVersion("new text", user);
		LicenseVersion newVersion = versionedLicense.getCurrentVersion();

	    assert newVersion.getVersionNumber() == 2 : "The version should equal 2 but equals " + 
		newVersion.getVersionNumber();
		
		assert newVersion.getLicense().getName().equals("my license") : "license name should equal my license but equals " + licenseVersion.getLicense();
	    assert newVersion.getLicense().getText().equals("new text") : "license text should equal License text but equals " + licenseVersion.getLicense().getText(); 
	}

}
