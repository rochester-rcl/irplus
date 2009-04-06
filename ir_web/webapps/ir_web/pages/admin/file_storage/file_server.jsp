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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<fmt:setBundle basename="messages"/>

<html>
<head>
        
   <title>View File Server</title>
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
    
    
    <!--  Style for dialog boxes -->
    <style>
 
        /* this is a simple fix for geco based browsers
         * this does have side affects if scroll bars are used.
         * in geco based browsers see cursor fix on yahoo
         * http://developer.yahoo.com/yui/container/
         */
        .mask 
        {
            overflow:visible; /* or overflow:hidden */
        }
    </style>
        

</head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">
                <h3>Viewing File Server: ${fileServer.name}</h3>
                
                <h3>File Databases</h3>
                <div class="dataTable">
	               <form method="POST" id="fileServers" name="fileServers" >
	                   <urstb:table width="100%">
	                   <urstb:thead>
	                       <urstb:tr>
	                           <urstb:td>Id</urstb:td>
                               <urstb:td>Name</urstb:td>
                               <urstb:td>Path</urstb:td>
                               <urstb:td>Description</urstb:td>
	                       </urstb:tr>
	                   </urstb:thead>
	                   <urstb:tbody
	                       var="fileDatabase" 
	                       oddRowClass="odd"
	                       evenRowClass="even"
	                       currentRowClassVar="rowClass"
	                       collection="${fileServer.fileDatabases}">
	                       
	                       <urstb:tr 
	                            cssClass="${rowClass}"
	                            onMouseOver="this.className='highlight'"
	                            onMouseOut="this.className='${rowClass}'">
	                            
	                            <urstb:td>
		                            ${fileDatabase.id}
	                            </urstb:td>
	                            <urstb:td>
			                         <a href="">${fileDatabase.name}</a>
	                            </urstb:td>
	                            <urstb:td>
			                        ${fileDatabase.path}</a>
	                            </urstb:td>
	                            <urstb:td>
			                        <a href="">${fileDatabase.description}</a>
	                            </urstb:td>
	                        </urstb:tr>
	                   </urstb:tbody>
	               </urstb:table>
              </form>
            </div>
            	
          </div>
          <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
  
   
    </body>
</html>