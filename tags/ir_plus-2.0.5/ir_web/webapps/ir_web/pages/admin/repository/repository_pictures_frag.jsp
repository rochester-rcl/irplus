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


<div class="dataTable">
	                       <urstb:table width="100%">
	                           <urstb:thead>
	                               <urstb:tr>
					                   <urstb:td>Thumbnail</urstb:td>         
	                                   <urstb:td>Description</urstb:td>
	                                   <urstb:td>Created Date</urstb:td>
	                                   <urstb:td>Created By</urstb:td>
	                                   <urstb:td>Delete</urstb:td>
	                               </urstb:tr>
	                           </urstb:thead>
	                           <urstb:tbody
	                                var="irFile" 
	                                oddRowClass="odd"
	                                evenRowClass="even"
	                                currentRowClassVar="rowClass"
	                                collection="${repository.pictures}">
	                                <urstb:tr  cssClass="${rowClass}"
	                                    onMouseOver="this.className='highlight'"
	                                    onMouseOut="this.className='${rowClass}'">
	                                    <urstb:td>
	                                       <c:if test="${ir:hasThumbnail(irFile)}">
                                               <c:url var="url" value="/repositoryThumbnailDownloader.action">
                                                   <c:param name="irFileId" value="${irFile.id}"/>
                                               </c:url>
                                               <img height="66px" width="100px" src="${url}"/>
                                           </c:if>
	                                    </urstb:td>
	                                    <urstb:td>${irFile.description}</urstb:td>
	                                    <urstb:td>${irFile.fileInfo.createdDate}</urstb:td>
	                                    <urstb:td>${irFile.owner.username}</urstb:td>
	                                    <urstb:td>
	                                        <button class="ur_button" 
 		                                    onmouseover="this.className='ur_buttonover';"
 		                                    onmouseout="this.className='ur_button';"
 		                                    onclick="javascript:YAHOO.ur.repository.confirmPictureDelete(${irFile.id});"
 		                                    id="showFolder">Delete Picture</button> 
	                                    </urstb:td>
	                               </urstb:tr>
	                           </urstb:tbody>
	                       </urstb:table>
                           </div>	