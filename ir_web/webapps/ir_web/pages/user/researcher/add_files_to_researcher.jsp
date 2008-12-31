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
        <title>Welcome</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
     <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/yui/tabview/assets/skins/sam/tabview.css"/>
    <ur:styleSheet href="page-resources/yui/tabview/assets/border_tabs.css"/>
    
    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	<ur:js src="page-resources/yui/button/button-min.js"/>
 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
 	<ur:js src="pages/js/base_path.js"/>
	<ur:js src="page-resources/js/user/add_files.js"/>    
 	
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

            <h2> Add files to Researcher </h2>

            <!--  this is the body region of the page -->
            <div id="bd">
            
            	<div class="clear">&nbsp;</div>
				
       	        <div class="yui-g">
			        <div class="yui-u first">
		       			 <!--  table of files and folders -->
	                      <div id="newPersonalFolders" >
	                          <ur:basicForm  id="folders" name="myPersonalFolders"  method="POST" action="user/getPersonalFilesFolders.action">
	                              <input type="hidden" id="myFolders_parentPersonalFolderId" 
	                                   name="parentPersonalFolderId" 
	                                   value="${parentPersonalFolderId}"/>
	                              <input type="hidden" id="myFolders_researcherId" 
	                                   name="researcherId" 
	                                   value="${researcherId}"/>
	                              <input type="hidden" id="myFolders_parentFolderId" 
	                                   name="parentFolderId" 
	                                   value="${parentFolderId}"/>
	                          </ur:basicForm>
	                      </div>
	                      <!--  end personal files and folders div -->
		       
		             </div>
		             <!--  end the first column -->
            
        	        <div class="yui-u">
        	        	<!--  Table of selected files -->
                    	<div id="newResearcherFolders" >
	                          <ur:basicForm  id="files" name="myResearcherFolders"  method="POST" action="user/getResearcherFileSystem.action">
	                              <input type="hidden" id="myResearcherFolders_researcherId" 
	                                   name="researcherId" 
	                                   value="${researcherId}"/>
	                              <input type="hidden" id="myResearcherFolders_parentFolderId" 
	                                   name="parentFolderId" 
	                                   value="${parentFolderId}"/>
	                          </ur:basicForm>
	                      </div>
	                      <!--  end table of selected files div -->
            	    </div>
                	<!--  end the second column -->
                
                
                
                </div>
                <!--  end the grid -->
				
				<div class="clear">&nbsp;</div>
				
				<table width="100%">
                  <tr>
                      <td align="left" >
                          <button class="ur_button" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.researcher.files.viewResearcherFolders();">Back to Folders</button>
                      </td>
                      
                     
                  </tr>
                </table>
                

	      <!--  Add file error dialog -->
	      <div id="fileErrorDialog" class="hidden">
	          <div class="hd">Cannot add file</div>
	          <div class="bd">
	               <div id="fileNameError" class="errorMessage"></div>
	          </div>
	      </div>
	      <!--  end Add file error  dialog -->  
      
                      
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
      
                
   
        </div>
        <!-- end doc -->
    
    </body>
</html>

    
