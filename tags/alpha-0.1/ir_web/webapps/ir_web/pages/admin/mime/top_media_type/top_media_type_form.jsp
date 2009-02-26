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

<!--  form fragment for dealing with editing MIME types
      this form will return with error messages in it if there
      is an issue.
 -->
 
<%@ taglib prefix="ir" uri="ir-tags"%>

    <!--  represents a successful submission -->
	<input type="hidden" id="newTopMediaTypeForm_success" 
		   value="${added}"/>
		   
    <input type="hidden" id="newTopMediaTypeForm_id"
         name="id" value="${topMediaType.id}""/>
		               
    <input type="hidden" id="newTopMediaTypeForm_new"
		   name="newTopMediaType" value="true"/>
		              
	<ur:div id="topMediaTypeError" cssClass="errorMessage"></ur:div>
	
	<!--  get the error messages from fieldErrors -->
	<p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		key="topMediaType.name"/></p>
	<p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		key="topMediaTypeAlreadyExists"/></p> 

    <table class="formTable">    
		    <tr>
		        <td align="left" class="label">Name:*</td>
		        <td align="left" class="input"><input type="text" 
	                       id="newTopMediaTypeForm_name" 
		                   name="topMediaType.name"
		                   size="55" 
		                   value="${topMediaType.name}"/>
		        </td>
		    </tr>
		    <tr>
			    <td align="left" class="label">Description:</td>
			    <td align="left" colspan="2" class="input"> <textarea name="topMediaType.description"
	                id="newTopMediaTypeForm_description"  cols="50" rows="8">${topMediaType.description}</textarea>
	            </td>
			</tr>
    </table>          		          