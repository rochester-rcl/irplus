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

<!--  form fragment for dealing with editing permissions
      this form will return with error messages in it if there
      is an issue.
 -->
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	
	<!--  represents a successful submission -->
	<input type="hidden" id="editPermissionseForm_success" value="${added}"/>

	<input type="hidden" id="editAutoShareId" name="folderAutoShareInfoId" value="${folderAutoShareInfo.id}"/>
  
  	    <!--  get the error messages from fieldErrors -->
		<p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		key="emptyPermissions"/></p>         
  
  <table>
  <c:forEach var="classTypePermission" items="${classTypePermissions}">
  <tr>
          <td>
         
      <input type="checkbox" name="selectedPermissions" id="${classTypePermission.name}" value="${classTypePermission.id}" 
          onclick="YAHOO.ur.auto_share.autoCheckPermission(this, selectedPermissions);"
      
      <c:forEach var="collaboratorPermission" items="${collaboratorPermissions}">
       
		   <c:if test="${classTypePermission.name == collaboratorPermission.name}">
		   		checked="true"
			</c:if>
		
		</c:forEach>      
       />
        </td>
        <td align="left">
        
        <p class="p_indent">${classTypePermission.description}</p>
        
      </td>
  </tr>
 
  </c:forEach>
 
  </table>
  <p> <input type="checkbox" name="includeSubFiles" checked="checked" value="true"/> Apply permission changes to current folders files</p>
  <p> <input type="checkbox" name="includeSubFolders" checked="checked" value="true"/> Apply changes to sub-folders and files</p>
