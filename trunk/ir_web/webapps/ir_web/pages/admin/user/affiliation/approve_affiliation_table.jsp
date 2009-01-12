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

<fmt:setBundle basename="messages"/>

<c:if test="${totalHits > 0}">
	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
	<c:import url="browse_all_approve_affiliation_pager.jsp"/>
</c:if>

<div class="dataTable">
	<ur:basicForm method="post" id="pendingApprovals" name="myPendingApprovals" >
	             
	    <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
					<urstb:td><ur:checkbox name="checkAllSetting" 
								value="off" onClick="YAHOO.ur.affiliation.approval.setCheckboxes();"/></urstb:td>         
	                <urstb:td>Id</urstb:td>
             
	                <urstb:tdHeadSort  height="33"
	                    currentSortAction="${sortType}"
	                    ascendingSortAction="javascript:YAHOO.ur.affiliation.approval.getPendingApprovals(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'asc');"
	                    descendingSortAction="javascript:YAHOO.ur.affiliation.approval.getPendingApprovals(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'desc');">
	                    <u>User Name</u>                                              
	                    <urstb:thImgSort
	                                 sortAscendingImage="page-resources/images/all-images/bullet_arrow_down.gif"
	                                 sortDescendingImage="page-resources/images/all-images/bullet_arrow_up.gif"/></urstb:tdHeadSort>
					<urstb:td>Affiliation</urstb:td>
	            </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="pendingUser" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${pendingUsers}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                        <ur:checkbox name="userIds" value="${pendingUser.id}"/>
	                        </urstb:td>
	                        <urstb:td>
		                        ${pendingUser.id}
	                        </urstb:td>
	                        <urstb:td>
			                   ${pendingUser.username} 
	                        </urstb:td>
	                         <urstb:td>
			                   ${pendingUser.firstName} 
	                        </urstb:td>
	                         <urstb:td>
			                   ${pendingUser.lastName} 
	                        </urstb:td>
	                        <urstb:td>
			                     <select id="affiliation_id" name="affiliationId" />
							      		<c:forEach items="${affiliations}" var="affiliation">
							      			<option value = "${affiliation.id}"
							      			<c:if test="${affiliation.id == pendingUser.affiliation.id}">
							      				selected
							      			</c:if>
							      			> ${affiliation.name}</option>
							      		</c:forEach>
						      	  </select>
	                        </urstb:td>
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>
		</ur:basicForm>
</div>	

<c:if test="${totalHits > 0}">
	<c:import url="browse_all_approve_affiliation_pager.jsp"/>
</c:if>
