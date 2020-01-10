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

<!--  form fragment for dealing with editing identifier types
      this form will return with error messages in it if there
      is an issue.
 -->		              
<input type="hidden" id="newPersonNameAuthorityIdentifierTypeForm_id" name="id" value="${personNameAuthorityIdentifierType.id}"/>
		               
<input type="hidden" id="newPersonNameAuthorityIdentifierType_new" name="newPersonNameAuthorityIdentifierType" value="true"/>
		              
<div id="personNameAuthorityIdentifierTypeError" class="errorMessage"></div>
			            
<table class="formTable">    
    <tr>
	    <td align="left" class="label">Name:</td>
		<td align="left" class="input"><input type="text" 
			 id="newPersonNameAuthorityIdentifierTypeForm_name" size="45" 
			 name="personNameAuthorityIdentifierType.name" value="${personNameAuthorityIdentifierType.name}"/> </td>
	</tr>
	<tr>
	    <td align="left" class="label">Description:</td>
		<td colspan="2" align="left" class="input"><textarea 
		    id="newPersonNameAuthorityIdentifierTypeForm_description"
			name="personNameAuthorityIdentifierType.description" cols="42" rows="4">${personNameAuthorityIdentifierType.description}</textarea></td>
	</tr>
</table>
