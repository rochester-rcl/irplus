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

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title>Welcome</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!--  css styles from yahoo -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/> 
	    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
	    <ur:styleSheet href="page-resources/css/base-ur.css"/>        
	    
	    <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/main_menu.css"/>
        <ur:styleSheet href="page-resources/css/tables.css"/>

        <!-- Dependencies --> 
	    <ur:js src="page-resources/yui/utilities/utilities.js"/>
	    <ur:js src="page-resources/yui/button/button-min.js"/>
	    <ur:js src="page-resources/yui/container/container-min.js"/>
	 	<ur:js src="page-resources/yui/menu/menu-min.js"/>

        <!-- Source File -->
        <ur:js src="page-resources/js/menu/main_menu.js"/>
        <ur:js src="pages/js/base_path.js"/>

	    <ur:js src="page-resources/js/admin/review_item.js"/> 
 	    
 	</head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">

                <div class="clear">&nbsp;</div>
            	<form name="previewForm" method="post">
            		<input type="hidden" name="reviewableItemId" value="${reviewableItem.id}"/>
            	</form>
				
				<!-- Begin - Display the Item preview -->
				
				<table class="noBorderTable" width="100%">
					<tr>
						<td colspan="2" class="noBorderTabletd">
							<label for="preview" class="noBorderTableLabel">${reviewableItem.item.name}   </label>
						</td>
					</tr>
					<tr>
						<td colspan="2" class="noBorderTabletd">
							<label for="title" class="noBorderTableGreyLabel">  </label>
						</td>
					</tr>
					<tr>
						<td width="100" class="noBorderTabletd">
						</td>
						<td align="right">
							
							<table class="greyBorderTable">
								<c:forEach items="${itemObjects}" var="object">
								<tr>
									<td width="10" class="noBorderTabletd">
										
									</td>
									<td class="bottomBorder" align="left">
										<c:if test="${object.type == 'FILE'}">
											<ir:fileTypeImg cssClass="tableImg" irFile="${object.irFile}"/> <ur:maxText numChars="40" text="${object.irFile.name}"></ur:maxText>
										</c:if>
										<c:if test="${object.type == 'URL'}">
											<img  alt="" class="tableImg" src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/>
    										<ur:maxText numChars="40" text="${object.name}"></ur:maxText>
										</c:if>
									</td>
									<td class="bottomBorder" align="left">
										${object.description}
									</td>
									<td width="10" class="noBorderTabletd">
										
									</td>
								</tr>
								</c:forEach>
							</table>
						</td>
					</tr>
					
					<tr>
						<td width="100" class="noBorderTabletd">
						</td>
						<td align="right">
                     		 <c:url value="editItemMetadata.action" var="editItemMetadata">
			     				<c:param name="genericItemId" value="${reviewableItem.item.id}"/>
			     				<c:param name="reviewableItemId" value="${reviewableItem.id}"/>
			 				 </c:url>

							<strong> <a href="${editItemMetadata}">Edit metadata</a> </strong>
							<table class="greyBorderBlueBgTable">
								<tr>
								<td width="60%" valign="top" class="greyBorderTableTd">
									<table class="noPaddingTable" width="100%" align="left">
										<tr>
										<td>	<label for="description" class="previewLabel"> Description </label> </td>
										</tr> 
									
										<tr>
										<td>	${reviewableItem.item.description} </td>
										</tr>

										<tr>
										<td>	<label for="abstract" class="previewLabel"> Abstract </label> </td>
										</tr> 
									
										<tr>
										<td>	${reviewableItem.item.itemAbstract} </td>
										</tr>
									</table>
								</td>
								
								<td valign="top" class="greyBorderTableTd">
									<table width="100%">
										<tr>
										<td class="previewLabel">Contributor:  </td>
										</tr>
										
										<c:forEach items="${reviewableItem.item.contributors}" var="itemContributor">
										<tr>
											<td>
											    <ir:authorName personName="${itemContributor.contributor.personName}" displayDates="true"/>
											</td>
										</tr>
										</c:forEach>

										<tr>
										<td class="previewLabel">Submitter:  </td>
										</tr>
										
										<tr>
											<td>
												${reviewableItem.item.owner.firstName} ${reviewableItem.item.owner.lastName}
											</td>
										</tr>

										<tr>
											<td class="previewLabel"> 
												Primary Item Type:
											</td>
										</tr>
										<tr>
											<td > 
												${reviewableItem.item.primaryContentType.name}
											</td>											
										</tr>

										<tr>
											<td class="previewLabel">
												<c:if  test="${reviewableItem.item.secondaryContentTypes != null}">
													Secondary Item Type(s): 
												</c:if>
 
											</td>
										</tr>

										<c:forEach items="${reviewableItem.item.secondaryContentTypes}" var="contentType">
										<tr>
											<td>
												${contentType.name} 
											</td>
										</tr>
										</c:forEach>

									
										<tr>
										<td class="previewLabel"> Series/Report Number:</td>
										</tr>
										
										<c:forEach items="${reviewableItem.item.itemReports}" var="itemReport">
										<tr>
											<td>
												${itemReport.series.name} ${itemReport.reportNumber}
											</td>
										</tr>
										</c:forEach>										

										<tr>
										<td class="previewLabel"> Identifiers: </td>
										</tr>
										
										<c:forEach items="${reviewableItem.item.itemIdentifiers}" var="itemIdentifier">
										<tr>
											<td>
												${itemIdentifier.identifierType.name} ${itemIdentifier.value}
											</td>
										</tr>
										</c:forEach>										

										<tr>
										<td class="previewLabel"> Language:</td>
										</tr>
										
										<tr>
											<td>
												${reviewableItem.item.languageType.name}
											</td>
										</tr>

										<tr>
										<td class="previewLabel"> Subject Keywords: </td>
										</tr>
										
										<tr>
											<td >
												${reviewableItem.item.itemKeywords}
											</td>
										</tr>
										
										<tr>
										<td class="previewLabel"> Sponsor:</td>
										</tr>

										<c:forEach items="${reviewableItem.item.itemSponsors}" var="itemSponsor">
											<tr>
												<td>
													${itemSponsor.sponsor.name}
												</td>
											</tr>
										</c:forEach>

										<tr>
										<td class="previewLabel"> Date this publication was first presented to the public:</td>
										</tr>
										
										<tr>
										<td>
											<c:if test="${reviewableItem.item.firstAvailableDate.month != 0}">
												Month: ${reviewableItem.item.firstAvailableDate.month} &nbsp;&nbsp;
											</c:if>

											<c:if test="${reviewableItem.item.firstAvailableDate.day != 0}">
												Day: ${reviewableItem.item.firstAvailableDate.day} &nbsp;&nbsp;
											</c:if>

											<c:if test="${reviewableItem.item.firstAvailableDate.year != 0}">
												Year: ${reviewableItem.item.firstAvailableDate.year} &nbsp;&nbsp;
											</c:if>
										</td>
										</tr>
										
										<tr>
										<td class="previewLabel"> Date this publication can be made available to the public:</td>
										</tr>

										<tr>
										<td>
											<c:if test="${reviewableItem.item.releaseDate.month != 0}">
												Month: ${reviewableItem.item.releaseDate.month} &nbsp;&nbsp;
											</c:if>

											<c:if test="${reviewableItem.item.releaseDate.day != 0}">
												Day: ${reviewableItem.item.releaseDate.day} &nbsp;&nbsp;
											</c:if>

											<c:if test="${reviewableItem.item.releaseDate.year != 0}">
												Year: ${reviewableItem.item.releaseDate.year} &nbsp;&nbsp;
											</c:if>
										</td>
										</tr>
										
										<tr>
										<td class="previewLabel"> Date this publication can be made available to the public:</td>
										</tr>

										<tr>
										<td>
											<c:if test="${reviewableItem.item.originalItemCreationDate.month != 0}">
												Month: ${reviewableItem.item.originalItemCreationDate.month} &nbsp;&nbsp;
											</c:if>

											<c:if test="${reviewableItem.item.originalItemCreationDate.day != 0}">
												Day: ${reviewableItem.item.originalItemCreationDate.day} &nbsp;&nbsp;
											</c:if>

											<c:if test="${reviewableItem.item.originalItemCreationDate.year != 0}">
												Year: ${reviewableItem.item.originalItemCreationDate.year} &nbsp;&nbsp;
											</c:if>
										</td>
										</tr>										

										<tr>
										<td class="previewLabel"> Date this publication was made available to public:</td>
										</tr>

										<tr>
										<td>
												${reviewableItem.item.releaseDate} &nbsp;&nbsp;
										</td>
										</tr>
										
										<c:if test="${reviewableItem.item.externalPublishedItem != null}">
											<tr>
											<td class="previewLabel"> Previously published/distributed information:</td>
											</tr>
											
											<tr>
											<td> Published Date:
												<c:if test="${reviewableItem.item.externalPublishedItem.publishedDate.month != 0}">
													Month: ${reviewableItem.item.externalPublishedItem.publishedDate.month} &nbsp;&nbsp;
												</c:if>
	
												<c:if test="${reviewableItem.item.externalPublishedItem.publishedDate.day != 0}">
													Day: ${reviewableItem.item.externalPublishedItem.publishedDate.day} &nbsp;&nbsp;
												</c:if>
	
												<c:if test="${reviewableItem.item.externalPublishedItem.publishedDate.year != 0}">
													Year: ${reviewableItem.item.externalPublishedItem.publishedDate.year} &nbsp;&nbsp;
												</c:if>
											</td>
											</tr>

											<tr>
											<td> Publisher: ${reviewableItem.item.externalPublishedItem.publisher.name}
											</td>
											</tr>											

											<tr>
											<td> Citation: ${reviewableItem.item.externalPublishedItem.citation}
											</td>
											</tr>											

										</c:if>
   										
									</table>
								</td>
								
								</tr>
							</table>
						</td>
					</tr>
					
					
				</table>
				<!-- End - Display the Item preview -->
       	        
       	        <div class="clear">&nbsp;</div>
       	        
       	       
			   
			
			<div class="clear">&nbsp;</div>
				
			<table width="100%">
              <tr>
                  <td align="left" width="200">
                       <button class="ur_button"  
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';"
                           onclick="javascript:YAHOO.ur.review.item.viewReviewPendingItems();">Back to Pending items</button>
                  </td>
                  <td align="right">
                      <button class="ur_button" 
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';"
                           onclick="javascript:YAHOO.ur.review.item.acceptItem()";>Accept</button>
                  </td>                      
                  
                  <td align="right" width="200">
                      <button class="ur_button" id="showRejection" 
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';">Reject</button>
                  </td>
              </tr>
            </table>
			                
            	
	    </div>
	    <!--  end the body tag --> 
	
	    <!--  this is the footer of the page -->
	    <c:import url="/inc/footer.jsp"/>
	        
	    </div>
	    <!-- end doc -->
	    
        <div id="rejectionDialog" class="hidden">
            <div class="hd">Reject the item</div>
            <div class="bd">
                  <ur:basicForm name="rejectionForm" 
                  method="post" action="admin/rejectReviewableItem.action">
              
                   <input type="hidden" name="reviewableItemId" value="${reviewableItem.id}"/>

	              
	              <table class="formTable">
	                  <tr>
	                      <td class="label"> Reason for rejection:*</td>
	                      <td align="left" class="input"> <textarea id="rejectionForm_reason" cols="42" rows="4" name="reason"></textarea></td>
	                  </tr>
	                 
	              </table>
                 </ur:basicForm>
           </div>
           <!-- end dialog body -->
       </div>
       <!--  end the new link dialog -->	    
    </body>
</html>

    
