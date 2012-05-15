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
 	
 	<ur:js src="page-resources/js/user/edit_group_workspace_project_page.js"/>
  </head>
    
   <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  
           

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body of the page -->
            <div id="bd">
                <input id="group_workspace_id" type ="hidden" value="${groupWorkspaceProjectPage.groupWorkspace.id}"/>
                <input id="group_workspace_project_page_id" type ="hidden" value="${groupWorkspaceProjectPage.id}"/>
                <c:url var="groupWorkspaceUrl" value="/user/workspace.action">
                   <c:param name="tabName" value="GROUP_WORKSPACE"/>
                   <c:param name="groupWorkspaceId" value="${groupWorkspaceProjectPage.groupWorkspace.id}"/>
                </c:url>
                
                <div id="group_workspace_project_page_status">
                <c:import url="edit_group_workspace_project_page_status.jsp"/>
	            </div>
                <!--  create the first column -->
	            <div class="yui-g">
	            <div class="yui-u first">
	                <div class="contentContainer">
	                    <div class="contentBoxTitle">
	                        <c:url value="/user/editGroupWorkspaceProjectPageDescription.action" var="editGroupWorkspaceProjectPageDescriptionUrl">
						            <c:param name="groupWorkspaceProjectPageId" value="${groupWorkspaceProjectPage.id}"/>
						    </c:url>
	                        <p><a href="${editGroupWorkspaceProjectPageDescriptionUrl}">Edit Description</a></p>
	                    </div>
	                    <div class="contentBoxContent">
                            <p>${groupWorkspaceProjectPage.description}</p>
                        </div>
                    </div>   
	                <div class="contentContainer">
	                    <div class="contentBoxTitle">
	                       <c:url value="/user/editGroupWorkspaceProjectPageMembers.action" var="editGroupWorkspaceProjectPageMembersUrl">
						            <c:param name="groupWorkspaceProjectPageId" value="${groupWorkspaceProjectPage.id}"/>
						    </c:url>
	                        <p><a href="${editGroupWorkspaceProjectPageMembersUrl}">Edit Members</a></p>
	                    </div>
	                    <div class="contentBoxContent">
	                        <table class="baseTable">
                            <c:forEach var="member" items="${groupWorkspaceProjectPage.membersByOrder}">
                               
                                   <tr>
			                           <td class="baseTableImage"> 
			                               <c:if test="${!empty(member.groupWorkspaceUser.user.researcher) && member.groupWorkspaceUser.user.researcher.isPublic}">
			                                   <c:if test="${ir:hasThumbnail(member.groupWorkspaceUser.user.researcher.primaryPicture)}">
			                                       <c:url var="url" value="/researcherThumbnailDownloader.action">
                                                       <c:param name="irFileId" value="${member.groupWorkspaceUser.user.researcher.primaryPicture.id}"/>
                                                       <c:param name="researcherId" value="${member.groupWorkspaceUser.user.researcher.id}"/>
                                                   </c:url>
                                                   <img  src="${url}"/>
                                               </c:if>
                                              
                                           </c:if>   
                                           <c:if test="${!ir:hasThumbnail(member.groupWorkspaceUser.user.researcher.primaryPicture)}">
                	                           <img class="noimage_size" src="${pageContext.request.contextPath}/page-resources/images/all-images/noimage.jpg" />
                                           </c:if>	 												
	                                   </td>   
	                                   <td>
	                                   <c:url var="editMemberUrl" value="/user/editGroupWorkspaceProjectPageMember.action">
                                           <c:param name="memberId" value="${member.id}"/>
                                       </c:url>
	                                   <p><a href="${editMemberUrl}">${member.groupWorkspaceUser.user.firstName}&nbsp;${member.groupWorkspaceUser.user.lastName}</a></p>
	                                   <p><strong>Title: </strong>${member.title}</p>
	                                   
	                                   <c:if test="${!empty(member.description)}" >
	                                       <p><strong>Description:</strong>${member.description}</p>
	                                   </c:if>
	                                   
	                                   <c:if test="${!empty(member.groupWorkspaceUser.user.researcher)  && member.groupWorkspaceUser.user.researcher.isPublic}">
	                                       <c:url var="researcherUrl" value="/viewResearcherPage.action">
                                               <c:param name="researcherId" value="${member.groupWorkspaceUser.user.researcher.id}"/>
                                           </c:url>
	                                       <strong><a href="${researcherUrl}">Researcher Page</a></strong>
	                                   </c:if>
	                                 
	                                   </td>
	                                   
	                                    
	                                   
	                               </tr>
	                               <tr>
	                                   <td colspan="2"><hr/></td>
	                               </tr>
	                               
                                
                            </c:forEach>
                            </table>
                        </div>
                    </div>
                 </div>
	             <!--  end the first column -->
	            
	             <div class="yui-u">
	                 <div class="contentContainer">
	                    <div class="contentBoxTitle">
	                        <c:url value="/user/editGroupWorkspaceProjectPageImages.action" var="editGroupWorkspaceProjectPageImagesUrl">
						        <c:param name="groupWorkspaceProjectPageId" value="${groupWorkspaceProjectPage.id}"/>
						    </c:url>
	                        <p><a href="${editGroupWorkspaceProjectPageImagesUrl}">Edit Images</a></p>
	                    </div>
	                    <div class="contentBoxContent">
                            <div id="group_workspace_project_page_image"> </div>
                        </div>
                    </div>  
                    <div class="contentContainer">
	                    <div class="contentBoxTitle">
	                    
	                        <c:url value="/user/editGroupWorkspaceProjectPageFileSystem.action" var="editGroupWorkspaceProjectPageFileSystemUrl">
						        <c:param name="groupWorkspaceProjectPageId" value="${groupWorkspaceProjectPage.id}"/>
						    </c:url>
	                        <p><a href="${editGroupWorkspaceProjectPageFileSystemUrl}">Edit Research/Publications</a></p>
	                    </div>
	                    <div class="contentBoxContent">
                            <p>
                            </p>
                        </div>
                    </div> 
	             </div>
            </div>
   
            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
        
               <!--  public page confirm dialog -->
       <div id="confirmPublicDialog" class="hidden">
          <div class="hd">Turn Group Workspace Project Page ON</div>
          <div class="bd">
              <p>This will make this page available to the public and search engines</p>
          </div>
       </div>
       <!--  end public page confirm dialog -->
       
        <!--  private page confirm dialog -->
       <div id="confirmPrivateDialog" class="hidden">
          <div class="hd">Turn Group Workspace Project Page OFF</div>
          <div class="bd">
              <p>This will make this project page hidden from the public</p>
          </div>
       </div>
       <!--  end private page confirm dialog -->
        
        
    </body>
</html>