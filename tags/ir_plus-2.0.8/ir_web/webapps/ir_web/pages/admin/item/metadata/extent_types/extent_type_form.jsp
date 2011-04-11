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

<%@ taglib prefix="ir" uri="ir-tags"%>

<input type="hidden" id="newExtentTypeForm_id" name="id" value="${extentType.id}"/>
		               
<input type="hidden" id="newExtentType_new" name="newExtentType" value="true"/>
		              
<div id="extentTypeError" class="errorMessage"></div>
			            
<table class="formTable">    
    <tr>
	    <td align="left" class="label">Name:</td>
		<td align="left" class="input"><input type="text" 
		    id="newExtentTypeForm_name" size="45" 
			name="extentType.name" value="${extentType.name}"/> </td>
	</tr>
	<tr>
	    <td align="left" class="label">Description:</td>
		<td colspan="2" align="left" class="input"><textarea 
		    id="newExtentTypeForm_description"
			name="extentType.description" cols="42" rows="4">${extentType.description}</textarea></td>
	</tr>
</table>