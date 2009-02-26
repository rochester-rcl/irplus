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
<form name="myFolders" method="post">
	<input type="hidden" id="myFolders_itemId" name="genericItemId" value="${item.id}"/>
	<input type="hidden" id="myFolders_parentFolderId" 
	                                   name="parentFolderId" 
	                                   value="${parentFolderId}"/>
	<input type="hidden" id="myFolders_parentCollectionId" 
	                                   name="parentCollectionId" 
	                                   value="${parentCollectionId}"/>	
    <input type="hidden" id="myFolders_institutionalItemId" 
	                                   name="institutionalItemId" 
	                                   value="${institutionalItemId}"/>
	                                                                      
	                                   
</form>

<!-- Displays the folder path -->
<div class="button_height">
/<span class="folderBtnImg">&nbsp;</span><ur:a href="javascript:YAHOO.ur.item.getFolderById('0')">${user.username}</ur:a>/
   <c:forEach var="folder" items="${folderPath}">
       <span class="folderBtnImg">&nbsp;</span><ur:a href="javascript:YAHOO.ur.item.getFolderById('${folder.id}')">${folder.name}</ur:a>/
   </c:forEach>
</div>
 <br/>	    
<!-- Table for files and folders  -->            
<table class="itemFolderTable" width="100%">
	<thead>
		<tr>
			<th class="thItemFolder" width="20%">Add</th>
			<th class="thItemFolder">File System</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="fileSystemObject" items="${fileSystem}">
			<tr >
				<td class="tdItemFolderLeftBorder">
				    <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
				    
				    	<c:if test='${user.id == fileSystemObject.versionedFile.owner.id}'>
						    <c:if test='${!ir:fileExistsInItem(fileSystemObject, item)}'>
			                    <span class="addBtnImg">&nbsp;</span><a href="javascript:YAHOO.ur.item.addFile('${fileSystemObject.versionedFile.id}');"> Add</a> 
							</c:if>
							<c:if test='${ir:fileExistsInItem(fileSystemObject, item)}'>
								Added
						    </c:if>
					    </c:if>

						<c:if test='${user.id != fileSystemObject.versionedFile.owner.id}'>
							Not an owner
					    </c:if>
	                 </c:if>

				    <c:if test="${fileSystemObject.fileSystemType.type == 'personalFolder'}">
	                    <span class="addBtnImg">&nbsp;</span><a href="javascript:YAHOO.ur.item.addFolder('${fileSystemObject.id}');"> Add</a> 
	                 </c:if>
                   
				</td>
				
				<td class="tdItemFolderRightBorder">
					 <c:if test="${fileSystemObject.fileSystemType.type == 'personalFolder'}">
	                 	<span class="folderBtnImg">&nbsp;</span> <a href="javascript:YAHOO.ur.item.getFolderById('${fileSystemObject.id}')"> <ur:maxText numChars="40" text="${fileSystemObject.name}"></ur:maxText> </a>
	                 </c:if>
	                 
	                 <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
	                    <ir:fileTypeImg cssClass="tableImg" versionedFile="${fileSystemObject.versionedFile}"/> <ur:maxText numChars="40" text="${fileSystemObject.name}"></ur:maxText>
	                 </c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
