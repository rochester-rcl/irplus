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

<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>


<c:set var="linkSize" value="${ur:collectionSize(collection.links)}"/>
<div class="dataTable">
<urstb:table width="100%">
    <urstb:thead>
        <urstb:tr>
            <urstb:td>Move</urstb:td>
            <urstb:td>Name</urstb:td>
            <urstb:td>URL</urstb:td>
            <urstb:td>Description</urstb:td>
            <urstb:td>Order</urstb:td>
            <urstb:td>Remove</urstb:td>
        </urstb:tr>
    </urstb:thead>
    <urstb:tbody
           var="link" 
           oddRowClass="odd"
                evenRowClass="even"
                currentRowClassVar="rowClass"
                rowStatus="rowStatus"
                collection="${collection.links}">
                    <urstb:tr 
                        cssClass="${rowClass}"
                        onMouseOver="this.className='highlight'"
                        onMouseOut="this.className='${rowClass}'">
                        <urstb:td>
                        <table border="0px">
						<tr>
								<c:if test="${rowStatus == 0}">
								<td>
									<img class="tableImg" alt="" src="${pageContext.request.contextPath}/page-resources/images/all-images/move_arrow_up_mute.gif" />
								</td>
								</c:if>

								<c:if test="${rowStatus != 0}">
								<td>
									<a href="javascript:YAHOO.ur.edit.institution.collection.moveLinkUp('${link.id}', '${collection.id}');"><img class="tableImg" alt="Move Up" 
									    src="${pageContext.request.contextPath}/page-resources/images/all-images/move_arrow_up.gif" /></a>
								</td>
								</c:if>
						</tr>
						<tr >
							<td> &nbsp;</td>
						</tr>		
						<tr>
								<c:if test="${(rowStatus + 1) != linkSize}">
								<td>
									<a href="javascript:YAHOO.ur.edit.institution.collection.moveLinkDown('${link.id}', '${collection.id}');"><img class="tableImg" alt="Move Down" 
									    src="${pageContext.request.contextPath}/page-resources/images/all-images/move_arrow_down.gif" /></a>
									</td>
								</c:if>  
								<c:if test="${(rowStatus + 1) == linkSize}">
									<td>
										<img class="tableImg" alt="" src="${pageContext.request.contextPath}/page-resources/images/all-images/move_arrow_down_mute.gif" />
									</td>
								</c:if>  
						</tr>							
					</table>
                        </urstb:td>
                        <urstb:td>
                             <a href="javascript:YAHOO.ur.edit.institution.collection.editLink('${link.institutionalCollection.id}', '${link.id}');">${link.name}</a>
                        </urstb:td>
                        <urstb:td>
                            ${link.url}  
                        </urstb:td>
                        <urstb:td>
                            ${link.description}   
                        </urstb:td>
                        <urstb:td>
                            ${link.order}   
                        </urstb:td>
                        <urstb:td>
                             <span class="deleteBtnImg">&nbsp;</span> <a href="javascript:YAHOO.ur.edit.institution.collection.removeLink('${link.id}');"> Remove </a>
                        </urstb:td>
                        
                    </urstb:tr>
            </urstb:tbody>
        </urstb:table>

</div>

