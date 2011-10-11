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
        <title>Browsing/Searching: All of ${repository.name}</title>
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

	    <ur:js src="page-resources/js/util/base_path.jsp"/>
	    <ur:js src="page-resources/js/util/ur_util.js"/>
	    <ur:js src="page-resources/js/menu/main_menu.js"/>
         
         <c:url var="browseRepositoryItems" value="/browseRepositoryItems.action"/>
         <c:url var="browsePersonNames" value="/browsePersonNames.action"/>
         <c:url var="browseSponsors" value="/browseSponsorNames.action"/>
         <c:url var="searchRepositoryItems" value="/startSearchRepositoryItems.action"/>
		 <script type="text/javascript">

		  function updateUrl()
		  {
			  document.contentType.submit();
		  }
    
          function handleBrowseClick(e) {  
               window.location='${browseRepositoryItems}';
          }

          function handleBrowseNamesClick(e) { 
              window.location='${browsePersonNames}';
          }
          
          function handleSearchClick(e)
          {
              window.location='${searchRepositoryItems}';
          }

          function handleBrowseSponsorsClick(e)
          {
              window.location="${browseSponsors}";
          }
          
          function init()
          {
                var myTabs = new YAHOO.widget.TabView("all-items-tabs");
	            var tab0 = myTabs.getTab(0);
	            tab0.addListener('click', handleBrowseClick);
	            
	            var tab1 = myTabs.getTab(1);
	            tab1.addListener('click', handleSearchClick);

	            var tab2 = myTabs.getTab(2);
	            tab2.addListener('click', handleBrowseNamesClick);

	            var tab3 = myTabs.getTab(3);
	            tab3.addListener('click', handleBrowseSponsorsClick);

	            var search = document.getElementById('search');
	            search.focus();
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
            	
            	 <h3> Browse/Search: <a href="home.action">${repository.name}</a></h3>
		        
		        
		        <!--  set up tabs for editing news -->
		        <div id="all-items-tabs" class="yui-navset">
		            <ul class="yui-nav">
		               <c:if test='${viewType == "browse"}'>
		                    <li class="selected"><a href="${browseRepositoryItems}"><em>Browse Publications</em></a></li>
		                    <li><a href="${searchRepositoryItems}"><em>Search</em></a></li>
		                    <li><a href="${browsePersonNames}"><em>Browse Authors/Contributors</em></a></li>
		                    <li><a href="${browseSponsors}"><em>Browse Sponsors</em></a></li>
		               </c:if>
		               <c:if test='${viewType == "search"}'>
		                    <li><a href="${browseRepositoryItems}"><em>Browse Publications</em></a></li>
		                    <li class="selected"><a href="${searchRepositoryItems}"><em>Search</em></a></li>
		                    <li><a href="${browsePersonNames}"><em>Browse Authors/Contributors</em></a></li>
		                    <li><a href="${browseSponsors}"><em>Browse Sponsors</em></a></li>
		               </c:if>
		                <c:if test='${viewType == "browsePersonName"}'>
		                    <li><a href="${browseRepositoryItems}"><em>Browse Publications</em></a></li>
		                    <li><a href="${searchRepositoryItems}"><em>Search</em></a></li>
		                    <li class="selected"><a href="${searchPersonNames}"><em>Browse Authors/Contributors</em></a></li>
		                    <li><a href="${browseSponsors}"><em>Browse Sponsors</em></a></li>
		               </c:if>
		               <c:if test='${viewType == "browseSponsorName"}'>
		                    <li><a href="${browseRepositoryItems}"><em>Browse Publications</em></a></li>
		                    <li><a href="${searchRepositoryItems}"><em>Search</em></a></li>
		                    <li><a href="${searchPersonNames}"><em>Browse Authors/Contributors</em></a></li>
		                    <li class="selected"><a href="${browseSponsors}"><em>Browse Sponsors</em></a></li>
		               </c:if>
		            </ul>
		
		            <div class="yui-content">
		            
		                <!--  first tab -->
		                <div id="tab1">
		                  
				         <c:if test='${viewType == "browse"}'>
				         
				        
				         <form name="contentType" action="${browseRepositoryItems}" method="get">
				             <input type="hidden" name="rowStart" value="0"/>
			                 <input type="hidden" name="startPageNumber" value="1"/>
			                 <input type="hidden" name="currentPageNumber" value="1"/>
			                 <input type="hidden" name="sortElement" value="${sortElement}"/>		
			                 <input type="hidden" name="sortType" value="${sortType}"/>	
			                 <input type="hidden" name="selectedAlpha" value="${alpha}"/>
				         Filter By Content Type:
				         <select name="contentTypeId" onchange="javascript:updateUrl();">
				            <c:if test="${contentTypeId == -1}">
				                <option  value="-1" selected="selected">All</option>
				            </c:if>
				            <c:if test="${contentTypeId != -1}">
				                <option value="-1">All</option>
				            </c:if>
						    <c:forEach items="${contentTypes}" var="contentType">
						        <c:if test="${contentType.id == contentTypeId}">
						            <option  selected="selected" value="${contentType.id}">${contentType.name}</option>
						        </c:if>
						        <c:if test="${contentType.id != contentTypeId}">
						            <option value="${contentType.id}">${contentType.name}</option>
						        </c:if>
						    </c:forEach>
						 </select>
						 </form>
				         
				         <div class="center">
				              <c:import url="browse_all_items_alpha_list.jsp"/>
				         </div>
				    	 <c:if test="${totalHits > 0}">
				         	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
				         </c:if>  
				         <c:import url="browse_all_items_pager.jsp"/>
						 <br/>
						


						<div class="dataTable">
							             
					        <urstb:table width="100%">
					            <urstb:thead>
					                <urstb:tr>
					                    <urstb:td>Thumbnail</urstb:td>
					                    <c:url var="sortAscendingNameUrl" value="/browseRepositoryItems.action">
										     <c:param name="rowStart" value="${rowStart}"/>
											 <c:param name="startPageNumber" value="${startPageNumber}"/>
											 <c:param name="currentPageNumber" value="${currentPageNumber}"/>	
											 <c:param name="sortElement" value="name"/>		
											 <c:param name="sortType" value="asc"/>
											 <c:param name="selectedAlpha" value="${selectedAlpha}"/>	
											 <c:param name="contentTypeId" value="${contentTypeId}"/>	
										</c:url>
					                     
					                    <c:url var="sortDescendingNameUrl" value="/browseRepositoryItems.action">
										    <c:param name="rowStart" value="${rowStart}"/>
											<c:param name="startPageNumber" value="${startPageNumber}"/>
											<c:param name="currentPageNumber" value="${currentPageNumber}"/>	
											<c:param name="sortElement" value="name"/>		
											<c:param name="sortType" value="desc"/>
											<c:param name="selectedAlpha" value="${selectedAlpha}"/>	
											<c:param name="contentTypeId" value="${contentTypeId}"/>	
										</c:url>
										
										<c:url var="sortAscendingPublicationDateUrl" value="/browseRepositoryItems.action">
										     <c:param name="rowStart" value="${rowStart}"/>
											 <c:param name="startPageNumber" value="${startPageNumber}"/>
											 <c:param name="currentPageNumber" value="${currentPageNumber}"/>	
											 <c:param name="sortElement" value="publicationDate"/>		
											 <c:param name="sortType" value="asc"/>
											 <c:param name="selectedAlpha" value="${selectedAlpha}"/>	
											 <c:param name="contentTypeId" value="${contentTypeId}"/>	
										</c:url>
					                     
					                    <c:url var="sortDescendingPublicationDateUrl" value="/browseRepositoryItems.action">
										    <c:param name="rowStart" value="${rowStart}"/>
											<c:param name="startPageNumber" value="${startPageNumber}"/>
											<c:param name="currentPageNumber" value="${currentPageNumber}"/>	
											<c:param name="sortElement" value="publicationDate"/>		
											<c:param name="sortType" value="desc"/>
											<c:param name="selectedAlpha" value="${selectedAlpha}"/>	
											<c:param name="contentTypeId" value="${contentTypeId}"/>	
										</c:url>
					                    
					                    
					                    <c:url var="sortAscendingFirstAvailableDateUrl" value="/browseRepositoryItems.action">
										     <c:param name="rowStart" value="${rowStart}"/>
											 <c:param name="startPageNumber" value="${startPageNumber}"/>
											 <c:param name="currentPageNumber" value="${currentPageNumber}"/>	
											 <c:param name="sortElement" value="firstAvailableDate"/>		
											 <c:param name="sortType" value="asc"/>
											 <c:param name="selectedAlpha" value="${selectedAlpha}"/>	
											 <c:param name="contentTypeId" value="${contentTypeId}"/>	
										</c:url>
					                     
					                    <c:url var="sortDescendingFirstAvailableDateUrl" value="/browseRepositoryItems.action">
										    <c:param name="rowStart" value="${rowStart}"/>
											<c:param name="startPageNumber" value="${startPageNumber}"/>
											<c:param name="currentPageNumber" value="${currentPageNumber}"/>	
											<c:param name="sortElement" value="firstAvailableDate"/>		
											<c:param name="sortType" value="desc"/>
											<c:param name="selectedAlpha" value="${selectedAlpha}"/>	
											<c:param name="contentTypeId" value="${contentTypeId}"/>	
										</c:url>
					                    
					                    
					                    <c:set var="nameSort" value="none"/>
					                    <c:if test='${sortElement == "name"}'>
					                        <c:set var="nameSort" value="${sortType}"/>
					                    </c:if>
					                    
					                    <c:set var="publicationDateSort" value="none"/>
					                    <c:if test='${sortElement == "publicationDate"}'>
					                        <c:set var="publicationDateSort" value="${sortType}"/>
					                    </c:if>
					                    
					                     <c:set var="firstAvailableDateSort" value="none"/>
					                    <c:if test='${sortElement == "firstAvailableDate"}'>
					                        <c:set var="firstAvailableDateSort" value="${sortType}"/>
					                    </c:if>
					                    
					                    <urstb:tdHeadSort  height="33"
					                        useHref="true"
					                        hrefVar="href"
                                            currentSortAction="${nameSort}"
                                            ascendingSortAction="${sortAscendingNameUrl}"
                                            descendingSortAction="${sortDescendingNameUrl}"
                                            defaultSort="asc">
                                            <a href="${href}">Name</a>                                              
                                            <urstb:thImgSort
                                                         sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
                                                         sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/></urstb:tdHeadSort>
					                    
					                     <urstb:tdHeadSort  height="33"
					                        width="125"
					                        useHref="true"
					                        hrefVar="href"
                                            currentSortAction="${publicationDateSort}"
                                            ascendingSortAction="${sortAscendingPublicationDateUrl}"
                                            descendingSortAction="${sortDescendingPublicationDateUrl}"
                                            defaultSort="desc">
                                            <a href="${href}">Publication Date</a>                                              
                                            <urstb:thImgSort
                                                         sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
                                                         sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/></urstb:tdHeadSort>
					                    <urstb:tdHeadSort  height="33"
					                        width="125"
					                        useHref="true"
					                        hrefVar="href"
                                            currentSortAction="${firstAvailableDateSort}"
                                            ascendingSortAction="${sortAscendingFirstAvailableDateUrl}"
                                            descendingSortAction="${sortDescendingFirstAvailableDateUrl}"
                                            defaultSort="desc">
                                            <a href="${href}">First Public Date</a>                                              
                                            <urstb:thImgSort
                                                         sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
                                                         sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/></urstb:tdHeadSort>
					                    <urstb:td>Contributors</urstb:td>
						                </urstb:tr>
						            </urstb:thead>
						            <urstb:tbody
						                var="institutionalItem" 
						                oddRowClass="odd"
						                evenRowClass="even"
						                currentRowClassVar="rowClass"
						                collection="${institutionalItems}">
						                    <urstb:tr 
						                        cssClass="${rowClass}"
						                        onMouseOver="this.className='highlight'"
						                        onMouseOut="this.className='${rowClass}'">
						                        
						                        <urstb:td>
						                        
						                             <c:if test="${ir:hasThumbnail(institutionalItem.versionedInstitutionalItem.currentVersion.item.primaryImageFile.irFile)}">
						                                  <ir:itemTransformUrl itemFile="${institutionalItem.versionedInstitutionalItem.currentVersion.item.primaryImageFile}" var="url" systemCode="PRIMARY_THUMBNAIL" download="true"/>
						                                  <c:if test="${url != null}">
						                                       <img class="basic_thumbnail" src="${url}" />
						                                  </c:if>
						                                     
			                                         </c:if>	
						                        </urstb:td>
						                        <urstb:td>
						                            <c:url var="itemView" value="/institutionalPublicationPublicView.action">
						                                 <c:param name="institutionalItemId" value="${institutionalItem.id}"/>
						                            </c:url>
						                        	<a href="${itemView}">${institutionalItem.name}</a><br/>
						                        	
						                        	<c:set var="description" value="${ir:getItemDescription(institutionalItem.versionedInstitutionalItem.currentVersion.item)}"/>
						                        	<c:if test="${!ir:isStringEmpty(description)}"><div class="smallText"><ur:maxText numChars="350" text="${description}"/></div></c:if>
						                        </urstb:td>
						                        
						                        <urstb:td>
						                            ${ir:getSlashedDate(institutionalItem.versionedInstitutionalItem.currentVersion.item.externalPublishedItem.publishedDate)}
						                        </urstb:td>
						                        <urstb:td>
						                            ${ir:getSlashedDate(institutionalItem.versionedInstitutionalItem.currentVersion.item.firstAvailableDate)}
						                        </urstb:td>
						                        <urstb:td>
						                             <c:forEach var="itemContributor" items="${institutionalItem.versionedInstitutionalItem.currentVersion.item.contributors}">
									                    <c:url var="contributorUrl" value="/viewContributorPage.action">
														    <c:param name="personNameId" value="${itemContributor.contributor.personName.id}"/>
														</c:url>						                             
						                                 <a href="${contributorUrl}"><ir:authorName personName="${itemContributor.contributor.personName}" displayDates="true"/><br/> 
						                             </c:forEach>
						                        </urstb:td>
						                    </urstb:tr>
						            </urstb:tbody>
						        </urstb:table>
						    </div>	
                         
					        <c:import url="browse_all_items_pager.jsp"/>
					        
					         <br/>
				             <br/>
				             
					        <div class="center">
				                 <c:import url="browse_all_items_alpha_list.jsp"/>
				            </div>
				         
				        
					        </c:if>
			        </div>
		            <!--  end first tab -->
		                  
		                  
	              	 <!--  start second tab -->
	               	 <div id="tab2">
	               	    <br/>
	               	     <c:url var="searchRepositoryItems" value="/searchRepositoryItems.action"/>
						<form method="GET" action="${searchRepositoryItems}">
						    Search: <input type="text" name="query" size="50" id="search" /><br/><br/>
						    <button type="submit" class="ur_button" 
		                               onmouseover="this.className='ur_buttonover';"
	 		                           onmouseout="this.className='ur_button';">Search</button>
						</form>
						<br/>
						
						<c:if test='${viewType == "search" && !searchInit}'>
						<div class="full_text_search_results">
						<div class="facet_container">
						    <div class="facet_search_selection">
						          <h3>Current Search</h3>
						          <p><strong>Search:</strong> ${searchDataHelper.userQuery} <br/></p>
						          <c:forEach var="facet" varStatus="status" items="${searchDataHelper.facetTrail}">
						              <c:url var="removeFacet" value="/filterSearchRepositoryItems.action">
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
						          <ir:facet var="authors" key="authors" facets="${searchDataHelper.facets}">
						              <h3>Contributors</h3>
						             
						              <c:forEach var="author" items="${authors}">
						                  <c:if test="${!ir:facetSelected(searchDataHelper, author)}">
						                      <c:url var="authorFilter" value="/filterSearchRepositoryItems.action">
						                          <c:param name="rowStart" value="0"/>
					                              <c:param name="startPageNumber" value="1"/>
					                              <c:param name="currentPageNumber" value="1"/>
						                          <c:param name="mostRecentFacetName" value="contributor_names"/>
						                          <c:param name="mostRecentFacetValue" value="${author.facetName}"/>
						                          <c:param name="mostRecentFacetDisplayName" value="Author"/>
						                          <c:param name="facets" value="${facets}"/>
						                          <c:param name="facetValues" value="${facetValues}"/>	
						                          <c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
						                          <c:param name="query" value="${searchDataHelper.userQuery}"/> 					  
						                      </c:url>
						                      <div class="hanging_indent"><a href="${authorFilter}">${author.facetName} (${author.hits})</a></div>
						                  </c:if>
						              </c:forEach>
						          </ir:facet>
						          
						          <ir:facet var="collections" key="collections" facets="${searchDataHelper.facets}">
						             <h3>Collections</h3>
						             <c:forEach var="collection" items="${collections}">
						                 <c:if test="${!ir:facetSelected(searchDataHelper, collection)}">
						                     <c:url var="collectionFilter" value="/filterSearchRepositoryItems.action">
						                         <c:param name="rowStart" value="0"/>
					                             <c:param name="startPageNumber" value="1"/>
					                             <c:param name="currentPageNumber" value="1"/>
						                         <c:param name="mostRecentFacetName" value="collection_name"/>
						                         <c:param name="mostRecentFacetValue" value="${collection.facetName}"/>
						                         <c:param name="mostRecentFacetDisplayName" value="Collection"/>
						                         <c:param name="facets" value="${facets}"/>
						                         <c:param name="facetValues" value="${facetValues}"/>	
						                         <c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
						                         <c:param name="query" value="${searchDataHelper.userQuery}"/> 					  
						                     </c:url>
						                    <div class="hanging_indent"><a href="${collectionFilter}">${collection.facetName} (${collection.hits})</a></div> 
						                 </c:if>
						             </c:forEach>
						        </ir:facet>
						
						          <ir:facet var="formats" key="formats" facets="${searchDataHelper.facets}">
						              <h3>Formats</h3>
						              <c:forEach var="format" items="${formats}">
						                 <c:if test="${!ir:facetSelected(searchDataHelper, format)}">
						                     <c:url var="formatFilter" value="/filterSearchRepositoryItems.action">
						                         <c:param name="rowStart" value="0"/>
					                             <c:param name="startPageNumber" value="1"/>
					                             <c:param name="currentPageNumber" value="1"/>
						                         <c:param name="mostRecentFacetName" value="content_type"/>
						                         <c:param name="mostRecentFacetValue" value="${format.facetName}"/>
						                         <c:param name="mostRecentFacetDisplayName" value="Format"/>
						                         <c:param name="facets" value="${facets}"/>
						                         <c:param name="facetValues" value="${facetValues}"/>	
						                         <c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
						                         <c:param name="query" value="${searchDataHelper.userQuery}"/> 					  
						                     </c:url>
						                    <div class="hanging_indent"><a href="${formatFilter}"> ${format.facetName} (${format.hits})</a></div>
						                  </c:if>
						              </c:forEach>
						          </ir:facet>
						
						         <ir:facet var="subjects" key="subjects" facets="${searchDataHelper.facets}">
						             <h3>Subjects</h3>
						             <c:forEach var="subject" items="${subjects}">
						                 <c:if test="${!ir:facetSelected(searchDataHelper, subject)}">
						                     <c:url var="subjectFilter" value="/filterSearchRepositoryItems.action">
						                         <c:param name="rowStart" value="0"/>
					                             <c:param name="startPageNumber" value="1"/>
					                             <c:param name="currentPageNumber" value="1"/>
						                         <c:param name="mostRecentFacetName" value="keywords"/>
						                         <c:param name="mostRecentFacetValue" value="${subject.facetName}"/>
						                         <c:param name="mostRecentFacetDisplayName" value="Subject"/>
						                         <c:param name="facets" value="${facets}"/>
						                         <c:param name="facetValues" value="${facetValues}"/>	
						                         <c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
						                         <c:param name="query" value="${searchDataHelper.userQuery}"/> 					  
						                     </c:url>
						                     <div class="hanging_indent"><a href="${subjectFilter}">${subject.facetName} (${subject.hits})</a></div>
						                 </c:if>
						             </c:forEach>
						         </ir:facet>
						
						         <ir:facet var="languages" key="languages" facets="${searchDataHelper.facets}">
						             <h3>Languages</h3>
						             <c:forEach var="language" items="${languages}">
						                 <c:if test="${!ir:facetSelected(searchDataHelper, language)}">
						                     <c:url var="languageFilter" value="/filterSearchRepositoryItems.action">
						                         <c:param name="rowStart" value="0"/>
					                             <c:param name="startPageNumber" value="1"/>
					                             <c:param name="currentPageNumber" value="1"/>
						                         <c:param name="mostRecentFacetName" value="language"/>
						                         <c:param name="mostRecentFacetValue" value="${language.facetName}"/>
						                         <c:param name="mostRecentFacetDisplayName" value="Language"/>
						                         <c:param name="facets" value="${facets}"/>
						                         <c:param name="facetValues" value="${facetValues}"/>	
						                         <c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
						                         <c:param name="query" value="${searchDataHelper.userQuery}"/> 					  
						                     </c:url>
						                     <div class="hanging_indent"><a href="${languageFilter}">${language.facetName} (${language.hits})</a></div>
						                 </c:if>
						             </c:forEach>
						        </ir:facet>
						    </div>
						</div>
						        
						<div class="search_results_table">
						    <div class="search_div_pager">
						        <c:import url="search_all_items_pager.jsp"/>
						    </div>
						    <c:if test="${searchDataHelper.hitSize > 0}">
						    	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${searchDataHelper.hitSize} for search: ${searchDataHelper.userQuery} (Relevance Ranked Order)</h3>               
						    
						    <div class="dataTable">
							
					        <urstb:table width="100%">
					            <urstb:thead>
					                    <urstb:td>Thumbnail</urstb:td>
					                    <urstb:td>Name</urstb:td>
					                    <urstb:td>Contributors</urstb:td>
						            </urstb:thead>
						            <urstb:tbody
						                var="institutionalItem" 
						                oddRowClass="odd"
						                evenRowClass="even"
						                currentRowClassVar="rowClass"
						                collection="${searchInstitutionalItems}">
						                    <urstb:tr 
						                        cssClass="${rowClass}"
						                        onMouseOver="this.className='highlight'"
						                        onMouseOut="this.className='${rowClass}'">
						                        <urstb:td>
						                             <c:if test="${ir:hasThumbnail(institutionalItem.versionedInstitutionalItem.currentVersion.item.primaryImageFile.irFile)}">
						                               <ir:itemTransformUrl systemCode="PRIMARY_THUMBNAIL" download="true" itemFile="${institutionalItem.versionedInstitutionalItem.currentVersion.item.primaryImageFile}" var="url"/>
                                                       <c:if test="${url != null}">
                                                         <img class="basic_thumbnail" src="${url}"/>
                                                       </c:if> 
			                                         </c:if>	
						                        </urstb:td>
						                        <urstb:td>
						                            <c:url var="itemView" value="/institutionalPublicationPublicView.action">
						                                 <c:param name="institutionalItemId" value="${institutionalItem.id}"/>
						                            </c:url>
						                        <a href="${itemView}">${institutionalItem.name}</a>
						                        <c:set var="description" value="${ir:getItemDescription(institutionalItem.versionedInstitutionalItem.currentVersion.item)}"/>
						                        <c:if test="${!ir:isStringEmpty(description)}"><div class="smallText"><ur:maxText numChars="150" text="${description}"/></div></c:if>
						                        </urstb:td>
						                        
						                        <urstb:td>
						                             <c:forEach var="itemContributor" items="${institutionalItem.versionedInstitutionalItem.currentVersion.item.contributors}">
									                    <c:url var="contributorUrl" value="/viewContributorPage.action">
														    <c:param name="personNameId" value="${itemContributor.contributor.personName.id}"/>
														</c:url>						                             
						                                 <a href="${contributorUrl}"> <ir:authorName personName="${itemContributor.contributor.personName}" displayDates="true"/></a> - ${itemContributor.contributor.contributorType.name} <br/> 
						                             </c:forEach>
						                        </urstb:td>
						                    </urstb:tr>
						            </urstb:tbody>
						        </urstb:table>
						    </div>
						    </c:if>
						    
						    
						       <c:if test="${searchDataHelper.hitSize <= 0}">
						           <h3> No results found  for search: ${searchDataHelper.userQuery} </h3>
						       </c:if>
						        <!--  bottom pager -->	
						        <div class="search_div_pager">
						            <c:import url="search_all_items_pager.jsp"/>
						        </div>
						    </div>
						    
						    
						    </div>
						
						</c:if>
					 </div>
		             <!--  end tab 2 -->
		             
		             <!--  start 3rd tab -->
		             <div id="tab3">
		                  
				         <c:if test='${viewType == "browsePersonName"}'>
				         
				         <div class="center">
				              <c:import url="browse_all_person_names_alpha_list.jsp"/>
				         </div>
				    	 <c:if test="${totalHits > 0}">
				         	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
				         </c:if>  
				         <c:import url="browse_all_person_names_pager.jsp"/>
						<br/>
						
						<div class="dataTable">
							             
					        <urstb:table width="100%">
					            <urstb:thead>
					                <urstb:tr>
					                    
					                    <!--  set up the url's for sorting by last name -->
					                     <c:url var="sortLastNameAscendingUrl" value="/browsePersonNames.action">
										     <c:param name="rowStart" value="${rowStart}"/>
											 <c:param name="startPageNumber" value="${startPageNumber}"/>
											 <c:param name="currentPageNumber" value="${currentPageNumber}"/>	
											 <c:param name="sortElement" value="lastName"/>		
											 <c:param name="sortType" value="asc"/>
											 <c:param name="selectedAlpha" value="${selectedAlpha}"/>	
										</c:url>
					                     
					                    <c:url var="sortLastNameDescendingUrl" value="/browsePersonNames.action">
										    <c:param name="rowStart" value="${rowStart}"/>
											<c:param name="startPageNumber" value="${startPageNumber}"/>
											<c:param name="currentPageNumber" value="${currentPageNumber}"/>	
											<c:param name="sortElement" value="lastName"/>		
											<c:param name="sortType" value="desc"/>
											<c:param name="selectedAlpha" value="${selectedAlpha}"/>	
										</c:url>
					                    
					                    <c:set var="lastNameSort" value="none"/>
					                    <c:if test='${sortElement == "lastName"}'>
					                        <c:set var="lastNameSort" value="${sortType}"/>
					                    </c:if>
					                    <urstb:tdHeadSort  height="33"
					                        useHref="true"
					                        hrefVar="href"
                                            currentSortAction="${lastNameSort}"
                                            ascendingSortAction="${sortLastNameAscendingUrl}"
                                            descendingSortAction="${sortLastNameDescendingUrl}">
                                            <a href="${href}">Name</a>                                              
                                            <urstb:thImgSort
                                                         sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
                                                         sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/></urstb:tdHeadSort>
					                     
						                </urstb:tr>
						            </urstb:thead>
						            <urstb:tbody
						                var="personName" 
						                oddRowClass="odd"
						                evenRowClass="even"
						                currentRowClassVar="rowClass"
						                collection="${personNames}">
						                    <urstb:tr 
						                        cssClass="${rowClass}"
						                        onMouseOver="this.className='highlight'"
						                        onMouseOut="this.className='${rowClass}'">
						                        <urstb:td>
						                            <c:url var="contributorUrl" value="/viewContributorPage.action">
														    <c:param name="personNameId" value="${personName.id}"/>
												    </c:url>	
						                             <a href="${contributorUrl}"><ir:authorName personName="${personName}" displayDates="true" lastNameFirst="true"/>
						                             </a>
						                        </urstb:td>
						                        
						                    </urstb:tr>
						            </urstb:tbody>
						        </urstb:table>
						    </div>	
                         
					        <c:import url="browse_all_person_names_pager.jsp"/>
					        
					         <br/>
				             <br/>
				             
					        <div class="center">
				                 <c:import url="browse_all_person_names_alpha_list.jsp"/>
				            </div>
				         
				        
					        </c:if>
			        </div>
		            <!--  end tab  3-->
		            
		            
		            <!--  start 4th tab -->
		            <div id="tab4">
		                  
				         <c:if test='${viewType == "browseSponsorName"}'>
				         
				         <div class="center">
				              <c:import url="browse_all_sponsor_names_alpha_list.jsp"/>
				         </div>
				    	 <c:if test="${totalHits > 0}">
				         	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
				         </c:if>  
				         <c:import url="browse_all_sponsor_names_pager.jsp"/>
						<br/>
						
						<div class="dataTable">
							             
					        <urstb:table width="100%">
					            <urstb:thead>
					                <urstb:tr>
					                    
					                    <!--  set up the url's for sorting by last name -->
					                     <c:url var="sortSponsorNameAscendingUrl" value="/browseSponsorNames.action">
										     <c:param name="rowStart" value="${rowStart}"/>
											 <c:param name="startPageNumber" value="${startPageNumber}"/>
											 <c:param name="currentPageNumber" value="${currentPageNumber}"/>	
											 <c:param name="sortType" value="asc"/>
											 <c:param name="selectedAlpha" value="${selectedAlpha}"/>	
										</c:url>
					                     
					                    <c:url var="sortSponsorNameDescendingUrl" value="/browseSponsorNames.action">
										    <c:param name="rowStart" value="${rowStart}"/>
											<c:param name="startPageNumber" value="${startPageNumber}"/>
											<c:param name="currentPageNumber" value="${currentPageNumber}"/>	
											<c:param name="sortType" value="desc"/>
											<c:param name="selectedAlpha" value="${selectedAlpha}"/>	
										</c:url>
					                    
					                    <c:set var="sponsorNameSortType" value="none"/>
					                    <c:if test='${sortElement == "sponsorName"}'>
					                        <c:set var="sponsorNameSortType" value="${sortType}"/>
					                    </c:if>
					                    <urstb:tdHeadSort  height="33"
					                        useHref="true"
					                        hrefVar="href"
                                            currentSortAction="${sponsorNameSortType}"
                                            ascendingSortAction="${sortSponsorNameAscendingUrl}"
                                            descendingSortAction="${sortSponsorNameDescendingUrl}">
                                            <a href="${href}">Name</a>                                              
                                            <urstb:thImgSort
                                                         sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
                                                         sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/></urstb:tdHeadSort>
					                     
						                <urstb:td>Description</urstb:td>
						                </urstb:tr>
						            </urstb:thead>
						            <urstb:tbody
						                var="sponsor" 
						                oddRowClass="odd"
						                evenRowClass="even"
						                currentRowClassVar="rowClass"
						                collection="${sponsors}">
						                    <urstb:tr 
						                        cssClass="${rowClass}"
						                        onMouseOver="this.className='highlight'"
						                        onMouseOut="this.className='${rowClass}'">
						                        <urstb:td>
						                            <c:url var="sponsorUrl" value="/viewSponsorPage.action">
														    <c:param name="sponsorId" value="${sponsor.id}"/>
												    </c:url>	
						                             <a href="${sponsorUrl}">${sponsor.name}</a>
						                        </urstb:td>
						                        <urstb:td>
						                            ${sponsor.description}
						                        </urstb:td>
						                    </urstb:tr>
						            </urstb:tbody>
						        </urstb:table>
						    </div>	
                         
					        <c:import url="browse_all_sponsor_names_pager.jsp"/>
					        
					         <br/>
				             <br/>
				             
					        <div class="center">
				                 <c:import url="browse_all_sponsor_names_alpha_list.jsp"/>
				            </div>
				         
				        
					        </c:if>
			        </div>
		            <!--  end tab  4-->
		            
		            
		            
		            
		             
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

    
