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

<!--  form fragment for dealing with editing ipaddress
      this form will return with error messages in it if there
      is an issue.
 -->
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

		<!--  represents a successful submission -->
		<input type="hidden" id="deleteIgnoreIpAddressForm_success" 
		       value="${added}"/>
	
	    <table class="formTable">    
			<tr>
			    <td align="left" class="label">Delete Ignore IP Address Range:</td>
			    <td align="left" colspan="2" class="input"> 
				    From
				    <input type="text" 
				    id="deleteIgnoreIpAddressForm_fromAddress1" 
				    name="fromAddress1" 
				    value="${fromAddress1}" size="4" maxlength="3"/> 
				  
				    <input type="text" 
				    id="deleteIgnoreIpAddressForm_fromAddress2" 
				    name="fromAddress2" 
				    value="${fromAddress2}" size="4" maxlength="3"/> 
				   
				    <input type="text" 
				    id="deleteIgnoreIpAddressForm_fromAddress3" 
				    name="fromAddress3" 
				    value="${fromAddress3}" size="4" maxlength="3"/> 
				   
				    <input type="text" 
				    id="deleteIgnoreIpAddressForm_fromAddress4" 
				    name="fromAddress4" 
				    value="${fromAddress4}" size="4" maxlength="3"/> 
				    
				    To 
				    <input type="text" 
				    id="deleteIgnoreIpAddressForm_toAddress4" 
				    name="toAddress4" 
				    value="${toAddress4}" size="4"  maxlength="3"/> 
			    </td>
			</tr>			
	    </table>

	   
