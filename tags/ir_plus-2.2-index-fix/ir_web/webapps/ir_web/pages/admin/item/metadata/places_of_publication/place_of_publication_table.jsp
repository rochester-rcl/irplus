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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>


<c:if test="${totalHits > 0}">
	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
</c:if>

<c:import url="browse_all_places_of_publication_pager.jsp"/>
<br/>


<div class="dataTable">
	<form method="post" id="placesOfPublication" name="placesOfPublication" >
	             
	    <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
	                <urstb:td>Id</urstb:td>
             
	                <c:url var="nameSortAsc" value="/admin/viewPlacesOfPublication.action">
					    <c:param name="rowStart" value="${rowStart}"/>
						<c:param name="startPageNumber" value="${startPageNumber}"/>
						<c:param name="currentPageNumber" value="${currentPageNumber}"/>	
						<c:param name="sortElement" value="name"/>		
						<c:param name="sortType" value="asc"/>
				    </c:url>
				    
				    <c:url var="nameSortDesc" value="/admin/viewPlacesOfPublication.action">
					    <c:param name="rowStart" value="${rowStart}"/>
						<c:param name="startPageNumber" value="${startPageNumber}"/>
						<c:param name="currentPageNumber" value="${currentPageNumber}"/>	
						<c:param name="sortElement" value="username"/>		
						<c:param name="sortType" value="desc"/>
				    </c:url>
	                
	                <urstb:tdHeadSort  height="33"
	                    useHref="true"
					    hrefVar="href"
	                    currentSortAction="${sortType}"
	                    ascendingSortAction="${nameSortAsc}"
	                    descendingSortAction="${nameSortDesc}">
	                    <a href="${href}">Name</a>                                              
	                    <urstb:thImgSort
	                                 sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
	                                 sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/></urstb:tdHeadSort>
	                <urstb:td>2/3 letter code</urstb:td>
	                <urstb:td>Description</urstb:td>
	                <urstb:td>Actions</urstb:td>
	                </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="placeOfPublication" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${placesOfPublication}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                        ${placeOfPublication.id}
	                        </urstb:td>
	                        <urstb:td>
		                   		${placeOfPublication.name}
	                        </urstb:td>
	                        <urstb:td>
	                             ${placeOfPublication.letterCode}
	                        </urstb:td>
	                        <urstb:td>
	                              ${placeOfPublication.description}
	                        </urstb:td>	 
	                        <urstb:td>
	                           
	                            <c:url value="/admin/editPlaceOfPublication.action" var="editUrl">
	                               <c:param name="id" value="${placeOfPublication.id}"/>
	                             </c:url>
	                             <a href="${editUrl}">Edit</a>/<a href="javascript:YAHOO.ur.placeOfPublication.deleteMapping(${placeOfPublication.id});">Delete</a>
	                        </urstb:td>	                           
	                        
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>
		</form>
</div>	

<c:import url="browse_all_places_of_publication_pager.jsp"/>


