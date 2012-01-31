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
                <h3> <a href="${editProjectPageUrl}">${groupWorkspaceProjectPage.groupWorkspace.name} </a> &gt; Edit Members </h3>
                 <!--  create the first column -->
	            <div class="yui-g">
	                <div class="yui-u first">
	                    <h3>Group Workspace Members</h3>
	                    <!-- Table for members in the workspace  -->            
                        <table class="itemFolderTable" width="100%">
	                        <thead>
		                        <tr>
			                        <th class="thItemFolder" width="20%">Add</th>
			                        <th class="thItemFolder">Member</th>
		                        </tr>
	                        </thead>
	                        <tbody>
		                        <c:forEach var="member" items="${groupWorkspaceProjectPage.groupWorkspace.users}">
			                    <tr>
				                    <td class="tdItemFolderLeftBorder">
				                       
				                        <c:if test="${!member.showOnProjectPage}">
				                            <c:url value="/user/addGroupWorkspaceProjectPageMember.action" var="addMemberUrl">
						                        <c:param name="groupWorkspaceProjectPageId" value="${groupWorkspaceProjectPage.id}"/>
						                        <c:param name="groupWorkspaceUserId" value="${member.id}"/>
						                    </c:url>
			                                <span class="addBtnImg">&nbsp;</span><a href="${addMemberUrl}"> Add</a> 
			                            </c:if>
			                             <c:if test="${member.showOnProjectPage}">
			                                 Added
			                             </c:if>
				                    </td>
				
				                    <td class="tdItemFolderRightBorder">
	                 	               ${member.user.firstName} &nbsp; ${member.user.lastName}
				                    </td>
			                    </tr>
		                        </c:forEach>
	                        </tbody>
                        </table>
	                </div>
	                <!--  end the first column -->
	            
	                <div class="yui-u">
	                    <h3>Currently On Project Page</h3>
	                    <!-- Table for members in the workspace  -->            
                        <table class="itemFolderTable" width="100%">
	                        <thead>
		                        <tr>
			                        <th class="thItemFolder" width="20%">Add</th>
			                        <th class="thItemFolder">Member</th>
		                        </tr>
	                        </thead>
	                        <tbody>
		                        <c:forEach var="member" items="${groupWorkspaceProjectPage.groupWorkspace.users}">
			                    <c:if test="${member.showOnProjectPage}">
			                        <c:url value="/user/removeGroupWorkspaceProjectPageMember.action" var="removeMemberUrl">
						                <c:param name="groupWorkspaceProjectPageId" value="${groupWorkspaceProjectPage.id}"/>
						                <c:param name="groupWorkspaceUserId" value="${member.id}"/>
						            </c:url>
			                    <tr>
				                    <td class="tdItemFolderLeftBorder">
			                            <span class="deleteBtnImg">&nbsp;</span><a href="${removeMemberUrl}"> Remove </a> 
				                    </td>
				
				                    <td class="tdItemFolderRightBorder">
	                 	               <a href="">${member.user.firstName} &nbsp; ${member.user.lastName}</a>
				                    </td>
			                    </tr>
			                    </c:if> 
		                        </c:forEach>
	                        </tbody>
                        </table>
                    </div>
                </div>
   
            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
        
        
    </body>
</html>