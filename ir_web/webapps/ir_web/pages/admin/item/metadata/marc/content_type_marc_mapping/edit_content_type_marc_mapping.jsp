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
    <title>Create MARC Mapping</title>
    
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
    
     
</head>

<body class=" yui-skin-sam">
    
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
    
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
       
        <h3><a href="<c:url value="/admin/viewContentTypeMarcMappings.action"/>">MARC21 Leader/content type mappings</a> &gt; Edit MARC21 Leader/Content Type MARC Mapping</h3>
  
        <p>Note: This is only a template - you only need to fill in the fields common to all items with the given content type.  
           Some fields like language and dates will be filled in automatically.</p>
        <div id="bd">
		
	  <form action="<c:url value="/admin/saveContentTypeMarcMapping.action"/>"  method="post">
	      <input type="hidden" name="id" value="${id}"/>
	      <div id="error" class="errorMessage">${message}</div>
	      <table class="formTable"> 
	      
           <tr>       
	           <td align="left" class="label">Content Type:*</td>
		       <td align="left" class="input">
		          <select name="contentTypeId">
		          <c:forEach items="${contentTypes}" var="contentType">
		               <option  
		                <c:if test="${contentTypeId == contentType.id}">
		                  selected="true"
		                </c:if>
		               
		               value="${contentType.id}">${contentType.name}</option>
		         </c:forEach>
		         </select> 
		      </td>
	       </tr>
	       <tr>       
	           <td align="left" class="label">Type of Record(Leader/06):</td>
		       <td align="left" class="input">
		          <select name="marcTypeOfRecordId">
		          <c:forEach items="${marcTypeOfRecords}" var="marcTypeOfRecord">
		               <option  
		                <c:if test="${marcTypeOfRecordId == marcTypeOfRecord.id}">
		                  selected="true"
		                </c:if>
		               
		               value="${marcTypeOfRecord.id}">${marcTypeOfRecord.name} - ${marcTypeOfRecord.recordType}</option>
		         </c:forEach>
		         </select> 
		      </td>
	       </tr>
	       <tr>       
	           <td align="left" class="label">Thesis Content Type</td>
		       <td align="left" class="input">
		           <input type="checkbox"  name="thesis" <c:if test="${thesis}">checked="true"</c:if> value="true"/>
		       </td>
	       </tr>
	       <tr>       
	           <td align="left" class="label">Record Status(Leader/05)</td>
		       <td align="left" class="input">
		           <input type="text"  size="1" maxlength="1" name="recordStatus" value="<c:if test="${!empty recordStatus}">${recordStatus}</c:if>"/>
		       </td>
	       </tr>
	       <tr>       
	           <td align="left" class="label">Bibliographic Level(Leader/07)</td>
		       <td align="left" class="input">
		           <input type="text"  size="1" maxlength="1" name="bibliographicLevel" value="<c:if test="${!empty bibliographicLevel}">${bibliographicLevel}</c:if>"/>
		       </td>
	       </tr>
	       <tr>       
	           <td align="left" class="label">Type Of Control(Leader/08)</td>
		       <td align="left" class="input">
		           <input type="text"  size="1" maxlength="1" name="typeOfControl" value="<c:if test="${!empty typeOfControl}">${typeOfControl}</c:if>"/>
		       </td>
	       </tr>
	       <tr>       
	           <td align="left" class="label">Encoding Level(Leader/17)</td>
		       <td align="left" class="input">
		           <input type="text"  size="1" maxlength="1" name="encodingLevel" value="<c:if test="${!empty encodingLevel }">${encodingLevel}</c:if>"/>
		       </td>
	       </tr>
	       <tr>       
	           <td align="left" class="label">Descriptive Cataloging Form(Leader/18)</td>
		       <td align="left" class="input">
		           <input type="text"  size="1" maxlength="1" name="descriptiveCatalogingForm" value="<c:if test="${!empty descriptiveCatalogingForm }">${descriptiveCatalogingForm}</c:if>"/>
		       </td>
	       </tr>
	       <tr>
	           <td align="left" class="label">006 Field:</td>
	           <td align="left" class="input">
	              
	              <c:forEach items="${field006}" var="theChar"  varStatus="status">
	                  ${status.index} <input type="text"  size="1" maxlength="1" name="cf006_${status.index}" value="<c:if test="${theChar != ' '}">${theChar}</c:if>"/>
	              </c:forEach>
	             <br/>
	             <br/>
	           </td>
	       </tr>
	       
	       <tr>
	           <td align="left" class="label">007 Field:</td>
	           <td align="left" class="input">
	              
	              <c:forEach items="${field007}" var="theChar"  varStatus="status">
	                  ${status.index} <input type="text"  size="1" maxlength="1" name="cf007_${status.index}" value="<c:if test="${theChar != ' '}">${theChar}</c:if>"/>
	              </c:forEach>
	             <br/>
	             <br/>
	           </td>
	       </tr>
	       
	       <tr>
	           <td align="left" class="label">008 Field:</td>
	           <td align="left" class="input">
	              
	              <c:forEach items="${field008}" var="theChar"  varStatus="status">
	                  ${status.index} <input type="text"  size="1" maxlength="1" name="cf008_${status.index}" value="<c:if test="${theChar != ' '}">${theChar}</c:if>"/>
	              </c:forEach>
	             
	           </td>
	       </tr>
	    </table>
	    <input type="submit" value="Save"/>
	    <input type="button" value="Cancel" onclick='javascript: window.location =  "<c:url value="/admin/viewContentTypeMarcMappings.action"/>"'/>
	    </form>
        </div>
        <!--  end body div -->
  
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End  doc div-->
  


</body>
</html>