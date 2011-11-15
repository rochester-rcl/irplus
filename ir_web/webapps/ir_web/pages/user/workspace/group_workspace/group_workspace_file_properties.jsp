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
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Group Workspace: ${groupSpace.name}</title>
    
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>

    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    <ur:styleSheet href="page-resources/css/home_page_content_area.css"/> 
    
    <!--  Style for dialog boxes -->
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	<ur:js src="page-resources/yui/tabview/tabview-min.js"/>
 	
 	
 	<ur:js src="page-resources/js/util/base_path.jsp"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
</head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
        
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
        
        <c:url var="workspaceUrl" value="/user/workspace.action">
            <c:param name="tabName" value="GROUP_WORKSPACE"/>
        </c:url>
        <c:url var="groupWorkspaceUrl" value="/user/workspace.action">
            <c:param name="tabName" value="GROUP_WORKSPACE"/>
            <c:param name="groupWorkspaceId" value="${groupWorkspaceFile.groupWorkspace.id}"/>
        </c:url>
        <div id="bd">
            <strong>Path:&nbsp;/
	            <span class="groupImg">&nbsp;</span>
	            <a href="${workspaceUrl}">Group Workspaces</a> /
	           
	            <span class="groupImg">&nbsp;</span>
                <a href="${groupWorkspaceUrl}">${groupWorkspaceFile.groupWorkspace.name}</a>&nbsp;/
              
	    
                 <c:forEach var="currentFolder" items="${folderPath}">
                      <c:url var="folderUrl" value="/user/workspace.action">
                          <c:param name="tabName" value="GROUP_WORKSPACE"/>
                          <c:param name="groupWorkspaceId" value="${groupWorkspaceFile.groupWorkspace.id}"/>
                          <c:param name="groupWorkspaceFolderId" value="${currentFolder.id}"/>
                      </c:url>
                      <span class="folderBtnImg">&nbsp;</span>
                           <a href="${folderUrl}">${currentFolder.name}</a>&nbsp;/
                 </c:forEach>
                  ${groupWorkspaceFile.versionedFile.nameWithExtension}
            </strong>
            <br/><br/>
            <h3>File Properties: ${groupWorkspaceFile.versionedFile.nameWithExtension}</h3>
            
            <h3>Current Version Information</h3>
              <table class="table">
                  <tr>
                      <td><strong>Editing Status:&nbsp;&nbsp;&nbsp;</strong></td>
                      <td><c:if test="${groupWorkspaceFile.versionedFile.locked}">
                              <span class="lockBtnImg">&nbsp;</span> File Locked by ${groupWorkspaceFile.versionedFile.lockedBy.username}
                          </c:if>
                          <c:if test="${!groupWorkspaceFile.versionedFile.locked}">
                              <span class="unlockBtnImg">&nbsp;</span> File Unlocked 
                          </c:if>
                      </td>
                  </tr>
                  <tr>
                      <td><strong>Name:&nbsp;</strong></td>
                      <td>${groupWorkspaceFile.versionedFile.nameWithExtension}</td>
                  </tr>
                  <tr>
                      <td><strong>Version:&nbsp;</strong></td>
                      <td>${groupWorkspaceFile.versionedFile.currentVersion.versionNumber}</td>
                  </tr>
                  <tr>
                      <td><strong>Created Date:&nbsp;</strong></td>
                      <td>${groupWorkspaceFile.versionedFile.currentVersion.irFile.fileInfo.createdDate}</td>
                  </tr>
                  <tr>
                      <td><strong>File Owner:&nbsp;</strong></td>
                      <td>${groupWorkspaceFile.versionedFile.owner.firstName}&nbsp;${groupWorkspaceFile.versionedFile.owner.lastName}</td>
                  </tr>
                  <tr>
                      <td><strong>Created By:&nbsp;</strong></td>
                      <td>${groupWorkspaceFile.versionedFile.currentVersion.versionCreator.username}</td>
                  </tr>
                   <tr>
                      <td><strong>Size:&nbsp;</strong></td>
                      <td><ir:fileSizeDisplay sizeInBytes="${groupWorkspaceFile.versionedFile.currentVersion.irFile.fileInfo.size}"/></td>
                  </tr>
                  <tr>
                      <td><strong>Size on Disk:&nbsp;</strong></td>
                      <td><ir:fileSizeDisplay sizeInBytes="${ir:infoSizeOnDisk(groupWorkspaceFile.versionedFile.currentVersion.irFile.fileInfo)}"/></td>
                  </tr>
                  <tr>
                      <td><strong>Path:&nbsp;</strong></td>
                      <td>${groupWorkspaceFile.versionedFile.currentVersion.irFile.fileInfo.fullPath}</td>
                  </tr>
                  <tr>
                      <td><strong>File Info Id:&nbsp;</strong></td>
                      <td>${groupWorkspaceFile.versionedFile.currentVersion.irFile.fileInfo.id}</td>
                  </tr>
                  <tr>
                      <td><strong>Checksums:&nbsp;</strong></td>
                      <td>
                          <c:forEach var="fileInfoChecksum"
                              items="${groupWorkspaceFile.versionedFile.currentVersion.irFile.fileInfo.fileInfoChecksums}">
                                  ${fileInfoChecksum.checksum} - ${fileInfoChecksum.algorithmType}
                          </c:forEach>
                      </td>
                  </tr>
              </table>
            
            
             <h3>User Permissions</h3>
            <div class="dataTable">
            <urstb:table width="100%">
                <urstb:thead>
                    <urstb:tr>
                        <urstb:td>User</urstb:td>
                        <urstb:td>Owner</urstb:td>
                        <urstb:td>Edit</urstb:td>
                        <urstb:td>Read</urstb:td>
                    </urstb:tr>
                </urstb:thead>
                <urstb:tbody
                    var="entry" 
                    oddRowClass="odd"
                    evenRowClass="even"
                    currentRowClassVar="rowClass"
                    collection="${fileAcl.userEntries}">
                    <urstb:tr 
                            cssClass="${rowClass}"
                            onMouseOver="this.className='highlight'"
                            onMouseOut="this.className='${rowClass}'">
                            
                            <urstb:td>
                                <c:if test="${entry.sid.researcher.primaryPicture != null }">
                                    <c:url var="url" value="/researcherThumbnailDownloader.action">
                                        <c:param name="irFileId" value="${entry.sid.researcher.primaryPicture.id}"/>
                                        <c:param name="researcherId" value="${entry.sid.researcher.id}"/>
                                    </c:url>
                                    <img class="basic_thumbnail" src="${url}"/>
                                   
                                </c:if>
                                
                                 <c:if test="${entry.sid.researcher.primaryPicture == null}">
                	                 <img class="basic_thumbnail" src="${pageContext.request.contextPath}/page-resources/images/all-images/noimage.jpg" class="noimage_size"/>
                                 </c:if>	
                                  <br/> 
                                ${entry.sid.firstName}&nbsp;${entry.sid.lastName}
                            </urstb:td>

                            <urstb:td>
                                <c:if test="${ entry.sid.id == groupWorkspaceFile.versionedFile.owner.id}"> Yes </c:if>
                                <c:if test="${ entry.sid.id != groupWorkspaceFile.versionedFile.owner.id}"> No </c:if>
                            </urstb:td>
                            
                            <urstb:td>
                                <c:if test='${ir:entryHasPermission(entry, "EDIT")}'>Yes</c:if>
                                <c:if test='${ !ir:entryHasPermission(entry, "EDIT")}'>No</c:if>
                            </urstb:td>

                            <urstb:td>
                               <c:if test='${ir:entryHasPermission(entry, "VIEW")}'>Yes</c:if>
                               <c:if test='${ !ir:entryHasPermission(entry, "VIEW")}'>No</c:if>
                            </urstb:td>

                          
                        
                    </urstb:tr>
                </urstb:tbody>
            </urstb:table>
            </div>    
            
            
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
                      collection="${groupWorkspaceFile.versionedFile.versions}">
                      <urstb:tr 
                          cssClass="${rowClass}"
                          onMouseOver="this.className='highlight'"
                          onMouseOut="this.className='${rowClass}'">
                          <urstb:td>
                             <c:if test='${ir:hasThumbnail(version.irFile)}'>
                                  <c:url var="url" value="/user/groupWorkspaceFileThumbnailDownloader.action">
                                      <c:param name="groupWorkspaceFileId" value="${groupWorkspaceFile.id}"/>
                                      <c:param name="versionNumber" value="${version.versionNumber}"/>
                                  </c:url>
                                  <img class="basic_thumbnail" src="${url}"/>
                             </c:if>
                          </urstb:td>
                        
                          <urstb:td>
                              <c:url var="groupWorkspaceFileDownloadUrl" value="/user/groupWorkspaceFileDownload.action">
	                              <c:param name="groupWorkspaceFileId" value="${groupWorkspaceFile.id}"/>
	                              <c:param name="versionNumber" value="${version.versionNumber}"/>
	                          </c:url>
	                          <a href="${groupWorkspaceFileDownloadUrl}">${version.irFile.nameWithExtension}</a>
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
            
        </div>
        <!--  end body div -->
      
        <!--  this is the footer of the page -->
        <c:import url="/inc/footer.jsp"/>
  
   </div>
   <!--  End doc div-->
 
</body>
</html>