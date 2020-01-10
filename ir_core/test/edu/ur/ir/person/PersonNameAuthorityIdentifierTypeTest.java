package edu.ur.ir.person;

import org.testng.annotations.Test;

@Test(groups = { "baseTests" }, enabled = true)
public class PersonNameAuthorityIdentifierTypeTest {
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		PersonNameAuthorityIdentifierType pt = new PersonNameAuthorityIdentifierType();
		pt.setName("itName");
		pt.setDescription("itDescription");
		pt.setId(55l);
		pt.setVersion(33);
		
		assert pt.getName().equals("itName") : "Should equal itName";
		assert pt.getDescription().equals("itDescription") : "Shoud equal itDescription";
		assert pt.getId().equals(55l) : "Should equal 55l";
		assert pt.getVersion() == 33 : "Should equal 33";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
		PersonNameAuthorityIdentifierType pt1 = new PersonNameAuthorityIdentifierType();
		pt1.setName("itName");
		pt1.setDescription("itDescription");
		pt1.setId(55l);
		pt1.setVersion(33);
		
		PersonNameAuthorityIdentifierType pt2 = new PersonNameAuthorityIdentifierType();
		pt2.setName("itName2");
		pt2.setDescription("itDescription2");
		pt2.setId(55l);
		pt2.setVersion(33);

		PersonNameAuthorityIdentifierType pt3 = new PersonNameAuthorityIdentifierType();
		pt3.setName("itName");
		pt3.setDescription("itDescription");
		pt3.setId(55l);
		pt3.setVersion(33);
		
		assert pt1.equals(pt3) : "Identifier types should be equal";
		assert !pt1.equals(pt2) : "Identifier types should not be equal";
		
		assert pt1.hashCode() == pt3.hashCode() : "Hash codes should be the same";
		assert pt2.hashCode() != pt3.hashCode() : "Hash codes should not be the same";
	}


}
