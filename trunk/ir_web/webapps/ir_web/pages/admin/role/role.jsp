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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Roles</title>
    
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
    <ur:js src="page-resources/js/admin/role.js"/>
     
</head>

<body class=" yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
    
    <!--  this is the header of the page -->
    <c:import url="/inc/header.jsp"/>
    
    <h2>Roles</h2>
  
    <div id="bd">        
      
      <table>
	        <tr>
				<td>
	                <button id="showRole" class="ur_button" 
	                               onmouseover="this.className='ur_buttonover';"
	                               onmouseout="this.className='ur_button';">New Role</button> 
                </td>
                <td>
                    <button id="showDeleteRole" class="ur_button" 
	                               onmouseover="this.className='ur_buttonover';"
	                               onmouseout="this.className='ur_button';">Delete</button>
                </td>	        

	        </tr>
	    </table>
	    
	    <ur:div id="newRoles"></ur:div>
	      
	    <ur:div id="newRoleDialog" cssClass="hidden">
	          <ur:div cssClass="hd">Role Information</ur:div>
		      <ur:div cssClass="bd">
		          <ur:basicForm id="addRole" name="newRoleForm" 
		              method="post" 
		              action="user/addRole.action">
		              
		              <input type="hidden" id="newRoleForm_id"
		               name="id" value=""/>
		               
		              <input type="hidden" id="newRoleForm_new"
		               name="newRole" value="true"/>
		              
		              <ur:div id="roleError" cssClass="errorMessage"></ur:div>

				 		<table class="formTable">    
						    <tr>       
					            <td align="left" class="label">Name:*</td>
					            <td align="left" class="input"><input type="text" 
							    id="newRoleForm_name" 
						          name="role.name" 
						          value="" size="45"/> </td>
							</tr>
							<tr>
							    <td align="left" class="label">Description:</td>
							    <td align="left" colspan="2" class="input"> <textarea id="newRoleForm_description" 
				          			name="role.description" cols="42" rows="4"></textarea>
					            </td>
							</tr>
					    </table>
				    			          
		          </ur:basicForm>
		      </ur:div>
	      </ur:div>
	      
	      <ur:div id="deleteRoleDialog" cssClass="hidden">
	          <ur:div cssClass="hd">Delete Roles</ur:div>
		      <ur:div cssClass="bd">
		          <ur:basicForm id="deleteRole" name="deleteRole" method="POST" 
		              action="user/deleteRole.action">
		              
		              
		              <ur:div id="deleteRoleError" cssClass="errorMessage"></ur:div>
			          <p>Are you sure you wish to delete the selected roles?</p>
		          </ur:basicForm>
		      </ur:div>
	      </ur:div>
	      
	      
	  </div>
	  
	  <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
   
   
   </div>      


</body>
</html>