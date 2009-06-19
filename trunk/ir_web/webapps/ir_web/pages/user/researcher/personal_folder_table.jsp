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

<!-- Displays the files and folders in a table.
Displayed on the left hand side of the add files to item page -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<!-- Form to store - item the user is working on, current parent folder and the selected file ids -->
<form name="myPersonalFolders" method="post">
	<input type="hidden" id="myFolders_parentPersonalFolderId" 
		name="parentPersonalFolderId" value="${parentPersonalFolderId}"/>
	<input type="hidden" id="myFolders_researcherId" 
	    name="researcherId" value="${researcherId}"/>
    <input type="hidden" id="myFolders_parentFolderId" 
           name="parentFolderId" value="${parentFolderId}"/>
</form>

<!-- Displays the folder path -->
/<span class="folderBtnImg">&nbsp;</span><ur:a href="javascript:YAHOO.ur.researcher.files.getPersonalFolderById('0')">My Files</ur:a>/
   <c:forEach var="folder" items="${personalFolderPath}">
       <span class="folderBtnImg">&nbsp;</span><ur:a href="javascript:YAHOO.ur.researcher.files.getPersonalFolderById('${folder.id}')">${folder.name}</ur:a>/
   </c:forEach>
	    
<div class="clear">&nbsp;</div>      

<!-- Table for files and folders  -->            
<table class="itemFolderTable" width="100%">
	<thead>
		<tr>
			<th class="thItemFolder" width="20%">Add</th>
			<th class="thItemFolder">Personal Files and Folders</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="fileSystemObject" items="${personalFileSystem}">
			<tr >
				<td class="tdItemFolderLeftBorder">
				    <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
	                    <span class="addBtnImg">&nbsp;</span><a href="javascript:YAHOO.ur.researcher.files.addFile('${fileSystemObject.versionedFile.id}');"> Add</a> 
				     </c:if>
 				</td>
				
				<td class="tdItemFolderRightBorder">
					 <c:if test="${fileSystemObject.fileSystemType.type == 'personalFolder'}">
	                 	<span class="folderBtnImg">&nbsp;</span> <ur:a href="javascript:YAHOO.ur.researcher.files.getPersonalFolderById('${fileSystemObject.id}')"> <ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText></ur:a>
	                 </c:if>
	                 
	                 <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
	                    <ir:fileTypeImg cssClass="tableImg" versionedFile="${fileSystemObject.versionedFile}"/> <ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText>
	                 </c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
