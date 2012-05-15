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
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ir" uri="ir-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
<fmt:setBundle basename="messages"/>
<html>
  <head>
    <title> Edit Project Page:&nbsp;${groupWorkspaceProjectPage.groupWorkspace.name}</title>
    <c:import url="/inc/meta-frag.jsp"/>
        
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>
    
    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/home_page_content_area.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    <ur:styleSheet href="page-resources/css/tree.css"/>
    
	<ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	<ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/tabview/tabview-min.js"/>
        
 	<ur:js src="page-resources/js/util/base_path.jsp"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/util/wait_dialog.js" />
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
 	<ur:js src="page-resources/js/util/ur_table.js"/>
 	
 	<ur:js src="page-resources/js/user/edit_group_workspace_project_page_file_system.js"/>
 	
  </head>
    
   <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  
           

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body of the page -->
            <div id="bd">
                <c:url value="/user/editGroupWorkspaceProjectPage.action" var="editProjectPageUrl">
				          <c:param name="groupWorkspaceProjectPageId" value="${groupWorkspaceProjectPage.id}"/>
				      </c:url>
                      <h3> <a href="${editProjectPageUrl}">${groupWorkspaceProjectPage.groupWorkspace.name} </a> &gt; Edit File System </h3>
               
                <div id="fileSystemTable">
                    <c:import url="group_workspace_project_page_file_system_table.jsp"/>
                </div>
            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
        <div id="newFolderDialog" class="hidden">
            <div class="hd">Folder Information</div>
            <div class="bd">
                  <form id="addFolder" name="newFolderForm" 
                  method="post" >
              
                   <input type="hidden" id="newFolderForm_parentFolderId"
                       name="parentFolderId" value="${parentFolderId}"/>
                       
                   <input type="hidden" id="newFolderForm_projectPageId"
                       name="projectPageId" value="${groupWorkspaceProjectPage.id}"/>                       
               
                   <input type="hidden" id="newFolderForm_new"
                       name="newFolder" value="true"/>
              
                   <input type="hidden" id="newfolderForm_folderId"
                       name="updateFolderId" value=""/>
                   
                  <div id="folderNameError" class="errorMessage"></div>
	              
	              <table class="formTable">
	                  <tr>
	                      <td align="left"  class="label"> Folder Name:*</td>
	                      <td align="left" class="input"> <input id="folder" type="text" name="folderName" value="" size="45"/></td>
	                  </tr>
	                  <tr>
	                      <td align="left"  class="label"> Folder Description:</td>
	                      <td align="left" class="input" colspan="2" ><textarea cols="42" rows="4" name="folderDescription"></textarea></td>
	                  </tr>
	              </table>
                 </form>
           </div>
           <!-- end dialog body -->
       </div>
       
       <!--  delete folder dialog -->
       <div id="deleteFileFolderConfirmDialog" class="hidden">
          <div class="hd">Remove from Group Workspace Page?</div>
          <div class="bd">
              <p>Do you want to remove the selected items from your group workspace project page?</p>
          </div>
       </div>
       <!--  end delete folder dialog -->
       
       <div id="newLinkDialog" class="hidden">
            <div class="hd">Link Information</div>
            <div class="bd">
                  <form id="addLink" name="newLinkForm" 
                  method="post" action="<c:url value="/user/addGroupWorkspaceProjectPageFileSystemLink.action"/>">
                     <input type="hidden" id="newLinkForm_parentFolderId" name="parentFolderId" value="${parentFolderId}"/>
                     <input type="hidden" id="newLinkForm_groupWorkspaceProjectPageId" name="groupWorkspaceProjectPageId" 
                         value="${groupWorkspaceProjectPage.id}"/>                       
                     <div id="researcherLinkFields">
                         <c:import url="group_workspace_project_page_file_system_link_form.jsp"/>
                     </div>
                 </form>
           </div>
           <!-- end dialog body -->
       </div>
       <!--  end the new link dialog -->
        
    </body>
</html>