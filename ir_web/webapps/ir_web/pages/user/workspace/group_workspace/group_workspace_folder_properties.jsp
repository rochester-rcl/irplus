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
            <c:param name="groupWorkspaceId" value="${groupWorkspaceFolder.groupWorkspace.id}"/>
        </c:url>
  
        <div id="bd">
            <strong>Path:&nbsp;/
	            <span class="groupImg">&nbsp;</span>
	            <a href="${workspaceUrl}">Group Workspaces</a> /
	           
	            <span class="groupImg">&nbsp;</span>
                <a href="${groupWorkspaceUrl}">${groupWorkspaceFolder.groupWorkspace.name}</a>&nbsp;/
              
	    
                 <c:forEach var="currentFolder" items="${folderPath}">
                      <c:url var="folderUrl" value="/user/workspace.action">
                          <c:param name="tabName" value="GROUP_WORKSPACE"/>
                          <c:param name="groupWorkspaceId" value="${groupWorkspaceFolder.groupWorkspace.id}"/>
                          <c:param name="groupWorkspaceFolderId" value="${currentFolder.id}"/>
                      </c:url>
                      <span class="folderBtnImg">&nbsp;</span>
                           <a href="${folderUrl}">${currentFolder.name}</a>&nbsp;/
                 </c:forEach>
            </strong>
            <br/><br/>
           
            <h3> Folder Properties </h3>
            
           
            <h3>User Permissions</h3>
            <div class="dataTable">
            <urstb:table width="100%">
                <urstb:thead>
                    <urstb:tr>
                        <urstb:td>User</urstb:td>
                        <urstb:td>Owner</urstb:td>
                        <urstb:td>Edit</urstb:td>
                        <urstb:td>Add File</urstb:td>
                        <urstb:td>Read</urstb:td>
                    </urstb:tr>
                </urstb:thead>
                <urstb:tbody
                    var="entry" 
                    oddRowClass="odd"
                    evenRowClass="even"
                    currentRowClassVar="rowClass"
                    collection="${folderAcl.userEntries}">
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
                                <c:if test="${ entry.sid.id == groupWorkspaceFolder.owner.id}"> Yes </c:if>
                                <c:if test="${ entry.sid.id != groupWorkspaceFolder.owner.id}"> No </c:if>
                            </urstb:td>
                            
                            <urstb:td>
                                <c:if test='${ir:entryHasPermission(entry, "GROUP_WORKSPACE_FOLDER_EDIT")}'>Yes</c:if>
                                <c:if test='${ !ir:entryHasPermission(entry, "GROUP_WORKSPACE_FOLDER_EDIT")}'>No</c:if>
                            </urstb:td>

                            <urstb:td>
                               <c:if test='${ir:entryHasPermission(entry, "GROUP_WORKSPACE_FOLDER_ADD_FILE")}'>Yes</c:if>
                               <c:if test='${ !ir:entryHasPermission(entry, "GROUP_WORKSPACE_FOLDER_ADD_FILE")}'>No</c:if>
                            </urstb:td>

                            <urstb:td>
                               <c:if test='${ir:entryHasPermission(entry, "GROUP_WORKSPACE_FOLDER_READ")}'>Yes</c:if>
                               <c:if test='${ !ir:entryHasPermission(entry, "GROUP_WORKSPACE_FOLDER_READ")}'>No</c:if>
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