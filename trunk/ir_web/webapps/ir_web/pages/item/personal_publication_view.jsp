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
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title>Viewing personal publication: ${item.name} </title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        
         <!-- Core + Skin CSS -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
        <ur:styleSheet href="page-resources/css/base-ur.css"/>
        <ur:styleSheet href="page-resources/yui/menu/assets/skins/sam/menu.css"/>

        <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/main_menu.css"/>
        <ur:styleSheet href="page-resources/css/tables.css"/>
   
        <!-- Dependencies --> 
        <ur:js src="page-resources/yui/yahoo-dom-event/yahoo-dom-event.js"/>
        <ur:js src="page-resources/yui/connection/connection-min.js"/>
        <ur:js src="page-resources/yui/container/container_core-min.js"/>
        <ur:js src="page-resources/yui/menu/menu-min.js"/>
   
        <!-- Source File -->
        <ur:js src="page-resources/js/menu/main_menu.js"/>
        <ur:js src="pages/js/base_path.js"/>

 	    
 	</head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">
				
				<!-- Begin - Display the Item preview -->
				
				<div >
				 <c:url value="viewResearcherPage.action" var="viewResearcherPage">
				     <c:param name="researcherId" value="${researcher.id}"/>
				 </c:url>
				 <h3>Researcher: <a href="${viewResearcherPage}">${researcher.user.firstName}&nbsp;${researcher.user.lastName}</a> <br/><br/>
				    <c:if test="${ir:hasThumbnail(researcher.primaryPicture)}">
				        <c:url var="url" value="/researcherThumbnailDownloader.action">
                            <c:param name="irFileId" value="${researcher.primaryPicture.id}"/>
                            <c:param name="researcherId" value="${researcher.id}"/>
                        </c:url>
                        <img align="middle" height="66px" width="100px" src="${url}"/>
                    </c:if>	    
			        <c:if test="${researcher.primaryPicture == null }">
	                     <img src="${pageContext.request.contextPath}/page-resources/images/all-images/noimage.jpg" height="100" width="100"/>
			        </c:if>	
			    <br/><br/>
				Publication: ${item.name} 
				</h3>
				</div>
				
				 <c:import url="item_files_frag.jsp">
				    <c:param name="isResearcherView" value="true"/>
			     </c:import>
					
			     <c:import url="item_metadata_frag.jsp"/>
       	        
       	       
       	        
	    </div>
	    <!--  end the body tag --> 
	
	    <!--  this is the footer of the page -->
	    <c:import url="/inc/footer.jsp"/>
	        
	    </div>
	    <!-- end doc -->
    </body>
</html>

    
