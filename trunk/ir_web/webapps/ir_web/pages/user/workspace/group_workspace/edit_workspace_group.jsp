<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />


<!--  
   Copyright 2008-2011 University of Rochester

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
    <title>Group: ${groupSpace.name}</title>
    
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
 	<ur:js src="page-resources/yui/tabview/tabview-min.js"/>
 		
 	<ur:js src="page-resources/js/util/base_path.jsp"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
 	
</head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
      
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <c:url var="workspaceUrl" value="/user/workspace.action">
            <c:param name="tabName" value="GROUP_WORKSPACE"/>
        </c:url>
         <c:url var="groupWorkspaceUrl" value="/user/getGroupWorkspace.action">
            <c:param name="id" value="${workspaceGroup.groupWorkspace.id}"/>
        </c:url>
        <h3><a href="${workspaceUrl}">Group Workspaces</a>  &gt; <a href="${groupWorkspaceUrl}">${workspaceGroup.groupWorkspace.name}</a> &gt; Editing Group: ${workspaceGroup.name}</h3>
  
        <div id="bd">
            <div id="workspaceGroupPropertiesTabs" class="yui-navset">
	             <ul class="yui-nav">
                     <li class="selected"><a href="#tab1"><em>Group Information</em></a></li>
                     <li><a href="#tab2"><em>Group Members</em></a></li>
                 </ul>

                 <div class="yui-content">
                     <!--  first tab -->
                     <div id="tab1">
                         <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                                    key="workspaceGroup.name"/></p>
		                 <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                                      key="workspaceGroupAlreadyExists"/></p>
                         <form id="editWorkspaceGroupInformation" name="workspaceGroupInformation" method="post" 
                             action="<c:url value="/user/updateWorkspaceGroup.action"/>">
                            <input type="hidden" id="workspaceGroupId" 
                                  name="id" value="${workspaceGroup.id}" />
                            <table class="formTable">
                                <tr>
                                    <td class="label">
                                        <strong>Name:</strong>
                                    </td>
                                    <td class="input">
                                        <input type="text" size="75" name="name" 
                                               value="<c:out value='${workspaceGroup.name}'/>"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label">
                                        <strong>Description</strong>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="input" colspan="2">
                                        <textarea name="description" 
                                        rows="20" cols="75"><c:out value='${workspaceGroup.description}'/></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label" colspan="2">
                                        <strong>Permissions group: ${workspaceGroup.name} has on workspace: ${workspaceGroup.groupWorkspace.name}</strong>
                                    </td>
                                </tr>
                                <c:forEach var="classTypePermission" items="${classTypePermissions}">
	                            <tr>
			                        <td>  
			                            <input type="checkbox" name="selectedPermissions" id="${classTypePermission.name}" value="${classTypePermission.id}"/>
			                        </td>
				                    <td>${classTypePermission.name} -  ${classTypePermission.description}</td>
			                    </tr>
			                    </c:forEach>
                                <tr>
                                   <td>
                                       <input type="submit"  value="save"/>
                                   </td>
                                </tr>
                            </table>
                       </form>
                     </div>
                     <!--  end first tab -->
                     
                     <!--  second tab -->
                     <div id="tab2">
                           
                     </div>
                     <!--  end second tab -->
                     
                    
                 </div>
                 
            </div>
        
        </div>
        <!--  end body div -->
      
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End doc div-->
  
 

</body>
</html>