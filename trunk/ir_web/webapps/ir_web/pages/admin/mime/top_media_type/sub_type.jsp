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
    <title>Edit Mime Sub Type</title>
    
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
    <ur:js src="pages/js/ur_table.js"/>
    
    <ur:js src="page-resources/js/admin/sub_type.js"/>
     
</head>

<body class=" yui-skin-sam">
    <form name ="topMediaTypeData">
      <input type="hidden" name="id" value="${id}"/>
    </form>
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
    
    <!--  this is the header of the page -->
    <c:import url="/inc/header.jsp"/>
    
    <h2>Sub Types for Top Media Type: ${topMediaType.name}</h2>
    
    <a href="<c:url value="/admin/viewTopMediaTypes.action"/>">Top Media Types</a>
    <br/><br/>
    <div id="bd">      
          <table>
	          <tr>
	              <td>
	                <button id="showSubType" class="ur_button" 
 		                               onmouseover="this.className='ur_buttonover';"
 		                               onmouseout="this.className='ur_button';">New Sub Type</button> 
	              </td>
	              <td>
	                <button id="showDeleteSubType" class="ur_button" 
 		                               onmouseover="this.className='ur_buttonover';"
 		                               onmouseout="this.className='ur_button';">Delete Sub Type</button> 
	               </td>
	          </tr>
	      </table>
	      
	      
	      <ur:div id="newSubTypes"></ur:div>
	      
	      <ur:div id="newSubTypeDialog">
	          <ur:div cssClass="hd">Sub Type Information</ur:div>
		      <ur:div cssClass="bd">
		          <ur:basicForm id="addSubType" name="newSubTypeForm" 
		              method="post" 
		              action="admin/updateSubType.action">
		              
		              <ur:div id="newSubTypeDialogFields">
	                   <%@ include file="/pages/admin/mime/top_media_type/sub_type_form.jsp" %>
	                  </ur:div>
		              
		          </ur:basicForm>
		      </ur:div>
	      </ur:div>
	      
	      <ur:div id="deleteSubTypeDialog">
	          <ur:div cssClass="hd">Delete Sub Types</ur:div>
		      <ur:div cssClass="bd">
		          <ur:basicForm id="deleteSubType" name="deleteSubType" method="POST" 
		              action="user/deleteSubType.action">
		              
		              
		              <ur:div id="deleteSubTypeError" cssClass="errorMessage"></ur:div>
			          <p>Are you sure you wish to delete the selected sub types?</p>
		          </ur:basicForm>
		      </ur:div>
	      </ur:div>
	      	      
	  </div>
	  <!--  End body div -->
	  
	  <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
   
   </div>  
   <!--  end doc div -->    	      
 
</body>
</html>