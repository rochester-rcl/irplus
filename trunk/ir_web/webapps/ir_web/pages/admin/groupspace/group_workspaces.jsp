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
    <title>Group Workspaces</title>
    
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
 	
 	
 	<ur:js src="page-resources/js/util/base_path.jsp"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/util/wait_dialog.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
 	<ur:js src="page-resources/js/admin/group_workspaces.js"/>
</head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
      
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <h3>Group Workspaces</h3>
  
        <div id="bd">
    
            <!--  set up tabs for the workspace -->
            <div id="group-workspace-tabs" class="yui-navset">
	            <ul class="yui-nav">
	                <li class="selected"><a href="#tab1"><em>Browse</em></a></li>
		            <li><a href="#tab2"><em>Search</em></a></li>
	            </ul>
	                
	                
	            <div class="yui-content">
	                <!--  first tab -->
	                <div id="tab1">
    
      
		                <button id="showGroupWorkspace" 
		                        class="ur_button" 
 		                        onmouseover="this.className='ur_buttonover';"
 		                        onmouseout="this.className='ur_button';">New Group Workspace</button> 
	                    <br/>
	                    <br/>
	                    <div id="groupWorkspaceTable">
	                        <c:import url="group_workspace_table.jsp"/>
	                    </div>
	                </div>
	                <!-- end tab 1 -->
	                <div id="tab2">
	                 <p>tab 2 content</p>
	                </div>
	                <!--  end tab 2 -->
	            </div>
	            <!-- end content -->
	            
	        </div>
	        <!-- end workspace  tabs-->
	
      </div>
      <!--  end body div -->
      
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End doc div-->
  
  <!-- form for groupsaces -->
  <div id="newGroupWorkspaceDialog" class="hidden">
      <div class="hd">Group Workspace Information</div>
      <div class="bd">
          <form id="addGroupWorkspace" 
                            name="newGroupWorkspaceForm" 
		                    method="post"
		                    action="/admin/createGroupWorkspace.action">
	          <div id="groupWorkspaceDialogFields">
	              <c:import url="group_workspace_form.jsp"/>
	          </div>
	      </form>
      </div>
  </div>
  
  <div id="deleteGroupWorkspaceDialog" class="hidden">
      <div class="hd">Delete Group Workspace</div>
		<div class="bd">
		    <form id="deleteGroupWorkspaceForm" name="deleteGroupWorkspace" method="post" 
		                action="/admin/deleteGroupWorkspace.action">
			   <p>Are you sure you wish to delete the selected group workspace?</p>
			   <input type="hidden" id="deleteId" name="id" value=""/>
		    </form>
		</div>
  </div>

  <!--  wait div -->
  <div id="wait_dialog_box" class="hidden">
	    <div class="hd">Processing...</div>
		<div class="bd">
		    <c:url var="wait" value="/page-resources/images/all-images/ajax-loader.gif"/>
		    <p><img src="${wait}"></img></p>
		</div>
  </div>   
   
</body>
</html>