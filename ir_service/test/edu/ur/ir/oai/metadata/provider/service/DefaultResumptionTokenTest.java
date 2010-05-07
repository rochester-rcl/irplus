/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.oai.metadata.provider.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.Test;

/**
 * Tests for the resumption token class.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultResumptionTokenTest {
	
	/** short date format for oai */
	private SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/** long date format */
	private SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	/**
	 * Make sure the resumption token properly parses valid strings
	 * @throws ParseException 
	 */
	public void resumptionTokenParsingTest() throws ParseException
	{
		String strFromShort = "1998-10-22";
		String strFromLong = "2000-01-22T10:25:33Z";
		
		String strUntilShort = "1998-10-23";
		String strUntilLong = "2001-01-13T10:27:33Z";
		
		Date fromShort = shortDateFormat.parse(strFromShort);
		Date fromLong = longDateFormat.parse(strFromLong);
		
		Date untilShort = shortDateFormat.parse(strUntilShort);
		Date untilLong = longDateFormat.parse(strUntilLong);	
		
		String token = "set=123;from=" + strFromShort + 
		";until=" + strUntilShort + ";metadataPrefix=oai_dc;lastId=33;batchSize=100;deleted=false";
		DefaultResumptionToken resumptionToken = new DefaultResumptionToken();
		resumptionToken.parseResumptionToken(token);
		
		assert resumptionToken.getBatchSize().equals(100) : "Batch size should equal 100 but equals " + resumptionToken.getBatchSize();
		assert resumptionToken.getFrom().equals(fromShort) : "from date should equal " + fromShort + " but equals " + resumptionToken.getFrom();
		assert resumptionToken.getUntil().equals(untilShort) : "until date should equal " + untilShort + " but equals " + resumptionToken.getUntil();
        assert resumptionToken.getLastId().equals(33l) : "last id should = 333 but equals " + resumptionToken.getLastId();
        assert resumptionToken.getMetadataPrefix().equals("oai_dc") : "Should equal oai_dc but equals " + resumptionToken.getMetadataPrefix();
        assert resumptionToken.getSet().equals(123l) : "set should = 123 but equals " + resumptionToken.getLastId();

	
        // change the dates to long format 
		token = "set=123;from=" + strFromLong + 
		";until=" + strUntilLong + ";metadataPrefix=oai_dc;lastId=33;batchSize=100;deleted=false";
		resumptionToken.parseResumptionToken(token);
		assert resumptionToken.getBatchSize().equals(100) : "Batch size should equal 100 but equals " + resumptionToken.getBatchSize();
		assert resumptionToken.getFrom().equals(fromLong) : "from date should equal " + fromLong + " but equals " + resumptionToken.getFrom();
		assert resumptionToken.getUntil().equals(untilLong) : "until date should equal " + untilLong + " but equals " + resumptionToken.getUntil();
        assert resumptionToken.getLastId().equals(33l) : "last id should = 33 but equals " + resumptionToken.getLastId();
        assert resumptionToken.getSet().equals(123l) : "set should = 123 but equals " + resumptionToken.getLastId();

        assert resumptionToken.getMetadataPrefix().equals("oai_dc") : "Should equal oai_dc but equals " + resumptionToken.getMetadataPrefix();
        
        // make sure token is re-created correctly
        assert resumptionToken.getAsTokenString().equals(token) : "Token " + token + " should equal " + resumptionToken.getAsTokenString();
	
        
        // try parsing a null token
        resumptionToken.parseResumptionToken("");
        assert resumptionToken.getAsTokenString().equals("") : "Resumption token should be empty but is " + resumptionToken.getAsTokenString();
	}

}
