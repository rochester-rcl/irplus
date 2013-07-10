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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Checksum Information</title>
    
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>

    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <!--  required imports -->
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="pages/js/ur_table.js"/>
    <script type="text/javascript">
		  function updateUrl()
		  {
			  document.checksumStatus.submit();
		  }
    </script>
</head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
      
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <h3>Checksum Information</h3>
         <c:url var="viewFileInfoChecksums" value="/admin/viewFileInfoChecksums.action"/>
          <c:url var="viewResetAllFailingChecksums" value="/admin/viewResetAllFailingChecksums.action"/>
          
         
        <div id="bd">
            <form name="checksumStatus" action="${viewFileInfoChecksums}" method="get">
				             <input type="hidden" name="rowStart" value="0"/>
			                 <input type="hidden" name="startPageNumber" value="1"/>
			                 <input type="hidden" name="currentPageNumber" value="1"/>
			                 <input type="hidden" name="sortElement" value="${sortElement}"/>		
			                 <input type="hidden" name="sortType" value="${sortType}"/>	
				         Filter By Status:
				         <select name="onlyFails" onchange="javascript:updateUrl();">
				             <option value="false" <c:if test="${!onlyFails}"> selected="selected" </c:if>>All</option>
				             <option value="true"  <c:if test="${onlyFails}"> selected="selected" </c:if>>Check Failed</option>
						 </select>
			</form>
			<br/>
			<a href="${viewResetAllFailingChecksums}">Reset all failing checksums</a>
			<br/>
            <c:import url="checksum_info_table.jsp"/>
            
         </div>
        <!--  end body div -->
      
        <!--  this is the footer of the page -->
        <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End doc div-->
  


</body>
</html>