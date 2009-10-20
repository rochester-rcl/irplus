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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Repository Versioned License</title>
    
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
</head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <h3><a href="<c:url value="/admin/viewRepositoryLicenses.action"/>">Repository Licenses</a> &gt; ${versionedLicense.name} </h3>
  
        <div id="bd">
 	        <c:url var="addNewLicenseVersionUrl" value="/admin/addNewLicenseVersion.action">
 	            <c:param name="versionedLicenseId" value="${versionedLicenseId}"/>
 	        </c:url>
 	        <a href="${addNewLicenseVersionUrl}">Add New Version</a>
 	        <br/>
 	        <br/>
 	        
 	        <h3> Versions </h3>
 	        <div class="dataTable">
	       	<urstb:table width="100%">
	          <urstb:thead>
	            <urstb:tr>
	                <urstb:td>Id</urstb:td>
                    <urstb:td>Name</urstb:td>
                    <urstb:td>Version</urstb:td>
                    <urstb:td>Description</urstb:td>
                    <urstb:td>Last Modified Date</urstb:td>
                    <urstb:td>Modified By</urstb:td>
	            </urstb:tr>
	          </urstb:thead>
	            <urstb:tbody
	                var="licenseVersion" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${versionedLicense.versions}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                        ${licenseVersion.id}
	                        </urstb:td>
	                        <urstb:td>
	                           <c:url value="viewLicenseVersion.action" var="viewLicenseVersionUrl">
	                               <c:param name="version" value="${licenseVersion.versionNumber}"/>
	                               <c:param name="versionedLicenseId" value="${versionedLicense.id}"/>
	                           </c:url>
			                   <a href="${viewLicenseVersionUrl}">${licenseVersion.license.name}</a>
	                        </urstb:td>
	                        <urstb:td>
	                             ${licenseVersion.versionNumber}
	                        </urstb:td>
	                        <urstb:td>
	                             ${licenseVersion.license.description}
	                        </urstb:td>
	                        <urstb:td>
	                             <fmt:formatDate pattern="MM/dd/yyyy HH:mm:ss" value="${licenseVersion.license.dateCreated}"/>
	                        </urstb:td>
	                        <urstb:td>
	                             ${licenseVersion.license.creator.firstName}&nbsp;${licenseVersion.license.creator.lastName}
	                        </urstb:td>
	                        
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>
	        </div>
        </div>
      <!--  end body div -->

      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End doc div-->

  
 
  
</body>
</html>