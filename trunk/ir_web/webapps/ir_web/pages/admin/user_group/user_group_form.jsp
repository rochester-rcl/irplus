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

<!--  form fragment for dealing with editing content types
      this form will return with error messages in it if there
      is an issue.
 -->
<%@ taglib prefix="ir" uri="ir-tags"%>

		<!--  represents a successful submission -->
		<input type="hidden" id="newUserGroupForm_success" 
		       value="${added}"/>
		       
		<!--  if editing an id must be passed -->     
	    <input type="hidden" id="newUserGroupForm_id"
		        name="id" value=""/>
		               
		<div id="userGroupError">            
	        <!--  get the error messages from fieldErrors -->
		    <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		    key="userGroupAlreadyExists"/></p>
		</div>   
		
		<table class="formTable">    
		    <tr>
			    <td align="left" class="label"> Name:</td>
			    <td align="left" class="input"><input type="text" size="50"
			    id="newUserGroupForm_name" 
			    name="userGroup.name" 
			    value="${userGroup.name}"/>
			    </td>      
	        </tr>
	        <tr>
	            <td align="left" class="label">Description:</td>
	       
	            <td colspan="2" class="input"><textarea name="userGroup.description" 
	                  id="newUserGroupForm_description" 
	                  cols="50" rows="4">${userGroup.description}</textarea></td>
	        </tr>
	    </table>
	   
