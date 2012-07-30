
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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>


<div class="dataTable">
       
        <urstb:table width="100%">
         <urstb:caption>
        Results for search: ${userWorkspaceSearchResults.originalQuery}
        Total Number of Hits: ${userWorkspaceSearchResults.totalHits}
        Total Number of Results: ${userWorkspaceSearchResults.numberObjects}
        </urstb:caption>
            <urstb:thead>
                <urstb:tr>
                    <urstb:td>Name</urstb:td>
                    <urstb:td>Path</urstb:td>
                    <urstb:td>Description</urstb:td>
                    <urstb:td>Open Location</urstb:td>
                </urstb:tr>
            </urstb:thead>
            <urstb:tbody
                var="fileSystemObject" 
                oddRowClass="odd"
                evenRowClass="even"
                currentRowClassVar="rowClass"
                collection="${userWorkspaceSearchResults.objects}">
                    <urstb:tr 
                        cssClass="${rowClass}"
                        onMouseOver="this.className='highlight'"
                        onMouseOut="this.className='${rowClass}'">
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
                                <c:url var="personalFileDownloadUrl" value="/user/personalFileDownload.action">
		                            <c:param name="personalFileId" value="${fileSystemObject.id}"/>
		                        </c:url>
                                <ir:fileTypeImg cssClass="tableImg" 
                                     versionedFile="${fileSystemObject.versionedFile}"/><a href="${personalFileDownloadUrl}">${fileSystemObject.versionedFile.nameWithExtension}</a>
                            </c:if>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalFolder'}">
	                            <span class="folderBtnImg">&nbsp;</span><a href="javascript:YAHOO.ur.workspace.search.showFolder('${fileSystemObject.id}')">${fileSystemObject.name}</a>
	                        </c:if>
	                        <c:if test="${fileSystemObject.fileSystemType.type == 'sharedInboxFile'}">
	                            <c:url var="inboxFileDownloadUrl" value="/user/inboxFileDownload.action">
		                            <c:param name="inboxFileId" value="${fileSystemObject.id}"/>
		                        </c:url>
	                            <ir:fileTypeImg cssClass="tableImg" 
                                    versionedFile="${fileSystemObject.versionedFile}"/> <a href="${inboxFileDownloadUrl}">${fileSystemObject.versionedFile.nameWithExtension}</a>
	                        </c:if>
 							<c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
                                <c:url var="personalItemPreviewUrl" value="/user/previewPublication.action">
		                            <c:param name="genericItemId" value="${fileSystemObject.versionedItem.currentVersion.item.id}"/>
		                        </c:url>
                                <span class="packageBtnImg">&nbsp;</span><a href="${personalItemPreviewUrl}">${fileSystemObject.name}</a>
                            </c:if>	
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalCollection'}">
                                <span class="folderBtnImg">&nbsp;</span><a href="javascript:YAHOO.ur.workspace.search.showCollection(${fileSystemObject.id})">${fileSystemObject.name}</a>
                            </c:if>                        
                        </urstb:td>
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
                               /${user.username}${fileSystemObject.path}
                            </c:if>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalFolder'}">
	                           /${user.username}${fileSystemObject.path}
	                        </c:if>
	                        <c:if test="${fileSystemObject.fileSystemType.type == 'sharedInboxFile'}">
	                            /Shared File Inbox${fileSystemObject.path}
	                        </c:if>
	                        <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
	                            /My Publications${fileSystemObject.path}
	                        </c:if>
	                        <c:if test="${fileSystemObject.fileSystemType.type == 'personalCollection'}">
	                            /My Publications${fileSystemObject.path}
	                        </c:if>
                        </urstb:td>
                        <urstb:td>${fileSystemObject.description}</urstb:td>
                        <urstb:td> 
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
                               <a href="javascript:YAHOO.ur.workspace.search.showFolder('${fileSystemObject.personalFolder.id}')">Open Folder</a>
                            </c:if>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalFolder'}">
	                            <a href="javascript:YAHOO.ur.workspace.search.showFolder('${fileSystemObject.id}')">Open Folder</a>
	                        </c:if>
	                        <c:if test="${fileSystemObject.fileSystemType.type == 'sharedInboxFile'}">
	                            <a href="javascript:YAHOO.ur.shared.file.inbox.getSharedFiles();YAHOO.ur.user.workspace.setActiveIndex('FILE_INBOX') ">Inbox</a>
	                        </c:if>
	                        <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
	                            <a href="javascript:YAHOO.ur.workspace.search.showCollection('${fileSystemObject.personalCollection.id}')">Open Folder</a>
	                        </c:if>
	                        <c:if test="${fileSystemObject.fileSystemType.type == 'personalCollection'}">
	                            <a href="javascript:YAHOO.ur.workspace.search.showCollection(${fileSystemObject.id})">Open Folder</a>
	                        </c:if>
                        </urstb:td>
                    </urstb:tr>
            </urstb:tbody>
        </urstb:table>
</table>
</div>
