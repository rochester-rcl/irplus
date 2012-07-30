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

<!-- This page allows a user to edit repository information -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>


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
    <ur:styleSheet href="page-resources/css/tables.css"/>
    <ur:styleSheet href="page-resources/yui/tabview/assets/skins/sam/tabview.css"/>
    <ur:styleSheet href="page-resources/yui/tabview/assets/border_tabs.css"/>    
    
    <!--  Style for dialog boxes -->
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	<ur:js src="page-resources/yui/tabview/tabview-min.js"/>
 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="page-resources/js/admin/repository.js"/>
   
    <script type="text/javascript">
       var myTabs = new YAHOO.widget.TabView("edit-repository-tabs");
    </script>    
    </head>

    <body class=" yui-skin-sam">
       <!--  yahoo doc 2 template creates a page 950 pixles wide -->
       <div id="doc2">  
       
           <!--  this is the header of the page -->
           <c:import url="/inc/header.jsp"/>
       
           <h3>Repository Administration - <fmt:message key="edit_repository.edit"/></h3>
           
           <div id="bd">
              
               <div id="edit-repository-tabs" class="yui-navset">
                
                   <ul class="yui-nav">
                       <li class="selected"><a href="#tab1"><em>Repository Information</em></a></li>
                       <li><a href="#tab2"><em>Repository Pictures</em></a></li>
                       <li><a href="#tab3"><em>Retired Licenses</em></a></li>
                   </ul>
                   
                   <div class="yui-content">
                       <!--  first tab -->
                       <div id="tab1">
                           <form method="post" name="viewRepository" action="admin/viewRepository.action">
                   
                           <table class="formTable">
                           <tr>
                               <td class="label">  Repository Name: </td>
                               <td class="input" colspan="2">
                                   <input size="50" name="repositoryName" value="${repository.name}"/>
                               </td>
                           </tr>
                           <tr>
                               <td class="label"> Institution Name: </td>
                               <td class="input" colspan="2">
                                  <input size="50" name="institutionName" value="${repository.institutionName}"/>
                               </td>
                           </tr>
                           <tr>
                               <td class="label"> Default Handle Name Authority:</td>
                               <td class="input" colspan="2">
                                   <select name="handleNameAuthorityId">
                                       <c:if test="${repository.defaultHandleNameAuthority == null}">
                                           <option selected="true" value="-1">No Naming Authority</option>
                                       </c:if>
                                       <c:if test="${repository.defaultHandleNameAuthority != null}">
                                           <option value="-1">No Naming Authority</option>
                                       </c:if>
                                       <c:forEach var="nameAuthority" items="${handleNameAuthorities}">
                                           <c:if test="${repository.defaultHandleNameAuthority.id == nameAuthority.id}">
                                               <option selected="true" value="${nameAuthority.id}">${nameAuthority.namingAuthority}</option>
                                           </c:if>
                                           <c:if test="${repository.defaultHandleNameAuthority.id != nameAuthority.id}">
                                               <option value="${nameAuthority.id}">${nameAuthority.namingAuthority}</option>
                                           </c:if>
                                       </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td class="label"> Current File Database:  </td>
                                <td class="input" colspan="2">
                                    <select name="defaultFileDatabaseId">
                                        <c:if test="${repository.fileDatabase == null}">
                                          <option selected="true" value="-1">No File Database</option>
                                        </c:if>
                                        <c:if test="${repository.fileDatabase != null}">
                                            <option value="-1">No File Database</option>
                                        </c:if>
                                        <c:forEach var="fileDatabase" items="${fileDatabases}">
                                            <c:if test="${repository.fileDatabase.id == fileDatabase.id}">
                                                <option selected="true" value="${fileDatabase.id}">${fileDatabase.name}</option>
                                            </c:if>
                                            <c:if test="${repository.fileDatabase.id != fileDatabase.id}">
                                                <option value="${fileDatabase.id}">${fileDatabase.name}</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td class="label"> Current License:</td>
                                <td class="input" colspan="2">
                                    <select name="defaultLicenseVersionId">
                                        <c:if test="${repository.defaultLicense == null}">
                                            <option selected="true" value="-1">No License</option>
                                        </c:if>
                                        <c:if test="${repository.defaultLicense != null}">
                                            <option value="-1">No License</option>
                                        </c:if>
                                        <c:forEach var="licenseVersion" items="${licenses}">
                                            <c:if test="${repository.defaultLicense.id == licenseVersion.id}">
                                               <option selected="true" value="${licenseVersion.id}">${licenseVersion.license.name} - [version&nbsp;${licenseVersion.versionNumber}]</option>
                                            </c:if>
                                            <c:if test="${repository.defaultLicense.id != licenseVersion.id}">
                                                <option value="${licenseVersion.id}">${licenseVersion.license.name} - [version&nbsp;${licenseVersion.versionNumber}]</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                    <td class="label"> Description: </td>
                                    <td class="input" colspan="2">
                                        <textarea name="repository.description" 
                                                  cols="60" rows="8">${repository.description}</textarea>
                                    </td>
                            </tr>
                            <tr>
                                    <td class="label"> Suspend Subscription Emails: </td>
                                    <td class="input" colspan="2">
                                        <input type="checkbox" name="suspendSubscriptions"  value="true" 
                                            <c:if test="${repository.suspendSuscriptionEmails}">
                                                   checked="true"
                                             </c:if>
                                         />
                                     </td>
                            </tr>
                            <tr>
                                     <td colspan="3" class="label">
                                         <h3>The index locations must be valid paths for the file system in use (Windows C:\  Unix / )</h3>
                                     </td>
                             </tr>
                             <tr>
                                     <td class="label">Name Authority Index Folder Location:</td>
                                     <td class="input" colspan="2">
                                         <input size="80" name="nameIndexFolder" value="${repository.nameIndexFolder}"/>
                                     </td>
                            </tr>
                            <tr>
                                     <td class="label">User Index Folder Location: </td>
                                     <td class="input" colspan="2">
                                         <input size="80" name="userIndexFolder" value="${repository.userIndexFolder}"/>
                                     </td>
                                 </tr>
                                 <tr>
                                     <td class="label"> Researcher Index Folder Location: </td>
                                     <td class="input" colspan="2">
                                         <input size="80" name="researcherIndexFolder" value="${repository.researcherIndexFolder}"/>
                                     </td>
                                 </tr>
                                 <tr>
                                     <td class="label"> Institutional Item Index Folder Location: </td>
                                     <td class="input" colspan="2">
                                         <input size="80" name="institutionalItemIndexFolder" value="${repository.institutionalItemIndexFolder}"/>
                                     </td>
                                 </tr>
                                 <tr>
                                     <td class="label"> User Workspace Folder Location: </td>
                                     <td class="input" colspan="2">
                                         <input size="80" name="userWorkspaceIndexFolder" value="${repository.userWorkspaceIndexFolder}"/>
                                     </td>
                                 </tr>
                                 <tr>
                                     <td class="buttons" colspan="3" >
                             
                                         <c:if test="${repository == null}">
                                             <input type="submit" name="action:createRepository" value="Create"/>
                                         </c:if>
                                         <c:if test="${repository != null}">
                                             <input type="submit" name="action:updateRepository" value="Save"/>
                                         </c:if>
                             
                                         <input type="submit"  name="action:cancelRepository" value="Cancel"/>
                                     </td>
                                 </tr>
                             </table>
                        </form>
   
                       <c:if test="${repository != null}">
                           Last subscription process email date: ${repository.lastSubscriptionProcessEmailDate}
                           <br/>
                           <br/>
                           
                           <c:url var="reIndexItemsUrl" value="/admin/reIndexInstitutionalItems.action"/>
                           <a href="${reIndexItemsUrl}">Re-Index Institutional Items</a>
                           
                           <br/>
                           <br/>
                           
                           <a href="<c:url value="/admin/reIndexUsers.action"/>">Re-Index Users</a>
                           
                           <br/>
                           <br/>
                           
                           <a href="<c:url value="/admin/reIndexResearchers.action"/>">Re-Index Researchers</a>
                           
                           <br/>
                           <br/>
                           
                           <a href="<c:url value="/admin/reIndexPersonNameAuthorities.action"/>">Re-Index Person Name Authorities</a>
                           
                           <br/>
                           <br/>
                           
                           <c:url var="resetAllHandles" value="/admin/resetAllHandles.action"/>
                           
                           <a href="${resetAllHandles}">Reset all handles</a>
                           <br/>
                           <br/>
                       </c:if>
		           </div>
	               <!--  end first tab -->
	                  
              	   <!--  start second tab -->
               	   <div id="tab2">
                       <c:if test="${repository != null}">
                       <c:url var="addPicture" value="/admin/addRepositoryPicture.action"/>
              
                       <a href="${addPicture}">Add Repository Picture</a>
              
                       <br/>
                       <br/>
            
                       <div id="repository_pictures">
                           <c:import url="repository_pictures_frag.jsp"/>
                        </div>
                    </c:if>
				   </div>
	               <!--  end tab 2 -->
	             
                   <!--  Start third tab -->
                   <div id="tab3">
                       <div class="dataTable">
	                       <urstb:table width="100%">
	                           <urstb:thead>
	                               <urstb:tr>
					                   <urstb:td>Retired License Id</urstb:td>         
	                                   <urstb:td>License Name</urstb:td>
	                                   <urstb:td>Version</urstb:td>
	                                   <urstb:td>Date Retired</urstb:td>
	                                   <urstb:td>Retired By</urstb:td>
	                               </urstb:tr>
	                           </urstb:thead>
	                           <urstb:tbody
	                                var="retiredLicense" 
	                                oddRowClass="odd"
	                                evenRowClass="even"
	                                currentRowClassVar="rowClass"
	                                collection="${repository.retiredLicenses}">
	                                <urstb:tr  cssClass="${rowClass}"
	                                    onMouseOver="this.className='highlight'"
	                                    onMouseOut="this.className='${rowClass}'">
	                                    <urstb:td> ${retiredLicense.id} </urstb:td>
	                                    <urstb:td> ${retiredLicense.licenseVersion.license.name}</urstb:td>
	                                    <urstb:td> ${retiredLicense.licenseVersion.versionNumber}</urstb:td>
	                                    <urstb:td> ${retiredLicense.dateRetired}</urstb:td>
	                                    <urstb:td> ${retiredLicense.retiredBy.firstName}&nbsp;${retiredLicense.retiredBy.lastName}</urstb:td>
	                               </urstb:tr>
	                           </urstb:tbody>
	                       </urstb:table>
                       </div>	
                   </div>
                   <!--  End third tab -->
                   
 	           </div>
	           <!--  end content -->
	       </div>
	       <!--  end tabs -->
          

              
  
           </div>
          <!--  end bd div -->
          
          <!--  this is the footer of the page -->
          <c:import url="/inc/footer.jsp"/>
          
     </div>
     <!--  End doc div-->
  
     
  	 <div id="deletePictureDialog">
	     <div class="hd">Delete Repository Picture</div>
		 <div class="bd">
		     <form id="delete_repository_picture" 
		           name="deleteRepositoryPicture" method="POST" 
		           action="<c:url value="/admin/deleteRepositoryPicture.action"/>">
		         <input type="hidden" id="picture_id" name="irFilePictureId" value=""/>
		              
		         <div id="deletePictureError" class="errorMessage"></div>
			     <p>Are you sure you wish to delete the selected picture?</p>
		     </form>
		 </div>
	 </div>
</body>
</html>