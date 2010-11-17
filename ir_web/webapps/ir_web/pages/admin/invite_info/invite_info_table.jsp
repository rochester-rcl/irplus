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

<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>

<c:if test="${totalHits > 0}">
	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
	<c:import url="invite_info_pager.jsp"/>
</c:if>

</br>
<div class="dataTable">
	<form method="post" id="inviteInfo">
	             
	    <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
					<urstb:td>Inviting User</urstb:td>
					<urstb:td>Invited User Email</urstb:td>
					<urstb:td>Created Date</urstb:td>
					<urstb:td>Invite Token</urstb:td>
	            </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="inviteInfo" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${inviteInfos}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                        ${inviteInfo.user.lastName}, ${inviteInfo.user.firstName}
	                        </urstb:td>
	                        <urstb:td>
	                             ${inviteInfo.email}
	                        </urstb:td>
	                        <urstb:td>
	                             ${inviteInfo.createdDate}
	                        </urstb:td>
	                        <urstb:td>
	                             ${inviteInfo.token}
	                        </urstb:td>
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>
		</form>
</div>	

<c:if test="${totalHits > 0}">
	<c:import url="invite_info_pager.jsp"/>
</c:if>


