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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>


<h3>Primary Picture</h3>
<c:if test="${collection.primaryPicture != null }">
    <div class="dataTable">
	<table>
		<thead>
			<tr>
				<td>Thumbnail</td>
				<td>Delete</td>
			</tr>
		</thead>
		<tbody>
			<c:set value="even" var="rowType" />
			<tr>
				<td class="${rowType}">
				<c:if test="${ir:hasThumbnail(collection.primaryPicture)}">
				    <c:url var="url" value="/institutionalCollectionThumbnailDownloader.action">
                        <c:param name="collectionId" value="${collection.id}"/>
                        <c:param name="irFileId" value="${collection.primaryPicture.id}"/>
                     </c:url>
                     <img height="66px" width="100px" src="${url}"/>
                 </c:if>
				</td>
				<td class="${rowType}">
				<button class="ur_button"
					onmouseover="this.className='ur_buttonover';"
					onmouseout="this.className='ur_button';"
					onclick="javascript:YAHOO.ur.edit.institution.collection.confirmPictureDelete(${collection.id}, ${collection.primaryPicture.id}, true);"
					id="showPrimaryDeleteConfirm">Delete</button>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
</c:if>
<c:if test="${collection.primaryPicture == null }">
There is currently no primary picture.
</c:if>


<h3>Pictures</h3>
<c:if test="${!ur:isEmpty(collection.pictures)}">


    <div class="dataTable">
              <urstb:table>
                  <urstb:thead>
                      <urstb:tr>
                          <urstb:td>Thumbnail</urstb:td>
                          <urstb:td>Delete</urstb:td>
                      </urstb:tr>
                  </urstb:thead>
                  <urstb:tbody
                      var="irFile" 
                      oddRowClass="odd"
                      evenRowClass="even"
                      currentRowClassVar="rowClass"
                      collection="${collection.pictures}">
                      <urstb:tr 
                          cssClass="${rowClass}"
                          onMouseOver="this.className='highlight'"
                          onMouseOut="this.className='${rowClass}'">
                        
                          <urstb:td>
                               <c:if test="${ir:hasThumbnail(irFile)}">
				                   <c:url var="url" value="/institutionalCollectionThumbnailDownloader.action">
                                       <c:param name="collectionId" value="${collection.id}"/>
                                       <c:param name="irFileId" value="${irFile.id}"/>
                                   </c:url>
                                   <img height="66px" width="100px" src="${url}"/>
                               </c:if>
                          </urstb:td>

                          <urstb:td >
                             <button class="ur_button"
					              onmouseover="this.className='ur_buttonover';"
					              onmouseout="this.className='ur_button';"
					              onclick="javascript:YAHOO.ur.edit.institution.collection.confirmPictureDelete(${collection.id}, ${irFile.id}, false);"
					              id="deleteCollectionPictureConfirm">Delete</button>
                          </urstb:td>                        

                      </urstb:tr>
                  </urstb:tbody>
              </urstb:table>
          </div>


</c:if>
<c:if test="${ur:isEmpty(collection.pictures)}">
    There are currently no institutional pictures.
</c:if>