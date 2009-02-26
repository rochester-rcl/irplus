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

 	    <ur:js src="page-resources/js/user/preview_publication.js"/> 
 	    
 	</head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">

                <div class="clear">&nbsp;</div>
				<p><strong> Preview Publication :  </strong> <span class="noBorderTableGreyLabel">${personalItem.fullPath}${item.name} </span> </p>

				<table width="735"  align="center"  height="48" >
                  	<tr>                                           
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                          <a href="javascript:YAHOO.ur.item.preview.viewAddFiles();"> Add Files</a>
                      </td>
                      
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                           <a href="javascript:YAHOO.ur.item.preview.viewAddMatadata();">Add Information</a>
                      </td>
                      
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                          <a href="javascript:YAHOO.ur.item.preview.viewContributors();"> Add Contributors</a>
                      </td>
                      
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/bluearrow.jpg">
                          Preview Publication
                      </td> 
                  </tr>
                </table>
				
				<table width="100%">
                  <tr>
                                           
                      <td align="left" width="100">
                          <button class="ur_button" id="saveItemMetadata" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.preview.finishLater();">Finish Later</button>
                      </td>
                      <td align="right">
                          <button class="ur_button" id="goto_previous" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.preview.viewContributors()";>Previous</button>
                      </td> 
                      
                      <c:if test="${institutionalItemId == null}">                           
	                      <td align="right" width="200">
	                          <button class="ur_button" id="goto_next" 
	                               onmouseover="this.className='ur_buttonover';"
	                               onmouseout="this.className='ur_button';"
	                               onclick="javascript:YAHOO.ur.item.preview.submitItem();">Submit to Collection</button>
	                      </td>
	                  </c:if>
                  </tr>
                </table>

            	<br/>
            	
            	<form name="previewForm" method="post">
            		<input type="hidden" name="genericItemId" value="${item.id}"/>
            		<input type="hidden" name="parentCollectionId" value="${parentCollectionId}"/>
            		<input type="hidden" name="institutionalItemId" value="${institutionalItemId}"/>
            	</form>
				
				<!-- Begin - Display the Item preview -->
				
				<table class="noBorderTable" width="100%">
					<tr>
						<td width="100" class="noBorderTabletd">
						</td>
						<td align="right">
							
							<a href="javascript:YAHOO.ur.item.preview.viewAddFiles();"> Edit Files</a>
							
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
							<a href="javascript:YAHOO.ur.item.preview.viewAddMatadata();">Edit information</a>
							<table class="greyBorderBlueBgTable">
								<tr>
								<td width="60%" valign="top" class="greyBorderTableTd">
									<table class="noPaddingTable" width="100%" align="left">
										<tr>
										<td>	<label for="description" class="previewLabel"> Description </label> </td>
										</tr> 
									
										<tr>
										<td>	${item.description} </td>
										</tr>

										<tr>
										<td>	<label for="abstract" class="previewLabel"> Abstract </label> </td>
										</tr> 
									
										<tr>
										<td>	${item.itemAbstract} </td>
										</tr>
									</table>
								</td>
								
								<td valign="top" class="greyBorderTableTd">
									<table width="100%">
										<tr>
										<td class="previewLabel">Contributor:  </td>
										</tr>
										
										<c:forEach items="${item.contributors}" var="itemContributor">
										<tr>
											<td>
												${itemContributor.contributor.personName.forename}&nbsp;${itemContributor.contributor.personName.surname} - ${itemContributor.contributor.contributorType.name}
											</td>
										</tr>
										</c:forEach>

										<tr>
										<td class="previewLabel">Submitter:  </td>
										</tr>
										
										<tr>
											<td>
												${item.owner.firstName}&nbsp;${item.owner.lastName}
											</td>
										</tr>

										<tr>
											<td class="previewLabel"> 
												Primary Item Type:
											</td>
										</tr>
										<tr>
											<td > 
												${item.primaryContentType.name}
											</td>											
										</tr>

										<tr>
											<td class="previewLabel">
												<c:if  test="${item.secondaryContentTypes != null}">
													Secondary Item Type(s): 
												</c:if>
 
											</td>
										</tr>

										<c:forEach items="${item.secondaryContentTypes}" var="contentType">
										<tr>
											<td>
												${contentType.name} 
											</td>
										</tr>
										</c:forEach>
																			
										<tr>
										<td class="previewLabel"> Series/Report Number:</td>
										</tr>
										
										<c:forEach items="${item.itemReports}" var="itemReport">
										<tr>
											<td>
												${itemReport.series.name} ${itemReport.reportNumber}
											</td>
										</tr>
										</c:forEach>										

										<tr>
										<td class="previewLabel"> Identifiers: </td>
										</tr>
										
										<c:forEach items="${item.itemIdentifiers}" var="itemIdentifier">
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
												${item.languageType.name}
											</td>
										</tr>

										<tr>
										<td class="previewLabel"> Subject Keywords: </td>
										</tr>
										
										<tr>
											<td >
												${item.itemKeywords}
											</td>
										</tr>
										
										<tr>
										<td class="previewLabel"> Sponsor:</td>
										</tr>
										
										<c:forEach items="${item.itemSponsors}" var="itemSponsor">
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
											<c:if test="${item.firstAvailableDate.month != 0}">
												Month: ${item.firstAvailableDate.month} &nbsp;&nbsp;
											</c:if>

											<c:if test="${item.firstAvailableDate.day != 0}">
												Day: ${item.firstAvailableDate.day} &nbsp;&nbsp;
											</c:if>

											<c:if test="${item.firstAvailableDate.year != 0}">
												Year: ${item.firstAvailableDate.year} &nbsp;&nbsp;
											</c:if>
										</td>
										</tr>
										
										<tr>
										<td class="previewLabel"> Date this publication was originally created:</td>
										</tr>

										<tr>
										<td>
											<c:if test="${item.originalItemCreationDate.month != 0}">
												Month: ${item.originalItemCreationDate.month} &nbsp;&nbsp;
											</c:if>

											<c:if test="${item.originalItemCreationDate.day != 0}">
												Day: ${item.originalItemCreationDate.day} &nbsp;&nbsp;
											</c:if>

											<c:if test="${item.originalItemCreationDate.year != 0}">
												Year: ${item.originalItemCreationDate.year} &nbsp;&nbsp;
											</c:if>
										</td>
										</tr>
										
										<tr>
										<td class="previewLabel"> Date this publication was made available to public:</td>
										</tr>

										<tr>
										<td>
												${item.releaseDate} &nbsp;&nbsp;
										</td>
										</tr>
										
										<c:if test="${item.externalPublishedItem != null}">
											<tr>
											<td class="previewLabel"> Previously published/distributed information:</td>
											</tr>
											
											<tr>
											<td> Published Date:
												<c:if test="${item.externalPublishedItem.publishedDate.month != 0}">
													Month: ${item.externalPublishedItem.publishedDate.month} &nbsp;&nbsp;
												</c:if>
	
												<c:if test="${item.externalPublishedItem.publishedDate.day != 0}">
													Day: ${item.externalPublishedItem.publishedDate.day} &nbsp;&nbsp;
												</c:if>
	
												<c:if test="${item.externalPublishedItem.publishedDate.year != 0}">
													Year: ${item.externalPublishedItem.publishedDate.year} &nbsp;&nbsp;
												</c:if>
											</td>
											</tr>

											<tr>
											<td> Publisher: ${item.externalPublishedItem.publisher.name}
											</td>
											</tr>											

											<tr>
											<td> Citation: ${item.externalPublishedItem.citation}
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
                  <td align="left" width="100">
                       <button class="ur_button" id="saveItemMetadata" 
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';"
                           onclick="javascript:YAHOO.ur.item.preview.finishLater();">Finish Later</button>
                  </td>
                  <td align="right">
                      <button class="ur_button" id="gotoNext" 
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';"
                           onclick="javascript:YAHOO.ur.item.preview.viewContributors()";>Previous</button>
                  </td>                      
                 
                 <c:if test="${institutionalItemId == null}">      
	                  <td align="right" width="200">
	                      <button class="ur_button" id="goto_next" 
	                           onmouseover="this.className='ur_buttonover';"
	                           onmouseout="this.className='ur_button';"
	                           onclick="javascript:YAHOO.ur.item.preview.submitItem();">Submit to Collection</button>
	                  </td>
				  </c:if>
              </tr>
            </table>
			                
            	
	    </div>
	    <!--  end the body tag --> 
	
	    <!--  this is the footer of the page -->
	    <c:import url="/inc/footer.jsp"/>
	        
	    </div>
	    <!-- end doc -->
    </body>
</html>

    
