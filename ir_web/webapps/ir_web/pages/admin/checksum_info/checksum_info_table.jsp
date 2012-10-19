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
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>

<fmt:setBundle basename="messages"/>

<c:import url="browse_all_checksums_pager.jsp"/>

<c:if test="${totalHits > 0}">
	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
</c:if>

<div class="dataTable">
<urstb:table width="100%">
	<urstb:thead>
		<urstb:tr>
			<urstb:td>Id</urstb:td>
			<urstb:td>File Id</urstb:td>
			<urstb:td>Upload Date Checked</urstb:td>
			<urstb:td>Original Checksum</urstb:td>
			<urstb:td>Check Passed</urstb:td>
			<urstb:td>Date Last Checked</urstb:td>
			<urstb:td>Date Last Passed</urstb:td>
		</urstb:tr>
	</urstb:thead>
	<urstb:tbody var="checksum" oddRowClass="odd"
		evenRowClass="even" currentRowClassVar="rowClass"
		collection="${checksums}">
		<urstb:tr cssClass="${rowClass}"
			onMouseOver="this.className='highlight'"
			onMouseOut="this.className='${rowClass}'">

			<urstb:td>
			    <c:url   var="checksumUrl" value="/admin/viewFileInfoChecksum.action">
			       <c:param name="checksumId" value="${checksum.id}"/>
			    </c:url>
			    <a href="${checksumUrl}">${checksum.id}</a>
			</urstb:td>
			<urstb:td>
			    ${checksum.fileInfo.id}
			</urstb:td>
			<urstb:td>
			    ${checksum.dateCalculated}
			</urstb:td>
			<urstb:td>
			    ${checksum.checksum}
			</urstb:td>
			<urstb:td>
			    ${checksum.reCalculatedPassed}
			</urstb:td>
			<urstb:td>
			    ${checksum.dateReCalculated}
			</urstb:td>
			<urstb:td>
			    ${checksum.dateLastCheckPassed}
			</urstb:td>
			
		</urstb:tr>
	</urstb:tbody>
</urstb:table>
</div>
<c:import url="browse_all_checksums_pager.jsp"/>

