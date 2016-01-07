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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title><ir:authorName personName="${personName}" displayDates="true"/></title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!--  contributor RSS feed -->
        <c:url var="contributorRss" value="viewContributorRss.action">
	        <c:param name="personNameId" value="${personName.id}"/>
		</c:url>
        <link rel="alternate" type="application/rss+xml" title="<ir:authorName personName="${personName}" displayDates="true"/> - Recent Submissions" href="${collectionRss}">
        
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

 	    <ur:js src="page-resources/js/google_analytics.js"/>
 	</head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">
				<table width ="100%">
					<tr>
						<c:if test="${researcher != null && researcher.isPublic}">
							<td width="12%">
								<c:url value="/viewResearcherPage.action" var="viewResearcherPageUrl">
								    <c:param name="researcherId" value="${researcher.id}"/>
								</c:url>
								<c:if test="${ir:hasThumbnail(researcher.primaryPicture)}">
								   <c:url var="url" value="/researcherThumbnailDownloader.action">
                                        <c:param name="irFileId" value="${researcher.primaryPicture.id}"/>
                                        <c:param name="researcherId" value="${researcher.id}"/>
                                   </c:url>
                                   <img class="centered_thumbnail" src="${url}"/>
                               </c:if>	    
								
						        <c:if test="${researcher.primaryPicture == null }">
				                     <img src="${pageContext.request.contextPath}/page-resources/images/all-images/noimage.jpg" class="noimage_size"/>
						        </c:if>
						     </td>
						     <td width="22%">
						     	<h3><a href="${contributorRss}"><ir:authorName personName="${personName}" displayDates="true"/></a>&nbsp;<img src="<c:url value='/page-resources/images/all-images/feed.jpg'/>" alt="RSS Feed"/></h3>
						     	
						     	<table 	class="blueBox">
						     		<tr> <td> Total Publications : ${publicationsCount}</td></tr>
						     	</table>
						     	
						     	<table 	class="blueBox">
						     		<tr> <td> Total Downloads : ${totalDownloads}</td></tr>
						     	</table>
						     	
						     	<a href="${viewResearcherPageUrl}"><h3>View  Researcher Page</h3></a>
						     	
							 </td>    
						</c:if>
						<c:if test="${researcher == null || !researcher.isPublic}">
							<td width="12%">
			                     <img src="${pageContext.request.contextPath}/page-resources/images/all-images/noimage.jpg" class="noimage_size"/>
						     </td>						
							<td width="22%">
								<h3><a href="${contributorRss}"><ir:authorName personName="${personName}" displayDates="true"/></a>&nbsp;<img src="<c:url value='/page-resources/images/all-images/feed.jpg'/>" alt="RSS Feed"/></h3>
						     	
						     	<table 	class="blueBox">
						     		<tr> <td> Total Publications : ${publicationsCount}</td></tr>
						     	</table>
						     	
						     	<table 	class="blueBox">
						     		<tr> <td> Total Downloads : ${totalDownloads}</td></tr>
						     	</table>
							 </td>
						</c:if>
						
						<c:url var="latestItemViewUrl" value="/institutionalPublicationPublicView.action">
	                                 <c:param name="institutionalItemVersionId" value="${latestItemVersion.id}"/>
                        </c:url>

		                <c:url var="mostDownloadedItemViewUrl" value="/institutionalPublicationPublicView.action">
	                                 <c:param name="institutionalItemVersionId" value="${mostDownloadedItemVersion.id}"/>
                        </c:url>
		                        
						<td width="33%" align="right">
							<table class="statBox">
								<tr> <td> Latest Submission: </td> </tr>
								<tr> <td> <p><strong><a href="${latestItemViewUrl}"><ur:maxText numChars="40" text="${latestItemVersion.item.name}"/></a></strong><br> ${latestPublicationContributorType}</p> </td> </tr>
								<tr> <td> Published: ${latestItemVersion.dateOfDeposit}</td> </tr>
							</table>
						</td>
						<td width="33%" align="right">
							<table class="statBox">
								<tr> <td> Most Downloaded: </td> </tr>
								<tr> <td> <p>&nbsp;<strong><a href="${mostDownloadedItemViewUrl}"><ur:maxText numChars="40" text="${mostDownloadedItemVersion.item.name}"/></a></strong><br>&nbsp;${mostDownloadedItemContributorType} </p> </td> </tr>
								<tr> <td> Downloads: ${mostDownloadedCount} </td> </tr>
							</table>
						</td>

					</tr>
            	</table>
            	
               <c:url var="sortAscendingTitleUrl" value="/viewContributorPage.action">
                   <c:param name="rowStart" value="${rowStart}"/>
				   <c:param name="startPageNumber" value="${startPageNumber}"/>
				   <c:param name="currentPageNumber" value="${currentPageNumber}"/>	
				   <c:param name="sortElement" value="title"/>		
				   <c:param name="sortType" value="asc"/>
				   <c:param name="personNameId" value="${personNameId}"/>
			   </c:url>
					                     
			   <c:url var="sortDescendingTitleUrl" value="/viewContributorPage.action">
			       <c:param name="rowStart" value="${rowStart}"/>
				   <c:param name="startPageNumber" value="${startPageNumber}"/>
				   <c:param name="currentPageNumber" value="${currentPageNumber}"/>	
				   <c:param name="sortElement" value="title"/>		
				   <c:param name="sortType" value="desc"/>
				   <c:param name="personNameId" value="${personNameId}"/>
			   </c:url>
			   
			    <c:url var="sortAscendingDownloadUrl" value="/viewContributorPage.action">
                   <c:param name="rowStart" value="${rowStart}"/>
				   <c:param name="startPageNumber" value="${startPageNumber}"/>
				   <c:param name="currentPageNumber" value="${currentPageNumber}"/>	
				   <c:param name="sortElement" value="download"/>		
				   <c:param name="sortType" value="asc"/>
				   <c:param name="personNameId" value="${personNameId}"/>
			   </c:url>
					                     
			   <c:url var="sortDescendingDownloadUrl" value="/viewContributorPage.action">
			       <c:param name="rowStart" value="${rowStart}"/>
				   <c:param name="startPageNumber" value="${startPageNumber}"/>
				   <c:param name="currentPageNumber" value="${currentPageNumber}"/>	
				   <c:param name="sortElement" value="download"/>		
				   <c:param name="sortType" value="desc"/>
				   <c:param name="personNameId" value="${personNameId}"/>
			   </c:url>
			   
			               	
               <c:url var="sortAscendingSubmissionUrl" value="/viewContributorPage.action">
                   <c:param name="rowStart" value="${rowStart}"/>
				   <c:param name="startPageNumber" value="${startPageNumber}"/>
				   <c:param name="currentPageNumber" value="${currentPageNumber}"/>	
				   <c:param name="sortElement" value="submissionDate"/>		
				   <c:param name="sortType" value="asc"/>
				   <c:param name="personNameId" value="${personNameId}"/>
			   </c:url>
					                     
			   <c:url var="sortDescendingSubmissionUrl" value="/viewContributorPage.action">
			       <c:param name="rowStart" value="${rowStart}"/>
				   <c:param name="startPageNumber" value="${startPageNumber}"/>
				   <c:param name="currentPageNumber" value="${currentPageNumber}"/>	
				   <c:param name="sortElement" value="submissionDate"/>		
				   <c:param name="sortType" value="desc"/>
				   <c:param name="personNameId" value="${personNameId}"/>
			   </c:url>
            	
            	<c:set var="titleSort" value="none"/>
			    <c:if test='${sortElement == "title"}'>
				    <c:set var="titleSort" value="${sortType}"/>
				</c:if>
				
				<c:set var="downloadSort" value="none"/>
				 <c:if test='${sortElement == "download"}'>
				    <c:set var="downloadSort" value="${sortType}"/>
				</c:if>
				
				<c:set var="submissionSort" value="none"/>
				 <c:if test='${sortElement == "submissionDate"}'>
				    <c:set var="submissionSort" value="${sortType}"/>
				</c:if>
            	
            	<c:if test="${totalHits > 0}">
				    <h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
				</c:if> 
            	<c:import url="contributor_page_view_pager.jsp"/>
            	
            	<div class="dataTable">
				    <urstb:table width="100%">
					    <urstb:thead>
					        <urstb:tr>
					        
					            <urstb:tdHeadSort  height="33" width="400"
					                useHref="true"
					                hrefVar="href"
                                    currentSortAction="${titleSort}"
                                    ascendingSortAction="${sortAscendingTitleUrl}"
                                    descendingSortAction="${sortDescendingTitleUrl}">
                                    <a href="${href}">Title</a>                                              
                                    <urstb:thImgSort
                                        sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
                                        sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/>
                                </urstb:tdHeadSort>
					        
					            <urstb:tdHeadSort  height="33" width="130"
					                useHref="true"
					                hrefVar="href"
                                    currentSortAction="${submissionSort}"
                                    ascendingSortAction="${sortAscendingSubmissionUrl}"
                                    descendingSortAction="${sortDescendingSubmissionUrl}">
                                    <a href="${href}">Submission Date</a>                                              
                                    <urstb:thImgSort
                                        sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
                                        sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/>
                                </urstb:tdHeadSort>
					            <urstb:td>Version</urstb:td>
					            <urstb:td>Published Under</urstb:td>
					            <urstb:td>Contribution</urstb:td>
					            <urstb:tdHeadSort  height="33" width="90"
					                useHref="true"
					                hrefVar="href"
                                    currentSortAction="${downloadSort}"
                                    ascendingSortAction="${sortAscendingDownloadUrl}"
                                    descendingSortAction="${sortDescendingDownloadUrl}">
                                    <a href="${href}">Downloads</a>                                              
                                    <urstb:thImgSort
                                        sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
                                        sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/>
                                </urstb:tdHeadSort>
					            

						    </urstb:tr>
						</urstb:thead>
						<urstb:tbody
						    var="contributorPublication" 
						    oddRowClass="odd"
						    evenRowClass="even"
						    currentRowClassVar="rowClass"
						    collection="${contributorPublications}">
						    <urstb:tr 
						        cssClass="${rowClass}"
						        onMouseOver="this.className='highlight'"
						        onMouseOut="this.className='${rowClass}'">
						                        
						        <urstb:td>
						            <c:url var="itemViewUrl" value="/institutionalPublicationPublicView.action">
	                                    <c:param name="institutionalItemVersionId" value="${contributorPublication.institutionalItemVersion.id}"/>
	                                </c:url>
		                            <a href="${itemViewUrl}">${contributorPublication.institutionalItemVersion.item.fullName}</a>
						        </urstb:td>
						        
						        <urstb:td>
						            <fmt:formatDate type="date" dateStyle="medium" timeStyle="short" value="${contributorPublication.institutionalItemVersion.dateOfDeposit}" />
						        </urstb:td>
						        
						        <urstb:td>
						            ${contributorPublication.institutionalItemVersion.versionNumber}
						        </urstb:td>
						                        
						        <urstb:td>
						            <ir:authorName personName="${contributorPublication.contributor.personName}" displayDates="false"/>
						        </urstb:td>
						        
						        <urstb:td>
						            ${contributorPublication.contributor.contributorType.name}
						        </urstb:td>
						        
						        <urstb:td>
						            ${contributorPublication.downloadsCount}
						        </urstb:td>
						    </urstb:tr>
						</urstb:tbody>
				    </urstb:table>
				</div>	
				<c:import url="contributor_page_view_pager.jsp"/>
            	       	        
		    </div>
		    <!--  end the body tag --> 
		
		    <!--  this is the footer of the page -->
		    <c:import url="/inc/footer.jsp"/>
	        
	    </div>
	    <!-- end doc -->
    </body>
</html>

    
