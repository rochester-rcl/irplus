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
  
<!-- Form to enter the new password  --> 

<%@ taglib prefix="ir" uri="ir-tags"%>

<div id="net_id_error">           
    <!--  get the error messages from fieldErrors -->
	<p class="errorMessage"><ir:printError errors="${fieldErrors}" 
			key="netIdAlreadyExists"/></p>
	<p class="errorMessage"><ir:printError errors="${fieldErrors}" 
			key="netIdPasswordFail"/></p> 
	<p class="errorMessage"><ir:printError errors="${fieldErrors}" 
			key="netIdPasswordEmpty"/></p>      
</div>

<input type="hidden" id="net_id_added" value="${added}"/>

<table class="formTable"> 

    
     <tr>
         <td class="label">Net Id *:</td>
         <td align="left" class="input"> 
             <input type="text" id="net_id" name="netId"  value="${irUser.ldapUserName}"/>
         </td>
     </tr>
     <tr>
         <td class="label">Net Id password*:</td>
         <td align="left" class="input"> 
              <input type="password"  id="net_id_password"  name="netIdPassword"  value=""/>
         </td>
     </tr>
 
</table>
