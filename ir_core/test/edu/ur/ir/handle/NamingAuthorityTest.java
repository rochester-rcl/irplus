package edu.ur.ir.handle;

import org.testng.annotations.Test;

/**
 * Quick basic tests for naming authority.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class NamingAuthorityTest {
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		NameAuthority authority = new NameAuthority();
		authority.setNamingAuthority("0.NA");
		authority.setLocalName("12345");
		authority.setId(55l);
		authority.setVersion(33);
		
		assert authority.getNamingAuthority().equals("0.NA") : "Should equal 0.NA but equals " + authority.getNamingAuthority();
		assert authority.getLocalName().equals("12345") : "Shoud equal 12345 but equals " + authority.getLocalName();
		assert authority.getFullNameAuthority().equals("0.NA/12345") : "should equal 0.NA/12345 but equals " + authority.getFullNameAuthority(); 
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		NameAuthority authority1 = new NameAuthority();
		authority1.setNamingAuthority("0.NA");
		authority1.setLocalName("12345");
		authority1.setId(1l);
		authority1.setVersion(33);
		
		NameAuthority authority2 = new NameAuthority();
		authority2.setNamingAuthority("0.NA1");
		authority2.setLocalName("111111");
		authority2.setId(2l);
		authority2.setVersion(33);

		
		NameAuthority authority3 = new NameAuthority();
		authority3.setNamingAuthority("0.NA");
		authority3.setLocalName("12345");
		authority3.setId(1l);
		authority3.setVersion(33);
		
		assert authority1.equals(authority3) : "Name authorties should be equal authority1 = " + authority1 + " authority 3 = " + authority3;
		assert !authority1.equals(authority2) : "Name authorties should not be equal auhority 1 = " + authority1 + " authority 2 = " + authority2;
		
		assert authority1.hashCode() == authority3.hashCode() : "Hash codes should be the same authority1 = " + authority1.hashCode() + " authority 3 = " + authority3.hashCode();
		assert authority2.hashCode() != authority3.hashCode() : "Hash codes should not be the same authority2 = " + authority2.hashCode() + " authority 3 = " + authority3.hashCode();
	}

}
