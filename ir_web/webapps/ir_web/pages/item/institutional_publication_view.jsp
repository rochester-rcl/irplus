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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<fmt:setBundle basename="messages"/>

<html>
    <head>
        <title>Viewing Institutional Publication: ${institutionalItemVersion.item.name}</title>
        <c:import url="/inc/meta-frag.jsp"/>
                
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>

    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <!--  required imports -->
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
    <ur:js src="page-resources/yui/menu/menu-min.js"/>
    
 	<!--  base path information -->
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="pages/js/ur_table.js"/>
    <ur:js src="page-resources/js/public/institutional_publication_view.js"/>
    
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

 	    
 	</head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">
				<h3><a href="home.action">irplus</a> &gt;
                  <c:forEach var="collection" items="${path}">
                                    <c:url var="pathCollectionUrl" value="/viewInstitutionalCollection.action">
                                         <c:param name="collectionId" value="${collection.id}"/>
                                    </c:url>
                                    <a href="${pathCollectionUrl}">${collection.name}</a> &gt;
                                </c:forEach>
                </h3>
				<!-- Begin - Display the Item preview -->
				
				<h3>${institutionalItemVersion.item.name}</h3>
				
				<c:if test="${institutionalItemVersion.handleInfo != null}">
				    <h3 class="errorMessage">URL to cite or link to: <a href="${institutionalItemVersion.handleInfo.nameAuthority.authorityBaseUrl}${institutionalItemVersion.handleInfo.nameAuthority.namingAuthority}/${institutionalItemVersion.handleInfo.localName}">${institutionalItemVersion.handleInfo.nameAuthority.authorityBaseUrl}${institutionalItemVersion.handleInfo.nameAuthority.namingAuthority}/${institutionalItemVersion.handleInfo.localName}</a></h3>
				</c:if>
				
				<c:if test="${showPublication }">
				    <c:if test="${institutionalItemVersion.item.embargoed}">
				        <div class="errorMessage"> <h3>This publication is still under embargo</h3> </div>
				    </c:if>
					<c:if test="${institutionalItemVersion.withdrawn}">
						<div class="errorMessage"> <h3> This publication is withdrawn. </h3> </div>
						<p class="errorMessage">
						    <br/>
						    Withdrawn By: ${institutionalItemVersion.withdrawnToken.user.firstName} ${institutionalItemVersion.withdrawnToken.user.lastName}
						    <br/>
						    Reason: ${institutionalItemVersion.withdrawnToken.reason}
						    <br/>
						    Date Withdrawn: ${institutionalItemVersion.withdrawnToken.date}
						</p>
					</c:if>
								
					<c:if test="${!institutionalItemVersion.withdrawn  || ir:userHasRole('ROLE_ADMIN', '') || institutionalItem.owner == user}">
					    <c:import url="item_files_frag.jsp">
					      <c:param name="isPreview" value="false"/>
					    </c:import>
					</c:if>
				
					<c:if test="${!institutionalItemVersion.withdrawn || institutionalItemVersion.withdrawnToken.showMetadata  
								              || ir:userHasRole('ROLE_ADMIN', '') || institutionalItem.owner == user}">
	                     <c:import url="item_metadata_frag.jsp"/>
				    </c:if>
					<!-- End - Display the Item preview -->
				
	
                    <!-- if statements for the buttons the forms are below this in a separate statements 
                         this is due to formatting in IE 6 -->
					<c:if test="${user != null && (institutionalItem.owner == user) || ir:userHasRole('ROLE_ADMIN', '')}">
					    
						<!--  only allow editing if this is the current largest version -->
						<c:if test="${institutionalItemVersion.versionNumber == institutionalItem.versionedInstitutionalItem.largestVersion}">
							     <button class="ur_button" 
						                       onmouseover="this.className='ur_buttonover';"
					 		                   onmouseout="this.className='ur_button';"
						 		               onclick="javascript:document.editForm.submit();"
						                       id="edit_publication">Edit Publication</button> 
						</c:if>
				    </c:if>
				    
				    <c:if test="${user != null && (institutionalItem.owner == user) || ir:userHasRole('ROLE_ADMIN', '')}">
							<c:if test="${!institutionalItemVersion.withdrawn}">
								<button class="ur_button" 
					                       onmouseover="this.className='ur_buttonover';"
				 		                   onmouseout="this.className='ur_button';"
					                       id="withdraw_publication">Withdraw Publication</button>     	        
							</c:if>
							<c:if test="${institutionalItemVersion.withdrawn}">
								<button class="ur_button" 
					                       onmouseover="this.className='ur_buttonover';"
				 		                   onmouseout="this.className='ur_button';"
					                       id="reinstate_publication">Reinstate Publication</button>     	        
							</c:if>					
		 	        </c:if>
		 	        
		 	        <c:if test="${user != null && (ir:userHasRole('ROLE_ADMIN', '')) }">
		 				<button class="ur_button" 
				                       onmouseover="this.className='ur_buttonover';"
			 		                   onmouseout="this.className='ur_button';"
			 		                   onclick="javascript:document.movePublicationForm.submit();"
				                       id="move_publication">Move Publication</button>
		 		   </c:if>
		 		   
		 		   <c:if test="${user != null && (ir:userHasRole('ROLE_ADMIN', '')) }">
						  <button class="ur_button" 
					                       onmouseover="this.className='ur_buttonover';"
				 		                   onmouseout="this.className='ur_button';"
				 		                   onclick="javascript:document.permissionForm.submit();"
					                       id="manage_permissions">Manage Permissions</button>  
				   </c:if>
				   <c:if test="${user != null && (institutionalItem.owner == user) || ir:userHasRole('ROLE_ADMIN', '')}">
						
						<button class="ur_button" type="submit" 
					                       onmouseover="this.className='ur_buttonover';"
				 		                   onmouseout="this.className='ur_button';"
				 		                   onclick="javascript:document.newVersionForm.submit();"
					                       id="new_version">Add New Version</button>
				 </c:if>
				 <c:if test="${user != null && (ir:userHasRole('ROLE_ADMIN', '')) }">	
					    <button class="ur_button" 
					            onmouseover="this.className='ur_buttonover';"
				 		        onmouseout="this.className='ur_button';"
					            id="delete_item">Delete</button>    
						
			    </c:if>   
				
				<c:if test="${user != null && (institutionalItem.owner == user) || ir:userHasRole('ROLE_ADMIN,ROLE_RESEARCHER', 'OR')}">
						  <button class="ur_button" type="submit" 
					                       onmouseover="this.className='ur_buttonover';"
				 		                   onmouseout="this.className='ur_button';"
				 		                   onclick="javascript:document.addToResearcherPageForm.submit();"
					                       id="add_researcher_page">Add to My Researcher page</button>    
				</c:if>
				
					
				<c:if test="${user != null && (institutionalItem.owner == user) || ir:userHasRole('ROLE_ADMIN', '')}">
					    
						<!--  only allow editing if this is the current largest version -->
						<c:if test="${institutionalItemVersion.versionNumber == institutionalItem.versionedInstitutionalItem.largestVersion}">
					           <form name="editForm" 
					              method="post" action="<c:url value="/user/viewEditItem.action"/>">
									<input type="hidden" name="institutionalItemId" value="${institutionalItem.id}"/>
									<input type="hidden" name="genericItemId" value="${institutionalItemVersion.item.id}"/>
					            </form>  			                        	        
						</c:if>
				    </c:if>
				    
				   
		 	        <c:if test="${user != null && (ir:userHasRole('ROLE_ADMIN', '')) }">
		 				
				           <form name="movePublicationForm" 
				              method="post" action="<c:url value="/admin/viewMoveInstitutionalItemLocations.action"/>">
								
								<input type="hidden" id="move_items_destination_id" name="destinationId" value="${institutionalItem.institutionalCollection.id}"/>
								<input type="hidden" id="move_items_item_ids" name="itemIds" value="${institutionalItemId}"/>
				            </form>  
		 				
		 		   </c:if>
		 		   <c:if test="${user != null && (ir:userHasRole('ROLE_ADMIN', '')) }">
						
				           <form name="permissionForm" 
				              method="post" action="<c:url value="/admin/viewInstitutionalItemPermissions.action"/>">
								
								<input type="hidden" id="permissions_item_id" name="institutionalItemId" value="${institutionalItem.id}"/>

				            </form>  			                        	        
				   </c:if>
				   <c:if test="${user != null && (institutionalItem.owner == user) || ir:userHasRole('ROLE_ADMIN', '')}">
						
				           <form name="newVersionForm" 
				              method="post" action="<c:url value="/user/viewAddInstitutionalItemVersion.action"/>">
								
								<input type="hidden" id="institutional_item_id" name="institutionalItemId" value="${institutionalItem.id}"/>
				            </form>  			                        	        
						
				 </c:if>
				 <c:if test="${user != null && (ir:userHasRole('ROLE_ADMIN', '')) }">	
						 
				           <form name="deleteForm" method="post" action="<c:url value="/admin/deleteInstitutionalItem.action"/>">
								<input type="hidden" id="institutional_item_id" name="institutionalItemId" value="${institutionalItem.id}"/>
				            </form>  			                        	        
			      </c:if>   
				
					<c:if test="${user != null && (institutionalItem.owner == user) || ir:userHasRole('ROLE_ADMIN,ROLE_RESEARCHER', 'OR')}">
						
				           <form name="addToResearcherPageForm" 
				              method="post" action="<c:url value="/user/viewResearcherInstitutionalItem.action"/>">
								
								<input type="hidden" id="institutional_item_id" name="institutionalItemId" value="${institutionalItem.id}"/>
								<input type="hidden" id="researcher_id" name="researcherId" value="${user.researcher.id}"/>
				            </form>  			                        	        
										
					</c:if>
					
			</c:if>
			<!--  end if for show publication -->
				
			<c:if test="${!showPublication && !ir:userHasRole('ROLE_ADMIN', '')}">
				 <div class="errorMessage"> <h3>${message}</h3></div> 
			</c:if>
				
			  <!-- *************************  All versions Start *************************  -->
            <c:if test="${institutionalItem != null}">
              
              <h3>All Versions</h3>
          
              <table class="simpleTable">
                  <thead>
                      <tr>    
	                      <th>
	                          Thumbnail
	                      </th>
	                      <th>
	                          Name
	                      </th>
	                      <th>
	                          File Version
	                      </th>
	                      <th>
	                          Created Date
	                      </th>
                      </tr>
                  </thead>
                  <tbody>
                      <c:forEach var="version" varStatus="status" items="${institutionalItem.versionedInstitutionalItem.institutionalItemVersions}">
                      <c:if test="${ (status.count % 2) == 0}">
                          <c:set value="even" var="rowType"/>
                      </c:if>
                      <c:if test="${ (status.count % 2) == 1}">
                          <c:set value="odd" var="rowType"/>
                      </c:if>
                      <tr>
                          <c:if test="${version.item.publiclyViewable || (institutionalItem.owner == user) || ir:userHasRole('ROLE_ADMIN', '') || showPublication}">
                          <td class="${rowType}">
                              <ir:transformUrl systemCode="PRIMARY_THUMBNAIL" download="true" irFile="${version.item.primaryImageFile.irFile}" var="url"/>
                              <c:if test="${url != null}">
                                  <img src="${url}"/>
                              </c:if>
                              
                          </td>
                          <td class="${rowType}">${version.item.name}
                          	<c:if test="${version.withdrawn}">
                          		<div class="errorMessage">(withdrawn on ${version.withdrawnToken.date})</div>
                          	</c:if>
                          </td>
                          <c:url var="publicationVersionDownloadUrl" value="institutionalPublicationPublicView.action">
	                          <c:param name="institutionalItemId" value="${institutionalItem.id}"/>
	                          <c:param name="versionNumber" value="${version.versionNumber}"/>
	                      </c:url>
                          <td class="${rowType}"><a href="${publicationVersionDownloadUrl}">${version.versionNumber}</a></td>
                          <td class="${rowType}">${version.dateOfDeposit}</td>
                          </c:if>
                          <c:if test="${!version.item.publiclyViewable && !(institutionalItem.owner == user) && !ir:userHasRole('ROLE_ADMIN', '') && !showPublication}">
                              <td class="${rowType}" colspan="4">
                                  The version of this item is private
                              </td>
                          </c:if>

                     </tr>
                     </c:forEach>  
                 </tbody>  
             </table>
			<!-- *************************  All versions End *************************  -->       	        
			</c:if>
       	        
	    </div>
	    <!--  end the body tag --> 
	
	    <!--  this is the footer of the page -->
	    <c:import url="/inc/footer.jsp"/>
	        
	    </div>
	    <!-- end doc -->
	    
	      <div id="withdrawDialog" class="hidden">
	          <div class="hd">Withdraw Publication</div>
		      <div class="bd">
		          <form id="withdraw_publication" name="withdrawPublicationForm" 
		              method="post" 
		              action="<c:url value="/user/withdrawPublication.action"/>">
		                   
  				      <input type="hidden" id="institutional_item_id"
		                   name="institutionalItemId" value="${institutionalItemId}"/>		                   
		              
		              <input type="hidden" id="institutional_item_version"
		                   name="versionNumber" value="${institutionalItemVersion.versionNumber}"/>
		              
		              <div id="withdraw_error_div" class="errorMessage"></div>
			          
			           <table class="formTable">    
						    <tr>       
					            <td align="left" class="label">Reason for withdraw :*</td>
					            <td align="left" class="input"><textarea cols="42" rows="4"
							    	id="withdraw_reason" name="withdrawReason"></textarea></td>
							</tr>

							<tr>
							    <td align="left" class="label">Display metadata:</td>
					            <td align="left" class="input"><input type="checkbox" 
							          id="withdraw_metadata" name="showMetadata" value="true"/>	
								 </td>
							</tr>

							<tr>
							    <td align="left" class="label">Withdraw all versions:</td>
					            <td align="left" class="input"><input type="checkbox" 
							          id="withdraw_versions" name="withdrawAllVersions" value="true"/>	
								 </td>
							</tr>
				          
			           </table>
			          
		          </form>
		      </div>
	      </div>
	      
	      <div id="reinstateDialog" class="hidden">
	          <div class="hd">Reinstate Publication</div>
		      <div class="bd">
		          <form id="reinstate_publication" name="reinstatePublicationForm" 
		              method="post" 
		              action="<c:url value="/user/reinstatePublication.action"/>">
		                   
  				      <input type="hidden" id="institutional_item_id"
		                   name="institutionalItemId" value="${institutionalItemId}"/>		                   
		              <input type="hidden" id="institutional_item_version"
		                   name="versionNumber" value="${institutionalItemVersion.versionNumber}"/>
		                   
		              <div id="reinstate_error_div" class="errorMessage"></div>
			          
			           <table class="formTable">    
						    <tr>       
					            <td align="left" class="label">Reason for reinstate :*</td>
					            <td align="left" class="input"><textarea cols="42" rows="4"
							    	id="withdraw_reason" name="withdrawReason"></textarea></td>
							</tr>

							<tr>
							    <td align="left" class="label">Reinstate all versions:</td>
					            <td align="left" class="input"><input type="checkbox" 
							          id="reinstate_versions" name="reinstateAllVersions" value="true"/>	
								 </td>
							</tr>
				          
			           </table>
			          
		          </form>
		      </div>
	      </div>

		<!--  delete item confirm dialog -->
		<div id="deleteItemConfirmDialog" class="hidden">
		    <div class="hd">Delete?</div>
			<div class="bd">
			    <p>Do you want to delete this Institutional Publication?</p>
			</div>
		</div>
		<!--  end delete item confirm dialog -->
    </body>
</html>

    
