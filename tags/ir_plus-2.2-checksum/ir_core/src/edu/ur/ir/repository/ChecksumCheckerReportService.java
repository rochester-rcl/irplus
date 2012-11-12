/**  
   Copyright 2012 University of Rochester

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

package edu.ur.ir.repository;

import javax.mail.MessagingException;

/**
 * Service to help with the reporting of the checksum checker 
 * 
 * @author NathanS
 *
 */
public interface ChecksumCheckerReportService {

	/**
	 * Send an email for reporting what the checksum checker has found.
	 * 
	 * @throws MessagingException - if a messaging exception occurs
	 */
	public void sendChecksumReportEmail() throws MessagingException; 

}
