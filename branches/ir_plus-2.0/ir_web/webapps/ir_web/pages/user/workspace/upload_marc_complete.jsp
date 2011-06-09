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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Upload Complete</title>
    
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>
    <ur:styleSheet href="page-resources/yui/menu/assets/skins/sam/menu.css"/>
    
    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <!-- CSS files --> 
    <ur:js src="page-resources/yui/yahoo-dom-event/yahoo-dom-event.js"/>
    <ur:js src="page-resources/yui/container/container_core-min.js"/>
    <ur:js src="page-resources/yui/menu/menu-min.js"/>

    <ur:js src="page-resources/js/menu/main_menu.js"/>
</head>

<body class="yui-skin-sam">
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">  

    <!--  this is the header of the page -->
    <c:import url="/inc/header.jsp"/>
            
    <!--  this is the body regin of the page -->
    <div id="bd">
    
        <h3>Upload Complete - The following publications were created:</h3>
        <c:url var="workspaceUrl" value="/user/workspace.action">
            <c:param name="showCollectionTab" value="true"/>
            <c:param name="parentCollectionId" value="${parentCollectionId}"/>
        </c:url>
        <h3><a href="${workspaceUrl}">Back to workspace</a></h3>
 	    <c:forEach items="${items}" var="item">
 	        <table border="1px">
 	            <tr>
 	                <td>Leading Articles</td>
 	                <td>${item.currentVersion.item.leadingNameArticles}</td>
 	            </tr>
 	            <tr>
 	                <td>Title</td>
 	                <td>${item.currentVersion.item.name}</td>
 	            </tr>
 	            <tr>
 	                <td>Content Type</td>
 	                <td>${item.currentVersion.item.primaryItemContentType.contentType.name}</td>
 	            </tr>
 	            <tr>
 	                <td>Other Titles</td>
 	                <td>
 	                <c:forEach items="${item.currentVersion.item.subTitles}" var="other">
 	                     ${other}<br/><br/> 
 	                </c:forEach>
 	                </td>
 	            </tr>
 	            <tr>
 	                <td>Description</td>
 	                <td>${item.currentVersion.item.description}</td>
 	            </tr>
 	            <tr>
 	                <td>Abstract</td>
 	                <td>${item.currentVersion.item.itemAbstract}</td>
 	            </tr>
 	            <tr>
 	                <td>Contributors</td>
 	                <td>
 	                <c:forEach items="${item.currentVersion.item.contributors}" var="contrib">
 	                     ${contrib.contributor.contributorType.name}: ${contrib.contributor.personName} <br/><br/> 
 	                </c:forEach>
 	                </td>
 	            </tr>
 	            <tr>
 	                <td>Publisher</td>
 	                <td>
 	                     ${item.currentVersion.item.externalPublishedItem.publisher}
 	                </td>
 	            </tr>
 	            <tr>
 	                <td>Publisher Location</td>
 	                <td>
 	                     ${item.currentVersion.item.externalPublishedItem.placeOfPublication}
 	                </td>
 	            </tr>            
 	             <tr>
 	                <td>Publication Date</td>
 	                <td>
 	                     ${item.currentVersion.item.externalPublishedItem.publishedDate}
 	                </td>
 	            </tr>
 	            <tr>
 	                <td>Keywords</td>
 	                <td>
 	                     ${item.currentVersion.item.itemKeywords}
 	                </td>
 	            </tr>
 	            <tr>
 	                <td>Identifiers</td>
 	                <td>
 	                <c:forEach items="${item.currentVersion.item.itemIdentifiers}" var="ident">
 	                     ${ident.identifierType}: ${ident.value} <br/><br/> 
 	                </c:forEach>
 	                </td>
 	            </tr>
 	            <tr>
 	                <td>Extents</td>
 	                <td>
 	                <c:forEach items="${item.currentVersion.item.itemExtents}" var="extent">
 	                     ${extent.extentType}: ${extent.value} <br/><br/> 
 	                </c:forEach>
 	                </td>
 	            </tr>
 	             <tr>
 	                <td>Language</td>
 	                <td>${item.currentVersion.item.languageType}</td>
 	            </tr>
 	        </table>  
 	        <br/>
 	        <br/>  
 	    </c:forEach>
 	  
      </div>
      <!--  end the body tag --> 

      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
        
    </div>
    <!-- end doc -->

</body>
</html>