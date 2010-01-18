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

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title><ir:authorName personName="${personName}" displayDates="true"/></title>
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
				<table width ="100%">
					<tr>
						<c:if test="${researcher != null && researcher.public}">
							<td width="12%">
								<c:url value="/viewResearcherPage.action" var="viewResearcherPageUrl">
								    <c:param name="researcherId" value="${researcher.id}"/>
								</c:url>
								<c:if test="${ir:hasThumbnail(researcher.primaryPicture)}">
								   <c:url var="url" value="/researcherThumbnailDownloader.action">
                                        <c:param name="irFileId" value="${researcher.primaryPicture.id}"/>
                                        <c:param name="researcherId" value="${researcher.id}"/>
                                   </c:url>
                                   <img align="middle" height="66px" width="100px" src="${url}"/>
                               </c:if>	    
								
						        <c:if test="${researcher.primaryPicture == null }">
				                     <img src="${pageContext.request.contextPath}/page-resources/images/all-images/noimage.jpg" height="100" width="100"/>
						        </c:if>
						     </td>
						     <td width="22%">
						     	<h3><ir:authorName personName="${personName}" displayDates="true"/></h3>
						     	
						     	<table 	class="blueBox">
						     		<tr> <td> Total Publications : ${publicationsCount}</td></tr>
						     	</table>
						     	
						     	<table 	class="blueBox">
						     		<tr> <td> Total Downloads : ${totalDownloads}</td></tr>
						     	</table>
						     	
						     	<a href="${viewResearcherPageUrl}"><h3>View  Researcher Page</h3></a>
						     	
							 </td>    
						</c:if>
						<c:if test="${researcher == null || !researcher.public}">
							<td width="12%">
			                     <img src="${pageContext.request.contextPath}/page-resources/images/all-images/noimage.jpg" height="100" width="100"/>
						     </td>						
							<td width="22%">
								<h3><ir:authorName personName="${personName}" displayDates="true"/></h3>
						     	
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
								<tr> <td> Latest Publication: </td> </tr>
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
            	
              <table class="simpleTable" width="100%">
                  <thead>
                      <tr>    
	                      <th>
	                          Title
	                      </th>
	                      <th>
	                          Version number
	                      </th>
	                      <th>
	                          Published under
	                      </th>
	                      <th>
	                          Contribution type
	                      </th>
	                      <th>
	                          Total Downloads
	                      </th>

                      </tr>
                  </thead>
                  <tbody>
                      <c:forEach var="contributorPublication" varStatus="status" items="${contributorPublications}">
                      <c:if test="${ (status.count % 2) == 0}">
                          <c:set value="even" var="rowType"/>
                      </c:if>
                      <c:if test="${ (status.count % 2) == 1}">
                          <c:set value="odd" var="rowType"/>
                      </c:if>
                      <tr>
                          
                          <td class="${rowType}">
	                      	 	<c:url var="itemViewUrl" value="/institutionalPublicationPublicView.action">
	                                 <c:param name="institutionalItemVersionId" value="${contributorPublication.institutionalItemVersion.id}"/>
	                            </c:url>
		                        <a href="${itemViewUrl}">${contributorPublication.institutionalItemVersion.item.name}</a>  <br>
                          </td>
						  <td class="${rowType}">
							${contributorPublication.institutionalItemVersion.versionNumber}
                          </td>
  
						  <td class="${rowType}">
						    <ir:authorName personName="${contributorPublication.contributor.personName}" displayDates="false"/>
                          </td>
                          
                          <td class="${rowType}">
							${contributorPublication.contributor.contributorType.name}
                          </td>
                          
                          <td class="${rowType}">
							${contributorPublication.downloadsCount}
                          </td>
                     </tr>
                     </c:forEach>  
                 </tbody>  
             </table>				
       	        
		    </div>
		    <!--  end the body tag --> 
		
		    <!--  this is the footer of the page -->
		    <c:import url="/inc/footer.jsp"/>
	        
	    </div>
	    <!-- end doc -->
    </body>
</html>

    
