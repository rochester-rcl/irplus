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
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="messages"/>


<c:if test="${totalHits > 0}">
	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
	<c:import url="institutional_collection_search_pager.jsp"/>
	<br/>
</c:if>

<div class="dataTable">
	             
	    <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
	                <urstb:td>Id</urstb:td>
	                <urstb:td>Name</urstb:td>
	                <urstb:td>Virtual Path</urstb:td>
	                <urstb:td>Edit</urstb:td>
	            </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="institutionalCollection" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${collections}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                        ${institutionalCollection.id}
	                        </urstb:td>
	                        <urstb:td>
			                    ${institutionalCollection.name}
	                        </urstb:td>
	                        <urstb:td>
		                   		${institutionalCollection.path}
	                        </urstb:td>
	                        <urstb:td>
		                        <ir:acl domainObject="${institutionalCollection}" hasPermission="ADMINISTRATION">
		                            <c:url var="getCollectionPropertiesUrl" value="/admin/viewInstitutionalCollection.action">
		                                <c:param name="collectionId" value="${institutionalCollection.id}"/>
		                            </c:url>
		                            <a href="${getCollectionPropertiesUrl}">Edit</a>
		                        </ir:acl>
	                        </urstb:td>
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>
</div>	

<c:if test="${totalHits > 0}">
		<c:import url="institutional_collection_search_pager.jsp"/>
</c:if>

