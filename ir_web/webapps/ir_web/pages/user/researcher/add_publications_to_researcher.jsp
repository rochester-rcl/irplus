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
        <title>Add Publications to Researcher Page</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
	    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
	    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
	    <ur:styleSheet href="page-resources/css/base-ur.css"/>
	    
	    <ur:styleSheet href="page-resources/css/main_menu.css"/>
	    <ur:styleSheet href="page-resources/css/global.css"/>
	    <ur:styleSheet href="page-resources/css/tables.css"/>
	    
	    <ur:js src="page-resources/yui/utilities/utilities.js"/>
	    <ur:js src="page-resources/yui/container/container-min.js"/>
	 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
	 	<ur:js src="page-resources/yui/button/button-min.js"/>
	 	
	 	<ur:js src="pages/js/base_path.js"/>
	 	<ur:js src="page-resources/js/util/ur_util.js"/>
	 	<ur:js src="page-resources/js/menu/main_menu.js"/>
	    <ur:js src="page-resources/js/user/researcher_add_publications.js"/>
	 	
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
            
            <h3> Add publications to Researcher </h3>
          
            <button class="ur_button" 
                    onmouseover="this.className='ur_buttonover';"
                    onmouseout="this.className='ur_button';"
                    onclick="javascript:YAHOO.ur.researcher.publications.viewResearcherFolders();">Back to Folders</button>
              <br/>
              <br/>
            <!--  this is the body region of the page -->
            <div id="bd">
            
       	        <div class="yui-g">
			        <div class="yui-u first">
		       			 <!--  table of personal publications and collections -->
	                      <div id="newPersonalCollections" >
	                          
	                          <form  id="collections" name="myPersonalCollections"  method="POST" action="user/getPersonalCollectionsAndItems.action">
	                             <input type="hidden" id="myCollections_parentCollectionId" 
	                                   name="parentCollectionId" 
	                                   value="${parentCollectionId}"/>
	                             <input type="hidden" id="myCollections_researcherId" 
	                                  name="researcherId" 
	                                   value="${researcherId}"/>
	                             <input type="hidden" id="myCollections_parentFolderId" 
	                                   name="parentFolderId" 
	                                   value="${parentFolderId}"/>
	                             
	                          </form>
	                      </div>
	                      <!--  end personal files and folders div -->
		       
		             </div>
		             <!--  end the first column -->
            
        	        <div class="yui-u">
        	        	<!--  Table of selected files -->
                    	<div id="newResearcherFolders" >
	                          <form  id="files" name="myResearcherFolders"  method="POST" action="user/getResearcherFolders.action">
	                              <input type="hidden" id="myResearcherFolders_parentFolderId" 
	                                   name="parentFolderId" 
	                                   value="${parentFolderId}"/>
	                          </form>
	                      </div>
	                      <!--  end table of selected files div -->
            	    </div>
                	<!--  end the second column -->
                
                
                
                </div>
                <!--  end the grid -->
				
				<br/>
				
                <button class="ur_button" 
                        onmouseover="this.className='ur_buttonover';"
                        onmouseout="this.className='ur_button';"
                        onclick="javascript:YAHOO.ur.researcher.publications.viewResearcherFolders();">Back to Folders</button>
   
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
      
         </div>
        <!-- end doc -->
        
        
	      <!--  Add file error dialog -->
	      <div id="publicationErrorDialog" class="hidden">
	          <div class="hd">Cannot add publication</div>
	          <div class="bd">
	               <div id="publicationNameError" class="errorMessage"></div>
	          </div>
	      </div>
	      <!--  end Add file error  dialog -->  
      
    
    </body>
</html>

    
