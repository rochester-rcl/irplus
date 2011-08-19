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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title>Browsing/Searching: Researchers</title>
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
        <ur:js src="page-resources/yui/tabview/tabview-min.js"/>

	    <ur:js src="pages/js/base_path.js"/>
	    <ur:js src="page-resources/js/util/ur_util.js"/>
	    <ur:js src="page-resources/js/menu/main_menu.js"/>
		<ur:js src="pages/js/ur_table.js"/>

         <c:url var="browseResearchers" value="/viewResearcherBrowse.action"/>
         <c:url var="searchAllResearchers" value="/startSearchResearchers.action"/>
		 <script type="text/javascript">
    
          function handleBrowseClick(e) {  
               window.location='${browseResearchers}';
          }
          
          function handleSearchClick(e)
          {
              window.location='${searchAllResearchers}';
          }
          
          function init()
          {
                var myTabs = new YAHOO.widget.TabView("researcher-tabs");
	            var tab0 = myTabs.getTab(0);
	            tab0.addListener('click', handleBrowseClick);
	            
	            var tab1 = myTabs.getTab(1);
	            tab1.addListener('click', handleSearchClick);
          }
          
          // initialize the code once the dom is ready
          YAHOO.util.Event.onDOMReady(init);
	       
	    </script>
	    
	    		 
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body regin of the page -->
            <div id="bd">
            	
            	 <h3> Browse/Search Researcher </h3>
		        
		        <!--  set up tabs for editing news -->
		        <div id="researcher-tabs" class="yui-navset">
		            <ul class="yui-nav">
		               <c:if test='${viewType == "browse"}'>
		                    <li class="selected"><a href="${browseResearchers}"><em>Browse</em></a></li>
		                    <li><a href="${searchAllResearchers}"><em>Search</em></a></li>
		               </c:if>
		               <c:if test='${viewType == "search"}'>
		                    <li><a href="${browseResearchers}"><em>Browse</em></a></li>
		                    <li class="selected"><a href="${searchAllResearchers}"><em>Search</em></a></li>
		               </c:if>
		            </ul>
		
		            <div class="yui-content">
		            
		                <!--  first tab -->
		                <div id="tab1">
				         <c:if test='${viewType == "browse"}'>
				         <c:if test="${totalHits > 0}">
				          	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>  
						 </c:if>
		                  <div class="clear">&nbsp;</div>

                          <c:import url="browse_researcher_pager.jsp"></c:import>
						  <br/>
						<div class="dataTable">
							                 
					        <urstb:table width="100%">
					            <urstb:thead>
					                <urstb:tr>
														                
					                    <urstb:td>Image</urstb:td>
										
					                
					                        <c:url var="sortAscLastNameUrl" value="/viewResearcherBrowse.action">
													<c:param name="rowStart" value="${rowStart}"/>
													<c:param name="startPageNumber" value="${startPageNumber}"/>
													<c:param name="currentPageNumber" value="${currentPageNumber}"/>	
													<c:param name="sortType" value="asc"/>
											</c:url>
											<c:url var="sortDescLastNameUrl" value="/viewResearcherBrowse.action">
													<c:param name="rowStart" value="${rowStart}"/>
													<c:param name="startPageNumber" value="${startPageNumber}"/>
													<c:param name="currentPageNumber" value="${currentPageNumber}"/>	
													<c:param name="sortType" value="desc"/>
											</c:url>
										
											
										
										
											<urstb:tdHeadSort  height="33"
					                        useHref="true"
					                        hrefVar="href"
                                            currentSortAction="${sortType}"
                                            ascendingSortAction="${sortAscLastNameUrl}"
                                            descendingSortAction="${sortDescLastNameUrl}">
                                            <a href="${href}">Name</a>                                              
                                            <urstb:thImgSort
                                                         sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
                                                         sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/></urstb:tdHeadSort>
										
										
										
						                </urstb:tr>
						            </urstb:thead>
						            <urstb:tbody
						                var="researcher" 
						                oddRowClass="odd"
						                evenRowClass="even"
						                currentRowClassVar="rowClass"
						                collection="${researchers}">
						                    <urstb:tr 
						                        cssClass="${rowClass}"
						                        onMouseOver="this.className='highlight'"
						                        onMouseOut="this.className='${rowClass}'">
						                        <urstb:td width="125px">
						                            <c:if test="${researcher.public}">
						                                <c:if test="${ir:hasThumbnail(researcher.primaryPicture)}">
						                                    <c:url var="url" value="/researcherThumbnailDownloader.action">
                                                                <c:param name="irFileId" value="${researcher.primaryPicture.id}"/>
                                                                <c:param name="researcherId" value="${researcher.id}"/>
                                                            </c:url>
                                                           <img class="basic_thumbnail" src="${url}"/>
                                                         </c:if>
			                                         </c:if>    
			                                         <c:if test="${!ir:hasThumbnail(researcher.primaryPicture) || !researcher.public}">
	                                                      <img class="basic_thumbnail" src="${pageContext.request.contextPath}/page-resources/images/all-images/noimage.jpg" class="noimage_size"/>
			                                         </c:if>	
						                        </urstb:td>
						                        <urstb:td>
						                            <c:url value="viewResearcherPage.action" var="viewResearcherPage">
						                        	    <c:param name="researcherId" value="${researcher.id}"/>
						                        	 </c:url>
													<a href="${viewResearcherPage}">${researcher.user.lastName},&nbsp;${researcher.user.firstName}</a><br>
													<c:if test="${researcher.researchInterest != '' && researcher.researchInterest != null}"><div class="smallText"><ur:maxText numChars="250" text="${researcher.researchInterest}"/></div></c:if>
						                        </urstb:td>
						                    </urstb:tr>
						            </urstb:tbody>
						        </urstb:table>
						    </div>	

				            <c:import url="browse_researcher_pager.jsp"></c:import>
				            </c:if>
			        </div>
		            <!--  end first tab -->
		                  
		                  
	              	 <!--  start second tab -->
	               	 <div id="tab2">
	               	    <br/>
	               	     <c:url var="searchAllResearchers" value="/searchResearchers.action"/>
						<form method="GET" action="${searchAllResearchers}">
						    Search: <input type="text" name="query" size="50"/>
						</form>
						<br/>

						<c:if test='${viewType == "search" && !searchInit}'>

							<div class="full_text_search_results">
							<div class="facet_container">
							    <div class="facet_search_selection">
							          <h3>Current Search </h3>
							          <p><strong>Search:</strong> ${searchDataHelper.userQuery} <br/></p>
							          <c:forEach var="facet" varStatus="status" items="${searchDataHelper.facetTrail}">
							              <c:url var="removeFacet" value="/filterSearchResearchers.action">
							                  <c:param name="rowStart" value="0"/>
						                      <c:param name="startPageNumber" value="1"/>
						                      <c:param name="currentPageNumber" value="1"/>
							                  <c:param name="facets" value="${facets}"/>
							                  <c:param name="facetValues" value="${facetValues}"/>	
							                  <c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
							                  <c:param name="query" value="${searchDataHelper.userQuery}"/>
							                  <c:param name="facetIndexToRemove" value="${status.index}"/>
							              </c:url>
							          <p><strong>${facet.displayName}:&nbsp;</strong>${facet.query}  <a href="${removeFacet}">(Remove)</a><br/></p>
							          </c:forEach>
							     </div>
							     
							     <div class="facets"> 
							          <ir:facet var="departments" key="departments" facets="${searchDataHelper.facets}">
							              <h3>Departments</h3>
							             
							              <c:forEach var="department" items="${departments}">
							                   <c:if test="${!ir:facetSelected(searchDataHelper, department)}">
							                      <c:url var="departmentFilter" value="/filterSearchResearchers.action">
							                          <c:param name="rowStart" value="0"/>
						                              <c:param name="startPageNumber" value="1"/>
						                              <c:param name="currentPageNumber" value="1"/>
							                          <c:param name="mostRecentFacetName" value="department"/>
							                          <c:param name="mostRecentFacetValue" value="${department.facetName}"/>
							                          <c:param name="mostRecentFacetDisplayName" value="Department"/>
							                          <c:param name="facets" value="${facets}"/>
							                          <c:param name="facetValues" value="${facetValues}"/>	
							                          <c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
							                          <c:param name="query" value="${searchDataHelper.userQuery}"/> 					  
							                      </c:url>
							                      <div class="hanging_indent"><a href="${departmentFilter}">${department.facetName}&nbsp;(${department.hits})</a></div>
							                   </c:if>
							              </c:forEach>
							          </ir:facet>
									  
									  <ir:facet var="fields" key="fields" facets="${searchDataHelper.facets}">
							             <h3>Fields</h3>
							             <c:forEach var="field" items="${fields}">
							               	 <c:if test="${!ir:facetSelected(searchDataHelper, field)}">
							                     <c:url var="fieldFilter" value="/filterSearchResearchers.action">
							                         <c:param name="rowStart" value="0"/>
						                             <c:param name="startPageNumber" value="1"/>
						                             <c:param name="currentPageNumber" value="1"/>
							                         <c:param name="mostRecentFacetName" value="field"/>
							                         <c:param name="mostRecentFacetValue" value="${field.facetName}"/>
							                         <c:param name="mostRecentFacetDisplayName" value="Field"/>
							                         <c:param name="facets" value="${facets}"/>
							                         <c:param name="facetValues" value="${facetValues}"/>	
							                         <c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
							                         <c:param name="query" value="${searchDataHelper.userQuery}"/> 					  
							                     </c:url>
							                    <div class="hanging_indent"><a href="${fieldFilter}">${field.facetName} (${field.hits})</a></div>
							               	 </c:if>
							             </c:forEach>
							        </ir:facet>	

									  <ir:facet var="keywords" key="keywords" facets="${searchDataHelper.facets}">
							             <h3>Keywords</h3>
							             <c:forEach var="keyword" items="${keywords}">
							               	 <c:if test="${!ir:facetSelected(searchDataHelper, keyword)}">
							                     <c:url var="keywordFilter" value="/filterSearchResearchers.action">
							                         <c:param name="rowStart" value="0"/>
						                             <c:param name="startPageNumber" value="1"/>
						                             <c:param name="currentPageNumber" value="1"/>
							                         <c:param name="mostRecentFacetName" value="key_words"/>
							                         <c:param name="mostRecentFacetValue" value="${keyword.facetName}"/>
							                         <c:param name="mostRecentFacetDisplayName" value="Keyword"/>
							                         <c:param name="facets" value="${facets}"/>
							                         <c:param name="facetValues" value="${facetValues}"/>	
							                         <c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
							                         <c:param name="query" value="${searchDataHelper.userQuery}"/> 					  
							                     </c:url>
							                     <div class="hanging_indent"><a href="${keywordFilter}">${keyword.facetName} (${keyword.hits})</a></div>
							               	 </c:if>
							             </c:forEach>
							        </ir:facet>								        						          
								</div>
							</div>
 

							<div class="search_results_table">
								<c:if test="${searchDataHelper.hitSize > 0}">
						    		<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${searchDataHelper.hitSize} for search: ${searchDataHelper.userQuery} (Relevance Ranked)</h3>  
								</c:if>
							    <div class="search_div_pager">
							        <c:import url="search_all_researchers_pager.jsp"/>
							    </div>
							    <br/>
							    <div class="dataTable">
								
						        <urstb:table  width="100%">
						            <urstb:thead>
						                    <urstb:td>Thumbnail</urstb:td>
						                    <urstb:td>Name</urstb:td>
						            </urstb:thead>
						            <urstb:tbody
						                var="researcher" 
						                oddRowClass="odd"
						                evenRowClass="even"
						                currentRowClassVar="rowClass"
						                collection="${searchResearchers}">
						                    <urstb:tr 
						                        cssClass="${rowClass}"
						                        onMouseOver="this.className='highlight'"
						                        onMouseOut="this.className='${rowClass}'">
						                        <urstb:td width="125px">
						                            <c:if test="${researcher.public}">
						                                <c:if test="${ir:hasThumbnail(researcher.primaryPicture)}">
						                                    <c:url var="url" value="/researcherThumbnailDownloader.action">
                                                                <c:param name="irFileId" value="${researcher.primaryPicture.id}"/>
                                                                 <c:param name="researcherId" value="${researcher.id}"/>
                                                            </c:url>
                                                            <img class="basic_thumbnail" src="${url}"/>
                                                         </c:if>
			                                         </c:if>    
			                                         <c:if test="${researcher.primaryPicture == null || !researcher.public}">
	                                                      <img class="basic_thumbnail" src="${pageContext.request.contextPath}/page-resources/images/all-images/noimage.jpg" class="noimage_size"/>
			                                         </c:if>	
						                        </urstb:td>
						                        <urstb:td>
						                                <c:if test="${researcher.public}">
						                        	        <c:url value="viewResearcherPage.action" var="viewResearcherPage">
						                        	            <c:param name="researcherId" value="${researcher.id}"/>
						                        	        </c:url>
														    <a href="${viewResearcherPage}">${researcher.user.lastName},&nbsp;${researcher.user.firstName}</a><br>
														    <c:if test="${researcher.researchInterest != '' && researcher.researchInterest != null}"><div class="smallText"><ur:maxText numChars="250" text="${researcher.researchInterest}"/></div></c:if>
														</c:if>
														<c:if test="${!researcher.public}">
														${researcher.user.lastName},&nbsp;${researcher.user.firstName} (Private)
													    </c:if>
						                        </urstb:td>
						                    </urstb:tr>
						            </urstb:tbody>
						        </urstb:table>
							    </div>
						        <!--  bottom pager -->	
						        <div class="search_div_pager">
						            <c:import url="search_all_researchers_pager.jsp"/>
						        </div>
							 </div>


							</div>
							    
								
						</c:if>
							
					 </div>
		             <!--  end tab 2 -->
		             
		          </div>
		          <!--  end content -->
		       </div>
		       <!--  end tabs -->
		    
		     </div>
		  	<!--  End body div -->
						
            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
