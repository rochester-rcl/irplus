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

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>User Groups</title>
    
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
    <ur:js src="page-resources/js/admin/edit_user_group.js"/>
</head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
      
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <h2>Edit User Groups</h2>
  
        <div id="bd">
      
            <!--  set up tabs for the user group -->
	        <div id="user-group-tabs" class="yui-navset">
	             <ul class="yui-nav">
                     <li class="selected"><a href="#tab1"><em>User Group</em></a></li>
                     <li><a href="#tab2"><em>Group Memebers</em></a></li>
                     <li><a href="#tab3"><em>Group Administrators</em></a></li>
                 </ul>
                 <c:url var="updateUserGroupUrl" value="/admin/updateUserGroup.action"/>
					
				 <c:if test='${!(ir:userHasRole("ROLE_ADMIN", "OR") || ir:isAdminOfGroup(user,userGroup))}'>
				 	<div class="clear">&nbsp;</div>
				 	<div class="errorMessage"> Group members/administrators can be added or removed only by Group admin or System admin. </div> 
				 </c:if>

                 <div class="yui-content">
                     <!--  Start first tab -->
                     <div id="tab1">
                         <form id="base_user_group_information" name="userGroupInformation" method="post" 
                             action="${updateUserGroupUrl}">
                            <input type="hidden" id="id" 
                                  name="id" value="${userGroup.id}" />
                            <table class="formTable">
                                <tr>
                                    <td class="label">
                                    <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                                 key="userGroupAlreadyExists"/></p>    
                                        <strong>Name:</strong>
                                    </td>
                                    <td class="input">
                                        <input type="text" size="75" name="userGroup.name" 
                                               value="${userGroup.name}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label">
                                        <strong>Description</strong>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="input" colspan="2">
                                        <textarea name="userGroup.description"
                                        rows="20" cols="75">${userGroup.description}</textarea>
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
                     <!--  End first tab -->
	                 
	                 <!--  Start second tab -->
                     <div id="tab2">
                         <table width="100%">
							<c:if test='${ir:userHasRole("ROLE_ADMIN", "OR") || ir:isAdminOfGroup(user,userGroup)}'>
	                             <tr>
	                                 <td></td>
	                                 <td>
	                                     <form method="GET" id="user_search_form" name="userSearchForm" 
	                                         action="javascript:YAHOO.ur.usergroup.edit.userSearch(0, 1, 1);">
	                                         Search: <input type="text" size="50" id="user_query" name="query" value=""/>
	                                         <input type="hidden" name="id" value="${userGroup.id}"/>
	                                     </form>
	                                 </td>
	                             </tr>
	                         </c:if>
                             <tr>
                                 <td><br/><br/></td>
                             </tr>
                             <tr>
                                 <td valign="top">
                                    <div id="group_members_div">
                                        <c:import url="user_group_users_frag.jsp"></c:import>
                                    </div>
                                 </td>
                                 <td valign="top">
                                     <div id="users_search_results_div"></div>
                                 </td>
                             </tr>
                         </table>
	                 </div>
	                 <!--  End second tab -->
	                 
	                 <!--  Start third tab -->
                     <div id="tab3">
                         <table width="100%">
                         	<c:if test='${ir:userHasRole("ROLE_ADMIN", "OR") || ir:isAdminOfGroup(user,userGroup)}'>
	                             <tr>
	                                 <td></td>
	                                 <td>
	                                     <form method="GET" id="admin_search_form" name="adminSearchForm" 
	                                         action="javascript:YAHOO.ur.usergroup.edit.adminSearch(0, 1, 1);">
	                                         Search: <input type="text" size="50" id="admin_query" name="query" value=""/>
	                                         <input type="hidden" name="id" value="${userGroup.id}"/>
	                                     </form>
	                                 </td>
	                             </tr>
                             </c:if>
                             <tr>
                                 <td><br/><br/></td>
                             </tr>
                             <tr>
                                 <td valign="top">
                                    <div id="group_admins_div">
                                        <c:import url="user_group_admins_frag.jsp"></c:import>
                                    </div>
                                 </td>
                                 <td valign="top">
                                     <div id="admin_search_results_div"></div>
                                 </td>
                             </tr>
                         </table>
	                 </div>
	                 <!--  End third tab -->
	             </div>
                 <!-- end tabs -->
	        
      </div>
      <!--  end body div -->
      
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End doc div-->
  
 
</body>
</html>