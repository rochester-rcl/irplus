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

<!--  form fragment for dealing with editing content types
      this form will return with error messages in it if there
      is an issue.
 -->

<input type="hidden" id="externalAccountTypeForm_id" name="id" value="${externalAccountType.id}"/>
		               
<input type="hidden" id="newExternalAccountType_new" name="newExternalAccountType" value="true"/>
<input type="hidden" id="newExternalAccountType_success" name="added" value="${added}"/>
		              
<div id="error" class="errorMessage">${message}</div>
<table class="formTable">    
    <tr>       
	    <td align="left" class="label">Name:*</td>
		<td align="left" class="input"><input type="text" 
		id="externalAccountType_name" name="externalAccountType.name"
		value="${externalAccountType.name}" size="45"/> </td>
	</tr>
	<tr>
	    <td align="left" class="label">Description:</td>
		<td align="left" colspan="2" class="input"> <textarea id="newExternalAccountTypeForm_description"
			name="externalAccountType.description" cols="42" rows="4">${externalAccountType.description}</textarea>
		</td>
	</tr>
</table>