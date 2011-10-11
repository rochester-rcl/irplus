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

<!--  images used by the page -->
<html>
    <head>
        <title>Welcome</title>
        <c:import url="/inc/meta-frag.jsp"/>

	    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>
	
	    <ur:styleSheet href="page-resources/css/main_menu.css"/>
	    <ur:styleSheet href="page-resources/css/global.css"/>
	    <ur:styleSheet href="page-resources/css/home_page_content_area.css"/>
	    <ur:styleSheet href="page-resources/css/tables.css"/>
        

        <!-- Dependencies --> 
    	<ur:js src="page-resources/yui/utilities/utilities.js"/>
        <ur:js src="page-resources/yui/button/button-min.js"/>
	    <ur:js src="page-resources/yui/container/container-min.js"/>
	    <ur:js src="page-resources/yui/menu/menu-min.js"/>
	    
        <!-- Source File -->
        <ur:js src="page-resources/js/util/base_path.jsp"/>
        <ur:js src="page-resources/js/util/ur_util.js"/>
        <ur:js src="page-resources/js/menu/main_menu.js"/>
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">
            
              
               <a href=" <c:url value="/user/workspace.action"/>">Go back to workspace</a>
               <c:if test="${!ur:isEmpty(institutionalItems)}">
                   <br/>
                   <br/>
                   The item was submitted as the following Institutional publication(s):
                   <br/>
                   <br/>
                   <c:forEach items="${institutionalItems}" var="item">
                       <c:url var="itemUrl" value="/institutionalPublicationPublicView.action">
                           <c:param name="institutionalItemId" value="${item.id}"/>
                       </c:url>
                       <a href="${itemUrl}">${item.institutionalCollection.name} - ${item.name}</a><br/>
                       
                   </c:forEach>
               </c:if>
                <c:if test="${!ur:isEmpty(reviewableItems)}">
                   <br/>
                   <br/>
                   The item was submitted to be reviewed:
                   <br/>
                   <br/>
                   <c:forEach items="${reviewableItems}" var="reviewableItem">
                       ${reviewableItem.institutionalCollection.name} - ${reviewableItem.item.name}<br/>
                   </c:forEach>
               </c:if>
               <c:if test="${ur:isEmpty(institutionalItems) && ur:isEmpty(reviewableItems)}">
                   <br/>
                   <br/>
                   The publication was not submitted to any collections.
               </c:if>
               
               
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
