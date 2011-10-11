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
<%@ taglib prefix="ir" uri="ir-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>


<html>

<head>
    <title>Properties for folder ${personalFolder.name}</title>
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>

    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <!--  required imports -->
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
    <ur:js src="page-resources/yui/menu/menu-min.js"/>
    
 	<!--  base path information -->
 	<ur:js src="page-resources/js/util/base_path.jsp"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="page-resources/js/user/folder_properties.js"/>
</head>

<body id="body" class="yui-skin-sam">

    
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">  
 
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
  
        <!--  this is the body regin of the page -->
        <div id="bd">
        
             <div id="folder_properties">
                 <c:import url="folder_properties_inner_page.jsp"/>
             </div>
             <!-- end the folder properties div -->
            
	    </div>
	    <!--  end body tag -->
	  
	  <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
      
    </div>
    <!--  end doc tag -->
	          
	<div id="newFolderDialog" class="hidden">
	    <div class="hd">Folder Information</div>
		<div class="bd">
		    <ur:basicForm id="addFolder" name="newFolderForm" 
		        method="post" action="user/updatePersonalFolder.action">
		              
		        <input type="hidden" id="newFolderForm_parentFolderId"
		               name="parentFolderId" value="${personalFolder.parent.id}"/>

              	<div id="newFolderDialogFields">
               		<%@ include file="/pages/user/workspace/personal_folder_form.jsp" %>
              	<div>

		     </ur:basicForm>
		 </div>
		 <!-- end dialog body -->
	 </div>
	 <!--  end the new folder dialog -->
	          


</body>
</html>