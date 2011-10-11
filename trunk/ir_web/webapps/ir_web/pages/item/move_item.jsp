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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp" />

<fmt:setBundle basename="messages" />

<html>
<head>

    <title>Move Institutional Publication</title>
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

    <!-- Source File -->
    <ur:js src="page-resources/js/menu/main_menu.js"/>

    <!--  base path information -->
    <ur:js src="page-resources/js/util/base_path.jsp" />
    <ur:js src="page-resources/js/util/ur_util.js" />
    <ur:js src="page-resources/js/util/wait_dialog.js" />
    <ur:js src="page-resources/js/public/move_institutional_item.js" />
</head>

<body class="yui-skin-sam">
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2"><!--  this is the header of the page --> 
        <c:import url="/inc/header.jsp" /> 

        <h3>Move Institutional Publication</h3>

        <!--  this is the body regin of the page -->
        <div id="bd">
            <div id="move_collection_frag">
                <c:import url="/pages/item/move_item_frag.jsp"/>
            </div>

            <!--  end the body tag --> <!--  this is the footer of the page --> 
            <c:import url="/inc/footer.jsp" />
        </div>
    </div>
    <!-- end doc -->
    
    <!--  generic error dialog -->   	     
    <div id="error_dialog_box" class="hidden">
        <div class="hd">Error</div>
	    <div class="bd">
	        <div id="default_error_dialog_content"></div>
	    </div>
    </div>
    <!-- End generic error dialog -->
    
    <!--  wait div -->
	<div id="wait_dialog_box" class="hidden">
	    <div class="hd">Processing...</div>
		<div class="bd">
		    <c:url var="wait" value="/page-resources/images/all-images/ajax-loader.gif"/>
		    <p><img src="${wait}"></img></p>
		</div>
	</div>   
	    
</body>
</html>