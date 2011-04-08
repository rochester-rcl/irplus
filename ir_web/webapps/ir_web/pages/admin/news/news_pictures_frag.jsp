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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<h3>Primary Picture</h3>
<c:if test="${newsItem.primaryPicture != null }">
    <div class="dataTable">
	<table width="100%">
		<thead>
			<tr>
				<td>Thumbnail</td>
				<td>Picture URL</td>
				<td>Action</td>
			</tr>
		</thead>
		<tbody>
			
			<tr class="even">
				<td>
				 <c:if test="${ir:hasThumbnail(newsItem.primaryPicture)}">
                     <c:url var="url" value="/newsThumbnailDownloader.action">
                         <c:param name="newsItemId" value="${newsItem.id}"/>
                        <c:param name="irFileId" value="${newsItem.primaryPicture.id}"/>
                     </c:url>
                     <img class="basic_thumbnail"  src="${url}"/>
                 </c:if>
				   
				</td>
				<c:url var="pictureUrl" value="/downloadNewsItemsPicture.action">
					<c:param name="newsItemId" value="${newsItem.id}" />
					<c:param name="getPrimaryImage" value="true" />
				</c:url>
				<td class="${rowType}">${pictureUrl}</td>
				<td class="${rowType}">
				<button class="ur_button"
					onmouseover="this.className='ur_buttonover'"
					onmouseout="this.className='ur_button'"
					onclick="javascript:YAHOO.ur.edit.news.confirmPictureDelete(${newsItem.id}, ${newsItem.primaryPicture.id}, 'true');"
					id="showFolder">Delete</button>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
</c:if>


<h3>Pictures</h3>
<c:if test="${!ur:isEmpty(newsItem.pictures)}">
     <div class="dataTable">
              <urstb:table width="100%">
                  <urstb:thead>
                      <urstb:tr>
                          <urstb:td>Thumbnail</urstb:td>
                          <urstb:td>Picture URL</urstb:td>
                          <urstb:td>Action</urstb:td>
                      </urstb:tr>
                  </urstb:thead>
                  <urstb:tbody
                      var="irFile" 
                      oddRowClass="odd"
                      evenRowClass="even"
                      currentRowClassVar="rowClass"
                      collection="${newsItem.pictures}">
                      <urstb:tr 
                          cssClass="${rowClass}"
                          onMouseOver="this.className='highlight'"
                          onMouseOut="this.className='${rowClass}'">
                          <urstb:td>
                          <c:if test="${ir:hasThumbnail(irFile)}">
                              <c:url var="url" value="/newsThumbnailDownloader.action">
                                  <c:param name="newsItemId" value="${newsItem.id}"/>
                                  <c:param name="irFileId" value="${irFile.id}"/>
                              </c:url>
                              <img class="basic_thumbnail"  src="${url}"/>
                          </c:if>
                          </urstb:td>
                        
                          <urstb:td>
                              <c:url var="pictureUrl" value="/downloadNewsItemsPicture.action">
					              <c:param name="newsItemId" value="${newsItem.id}" />
					              <c:param name="irFileId" value="${irFile.id}" />
				              </c:url>
                              ${pictureUrl}
                          </urstb:td>

                          <urstb:td>
                          <button class="ur_button"
					              onmouseover="this.className='ur_buttonover';"
					              onmouseout="this.className='ur_button';"
					              onclick="javascript:YAHOO.ur.edit.news.confirmPictureDelete(${newsItem.id}, ${irFile.id}, false);"
					              id="showFolder">Delete</button>
                          </urstb:td>                        

                      </urstb:tr>
                  </urstb:tbody>
              </urstb:table>
              </div>

</c:if>