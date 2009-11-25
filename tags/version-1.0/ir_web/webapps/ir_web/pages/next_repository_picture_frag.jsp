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
    <c:url var="pictureUrl" value="/downloadRepositoryPicture.action">
        <c:param name="irFileId" value="${irFile.id}"/>
    </c:url>
    
   <img class="repository_image" width="466px" height="278px" src="${pictureUrl}"/>
   
   <c:if test="${numPictures > 1}"> 
   <table class="buttonTable">
        <tr>
            <td class="leftButton">
                <button class="ur_button" 
	                onmouseover="this.className='ur_buttonover';"
 		            onmouseout="this.className='ur_button';"
 		            onclick="javascript:YAHOO.ur.public.home.getRepositoryPicture(${currentLocation}, 'PREV');">&lt; Previous</button>
 		    </td>
 		    <td class="rightButton">
 		        <button class="ur_button" 
	                onmouseover="this.className='ur_buttonover';"
 		            onmouseout="this.className='ur_button';"
 		            onclick="javascript:YAHOO.ur.public.home.getRepositoryPicture(${currentLocation}, 'NEXT');">Next &gt;</button>
            </td>
        </tr>
    </table>
    </c:if>
</c:if>
<c:if test="${irFile == null }">
    <p>There are no pictures to display</p>
</c:if>