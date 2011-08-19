
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ir" uri="ir-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Manage Users</title>
    
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>   
    <ur:styleSheet href="page-resources/css/base-ur.css"/>
    
    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <!--  required imports -->
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	<ur:js src="page-resources/yui/tabview/tabview-min.js"/>
 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
	<ur:js src="pages/js/ur_table.js"/>
	<ur:js src="page-resources/js/admin/user.js"/>
	
	<script type="text/javascript">

		  function updateUrl()
		  {
			  document.userRoleAffiliationType.submit();
		  }
    </script>
     
</head>

<body class=" yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
    
    <!--  this is the header of the page -->
    <c:import url="/inc/header.jsp"/>
    
    <h3>Manage Users </h3>
  
    <div id="bd">        
        <!--  set up tabs for the workspace -->
        <div id="user-tabs" class="yui-navset">
	         <ul class="yui-nav">
	             <li class="selected"><a href="#tab1"><em><u>Users</u></em></a></li>
	             <li><a href="#tab2"><em><u>Search</u></em></a></li>
	         </ul>
	
	        <!--  first tab -->
	        <div class="yui-content">
	            <div id="tab1">
	            	 
	            	  <br/>
					     <button id="showUser" class="ur_button" 
			 		         onmouseover="this.className='ur_buttonover';"
			 		         onmouseout="this.className='ur_button';">New User</button> 
				       <br/>
				       <br/>
				       <form name="userRoleAffiliationType" action="<c:url value="/admin/viewUsers.action"/>" method="get">
				             <input type="hidden" name="rowStart" value="0"/>
			                 <input type="hidden" name="startPageNumber" value="1"/>
			                 <input type="hidden" name="currentPageNumber" value="1"/>
			                 <input type="hidden" name="sortElement" value="${sortElement}"/>		
			                 <input type="hidden" name="sortType" value="${sortType}"/>	
			                 
				         Filter By Role:
				         <select name="roleId" onchange="javascript:updateUrl();">
				            <c:if test="${roleId == -1}">
				                <option  value="-1" selected="selected">Any</option>
				            </c:if>
				            <c:if test="${roleId != -1}">
				                <option value="-1">Any</option>
				            </c:if>
						    <c:forEach items="${roles}" var="role">
						        <c:if test="${role.id == roleId}">
						            <option  selected="selected" value="${role.id}">${role.name}</option>
						        </c:if>
						        <c:if test="${role.id != roleId}">
						            <option value="${role.id}">${role.name}</option>
						        </c:if>
						    </c:forEach>
						 </select>
						 and affiliation
						 <select name="affiliationId" onchange="javascript:updateUrl();">
				            <c:if test="${affiliationId == -1}">
				                <option  value="-1" selected="selected">Any</option>
				            </c:if>
				            <c:if test="${affiliationId != -1}">
				                <option value="-1">Any</option>
				            </c:if>
						    <c:forEach items="${affiliations}" var="affiliation">
						        <c:if test="${affiliation.id == affiliationId}">
						            <option  selected="selected" value="${affiliation.id}">${affiliation.name}</option>
						        </c:if>
						        <c:if test="${affiliation.id != affiliationId}">
						            <option value="${affiliation.id}">${affiliation.name}</option>
						        </c:if>
						    </c:forEach>
						 </select>
						 
						 </form>
				         
				      <div id="newUsers">
				          <c:import url="users_table.jsp"/>
				      </div>
				</div>

	            <div id="tab2">
	            	<form method="post" id="user_search_form" name="userSearchForm" action="javascript:YAHOO.ur.user.searchUser(0,1,1);" >
	            		
	            		<br/>
						Search User : <input type="input" id="userSearchQuery" name="query" size="50"/>
						<button id="search_user" class="ur_button" type="button"
		                       onmouseover="this.className='ur_buttonover';"
		                       onmouseout="this.className='ur_button';"
		                       onclick="javascript:YAHOO.ur.user.searchUser(0,1,1);">Search</button>
					</form>
					<br/>
	                <br/>
					<div id="search_results_div"></div>
				</div>

			</div>
		</div>
	</div>
	<!--  End body div -->
	  
	  <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
   
   	      <div id="newUserDialog" class="hidden">
	          <div class="hd">User Information</div>
		      <div class="bd">
		          <form id="addUser" name="newUserForm" method="post" 
		              action="admin/createUser.action">
	                
	                  <div id="newUserDialogFields">
	                       <c:import url="/pages/admin/user/add_user_form.jsp"/>
	                  </div>
		          </form>
		      </div>
	      </div>

	      <div id="changePasswordDialog" class="hidden">
	          <div class="hd">Change Password</div>
		      <div class="bd">
		          
		          <form id="changePassword" name="changePasswordForm" method="post" 
		              action="<c:url value="/admin/changePassword.action"/>">
	                
	                  <div id="changePasswordDialogFields">
	                      <c:import url="/pages/admin/user/change_password_form.jsp"/>
	                  </div>
		          </form>
		      </div>
	      </div>


	      <div id="deleteUserDialog" class="hidden">
	          <div class="hd">Delete Users</div>
		      <div class="bd">
		          <form id="deleteUser" name="deleteUser" method="POST" 
		              action="<c:url value="/admin/deleteUser.action"/>">
		              <div id="deleteUserError" class="errorMessage"></div>
		              <input id="deleteUserId" type="hidden" name="id" value=""/>
			          <p>Are you sure you wish to delete the selected user?</p>
		          </form>
		      </div>
	      </div>
	      
	       <div id="loginAsUserDialog" class="hidden">
	          <div class="hd">Login as user</div>
		      <div class="bd">
		          <form id="loginAsUser" name="loginAsUserForm" method="POST" 
		              action="<c:url value="/admin/loginAsUser.action"/>">
		              
		              <input type="hidden" id="loginAsUserForm_id" name="id">
			          <p>Are you sure you want to login as the selected user?</p>
		          </form>
		      </div>
	      </div>
	      
	      	          
	     <div id="error_dialog_box" class="hidden">
	         <div class="hd">Error</div>
		     <div class="bd">
		         <div id="default_error_dialog_content">
		         </div>
		     </div>
	     </div>
   </div>  
   <!--  end doc div -->    


</body>
</html>