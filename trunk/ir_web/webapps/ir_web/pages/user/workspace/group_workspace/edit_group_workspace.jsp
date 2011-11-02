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
 	<ur:js src="page-resources/js/user/user_edit_group_workspace.js"/>
 	<ur:js src="page-resources/js/user/invite_group_workspace_users.js"/>
</head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
      
        
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <input type="hidden" id="set_tab_name" name="set_tab_name"  value="${tabName}"/>
        <c:url var="workspace" value="/user/workspace.action">
            <c:param name="tabName" value="GROUP_WORKSPACE"/>
        </c:url>
        <c:url var="groupWorkspaceUrl" value="/user/workspace.action">
            <c:param name="tabName" value="GROUP_WORKSPACE"/>
            <c:param name="groupWorkspaceId" value="${groupWorkspace.id}"/>
       </c:url>
        <h3><a href="${workspace}">Group Workspaces</a> &gt; <a href="${groupWorkspaceUrl}">${groupWorkspace.name}</a></h3>
  
        <div id="bd">
            <div id="groupWorkspacePropertiesTabs" class="yui-navset">
	             <ul class="yui-nav">
                     <li class="selected"><a href="#tab1"><em>Group Workspace Information</em></a></li>
                     <li><a href="#tab2"><em>Invite User(s)</em></a></li>
                     <li><a href="#tab3"><em>User(s)</em></a></li>
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
                          
                             <br/>
                             <br/>
                             
                             <!--  create the grid -->
                             <div >
                                <div class="contentContainer">
			                        <div class="contentBoxTitle">
			                            <p>User(s) to invite</p>
			                        </div> 
			                        <form id="addInvite" name="newInviteForm" method="POST" action="<c:url value="/user/inviteGroupWorkspaceUsers.action" />">
			                            <input type="hidden" name="groupWorkspaceId" value="${groupWorkspace.id}"/>	           
		                                <div class="contentBoxContent">
					                        <table class="formTable">
						                        <tr>
							                        <td colspan="2">  <div id="inviteUserError" class="errorMessage"></div> </td>
							                    </tr>
							                    <tr> 
							                        <td></td>
							                        <td> Separate Emails by semicolons (;) </td>
							                    </tr>
							                    <tr> 
							                        <td> Email(s)</td>
								                    <td> <textarea name="emails" id="newInviteForm_inviteEmail" cols="52" rows="5"></textarea></td>
						                       </tr>
							                   <tr>
							                       <td>Message</td>
							                       <td>
							                           <textarea name="inviteMessage" id="newInviteForm_inviteMessage" cols="52" rows="8"></textarea>
							                       </td>
							                   </tr>
							                   <c:forEach var="classTypePermission" items="${classTypePermissions}">
										       <tr>
												  <td>  <input type="checkbox" name="selectedPermissions" id="${classTypePermission.name}" value="${classTypePermission.id}" 
												      onclick="YAHOO.ur.group_workspace_invite.autoCheckPermission(this, selectedPermissions);"/> </td>
										          <td> ${classTypePermission.description}</td>
											   </tr>
			           				           </c:forEach>
			           				           <tr>
			           				               <td>
			           				                   <input type="checkbox" name="setAsOwner" id="owner" value="true" 
			           				                          onclick="YAHOO.ur.group_workspace_invite.setOwner(this, selectedPermissions);"/>
			           				               </td>
			           				               <td>
			           				                   Add as group workspace owner
			           				               </td>
			           				           </tr>
							                   <tr>
							                       <td colspan="2" align="center"> 
								                       <button class="ur_button" id="inviteUser" type="button"
							                               onmouseover="this.className='ur_buttonover';"
							                               onmouseout="this.className='ur_button';"
								                           onclick="javascript: YAHOO.ur.group_workspace_invite.submitInviteForm();"
							                           >Send Invite</button>
                              	                    </td>
						                       </tr>
							               </table>
			                           </div>
			                       </form>
			                     </div>
                             </div>
                     </div>
                     <!--  end second tab -->
                     
                     
                     <!--  third tab -->
                     <div id="tab3">
                          
                             <br/>
                             <br/>
                             
                            
                            <div id="workspace_owners">
                                <h3>Current Members</h3>
                                <c:import url="group_workspace_users_table.jsp"/>
                            </div>
                                
                            <div id="workspace_owners_invite">
                                <h3>Waiting for Users to Create Account</h3>
                                <c:import url="group_workspace_invites_table.jsp"/>
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
  
  
 	   <form id="removeUserConfirmationForm" name="removeUserForm" 
		     method="post" action="<c:url value="/user/removeGroupWorkspaceUser.action"/>">
		     <input type="hidden" id="removeUserFormUserId" name="removeUserId" value="">
		     <input type="hidden" id="removeUserFormGroupWorkspaceId" name="groupWorkspaceId" value="${groupWorkspace.id}">
	   </form>	
	   
	   <!--  start remove user dialog -->
	   <div id="removeUserConfirmDialog" class="hidden">
			     <div class="hd">Remove user?</div>
			     <div class="bd">
			        <p>Do you want to remove the user from the group?</p>
			     </div>
		</div>
		<!--  end remove user dialog -->
	   
	   
	   <form id="removeInviteConfirmationForm" name="removeInviteForm" 
		     method="post" action="<c:url value="/user/removeGroupWorkspaceInvite.action"/>">
		     <input type="hidden" id="removeInviteFormInviteId" name="inviteId" value="">
		     <input type="hidden" id="removeInviteFormGroupWorkspaceId" name="groupWorkspaceId" value="${groupWorkspace.id}">
	   </form>	
	   
	   <!--  start remove invite dialog -->
	   <div id="removeInviteConfirmDialog" class="hidden">
			     <div class="hd">Remove invite?</div>
			     <div class="bd">
			        <p>Do you want to remove the invite to the group?</p>
			     </div>
		</div>
		<!--  end remove invite dialog -->
 

</body>
</html>