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
	<c:import url="browse_all_person_name_authority_identifier_types_pager.jsp"/>
</c:if>



<div class="dataTable">
	<ur:basicForm method="post" id="personNameAuthorityIdentifierTypes" name="myPersonNameAuthorityIdentifierTypes" >
	             
	    <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
					<urstb:td><ur:checkbox name="checkAllSetting" 
								value="off" onClick="YAHOO.ur.person.name.authority.identifier.type.setCheckboxes();"/></urstb:td>         
	                <urstb:td>Id</urstb:td>
             
	                <urstb:tdHeadSort  height="33"
	                    currentSortAction="${sortType}"
	                    ascendingSortAction="javascript:YAHOO.ur.person.name.authority.identifier.type.getPersonNameAuthorityIdentifierTypes(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'asc');"
	                    descendingSortAction="javascript:YAHOO.ur.person.name.authority.identifier.type.getPersonNameAuthorityIdentifierTypes(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'desc');">
	                    <u>Name</u>                                              
	                    <urstb:thImgSort
	                                 sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
	                                 sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/></urstb:tdHeadSort>
					<urstb:td>Description</urstb:td>
					<urstb:td>System Code</urstb:td>
	            </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="identifierType" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${personNameAuthorityIdentifierTypes}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                        <ur:checkbox name="personNameAuthorityIdentifierTypeIds" value="${identifierType.id}"/>
	                        </urstb:td>
	                        <urstb:td>
		                        ${identifierType.id}
	                        </urstb:td>
	                        <urstb:td>
			                   <a href="javascript:YAHOO.ur.person.name.authority.identifier.type.editIdentifierType(${identifierType.id});">${identifierType.name}</a>
	                        </urstb:td>
	                        <urstb:td>
	                             ${identifierType.description}
	                        </urstb:td>
	                        <urstb:td>
	                             ${identifierType.uniqueSystemCode}
	                        </urstb:td>
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>
		</ur:basicForm>
</div>	

<c:if test="${totalHits > 0}">
	<c:import url="browse_all_person_name_authority_identifier_types_pager.jsp"/>
</c:if>



