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

<!-- This page allows a user to edit repository information -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<!--  bundle for messages -->
<fmt:setBundle basename="messages"/>

<html>

    <head>
        <title>Repository Administration</title>
        
        <!-- Medatadata fragment for page cache -->
        <c:import url="/inc/meta-frag.jsp"/>
        
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>

    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/jmesa/jmesa.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <!--  Style for dialog boxes -->
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="page-resources/ur-research/ur-table/ur-table.js"/>
    <ur:js src="page-resources/js/admin/repository.js"/>
   
        
    </head>

    <body class=" yui-skin-sam">
       <!--  yahoo doc 2 template creates a page 950 pixles wide -->
       <div id="doc2">  
       
           <!--  this is the header of the page -->
           <c:import url="/inc/header.jsp"/>
       
       
           <h3>Repository Administration</h3>
           
           <div id="bd">
              <b><fmt:message key="edit_repository.edit"/></b>
    
              <ur:basicForm method="post" name="viewRepository"  
                   action="/ir_web/admin/viewRepository.action">
                  <table class="formTable">
                      <tr>
                          <td class="label">
                              Repository Name:
                          </td>
                          <td class="input" colspan="2">
                              <ur:textInput name="repository.name" value="${repository.name}"/>
                          </td>
                      </tr>
                      <tr>
                          <td class="label">
                              Institution Name:
                          </td>
                          <td class="input" colspan="2">
                              <ur:textInput name="repository.institutionName" value="${repository.institutionName}"/>
                          </td>
                      </tr>
                      <tr>
                          <td class="label">
                              Description:
                          </td>
                          <td class="input" colspan="2">
                              <ur:textArea name="repository.description" 
                                  cols="60" rows="8">${repository.description}</ur:textArea>
                          </td>
                      </tr>
                      <tr>
                          <td class="buttons" colspan="3" >
                             
                             <input type="submit" 
                                 name="action:saveRepository" value="Save"/>
                             
                             <input type="submit" 
                                  name="action:cancelRepository" value="Cancel"/>
                          </td>
                      </tr>
                  </table>
              </ur:basicForm>
   
              
              <c:url var="reIndexItemsUrl" value="/admin/reIndexInstitutionalItems.action"/>
              <a href="${reIndexItemsUrl}">Re-Index Institutional Items</a>
              <br/>
              <br/>
              
              <c:url var="addPicture" value="/admin/addRepositoryPicture.action"/>
              
              <a href="${addPicture}">Add Repository Picture</a>
              
              <br/>
              <br/>
            
              <div id="repository_pictures">
                  <table class="simpleTable">
                      <thead>
                          <tr>    
	                          <th>
	                              Thumbnail
	                          </th>
	                          <th>
	                              Description
	                          </th>
	                          <th>
	                              Created Date
	                          </th>
	                          <th>
	                              Created By
	                          </th>
	                          <th>
	                              Delete
	                          </th>
                          </tr>
                      </thead>
                      <tbody>
                          <c:forEach var="irFile" varStatus="status" items="${repository.pictures}">
                              <c:if test="${ (status.count % 2) == 0}">
                                  <c:set value="even" var="rowType"/>
                              </c:if>
                              <c:if test="${ (status.count % 2) == 1}">
                                  <c:set value="odd" var="rowType"/>
                              </c:if>
                         
                           <tr>
                               <td class="${rowType}">
                                   <ir:transformUrl systemCode="PRIMARY_THUMBNAIL" download="true" irFile="${irFile}" var="url"/>
                                   <c:if test="${url != null}">
                                      <img src="${url}"/></a>
                                   </c:if>
                                
                               </td>
     
                               <td class="${rowType}">${irFile.description}</td>
                               <td class="${rowType}">${irFile.fileInfo.createdDate}</td>
                               <td class="${rowType}">${irFile.owner.username}</td>
                               <td class="${rowType}">
 		                              <button class="ur_button" 
 		                               onmouseover="this.className='ur_buttonover';"
 		                               onmouseout="this.className='ur_button';"
 		                               onclick="javascript:YAHOO.ur.repository.confirmPictureDelete(${irFile.id});"
 		                               id="showFolder">Delete Picture</button> 
	                           </td>
                           </tr>
                  
                           </c:forEach>  
                       </tbody>  
                   </table>
              </div>
           </div>
          <!--  end bd div -->
          
          <!--  this is the footer of the page -->
          <c:import url="/inc/footer.jsp"/>
          
     </div>
     <!--  End doc div-->
  
  	 <ur:div id="deletePictureDialog">
	             <ur:div cssClass="hd">Delete Repository Picture</ur:div>
		         <ur:div cssClass="bd">
		             <ur:basicForm id="delete_repository_picture" 
		                  name="deleteRepositoryPicture" method="POST" 
		                  action="admin/deleteRepositoryPicture.action">
		              <input type="hidden" id="picture_id" name="irFilePictureId" value=""/>
		              
		              <ur:div id="deletePictureError" cssClass="errorMessage"></ur:div>
			          <p>Are you sure you wish to delete the selected picture?</p>
		          </ur:basicForm>
		      </ur:div>
	      </ur:div>
</body>
</html>