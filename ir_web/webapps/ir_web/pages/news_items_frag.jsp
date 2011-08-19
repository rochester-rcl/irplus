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


<c:if test="${!ur:isEmpty(newsItems)}">
    <table  class="baseTable">

<c:forEach items="${newsItems}" var="newsItem" >
        <c:url var="newsUrl" value="/viewNews.action">
             <c:param name="newsItemId" value="${newsItem.id}"/>
        </c:url>
        <tr>
            <td class="baseTableImage">
                 <c:if test="${ir:hasThumbnail(newsItem.primaryPicture)}">
                    <c:url var="url" value="/newsThumbnailDownloader.action">
                        <c:param name="newsItemId" value="${newsItem.id}"/>
                        <c:param name="irFileId" value="${newsItem.primaryPicture.id}"/>
                     </c:url>
                     <img class="basic_thumbnail" src="${url}"/>
                 </c:if>
            </td>
            <td>
                <p><strong><a href="${newsUrl}">${newsItem.name}</a></strong> - ${newsItem.description}</p>
            </td>
        </tr>
        
           
</c:forEach>
    </table>
    <c:if test="${newsItemCount > 2}">
    <table class="buttonTable">
        <tr>
            <td class="leftButton">
                       <button class="ur_button" 
	                           onmouseover="this.className='ur_buttonover';"
 		                       onmouseout="this.className='ur_button';"
 		                       onclick="javascript:YAHOO.ur.public.home.getNewsItems(${currentNewsItemLocation}, 'PREV');">&lt; Previous</button>
 		    </td>
 		    <td class="rightButton">
 		               <button class="ur_button" 
	                           onmouseover="this.className='ur_buttonover';"
 		                       onmouseout="this.className='ur_button';"
 		                       onclick="javascript:YAHOO.ur.public.home.getNewsItems(${currentNewsItemLocation}, 'NEXT');">Next &gt;</button>
             </td>
         </tr>
    </table>
    </c:if>
</c:if>
    


<c:if test="${ur:isEmpty(newsItems)}">
    <p>There are currently no news items</p>
</c:if>