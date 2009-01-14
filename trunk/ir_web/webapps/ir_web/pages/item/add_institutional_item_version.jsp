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
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp" />

<fmt:setBundle basename="messages" />

<html>
<head>

    <title>Add new version</title>
    <c:import url="/inc/meta-frag.jsp" />

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

     <!--  base path information -->
    <ur:js src="pages/js/base_path.js" />
    <ur:js src="page-resources/js/util/ur_util.js" />
    <ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="pages/js/ur_table.js"/>
    <ur:js src="page-resources/js/user/add_institutional_item_version.js" />
</head>

<body class="yui-skin-sam">
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2"><!--  this is the header of the page --> 
        <c:import url="/inc/header.jsp" /> 

        <h3>Add new version for "${institutionalItem.name}"</h3>

        <!--  this is the body regin of the page -->
        <div id="bd">
	        
	    <c:url var="cancelUrl" value="/institutionalPublicationPublicView.action">
	        <c:param name="institutionalItemId" value="${institutionalItemId}"/>
	    </c:url>
	    
	    <button class="ur_button"
				onmouseover="this.className='ur_buttonover';"
				onmouseout="this.className='ur_button';"
				onclick="location.href='${cancelUrl}';">Cancel</button>
			        
		<ur:basicForm method="post" name="addVersionForm" >
			      <input type="hidden" id="addVersionForm_institutionalItemId" 
		               name="institutionalItemId" value="${institutionalItemId}"/>
		          <input type="hidden" id="addVersionForm_itemVersionId" 
		               name="itemVersionId"/>
		</ur:basicForm>

        <div id="newPersonalCollections">
	      <ur:basicForm id="collections" name="myCollections">
	          <input type="hidden" id="myCollections_parentCollectionId" 
	               name="parentCollectionId" 
	               value="${parentCollectionId}"/>
			   <input type="hidden" id="myCollections_institutionalItemId" 
		               name="institutionalItemId" value="${institutionalItemId}"/>
	               
	      </ur:basicForm>
	     </div>
	                      

            <!--  end the body tag --> <!--  this is the footer of the page --> 
            <c:import url="/inc/footer.jsp" />
        </div>
    </div>
    <!-- end doc -->
</body>
</html>