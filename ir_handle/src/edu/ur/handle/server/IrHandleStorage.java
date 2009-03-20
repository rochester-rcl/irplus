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

package edu.ur.handle.server;

import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.ur.ir.handle.HandleService;


import net.handle.hdllib.HandleException;
import net.handle.hdllib.HandleStorage;
import net.handle.hdllib.HandleValue;
import net.handle.hdllib.ScanCallback;
import net.handle.util.StreamTable;

import net.handle.hdllib.Util;

/**
 * Implemenation of handle storage for the irplus system.
 * 
 * @author Nathan Sarr
 *
 */
public class IrHandleStorage implements HandleStorage{

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(IrHandleStorage.class);
	
	/** service for dealing with handles */
	private HandleService handleService;
	
	/** Spring application context */
	private ApplicationContext ctx;

	/**
	 * Not implemented at this time.
	 * 
	 * @see net.handle.hdllib.HandleStorage#checkpointDatabase()
	 */
	public void checkpointDatabase() throws HandleException {
		System.out.println("checkpoint database called - NOT IMPLEMENTED");
		log.debug("checkpoint database called - NOT IMPLEMENTED");
	}

	/**
	 * Not implemented at this time.
	 * 
	 * @see net.handle.hdllib.HandleStorage#createHandle(byte[], net.handle.hdllib.HandleValue[])
	 */
	public void createHandle(byte[] arg0, HandleValue[] arg1)
			throws HandleException {
		System.out.println("create handle called - NOT IMPLEMENTED");
		log.debug("create handle called - NOT IMPLEMENTED");
	}

	/**
	 * Not implemented at this time
	 * @see net.handle.hdllib.HandleStorage#deleteAllRecords()
	 */
	public void deleteAllRecords() throws HandleException {
		System.out.println("delete all records called - NOT IMPLEMENTED");
		log.debug("delete all records called - NOT IMPLEMENTED");
	}

	public boolean deleteHandle(byte[] arg0) throws HandleException {
		System.out.println("delete all handle called - NOT IMPLEMENTED");
		log.debug("delete all handle called - NOT IMPLEMENTED");
		return false;
	}

	public Enumeration getHandlesForNA(byte[] namingAuthorityBytes) throws HandleException {
		log.debug("Get handles for na called ");
		System.out.println("Get handles for na called ");
		String namingAuthority = Util.decodeString(namingAuthorityBytes);
		log.debug("Naming authority = " + namingAuthority);
		System.out.println("Naming authority = " + namingAuthority);
		
		return null;
	}

	public byte[][] getRawHandleValues(byte[] handle, int[] indexList, byte[][] typeList)
			throws HandleException {

        String handleName = Util.decodeString(handle);
       
        System.out.println("get raw handle values for handle " + handleName);
        log.debug("get raw handle values for handle " + handleName);
		return null;
	}

	public boolean haveNA(byte[] naBytes) throws HandleException {
		
		boolean haveNa = false;
		String namingAuthority = Util.decodeString(naBytes);
		System.out.println("checking to see if we have naming authority : " + namingAuthority);
		log.debug("checking to see if we have naming authority : " + namingAuthority);
		String[] namingAuthorityParts = namingAuthority.split("/");
		
		if( namingAuthorityParts.length < 2 )
		{
			// false
		}
		else
		{
			String prefix = namingAuthorityParts[1];
			System.out.println("Checing for prefix " + prefix);
			log.debug("Checing for prefix " + prefix);
			if( handleService.getNameAuthority(prefix) != null )
			{
			    haveNa = true;	
			}
			else
			{
				// false
			}
		}
		System.out.println("returning haveNa = " + haveNa);
		log.debug("returning haveNa = " + haveNa);
		return haveNa;
		
		
		
	}

	public void init(StreamTable arg0) throws Exception {
	    log.debug("init called - initializing spring context");
	    System.out.println("init called - initializing spring context");
		/** Application context for loading bean specific information */
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		log.debug("loading handle service ");
		System.out.println("loading handle service ");
		handleService = (HandleService)ctx.getBean("handleService");
		
		if( handleService == null )
		{
			throw new RuntimeException("handle service not found");
		}
	}

	public void scanHandles(ScanCallback arg0) throws HandleException {
		log.debug("scan handles called - NOT IMPLEMENTED");
		System.out.println("scan handles called - NOT IMPLEMENTED");
		
	}

	public void scanNAs(ScanCallback arg0) throws HandleException {
		log.debug("scanNAs called - NOT IMPLEMENTED");
		System.out.println("scanNAs called - NOT IMPLEMENTED");
		
	}

	public void setHaveNA(byte[] arg0, boolean arg1) throws HandleException {
		log.debug("set have na called - NOT IMPLEMENTED");
		System.out.println("set have na called - NOT IMPLEMENTED");
	}

	public void shutdown() {
		log.debug("shutdown called - NOT IMPLEMENTED");
		System.out.println("shutdown called - NOT IMPLEMENTED");
	}

	public void updateValue(byte[] arg0, HandleValue[] arg1)
			throws HandleException {
		log.debug("update value called - NOT IMPLEMENTED");
		System.out.println("update value called - NOT IMPLEMENTED");
	}
	
	public HandleService getHandleService() {
		return handleService;
	}

	public void setHandleService(HandleService handleService) {
		this.handleService = handleService;
	}


}
