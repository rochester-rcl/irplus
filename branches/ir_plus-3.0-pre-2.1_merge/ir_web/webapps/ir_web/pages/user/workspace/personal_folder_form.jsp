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

<!--  represents a successful submission -->
<input type="hidden" id="newFolderForm_success" 
		   value="${folderAdded}"/>
		   		               
<input type="hidden" id="newFolderForm_new"
       name="newFolder" value="true"/>
      
<input type="hidden" id="newfolderForm_folderId"
    name="updateFolderId" value="${updateFolderId}"/>

<input type="hidden" id="newfolderForm_workspaceId"
    name="groupWorkspaceId" value="${groupWorkspaceId}"/>

<div id="folder_error_div">       
    <!--  get the error messages from fieldErrors -->
    <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
       key="personalFolderAlreadyExists"/></p>
    <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
        key="illegalPersonalFolderName"/></p> 
</div>
          
 <table class="formTable">
     <tr>
         <td class="label"> Folder Name:</td>
         <td align="left" class="input"> 
             <input id="folder" size="50" type="text" name="folderName" value="${folderName}"/>
         </td>
     </tr>
     <tr>
         <td class="label"> Folder Description:</td>
         <td align="left" class="input"><textarea cols="45" rows="3" name="folderDescription">${folderDescription}</textarea></td>
     </tr>
 </table>