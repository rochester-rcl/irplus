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

<c:if test="${irFile.id > 0}">
    <c:url var="pictureUrl" value="/downloadResearcherPicture.action">
        <c:param name="irFileId" value="${irFile.id}"/>
        <c:param name="researcherId" value="${researcher.id}"/>
    </c:url>
    
 
    <img class="repository_image"  src="${pictureUrl}"/>
    <c:if test="${numResearcherPictures > 1}">
    <div class="button_next_left">
                <button class="ur_button" 
	                onmouseover="this.className='ur_buttonover';"
 		            onmouseout="this.className='ur_button';"
 		            onclick="javascript:YAHOO.ur.researcher.page.getResearcherPicture(${currentLocation}, 'PREV');">&lt; Previous</button>
 	</div>
 	<div class="button_next_right">
 		        <button class="ur_button" 
	                onmouseover="this.className='ur_buttonover';"
 		            onmouseout="this.className='ur_button';"
 		            onclick="javascript:YAHOO.ur.researcher.page.getResearcherPicture(${currentLocation}, 'NEXT');">Next &gt;</button>
    </div>
    </c:if>
</c:if>


<c:if test="${irFile == null }">
    
     <img class="repository_image picture_module_size" src="${pageContext.request.contextPath}/page-resources/images/all-images/noimage.jpg"/>
</c:if>

