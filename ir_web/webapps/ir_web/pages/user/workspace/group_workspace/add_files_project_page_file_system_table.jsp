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

<!-- Form to select files to add to the item -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<!-- Form to store the selected file ids and item id the user is working on -->
<form name="projectPageFoldersForm" method="post">
	
	<input type="hidden" id="myProjectPageFoldersGroupWorkspaceId" name="groupWorkspaceId" 
	  value="${groupWorkspaceId}"/>
	<input type="hidden" id="myGroupWorkspaceProjectPageFoldersParentFolderId" 
	                                   name="parentFolderId" 
	                                   value="${parentFolderId}"/> 
	<input type="hidden" id="projectPageFolderFormVersionedFileId" 
	                                   name="versionedFileId" 
	                                   value=""/> 
</form>

<form name="updateProjectPageFileVersionForm" method="post">
	
	<input type="hidden" id="myProjectPageFoldersGroupWorkspaceId" name="groupWorkspaceId" 
	  value="${groupWorkspaceId}"/>
	<input type="hidden" id="updateProjectPageFileVersionFormProjectPageFileId" 
	                                   name="groupWorkspaceProjectPageFileId" 
	                                   value=""/> 
	<input type="hidden" id="updateProjectPageFileVersionFormFileVersionId" 
	                                   name="fileVersionId" 
	                                   value=""/> 
</form>


<!-- Displays the folder path -->
/<span class="folderBtnImg">&nbsp;</span><ur:a href="javascript:YAHOO.ur.group_workspace_project_page.files.getProjectPageFolderById('0')">Project Page</ur:a>/
   <c:forEach var="folder" items="${groupWorkspaceProjectPageFolderPath}">
       <span class="folderBtnImg">&nbsp;</span><ur:a href="javascript:YAHOO.ur.group_workspace_project_page.files.getProjectPageFolderById('${folder.id}')">${folder.name}</ur:a>/
   </c:forEach>
	    
<div class="clear">&nbsp;</div>  

<!-- Table for files and folders  -->            
<table class="itemFolderTable" width="100%">
	<thead>
		<tr>
			<th class="thItemFolder">Project Page File System</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="groupWorkspaceProjectPageFileSystemVersion" items="${groupWorkspaceProjectPageFileSystemVersions}">
			<tr >
				<td class="tdItemFolderSideBorder">
				
					 <c:if test="${groupWorkspaceProjectPageFileSystemVersion.fileSystem.fileSystemType.type == 'groupWorkspaceProjectPageFolder'}">
	                 	<span class="folderBtnImg"/>&nbsp;</span> <a href="javascript:YAHOO.ur.group_workspace_project_page.files.getProjectPageFolderById('${groupWorkspaceProjectPageFileSystemVersion.fileSystem.id}')"> 
	                 	  <ur:maxText numChars="50" text="${groupWorkspaceProjectPageFileSystemVersion.fileSystem.name}"></ur:maxText> </a>
	                 </c:if>
	                 
	                 <c:if test="${groupWorkspaceProjectPageFileSystemVersion.fileSystem.fileSystemType.type == 'groupWorkspaceProjectPageFile'}">
	                    <ir:fileTypeImg cssClass="tableImg" irFile="${groupWorkspaceProjectPageFileSystemVersion.fileSystem.irFile}"/> 
	                       <ur:maxText numChars="50" text="${groupWorkspaceProjectPageFileSystemVersion.fileSystem.name}"></ur:maxText>

				      	   <select id="file_version" name="version_${groupWorkspaceProjectPageFileSystemVersion.fileSystem.id}" 
				      	            onChange="javascript:YAHOO.ur.group_workspace_project_page.files.changeFileVersion(this, '${groupWorkspaceProjectPageFileSystemVersion.fileSystem.id}')" /> 
				      	   
					      		<c:forEach var="version" items="${groupWorkspaceProjectPageFileSystemVersion.versions}" >
					      			<option value = "${version.id}"
					      			<c:if test="${groupWorkspaceProjectPageFileSystemVersion.fileSystem.irFile.id == version.irFile.id}">
					      				selected
					      			</c:if>  
					      			> ${version.versionNumber}</option>
					      		</c:forEach>
				      	   </select> 	                    
	                 </c:if>
	                 
	                 <c:if test="${groupWorkspaceProjectPageFileSystemVersion.fileSystem.fileSystemType.type == 'groupWorkspaceProjectPagePublication'}">
	                    <span class="scriptImg">&nbsp;</span><ur:maxText numChars="50" text="${groupWorkspaceProjectPageFileSystemVersion.fFileSystem.name}"></ur:maxText>
	                 </c:if>
	                 
	                 <c:if test="${groupWorkspaceProjectPageFileSystemVersion.fileSystem.fileSystemType.type == 'groupWorkspaceProjectPageFileSystemLink'}">
	                   	&nbsp;<img  alt="" class="buttonImg"
		                       src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/>
		                       <ur:maxText numChars="50" text="${groupWorkspaceProjectPageFileSystemVersion.fileSystem.name}"></ur:maxText>
	                 </c:if>	

	                 <c:if test="${groupWorkspaceProjectPageFileSystemVersion.fileSystem.fileSystemType.type == 'groupWorkspaceProjectPageInstitutionalItem'}">
	                 
	                   	&nbsp;<span class="packageBtnImg">&nbsp;</span><ur:maxText numChars="50" text="${groupWorkspaceProjectPageFileSystemVersion.fileSystem.name}"></ur:maxText>
	                 </c:if>	                 
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>