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

<div id="group_workspace_rename_error_div">       
    <!--  get the error messages from fieldErrors -->
    <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
       key="renameFileMessage"/></p>
    <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
        key="illegalNameError"/></p> 
</div>

<!--  represents a successful submission -->
<input type="hidden" id="group_workspace_renameForm_success" 
		   value="${fileRenamed}"/>

<!--  represents a successful submission -->
<input type="hidden" id="renameGroupWorkspaceForm_success" 
		   value="${fileRenamed}"/>

<input type="hidden" name="groupWorkspaceFileId"  value="${groupWorkspaceFileId}"/>
<table class="formTable">
    <tr>
        <td>
            New File Name: 
        </td>
        <td>
            <input type="text" name="newFileName"  size="60" value="${newFileName}"/> 
        </td>
    </tr>
    <tr>
        <td>
            Description: 
        </td>
       
        <td align="left" class="input"><textarea 
             name="fileDescription"  cols="60" rows="2">${fileDescription}</textarea>
        </td>
    </tr>
</table>

       
