<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />


<!--  
   Copyright 2008-2011 University of Rochester

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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="messages"/>

   <br/>

   <table width="100%">
   <tr>
       <td align="left" width="75%">
           <strong>Path:&nbsp;/
	          <span class="groupImg">&nbsp;</span>
	          <a href="javascript:YAHOO.ur.user.group_workspace.getGroupWorkspaces()">Group Workspaces</a> /
	          <c:if test="${!empty folderPath}">
	              <span class="groupImg">&nbsp;</span>
                   <a href="javascript:YAHOO.ur.user.group_workspace.getGroupWorkspaceById(${groupWorkspace.id})">${groupWorkspace.name}</a>&nbsp;/
               </c:if>
               <c:if test="${empty folderPath}">
                   <span class="groupImg">&nbsp;</span>
                   ${groupWorkspace.name}&nbsp;/
               </c:if>
	    
               <c:forEach var="folder" items="${folderPath}">
               <span class="folderBtnImg">&nbsp;</span>
                   <c:if test="${folder.id != parentFolderId}">
                       <a href="javascript:YAHOO.ur.user.group_workspace.getFolderById(${folder.id},-1)">${folder.name}</a>&nbsp;/
                   </c:if>
                   <c:if test="${folder.id == parentFolderId}">
                       ${folder.name}&nbsp;/
                   </c:if>
               </c:forEach>
           </strong>
       </td>      
   </tr>
   </table>
   
   
  <div align="right">
	           <button class="ur_button" 
 		                   onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
 		                   onClick="YAHOO.ur.user.group_workspace.groupFolderDialog.showFolder();"
 		                   id="showFolder"><span class="addFolderBtnImg">&nbsp;</span><fmt:message key="new_folder"/></button> 
	           <c:if test='${ir:userHasRole("ROLE_AUTHOR", "OR")}'>
	               <button class="ur_button" 
	                       onclick="YAHOO.ur.user.group_workspace.singleFileUploadDialog.showDialog();"
	                       onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
	                       id="addSingleFileButton"><span class="pageAddBtnImg">&nbsp;</span>Add File</button>
	               <button class="ur_button" onclick="javascript:document.addFilesForm.submit();"
	                       onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
	                       id="addFilesButton"><span class="pageCopyBtnImg">&nbsp;</span>Add Files</button>
	          </c:if>
	               <button class="ur_button" 
	                       onclick="YAHOO.ur.folder.moveFolderData();"
	                       onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
	                       id="moveButton"><span class="pageWhiteGoBtnImg">&nbsp;</span>Move</button>
	               <button class="ur_button" 
	                       onclick="YAHOO.ur.user.group_workspace.deleteFolder.showDialog();"
	                       onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
	                       id="deleteButton"><span class="deleteBtnImg">&nbsp;</span>Delete</button>
	                       
	          <c:if test='${ir:userHasRole("ROLE_AUTHOR", "OR")}'>
	               <button class="ur_button" 
	                       onclick="YAHOO.ur.folder.inviteUser();"
	                       onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
	                       id="shareButton"><span class="groupAddBtnImg">&nbsp;</span>Share</button>
	               <button class="ur_button" id="showPublishItem" 
	                   onclick="javascript:YAHOO.ur.personal.collection.newItemDialog.createFromFilesFolders();"
	                   onmouseover="this.className='ur_buttonover';"
 		               onmouseout="this.className='ur_button';"><span class="reportGoBtnImg">&nbsp;</span>Publish</button>
	           </c:if>
    </div>
    <br/>
 
               
<div class="dataTable">
	
	
    <c:url var="myGroupWorkspaceFoldersUrl" value="/user/workspace.action"/>
	<form method="post" id="groupFolders" name="groupFolders" action="${myFoldersUrl}">
	       
        <input type="hidden" id="groupFoldersParentFolderId" name="parentFolderId" value="${parentFolderId}"/>
        <input type="hidden" id="groupFoldersGroupWorkspaceId" name="groupWorkspaceId" value="${groupWorkspace.id}"/>
        <c:url var="downArrow" value="/page-resources/images/all-images/bullet_arrow_down.gif" />
        <urstb:table width="100%">
            <urstb:thead>
                <urstb:tr>
                    <urstb:td><input type="checkbox" name="checkAllSetting" 
	                    value="off" 
	                    onClick="alert('clicked')"/>
	                </urstb:td>
                    <urstb:td>Type</urstb:td>
                    <urstb:td>Name</urstb:td>
                    <urstb:td>Version</urstb:td>
                    <urstb:td>File Size</urstb:td>
                    <urstb:td>Properties</urstb:td>
                    <urstb:td>Owner</urstb:td>
                </urstb:tr>
            </urstb:thead>
            <urstb:tbody
                var="fileSystemObject" 
                oddRowClass="odd"
                evenRowClass="even"
                currentRowClassVar="rowClass"
                collection="${fileSystem}">
                    <urstb:tr 
                        cssClass="${rowClass}"
                        onMouseOver="this.className='highlight'"
                        onMouseOut="this.className='${rowClass}'">
                        <urstb:td>
                            <!-- this deals with folder information
	                          folders get an id of the folder_checkbox_{id} 
	                          where id  is the id of the folder -->
	                          <c:if test="${fileSystemObject.fileSystemType.type == 'groupFolder'}">
	                              <input type="checkbox" name="groupFolderIds" id="folder_checkbox_${fileSystemObject.id}" 
	                                     value="${fileSystemObject.id}"/>
	                         </c:if>
	                     
	                         <!-- this deals with file information
	                              folders get an id of the folder_checkbox_{id} 
	                              where id  is the id of the folder -->
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'groupFile'}">
	                             <input type="checkbox" name="groupFileIds" id="file_checkbox_${fileSystemObject.id}" 
	                                 value="${fileSystemObject.id}"/>
	                         </c:if>
                        </urstb:td>
                        
                        <urstb:td>
                               <!-- this deals with folder information
	                          folders get an id of the folder_checkbox_{id} 
	                          where id  is the id of the folder -->
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'groupFolder'}">
	                            <button type="button"  class="table_button" 
	                                onmouseover="this.className='table_buttonover';"
 		                            onmouseout="this.className='table_button';"
	                                ><span class="folderBtnImg"></span><img src="${downArrow}"/></button>
	                         </c:if>
	                     
	                         <!-- this deals with file information
	                              folders get an id of the folder_checkbox_{id} 
	                              where id  is the id of the folder -->
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'groupFile'}">
	                              <button type="button" class="table_button"
	                                 onmouseover="this.className='table_buttonover';"
 		                             onmouseout="this.className='table_button';"><ir:fileTypeImg cssClass="tableImg" versionedFile="${fileSystemObject.versionedFile}"/><img src="${downArrow}"/></button>
	                         </c:if>
                        </urstb:td>
                        
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'groupFolder'}">
	                            <a href="javascript:YAHOO.ur.user.group_workspace.getFolderById(${fileSystemObject.id}, -1)"><ur:maxText numChars="50" text="${fileSystemObject.name}"/></a><c:if test="${fileSystemObject.description != '' && fileSystemObject.description != null}"><div class="smallText">Description: <ur:maxText numChars="50" text="${fileSystemObject.description}"/></div></c:if>
	                        </c:if>
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'groupFile'}">
		                        <c:url var="groupWorkspaceFileDownloadUrl" value="/user/groupWorkspaceFileDownload.action">
		                            <c:param name="groupWorkspaceFileId" value="${fileSystemObject.id}"/>
		                        </c:url>
	                            <a href="${groupWorkspaceFileDownloadUrl}"><ur:maxText numChars="50" text="${fileSystemObject.name}"/></a>
	                            <c:if test="${fileSystemObject.versionedFile.locked}">
	                                <span class="lockBtnImg">&nbsp;</span><div class="smallText">Locked by ${fileSystemObject.versionedFile.lockedBy.username}</div>
	                            </c:if>
	                            <c:if test="${fileSystemObject.versionedFile.description != '' && fileSystemObject.versionedFile.description != null}"><div class="smallText">Description: <ur:maxText numChars="50" text="${fileSystemObject.versionedFile.description}"/></div></c:if>
	                         </c:if>
                        </urstb:td>
                        
                        <urstb:td>
 	                         <!-- this deals with file information
	                              folders get an id of the folder_checkbox_{id} 
	                              where id  is the id of the folder -->
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'groupFile'}">
	                             
	                         </c:if>
                        </urstb:td>
                        
                        <urstb:td>
 	                         <!-- this deals with file information
	                              folders get an id of the folder_checkbox_{id} 
	                              where id  is the id of the folder -->
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'groupFile'}">
	                             
	                         </c:if>
                        </urstb:td>
                            
                        <urstb:td>
                            <a href="">Properties</a>
                        </urstb:td>
                            
                        <urstb:td>
                            <!-- this deals with folder information
	                          folders get an id of the folder_checkbox_{id} 
	                          where id  is the id of the folder -->
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'groupFolder'}">
	                              ${fileSystemObject.owner.firstName}&nbsp;${fileSystemObject.owner.lastName}
	                         </c:if>
	                     
	                         <!-- this deals with file information
	                              folders get an id of the folder_checkbox_{id} 
	                              where id  is the id of the folder -->
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'groupFile'}">
	                             
	                         </c:if>
                        </urstb:td>
                            
                        </urstb:tr>
            </urstb:tbody>
        </urstb:table>
	    <input type="hidden" id="move_files_folders_destination_id" name="destinationFolderId" value=""/>
    </form>
</div>