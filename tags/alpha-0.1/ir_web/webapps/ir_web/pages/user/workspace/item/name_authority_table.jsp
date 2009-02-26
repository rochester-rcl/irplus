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

<!-- Displays the search results for the names
 -->
 
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${totalHits > 0}">
	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
	<c:import url="search_contributor_pager.jsp"/>
</c:if>	    	


<ur:basicForm method="post" id="names" name="myNames" 
     action="javascript:YAHOO.ur.item.contributor.getNames(0)">

	 <input type="hidden" id="names_query" name="query" value="${query}"/>
	 <input type="hidden" id="names_itemId" name="genericItemId" value="${item.id}"/>
	 <input type="hidden" id="names_rowStart" name="rowStart" value="${rowStart}"/>
	 <input type="hidden" id="start_page_num" name="startPageNumber" value="${startPageNumber}"/>
	 <input type="hidden" id="current_page_num" name="currentPageNumber" value="${currentPageNumber}"/>

	<!-- Table for files and folders  -->            
	<table class="itemFolderTable" width="100%">
		<thead>
			<tr>
				<th class="thItemFolder" width="17%">Add</th>
				<th class="thItemFolder">Contributors</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="nameAuthority" items="${nameAuthorities}">
				<ir:displayNames personNameAuthority="${nameAuthority}" genericItem="${item}" user="${user}"/>
			</c:forEach>
		</tbody>
	</table>	

</ur:basicForm>

<c:if test="${totalHits > 0}">
	<c:import url="search_contributor_pager.jsp"/>
</c:if>	    	

				