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
        <title>Viewing personal publication: ${item.name} </title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        
         <!-- Core + Skin CSS -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
        <ur:styleSheet href="page-resources/css/base-ur.css"/>
        <ur:styleSheet href="page-resources/yui/menu/assets/skins/sam/menu.css"/>

        <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/main_menu.css"/>
        <ur:styleSheet href="page-resources/css/tables.css"/>
   
        <!-- Dependencies --> 
        <ur:js src="page-resources/yui/yahoo-dom-event/yahoo-dom-event.js"/>
        <ur:js src="page-resources/yui/connection/connection-min.js"/>
        <ur:js src="page-resources/yui/container/container_core-min.js"/>
        <ur:js src="page-resources/yui/menu/menu-min.js"/>
   
        <!-- Source File -->
        <ur:js src="page-resources/js/menu/main_menu.js"/>
        <ur:js src="pages/js/base_path.js"/>

 	    
 	</head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">
				
				<!-- Begin - Display the Item preview -->
				
				<div >
				 <c:url value="viewResearcherPage.action" var="viewResearcherPage">
				     <c:param name="researcherId" value="${researcher.id}"/>
				 </c:url>
				 <h3>Researcher: <a href="${viewResearcherPage}">${researcher.user.firstName}&nbsp;${researcher.user.lastName}</a> <br/><br/>
				    <ir:transformUrl systemCode="PRIMARY_THUMBNAIL" download="true" irFile="${researcher.primaryPicture}" var="url"/>
    			    <c:if test="${url != null }">
						     <img align="middle" src="${url}" />
			        </c:if>	
			        <c:if test="${url == null }">
	                     <img src="${pageContext.request.contextPath}/page-resources/images/all-images/noimage.jpg" height="100" width="100"/>
			        </c:if>	
			    <br/><br/>
				Publication: ${item.name} 
				</h3>
				</div>
				<table class="noBorderTable" width="100%">
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
										     <c:url var="itemFileDownload" value="/fileDownloadForInstitutionalItem.action">
										         <c:param value="${item.id}" name="itemId"/>
										         <c:param value="${object.id}" name="itemFileId"/>
										     </c:url>
											<ir:fileTypeImg cssClass="tableImg" irFile="${object.irFile}"/> <a href="${itemFileDownload}">${object.irFile.name}</a>
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
												<ir:authorName personName="${itemContributor.contributor.personName}" displayDates="true"/> - ${itemContributor.contributor.contributorType.name}
											</td>
										</tr>
										</c:forEach>

										<tr>
										<td class="previewLabel">Submitter:  </td>
										</tr>
										
										<tr>
											<td>
												${item.owner.firstName} ${item.owner.lastName}
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
       	        
	    </div>
	    <!--  end the body tag --> 
	
	    <!--  this is the footer of the page -->
	    <c:import url="/inc/footer.jsp"/>
	        
	    </div>
	    <!-- end doc -->
    </body>
</html>

    
