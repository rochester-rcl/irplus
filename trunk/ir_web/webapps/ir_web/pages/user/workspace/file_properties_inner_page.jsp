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


            <!-- This is only to top level properties information -->
             <ur:basicForm  id="file_properties_form" name="filePropertiesForm" 
                 action="user/viewPersonalFile.action">
     
	             <input type="hidden" id="personalFileId" 
	             name="personalFileId"  value="${personalFile.id}"/>
	         </ur:basicForm>
	        
            <c:url var="rootFolderUrl" value="/user/workspace.action">
               <c:param name="parentFolderId" value="0"/>
            </c:url>
            <h3> File Properties for :  /<span class="folderBtnImg">&nbsp;</span><a href="${rootFolderUrl}">personalFolders</a>/
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
	    
	         <br/>
              <table class="table">
                  <tr>
                      <td><strong>Current Editing Status:&nbsp;</strong></td>
                      <td><c:if test="${personalFile.versionedFile.locked}">
                              <span class="lockBtnImg">&nbsp;</span> File Locked by ${personalFile.versionedFile.lockedBy.username}
                          </c:if>
                          <c:if test="${!personalFile.versionedFile.locked}">
                              <span class="unlockBtnImg">&nbsp;</span> File Unlocked 
                          </c:if>
                      </td>
                  </tr>
                  <tr>
                      <td><strong>Current Version Name:&nbsp;</strong></td>
                      <td>${personalFile.versionedFile.nameWithExtension}</td>
                  </tr>
                  <tr>
                      <td><strong>Current Version:&nbsp;</strong></td>
                      <td>${personalFile.versionedFile.currentVersion.versionNumber}</td>
                  </tr>
                  <tr>
                      <td><strong>Current Version Created Date:&nbsp;</strong></td>
                      <td>${personalFile.versionedFile.currentVersion.irFile.fileInfo.createdDate}</td>
                  </tr>
                  <tr>
                      <td><strong>Current Version Created By:&nbsp;</strong></td>
                      <td>${personalFile.versionedFile.currentVersion.versionCreator.username}</td>
                  </tr>
                  <tr>
                      <td><strong>Current Version Size:&nbsp;</strong></td>
                      <td>${ir:infoSizeOnDisk(personalFile.versionedFile.currentVersion.irFile.fileInfo)}</td>
                  </tr>
                  <tr>
                      <td><strong>Current Version Path:&nbsp;</strong></td>
                      <td>${personalFile.versionedFile.currentVersion.irFile.fileInfo.fullPath}</td>
                  </tr>
                  <tr>
                      <td><strong>Current File Info Id:&nbsp;</strong></td>
                      <td>${personalFile.versionedFile.currentVersion.irFile.fileInfo.id}</td>
                  </tr>
                  <tr>
                      <td><strong>Current Version Checksums:&nbsp;</strong></td>
                      <td>
                          <c:forEach var="fileInfoChecksum"
                              items="${personalFile.versionedFile.currentVersion.irFile.fileInfo.fileInfoChecksums}">
                                  ${fileInfoChecksum.checksum} - ${fileInfoChecksum.algorithmType}
                          </c:forEach>
                      </td>
                  </tr>
              </table>
              
              <table>
	             <tr>
	                 <td>
	                     <ir:acl domainObject="${personalFile.versionedFile}" hasPermission="EDIT">
           
	                        <button class="ur_button" id="addVersionedFileButton" 
	                            onclick="YAHOO.ur.file.properties.versionedFileUploadDialog.showDialog()";
	                            onmouseover="this.className='ur_buttonover';"
 		                        onmouseout="this.className='ur_button';"><span class="pageAddBtnImg">&nbsp;</span> Add New Version</button>
	                     </ir:acl>
	                 </td>
	                 <td>
	                     <button class="ur_button" onclick="" 
	                         onmouseover="this.className='ur_buttonover';"
 		                     onmouseout="this.className='ur_button';"><span class="reportGoBtnImg">&nbsp;</span> Publish</button>
	                 </td>
	                 <td>
	                    
	                     <c:if test="${ir:canShareFile(user, personalFile.versionedFile)}">   
	                         <button class="ur_button" onclick="javascript:shareWithUsers.submit()" 
	                             onmouseover="this.className='ur_buttonover';"
 		                         onmouseout="this.className='ur_button';"><span class="groupAddBtnImg">&nbsp;</span>Share</button>
	                     </c:if>
	                 </td>
	                  
	                 <c:if test="${ir:canLockFile(user, personalFile.versionedFile)}">
	                 <td>
	                      <button class="ur_button" onclick="javascript:YAHOO.ur.file.properties.getLockOnFileId('${personalFile.id}', '${user.id}');" 
	                          onmouseover="this.className='ur_buttonover';"
 		                      onmouseout="this.className='ur_button';"><span class="lockBtnImg">&nbsp;</span>Lock &amp; Edit</button>
	                 </td>
	                 </c:if>
	             
	                 <c:if test="${ir:isLocker(user,personalFile.versionedFile)}">
	                 <td>
	                     <button class="ur_button" onclick="javascript:YAHOO.ur.file.properties.unLockFile('${personalFile.id}', '${user.id}');" 
	                         onmouseover="this.className='ur_buttonover';"
 		                     onmouseout="this.className='ur_button';"> <span class="unlockBtnImg">&nbsp;</span>Unlock</button>
	                 </td>
	                 </c:if>
	             
	                 <td>
	                     <button class="ur_button" onClick="javascript:YAHOO.ur.file.properties.renameFile('${personalFile.id}');" 
	                         onmouseover="this.className='ur_buttonover';"
 		                     onmouseout="this.className='ur_button';"
 		                ><span class="reportEditBtnImg">&nbsp;</span>Rename</button>
	                 </td>

	                 <td>
	                     <button class="ur_button" id="change_owner" 
	                         onmouseover="this.className='ur_buttonover';"
 		                     onmouseout="this.className='ur_button';"
 		                >Change Owner</button>
	                 </td>	                 
	             </tr>
	          </table>
          
              <h3>Sharing</h3>
          
              <table class="simpleTable">
                  <thead>
                  <tr>    
	                  <th>
	                      Name
	                  </th>
	                  <th>
	                      User Name
	                  </th>
	                  <th>
	                      Email
	                  </th>
                  </tr>
                  </thead>
                  <tbody>
                      <c:forEach var="collaborator" varStatus="status" 
                         items="${personalFile.versionedFile.collaborators}">
                      <c:if test="${ (status.count % 2) == 0}">
                          <c:set value="even" var="rowType"/>
                      </c:if>
                      <c:if test="${ (status.count % 2) == 1}">
                          <c:set value="odd" var="rowType"/>
                      </c:if>
                      <tr>
                          <td class="${rowType}">${collaborator.collaborator.firstName} ${collaborator.collaborator.lastName}</td>
                          <td class="${rowType}">${collaborator.collaborator.username}</td>
                          <td class="${rowType}">${collaborator.collaborator.defaultEmail.email}</td>
                      </tr>
                  
                      </c:forEach>  
                  </tbody>  
              </table>
	     
              <h3>All File Versions</h3>
          
          
              <table class="simpleTable">
                  <thead>
                      <tr>    
	                      <th>
	                          Thumbnail
	                      </th>
	                      <th>
	                          File Version
	                      </th>
	                      <th>
	                          Checksum
	                      </th>
	                      <th>
	                          Created Date
	                      </th>
	                      <th>
	                          Created By
	                      </th>
                      </tr>
                  </thead>
                  <tbody>
                      <c:forEach var="version" varStatus="status" items="${personalFile.versionedFile.versions}">
                      <c:if test="${ (status.count % 2) == 0}">
                          <c:set value="even" var="rowType"/>
                      </c:if>
                      <c:if test="${ (status.count % 2) == 1}">
                          <c:set value="odd" var="rowType"/>
                      </c:if>
                      <tr>
                          <td class="${rowType}">
                          <ir:transformUrl irFile="${version.irFile}"  var="url" systemCode="PRIMARY_THUMBNAIL" download="true"/>
                              <c:if test='${url != null}'>
                              <img src="${url}"/>
                              </c:if>
                          </td>
                          <c:url var="personalFileDownloadUrl" value="/user/personalFileDownload.action">
	                          <c:param name="personalFileId" value="${personalFile.id}"/>
	                          <c:param name="versionNumber" value="${version.versionNumber}"/>
	                      </c:url>
                          <td class="${rowType}"><a href="${personalFileDownloadUrl}">${version.versionNumber}</a></td>
                          <td class="${rowType}">
                              <c:forEach var="fileInfoChecksum"
                                  items="${version.irFile.fileInfo.fileInfoChecksums}">
                                      ${fileInfoChecksum.checksum} - ${fileInfoChecksum.algorithmType}
                              </c:forEach>
                          
                          </td>
                          <td class="${rowType}">${version.irFile.fileInfo.createdDate}</td>
                          <td class="${rowType}">${version.versionCreator.username}</td>
                     </tr>
                     </c:forEach>  
                 </tbody>  
             </table>
          
             <c:url var="addNewFileVersion" value="/user/viewNewFileVersionUpload.action">
                 <c:param name="personalFileId" value="${personalFile.id}"/>
             </c:url>
             