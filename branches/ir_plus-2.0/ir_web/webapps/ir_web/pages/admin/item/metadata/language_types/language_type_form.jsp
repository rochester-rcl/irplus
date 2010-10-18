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
<%@ taglib prefix="ir" uri="ir-tags"%>

<input type="hidden" id="newLanguageTypeForm_languageTypeId" name="id" value="${languageType.id}"/>
		               
<input type="hidden" id="newLanguageType_new" name="newLanguageType" value="true"/>
		              
<div id="languageTypeError" class="errorMessage"></div>
<table class="formTable"> 
    <tr>
	    <td align="left" class="label">Name:*</td>
		<td align="left" class="input"><input type="text" 
			id="newLanguageTypeForm_name" size="45" 
			name="languageType.name" value="${languageType.name}"/></td>
    </tr>
    <tr>
        <td align="left" class="label">ISO 639-2</td>
		<td align="left" class="input"><input type="text" 
			id="newLanguageTypeForm_639_2" size="3" maxlength="3"
			name="languageType.iso639_2" value="${languageType.iso639_2}"/></td>
    </tr>
    <tr>
        <td align="left" class="label">ISO 639-1</td>
		<td align="left" class="input"><input type="text" 
			id="newLanguageTypeForm_639_1" size="2"  maxlength="2"
			name="languageType.iso639_1" value="${languageType.iso639_1}"/></td>
    </tr>
    
	<tr>
	    <td align="left" class="label">Description:</td>
		<td colspan="2" align="left" class="input"><textarea 
		    id="newLanguageTypeForm_description"
			name="languageType.description" cols="42" rows="4">${languageType.description}</textarea></td>
	</tr>
</table>