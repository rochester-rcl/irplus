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
    <ur:js src="pages/js/ur_table.js"/>
    <ur:js src="page-resources/js/admin/user_group.js"/>
</head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
      
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <h3>User Groups</h3>
  
        <div id="bd">
        
            <!--  set up tabs for the workspace -->
            <div id="user-group-tabs" class="yui-navset">
	            <ul class="yui-nav">
		            <li class="selected"><a href="#tab1"><em>Browse</em></a></li>
		            <li><a href="#tab2"><em>Search</em></a></li>
	            </ul>
	                
	                
	            <div class="yui-content">
	                <!--  first tab -->
	                <div id="tab1">
      
		                <button id="showUserGroup" class="ur_button" 
 		                        onmouseover="this.className='ur_buttonover';"
 		                        onmouseout="this.className='ur_button';">New User Group</button> 
	                    
	                    <button id="showDeleteUserGroup" class="ur_button" 
 		                        onmouseover="this.className='ur_buttonover';"
 		                        onmouseout="this.className='ur_button';">Delete</button>
	                    <br/>
	                    <br/>
	                    <div id="newUserGroups"></div>
	               </div>
	               
	               <div id="tab2">
	               
	                    <form method="GET" 
	                          id="user_group_search_form" 
	                          name="userGroupSearchForm" 
	                          action="javascript:YAHOO.ur.usergroup.userGroupSearch(0, 1, 1);">
	                          
	                          Search: <input type="text" size="50" id="user_group_query" name="query" value=""/>
	                         
	                         <input type="submit" value="search"/>
	                    </form>
	                   
	                    <div  id="user_group_search_results_div"></div>
	               </div>
	           </div>
	           <!-- end content div -->
	           
	       </div>
	       <!--  end content div -->
      </div>
      <!--  end body div -->
      
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End doc div-->
  
  <div id="newUserGroupDialog">
    <div class="hd">UserGroup Information</div>
    <div class="bd">
      <form id="addUserGroup" name="newUserGroupForm" 
		                    method="post" 
		                    action="/admin/createUserGroup.action">
	    <div id="newUserGroupDialogFields">
	        <c:import url="user_group_form.jsp"/>
	    </div>
	  </form>
    </div>
  </div>
	         
  <div id="deleteUserGroupDialog">
    <div class="hd">Delete User Groups</div>
	  <div class="bd">
	    <form id="deleteUserGroup" name="deleteUserGroup" method="post" 
		                action="/admin/deleteUserGroup.action">
		 <div id="deleteUserGroupError" class="errorMessage"></div>
		   <p>Are you sure you wish to delete the selected user groups?</p>
        </form>
      </div>
  </div>
</body>
</html>