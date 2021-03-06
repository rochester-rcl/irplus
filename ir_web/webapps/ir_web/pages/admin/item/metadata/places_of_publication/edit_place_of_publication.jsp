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
    <title>Create/Edit Place of Publication</title>
    
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
      
       
        <h3><a href="<c:url value="/admin/viewPlacesOfPublication.action"/>">Places of Publication</a> &gt; Create/Edit Place of Publication</h3>
        <div id="bd">
		
	  <form action="<c:url value="/admin/savePlaceOfPublication.action"/>"  method="post">
	      <input type="hidden" name="id" value="${placeOfPublication.id}"/>
	      <div id="error" class="errorMessage">${message}</div>
	      <table class="formTable"> 
	      
	       <tr>       
	           <td align="left" class="label">Name*</td>
		       <td align="left" class="input">
		           <input type="text"  size="50"  name="placeOfPublication.name" value="<c:out value="${placeOfPublication.name}"/>"/>
		       </td>
	       </tr>
	       <tr>       
	           <td align="left" class="label">2/3 Letter Code</td>
		       <td align="left" class="input">
		           <input type="text"  size="3" maxlength="3" name="placeOfPublication.letterCode" value="<c:out value="${placeOfPublication.letterCode}"/>"/>
		       </td>
	       </tr>
	       <tr>
	           <td align="left" class="label">Description:</td>
		       <td colspan="2" align="left" class="input"><textarea  
		           name="placeOfPublication.description" cols="42" rows="4"><c:out value="${placeOfPublication.description}"/></textarea></td>
	      </tr>
	       
	    </table>
	    <input type="submit" value="Save"/>
	     <input type="button" value="Cancel" onclick='javascript: window.location =  "<c:url value="/admin/viewPlacesOfPublication.action"/>"'/>
	    </form>
        </div>
        <!--  end body div -->
  
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End  doc div-->
  


</body>
</html>