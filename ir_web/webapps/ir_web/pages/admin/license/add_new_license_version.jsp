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
    <title>New Repository License</title>
    
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

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
        
        <c:url value="viewVersionedLicense.action" var="viewVersionedLicenseUrl">
	        <c:param name="versionedLicenseId" value="${versionedLicense.id}"/>
	    </c:url>
        <h3><a href="<c:url value="/admin/viewRepositoryLicenses.action"/>">Repository Licenses</a> &gt; <a href="${viewVersionedLicenseUrl}">${versionedLicense.name}</a></h3>
      
        <h3>New License Version</h3>
  
        <div id="bd">
 	        
	        <form method="post" class="formTable" action="saveNewLicenseVersion.action">
	            <input type="hidden" name="versionedLicenseId" value="${versionedLicense.id}"/>
	            <table>
	                <tr>
	                    <td class="label"><div class="errorMessage"><ir:printError errors="${fieldErrors}"  key="name"/></div>License Name*:</td>
	                    <td class="input"><input type="text" name="name" size="50" value="${versionedLicense.name}"/></td>
	                </tr>
	                 <tr>     
	                    <td class="label">Description/Revision Notes</td>
	                    <td class="input"><textarea name="description" rows="10" cols="70"></textarea></td>
	                </tr>
	                <tr>     
	                    <td class="label">License Text</td>
	                    <td class="input"><textarea name="text" rows="30" cols="70"></textarea></td>
	                </tr>
	                <tr>
	                    <td class="input" colspan="2" align="center">
	                        <input type="submit" name="action:saveNewLicenseVersion" value="Save"/>
	                        <input type="submit" name="action:viewVersionedLicense" value="Cancel"/>
	                    </td>
	                </tr>
	                
	            </table>
	           
	        </form>
        </div>
      <!--  end body div -->

      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End doc div-->
 
  
</body>
</html>