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
package edu.ur.file.checksum;

import java.io.File;

import org.testng.annotations.Test;

/**
 * Test calculating the checksum.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class Md5ChecksumCalculatorTest {
	
	public void testCalculate()
	{
	    Md5ChecksumCalculator md5Calc = new Md5ChecksumCalculator();
	    File f = new File("pdf-index-test.pdf");
	    assert f.exists() : "file " + f.getAbsolutePath() + " does not exist";
	    assert f.canRead() : "Cannot read file " + f.getAbsolutePath();
	    String checksum = md5Calc.calculate(f);
	    assert checksum.equals("6839764b09073771141896dc52ee8b41") : 
	    	"checksum equals = " + checksum + " but should equal "
	    	+ "6839764b09073771141896dc52ee8b41";
	    
	    f = new File("rtf-index-test.rtf");
	    assert f.exists(): "file " + f.getAbsolutePath() + " does not exist";
	    assert f.canRead(): "Cannot read file " + f.getAbsolutePath();
	    checksum = md5Calc.calculate(f);
	    assert checksum.equals("d7736ab4ca7389555802cfa131b6656d") : 
	    	"checksum equals = " + checksum + " but should equal "
	    	+ "d7736ab4ca7389555802cfa131b6656d";
	    

	    f = new File("word-index-test.doc");
	    assert f.exists(): "file " + f.getAbsolutePath() + " does not exist";
	    assert f.canRead(): "Cannot read file " + f.getAbsolutePath();
	    checksum = md5Calc.calculate(f);
	    assert checksum.equals("b6742b318c785c3a8a01d8611bb9f551") : 
	    	"checksum equals = " + checksum + " but should equal "
	    	+ "b6742b318c785c3a8a01d8611bb9f551";

	    
	}

}
