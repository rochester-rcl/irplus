package edu.ur.ir.repository;

import org.testng.annotations.Test;

import edu.ur.ir.user.IrUser;

/**
 * Test the Repository Class
 * 
 * @author Nathan Sarr
 * 
 */

@Test(groups = { "baseTests" }, enabled = true)
public class LicenseTest 
{

    public void basicLicesneTest()
    {
		IrUser user = new IrUser();
		user.setLastName("familyName");
		user.setFirstName("forename");
		
		License license = new License("license1", "This is the license text", user);
		
		assert license.getDateCreated() != null : "License should exist";
		assert license.getCreator().equals(user) : "The user should equal the creator " + user;
		assert license.getText().equals("This is the license text");
    }
    
    public void licenseEqualsTest()
    {
    	IrUser user = new IrUser();
		user.setLastName("familyName");
		user.setFirstName("forename");
		
		License license1 = new License("license1", "This is the license text", user);
		License license2 = new License("license2", "This is different license text", user);
		License license3 = new License("license1", "This is the license text", user);
		
		assert license1.equals(license3) : "Licenses should be equal license 1 = " + license1 + 
		"license 3 = " + license3;
		
    	assert license1.hashCode() == license3.hashCode() : "Hash codes should be equal license 1 = "
    		+ license1.hashCode() + " licnese3 has code = " + license3.hashCode();
    	
		assert !license1.equals(license2) : "Licenses should be equal license 1 = " + license1 + 
		"license 2 = " + license2;
		
    	assert license1.hashCode() != license2.hashCode() : "Hash codes should be equal license 1 = "
    		+ license1.hashCode() + " licnese2 has code = " + license2.hashCode();
    	
    }
}
