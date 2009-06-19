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

<c:if test="${totalHits > 0}">
	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
	<c:import url="browse_all_persons_pager.jsp"/>
</c:if>

<div class="dataTable">
	<ur:basicForm method="post" id="people" name="myPersons" >
	    <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
					<urstb:td><ur:checkbox name="checkAllSetting" 
								value="off" onClick="YAHOO.ur.person.setCheckboxes();"/></urstb:td>         
	                <urstb:td>Id</urstb:td>
             
	                <c:set var="surnameSort" value="none"/>
	                <c:if test='${sortElement == "surname"}'>
	                    <c:set var="surnameSort" value="${sortType}"/>
	                </c:if>
	                
	                <urstb:tdHeadSort  height="33"
	                    currentSortAction="${surnameSort}"
	                    ascendingSortAction="javascript:YAHOO.ur.person.getPersons(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'surname', 'asc');"
	                    descendingSortAction="javascript:YAHOO.ur.person.getPersons(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'surname', 'desc');">
	                    <u>Last Name</u>                                              
	                    <urstb:thImgSort
	                                 sortAscendingImage="page-resources/images/all-images/bullet_arrow_down.gif"
	                                 sortDescendingImage="page-resources/images/all-images/bullet_arrow_up.gif"/></urstb:tdHeadSort>
	
	                <c:set var="forenameSort" value="none"/>
	                <c:if test='${sortElement == "forename"}'>
	                    <c:set var="forenameSort" value="${sortType}"/>
	                </c:if>
	                
	                <urstb:tdHeadSort  height="33"
	                    currentSortAction="${forenameSort}"
	                    ascendingSortAction="javascript:YAHOO.ur.person.getPersons(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'forename', 'asc');"
	                    descendingSortAction="javascript:YAHOO.ur.person.getPersons(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'forename', 'desc');">
	                    <u>First Name</u>                                              
	                    <urstb:thImgSort
	                                 sortAscendingImage="page-resources/images/all-images/bullet_arrow_down.gif"
	                                 sortDescendingImage="page-resources/images/all-images/bullet_arrow_up.gif"/></urstb:tdHeadSort>
	
	                <urstb:td>Middle Name</urstb:td>
	                <urstb:td>Family Name</urstb:td>
 	                <urstb:td>Initials</urstb:td>
 	                <urstb:td>Numeration</urstb:td>
	                <urstb:td>Birth Year</urstb:td>
	                <urstb:td>Death Year</urstb:td>
	                <urstb:td>Edit</urstb:td>
	                </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="personNameAuthority" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${personNameAuthorities}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                        <ur:checkbox name="personIds" value="${personNameAuthority.id}"/>
	                        </urstb:td>
	                        <urstb:td>
		                        ${personNameAuthority.id}
	                        </urstb:td>
	                        <urstb:td>
			                   <c:url var="editPersonUrl" value="/admin/personEdit.action">
		                           <c:param name="personId" value="${personNameAuthority.id}"/>
		                       </c:url>
			                   <a href="${editPersonUrl}">${personNameAuthority.authoritativeName.surname}</a>
	                        
	                        </urstb:td>
	                        <urstb:td>
		                   		${personNameAuthority.authoritativeName.forename}
	                        </urstb:td>
	                        <urstb:td>
	                             ${personNameAuthority.authoritativeName.middleName}
	                        </urstb:td>
	                        <urstb:td>
	                             ${personNameAuthority.authoritativeName.familyName}
	                        </urstb:td>	                        
	                        <urstb:td>
		                   		${personNameAuthority.authoritativeName.initials}
	                        </urstb:td>
	                        <urstb:td>
	                             ${personNameAuthority.authoritativeName.numeration}
	                        </urstb:td>
	                        <urstb:td>
								<c:if test="${personNameAuthority.birthDate.year != 0}">
	                  				 ${personNameAuthority.birthDate.year}
	                			</c:if>
	                        </urstb:td>	                        
	                        <urstb:td>
				                <c:if test="${personNameAuthority.deathDate.year != 0}">
				                   ${personNameAuthority.deathDate.year}
				                </c:if>
	                        </urstb:td>
	                        <urstb:td>
			                   <a href="javascript:YAHOO.ur.person.editPerson(${personNameAuthority.id},
			                   												  ${personNameAuthority.authoritativeName.id},
			                   												  '${personNameAuthority.authoritativeName.forename}',
			                   												  '${personNameAuthority.authoritativeName.surname}',
			                   												  '${personNameAuthority.authoritativeName.middleName}',
			                   												  '${personNameAuthority.authoritativeName.familyName}',
			                   												  '${personNameAuthority.authoritativeName.initials}',
			                   												  '${personNameAuthority.authoritativeName.numeration}',
			                   												  ${personNameAuthority.birthDate.year},
			                   												  ${personNameAuthority.deathDate.year});"> Edit </a>
	                    	</urstb:td>
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>	
		</ur:basicForm>
	</div>
<c:if test="${totalHits > 0}">
	<c:import url="browse_all_persons_pager.jsp"/>
</c:if>
