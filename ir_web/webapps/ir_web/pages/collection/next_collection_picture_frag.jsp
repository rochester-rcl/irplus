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

<c:if test="${irPictureFile.id > 0}">
    <c:url var="pictureUrl" value="/downloadCollectionPicture.action">
        <c:param name="irFileId" value="${irPictureFile.id}"/>
        <c:param name="collectionId" value="${collectionId}"/>
    </c:url>
    
    <img class="repository_image picture_module_size"  src="${pictureUrl}"/>

    <c:if test="${numCollectionPictures > 1}">
    <table class="buttonTable">
        <tr>
            <td class="leftButton">
                <button class="ur_button" 
	                onmouseover="this.className='ur_buttonover';"
 		            onmouseout="this.className='ur_button';"
 		            onclick="javascript:YAHOO.ur.public.collection.view.getCollectionPicture(${currentPictureLocation}, 'PREV');">&lt; Previous</button>
 		    </td>
 		    <td class="rightButton">
 		        <button class="ur_button" 
	                onmouseover="this.className='ur_buttonover';"
 		            onmouseout="this.className='ur_button';"
 		            onclick="javascript:YAHOO.ur.public.collection.view.getCollectionPicture(${currentPictureLocation}, 'NEXT');">Next &gt;</button>
            </td>
         </tr>
    </table>
    </c:if>
</c:if>
<c:if test="${irPictureFile == null }">
    <p>There are no pictures to display</p>
</c:if>