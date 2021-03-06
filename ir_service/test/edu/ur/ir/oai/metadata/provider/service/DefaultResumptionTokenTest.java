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

import edu.ur.ir.oai.OaiUtil;
import edu.ur.ir.oai.exception.BadArgumentException;
import edu.ur.ir.oai.exception.BadResumptionTokenException;

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
	 * @throws BadResumptionTokenException 
	 * @throws BadArgumentException 
	 */
	public void resumptionTokenParsingTest() throws ParseException, BadResumptionTokenException, BadArgumentException
	{
		// these are zulu times and dates
		String strFromShort = "1998-10-22";
		String strFromLong = "2000-01-22T10:25:33Z";
		
		String strUntilShort = "1998-10-23";
		String strUntilLong = "2001-01-13T10:27:33Z";
		
		Date fromShort = shortDateFormat.parse(strFromShort);
		Date localFromShort = OaiUtil.getLocalTime(fromShort);
		Date fromLong = longDateFormat.parse(strFromLong);
		Date localFromLong = OaiUtil.getLocalTime(fromLong);
		
		
		Date untilShort = shortDateFormat.parse(strUntilShort);
		Date localUntilShort = OaiUtil.getLocalTime(untilShort);
		Date untilLong = longDateFormat.parse(strUntilLong);	
		Date localUntilLong = OaiUtil.getLocalTime(untilLong);
		
		String token = "set=123;from=" + strFromShort + 
		";until=" + strUntilShort + ";metadataPrefix=oai_dc;lastId=33;batchSize=100;deleted=false";
		DefaultResumptionToken resumptionToken = new DefaultResumptionToken();
		resumptionToken.parseResumptionToken(token);
		
		assert resumptionToken.getBatchSize().equals(100) : "Batch size should equal 100 but equals " + resumptionToken.getBatchSize();
		assert resumptionToken.getFrom().equals(localFromShort) : "from date should equal " + localFromShort + " but equals " + resumptionToken.getFrom();
		assert resumptionToken.getUntil().equals(localUntilShort) : "until date should equal " + localUntilShort + " but equals " + resumptionToken.getUntil();
        assert resumptionToken.getLastId().equals(33l) : "last id should = 333 but equals " + resumptionToken.getLastId();
        assert resumptionToken.getMetadataPrefix().equals("oai_dc") : "Should equal oai_dc but equals " + resumptionToken.getMetadataPrefix();
        assert resumptionToken.getSet().equals("123") : "set should = 123 but equals " + resumptionToken.getSet();
        assert resumptionToken.getLastSetId().equals(123l) : "last set id should equal 123 but equals " +resumptionToken.getLastSetId(); 

	
        // change the dates to long format 
		token = "set=123:128;from=" + strFromLong + 
		";until=" + strUntilLong + ";metadataPrefix=oai_dc;lastId=33;batchSize=100;deleted=false";
		resumptionToken.parseResumptionToken(token);
		assert resumptionToken.getBatchSize().equals(100) : "Batch size should equal 100 but equals " + resumptionToken.getBatchSize();
		assert resumptionToken.getFrom().equals(localFromLong) : "from date should equal " + localFromLong + " but equals " + resumptionToken.getFrom();
		assert resumptionToken.getUntil().equals(localUntilLong) : "until date should equal " + localUntilLong + " but equals " + resumptionToken.getUntil();
        assert resumptionToken.getLastId().equals(33l) : "last id should = 33 but equals " + resumptionToken.getLastId();
        assert resumptionToken.getSet().equals("123:128") : "set should = 123:128 but equals " + resumptionToken.getSet();
        assert resumptionToken.getLastSetId().equals(128l) : "last set id should equal 128 but equals " +resumptionToken.getLastSetId(); 
        
        assert resumptionToken.getMetadataPrefix().equals("oai_dc") : "Should equal oai_dc but equals " + resumptionToken.getMetadataPrefix();
        
        // make sure token is re-created correctly
        assert resumptionToken.getAsTokenString().equals(token) : "Token " + token + " should equal " + resumptionToken.getAsTokenString();
 	}

}
