
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
    <title>Properties for file ${personalFile.versionedFile.nameWithExtension}</title>
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
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="page-resources/js/user/file_properties.js"/>
</head>

<body id="body" class="yui-skin-sam">

    
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">  
 
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
  
        <!--  this is the body regin of the page -->
        <div id="bd">
            <h3> Properties for : ${personalItem.name}  </h3>
		    <h3>Path: /<span class="worldBtnImg">&nbsp;</span>
		         <c:if test="${parentCollectionId != 0}">
                      <c:url var="viewRootCollectionsUrl" value="/user/workspace.action">
                          <c:param name="parentCollectionId" value="0"/>
                          <c:param name="showCollectionTab" value="true"/>
                      </c:url>
		             <a href="${viewRootCollectionsUrl}">My Publications</a>&nbsp;/
		         </c:if>
		         <c:if test="${parentCollectionId == 0}">
		                   My Publications&nbsp;/
		         </c:if>
		         
	             <c:url var="viewCollectionsUrl" value="/user/workspace.action">
                      <c:param name="showCollectionTab" value="true"/>
                  </c:url>
		         
		         
		         <c:forEach var="collection" items="${collectionPath}">
		               <span class="worldBtnImg">&nbsp;</span>
		                   <c:if test="${collection.id != parentCollectionId}">
		                       <a href="${viewCollectionsUrl}&parentCollectionId=${collection.id}">${collection.name}</a>&nbsp;/
		                   </c:if>
		                   <c:if test="${collection.id == parentCollectionId}">
		                       ${collection.name}&nbsp;/
		                   </c:if>
		         </c:forEach>
		    </h3>

        	
        	
        	
        	<strong> All versions </strong>
        	<div class="clear">&nbsp;</div>
        	
              <table class="simpleTable">
                  <thead>
                      <tr>    
	                      <th>
	                          Name
	                      </th>
	                      <th>
	                          Version Number
	                      </th>
	                      <th>
	                          Published To Collections
	                      </th>
                      </tr>
                  </thead>
                  <tbody>
        
                      <c:forEach var="info" varStatus="status" items="${itemVersionInfo}">
                      <c:if test="${ (status.count % 2) == 0}">
                          <c:set value="even" var="rowType"/>
                      </c:if>
                      <c:if test="${ (status.count % 2) == 1}">
                          <c:set value="odd" var="rowType"/>
                      </c:if>
                      <tr>
                          <td class="${rowType}">${info.version.item.name} </td>
                          <c:url var="itemPreviewUrl" value="/user/viewPersonalItemVersion.action">
	                          <c:param name="genericItemId" value="${info.version.item.id}"/>
	                          <c:param name="personalItemId" value="${personalItem.id}"/>
	                      </c:url>
                          <td class="${rowType}"><a href="${itemPreviewUrl}">${info.version.versionNumber}</a></td>
                          <td class="${rowType}">
 	                          		<c:forEach var="collection" items="${info.collections}">
		                          		 ${collection.path}${collection.name} <div class="clear"> &nbsp;</div>
	                          		</c:forEach>
                          
                          </td>
                     </tr>
                     </c:forEach>  
                 </tbody>  
             </table>                     

             <!-- end the file properties div -->
            
	    </div>
	    <!--  end body tag -->
	  
	  <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
      
    </div>
    <!--  end doc tag -->
</body>
</html>