<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />


<!--  
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
-->

<!--  form fragment for dealing with editing emails
      this form will return with error messages in it if there
      is an issue.
 -->
<%@ taglib prefix="ir" uri="ir-tags"%>

		<!--  represents a successful submission -->
		<input type="hidden" id="newEmailForm_success" 
		       value="${added}"/>
		        
		<!--  User id must be passed -->     
	    <input type="hidden" id="newEmailForm_emailId"
		        name="emailId" value="${email.id}"/>
		               
	    <input type="hidden" id="newEmailForm_new"
		        name="newEmail" value="true"/>

	    <input type="hidden" id="newEmailForm_oldEmail"
		        name="oldEmail" value="${email.email}"/>
		        
		<input type="hidden" id="newEmailForm_message"
		        value="${emailVerificationMessage}"/>		       
		        		              
	    <!--  get the error messages from fieldErrors -->

        <div id="email_create_error">
		<p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		key="emailExistError"/></p>  
		</div>

 		<table class="formTable">    
		    <tr>       
	            <td align="left" class="label">Email Id:*</td>
	            <td align="left" class="input"><input type="text" 
			    id="newEmailForm_email" 
			    name="email.email" 
			    value="${email.email}" size="45"/> </td>
			</tr>
	    </table>
	    
