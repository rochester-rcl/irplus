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

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>


<html>
    <head>
        <title>Add Files to Group Workspace Project Page</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>
    
    
    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	<ur:js src="page-resources/yui/button/button-min.js"/>
 	
 	<ur:js src="page-resources/js/util/base_path.jsp"/>
 	<ur:js src="page-resources/js/util/ur_util.js" />
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
	<ur:js src="page-resources/js/user/group_workspace_project_page_add_files.js"/>    
 	
    <!--  Style for dialog boxes -->
    <style>

        /* this is a simple fix for geco based browsers
         * this does have side affects if scroll bars are used.
         * in geco based browsers see cursor fix on yahoo
         * http://developer.yahoo.com/yui/container/
         */
        .mask 
        {
            overflow:visible; /* or overflow:hidden */
        }
    </style>
	    
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>

            <h3> Add files to Project Page: ${groupWorkspaceProjectPage.groupWorkspace.name}</h3>
                <button class="ur_button" 
                        onmouseover="this.className='ur_buttonover';"
                        onmouseout="this.className='ur_button';"
                        onclick="YAHOO.ur.group_workspace_project_page.files.viewProjectPageFolders();">Back to Folders</button>
            <!--  this is the body region of the page -->
            <div id="bd">
            
            	<br/>
				
       	        <div class="yui-g">
			        <div class="yui-u first">
		                  <!--  table of files and folders -->
	                      <div id="myGroupWorkspaceFolders" >
	                          <c:import url="add_files_project_page_group_workspace_file_system_table.jsp"/>
	                      </div>
	                      <!--  end personal files and folders div -->
		            </div>
		            <!--  end the first column -->
            
        	        <div class="yui-u">
        	          <div id="myProjectPageFolders">
        	              <c:import url="add_files_project_page_file_system_table.jsp"/>
        	          </div>
            	    </div>
                	<!--  end the second column -->
                
                </div>
                <!--  end the grid -->
				 
				 <br/>
                <button class="ur_button" 
                        onmouseover="this.className='ur_buttonover';"
                        onmouseout="this.className='ur_button';"
                        onclick="javascript:YAHOO.ur.group_workspace_project_page.files.viewProjectPageFolders();">Back to Folders</button>
    
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
   
        </div>
        <!-- end doc -->
        
          <!--  Add file error dialog -->
	      <div id="fileErrorDialog" class="hidden">
	          <div class="hd">Cannot add file</div>
	          <div class="bd">
	               <div id="fileNameError" class="errorMessage"></div>
	          </div>
	      </div>
	      <!--  end Add file error  dialog -->  
      
    
    </body>
</html>

    
