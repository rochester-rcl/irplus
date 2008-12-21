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

<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<input type="hidden" id="changePasswordForm_id" name="id">


<table class="formTable">    
    <tr>       
        <td align="left" class="label">New Password:*</td>
        <td align="left" class="input"><input type="password"
         id="changePasswordForm_password" name="password" size="45"/> </td>
	</tr>
    <tr>       
        <td align="left" class="label">Confirm Password:*</td>
        <td align="left" class="input"><input type="password"
         id="changePasswordForm_password_check" name="confirmPassword" size="45"/> </td>
	</tr>	
	<tr>
	    <td align="left" class="label">Email Message:</td>
	    <td align="left" colspan="2" class="input"> <textarea 
	    	id="changePasswordForm_email_message" name="emailMessage" cols="42" rows="4">${emailMessage}</textarea>
        </td>
	</tr>
    <tr>       
        <td align="left" class="label">Email new password to user:</td>
        <td align="left" class="input"><input type="checkbox" 
        	id="changePasswordForm_send_email" name="emailPassword" value="true"/> </td>
	</tr>		
</table>
	    


