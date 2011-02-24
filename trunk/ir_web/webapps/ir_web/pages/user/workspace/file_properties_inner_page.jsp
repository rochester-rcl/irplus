
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>


            <!-- This is only to top level properties information -->
             <form  id="file_properties_form" name="filePropertiesForm" 
                 action="user/viewPersonalFile.action">
     
	             <input type="hidden" id="personalFileId" 
	             name="personalFileId"  value="${personalFile.id}"/>
	         </form>
	        
            <c:url var="rootFolderUrl" value="/user/workspace.action">
               <c:param name="parentFolderId" value="0"/>
            </c:url>
            <h3> File Properties for :  /<span class="folderBtnImg">&nbsp;</span><a href="${rootFolderUrl}">${user.username}</a>/
                 <c:forEach var="folder" items="${folderPath}">
                     <c:url var="folderUrl" value="/user/workspace.action">
                         <c:param name="parentFolderId" value="${folder.id}"/>
                     </c:url>
                     <span class="folderBtnImg">&nbsp;</span><a href="${folderUrl}">${folder.name}</a>/
                 </c:forEach>
                 ${personalFile.versionedFile.nameWithExtension}
             </h3>
            
             <!-- This is only to share a file -->
	         <c:url var="getFileSharingUrl" value="/user/viewInviteUser.action"/>
             <form  id="share_with_users" name="shareWithUsers" method="post" action="${getFileSharingUrl}">
                 <input type="hidden" name="shareFileIds" value="${personalFile.id}"/>
                 <input type="hidden" name="parentFolderId" value="${personalFile.personalFolder.id}"/>
	         </form>
	    
	         <h3>Current Version Information</h3>
              <table class="table">
                  <tr>
                      <td><strong>Editing Status:&nbsp;&nbsp;&nbsp;</strong></td>
                      <td><c:if test="${personalFile.versionedFile.locked}">
                              <span class="lockBtnImg">&nbsp;</span> File Locked by ${personalFile.versionedFile.lockedBy.username}
                          </c:if>
                          <c:if test="${!personalFile.versionedFile.locked}">
                              <span class="unlockBtnImg">&nbsp;</span> File Unlocked 
                          </c:if>
                      </td>
                  </tr>
                  <tr>
                      <td><strong>Name:&nbsp;</strong></td>
                      <td>${personalFile.versionedFile.nameWithExtension}</td>
                  </tr>
                  <tr>
                      <td><strong>Version:&nbsp;</strong></td>
                      <td>${personalFile.versionedFile.currentVersion.versionNumber}</td>
                  </tr>
                  <tr>
                      <td><strong>Created Date:&nbsp;</strong></td>
                      <td>${personalFile.versionedFile.currentVersion.irFile.fileInfo.createdDate}</td>
                  </tr>
                  <tr>
                      <td><strong>File Owner:&nbsp;</strong></td>
                      <td>${personalFile.versionedFile.owner.firstName}&nbsp;${personalFile.versionedFile.owner.lastName}</td>
                  </tr>
                  <tr>
                      <td><strong>Created By:&nbsp;</strong></td>
                      <td>${personalFile.versionedFile.currentVersion.versionCreator.username}</td>
                  </tr>
                   <tr>
                      <td><strong>Size:&nbsp;</strong></td>
                      <td><ir:fileSizeDisplay sizeInBytes="${personalFile.versionedFile.currentVersion.irFile.fileInfo.size}"/></td>
                  </tr>
                  <tr>
                      <td><strong>Size on Disk:&nbsp;</strong></td>
                      <td><ir:fileSizeDisplay sizeInBytes="${ir:infoSizeOnDisk(personalFile.versionedFile.currentVersion.irFile.fileInfo)}"/></td>
                  </tr>
                  <tr>
                      <td><strong>Path:&nbsp;</strong></td>
                      <td>${personalFile.versionedFile.currentVersion.irFile.fileInfo.fullPath}</td>
                  </tr>
                  <tr>
                      <td><strong>File Info Id:&nbsp;</strong></td>
                      <td>${personalFile.versionedFile.currentVersion.irFile.fileInfo.id}</td>
                  </tr>
                  <tr>
                      <td><strong>Checksums:&nbsp;</strong></td>
                      <td>
                          <c:forEach var="fileInfoChecksum"
                              items="${personalFile.versionedFile.currentVersion.irFile.fileInfo.fileInfoChecksums}">
                                  ${fileInfoChecksum.checksum} - ${fileInfoChecksum.algorithmType}
                          </c:forEach>
                      </td>
                  </tr>
              </table>
              
   
	         <ir:acl domainObject="${personalFile.versionedFile}" hasPermission="EDIT">
                 <button class="ur_button" id="addVersionedFileButton" 
	                     onclick="YAHOO.ur.file.properties.versionedFileUploadDialog.showDialog()";
	                     onmouseover="this.className='ur_buttonover';"
 		                 onmouseout="this.className='ur_button';"><span class="pageAddBtnImg">&nbsp;</span> Add New Version</button>
	         </ir:acl>
	                
	         <c:if test="${ir:canShareFile(user, personalFile.versionedFile)}">   
	             <button class="ur_button" onclick="javascript:shareWithUsers.submit()" 
	                     onmouseover="this.className='ur_buttonover';"
 		                 onmouseout="this.className='ur_button';"><span class="groupAddBtnImg">&nbsp;</span>Share</button>
	         </c:if>
	                
	         <c:if test="${ir:canLockFile(user, personalFile.versionedFile)}">
	             <button class="ur_button" onclick="javascript:YAHOO.ur.file.properties.getLockOnFileId('${personalFile.id}', '${user.id}');" 
	                     onmouseover="this.className='ur_buttonover';"
 		                 onmouseout="this.className='ur_button';"><span class="lockBtnImg">&nbsp;</span>Lock &amp; Edit</button>
	         </c:if>
	             
	         <c:if test="${ir:isLocker(user,personalFile.versionedFile)}">
	             <button class="ur_button" onclick="javascript:YAHOO.ur.file.properties.unLockFile('${personalFile.id}', '${user.id}');" 
	                     onmouseover="this.className='ur_buttonover';"
 		                 onmouseout="this.className='ur_button';"> <span class="unlockBtnImg">&nbsp;</span>Unlock</button>
	         </c:if>
	         
	         <ir:acl domainObject="${personalFile.versionedFile}" hasPermission="EDIT">
	             <button class="ur_button" onClick="javascript:YAHOO.ur.file.properties.renameFile('${personalFile.id}');" 
	                 onmouseover="this.className='ur_buttonover';"
 		             onmouseout="this.className='ur_button';"
 		                ><span class="reportEditBtnImg">&nbsp;</span>Rename</button>
		     </ir:acl>
		     
		     <c:if test="${ir:isOwner(user, personalFile.versionedFile)}">             
		         <button class="ur_button" id="change_owner" onClick="javascript:YAHOO.ur.file.properties.changeOwnerDialog.showDialog();"
	                     onmouseover="this.className='ur_buttonover';"
 		                 onmouseout="this.className='ur_button';">Change Owner</button>
              </c:if>
              
              

              <c:if test="${!ur:isEmpty(personalFile.versionedFile.collaborators)}">
              <h3>Sharing - Confirmed</h3>
              
              <div class="dataTable">
              <urstb:table width="100%">
                  <urstb:thead>
                      <urstb:tr>
                          <urstb:td> Name </urstb:td>
                          <urstb:td> User Name </urstb:td>
                          <urstb:td> Email </urstb:td>
                      </urstb:tr>
                  </urstb:thead>
                  <urstb:tbody
                      var="collaborator" 
                      oddRowClass="odd"
                      evenRowClass="even"
                      currentRowClassVar="rowClass"
                      collection="${personalFile.versionedFile.collaborators}">
                      <urstb:tr 
                          cssClass="${rowClass}"
                          onMouseOver="this.className='highlight'"
                          onMouseOut="this.className='${rowClass}'">
                          <urstb:td>
                          ${collaborator.collaborator.firstName} &nbsp; ${collaborator.collaborator.lastName}
                          </urstb:td>
                        
                          <urstb:td>
                           <c:url var="showAllSharedFiles" value="/user/viewFilesSharedWithUser.action">
                              <c:param name="sharedWithUserId" value="${collaborator.collaborator.id}"/>
                          </c:url>
                          <a href="${showAllSharedFiles}">${collaborator.collaborator.username}</a>
                          </urstb:td>

                          <urstb:td>
                          ${collaborator.collaborator.defaultEmail.email}
                          </urstb:td>                        
                            
                      </urstb:tr>
                  </urstb:tbody>
              </urstb:table>
              </div>
              </c:if>
              
              <c:if test="${!ur:isEmpty(personalFile.versionedFile.invitees)}">
              <h3>Sharing - Awaiting account creation</h3>
              <div class="dataTable">
              <urstb:table width="100%">
                  <urstb:thead>
                      <urstb:tr>
                          <urstb:td> Email </urstb:td>
                      </urstb:tr>
                  </urstb:thead>
                  <urstb:tbody
                      var="invite" 
                      oddRowClass="odd"
                      evenRowClass="even"
                      currentRowClassVar="rowClass"
                      collection="${personalFile.versionedFile.invitees}">
                      <urstb:tr 
                          cssClass="${rowClass}"
                          onMouseOver="this.className='highlight'"
                          onMouseOut="this.className='${rowClass}'">
                          <urstb:td>
                          ${invite.inviteToken.email} 
                          </urstb:td>
                      </urstb:tr>
                  </urstb:tbody>
              </urstb:table>
              </div>
              </c:if>
              
	          <h3>All File Versions</h3>
	     
	          <div class="dataTable">
              <urstb:table width="100%">
                  <urstb:thead>
                      <urstb:tr>
                          <urstb:td>Thumbnail</urstb:td>
                          <urstb:td>Name</urstb:td>
                          <urstb:td>Description</urstb:td>
                          <urstb:td>File Version</urstb:td>
                          <urstb:td>Checksum</urstb:td>
                          <urstb:td>Created Date</urstb:td>
                          <urstb:td>Created By</urstb:td>
                      </urstb:tr>
                  </urstb:thead>
                  <urstb:tbody
                      var="version" 
                      oddRowClass="odd"
                      evenRowClass="even"
                      currentRowClassVar="rowClass"
                      collection="${personalFile.versionedFile.versions}">
                      <urstb:tr 
                          cssClass="${rowClass}"
                          onMouseOver="this.className='highlight'"
                          onMouseOut="this.className='${rowClass}'">
                          <urstb:td>
                             <c:if test='${ir:hasThumbnail(version.irFile)}'>
                                  <c:url var="url" value="/user/personalFileThumbnailDownloader.action">
                                      <c:param name="personalFileId" value="${personalFile.id}"/>
                                      <c:param name="versionNumber" value="${version.versionNumber}"/>
                                  </c:url>
                                  <img height="66px" width="100px" src="${url}"/>
                             </c:if>
                          </urstb:td>
                        
                          <urstb:td>
                              <c:url var="personalFileDownloadUrl" value="/user/personalFileDownload.action">
	                              <c:param name="personalFileId" value="${personalFile.id}"/>
	                              <c:param name="versionNumber" value="${version.versionNumber}"/>
	                          </c:url>
	                          <a href="${personalFileDownloadUrl}">${version.irFile.nameWithExtension}</a>
                          </urstb:td>

                          <urstb:td>
                              ${version.irFile.description}
                          </urstb:td>                        

                          <urstb:td>
                              ${version.versionNumber}
                          </urstb:td>                        

                          <urstb:td>
                              <c:forEach var="fileInfoChecksum"
                                  items="${version.irFile.fileInfo.fileInfoChecksums}">
                                      ${fileInfoChecksum.checksum} - ${fileInfoChecksum.algorithmType}
                              </c:forEach>
                          </urstb:td>                        

                          <urstb:td>
                              ${version.irFile.fileInfo.createdDate}
                          </urstb:td>                        

                          <urstb:td>
                              ${version.versionCreator.firstName} &nbsp; ${version.versionCreator.lastName}
                          </urstb:td>                        

                      </urstb:tr>
                  </urstb:tbody>
              </urstb:table>
              </div>
              
             <c:url var="addNewFileVersion" value="/user/viewNewFileVersionUpload.action">
                 <c:param name="personalFileId" value="${personalFile.id}"/>
             </c:url>
             