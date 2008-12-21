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

	<input type="hidden" id="fileCollaboratorId" name="fileCollaboratorId" value="${fileCollaboratorId}"/>
  
  	    <!--  get the error messages from fieldErrors -->
		<p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		key="emptyPermissions"/></p>         
  
  <c:forEach var="classTypePermission" items="${classTypePermissions}">
      <input type="checkbox" name="selectedPermissions" id="${classTypePermission.name}" value="${classTypePermission.id}" 
          onclick="YAHOO.ur.invite.autoCheckPermission(this, selectedPermissions);"
      
      <c:forEach var="collaboratorPermission" items="${collaboratorPermissions}">
       
		   <c:if test="${classTypePermission.name == collaboratorPermission.name}">
		   		checked="true"
			</c:if>
		
		</c:forEach>      
       />
      ${classTypePermission.description}
	  <div class="clear">&nbsp;</div>

  </c:forEach>
