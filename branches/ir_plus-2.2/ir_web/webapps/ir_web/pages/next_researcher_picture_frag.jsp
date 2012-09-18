
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
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<c:if test="${!ur:isEmpty(researchers)}">

    <c:url var="browseResearchers" value="/viewResearcherBrowse.action"/>
    <p><strong><a href="${browseResearchers}">Browse All/Search</a></strong></p>
	<c:forEach var="researcher" items="${researchers}" varStatus="status">
        <c:url var="researcherUrl" value="/viewResearcherPage.action">
             <c:param name="researcherId" value="${researcher.id}"/>
        </c:url>
	<table class="baseTable">
	    
	    <tr>
			<td class="baseTableImage"> 
			    <c:if test="${ir:hasThumbnail(researcher.primaryPicture)}">
			        <c:url var="url" value="/researcherThumbnailDownloader.action">
                        <c:param name="irFileId" value="${researcher.primaryPicture.id}"/>
                        <c:param name="researcherId" value="${researcher.id}"/>
                    </c:url>
                    <img src="${url}"/>
                </c:if>
                <c:if test="${!ir:hasThumbnail(researcher.primaryPicture)}">
                	   <img class="basic_thumbnail" src="${pageContext.request.contextPath}/page-resources/images/all-images/noimage.jpg" class="noimage_size"/>
                </c:if>	    												
	        </td>   
	        <td>
	              <p><strong><a href="${researcherUrl}">${researcher.user.firstName}&nbsp;${researcher.user.lastName}</a></strong>
	              <br><ur:maxText numChars="100" text="${researcher.researchInterest}"></ur:maxText> 
	              </p>
	        </td>     
	    </tr>
	
	</table>
	</c:forEach>
	
	<c:if test="${researcherCount > 2}">
	<table class="buttonTable">
	    <tr>
	        <td class="leftButton">
	            <button class="ur_button" 
		            onmouseover="this.className='ur_buttonover';"
	 		        onmouseout="this.className='ur_button';"
	 		        onclick="javascript:YAHOO.ur.public.home.getResearcherPicture(${currentResearcherLocation}, 'PREV');">&lt; Previous</button>
	 		</td>
	 		<td class="rightButton">
	 		    <button class="ur_button" 
		            onmouseover="this.className='ur_buttonover';"
	 		        onmouseout="this.className='ur_button';"
	 		        onclick="javascript:YAHOO.ur.public.home.getResearcherPicture(${currentResearcherLocation}, 'NEXT');">Next &gt;</button>
	        </td>
	     </tr>
	</table>
	</c:if>
</c:if>


<c:if test="${ur:isEmpty(researchers)}">
    <p>There are no researchers to display</p>
</c:if>

