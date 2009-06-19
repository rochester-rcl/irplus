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
<form name="myResearcherFolders" method="post">
	
	<input type="hidden" id="myResearcherFolders_parentFolderId" 
	                                   name="parentFolderId" 
	                                   value="${parentFolderId}"/> 
</form>

<!-- Displays the folder path -->
/<span class="folderBtnImg">&nbsp;</span><ur:a href="javascript:YAHOO.ur.researcher.files.getResearcherFolderById('0')">My Research</ur:a>/
   <c:forEach var="folder" items="${researcherFolderPath}">
       <span class="folderBtnImg">&nbsp;</span><ur:a href="javascript:YAHOO.ur.researcher.files.getResearcherFolderById('${folder.id}')">${folder.name}</ur:a>/
   </c:forEach>
	    
<div class="clear">&nbsp;</div>      

<!-- Table for files and folders  -->            
<table class="itemFolderTable" width="100%">
	<thead>
		<tr>
			<th class="thItemFolder">Researcher Files and Folders</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="researcherFileSystemVersion" items="${researcherFileSystemVersions}">
			<tr >
				<td class="tdItemFolderSideBorder">
				
					 <c:if test="${researcherFileSystemVersion.researcherFileSystem.fileSystemType.type == 'researcherFolder'}">
	                 	<span class="folderBtnImg">&nbsp;</span> <ur:a href="javascript:YAHOO.ur.researcher.files.getResearcherFolderById('${researcherFileSystemVersion.researcherFileSystem.id}')"> <ur:maxText numChars="50" text="${researcherFileSystemVersion.researcherFileSystem.name}"></ur:maxText> </ur:a>
	                 </c:if>
	                 
	                 <c:if test="${researcherFileSystemVersion.researcherFileSystem.fileSystemType.type == 'researcherFile'}">
	                    <ir:fileTypeImg cssClass="tableImg" irFile="${researcherFileSystemVersion.researcherFileSystem.irFile}"/> <ur:maxText numChars="50" text="${researcherFileSystemVersion.researcherFileSystem.name}"> - this one</ur:maxText>

				      	   <select id="file_version" name="version_${researcherFileSystemVersion.researcherFileSystem.id}" onChange="javascript:YAHOO.ur.researcher.files.changeFileVersion(this, '${researcherFileSystemVersion.researcherFileSystem.id}');" /> 
				      	   
					      		<c:forEach var="version" items="${researcherFileSystemVersion.versions}" >
					      			<option value = "${version.id}"
					      			<c:if test="${researcherFileSystemVersion.researcherFileSystem.irFile.id == version.irFile.id}">
					      				selected
					      			</c:if>  
					      			> ${version.versionNumber}</option>
					      		</c:forEach>
				      	   </select> 	                    
	                 </c:if>
	                 
	                 <c:if test="${researcherFileSystemVersion.researcherFileSystem.fileSystemType.type == 'researcherPublication'}">
	                    <span class="packageBtnImg">&nbsp;</span><ur:maxText numChars="50" text="${researcherFileSystemVersion.researcherFileSystem.name}"></ur:maxText>
	                 </c:if>
	                 
	                 <c:if test="${researcherFileSystemVersion.researcherFileSystem.fileSystemType.type == 'researcherLink'}">
	                   	&nbsp;<img  alt="" class="buttonImg"
		                       src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/><ur:maxText numChars="50" text="${researcherFileSystemVersion.researcherFileSystem.name}"></ur:maxText>
	                 </c:if>	

	                 <c:if test="${researcherFileSystemVersion.researcherFileSystem.fileSystemType.type == 'researcherInstitutionalItem'}">
	                 
	                   	&nbsp;<span class="packageBtnImg">&nbsp;</span><ur:maxText numChars="50" text="${researcherFileSystemVersion.researcherFileSystem.name}"></ur:maxText>
	                 </c:if>	                 
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>