package edu.ur.ir.handle;

import org.testng.annotations.Test;

/**
 * Class to test Handle info
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class HandleInfoTest {
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		HandleNameAuthority authority1 = new HandleNameAuthority("12345");
		authority1.setId(1l);
		authority1.setVersion(33);
		
		HandleNameAuthority authority2 = new HandleNameAuthority("111111");
		authority2.setId(2l);
		authority2.setVersion(33);

		
		HandleInfo handleInfo1 = new HandleInfo("12345", "http://www.google.com", authority1);
		HandleInfo handleInfo2 = new HandleInfo("12345", "http://www.google.com", authority2);
		HandleInfo handleInfo3 = new HandleInfo("12345", "http://www.google.com", authority1);
		
		
		assert handleInfo1.equals(handleInfo3) : "Name handleInfos should be equal handleInfo1 = " + handleInfo1 + " handleInfo 3 = " + handleInfo3;
		assert !handleInfo1.equals(handleInfo2) : "Name handleInfos should not be equal handleInfo 1 = " + handleInfo1 + " handleInfo 2 = " + handleInfo2;
		
		assert handleInfo1.hashCode() == handleInfo3.hashCode() : "Hash codes should be the same handleInfo1 = " + handleInfo1.hashCode() + " handleInfo 3 = " + handleInfo3.hashCode();
		assert handleInfo2.hashCode() != handleInfo3.hashCode() : "Hash codes should not be the same handleInfo2 = " + handleInfo2.hashCode() + " handleInfo 3 = " + handleInfo3.hashCode();
	}


}
