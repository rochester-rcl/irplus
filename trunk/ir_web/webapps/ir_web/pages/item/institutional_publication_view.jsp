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

 	    
 	</head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">
				<h3><a href="home.action">irplus</a> >
                                <c:forEach var="collection" items="${path}">
                                    <c:url var="pathCollectionUrl" value="/viewInstitutionalCollection.action">
                                         <c:param name="collectionId" value="${collection.id}"/>
                                    </c:url>
                                    <a href="${pathCollectionUrl}">${collection.name}</a> >
                                </c:forEach>
                </h3>
				<!-- Begin - Display the Item preview -->
				
				<h3>${institutionalItemVersion.item.name}</h3>
				
				<c:if test="${showPublication}">
									
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
					
					<table class="noBorderTable" width="100%">
						<tr>
							<td width="100" class="noBorderTabletd">
							</td>
							<td align="right">
								
								<c:if test="${!institutionalItemVersion.withdrawn  
								              || ir:userHasRole('ROLE_ADMIN', '') || institutionalItem.owner == user}">
								<table class="greyBorderTable">
									<c:forEach items="${itemObjects}" var="object">
									<tr>
										<td width="10" class="noBorderTabletd">
											
										</td>
										<td class="bottomBorder" align="left">
										   
										
											    <c:if test="${object.type == 'FILE'}">
											    	<ir:fileTypeImg cssClass="tableImg" irFile="${object.irFile}"/>
											    	
											    	<c:if test="${object.public || institutionalItem.owner == user || ir:hasPermission('ITEM_FILE_READ',object) }">
												        <c:url var="itemFileDownload" value="/fileDownloadForInstitutionalItem.action">
												            <c:param value="${institutionalItemVersion.item.id}" name="itemId"/>
												             <c:param value="${object.id}" name="itemFileId"/>
												         </c:url>
													     <a href="${itemFileDownload}">
	
													</c:if>
													
												    ${object.irFile.nameWithExtension} </a> (No. of downloads : ${ir:fileDownloadCount(object.irFile)})
											    </c:if>
											    
											    <c:if test="${object.type == 'URL'}">
											        <img  alt="" class="tableImg" src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/>
	    										    <ur:maxText numChars="40" text="${object.name}"></ur:maxText>
											     </c:if>
											
										</td>
										<td class="bottomBorder" align="left">
											${object.description}
										</td>
									</tr>
									</c:forEach>
								</table>
								</c:if>
							</td>
						</tr>
						<c:if test="${!institutionalItemVersion.withdrawn || institutionalItemVersion.withdrawnToken.showMetadata  
								              || ir:userHasRole('ROLE_ADMIN', '') || institutionalItem.owner == user}">
						<tr>
							<td width="100" class="noBorderTabletd">
							</td>
							<td align="right">
	
								<table class="greyBorderBlueBgTable">
									<tr>
									<td width="60%" valign="top" class="greyBorderTableTd">
										<table class="noPaddingTable" width="100%" align="left">
										    <tr>
											    <td><label for="description" class="previewLabel"> Other Titles </label> </td>
											</tr> 
											<tr>
											    <td>
											        <c:forEach items="${institutionalItemVersion.item.subTitles}" var="otherTitle">
											            ${otherTitle.title}<br/>
											        </c:forEach>
											    </td>
											</tr>
											<tr>
											    <td><label for="description" class="previewLabel"> Description </label> </td>
											</tr> 
										
											<tr>
											    <td>${institutionalItemVersion.item.description}</td>
											</tr>
	
											<tr>
											    <td><label for="abstract" class="previewLabel"> Abstract </label></td>
											</tr> 
										
											<tr>
											    <td>${institutionalItemVersion.item.itemAbstract}</td>
											</tr>
										</table>
									</td>
									
									<td valign="top" class="greyBorderTableTd">
										<table width="100%">
											<tr>
											<td class="previewLabel">Contributor:  </td>
											</tr>
											
											<c:forEach items="${institutionalItemVersion.item.contributors}" var="itemContributor">
											<tr>
												<td>
												        <c:url var="contributorUrl" value="/viewContributorPage.action">
														    <c:param name="contributorId" value="${itemContributor.contributor.id}"/>
														</c:url>						                             
						                                 <a href="${contributorUrl}"> <ir:authorName personName="${itemContributor.contributor.personName}" displayDates="true"/></a> - ${itemContributor.contributor.contributorType.name}
												</td>
											</tr>
											</c:forEach>
	
											<tr>
											<td class="previewLabel">Submitter:  </td>
											</tr>
											
											<tr>
												<td>
													${institutionalItemVersion.item.owner.firstName} &nbsp; ${institutionalItemVersion.item.owner.lastName}
												</td>
											</tr>
	
											<tr>
												<td class="previewLabel"> 
													Primary Item Type:
												</td>
											</tr>
											<tr>
												<td > 
													${institutionalItemVersion.item.primaryContentType.name}
												</td>											
											</tr>
	
											<tr>
												<td class="previewLabel">
													<c:if  test="${institutionalItemVersion.item.secondaryContentTypes != null}">
														Secondary Item Type(s): 
													</c:if>
	 
												</td>
											</tr>
	
											<c:forEach items="${institutionalItemVersion.item.secondaryContentTypes}" var="contentType">
											<tr>
												<td>
													${contentType.name} 
												</td>
											</tr>
											</c:forEach>

																	
											<tr>
											<td class="previewLabel"> Series/Report Number:</td>
											</tr>
											
											<c:forEach items="${institutionalItemVersion.item.itemReports}" var="itemReport">
											<tr>
												<td>
													${itemReport.series.name} &nbsp; ${itemReport.reportNumber}
												</td>
											</tr>
											</c:forEach>										
	
											<tr>
											<td class="previewLabel"> Identifiers: </td>
											</tr>
											
											<c:forEach items="${institutionalItemVersion.item.itemIdentifiers}" var="itemIdentifier">
											<tr>
												<td>
													${itemIdentifier.identifierType.name}: &nbsp; ${itemIdentifier.value}
												</td>
											</tr>
											</c:forEach>										
	
											<tr>
											<td class="previewLabel"> Language:</td>
											</tr>
											
											<tr>
												<td>
													${institutionalItemVersion.item.languageType.name}
												</td>
											</tr>
	
											<tr>
											<td class="previewLabel"> Subject Keywords: </td>
											</tr>
											
											<tr>
												<td >
													${institutionalItemVersion.item.itemKeywords}
												</td>
											</tr>
											
											<tr>
											<td class="previewLabel"> Sponsor:</td>
											</tr>
	
											<c:forEach items="${institutionalItemVersion.item.itemSponsors}" var="itemSponsor">
												<tr>
													<td>
														${itemSponsor.sponsor.name} - ${itemSponsor.description}
													</td>						
												</tr>
											</c:forEach>										
	
											<tr>
											<td class="previewLabel"> Date this publication was first presented to the public:</td>
											</tr>
											
											<tr>
											<td>
												<c:if test="${institutionalItemVersion.item.firstAvailableDate.month != 0}">
													Month: ${institutionalItemVersion.item.firstAvailableDate.month} &nbsp;&nbsp;
												</c:if>
	
												<c:if test="${institutionalItemVersion.item.firstAvailableDate.day != 0}">
													Day: ${institutionalItemVersion.item.firstAvailableDate.day} &nbsp;&nbsp;
												</c:if>
	
												<c:if test="${institutionalItemVersion.item.firstAvailableDate.year != 0}">
													Year: ${institutionalItemVersion.item.firstAvailableDate.year} &nbsp;&nbsp;
												</c:if>
											</td>
											</tr>
											
											<tr>
											<td class="previewLabel"> Date this publication was originally created:</td>
											</tr>
	
											<tr>
											<td>
												<c:if test="${institutionalItemVersion.item.originalItemCreationDate.month != 0}">
													Month: ${institutionalItemVersion.item.originalItemCreationDate.month} &nbsp;&nbsp;
												</c:if>
	
												<c:if test="${institutionalItemVersion.item.originalItemCreationDate.day != 0}">
													Day: ${institutionalItemVersion.item.originalItemCreationDate.day} &nbsp;&nbsp;
												</c:if>
	
												<c:if test="${institutionalItemVersion.item.originalItemCreationDate.year != 0}">
													Year: ${institutionalItemVersion.item.originalItemCreationDate.year} &nbsp;&nbsp;
												</c:if>
											</td>
											</tr>

											<tr>
											<td class="previewLabel"> Date this publication was made available to public:</td>
											</tr>
	
											<tr>
											<td>
													${institutionalItemVersion.item.releaseDate} &nbsp;&nbsp;
											</td>
											</tr>
	
											<c:if test="${institutionalItemVersion.item.externalPublishedItem != null}">
												<tr>
												<td class="previewLabel"> Previously published/distributed information:</td>
												</tr>
												
												<tr>
												<td> Published Date:
													<c:if test="${institutionalItemVersion.item.externalPublishedItem.publishedDate.month != 0}">
														Month: ${institutionalItemVersion.item.externalPublishedItem.publishedDate.month} &nbsp;&nbsp;
													</c:if>
		
													<c:if test="${institutionalItemVersion.item.externalPublishedItem.publishedDate.day != 0}">
														Day: ${institutionalItemVersion.item.externalPublishedItem.publishedDate.day} &nbsp;&nbsp;
													</c:if>
		
													<c:if test="${institutionalItemVersion.item.externalPublishedItem.publishedDate.year != 0}">
														Year: ${institutionalItemVersion.item.externalPublishedItem.publishedDate.year} &nbsp;&nbsp;
													</c:if>
												</td>
												</tr>
	
												<tr>
												<td> Publisher: ${institutionalItemVersion.item.externalPublishedItem.publisher.name}
												</td>
												</tr>											
	
												<tr>
												<td> Citation: ${institutionalItemVersion.item.externalPublishedItem.citation}
												</td>
												</tr>											
	
											</c:if>
										</table>
									</td>
									
									</tr>
								</table>
							</td>
						</tr>
					</c:if>
						
					</table>
					<!-- End - Display the Item preview -->
				
	
				<table class="formTable" width="100%"> 
					<tr> 
					<c:if test="${user != null && (institutionalItem.owner == user) || ir:userHasRole('ROLE_ADMIN', '')}">
						<td>
						<c:if test="${institutionalItemVersion.versionNumber == institutionalItem.versionedInstitutionalItem.largestVersion}">
							  
					           <ur:basicForm name="editForm" 
					              method="post" action="/user/viewEditItem.action">
									
									<input type="hidden" name="institutionalItemId" value="${institutionalItem.id}"/>
									<input type="hidden" name="genericItemId" value="${institutionalItemVersion.item.id}"/>
				
									<button class="ur_button" type="submit"
						                       onmouseover="this.className='ur_buttonover';"
					 		                   onmouseout="this.className='ur_button';"
						                       id="edit_publication">Edit Publication</button>    
					            </ur:basicForm>  			                        	        
							
						</c:if>
						</td>
				    </c:if>
				    
				    <c:if test="${user != null && (institutionalItem.owner == user) || ir:userHasRole('ROLE_ADMIN', '')}">
						<td>  
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
		 				</td> 
		 	        </c:if>
		 	        <c:if test="${user != null && (ir:userHasRole('ROLE_ADMIN', '')) }">
		 				<td>
				           <ur:basicForm name="movePublicationForm" 
				              method="post" action="/admin/viewMoveInstitutionalItemLocations.action">
								
								<input type="hidden" id="move_items_destination_id" name="destinationId" value="${institutionalItem.institutionalCollection.id}"/>
								<input type="hidden" id="move_items_item_ids" name="itemIds" value="${institutionalItemId}"/>
					 				
		 						<button class="ur_button" type="submit" 
				                       onmouseover="this.className='ur_buttonover';"
			 		                   onmouseout="this.className='ur_button';"
				                       id="move_publication">Move Publication</button>
				            </ur:basicForm>  
		 				</td>
		 		   </c:if>
		 		   <c:if test="${user != null && (ir:userHasRole('ROLE_ADMIN', '')) }">
						<td>  
				           <ur:basicForm name="permissionForm" 
				              method="post" action="/admin/viewInstitutionalItemPermissions.action">
								
								<input type="hidden" id="permissions_item_id" name="institutionalItemId" value="${institutionalItem.id}"/>
			
								<button class="ur_button" type="submit" 
					                       onmouseover="this.className='ur_buttonover';"
				 		                   onmouseout="this.className='ur_button';"
					                       id="manage_permissions">Manage Permissions</button>    
				            </ur:basicForm>  			                        	        
						</td>
				   </c:if>
				   <c:if test="${user != null && (institutionalItem.owner == user) || ir:userHasRole('ROLE_ADMIN', '')}">
						<td>  
				           <ur:basicForm name="newVersionForm" 
				              method="post" action="/user/viewAddInstitutionalItemVersion.action">
								
								<input type="hidden" id="institutional_item_id" name="institutionalItemId" value="${institutionalItem.id}"/>
			
								<button class="ur_button" type="submit" 
					                       onmouseover="this.className='ur_buttonover';"
				 		                   onmouseout="this.className='ur_button';"
					                       id="new_version">Add New Version</button>    
				            </ur:basicForm>  			                        	        
						</td>
				 </c:if>
				 <c:if test="${user != null && (ir:userHasRole('ROLE_ADMIN', '')) }">	
						<td>  
				           <ur:basicForm name="deleteForm" method="post" action="/admin/deleteInstitutionalItem.action">
								<input type="hidden" id="institutional_item_id" name="institutionalItemId" value="${institutionalItem.id}"/>
				            </ur:basicForm>  			                        	        
			
								<button class="ur_button" 
					                       onmouseover="this.className='ur_buttonover';"
				 		                   onmouseout="this.className='ur_button';"
					                       id="delete_item">Delete</button>    
						</td>	
			      </c:if>   
				</tr> 
				 
					<c:if test="${user != null && (institutionalItem.owner == user) || ir:userHasRole('ROLE_ADMIN,ROLE_RESEARCHER', 'OR')}">
						<tr> <td colspan="5" align="center">  
				           <ur:basicForm name="addToResearcherPageForm" 
				              method="post" action="/user/viewResearcherInstitutionalItem.action">
								
								<input type="hidden" id="institutional_item_id" name="institutionalItemId" value="${institutionalItem.id}"/>
								<input type="hidden" id="researcher_id" name="researcherId" value="${user.researcher.id}"/>
								
								<button class="ur_button" type="submit" 
					                       onmouseover="this.className='ur_buttonover';"
				 		                   onmouseout="this.className='ur_button';"
					                       id="add_researcher_page">Add to My Researcher page</button>    
				            </ur:basicForm>  			                        	        
						</td>	</tr>								
					</c:if>
				 
	            </table>           
	
	
       	       	<div class="clear">&nbsp;</div>
			</c:if>
				
			<c:if test="${!showPublication}">
				<h3> <div class="errorMessage"> ${message} </div> </h3>
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
		          <ur:basicForm id="withdraw_publication" name="withdrawPublicationForm" 
		              method="post" 
		              action="user/withdrawPublication.action">
		                   
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
			          
		          </ur:basicForm>
		      </div>
	      </div>
	      
	      <div id="reinstateDialog" class="hidden">
	          <div class="hd">Reinstate Publication</div>
		      <div class="bd">
		          <ur:basicForm id="reinstate_publication" name="reinstatePublicationForm" 
		              method="post" 
		              action="user/reinstatePublication.action">
		                   
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
			          
		          </ur:basicForm>
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

    
