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

<c:forEach items="${alphaList}" var="alpha">
    <c:if test="${alpha != selectedAlpha}">
				             
	    <c:url var="alphaUrl" value="/browsePersonNames.action">
		    <c:param name="rowStart" value="0"/>
			<c:param name="startPageNumber" value="1"/>
			<c:param name="currentPageNumber" value="1"/>
			<c:param name="sortElement" value="${sortElement}"/>		
			<c:param name="sortType" value="${sortType}"/>	
			<c:param name="selectedAlpha" value="${alpha}"/>																	
		</c:url>
		<a href="${alphaUrl}"><strong>${alpha}</strong></a>
    </c:if>
	
	<c:if test="${alpha == selectedAlpha}">
	    <strong>${alpha}</strong>
	</c:if>
</c:forEach>