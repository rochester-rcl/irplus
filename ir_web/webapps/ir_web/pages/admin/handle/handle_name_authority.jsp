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

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Handle Name Authorities</title>
    
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
	<ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>

    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="pages/js/ur_table.js"/>
    <ur:js src="page-resources/js/admin/handle_name_authority.js"/>
</head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixels wide -->
    <div id="doc2">
      
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <h3>Edit Handle Name Authorities</h3>
  
        <div id="bd">
      
		    <button id="showHandleNameAuthority" class="ur_button" 
 		                               onmouseover="this.className='ur_buttonover';"
 		                               onmouseout="this.className='ur_button';">New Handle Name Authority</button> 
	        <button id="showDeleteHandleNameAuthority" class="ur_button" 
 		                               onmouseover="this.className='ur_buttonover';"
 		                               onmouseout="this.className='ur_button';">Delete</button>

            <br/>
            <br/>
	        <div id="newHandleNameAuthorities"></div>
         </div>
         <!--  end body div -->
      
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End doc div-->
  
  <div id="newHandleNameAuthorityDialog" class="hidden">
    <div class="hd">Handle Name Authority Information</div>
    <div class="bd">
      <form id="addHandleNameAuthority" name="newHandleNameAuthorityForm" 
		                    method="post" 
		                    action="admin/createHandleNameAuthority.action">
	    <div id="newHandleNameAuthorityDialogFields">
	       <c:import url="handle_name_authority_form.jsp"/>
	    </div>
	  </form>
    </div>
  </div>
	         
  <div id="deleteHandleNameAuthorityDialog" class="hidden">
    <div class="hd">Delete Handle Name Authorities</div>
	  <div class="bd">
	    <form id="deleteContentType" name="deleteContentType" method="post" 
		                action="admin/deleteHandleNameAuthority.action">
		 <div id="deleteHandleNameAuthorityError" class="errorMessage"></div>
		   <p>Are you sure you wish to delete the selected handle name authorities?</p>
        </form>
      </div>
  </div>
</body>
</html>