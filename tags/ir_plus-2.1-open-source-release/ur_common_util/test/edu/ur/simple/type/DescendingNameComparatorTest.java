package edu.ur.simple.type;

import org.testng.annotations.Test;


/**
 * Test the name comparator
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups ={"baseTests"}, enabled = true)
public class DescendingNameComparatorTest {
	
	public void testAscendingOrder()
	{
		
		NameTest nt1 = new NameTest("A");
		NameTest nt2 = new NameTest("B");
		NameTest nt4 = new NameTest("z");
		
		DescendingNameComparator anc = new DescendingNameComparator();
		assert anc.compare(nt1, nt1) == 0: "Should equal 0 but equals " + anc.compare(nt1, nt1);
		assert anc.compare(nt1, nt2) >= 1: "Should be less than 1 but is " + anc.compare(nt1, nt2);
		assert anc.compare(nt2, nt1) <= 1: "Should equal 1 but equals " + anc.compare(nt2, nt1);
		assert anc.compare(nt4, nt2) <= 1: "Should equal -1 but equals " + anc.compare(nt4, nt2);
		
	}
	
	/**
	 * Simple class for testing.
	 * 
	 * @author Nathan Sarr
	 *
	 */
	class NameTest implements NameAware
	{

		private String name;
		
		public NameTest(String name)
		{
		    this.name = name;	
		}
		
		public String getName() {
			return name;
		}
		
	}
}
