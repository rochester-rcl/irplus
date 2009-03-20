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
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.handle.HandleService;


import net.handle.hdllib.Encoder;
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
		log.debug("checkpoint database called - NOT IMPLEMENTED");
	}

	/**
	 * Not implemented at this time.
	 * 
	 * @see net.handle.hdllib.HandleStorage#createHandle(byte[], net.handle.hdllib.HandleValue[])
	 */
	public void createHandle(byte[] arg0, HandleValue[] arg1)
			throws HandleException {
		log.debug("create handle called - NOT IMPLEMENTED");
	}

	/**
	 * Not implemented at this time
	 * @see net.handle.hdllib.HandleStorage#deleteAllRecords()
	 */
	public void deleteAllRecords() throws HandleException {
		log.debug("delete all records called - NOT IMPLEMENTED");
	}

	public boolean deleteHandle(byte[] arg0) throws HandleException {
		log.debug("delete all handle called - NOT IMPLEMENTED");
		return false;
	}

	public Enumeration getHandlesForNA(byte[] namingAuthorityBytes) throws HandleException {
		log.debug("Get handles for na called ");
		String namingAuthority = Util.decodeString(namingAuthorityBytes);
		log.debug("Naming authority = " + namingAuthority);
		
		return null;
	}

	/**
	 * This is the irplus implemenation.  This implementation assumes there is only
	 * one handle value per naming authority - there is no use of the index value - which
	 * allows multiple records for a single handle value.
	 * 
	 * @param handle
	 * @param indexList
	 * @param typeList
	 * @return
	 * @throws HandleException
	 */
	public byte[][] getRawHandleValues(byte[] handle, int[] indexList, byte[][] typeList)
			throws HandleException {

        String handleName = Util.decodeString(handle);
        log.debug("get raw handle values for handle " + handleName);
        
        // irplus handle system information
        HandleInfo handleInfo = handleService.getHandleInfo(handleName);
        log.debug("handleInfo = " + handleInfo);
        byte rawValues[][] = null;
       
        if( handleInfo != null )
        {
            // holds all the handle values
            List<HandleValue> values = new LinkedList<HandleValue>();
        
            // cni handle value
             HandleValue value = new HandleValue();
        
            // transfer the data
             value.setIndex(handleInfo.getIndex());
             value.setType(Util.encodeString(handleInfo.getDataType()));
             value.setData(Util.encodeString(handleInfo.getData()));
            
             if( handleInfo.getTimeToLiveType() != null )
             {
                 value.setTTLType(handleInfo.getTimeToLiveType().byteValue());
             }
             
             if( handleInfo.getTimeToLive() != null )
             {
                 value.setTTL(handleInfo.getTimeToLive().intValue());
             }
             else
             {
            	 value.setTTL(100);
             }
             
             if( handleInfo.getTimestamp() != null )
             {
                 value.setTimestamp(handleInfo.getTimestamp().intValue());
             }
             else
             {
            	 value.setTimestamp(100);
             }
             value.setReferences(null);
             value.setAdminCanRead(handleInfo.isAdminRead());
             value.setAdminCanWrite(handleInfo.isAdminWrite());
             value.setAnyoneCanRead(handleInfo.isPublicRead());
             value.setAnyoneCanWrite(handleInfo.isPublicWrite());
             
             
             // only adding one here
             values.add(value);
             
             // encode the value
             if(values.size() > 0)
             {
            	 // create a 2-D byte array of the proper size
                 rawValues = new byte[values.size()][];
                 int counter = 0;
                 for(HandleValue hv : values)
                 {
                     rawValues[counter] = new byte[Encoder.calcStorageSize(hv)];
                     Encoder.encodeHandleValue(rawValues[counter], 0, hv);
                     counter = counter++;
                 }
             }
        }
       
		return rawValues;
	}

	public boolean haveNA(byte[] naBytes) throws HandleException {
		
		boolean haveNa = false;
		String namingAuthority = Util.decodeString(naBytes);
		log.debug("checking to see if we have naming authority : " + namingAuthority);
		String[] namingAuthorityParts = namingAuthority.split("/");
		
		if( namingAuthorityParts.length < 2 )
		{
			// false
		}
		else
		{
			String prefix = namingAuthorityParts[1];
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
		log.debug("returning haveNa = " + haveNa);
		return haveNa;
	
	}

	public void init(StreamTable arg0) throws Exception {
	    log.debug("init called - initializing spring context");
		/** Application context for loading bean specific information */
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		log.debug("loading handle service ");
		handleService = (HandleService)ctx.getBean("handleService");
		
		if( handleService == null )
		{
			throw new RuntimeException("handle service not found");
		}
	}

	public void scanHandles(ScanCallback arg0) throws HandleException {
		log.debug("scan handles called - NOT IMPLEMENTED");
		
	}

	public void scanNAs(ScanCallback arg0) throws HandleException {
		log.debug("scanNAs called - NOT IMPLEMENTED");
		
	}

	public void setHaveNA(byte[] arg0, boolean arg1) throws HandleException {
		log.debug("set have na called - NOT IMPLEMENTED");
	}

	public void shutdown() {
		log.debug("shutdown called - NOT IMPLEMENTED");
	}

	public void updateValue(byte[] arg0, HandleValue[] arg1)
			throws HandleException {
		log.debug("update value called - NOT IMPLEMENTED");
	}
	
	public HandleService getHandleService() {
		return handleService;
	}

	public void setHandleService(HandleService handleService) {
		this.handleService = handleService;
	}
	
}
