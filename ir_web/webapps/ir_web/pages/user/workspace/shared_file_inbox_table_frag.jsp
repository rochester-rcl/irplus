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

<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>


<div class="dataTable">
    <ur:basicForm method="post" id="my_shared_inbox_files" name="mySharedInboxFiles" >
    <input type="hidden" id="shared_inbox_files_count" value="${sharedInboxFilesCount}">
    <c:if test="${!empty(sharedInboxFiles)}">
        <urstb:table width="100%">
            <urstb:thead>
                <urstb:tr>
                    <urstb:td>
                        <ur:checkbox name="checkAllSetting" 
	                    value="off" 
	                    onClick="YAHOO.ur.shared.file.inbox.setCheckboxes();"/>&nbsp;Inbox File
	                </urstb:td>
	                <urstb:td>
	                   &nbsp;Id&nbsp;
	                </urstb:td>
	                <urstb:td>
	                   File Name
	                </urstb:td>
	                <urstb:td>
	                   Shared By
	                </urstb:td>
	             </urstb:tr>
	         </urstb:thead>
	         <urstb:tbody
                var="sharedInboxFile" 
                oddRowClass="odd"
                evenRowClass="even"
                currentRowClassVar="rowClass"
                collection="${sharedInboxFiles}">
                <urstb:tr 
                        cssClass="${rowClass}"
                        onMouseOver="this.className='highlight'"
                        onMouseOut="this.className='${rowClass}'">
                        <urstb:td>
                            <ur:checkbox name="sharedInboxFileIds" value="${sharedInboxFile.id}"/>&nbsp;<ir:fileTypeImg cssClass="tableImg" versionedFile="${sharedInboxFile.versionedFile}"/>
                        </urstb:td>
                        <urstb:td>
                            ${sharedInboxFile.id}
                        </urstb:td>
                        <urstb:td>
                                <c:url var="inboxFileDownloadUrl" value="/user/inboxFileDownload.action">
		                            <c:param name="inboxFileId" value="${sharedInboxFile.id}"/>
		                        </c:url>
	                            <a href="${inboxFileDownloadUrl}"><ur:maxText numChars="50" text="${sharedInboxFile.versionedFile.nameWithExtension}"></ur:maxText></a>
                        </urstb:td>
                        <urstb:td>
                            ${sharedInboxFile.sharingUser.firstName}&nbsp;${sharedInboxFile.sharingUser.lastName}
                        </urstb:td>
                </urstb:tr>
            </urstb:tbody>
	     </urstb:table>
    </c:if>
	<input type="hidden" id="shared_destination_folder_id" name="destinationFolderId" value=""/>
    </ur:basicForm>
    <c:if test="${empty(sharedInboxFiles)}">
       <h3>There are no shared files currently in your inbox.</h3>
    </c:if>
</div>