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
    <title>Group Workspace: ${groupWorkspace.name}</title>
    
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
 	<ur:js src="page-resources/js/admin/edit_group_workspace.js"/>
</head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
      
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <h3><a href="<c:url value="/admin/viewGroupWorkspaces.action"/>">Group Workspaces</a> &gt; Editing: ${groupWorkspace.name}</h3>
  
        <div id="bd">
            <div id="groupWorkspacePropertiesTabs" class="yui-navset">
	             <ul class="yui-nav">
                     <li class="selected"><a href="#tab1"><em>Group Workspace Information</em></a></li>
                     <li><a href="#tab2"><em>Owner(s)</em></a></li>
                     <li ><a href="#tab3"><em>Group(s)</em></a></li>
                 </ul>

                 <div class="yui-content">
                     <!--  first tab -->
                     <div id="tab1">
                         <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                                    key="groupWorkspace.name"/></p>
		                 <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                                      key="groupWorkspaceAlreadyExists"/></p>
                         <form id="editGroupWorkspaceInformation" name="groupWorkspaceInformation" method="post" 
                             action="<c:url value="/admin/updateGroupWorkspace.action"/>">
                            <input type="hidden" id="groupWorkspaceId" 
                                  name="id" value="${groupWorkspace.id}" />
                            <table class="formTable">
                                <tr>
                                    <td class="label">
                                        <strong>Name:</strong>
                                    </td>
                                    <td class="input">
                                        <input type="text" size="75" name="name" 
                                               value="<c:out value='${groupWorkspace.name}'/>"/>
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
                                        rows="20" cols="75"><c:out value='${groupWorkspace.description}'/></textarea>
                                    </td>
                                </tr>
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
                         <h3>Owners</h3>
                        
                     </div>
                     <!--  end second tab -->
                     
                     <!--  third tab -->
                     <div id="tab3">
                         tab 3
                     </div>
                     <!--  end third tab -->
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