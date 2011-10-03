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
    <title>Invite user(s)</title>
    
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

    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	<ur:js src="page-resources/yui/tabview/tabview-min.js"/>

    <!-- Source File -->
    <ur:js src="page-resources/js/menu/main_menu.js"/>
 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/user/invite_group_workspace_users.js"/>
 
  
</head>



<body  class="yui-skin-sam">
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
      
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <div id="bd">
			<h3><a href="">Group Workspaces</a> &gt; <a href="">${groupWorkspace.name}</a></h3>
			<h3> Invite user(s) to Group Workspace: ${groupWorkspace.name} </h3>
			
			
			<!--  create the first column -->
			<div class="yui-g">
			    <div class="yui-u first">
			        <div class="contentContainer">
			            <div class="contentBoxTitle">
			                <p>User(s) to invite</p>
			            </div> 
			            <form id="addInvite" name="newInviteForm" method="POST" action="<c:url value="/user/inviteGroupWorkspaceUsers.action" />">
			                <input type="hidden" name="groupWorkspaceId" value="${groupWorkspaceId}"/>	           
		                    <div class="contentBoxContent">
			                    <p>
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
								          
				                </p>
			                </div>
			            </form>
			        </div>
			    </div>
			    <div class="yui-u">
			        <div class="contentContainer">
			            <div class="contentBoxTitle">
			                <p>Members/Invited Users</p>
			            </div>
			             <div class="contentBoxContent">
			                    
			                       <h3> Current Group Members </h3>
			                       <c:forEach var="member" items="${groupWorkspace.users}">
			                           ${member.user.firstName}&nbsp;${member.user.lastName}
			                           <c:if test="${member.owner}">
			                               [owner]
			                           </c:if>
			                           <br/>
			                       </c:forEach>
			                       
			                       <h3> Invited Repository Members </h3>
			                       
			                       <c:forEach var="invite" items="${groupWorkspace.existingUserInvites}">
			                           ${invite.invitedUser.firstName}&nbsp;${invite.invitedUser.lastName}
			                           <c:if test="${invite.setAsOwner}">
			                               [owner]
			                           </c:if>
			                            <br/>
			                       </c:forEach>
			                       
			                       <h3> Invited Non-Repository Members </h3>
			                       <c:forEach var="invite" items="${groupWorkspace.emailInvites}">
			                           ${invite.inviteToken.email}
			                           <c:if test="${invite.setAsOwner}">
			                               [owner]
			                           </c:if>
			                            <br/>
			                       </c:forEach>
			                    
			             </div>
			         </div>
			    </div>
			</div>
			                	            
	   </div>
	   <!-- end body tag -->
	   
	   <!--  this is the footer of the page -->
       <c:import url="/inc/footer.jsp"/>
    </div>
</body>
</html>