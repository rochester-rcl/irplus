
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

<!--  form fragment for dealing with editing MIME sub types
      this form will return with error messages in it if there
      is an issue.
 -->
 
<%@ taglib prefix="ir" uri="ir-tags"%>

    <!--  represents a successful submission -->
	<input type="hidden" id="newSubTypeForm_success" 
		   value="${added}"/>
		   
    <input type="hidden" id="newSubTypeForm_id"
         name="id" value="${id}"/>
         
    <input type="hidden" id="newSubTypeForm_subTypeId"
         name="subTypeId" value="${subTypeId}"/>
		               
    <input type="hidden" id="newSubTypeForm_new"
		   name="newSubType" value="true"/>
		              
	<ur:div id="SubTypeError" cssClass="errorMessage">
	
	    <!--  get the error messages from fieldErrors -->
	    <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		    key="subType.name"/></p>
	    <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		    key="subTypeAlreadyExists"/></p> 
	</ur:div>

    <table class="formTable">    
		    <tr>
		        <td align="left" class="label">Name:*</td>
		        <td align="left" class="input"><input type="text" 
	                       id="newSubTypeForm_name" 
		                   name="name" 
		                    value="${name}"/>
		        </td>
		    </tr>
		    <tr>
			    <td align="left" class="label">Description:</td>
			    <td align="left" colspan="2" class="input"> <textarea name="description"
	                id="newSubTypeForm_description"  cols="42" rows="4">${description}</textarea>
	            </td>
			</tr>
    </table>     
	