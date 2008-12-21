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
	
	<input type="hidden" id="myResearcherFolders_researcherId" name="researcherId" value="${researcherId}"/>
	<input type="hidden" id="myResearcherFolders_parentFolderId" 
	                                   name="parentFolderId" 
	                                   value="${parentFolderId}"/> 
</form>

<!-- Displays the folder path -->
/<img class="tableImg" alt="" src="${pageContext.request.contextPath}/page-resources/images/all-images/folder.gif"/><ur:a href="javascript:YAHOO.ur.researcher.publications.getResearcherFolderById('0')">${user.username}</ur:a>/
   <c:forEach var="folder" items="${researcherFolderPath}">
       <img class="tableImg" alt="" src="${pageContext.request.contextPath}/page-resources/images/all-images/folder.gif"/><ur:a href="javascript:YAHOO.ur.researcher.publications.getResearcherFolderById('${folder.id}')">${folder.name}</ur:a>/
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
		<c:forEach var="fileSystemObject" items="${researcherFileSystem}">
			<tr >
				<td class="tdItemFolderSideBorder">
					 <c:if test="${fileSystemObject.fileSystemType.type == 'researcherFolder'}">
	                 	<img class="tableImg" alt="" src="${pageContext.request.contextPath}/page-resources/images/all-images/folder.gif"/> <ur:a href="javascript:YAHOO.ur.researcher.publications.getResearcherFolderById('${fileSystemObject.id}')"> <ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText> </ur:a>
	                 </c:if>
	                 
	                 <c:if test="${fileSystemObject.fileSystemType.type == 'researcherFile'}">
	                    <ir:fileTypeImg cssClass="tableImg" irFile="${fileSystemObject.irFile}"/> <ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText>
	                 </c:if>

	                 <c:if test="${fileSystemObject.fileSystemType.type == 'researcherPublication'}">
	                    <span class="packageBtnImg">&nbsp;</span><ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText>
	                 </c:if>	 

                	<c:if test="${fileSystemObject.fileSystemType.type == 'researcherLink'}">
                    	&nbsp;<img  alt=""   class="buttonImg"
		                       src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/><ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText>
                	</c:if>	
                	
                	 <c:if test="${fileSystemObject.fileSystemType.type == 'researcherInstitutionalItem'}">
	                 
	                   	&nbsp;<img  alt="" class="buttonImg"
		                       src="${pageContext.request.contextPath}/page-resources/images/all-images/package.gif"/><ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText>
	                 </c:if>	                                 
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>