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
 	
  </head>
    
   <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  
           

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body of the page -->
            <div id="bd">
                <c:url var="groupWorkspaceUrl" value="/user/workspace.action">
                   <c:param name="tabName" value="GROUP_WORKSPACE"/>
                   <c:param name="groupWorkspaceId" value="${groupWorkspaceProjectPage.groupWorkspace.id}"/>
                </c:url>
                <h3>Project Page:&nbsp;<a href="${groupWorkspaceUrl}">${groupWorkspaceProjectPage.groupWorkspace.name}</a>
                 
                   <input type="radio" id="researcher_page_off" name="isPublic" onclick="javascript:YAHOO.ur.edit.researcher.confirmPrivateDialog.showDialog();"
	               <c:if test="${!groupWorkspaceProjectPage.pagePublic}">
	                   checked
	               </c:if>
	               /> <c:if test="${!groupWorkspaceProjectPage.pagePublic}"><span class="errorMessage">OFF</span></c:if> <c:if test="${groupWorkspaceProjectPage.pagePublic}">OFF</c:if>
	               &nbsp;    	
	               <input type="radio" id="researcher_page_on" name="isPublic" onclick="javascript:YAHOO.ur.edit.researcher.confirmPublicDialog.showDialog();"
	                   <c:if test="${groupWorkspaceProjectPage.pagePublic}">
	            			checked
	                   </c:if>
	                /><c:if test="${groupWorkspaceProjectPage.pagePublic}"><span class="greenMessage">ON</span></c:if> <c:if test="${!groupWorkspaceProjectPage.pagePublic}">ON</c:if>
                </h3>
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
			                               <c:if test="${!empty(member.groupWorkspaceUser.user.researcher) && member.groupWorkspaceUser.user.researcher.public}">
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
	                                   
	                                   <c:if test="${!empty(member.groupWorkspaceUser.user.researcher)  && member.groupWorkspaceUser.user.researcher.public}">
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
	                        <p><a href="">Edit Images</a></p>
	                    </div>
	                    <div class="contentBoxContent">
                            
                        </div>
                    </div>  
                    <div class="contentContainer">
	                    <div class="contentBoxTitle">
	                        <p><a href="">Edit Research/Publications</a></p>
	                    </div>
	                    <div class="contentBoxContent">
                            
                        </div>
                    </div> 
	             </div>
            </div>
   
            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
        
        
    </body>
</html>