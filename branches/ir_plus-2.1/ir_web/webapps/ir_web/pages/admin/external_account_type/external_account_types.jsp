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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Edit External Account Types</title>
    
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
 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
 	<ur:js src="page-resources/js/admin/external_account_type.js"/>
     
</head>

<body class=" yui-skin-sam">
    
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
    
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <h3>Edit External Account Types</h3>
  
        <div id="bd">
		<button id="showAddExternalAccountType" class="ur_button" 
 		    onmouseover="this.className='ur_buttonover';"
 		    onmouseout="this.className='ur_button';">New External Account Type</button> 
	   
	    <br/>
	    <br/> 
	    
	    <div id="external_account_table">
	        <c:import url="external_account_type_table.jsp"/>
        </div>	
        <!-- end div -->
          
      </div>
      <!--  end body div -->
  
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End  doc div-->
  
   <div id="newExternalAccountTypeDialog" class="hidden">
      <div class="hd">External Account Type Information</div>
      <div class="bd">
          <form id="newExternalAccountTypeForm" name="newExternalAccountTypeForm" 
		                    method="post" 
		                    action="/admin/createExternalAccountType.action">
	          <div id="newExternalAccountTypeDialogFields">
	              <c:import url="external_account_type_form.jsp"/>
	          </div>
	      </form>
      </div>
  </div>
	         
  <div id="deleteExternalAccountTypeDialog" class="hidden">
      <div class="hd">Delete External Account Type</div>
		<div class="bd">
		    <form id="deleteExternalAccountType" name="deleteExternalAccountType" method="post" 
		                action="/admin/deleteExternalAccountType.action">
		       <div id="deleteExternalAccountTypeError" class="errorMessage"></div>
		       <input type="hidden" value="" name="id" id="deleteId"/>
			   <p>Are you sure you wish to delete the selected External Account Type?</p>
		    </form>
		</div>
  </div>

</body>
</html>