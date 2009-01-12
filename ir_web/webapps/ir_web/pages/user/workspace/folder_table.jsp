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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="messages"/>

  <h3> File system size : <ir:fileSizeDisplay sizeInBytes="${fileSystemSize}"/>  </h3>

  <h3>Path:&nbsp;/
	    <span class="folderBtnImg">&nbsp;</span>
	          <c:if test="${parentFolderId != 0}">
                   <a href="javascript:YAHOO.ur.folder.getFolderById('0')">${user.username}</a>&nbsp;/
               </c:if>
               <c:if test="${parentFolderId == 0}">
                   ${user.username}&nbsp;/
               </c:if>
	    
           <c:forEach var="folder" items="${folderPath}">
               <span class="folderBtnImg">&nbsp;</span>
                   <c:if test="${folder.id != parentFolderId}">
                       <a href="javascript:YAHOO.ur.folder.getFolderById('${folder.id}')">${folder.name}</a>&nbsp;/
                   </c:if>
                   <c:if test="${folder.id == parentFolderId}">
                       ${folder.name}&nbsp;/
                   </c:if>
           </c:forEach>
   </h3>
      

    <table width="100%">
        <tr>
            <td align="left">
                <select onchange="javascript:YAHOO.ur.folder.executeCheckboxAction(this.options[this.selectedIndex].value);">
                    <option value="action" id="folder_checkbox_action_set"  selected="selected">Action on checked files and folders ...</option>
	                <option value="publish">Publish</option>
	                <option value="share">Share</option>
                    <option value="delete">Delete</option>
                    <option value="move">Move</option>
	            </select>
	        </td>
	        <td align="right">
	           <button class="ur_button" 
 		                   onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
 		                   onClick="YAHOO.ur.folder.newFolderDialog.showFolder();"
 		                   id="showFolder"><span class="addFolderBtnImg">&nbsp;</span><fmt:message key="new_folder"/></button> 
	           <c:if test='${ir:userHasRole("ROLE_AUTHOR,ROLE_RESEARCHER", "OR")}'>
	               <button class="ur_button" 
	                       onclick="YAHOO.ur.folder.singleFileUploadDialog.showDialog();"
	                       onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
	                       id="addSingleFileButton"><span class="pageAddBtnImg">&nbsp;</span>Add File</button>
	               <button class="ur_button" onclick="javascript:document.addFilesForm.submit();"
	                       onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
	                       id="addFilesButton"><span class="deleteBtnImg">&nbsp;</span>Add Files</button>
	               <button class="ur_button" id="showPublishItem" 
	                   onclick="javascript:YAHOO.ur.personal.collection.newItemDialog.createFromFilesFolders();"
	                   onmouseover="this.className='ur_buttonover';"
 		               onmouseout="this.className='ur_button';"><span class="reportGoBtnImg">&nbsp;</span>Publish</button>
	           </c:if>
	        </td>
	    </tr>
	</table>
                
<div class="dataTable">
	<c:url var="uploadFiles" value="/user/viewUploadFilesPage.action"/>
	
	<form method="get" action="${uploadFiles}" name="addFilesForm">
	    <input type="hidden" value="${parentFolderId}" name="folderId"/>
	</form>
	
    <c:url var="myFoldersUrl" value="/user/workspace.action"/>
       
	<ur:basicForm method="post" id="folders" name="myFolders" action="${myFoldersUrl}">
	 
	    
	       <!-- Begin - To create new Publication with selected files, item name is set in this form and submitted -->
	       <input type="hidden" id="myFolders_item_name" name="itemName"/>
	       <!-- End - To create new Publication -->
	       
           <input type="hidden" id="myFolders_parentFolderId" name="parentFolderId" value="${parentFolderId}"/>
           <input type="hidden" id="page_type" 
	                  name="pageType"  value="folder_page"/>
	       <input type="hidden" id="folder_sort_type" name="sortType" value="${sortType}"/>
	       <input type="hidden" id="folder_sort_element" name="sortElement" value="${sortElement}"/>
       
        <urstb:table width="100%">
            <urstb:thead>
                <urstb:tr>
                    <urstb:td><ur:checkbox name="checkAllSetting" 
	                    value="off" 
	                    onClick="YAHOO.ur.folder.setCheckboxes()"/>
	                </urstb:td>
                    <urstb:tdHeadSort
                       height="33"
                       currentSortAction="${folderTypeSort}"
                       ascendingSortAction="javascript:YAHOO.ur.folder.updateSort('asc', 'type');"
                       descendingSortAction="javascript:YAHOO.ur.folder.updateSort('desc', 'type');">
                       <u>Type</u><urstb:thImgSort
                            sortAscendingImage="page-resources/images/all-images/bullet_arrow_down.gif"
                            sortDescendingImage="page-resources/images/all-images/bullet_arrow_up.gif"/></urstb:tdHeadSort>
                    
                    <urstb:td>Id</urstb:td>
                    
                    <urstb:tdHeadSort
                       height="33"
                       currentSortAction="${folderNameSort}"
                       ascendingSortAction="javascript:YAHOO.ur.folder.updateSort('asc', 'name');"
                       descendingSortAction="javascript:YAHOO.ur.folder.updateSort('desc', 'name');">
                       <u>Name</u><urstb:thImgSort
                            sortAscendingImage="page-resources/images/all-images/bullet_arrow_down.gif"
                            sortDescendingImage="page-resources/images/all-images/bullet_arrow_up.gif"/></urstb:tdHeadSort>
                        
                    <urstb:td>Version</urstb:td>
                    <urstb:td>File Size</urstb:td>
                    <urstb:td>Properties</urstb:td>
                    <urstb:td>Share</urstb:td>
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
	                     <c:if test="${fileSystemObject.fileSystemType.type == 'personalFolder'}">
	                        <ur:checkbox name="folderIds" id="folder_checkbox_${fileSystemObject.id}" 
	                            value="${fileSystemObject.id}"/>
	                     </c:if>
	                     
	                     <!-- this deals with file information
	                          folders get an id of the folder_checkbox_{id} 
	                          where id  is the id of the folder -->
	                     <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
	                         <ur:checkbox name="fileIds" id="file_checkbox_${fileSystemObject.id}" 
	                             value="${fileSystemObject.id}"/>
	                     </c:if>
                        </urstb:td>
                        <urstb:td>
                            <!-- this deals with folder information
	                          folders get an id of the folder_checkbox_{id} 
	                          where id  is the id of the folder 
	                          clicking on a link creates a popup menu-->
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'personalFolder'}">
	                        
	                             <button type="button" class="table_button" 
	                                onclick="javascript:YAHOO.ur.folder.buildFolderMenu(${fileSystemObject.id}, 
	                                this,'folder_'+ ${fileSystemObject.id}, 
	                                'folder_menu_' + ${fileSystemObject.id},
	                                '${ur:escapeSingleQuote(fileSystemObject.name)}');"><span class="folderBtnImg">&nbsp;</span><span class="blueDownBtnImg">&nbsp;</span></button>
	                            <div id="folder_${fileSystemObject.id}"></div>
	                         </c:if>
	                     
	                         <!-- clicking on a link creates a popup menu-->
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
	                                	                        
	                             <!--  build the menu on click -->
	                             <button type="button" class="table_button"
	                                 onclick="javascript:YAHOO.ur.folder.buildFileMenu(this, 'file_'+ ${fileSystemObject.id}, 
	                                 'file_menu_' + ${fileSystemObject.id}, 
	                                 ${fileSystemObject.id}, 
	                                 ${user.id}, 
	                                 ${fileSystemObject.versionedFile.locked}, 
	                                 ${ir:isLocker(user,fileSystemObject.versionedFile)}, 
	                                 ${ir:canLockFile(user,fileSystemObject.versionedFile)},
	                                 ${ir:canBreakLock(user,fileSystemObject.versionedFile)},
	                                 ${ir:canShareFile(user,fileSystemObject.versionedFile)}, 
	                                 ${ir:canEditFile(user,fileSystemObject.versionedFile)},
	                                 '${ur:escapeSingleQuote(fileSystemObject.name)}', '${ur:escapeSingleQuote(fileSystemObject.versionedFile.name)}' );"><ir:fileTypeImg cssClass="tableImg" versionedFile="${fileSystemObject.versionedFile}"/><span class="blueDownBtnImg">&nbsp;</span></button>
	                             <div id="file_${fileSystemObject.id}"></div>
	                         </c:if>
                        </urstb:td>
                        <urstb:td>
                            ${fileSystemObject.id}
                        </urstb:td>
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalFolder'}">
	                            <a href="javascript:YAHOO.ur.folder.getFolderById('${fileSystemObject.id}')"><ur:maxText numChars="50" text="${fileSystemObject.name}"/></a><c:if test="${fileSystemObject.description != ''}"><div class="smallText">Description: <ur:maxText numChars="50" text="${fileSystemObject.description}"/></div></c:if>
	                        </c:if>
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
		                        <c:url var="personalFileDownloadUrl" value="/user/personalFileDownload.action">
		                            <c:param name="personalFileId" value="${fileSystemObject.id}"/>
		                        </c:url>
	                            <a href="${personalFileDownloadUrl}"><ur:maxText numChars="50" text="${fileSystemObject.name}"/></a>
	                            <c:if test="${fileSystemObject.versionedFile.locked}">
	                                <span class="lockBtnImg">&nbsp;</span><div class="smallText">Locked by ${fileSystemObject.versionedFile.lockedBy.username}</div>
	                            </c:if>
	                         </c:if>
                        </urstb:td>
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
	                         ${fileSystemObject.versionedFile.largestVersion}
	                        </c:if>
                        </urstb:td>
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
	                         <ir:fileSizeDisplay sizeInBytes="${fileSystemObject.versionedFile.currentFileSizeBytes}"/>
	                        </c:if>
                        </urstb:td>
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
                                <c:url var="fileProperties" value="/user/viewPersonalFile.action">
                                    <c:param name="personalFileId" value="${fileSystemObject.id}"/>
                                </c:url>
	                            <a href="${fileProperties}">properties</a>
	                        </c:if>
	                        <c:if test="${fileSystemObject.fileSystemType.type == 'personalFolder'}">
                                <c:url var="folderProperties" value="/user/viewFolderProperties.action">
                                    <c:param name="personalFolderId" value="${fileSystemObject.id}"/>
                                </c:url>	                        
	                           <a href="${folderProperties}">Properties</a>
	                        </c:if>
                        </urstb:td>
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
               	                <!--  users with only invite share permissions can share files -->
               	                <!--  owners always have invite permissions -->
               	                <c:if test="${ir:isOwner(user, fileSystemObject.versionedFile)}">
               	                    <c:if test="${empty fileSystemObject.versionedFile.collaborators}">
	                      		        <span class="groupAddBtnImg">&nbsp;</span> <a href="Javascript:YAHOO.ur.folder.shareSingleConfirm('file_checkbox_${fileSystemObject.id}');"> shareable </a>
	                      		    </c:if>
	                      		    <c:if test="${!empty fileSystemObject.versionedFile.collaborators}">
	                      		        <span class="groupAddBtnImg">&nbsp;</span> <a href="Javascript:YAHOO.ur.folder.shareSingleConfirm('file_checkbox_${fileSystemObject.id}');"> shared </a>
	                      		    </c:if>
               	                </c:if>
               	                <c:if test="${!ir:isOwner(user, fileSystemObject.versionedFile)}">
               	                    <ir:acl domainObject="${fileSystemObject.versionedFile}" hasPermission="SHARE">
	                      		        <c:if test="${empty fileSystemObject.versionedFile.collaborators}">
	                      		             <span class="groupAddBtnImg">&nbsp;</span> <a href="Javascript:YAHOO.ur.folder.shareSingleConfirm('file_checkbox_${fileSystemObject.id}');"> shareable </a>
	                      		        </c:if>
	                      		        <c:if test="${!empty fileSystemObject.versionedFile.collaborators}">
	                      		            <span class="groupAddBtnImg">&nbsp;</span> <a href="Javascript:YAHOO.ur.folder.shareSingleConfirm('file_checkbox_${fileSystemObject.id}');"> shared </a>
	                      		        </c:if>
	                                </ir:acl>
	                               
	                                <c:if test="${!ir:canShareFile(user,fileSystemObject.versionedFile)}">
	                                    Not Shareable
	                                </c:if>
               	                </c:if>
               	            
	                        </c:if>
                        </urstb:td>
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
	                         ${fileSystemObject.versionedFile.owner.username }
	                        </c:if>
                        </urstb:td>
                    </urstb:tr>
            </urstb:tbody>
        </urstb:table>
	    <input type="hidden" id="move_files_folders_destination_id" name="destinationFolderId" value=""/>
    </ur:basicForm>
</div>