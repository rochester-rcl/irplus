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
<%@ taglib prefix="ir" uri="ir-tags"%>
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
 		
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
 	<ur:js src="page-resources/js/user/user_edit_group_workspace.js"/>
</head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
      
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <c:url var="workspace" value="/user/workspace.action">
            <c:param name="tabName" value="GROUP_WORKSPACE"/>
        </c:url>
        <h3><a href="${workspace}">Group Workspaces</a> <a href=""></a> &gt; Editing: ${groupWorkspace.name}</h3>
  
        <div id="bd">
            <div id="groupWorkspacePropertiesTabs" class="yui-navset">
	             <ul class="yui-nav">
                     <li class="selected"><a href="#tab1"><em>Group  Information</em></a></li>
                     <li><a href="#tab2"><em>Owner(s)</em></a></li>
                     <li ><a href="#tab3"><em>Group(s)</em></a></li>
                 </ul>

                 <div class="yui-content">
                     <!--  first tab -->
                     <div id="tab1">
                         <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                                    key="groupWorkspace.name"/></p>
		                 <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                                      key="groupWorkspaceAlreadyExists"/></p>
                         <form id="editGroupWorkspaceInformation" name="groupWorkspaceInformation" method="post" 
                             action="<c:url value="/user/updateGroupWorkspace.action"/>">
                            <input type="hidden" id="groupWorkspaceId" 
                                  name="id" value="${groupWorkspace.id}" />
                            <table class="formTable">
                                <tr>
                                    <td class="label">
                                        <strong>Name:</strong>
                                    </td>
                                    <td class="input">
                                        <input type="text" size="75" name="name" 
                                               value="<c:out value='${groupWorkspace.name}'/>"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label">
                                        <strong>Description</strong>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="input" colspan="2">
                                        <textarea name="description" 
                                        rows="20" cols="75"><c:out value='${groupWorkspace.description}'/></textarea>
                                    </td>
                                </tr>
                                <tr>
                                   <td>
                                       <input type="submit"  value="save"/>
                                   </td>
                                </tr>
                            </table>
                       </form>
                     </div>
                     <!--  end first tab -->
                     
                     <!--  second tab -->
                     <div id="tab2">
                           <c:if test='${ir:userHasRole("ROLE_ADMIN", "OR") || ir:isAdminOfGroup(user,userGroup)}'>
	                            <form method="GET" id="admin_search_form" name="adminSearchForm" 
	                                         action="javascript:YAHOO.ur.usergroup.edit.adminSearch(0, 1, 1);">
	                                         Search: <input type="text" size="50" id="admin_query" name="query" value=""/>
	                                         <input type="hidden" name="id" value="${userGroup.id}"/>
	                                     </form>
                             </c:if>
                             <br/>
                             <br/>
                             
                             <!--  create the grid -->
                             <div class="yui-g">
                                 <!--  create the first column -->
                                 <div class="yui-u first">
                                     <div id="workspace_owners">
                                         <c:import url="group_workspace_owners_table.jsp"/>
                                     </div>
                                 </div>
                                 <div class="yui-u">
                                     <div id="workspace_owners_invite">
                                     </div>
                                 </div>
                             </div>
                     </div>
                     <!--  end second tab -->
                     
                     <!--  third tab -->
                     <div id="tab3">
                         <div id="workspace_groups">
                             <br/>
                                 <div align="right">
	                             <button class="ur_button" 
 		                             onmouseover="this.className='ur_buttonover';"
 		                             onmouseout="this.className='ur_button';"
 		                             onClick="javascript:YAHOO.ur.user.workspace_group.newWorkspaceGroupDialog.showDialog()"><span class="groupAddBtnImg">&nbsp;</span>New Group</button> 
                                 </div>
                            
                             <br/>
                             <div id="workspace_groups_table">
                                 <c:import url="workspace_groups_table.jsp"/>
                             </div>
                         </div>
                     </div>
                     <!--  end third tab -->
                 </div>
                 
            </div>
        
        </div>
        <!--  end body div -->
      
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End doc div-->
  
  
  <!-- Dialog box for deleting a workspace group-->
  <div id="deleteWorkspaceGroupDialog" class="hidden">
      <div class="hd">Delete Workspace Group</div>
		<div class="bd">
		    <form id="deleteWorkspaceGroupForm" name="deleteWorkspaceGroup" method="post" 
		                action="/user/deleteWorkspaceGroup.action">
			   <p>Are you sure you wish to delete the selected workspace group?</p>
			   <input type="hidden" id="deleteId" name="id" value=""/>
		    </form>
		</div>
  </div>
  <!-- Dialog box for deleting a workspace group -->
 
  <!-- Dialog box for creating a workspace group-->
  <div id="newWorkspaceGroupDialog" class="hidden">
       <div class="hd">Workspace Group Information</div>
       <div class="bd">
          <form id="addWorkspaceGroup" 
                            name="newWorkspaceGroupForm" 
		                    method="post"
		                    action="/user/createWorkspaceGroup.action">
	          <div id="workspaceGroupDialogFields">
	              <c:import url="workspace_group_form.jsp"/>
	          </div>
	      </form>
       </div>
  </div>
  <!--  end dialog for creating a workspace group--> 

</body>
</html>