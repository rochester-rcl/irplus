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

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Edit Sub Type</title>
    
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
    <ur:js src="page-resources/js/admin/sub_type_extension.js"/>
     
</head>

<body class=" yui-skin-sam">

  <form name ="subTypeData">
      <input type="hidden" name="id" value="${id}"/>
  </form>

  <!--  yahoo doc 2 template creates a page 950 pixles wide -->
  <div id="doc2">
    
  <!--  this is the header of the page -->
  <c:import url="/inc/header.jsp"/>
    
  <h3>Extensions for Sub Type: ${subType.name}</h3>
  <c:url var="viewTopMediaTypeUrl" value="/admin/initSubType.action">
       <c:param name="id" value="${topMediaType.id}"/>
  </c:url>
  
  <form name ="subTypeData">
      <input type="hidden" name="subTypeId" id="form_data_subTypeId" value="${subTypeId}"/>
  </form>
	                  
  <a href="<c:url value="/admin/viewTopMediaTypes.action"/>">All Top Media Types</a> &gt; <a href="${viewTopMediaTypeUrl}"> ${topMediaType.name} Sub Types</a>
    <br/>
    <br/>
  <div id="bd">      
          
          
		  <button id="showSubTypeExtension" class="ur_button" 
 		      onmouseover="this.className='ur_buttonover';"
 		      onmouseout="this.className='ur_button';">New Extension</button> 
		  
		  <button id="showDeleteSubTypeExtension" class="ur_button" 
 		      onmouseover="this.className='ur_buttonover';"
 		      onmouseout="this.className='ur_button';">Delete Extension</button> 
	      
	      <br/>
	      <br/>              
	      <div id="newSubTypeExtensions"> </div>
	      

	      
	  </div>
	  <!--  End body div -->
	  
	  <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
   
   </div>  
   <!--  end doc div -->  
   
   	      <div id="newSubTypeExtensionDialog">
	          <div class="hd">Sub Type Extension Information</div>
		      <div class="bd">
		          <form id="addSubTypeExtension" name="newSubTypeExtensionForm" 
		              method="post" 
		              action="/admin/updateSubTypeExtension.action">
		              
		              <div id="newSubTypeExtensionDialogFields">
		                  <c:import url="sub_type_extension_form.jsp"/>
	                  </div>
		              
		          </form>
		      </div>
	      </div>
	      
	      <div id="deleteSubTypeExtensionDialog">
	          <div class="hd">Delete Sub Type Extension</div>
		      <div class="bd">
		          <form id="deleteSubTypeExtension" name="deleteSubTypeExtension" method="POST" 
		              action="/admin/deleteSubTypeExtension.action">
		              
		              
		              <div id="deleteSubTypeExtensionError" class="errorMessage"></div>
			          <p>Are you sure you wish to delete the selected sub type extensions?</p>
		          </form>
		      </div>
	      </div>  

</body>
</html>