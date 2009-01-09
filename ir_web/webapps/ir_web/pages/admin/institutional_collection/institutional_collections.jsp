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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<fmt:setBundle basename="messages"/>

<html>
<head>
        
   <title>Institutional Collections</title>
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
    
    <ur:js src="pages/js/base_path.js"/>
    <ur:js src="page-resources/js/util/ur_util.js"/>
    <ur:js src="page-resources/js/menu/main_menu.js"/>  
	<ur:js src="pages/js/ur_table.js"/>
    <ur:js src="page-resources/js/admin/institutional_collection.js"/>
    
    
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
            
            <!--  this is the body region of the page -->
            <div id="bd">
            
                <h3>Institutional Collection Administration</h3>
            
              <ur:div id="newInstitutionalCollections">
    	          <ur:basicForm id="institutionalCollections" name="institutionalCollections">
	              <input type="hidden" 
	                  id="institutionalCollections_parentCollectionId" 
	                  name="parentCollectionId" 
	                 value="${parentCollectionId}"/>
	              </ur:basicForm>
	          </ur:div>
    
           
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
  
        <ur:div id="newCollectionDialog" cssClass="hidden">
	        <ur:div cssClass="hd">New Collection</ur:div>
		    <ur:div cssClass="bd">
		        <ur:basicForm name="newCollectionForm" method="post" 
		                  action="admin/createInstitutionalCollection.action">
		                  <input type="hidden" id="newCollectionForm_parentCollectionId" 
		                      name="parentCollectionId" value="${parentCollectionId}"/>
		               
		                  <ur:div id="collectionNameError" cssClass="errorMessage"></ur:div>
		                  <table class="formTable">
		                      <tr>
		                          <td class="label">Collection Name:</td>
		                          <td class="input"><input type="text" size="50" name="collectionName" value=""/></td>
		                      </tr>
		                  </table>
		        </ur:basicForm>
		    </ur:div>
	    </ur:div> 
	    
	    <ur:div id="deleteCollectionDialog" cssClass="hidden">
	        <ur:div cssClass="hd">Delete Collection?</ur:div>
		    <ur:div cssClass="bd">
		         Are you sure you wish to delete the selected collections?
		    </ur:div>
	    </ur:div>   
    </body>
</html>