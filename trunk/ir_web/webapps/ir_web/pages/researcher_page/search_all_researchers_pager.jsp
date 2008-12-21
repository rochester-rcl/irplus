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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>




	<ur:pager currentPageNumber="${currentPageNumber}" 
	     startPageNumber="${startPageNumber}"  
		 totalHits="${totalHits}"
		 numberOfPagesToShow="${numberOfPagesToShow}"  
		 numberOfResultsToShow="${numberOfResultsToShow}" >	

		<ur:firstPage>
	          <c:url var="browseUrl" value="/filterSearchResearchers.action">
		           <c:param name="rowStart" value="0"/>
			       <c:param name="startPageNumber" value="1"/>
			       <c:param name="currentPageNumber" value="1"/>
			       <c:param name="facets" value="${facets}"/>
				   <c:param name="facetValues" value="${facetValues}"/>	
				   <c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
				   <c:param name="query" value="${searchDataHelper.userQuery}"/>
		      </c:url>										
		 
		      <a href="${browseUrl}">First</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</ur:firstPage>
				
		<ur:previousPage>
		    <c:url var="browseUrl" value="/filterSearchResearchers.action">
	                <c:param name="rowStart" value="${rowStart}"/>
					<c:param name="startPageNumber" value="${prevousPageStartPageNumber}"/>
					<c:param name="currentPageNumber" value="${currentPageNumber - 1}"/>
					<c:param name="facets" value="${facets}"/>
				    <c:param name="facetValues" value="${facetValues}"/>	
				    <c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
				    <c:param name="query" value="${searchDataHelper.userQuery}"/>																				
			</c:url>	

			<&nbsp;<a href="${browseUrl}">Previous</a> &nbsp;&nbsp;

			<ur:morePrevious>
		          <c:url var="browseUrl" value="/filterSearchResearchers.action">
						<c:param name="rowStart" value="${rowStart}"/>
						<c:param name="startPageNumber" value="${startPageNumberForPreviousSet}"/>
						<c:param name="currentPageNumber" value="${startPageNumberForPreviousSet}"/>
						<c:param name="facets" value="${facets}"/>
					    <c:param name="facetValues" value="${facetValues}"/>	
					    <c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
					    <c:param name="query" value="${searchDataHelper.userQuery}"/>																				
			      </c:url>										
			      &nbsp;<a href="${browseUrl}">....</a> &nbsp;&nbsp;
			 </ur:morePrevious>		      
	
	
		</ur:previousPage>
								
		<ur:forEachPage var="pageNumber">
		    <c:if test="${pageNumber != currentPageNumber}">
			     <c:url var="browseUrl" value="/filterSearchResearchers.action">
				     <c:param name="rowStart" value="${rowStart}"/>
					 <c:param name="startPageNumber" value="${startPageNumber}"/>
					 <c:param name="currentPageNumber" value="${pageNumber}"/>
					 <c:param name="facets" value="${facets}"/>
				     <c:param name="facetValues" value="${facetValues}"/>	
				     <c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
				     <c:param name="query" value="${searchDataHelper.userQuery}"/>											
				 </c:url>		
																			
				 <a href="${browseUrl}">${pageNumber}</a>&nbsp;&nbsp;
		     </c:if>
										
			 <c:if test="${pageNumber == currentPageNumber}">
			     ${pageNumber}&nbsp;&nbsp;
			 </c:if>
		</ur:forEachPage>								
															
		<ur:nextPage>
	       <ur:moreNext>
		        <c:url var="browseUrl" value="/filterSearchResearchers.action">
				    <c:param name="rowStart" value="${nextSetRowStart}"/>
					<c:param name="startPageNumber" value="${startPageNumberForNextSet}"/>
					<c:param name="currentPageNumber" value="${startPageNumberForNextSet}"/>	
					<c:param name="facets" value="${facets}"/>
					<c:param name="facetValues" value="${facetValues}"/>	
					<c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
					<c:param name="query" value="${searchDataHelper.userQuery}"/>																		
			    </c:url>										
				<a href="${browseUrl}">....</a>&nbsp;&nbsp;&nbsp;
	        </ur:moreNext>

		    <c:url var="browseUrl" value="/filterSearchResearchers.action">
			    <c:param name="rowStart" value="${rowStart}"/>
				<c:param name="startPageNumber" value="${nextPageStartPageNumber}"/>
				<c:param name="currentPageNumber" value="${currentPageNumber + 1}"/>	
				<c:param name="facets" value="${facets}"/>
				<c:param name="facetValues" value="${facetValues}"/>	
				<c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
				<c:param name="query" value="${searchDataHelper.userQuery}"/>																		
			</c:url>	

		     <a href="${browseUrl}">Next</a>&nbsp;> &nbsp;&nbsp;
	    </ur:nextPage>

	       <ur:lastPage>
		        <c:url var="browseUrl" value="/filterSearchResearchers.action">
				    <c:param name="rowStart" value="${rowstartForLastPage}"/>
					<c:param name="startPageNumber" value="${startPageNumber}"/>
					<c:param name="currentPageNumber" value="${currentPageNumber}"/>	
					<c:param name="facets" value="${facets}"/>
				    <c:param name="facetValues" value="${facetValues}"/>	
				    <c:param name="facetDisplayNames" value="${facetDisplayNames}"/>	
				    <c:param name="query" value="${searchDataHelper.userQuery}"/>																		
			    </c:url>										
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${browseUrl}">Last</a> 
			</ur:lastPage>	
				    
	</ur:pager>	

				         
				         