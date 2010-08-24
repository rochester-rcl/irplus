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


<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title>${repository.name} Repository Statistics</title>
        
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!-- Core + Skin CSS -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
        <ur:styleSheet href="page-resources/css/base-ur.css"/>
        <ur:styleSheet href="page-resources/yui/menu/assets/skins/sam/menu.css"/>

        <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/home_page_content_area.css"/>
        <ur:styleSheet href="page-resources/css/main_menu.css"/>
        <ur:styleSheet href="page-resources/css/tables.css"/>

        <!-- Dependencies --> 
        <ur:js src="page-resources/yui/yahoo-dom-event/yahoo-dom-event.js"/>
        <ur:js src="page-resources/yui/connection/connection-min.js"/>
        <ur:js src="page-resources/yui/container/container_core-min.js"/>
        <ur:js src="page-resources/yui/menu/menu-min.js"/>
        
        <!-- Source File -->
        <ur:js src="page-resources/js/menu/main_menu.js"/>
        
        <!--  base path information -->
 	    <ur:js src="pages/js/base_path.js"/>
 	    <ur:js src="page-resources/js/util/ur_util.js"/>
        <ur:js src="page-resources/js/public/home.js"/>
        
        <!--  styling for page specific elements -->
        <style type="text/css">
            .ur_button
            {
                width: 100px;
                margin: 0px;
                padding: 0px;
            }
            
            .ur_buttonover
            {
                width: 100px;
                margin: 0px;
                padding: 0px;
            }
        </style>
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body regin of the page -->
            <div id="bd">
                <h3>General Repository Information: <a href="home.action">${repository.name}</a></h3>
                <strong>Number of collections:</strong> ${numberOfCollections}<br/><br/>
                <strong>Number of publications:</strong> ${numberOfPublications}<br/><br/>
                <strong>Number of file downloads:</strong> ${numberOfFileDownloads}<br/><br/>
                <strong>Number of members:</strong> ${numberOfUsers}<br/><br/>
                <strong>Number of researchers:</strong> ${numberOfResearchers}<br/><br/>
                <strong>Number of public researchers:</strong> ${numberOfPublicResearchers}<br/><br/>
                
                <c:if test="${!ur:isEmpty(contentTypeCounts)}">
                <h3>Repository Content Type Counts</h3>
                <c:forEach var="contentTypeCount" items="${contentTypeCounts}">
                    <c:if test="${contentTypeCount.count > 0 }">
                        <c:url var="browseUrl" value="/browseRepositoryItems.action">
		                    <c:param name="rowStart" value="0"/>
			                <c:param name="startPageNumber" value="1"/>
			                <c:param name="currentPageNumber" value="1"/>
			                <c:param name="contentTypeId" value="${contentTypeCount.contentType.id}"/>																					
		                </c:url>
                    <strong>${contentTypeCount.contentType.name}:</strong> <a href="${browseUrl}">${contentTypeCount.count} </a><br/><br/>
                    </c:if>
                </c:forEach>
                </c:if> 
                
                <c:if test="${sponsorCount > 0}">
                <h3>Sponsor Count</h3>
                <c:url var="sponsorUrl" value="/browseSponsorNames.action">
		            <c:param name="rowStart" value="0"/>
			        <c:param name="startPageNumber" value="1"/>
			        <c:param name="currentPageNumber" value="1"/>
		         </c:url>
                <strong>Sponsor Count:</strong><a href="${sponsorUrl}">${sponsorCount}</a>
                </c:if>
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
